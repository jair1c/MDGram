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

// MDGram: motor de traducción propio vía Google Translate (endpoint público, sin API key ni Premium).
// Portado en concepto del traductor de OwlGram/OctoGram (GPL, atribución en los créditos), pero
// reimplementado autocontenido para no arrastrar sus dependencias (OwlConfig, StandardHTTPRequest...).
// MVP: traduce solo el TEXTO de cada mensaje (sin preservar entidades bold/link — pendiente a futuro).
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

    private static String googleTranslate(String text, String toLang) throws Exception {
        String url = "https://translate.google.com/translate_a/single?dj=1&sl=auto&tl="
                + URLEncoder.encode(toLang == null ? "en" : toLang, "UTF-8")
                + "&ie=UTF-8&oe=UTF-8&client=at&dt=t&otf=2&q="
                + URLEncoder.encode(text, "UTF-8");

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        try {
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            JSONObject root = new JSONObject(response.toString());
            JSONArray sentences = root.optJSONArray("sentences");
            if (sentences == null) {
                return text; // sin cambios si el formato no es el esperado
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sentences.length(); i++) {
                JSONObject s = sentences.optJSONObject(i);
                if (s != null) {
                    sb.append(s.optString("trans", ""));
                }
            }
            String result = sb.toString();
            return result.length() > 0 ? result : text;
        } finally {
            conn.disconnect();
        }
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
