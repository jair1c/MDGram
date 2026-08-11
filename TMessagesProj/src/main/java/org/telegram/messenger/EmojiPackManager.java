package org.telegram.messenger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// MDGram: gestor de packs de emoji personalizados (feature "Fuente de emoji personalizada" de Conversación).
// Un pack = una carpeta filesDir/emoji_packs/{id}/ con un PNG por emoji ({page}_{page2}.png, mismo índice que
// assets). Los emojis que falten en el pack caen al set horneado (Apple) por el fallback en Emoji.loadEmoji.
// Manifiesto de packs disponibles: JSON en el repo (raw GitHub). Descarga del zip: GitHub Releases.
public class EmojiPackManager {

    public static final String MANIFEST_URL = "https://raw.githubusercontent.com/jair1c/MDGram/main/emoji_packs.json";
    private static final String MARKER = ".installed";

    public static class PackInfo {
        public String id;
        public String name;
        public String url;
        public int sizeMB;
        public int count;
    }

    public static File packsDir() {
        return new File(ApplicationLoader.applicationContext.getFilesDir(), "emoji_packs");
    }

    public static File packDir(String id) {
        return new File(packsDir(), id);
    }

    public static boolean isInstalled(String id) {
        return new File(packDir(id), MARKER).exists();
    }

    // ids de packs ya instalados (carpetas con el marcador .installed).
    public static ArrayList<String> listInstalled() {
        ArrayList<String> out = new ArrayList<>();
        File dir = packsDir();
        File[] subs = dir.listFiles();
        if (subs != null) {
            for (File f : subs) {
                if (f.isDirectory() && new File(f, MARKER).exists()) {
                    out.add(f.getName());
                }
            }
        }
        return out;
    }

    public static void deletePack(String id) {
        deleteRecursive(packDir(id));
        if (id.equals(MDGramConfig.emojiPack())) {
            MDGramConfig.setEmojiPack("default");
            Emoji.reloadEmoji();
        }
    }

    private static void deleteRecursive(File f) {
        if (f == null || !f.exists()) return;
        if (f.isDirectory()) {
            File[] c = f.listFiles();
            if (c != null) for (File x : c) deleteRecursive(x);
        }
        f.delete();
    }

    public interface ManifestCallback {
        void onResult(ArrayList<PackInfo> packs, String error);
    }

    public static void fetchManifest(ManifestCallback cb) {
        Utilities.globalQueue.postRunnable(() -> {
            ArrayList<PackInfo> packs = null;
            String error = null;
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(MANIFEST_URL).openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setRequestProperty("User-Agent", "MDGram-Emoji");
                int code = conn.getResponseCode();
                if (code == 200) {
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                        String line;
                        while ((line = r.readLine()) != null) sb.append(line);
                    }
                    JSONObject json = new JSONObject(sb.toString());
                    JSONArray arr = json.optJSONArray("packs");
                    packs = new ArrayList<>();
                    if (arr != null) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            PackInfo p = new PackInfo();
                            p.id = o.optString("id", "");
                            p.name = o.optString("name", p.id);
                            p.url = o.optString("url", "");
                            p.sizeMB = o.optInt("sizeMB", 0);
                            p.count = o.optInt("count", 0);
                            if (!p.id.isEmpty() && !p.url.isEmpty()) packs.add(p);
                        }
                    }
                } else {
                    error = "HTTP " + code;
                }
            } catch (Exception e) {
                error = e.getMessage() != null ? e.getMessage() : "error";
            } finally {
                if (conn != null) conn.disconnect();
            }
            final ArrayList<PackInfo> fp = packs;
            final String fe = error;
            AndroidUtilities.runOnUIThread(() -> cb.onResult(fp, fe));
        });
    }

    public interface DownloadListener {
        void onProgress(long downloaded, long total);
        void onComplete();
        void onError(String error);
    }

    public static volatile boolean cancel = false;

    // Descarga el zip del pack y lo descomprime a filesDir/emoji_packs/{id}/. Escribe el marcador .installed
    // al terminar OK. Sigue redirects a mano (GitHub 302). Concepto igual a MDGramUpdater.downloadApk.
    public static void downloadAndInstall(PackInfo pack, DownloadListener listener) {
        cancel = false;
        Utilities.globalQueue.postRunnable(() -> {
            HttpURLConnection conn = null;
            File dir = packDir(pack.id);
            File tmpZip = new File(packsDir(), pack.id + ".zip.tmp");
            try {
                packsDir().mkdirs();
                // limpiar una instalación previa/parcial
                deleteRecursive(dir);
                dir.mkdirs();

                String current = pack.url;
                int redirects = 0;
                while (true) {
                    conn = (HttpURLConnection) new URL(current).openConnection();
                    conn.setInstanceFollowRedirects(false);
                    conn.setConnectTimeout(20000);
                    conn.setReadTimeout(30000);
                    conn.setRequestProperty("User-Agent", "MDGram-Emoji");
                    int code = conn.getResponseCode();
                    if (code >= 300 && code < 400) {
                        String loc = conn.getHeaderField("Location");
                        conn.disconnect();
                        conn = null;
                        if (loc == null || ++redirects > 6) throw new Exception("too many redirects");
                        current = loc;
                        continue;
                    }
                    if (code != 200) throw new Exception("HTTP " + code);
                    break;
                }
                long total = conn.getContentLength();
                // 1) descargar el zip a un archivo temporal (con progreso)
                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(tmpZip);
                byte[] buf = new byte[65536];
                long downloaded = 0, lastReport = 0;
                int read;
                while ((read = in.read(buf)) != -1) {
                    if (cancel) {
                        out.close(); in.close(); tmpZip.delete(); deleteRecursive(dir);
                        return;
                    }
                    out.write(buf, 0, read);
                    downloaded += read;
                    long now = System.currentTimeMillis();
                    if (now - lastReport > 80) {
                        final long d = downloaded, t = total;
                        AndroidUtilities.runOnUIThread(() -> listener.onProgress(d, t));
                        lastReport = now;
                    }
                }
                out.flush(); out.close(); in.close();

                // 2) descomprimir
                try (ZipInputStream zis = new ZipInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(tmpZip)))) {
                    ZipEntry entry;
                    byte[] zbuf = new byte[65536];
                    while ((entry = zis.getNextEntry()) != null) {
                        if (cancel) { tmpZip.delete(); deleteRecursive(dir); return; }
                        String name = entry.getName();
                        // solo archivos planos {page}_{page2}.png; ignorar rutas raras (anti path traversal)
                        if (entry.isDirectory() || name.contains("/") || name.contains("\\") || name.contains("..")) {
                            continue;
                        }
                        File outFile = new File(dir, name);
                        try (FileOutputStream fo = new FileOutputStream(outFile)) {
                            int n;
                            while ((n = zis.read(zbuf)) != -1) fo.write(zbuf, 0, n);
                        }
                    }
                }
                tmpZip.delete();
                // marcador de instalación completa
                new FileOutputStream(new File(dir, MARKER)).close();

                AndroidUtilities.runOnUIThread(listener::onComplete);
            } catch (Exception e) {
                tmpZip.delete();
                deleteRecursive(dir);
                final String err = e.getMessage() != null ? e.getMessage() : "error";
                AndroidUtilities.runOnUIThread(() -> listener.onError(err));
            } finally {
                if (conn != null) conn.disconnect();
            }
        });
    }
}
