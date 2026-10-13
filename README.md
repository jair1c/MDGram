# MDGram

Cliente de Telegram no oficial para Android — revival del proyecto **MDGram** original, para uso personal.

## Descargas

Última versión en **[Releases](https://github.com/jair1c/MDGram/releases)**. Sitio oficial: **https://mdgram-web.vercel.app/**

- **V5.1 Universal ARM32/ARM64** — Android 7 o posterior. Elige esta descarga para V5.1; esta release no ofrece APK separados por arquitectura.
- Las descargas separadas Arm64 / Armv7a corresponden a versiones anteriores.

Instala el APK permitiendo "instalar apps de esta fuente".

## Aviso para MDGram V5.1

**V5.1 estable ya está publicada, sin etiqueta beta.** [Descargar APK firmado](https://github.com/jair1c/MDGram/releases/download/V5.1/MDGram-universal.apk) · [Notas de la versión](RELEASE_NOTES_V5.1.md).

V5.1 cambia el paquete de `com.mdgram.messenger.web` a `com.mdgram.messenger`. Android la instala como una aplicación distinta. V5 y anteriores pueden coexistir; instala V5.1, inicia sesión y verifica tus chats antes de eliminar la anterior. La sesión y los datos locales no se transfieren automáticamente; no se promete migración de chats secretos.

Se comprobaron navegación, traducción, grupos/canales, recepción de mensajes y notificaciones y estabilidad en Galaxy A36. No se realizaron llamadas ni pruebas Premium; consulta el alcance en [RELEASE_VERIFICATION_2026-10-13.md](RELEASE_VERIFICATION_2026-10-13.md).

## Licencia

**GPL v2 o posterior**, heredada de Telegram / NekoX. Ver [LICENSE](LICENSE).

## Créditos

MDGram deriva y toma código o funciones de estos proyectos de código abierto (todos GPL):

- **[Telegram](https://github.com/DrKLO/Telegram)** — base de la aplicación.
- **[NekoX](https://github.com/NekoX-Dev/NekoX)** — código base de este proyecto.
- **[Cherrygram](https://github.com/arsLan4k1390/Cherrygram)** — funciones de Conversación y General.
- **OwlGram** — base del traductor de mensajes.
- **OctoGram** — referencia de mods y traductor.
- **MDGram original** de Richar Correa — inspiración y diseño de referencia.

## Código y compilación de V5.1

El código correspondiente al APK está en [codex/release-v5.1](https://github.com/jair1c/MDGram/tree/2b19922018f829c8138d68d4070b0492cf4e055d), basado en Telegram 12.10.1 / Cherrygram. Usa esa revisión para compilar V5.1; el árbol de main conserva la base anterior.

Configura tu propio api_id y api_hash en Extra.kt (el código público contiene marcadores), tus archivos Firebase y tu keystore.properties local. No publiques credenciales ni claves de firma. Compila con JDK 21 y ejecuta:

```
./gradlew :TMessagesProj_AppStandalone:assembleAfatStandalone
```

La configuración del proyecto genera bytecode Java/Kotlin 17. El APK oficial es ARM32/ARM64, Android 7 o posterior.
