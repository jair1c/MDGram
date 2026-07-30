# MDGram

Cliente de Telegram no oficial para Android — revival del proyecto **MDGram** original, para uso personal.

## Descargas

Última versión en **[Releases](https://github.com/jair1c/MDGram/releases)**. Sitio oficial: **https://mdgram-web.vercel.app/**

- **Universal** — funciona en todos los Android (elígela si no sabes tu arquitectura).
- **Arm64 / Armv7a** — descargas más livianas según tu dispositivo.

Instala el APK permitiendo "instalar apps de esta fuente".

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

## Compilar

Este repositorio **no incluye** los archivos `google-services.json` (config de Firebase). Coloca el tuyo en cada módulo que lo requiera antes de compilar. Luego:

```
./gradlew :TMessagesProj_AppStandalone:assembleAfatStandalone -PabiSplits
```

Genera los APK firmados (universal + por ABI) en `TMessagesProj_AppStandalone/build/outputs/apk/afat/standalone/`.
