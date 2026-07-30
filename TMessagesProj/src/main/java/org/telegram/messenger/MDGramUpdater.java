package org.telegram.messenger;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// MDGram: motor de actualizaciones in-app (celda "Actualizar"). Consulta un manifiesto JSON remoto,
// compara contra la versión MDGram local y devuelve la info si hay una versión más nueva.
// El manifiesto se hospeda en la web del proyecto (Vercel); la descarga del APK se hospeda en
// GitHub Releases (ver "Plan de publicación" en SESSION_NOTES). Concepto OwlGram/OctoGram, propio.
public class MDGramUpdater {

    // Versión MDGram PROPIA — independiente del versionCode de Telegram (que se multiplica por ABI en
    // build.gradle: versionCode*10 + abiVersionCode). INCREMENTAR en cada release publicado; el
    // manifiesto remoto compara su "versionCode" contra esto. Al publicar, mover a un buildConfigField.
    public static final int MD_VERSION_CODE = 1;
    public static final String MD_VERSION_NAME = "V1";

    // Manifiesto remoto de versión (JSON). Formato esperado:
    // { "versionCode": 2, "versionName": "V2", "changelog": "…", "apkUrl": "https://…/app.apk" }
    public static final String MANIFEST_URL = "https://mdgram-web.vercel.app/api/latest";

    public static class UpdateInfo {
        public int versionCode;
        public String versionName;
        public String changelog;
        public String apkUrl;
        public boolean isNewer; // true si versionCode > MD_VERSION_CODE
    }

    public interface Callback {
        // info != null => el manifiesto se leyó OK (mirar info.isNewer para saber si hay actualización)
        // info == null && error != null => no se pudo comprobar
        void onResult(UpdateInfo info, String error);
    }

    public static void checkForUpdate(Callback cb) {
        Utilities.globalQueue.postRunnable(() -> {
            UpdateInfo result = null;
            String error = null;
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(MANIFEST_URL).openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setRequestProperty("User-Agent", "MDGram-Updater");
                conn.setRequestProperty("Accept", "application/json");
                int code = conn.getResponseCode();
                if (code == 200) {
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    JSONObject json = new JSONObject(sb.toString());
                    int remoteCode = json.optInt("versionCode", 0);
                    UpdateInfo info = new UpdateInfo();
                    info.versionCode = remoteCode;
                    info.versionName = json.optString("versionName", "");
                    info.changelog = json.optString("changelog", "");
                    info.apkUrl = json.optString("apkUrl", "");
                    info.isNewer = remoteCode > MD_VERSION_CODE;
                    result = info;
                } else {
                    error = "HTTP " + code;
                }
            } catch (Exception e) {
                error = e.getMessage() != null ? e.getMessage() : "error";
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            final UpdateInfo fResult = result;
            final String fError = error;
            AndroidUtilities.runOnUIThread(() -> cb.onResult(fResult, fError));
        });
    }
}
