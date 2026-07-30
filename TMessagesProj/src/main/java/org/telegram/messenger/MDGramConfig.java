package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;

// MDGram: config central de las opciones del hub "Ajustes de MDGram" (pantalla General y siguientes).
// Usa las mismas SharedPreferences "mdgramconfig" que MDGramTranslator, para no dispersar el estado.
// Los defaults imitan el estado por defecto del MDGram original (foto de perfil como fondo/oscurecida ON,
// ocultar número OFF).
public class MDGramConfig {

    // radio del difuminado de la foto de perfil (en dp), aplicado vía RenderEffect. Suave a propósito.
    public static final float PROFILE_BLUR_RADIUS_DP = 4f;

    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("mdgramconfig", Context.MODE_PRIVATE);
    }

    // ---- General → cabecera de perfil del cajón lateral (MDGramSideDrawer) ----

    public static boolean profilePhotoAsBackground() {
        return prefs().getBoolean("profile_photo_bg", true);
    }

    public static void setProfilePhotoAsBackground(boolean v) {
        prefs().edit().putBoolean("profile_photo_bg", v).apply();
    }

    public static boolean blurProfilePhoto() {
        return prefs().getBoolean("profile_photo_blur", true);
    }

    public static void setBlurProfilePhoto(boolean v) {
        prefs().edit().putBoolean("profile_photo_blur", v).apply();
    }

    public static boolean darkenProfilePhoto() {
        return prefs().getBoolean("profile_photo_darken", true);
    }

    public static void setDarkenProfilePhoto(boolean v) {
        prefs().edit().putBoolean("profile_photo_darken", v).apply();
    }

    public static boolean hidePhoneNumber() {
        return prefs().getBoolean("hide_phone", false);
    }

    public static void setHidePhoneNumber(boolean v) {
        prefs().edit().putBoolean("hide_phone", v).apply();
    }

    // ---- General → "Redondeo de números" (35,702 ↔ 35.7k). Enganchado en LocaleController.formatShortNumber.
    // Concepto portado de Cherrygram (CP_NoRounding1, GPL). Default: NO redondear (números completos), fiel al
    // MDGram original (su toggle viene OFF). Cacheado porque formatShortNumber se llama en rutas de dibujado.
    private static Boolean roundNumbersCache;

    public static boolean roundNumbers() {
        if (roundNumbersCache == null) {
            roundNumbersCache = prefs().getBoolean("round_numbers", false);
        }
        return roundNumbersCache;
    }

    public static void setRoundNumbers(boolean v) {
        roundNumbersCache = v;
        prefs().edit().putBoolean("round_numbers", v).apply();
    }

    // ---- Otros Mods → "Mostrar notificación persistente" (foreground service que mantiene la app viva).
    // Concepto portado de Cherrygram (CG_ResidentNotification, GPL). Default OFF.
    public static boolean residentNotification() {
        return prefs().getBoolean("resident_notification", false);
    }

    public static void setResidentNotification(boolean v) {
        prefs().edit().putBoolean("resident_notification", v).apply();
    }

    // ---- General → Acelerador de descarga/carga. Concepto portado de Cherrygram (EP_*SpeedBoost, GPL).
    // Enganchado en FileLoadOperation.updateParams (descarga) y FileUploadOperation (subida). Default OFF.
    public static final int BOOST_NONE = 0;      // No — 128KB / 4 requests (comportamiento stock)
    public static final int BOOST_AVERAGE = 1;   // Rápida — 512KB / 8 requests
    public static final int BOOST_EXTREME = 2;   // Ultra — 1MB / 12 requests

    private static Integer downloadBoostCache;

    public static int downloadSpeedBoost() {
        if (downloadBoostCache == null) {
            downloadBoostCache = prefs().getInt("download_speed_boost", BOOST_NONE);
        }
        return downloadBoostCache;
    }

    public static void setDownloadSpeedBoost(int v) {
        downloadBoostCache = v;
        prefs().edit().putInt("download_speed_boost", v).apply();
    }

    public static boolean uploadSpeedBoost() {
        return prefs().getBoolean("upload_speed_boost", false);
    }

    public static void setUploadSpeedBoost(boolean v) {
        prefs().edit().putBoolean("upload_speed_boost", v).apply();
    }

    public static boolean slowNetworkMode() {
        return prefs().getBoolean("slow_network_mode", false);
    }

    public static void setSlowNetworkMode(boolean v) {
        prefs().edit().putBoolean("slow_network_mode", v).apply();
    }

    // ---- Conversación → "Deshabilitar sensor de proximidad" (no acercar la pantalla al oído en audios).
    // Enganchado en MediaController (registro del proximitySensor). Default OFF.
    public static boolean disableProximitySensor() {
        return prefs().getBoolean("disable_proximity", false);
    }

    public static void setDisableProximitySensor(boolean v) {
        prefs().edit().putBoolean("disable_proximity", v).apply();
    }

    // ---- Conversación → "Ocultar enviar como canal" (oculta el botón de identidad en el compositor).
    // Concepto de Cherrygram (CP_HideSendAsChannel). Enganchado en ChatActivityEnterView.updateSendAsButton.
    public static boolean hideSendAsChannel() {
        return prefs().getBoolean("hide_send_as", false);
    }

    public static void setHideSendAsChannel(boolean v) {
        prefs().edit().putBoolean("hide_send_as", v).apply();
    }

    // ---- Conversación → "Ir al siguiente canal" (jalar abajo para ir al siguiente canal no leído).
    // Default ON (como el original). OFF = deshabilitar. Enganchado en ChatActivity.setNextChannels.
    public static boolean goToNextChannel() {
        return prefs().getBoolean("go_to_next_channel", true);
    }

    public static void setGoToNextChannel(boolean v) {
        prefs().edit().putBoolean("go_to_next_channel", v).apply();
    }

    // ---- Conversación → "Enviar fotos en alta calidad" (tope 1280→2560px al enviar).
    // Concepto de Cherrygram (CP_LargePhotos). Hook en AndroidUtilities.getPhotoSize(). Default OFF.
    public static boolean largePhotos() {
        return prefs().getBoolean("large_photos", false);
    }

    public static void setLargePhotos(boolean v) {
        prefs().edit().putBoolean("large_photos", v).apply();
    }

    // ---- Conversación → "Efecto de desenfoque" (blur del fondo al abrir el menú de un mensaje).
    // Implementación propia con RenderEffect (Android 12+), no el archivo de Cherrygram. Default OFF.
    public static boolean blurMenu() {
        return prefs().getBoolean("blur_menu", false);
    }

    public static void setBlurMenu(boolean v) {
        prefs().edit().putBoolean("blur_menu", v).apply();
    }

    // ---- Conversación → "Intensidad del desenfoque" (slider 0..100). Mapea a un radio de blur en px:
    // radio = dp(4 + intensidad/100 * 32) → rango dp(4)..dp(36). Default 50 = dp(20) (el radio fijo previo).
    // Cacheado porque se lee al abrir el menú de cada mensaje.
    private static Integer blurMenuIntensityCache;

    public static int blurMenuIntensity() {
        if (blurMenuIntensityCache == null) {
            blurMenuIntensityCache = prefs().getInt("blur_menu_intensity", 50);
        }
        return blurMenuIntensityCache;
    }

    public static float blurMenuRadiusPx() {
        return AndroidUtilities.dp(4 + blurMenuIntensity() / 100f * 32f);
    }

    public static void setBlurMenuIntensity(int v) {
        if (v < 0) v = 0;
        if (v > 100) v = 100;
        blurMenuIntensityCache = v;
        prefs().edit().putInt("blur_menu_intensity", v).apply();
    }

    // ---- Conversación → "Ocultar texto de referencia" (oculta el texto de pista del campo de mensaje).
    // Concepto de Cherrygram. Hook en ChatActivityEnterView.updateFieldHint. Default OFF.
    public static boolean hideChatHint() {
        return prefs().getBoolean("hide_chat_hint", false);
    }

    public static void setHideChatHint(boolean v) {
        prefs().edit().putBoolean("hide_chat_hint", v).apply();
    }

    // ---- Conversación → "Mejoras de voz" (supresión de ruido + normalización en notas de voz).
    // Adjunta NoiseSuppressor + AutomaticGainControl al AudioRecord de grabación. Default OFF.
    public static boolean voiceEnhancement() {
        return prefs().getBoolean("voice_enhancement", false);
    }

    public static void setVoiceEnhancement(boolean v) {
        prefs().edit().putBoolean("voice_enhancement", v).apply();
    }

    // ---- Conversación → "Tamaño de sticker" (nivel: 0=pequeño, 1=normal, 2=grande). Multiplica el tamaño
    // de display de stickers/emoji animado en ChatMessageCell. Cacheado (se lee en el layout de cada celda).
    private static Float stickerMultCache;

    private static float multForLevel(int lvl) {
        return lvl == 0 ? 0.72f : lvl == 2 ? 1.32f : 1.0f;
    }

    public static int stickerSizeLevel() {
        return prefs().getInt("sticker_size", 1);
    }

    public static float stickerSizeMultiplier() {
        if (stickerMultCache == null) {
            stickerMultCache = multForLevel(prefs().getInt("sticker_size", 1));
        }
        return stickerMultCache;
    }

    public static void setStickerSizeLevel(int v) {
        stickerMultCache = multForLevel(v);
        prefs().edit().putInt("sticker_size", v).apply();
    }

    // ---- Conversación → Estilos → "Estilos de barra superior" (ActionBar del chat).
    // 0 = iOS (título centrado + avatar a la derecha, sin íconos búsqueda/menú — el header de la Etapa A).
    // 1 = Material (avatar izq + título izq + menú 3 puntos, el header estándar de Telegram nuevo).
    // Default 0 (iOS) = fiel al original (allí "Stilo iOS" viene seleccionado). Se lee al abrir el chat.
    public static final int ACTIONBAR_IOS = 0;
    public static final int ACTIONBAR_MATERIAL = 1;

    public static int actionBarStyle() {
        return prefs().getInt("actionbar_style", ACTIONBAR_IOS);
    }

    public static boolean actionBarStyleIOS() {
        return actionBarStyle() == ACTIONBAR_IOS;
    }

    public static void setActionBarStyle(int v) {
        prefs().edit().putInt("actionbar_style", v).apply();
    }
}
