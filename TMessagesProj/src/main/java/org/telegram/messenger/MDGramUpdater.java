package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// MDGram: motor de actualizaciones in-app (celda "Actualizar"). Consulta un manifiesto JSON remoto,
// compara contra la versión MDGram local y devuelve la info si hay una versión más nueva.
// El manifiesto se hospeda en la web del proyecto (Vercel); la descarga del APK se hospeda en
// GitHub Releases (ver "Plan de publicación" en SESSION_NOTES). Concepto OwlGram/OctoGram, propio.
public class MDGramUpdater {

    // Versión MDGram PROPIA — independiente del versionCode de Telegram (que se multiplica por ABI en
    // build.gradle: versionCode*10 + abiVersionCode). El manifiesto remoto compara su "versionCode"
    // contra esto. FUENTE ÚNICA: gradle.properties (MDGRAM_VERSION_CODE / MDGRAM_VERSION_NAME), expuesta
    // vía BuildConfig — el CI la sobreescribe con -PMDGRAM_VERSION_CODE=N. Subir SOLO ahí en cada release.
    public static final int MD_VERSION_CODE = BuildConfig.MDGRAM_VERSION_CODE;
    public static final String MD_VERSION_NAME = BuildConfig.MDGRAM_VERSION_NAME;

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

    // ---- Descarga in-app del APK + instalación (para no abrir el navegador ni mostrar GitHub) ----

    public interface DownloadListener {
        void onProgress(long downloaded, long total); // bytes; total=-1 si desconocido
        void onComplete(File apk);
        void onError(String error);
    }

    public static volatile boolean cancelDownload = false;

    // Descarga apkUrl a filesDir/cache/mdgram_update.apk (servible por el FileProvider ya existente).
    // Sigue redirects a mano (Vercel /dl 308 → GitHub 302 → S3) porque HttpURLConnection no sigue 307/308 solo.
    public static void downloadApk(String apkUrl, DownloadListener listener) {
        cancelDownload = false;
        Utilities.globalQueue.postRunnable(() -> {
            HttpURLConnection conn = null;
            File cacheDir = new File(ApplicationLoader.applicationContext.getFilesDir(), "cache");
            cacheDir.mkdirs();
            File outFile = new File(cacheDir, "mdgram_update.apk");
            try {
                String current = apkUrl;
                int redirects = 0;
                while (true) {
                    conn = (HttpURLConnection) new URL(current).openConnection();
                    conn.setInstanceFollowRedirects(false);
                    conn.setConnectTimeout(20000);
                    conn.setReadTimeout(30000);
                    conn.setRequestProperty("User-Agent", "MDGram-Updater");
                    int code = conn.getResponseCode();
                    if (code >= 300 && code < 400) {
                        String loc = conn.getHeaderField("Location");
                        conn.disconnect();
                        conn = null;
                        if (loc == null || ++redirects > 6) {
                            throw new Exception("too many redirects");
                        }
                        current = loc;
                        continue;
                    }
                    if (code != 200) {
                        throw new Exception("HTTP " + code);
                    }
                    break;
                }
                long total = conn.getContentLength();
                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[32768];
                long downloaded = 0;
                long lastReport = 0;
                int read;
                while ((read = in.read(buf)) != -1) {
                    if (cancelDownload) {
                        out.close();
                        in.close();
                        outFile.delete();
                        return; // cancelado en silencio
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
                out.flush();
                out.close();
                in.close();
                final long d = downloaded, t = total;
                final File f = outFile;
                AndroidUtilities.runOnUIThread(() -> {
                    listener.onProgress(d, t);
                    listener.onComplete(f);
                });
            } catch (Exception e) {
                final String err = e.getMessage() != null ? e.getMessage() : "error";
                AndroidUtilities.runOnUIThread(() -> listener.onError(err));
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }

    // ¿Puede la app instalar APKs? (Android 8+ exige el permiso "instalar de esta fuente" por app).
    public static boolean canInstall(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    // Manda al usuario a activar "instalar apps de esta fuente" para MDGram.
    public static void requestInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Intent i = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:" + context.getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception ignored) {
            }
        }
    }

    // Lanza el diálogo de instalación nativo de Android con el APK descargado (vía FileProvider).
    public static void installApk(Context context, File apk) {
        try {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apk);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ignored) {
        }
    }
}
