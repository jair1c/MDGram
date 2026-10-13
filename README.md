# MDGram V5.1

Cliente no oficial de Telegram para Android, basado en Telegram 12.10.1 y Cherrygram. [Release estable V5.1](https://github.com/jair1c/MDGram/releases/tag/V5.1), APK universal ARM32/ARM64 para Android 7 o posterior. La versión cambia el paquete a com.mdgram.messenger y requiere iniciar sesión en la nueva aplicación.

La descripción, el estado técnico, las instrucciones de compilación y los pendientes específicos de MDGram están en [PROJECT_STATUS.md](PROJECT_STATUS.md).

## Aviso para MDGram V5.1

V5.1 cambia el paquete de `com.mdgram.messenger.web` a `com.mdgram.messenger`; Android lo instalará como una aplicación distinta, sin migrar automáticamente la sesión ni los datos locales. Las notas de la versión están en [RELEASE_NOTES_V5.1.md](RELEASE_NOTES_V5.1.md).

[Telegram](https://telegram.org) is a messaging app with a focus on speed and security. It’s superfast, simple and free.
This repo contains the official source code for [Telegram App for Android](https://play.google.com/store/apps/details?id=org.telegram.messenger).

## Creating your Telegram Application

We welcome all developers to use our API and source code to create applications on our platform.
There are several things we require from **all developers** for the moment.

1. [**Obtain your own api_id**](https://core.telegram.org/api/obtaining_api_id) for your application.
2. Please **do not** use the name Telegram for your app — or make sure your users understand that it is unofficial.
3. Kindly **do not** use our standard logo (white paper plane in a blue circle) as your app's logo.
3. Please study our [**security guidelines**](https://core.telegram.org/mtproto/security_guidelines) and take good care of your users' data and privacy.
4. Please remember to publish **your** code too in order to comply with the licences.

### API, Protocol documentation

Telegram API manuals: https://core.telegram.org/api

MTproto protocol manuals: https://core.telegram.org/mtproto

### Compilation Guide

**Note**: In order to support [reproducible builds](https://core.telegram.org/reproducible-builds), this repo contains dummy release.keystore,  google-services.json and filled variables inside BuildVars.java. Before publishing your own APKs please make sure to replace all these files with your own.

You will require Android Studio 2025.1.4, Android NDK 27.2.12479018 and Android SDK 36.

1. Clone the Telegram source code with its submodules:
   ```bash
   git clone --recursive --shallow-submodules https://github.com/DrKLO/Telegram.git Telegram
   ```
   In case you forgot the `--recursive` flag, change to the `Telegram` directory and run:
   ```bash
   git submodule init && git submodule update --init --recursive --depth=1
   ```
2. Copy your release.keystore into TMessagesProj/config
3. Fill out RELEASE_KEY_PASSWORD, RELEASE_KEY_ALIAS, RELEASE_STORE_PASSWORD in gradle.properties to access your  release.keystore
4.  Go to https://console.firebase.google.com/, create two android apps with application IDs org.telegram.messenger and org.telegram.messenger.beta, turn on firebase messaging and download google-services.json, which should be copied to the same folder as TMessagesProj.
5. Open the project in the Studio (note that it should be opened, NOT imported).
6. Fill out values in TMessagesProj/src/main/java/org/telegram/messenger/BuildVars.java – there’s a link for each of the variables showing where and which data to obtain.
7. You are ready to compile Telegram.

### Localization

We moved all translations to https://translations.telegram.org/en/android/. Please use it.

### Configuración privada de la compilación MDGram

El código público usa marcadores APP_ID=0 y APP_HASH vacío en Extra.kt. Obtén tu propio api_id/api_hash en my.telegram.org y configura esos campos localmente antes de compilar. El APK oficial firmado contiene la configuración de MDGram. No publiques credenciales ni keystore.properties; la firma de release requiere tu propio almacén de claves.
