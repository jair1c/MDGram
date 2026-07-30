package org.telegram.messenger;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import org.telegram.ui.LaunchActivity;

// MDGram: "Otros Mods → Mostrar notificación persistente". Foreground service que muestra una notificación
// ongoing (mantiene el proceso vivo). Concepto portado de Cherrygram (CG_ResidentNotification, GPL),
// reimplementado contra nuestra base. El manifest lo registra con foregroundServiceType="dataSync".
public class MDGramResidentService extends Service {

    private static final int NOTIFICATION_ID = 38452;

    // arranca o detiene el servicio según el flag MDGramConfig.residentNotification()
    public static void update() {
        Context ctx = ApplicationLoader.applicationContext;
        if (ctx == null) {
            return;
        }
        Intent intent = new Intent(ctx, MDGramResidentService.class);
        try {
            if (MDGramConfig.residentNotification()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.startForegroundService(intent);
                } else {
                    ctx.startService(intent);
                }
            } else {
                ctx.stopService(intent);
            }
        } catch (Throwable e) {
            // ForegroundServiceStartNotAllowedException si se intenta desde background (Android 12+): se
            // ignora; se reintenta la próxima vez que la app pase a primer plano.
            FileLog.e(e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            NotificationsController.checkOtherNotificationsChannel();
            Intent open = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            int piFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                piFlags |= PendingIntent.FLAG_IMMUTABLE;
            }
            PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, open, piFlags);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext)
                    .setSmallIcon(R.drawable.notification)
                    .setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL)
                    .setContentTitle("MDGram")
                    .setContentText("MDGram se está ejecutando")
                    .setOngoing(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setContentIntent(contentIntent);
            startForeground(NOTIFICATION_ID, builder.build());
        } catch (Throwable e) {
            FileLog.e(e);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            stopForeground(true);
        } catch (Throwable ignore) {
        }
    }
}
