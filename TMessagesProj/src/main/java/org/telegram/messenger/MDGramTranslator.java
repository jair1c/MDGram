package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Locale;

import org.telegram.tgnet.TLRPC;

// MDGram: motor de traducción propio vía Google Translate / Yandex (sin API key ni Premium).
// Portado en concepto del traductor de OwlGram/OctoGram (GPL, atribución en los créditos).
// Ahora con preservación de formato (negritas, cursivas, enlaces, código, tachado, spoilers).
public class MDGramTranslator {

    // proveedor activo, persistido. true = Google (motor propio); false = Telegram nativo.
    // Se elige en la pantalla "Traducir" del hub. Default: Google.
    public static final int PROVIDER_GOOGLE = 0;
    public static final int PROVIDER_TELEGRAM = 1;
    public static final int PROVIDER_YANDEX = 2;

    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("mdgramconfig", Context.MODE_PRIVATE);
    }

    public static int getProvider() {
        return prefs().getInt("translator_provider", PROVIDER_GOOGLE);
    }

    public static void setProvider(int provider) {
        prefs().edit().putInt("translator_provider", provider).apply();
    }

    // ¿usar nuestro motor (Google/Yandex)? Si es Telegram, mdSendTranslate delega a la API nativa.
    public static boolean enabled() {
        return getProvider() != PROVIDER_TELEGRAM;
    }

    public interface Result {
        void done(ArrayList<String> translated);
        void error();
    }

    public interface ResultEntities {
        void done(ArrayList<TLRPC.TL_textWithEntities> translated);
        void error();
    }

    // Traduce con preservación completa de entidades (negritas, links, etc.)
    public static void translateWithEntities(ArrayList<TLRPC.TL_textWithEntities> items, String toLang, ResultEntities cb) {
        Utilities.globalQueue.postRunnable(() -> {
            try {
                int provider = getProvider();
                ArrayList<TLRPC.TL_textWithEntities> out = new ArrayList<>();
                for (TLRPC.TL_textWithEntities item : items) {
                    if (item == null || item.text == null || item.text.length() == 0) {
                        TLRPC.TL_textWithEntities empty = new TLRPC.TL_textWithEntities();
                        empty.text = item != null && item.text != null ? item.text : "";
                        empty.entities = new ArrayList<>();
                        out.add(empty);
                        continue;
                    }
                    String html = textToHtml(item);
                    String translatedHtml;
                    if (provider == PROVIDER_YANDEX) {
                        translatedHtml = yandexTranslate(html, toLang);
                    } else {
                        translatedHtml = googleTranslate(html, toLang);
                    }
                    out.add(htmlToText(translatedHtml));
                }
                cb.done(out);
            } catch (Exception e) {
                FileLog.e(e);
                cb.error();
            }
        });
    }

    // traduce una lista de textos al idioma destino, en orden. Todo en un hilo de fondo.
    public static void translate(ArrayList<String> texts, String toLang, Result cb) {
        Utilities.globalQueue.postRunnable(() -> {
            try {
                int provider = getProvider();
                ArrayList<String> out = new ArrayList<>();
                for (String t : texts) {
                    if (t == null || t.length() == 0) {
                        out.add(t == null ? "" : t);
                    } else if (provider == PROVIDER_YANDEX) {
                        out.add(yandexTranslate(t, toLang));
                    } else {
                        out.add(googleTranslate(t, toLang));
                    }
                }
                cb.done(out);
            } catch (Exception e) {
                FileLog.e(e);
                cb.error();
            }
        });
    }

    public static String textToHtml(TLRPC.TL_textWithEntities twe) {
        if (twe == null || twe.text == null) return "";
        if (twe.entities == null || twe.entities.isEmpty()) return twe.text;

        ArrayList<TLRPC.MessageEntity> sorted = new ArrayList<>(twe.entities);
        Collections.sort(sorted, (a, b) -> Integer.compare(b.offset, a.offset));

        StringBuilder sb = new StringBuilder(twe.text);
        for (TLRPC.MessageEntity e : sorted) {
            if (e.offset < 0 || e.offset + e.length > sb.length()) continue;
            int start = e.offset;
            int end = e.offset + e.length;
            String openTag = null;
            String closeTag = null;
            if (e instanceof TLRPC.TL_messageEntityBold) {
                openTag = "<b>"; closeTag = "</b>";
            } else if (e instanceof TLRPC.TL_messageEntityItalic) {
                openTag = "<i>"; closeTag = "</i>";
            } else if (e instanceof TLRPC.TL_messageEntityUnderline) {
                openTag = "<u>"; closeTag = "</u>";
            } else if (e instanceof TLRPC.TL_messageEntityStrike) {
                openTag = "<s>"; closeTag = "</s>";
            } else if (e instanceof TLRPC.TL_messageEntityCode) {
                openTag = "<code>"; closeTag = "</code>";
            } else if (e instanceof TLRPC.TL_messageEntityPre) {
                openTag = "<pre>"; closeTag = "</pre>";
            } else if (e instanceof TLRPC.TL_messageEntitySpoiler) {
                openTag = "<span class=\"tg-spoiler\">"; closeTag = "</span>";
            } else if (e instanceof TLRPC.TL_messageEntityTextUrl) {
                openTag = "<a href=\"" + ((TLRPC.TL_messageEntityTextUrl) e).url + "\">"; closeTag = "</a>";
            } else if (e instanceof TLRPC.TL_messageEntityUrl) {
                String u = sb.substring(start, end);
                openTag = "<a href=\"" + u + "\">"; closeTag = "</a>";
            }
            if (openTag != null && closeTag != null) {
                sb.insert(end, closeTag);
                sb.insert(start, openTag);
            }
        }
        return sb.toString();
    }

    public static TLRPC.TL_textWithEntities htmlToText(String html) {
        TLRPC.TL_textWithEntities result = new TLRPC.TL_textWithEntities();
        result.entities = new ArrayList<>();
        if (html == null || html.isEmpty()) {
            result.text = "";
            return result;
        }

        StringBuilder cleanText = new StringBuilder();
        class Tag {
            String name;
            String url;
            int startOffset;
        }
        ArrayList<Tag> tagStack = new ArrayList<>();

        int i = 0;
        int len = html.length();
        while (i < len) {
            if (html.charAt(i) == '<') {
                int closeIndex = html.indexOf('>', i);
                if (closeIndex != -1) {
                    String tagContent = html.substring(i + 1, closeIndex).trim();
                    boolean isClosing = tagContent.startsWith("/");
                    String tagName = isClosing ? tagContent.substring(1).trim().toLowerCase(Locale.US) : tagContent.split("\\s+")[0].toLowerCase(Locale.US);

                    if (isClosing) {
                        for (int k = tagStack.size() - 1; k >= 0; k--) {
                            Tag t = tagStack.get(k);
                            if (t.name.equals(tagName)) {
                                tagStack.remove(k);
                                int start = t.startOffset;
                                int length = cleanText.length() - start;
                                if (length > 0) {
                                    TLRPC.MessageEntity entity = null;
                                    if ("b".equals(tagName) || "strong".equals(tagName)) {
                                        entity = new TLRPC.TL_messageEntityBold();
                                    } else if ("i".equals(tagName) || "em".equals(tagName)) {
                                        entity = new TLRPC.TL_messageEntityItalic();
                                    } else if ("u".equals(tagName)) {
                                        entity = new TLRPC.TL_messageEntityUnderline();
                                    } else if ("s".equals(tagName) || "strike".equals(tagName) || "del".equals(tagName)) {
                                        entity = new TLRPC.TL_messageEntityStrike();
                                    } else if ("code".equals(tagName)) {
                                        entity = new TLRPC.TL_messageEntityCode();
                                    } else if ("pre".equals(tagName)) {
                                        entity = new TLRPC.TL_messageEntityPre();
                                    } else if ("span".equals(tagName) && "tg-spoiler".equals(t.url)) {
                                        entity = new TLRPC.TL_messageEntitySpoiler();
                                    } else if ("a".equals(tagName) && t.url != null) {
                                        TLRPC.TL_messageEntityTextUrl tu = new TLRPC.TL_messageEntityTextUrl();
                                        tu.url = t.url;
                                        entity = tu;
                                    }
                                    if (entity != null) {
                                        entity.offset = start;
                                        entity.length = length;
                                        result.entities.add(entity);
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        Tag t = new Tag();
                        t.name = tagName;
                        t.startOffset = cleanText.length();
                        if ("a".equals(tagName)) {
                            int hrefIdx = tagContent.toLowerCase(Locale.US).indexOf("href=");
                            if (hrefIdx != -1) {
                                int qStart = tagContent.indexOf('"', hrefIdx);
                                if (qStart == -1) qStart = tagContent.indexOf('\'', hrefIdx);
                                if (qStart != -1) {
                                    int qEnd = tagContent.indexOf(tagContent.charAt(qStart), qStart + 1);
                                    if (qEnd != -1) {
                                        t.url = tagContent.substring(qStart + 1, qEnd);
                                    }
                                }
                            }
                        } else if ("span".equals(tagName)) {
                            if (tagContent.contains("tg-spoiler") || tagContent.contains("spoiler")) {
                                t.url = "tg-spoiler";
                            }
                        }
                        tagStack.add(t);
                    }
                    i = closeIndex + 1;
                    continue;
                }
            } else if (html.charAt(i) == '&') {
                int semi = html.indexOf(';', i);
                if (semi != -1 && semi - i <= 10) {
                    String ent = html.substring(i + 1, semi);
                    if ("amp".equals(ent)) cleanText.append('&');
                    else if ("lt".equals(ent)) cleanText.append('<');
                    else if ("gt".equals(ent)) cleanText.append('>');
                    else if ("quot".equals(ent)) cleanText.append('"');
                    else if ("apos".equals(ent) || "#39".equals(ent)) cleanText.append('\'');
                    else if ("nbsp".equals(ent)) cleanText.append(' ');
                    else {
                        cleanText.append(html, i, semi + 1);
                    }
                    i = semi + 1;
                    continue;
                }
            }
            cleanText.append(html.charAt(i));
            i++;
        }

        result.text = cleanText.toString();
        return result;
    }

    private static String googleTranslate(String text, String toLang) throws Exception {
        String tl = toLang != null ? toLang.split("_")[0] : "en";
        if ("nb".equals(tl)) tl = "no";
        String[] clients = new String[]{"dict-chrome-ex", "tw-ob"};
        Exception lastException = null;

        for (String client : clients) {
            String url = "https://translate.googleapis.com/translate_a/single?client=" + client + "&sl=auto&tl="
                    + URLEncoder.encode(tl, "UTF-8")
                    + "&dt=t&q="
                    + URLEncoder.encode(text, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

            try {
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
                JSONArray root = new JSONArray(response.toString());
                JSONArray sentences = root.optJSONArray(0);
                if (sentences == null) {
                    return text;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sentences.length(); i++) {
                    JSONArray s = sentences.optJSONArray(i);
                    if (s != null && s.length() > 0) {
                        sb.append(s.optString(0, ""));
                    }
                }
                String result = sb.toString();
                if (result.length() > 0) {
                    return result;
                }
            } catch (Exception e) {
                lastException = e;
            } finally {
                conn.disconnect();
            }
        }

        if (lastException != null) {
            throw lastException;
        }
        return text;
    }

    private static String yandexTranslate(String text, String toLang) throws Exception {
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        String url = "https://translate.yandex.net/api/v1/tr.json/translate?id=" + uuid + "-0-0&srv=android";
        String body = "lang=" + URLEncoder.encode(toLang == null ? "en" : toLang, "UTF-8") + "&text=" + URLEncoder.encode(text, "UTF-8");
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent", "ru.yandex.translate/3.20.2024");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try {
            conn.getOutputStream().write(body.getBytes("UTF-8"));
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            JSONObject root = new JSONObject(response.toString());
            JSONArray arr = root.optJSONArray("text");
            if (arr == null) {
                return text;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.length(); i++) {
                sb.append(arr.optString(i, ""));
            }
            String result = sb.toString();
            return result.length() > 0 ? result : text;
        } finally {
            conn.disconnect();
        }
    }
}
