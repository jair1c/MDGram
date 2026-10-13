# MDGram: estado técnico del proyecto

Última actualización: 7 de octubre de 2026.
### Explorar temas — 7 de octubre de 2026

- Se capturó la referencia V21 y se ajustó el navegador actual a fondo blanco, cabecera alineada a la izquierda, controles azules y secciones sin tarjetas externas.
- La comparación sigue abierta: las vistas previas actuales muestran fondos blancos y burbujas distintas; falta resolver la carga/dibujo de fondos, patrones y otros detalles. No se cambió el tema activo.
- APK firmado compilado e instalado en MEmu con sesión conservada: .test-artifacts/build-ui-themes-background.log. Cabecera/marco verificados; fondos blancos aún presentes después de normalizar RGB a colores opacos. Captura: .test-artifacts/ui-audit/current/themes-background-check.png. La sección continúa pendiente.

- Corrección posterior verificada: el navegador usaba las variantes Day (índice 1) sin patrones. Ahora usa Blue (índice 0) en modo claro, recuperando los nueve fondos de V21. Las burbujas de cuadrícula son cápsulas. APK firmado compilado e instalado correctamente: .test-artifacts/build-ui-themes-patterned.log; captura .test-artifacts/ui-audit/current/themes-patterned-final.png. Quedan colores de algunas burbujas, fuente, luna y espaciado. La sesión y el tema activo se conservaron.

- Afinado posterior instalado: luna rellena, encabezado Linotte regular y cápsulas más estrechas. build-ui-themes-polish.log correcto; captura themes-polish-final.png. Modo oscuro y retorno a claro comprobados. Pendiente: al cambiar modo en vivo la cabecera vuelve a gris hasta reabrir, y varios colores de burbuja difieren de V21.

### Traducir — 7 de octubre de 2026

- Se comparó con V21 y se ajustó el fondo blanco, título a la izquierda y flecha azul, conservando el orden de controles. El aviso de privacidad ahora identifica al proveedor seleccionado.
- Botón contextual, proveedor e idioma destino están conectados. Chats enteros, tipo, exclusiones, traducción automática y Mantener Markdown siguen pendientes de integración. Quedan diferencias de fuente, interruptor, color y espaciado.
- Compilación firmada e instalación incremental correctas: .test-artifacts/build-ui-translate-v21.log. En MEmu se probaron proveedor, idioma y botón; todos se restauraron a su estado previo. Captura: .test-artifacts/ui-audit/current/translate-v21-final.png. La traducción real no se probó en esta pasada.

### Otros Mods — 7 de octubre de 2026

- Se comparó la pantalla con V21: fondo blanco, título a la izquierda, flecha azul y fila de notificación persistente. El ajuste de ocultar Premium queda accesible en el menú adicional, conservando su preferencia.
- En MEmu Android 12 se comprobó el inicio del servicio en primer plano (notificación 38452) y su retirada al apagar el ajuste. Se restauró desactivado. El menú adicional abrió correctamente.
- Quedan diferencias de tipografía e interruptor. Próxima comparación visual: Traducir mensajes. Evidencia y límites en UI_AUDIT.md.
### Página Principal — 7 de octubre de 2026

- Se comparó con MDGram V21 y se recuperó su orden de ajustes, fondo blanco, título a la izquierda y vistas previas de carpetas y barra inferior. Captura final: `.test-artifacts/ui-audit/current/home-settings-v21-final.png`.
- Se conectó Título de inicio al título real de Chats y se verificó en MEmu después de reiniciar; se restauró MDGram. Los selectores de título/estilo de carpetas abren correctamente. Las preferencias actuales se conservaron.
- APK firmado compilado e instalado con sesión intacta: `.test-artifacts/build-ui-home-v21-final.log`. Bottom Bar ios aún no tiene implementación; quedan detalles visuales de altura, interruptores, color y tipografía. Próxima comparación: Otros mods.

### Auditoría visual de Chats (ajustes) — 6 de octubre de 2026

- Se comparó la sección Conversación del hub con las tres capturas de `APK/MDGram_V21_Universal.apk`. La pantalla migrada se titula «Chats» y organiza controles de sticker, emojis, fotos, burbujas, ActionBar, Media y entrada en el orden de V21. Capturas y diferencias pendientes: `UI_AUDIT.md`.
- Se sustituyeron los diálogos de sticker y calidad de fotos por deslizadores visibles; los selectores de burbuja y ActionBar usan tarjetas de vista previa. Estos ajustes conservaron sus conexiones existentes a `MDGramConfig`. Las dos filas de desenfoque contextual comparten la misma preferencia y se sincronizan visualmente.
- `assembleAfatStandalone` firmado completó en `.test-artifacts/build-ui-conversation-v21-final2.log`. El APK se instaló sobre MEmu sin borrar la sesión. Se recorrió la pantalla, se cambiaron y restauraron los controles de estilo, calidad, tamaño y desenfoque, y se capturaron `.test-artifacts/ui-audit/current/conversation-v21-final.png` y `conversation-v21-final-lower.png`.
- La vista previa del sticker usa la ilustración de MDGram y las tarjetas de estilo siguen siendo esquemáticas; falta afinar el dibujo. Los cambios de valor se verificaron en la interfaz, pero su efecto en medios y mensajes reales permanece para la fase funcional. La siguiente sección visual del hub es «Pantalla principal».

### Auditoría visual de General — 6 de octubre de 2026

- Se comparó la pantalla General de `APK/MDGram_V21_Universal.apk` con la migración en MEmu. La nueva composición usa una fila de cinco iconos MDGram, perfil compacto, controles de foto y teléfono, fuentes y acelerador de descarga visible con tres niveles. Evidencia: `UI_AUDIT.md` y `.test-artifacts/ui-audit/current/general-v21-final.png`.
- Los cinco iconos se extrajeron del APK V21 en cinco densidades. Sus cuatro variantes alternas tienen aliases reales de launcher; Naranja se activó en MEmu y luego se restauró Por defecto. El controlador tolera aliases antiguos de Cherrygram ausentes del manifiesto.
- Para cuentas sin preferencia previa, la foto como fondo comienza desactivada, como V21. Se verificó el cambio en vivo y se devolvió a desactivada. El selector de fuentes abrió correctamente y el nivel Rápida del acelerador se probó y se devolvió a No.
- `assembleAfatStandalone` firmado terminó correctamente (`.test-artifacts/build-ui-general-v21-final2.log`) y el APK se instaló incrementalmente conservando la sesión. Sigue pendiente el tinte dinámico de Material You: el PNG extraído se ve rosa, mientras que en la captura original del emulador aparece azul. La verificación de rendimiento real de descarga/carga sigue pendiente de la fase funcional.

### Canal de actualización — auditoría del 1 de octubre de 2026

- Canal existente: [GitHub jair1c/MDGram](https://github.com/jair1c/MDGram). `MDGramUpdater` consulta `https://mdgram-web.vercel.app/api/latest`, que refleja `version.json` de GitHub; `/dl` redirige a la APK universal de la última GitHub Release. La pantalla Ajustes de MDGram → Actualizar y el aviso al abrir la app ya usan ese motor. La banda que se quitó era exclusivamente el actualizador de Telegram.
- Al verificar el canal, GitHub publicaba V5 (código MDGram 7) y el APK local/instalado declaraba V5.1 (código MDGram 8). El `version.json` local de V5.1 aún no estaba publicado en `main`; no debe adelantarse al APK correspondiente.
- Se descargó la release V5 y se comprobó con `aapt`/`apksigner`: paquete `com.mdgram.messenger.web`, Android `versionCode` 65139, firma SHA-256 `78dc0c73b6e894145c46134b7ceb082bcd8bbe4f926dfad2d469accb20561a92`. El APK actual usa `com.mdgram.messenger`, `versionCode` 70389 y la misma firma. V5 no puede actualizar la app actual porque cambió el ID del paquete; se añadió validación de paquete, firma y `versionCode` antes de abrir el instalador.
- Próxima release para esta línea: compilar y publicar desde la base migrada con paquete `com.mdgram.messenger`, la misma keystore y `versionCode` Android mayor que 70389; después actualizar el manifiesto remoto. Probar en MEmu una actualización incremental desde esta APK hacia esa release y comprobar que conserva sesión y datos. No se publicó ni se instaló V5 sobre la app actual.
- Verificación local: `assembleAfatStandalone` terminó correctamente (`.test-artifacts/build-update-channel.log`); el APK se instaló incrementalmente en MEmu. En Ajustes de MDGram → Actualizar, «Buscar actualización» respondió «Estás en la última versión» al comparar V5.1 local contra V5 remoto. La validación de rechazo de un APK incompatible quedó compilada, pero falta probar el recorrido de descarga e instalación con una release nueva compatible.
- Aviso de migración publicado el 1 de octubre en el README de GitHub (`main`, commit `185faa8f`). La release V5.1 aún no existe; su aviso de cambio de paquete está preparado en `RELEASE_NOTES_V5.1.md` para incluirlo al publicarla.

## Objetivo

MDGram es un cliente Android no oficial de Telegram basado en Telegram 12.10.1 y Cherrygram. El proyecto conserva el protocolo, la mensajería y la interfaz principal de Telegram, e incorpora una identidad propia, un centro de ajustes MDGram y funciones adicionales de apariencia, rendimiento, multimedia, privacidad y traducción.

El trabajo actual busca dejar una compilación Android reproducible, conectar cada opción visible con su comportamiento real, reparar la traducción externa y comprobar los recorridos principales en un dispositivo o emulador.

## Base y estructura

- Rama de trabajo: `migracion-cherrygram-12.10`.
- Base registrada: commit `a9a5a25f`, integración de MDGram V5 sobre Telegram 12.10 y Cherrygram.
- Módulo central: `TMessagesProj`.
- Aplicación usada para pruebas: `TMessagesProj_AppStandalone`.
- Variante usada para la auditoría instalada: `afatStandalone` firmada.
- ID de aplicación de la variante instalada: `com.mdgram.messenger`.
- SDK de compilación y objetivo: Android 36.
- SDK mínimo: Android 24.
- Java y Kotlin JVM target: 17.
- NDK: 27.2.12479018.
- Arquitecturas: ARMv7 y ARM64.

## Componentes propios principales

- `MDGramSettingsActivity`: interfaz central de ajustes.
- `MDGramConfig`: almacenamiento y lectura de preferencias en `mdgramconfig`.
- `MDGramTranslator`: traducción mediante Google o Yandex y conversión de entidades Telegram/HTML.
- `MDGramResidentService`: servicio en primer plano opcional para mantener la app activa.
- `MDGramUpdater` y `MDGramUpdateSheet`: consulta y presentación de actualizaciones.
- `AboutMDGramActivity`: información, versión, enlaces y créditos.
- `MDGramSideDrawer`: personalización de la cabecera y del menú lateral.

## Cambios implementados

### Compilación y arranque

- Se actualizaron Google Services a 4.5.0 y Firebase Crashlytics Gradle a 3.0.8.
- El plugin de Google Services se aplica en el módulo de aplicación, evitando una configuración incorrecta en la biblioteca principal.
- Crashlytics se aplica a `TMessagesProj_AppStandalone` y dispone de un identificador de mapeo para `afatDebug`.
- Kotlin compila con JVM target 17, igual que Java.
- El manifiesto Standalone usa `org.telegram.messenger.ApplicationLoaderImpl`, corrigiendo el nombre de clase inválido que impedía arrancar.
- `MDGramResidentService` está declarado en el manifiesto.
- La inicialización de cuentas vacías es diferida. La cuenta 0 se conserva para el acceso y las cuentas adicionales solo se inicializan cuando están activas, reduciendo bases de datos, hilos y consumo nativo al arrancar.
- El máximo se ajustó a tres cuentas normales y cinco totales para mantener un consumo razonable.

### Identidad y versión

- La pantalla de bienvenida muestra `MDGram` como nombre de la aplicación.
- El identificador base es `com.mdgram.messenger`; la compilación de depuración añade `.web`.
- Los metadatos externos de actualización se incrementaron a V5.1, código 8.

### Ajustes conectados al comportamiento

- Redondeo o presentación completa de cantidades.
- Aceleración de descarga con niveles normal, rápida y ultra.
- Aceleración de subida.
- Desactivación del sensor de proximidad durante audio.
- Mejora de notas de voz con supresión de ruido y control automático de ganancia cuando el dispositivo los admite.
- Tamaño de stickers mediante multiplicador real de renderizado.
- Notificación persistente mediante servicio en primer plano.
- Fondo, desenfoque y oscurecimiento de la foto del perfil en el menú lateral.
- Ocultación del número telefónico en la cabecera.
- Opciones de conversación como ocultar “enviar como”, navegación al siguiente canal, texto de referencia, desenfoque del menú y estilos visuales.
- Selector de calidad de foto con tamaños entre 800 y 2560 píxeles.
- Opción para ocultar la barra de búsqueda principal.

### Traductor

- Se integraron los proveedores Google y Yandex como alternativa a la traducción de Telegram.
- La traducción se ejecuta fuera del hilo de interfaz.
- Se corrigió el manejo de HTML para preservar negrita, cursiva, subrayado, tachado, código, bloques `pre`, spoilers y enlaces.
- Se escapan texto, atributos y URL antes de enviarlos al proveedor.
- Se reconstruyen entidades solapadas o anidadas sin corromper sus posiciones.
- Google y Yandex reciben explícitamente `format=html` cuando se traducen mensajes con entidades.
- Si un proveedor falla, se devuelve el error por el flujo normal de Telegram en lugar de bloquear la interfaz.

### Credenciales de Telegram

- Se creó la aplicación `MDGram` en `my.telegram.org`.
- `BuildVars` obtiene `APP_ID` y `APP_HASH` desde `uz.unnarsx.cherrygram.Extra`.
- Los valores reales están configurados en `TMessagesProj/src/main/java/uz/unnarsx/cherrygram/Extra.kt`.
- Existe una copia privada en `MDGRAM_CREDENTIALS.local.md`.
- Ambos archivos están excluidos por `.gitignore`. Este documento no contiene los valores secretos.

## Compilación local

Desde PowerShell en la raíz del repositorio:

```powershell
.\gradlew.bat :TMessagesProj_AppStandalone:assembleAfatStandalone
```

APK resultante:

```text
TMessagesProj_AppStandalone/build/outputs/apk/afat/standalone/app.apk
```

Instalación en el dispositivo conectado:

```powershell
adb install -r -d TMessagesProj_AppStandalone\build\outputs\apk\afat\standalone\app.apk
```

Para abrirla, debe usarse el paquete de la variante:

```powershell
adb shell monkey -p com.mdgram.messenger -c android.intent.category.LAUNCHER 1
```

La variante `afatDebug` conserva el paquete `com.mdgram.messenger.web` para pruebas paralelas; el APK actual firmado y usado en la auditoría es `afatStandalone`.

## Validación realizada

El 15 de septiembre de 2026 se ejecutó la siguiente comprobación:

- `assembleAfatDebug`: correcto en las dos compilaciones de cierre; 98 tareas y `BUILD SUCCESSFUL`.
- APK generada e instalada correctamente sobre MEmu con `adb install -r -d`, conservando los datos de usuario.
- Dispositivo ADB: MEmu en `127.0.0.1:21503`, modelo virtual `SM-S938B`.
- Arranque: correcto, sin excepción fatal.
- Bienvenida: muestra el nombre MDGram y texto en español.
- Navegación a acceso: correcta.
- La sesión autenticada sobrevivió al reinicio y a la reinstalación incremental.
- No reaparecieron `API_ID_INVALID`, `API_ID_PUBLISHED_FLOOD` ni excepciones fatales.
- En Mensajes guardados se enviaron y visualizaron texto, foto con descripción, TXT y notas de voz con la mejora de voz apagada y encendida.
- Se tradujo texto con Google y Yandex; se cambió de proveedor desde la interfaz y se restauró Google.
- Se probaron ajustes de foto de perfil, búsqueda, redondeo, aceleradores, proximidad, stickers, calidad de foto, voz, traducción y servicio residente; todos persistieron y se restauraron a sus valores de prueba.
- Se descargó `20260810_115916-settings.cherry` (287 B). La referencia caducada se renovó y la descarga terminó correctamente.
- La pantalla `Pantalla principal` ya no muestra `TabTitleStyle` ni `TabStyle`: ahora permite elegir modo de pestañas, mostrar títulos, dibujar borde y ocultar “Todos”. La selección del modo se probó y se restauró a “Icono y texto”. Se retiró el control de título de usuario porque la cabecera debe mantener la identidad MDGram.
- Se actualizaron los textos visibles de preferencias, acerca de y seguimiento de canal a MDGram en español e inglés. Las menciones restantes de Cherrygram corresponden a créditos/licencia, nombres de paquetes internos o enlaces heredados y requieren decisión editorial antes de reemplazarse.
- Se unificaron el icono de aplicación, icono redondo, splash, contactos, autenticación y pantalla de versión en `mdgram_icon_3tone`. El launcher ahora usa `@mipmap/mdgram_launcher`, un icono adaptativo con fondo blanco y fallback para versiones anteriores. Las rutas de notificación (`CGResourcesHelper` y el servicio residente) ya devuelven el icono de MDGram y no `cg_notification`/`notification` heredados. El recurso MDGram sí existe en `TMessagesProj/src/main/res/drawable/mdgram_icon_3tone.xml`; los recursos `ic_launcher_dr`, `icon_foreground*` e `icon_launcher_cherry*` eran Telegram/Cherrygram heredados.

## Pendientes prioritarios

### Contexto recuperado de SESSION_NOTES — 16 de septiembre de 2026

- `SESSION_NOTES.md` documenta el desarrollo previo desde la base NekoX/Telegram, la publicación automatizada hasta V5 y la decisión posterior de migrar a Cherrygram. La base actual ya corresponde a esa migración (`Telegram 12.10.1`, `Cherrygram 11.2.0`, `MDGram V5.1`).
- `MDGram_V21_Universal.apk` (`app.mdgram.android`) es la referencia visual original. `MDGram.apk` (`com.mdgram.messenger.web`) es la última compilación de MDGram previa a la migración; deben compararse en ese orden cuando difieran.
- Las notas confirman que el teléfono de uso real ya fue migrado a la firma personal de MDGram. Los builds debug con otra firma no pueden actualizarlo; para ese dispositivo se debe usar `assembleAfatStandalone` y conservar siempre la misma keystore.
- Las prioridades históricas aún relevantes son revisar qué personalizaciones sobrevivieron a la migración, preservar entidades en el traductor, terminar validaciones físicas de multimedia/sensores y mantener el pipeline de releases. Varias decisiones visuales recientes deben contrastarse con estas notas antes de sustituir recursos originales.
- Referencia capturada después del primer inicio de sesión en el APK V21: `.test-artifacts/reference-v21/first-login-reference.png`, acompañada de `first-login-reference.xml`. Debe replicarse en la migración como un diálogo inicial con título `MDGram V21`, mensaje de bienvenida, botones para canal, grupo, Facebook y web oficial, y acción Cancelar. La captura también documenta el header, buscador y pestañas inferiores del original.

### Firma local recuperada — 16 de septiembre de 2026

- Se encontró `keystore.properties` con sus cuatro campos completos. Sus secretos no se copiaron a la documentación ni a la salida de herramientas.
- Se restauró en `TMessagesProj_AppStandalone/build.gradle` la carga de `keystore.properties`: debug conserva la firma de desarrollo y standalone usa exclusivamente la configuración personal.
- La ruta absoluta guardada pertenece a la PC anterior. El script acepta automáticamente un archivo con el mismo nombre (`mdgram-release.jks`) colocado junto a `keystore.properties`, lo que hace portable la configuración.
- `keystore.properties`, `*.jks` y `*.keystore` quedaron ignorados por Git. Se recuperó `mdgram-release.jks`, la compilación standalone quedó operativa y el certificado coincide con la huella histórica SHA-256 `78:DC:0C:73:B6:E8:94:14:5C:46:13:4B:7C:EB:08:2B:CD:8B:BE:4F:92:6D:FA:D2:D4:69:AC:CB:20:56:1A:92`.

### Paquetes coexistentes para la auditoría — 16 de septiembre de 2026

- La variante standalone actual usa `com.mdgram.messenger`; se retiró de ella el sufijo heredado `.web`. La variante debug conserva ese sufijo.
- Esto permite instalar en paralelo el original V21 (`app.mdgram.android`), la versión previa a la migración (`com.mdgram.messenger.web`) y la migración actual (`com.mdgram.messenger`).
- `assembleAfatStandalone` finalizó correctamente, `apksigner` verificó la firma v2 y la huella histórica, y el APK se instaló y abrió en MEmu. Artefacto: `TMessagesProj_AppStandalone/build/outputs/apk/afat/standalone/app.apk`.

### Auditoría visual V21 — 16 de septiembre de 2026

- La comparación actual usa solamente el original `MDGram_V21_Universal.apk` y la migración `com.mdgram.messenger`; la versión previa a la migración queda reservada para una etapa posterior.
- Inicio recuperó la estructura V21: cuatro pestañas inferiores, buscador fijo, hamburguesa, luna, título MDGram centrado, divisores e iconos clásicos.
- Se extrajeron del V21 los paquetes tipográficos y se restauró Linotte como fuente destacada. Evidencia: `.test-artifacts/ui-audit/current/home-ui-pass-3.png`.
- La bienvenida posterior al primer acceso quedó incorporada y validada. Sus enlaces se conectarán cuando comience la fase funcional.
- Se detectó que Ajustes conserva la estructura moderna de tarjetas y una cabecera grande; se documentó con `.test-artifacts/ui-audit/current/settings-ui-pass-3.png` para la siguiente pasada visual.
- El detalle y la matriz viva de comparación están en `UI_AUDIT.md`.
- La navegación inferior se corrigió para ocupar todo el ancho y distribuir uniformemente las cuatro pestañas. Evidencia final: `.test-artifacts/ui-audit/current/home-v21-final.png`.
- La capa de historias ya no dibuja el wordmark de Cherrygram sobre Inicio; usa la cabecera compacta histórica de MDGram y mantiene libre la hamburguesa.
- Ajustes recuperó la composición V21 con perfil compacto, datos de cuenta, accesos directos MDGram e iconos grises. Evidencia: `.test-artifacts/ui-audit/current/settings-v21-final.png`.
- Se comprobó que la fila `Ajustes de MDGram` abre el centro actual y conserva sus rutas a General, Conversación y Pantalla principal.
- El APK standalone firmado volvió a compilar e instalar incrementalmente sin perder la sesión. Registro: `.test-artifacts/build-ui-settings-pass-3.log`.
- `Mensajes guardados` recuperó la cabecera plana de V21: fondo blanco, título centrado, marcador a la derecha, controles azules y contador azul. Se retiró el vidrio de Cherrygram cuando está seleccionado el estilo iOS de MDGram.
- La base visual se migró una vez al tema claro `Day`; el chat usa fondo blanco y el degradado celeste se calibró contra la captura original. Evidencia final: `.test-artifacts/ui-audit/current/saved-messages-v21-final-badge.png`.
- La comparación de píxeles confirmó una coincidencia práctica del degradado: V21 `#80CFE5` a `#5BA3D4`; migración `#80CFE4` a `#5BA3D5`.
- `assembleAfatStandalone` finalizó correctamente y el APK firmado se instaló de forma incremental conservando la sesión. Registro: `.test-artifacts/build-ui-saved-final-resume.log`.
- Contactos del tab principal se migró a la estructura visual V21: cabecera blanca, tres accesos planos, banda de ordenamiento gris y lista sin tarjetas. Referencia y resultado: `.test-artifacts/ui-audit/original/contacts.png` y `.test-artifacts/ui-audit/current/contacts-v21-final.png`.
- Se verificaron en MEmu los recorridos hacia Nuevo grupo, Nuevo chat secreto y Nuevo canal, además de la búsqueda de contactos. La flecha de Contactos vuelve a Chats; el APK se reinstaló incrementalmente sin borrar la sesión. Registro de compilación: `.test-artifacts/build-ui-contacts-v21.log`.
- Llamadas recuperó la composición V21: fondo blanco, título a la izquierda, sección `Videochats activos`, fila plana y botón flotante cuadrado con teléfono. La referencia está en `.test-artifacts/ui-audit/original/calls.png` y el resultado final en `.test-artifacts/ui-audit/current/calls-v21-final.png`.
- El hub `Ajustes de MDGram` quedó alineado visualmente con el original V21: fondo blanco, tarjetas gris `#F4F4F4`, cabecera sin título repetido y sección `TELEGRAM` dentro de su tarjeta. Capturas en `.test-artifacts/ui-audit/original/mdgram-settings-hub-next.png` y `.test-artifacts/ui-audit/current/mdgram-settings-hub-v21-final.png`. Build firmado correcto e instalación incremental con sesión conservada; se probaron seis rutas principales sin modificar ajustes.
- Siguiente pantalla visual identificada: `General`. En la migración, el selector extenso de iconos Cherrygram desplaza los controles V21; la referencia muestra solo cinco iconos MDGram en una fila, perfil compacto y controles de fuente/velocidad. Capturas comparables: `.test-artifacts/ui-audit/original/general-next.png` y `.test-artifacts/ui-audit/current/general-next.png`. Aún no se modificó esa pantalla.
- El botón de Llamadas abrió `Nueva llamada` en MEmu sin iniciar una llamada; la flecha volvió a Chats. El módulo standalone deja de ofrecer el APK de actualización de Telegram, incompatible con el paquete y firma de MDGram; la banda desapareció tras reinstalar incrementalmente el APK, sin perder la sesión. El canal propio de GitHub ya estaba implementado; ver la auditoría del 1 de octubre. Compilación final de esta etapa: `.test-artifacts/build-ui-calls-v21.log`.

### Referencia original e icono de notificaciones V21 — 16 de septiembre de 2026

- El usuario aclaró que `APK/MDGram_V21_Universal.apk` es el original; `APK/MDGram.apk` corresponde a su última versión antes de migrar a la base Cherrygram.
- Se reemplazaron los PNG `mdgram_notification` por los de `drawable/notification` del original V21, sin modificar sus imágenes. Incluye mdpi, hdpi, xhdpi, xxhdpi y xxxhdpi; entradas ZIP `res/LXZ.png`, `res/XGn.png`, `res/Vrr.png`, `res/un7.png` y `res/fOK.png`.
- SHA256 del APK V21: `12F8CE54C69D16EF0B153B3EF7FB18ACB4699BB4C01874D26161228F49775561`. Esta selección sustituye la extracción de notificaciones de `MDGram.apk` documentada más abajo. Launcher, bienvenida y paleta conservan los cambios anteriores.
- Validación: compilación correcta (28s), cinco densidades presentes en el APK generado e instalación incremental correcta en MEmu después de reiniciar el emulador. La app abre sin excepciones fatales. Pendiente comprobar visualmente el icono mediante una notificación real.

### Corrección de identidad cromática — 16 de septiembre de 2026

- Aclaración del usuario: la petición anterior sobre los colores se interpretó incorrectamente. La interfaz debe usar la identidad de MDGram, no conservar el rosa de Cherrygram.
- Se centralizaron `MDGRAM_COLOR` (#3465AE) y `MDGRAM_COLOR_LIGHT` (#4283C4), basados en el logo original, para sustituir el rosa de marca #A33156/#E54C7F.
- Aplicado al círculo de bienvenida, degradado de inicio, valores predeterminados `cgGradient1/2` y accesos de cámara, acerca de y ajustes avanzados. Se mantienen los identificadores internos de tema para compatibilidad con temas personalizados.
- Validación: compilación correcta (2m 58s) e instalación incremental en MEmu. Bienvenida comprobada visualmente en modo claro y oscuro; círculo y botón muestran los azules MDGram. Se restauró el modo claro. Capturas en `.test-artifacts/apk-original/welcome-mdgram-blue.png` y `welcome-mdgram-blue-dark.png`. Los ajustes tras iniciar sesión se revisaron en código, sin validación visual en esta sesión.

### Actualización de iconos — 16 de septiembre de 2026

- Se extrajo `drawable/notification` de `APK/MDGram.apk` (versión 12.4.1), conservando los PNG originales mdpi, hdpi, xhdpi y xxhdpi como `mdgram_notification.png`. Es una silueta blanca con recorte circular, distinta del logo de tres tonos.
- Origen SHA256: `50F818B82D78B2408FE73C65686F8F51B92A4C020E1D9F30288C26D8E49DA5F1`. Entradas ZIP: `res/5SL.png`, `res/3l3.png`, `res/MSy.png` y `res/SPu.png`, respectivamente.
- `CGResourcesHelper` (mensajes y servicio residente) y `MDGramResidentService` usan ahora `R.drawable.mdgram_notification`.
- Validación: `assembleAfatDebug` correcto (4m 20s), cuatro PNG presentes en el APK y reinstalación incremental correcta en MEmu. El comando de notificación local por ADB no produjo una notificación visible; queda pendiente confirmar visualmente una notificación real de la app. No se enviaron mensajes a terceros.
- El launcher conserva fondo blanco y foreground centrado de 80dp; la primera solución con ScaleDrawable se descartó porque dejaba el logo invisible.
- Actualización posterior: se sustituyó ese escalado manual por los PNG originales de `mipmap/ic_launcher_sa`, `mipmap/icon_background_sa` y `mipmap/icon_foreground_sa` extraídos de `APK/MDGram.apk`, en cinco densidades. El adaptive icon usa las capas originales; su variante monocromática usa la silueta MDGram porque el APK original aún apuntaba al avión de Telegram en esa variante.
- Bienvenida: `IntroActivity` usa `drawable/intro_tg_plane` del APK original, extraído como `mdgram_intro_logo` en cuatro densidades. Se conservan los colores existentes de Cherrygram por petición del usuario. Se corrigió además la generación OpenGL para reservar las 24 texturas utilizadas, incluida la máscara de la bienvenida.
- Validación del launcher y bienvenida: compilación correcta (2m 30s), instalación incremental correcta y comprobación visual en MEmu. Capturas: `.test-artifacts/apk-original/launcher-final.png` y `welcome-final.png`. El launcher muestra el logo original con márgenes amplios; la bienvenida muestra el símbolo MDGram y conserva el círculo y botón rosa de Cherrygram.
- Incidente de la sesión anterior: se ejecutó por error `cmd package clear com.mdgram.messenger.web` en MEmu, borrando datos locales y la sesión. No forma parte del procedimiento de instalación. Usar únicamente instalación incremental para preservar los datos.

### P0: completar los recorridos en el teléfono

- Probar mensajería y multimedia desde un teléfono físico además de MEmu.
- Probar el traductor con enlaces, negrita, emojis y entidades anidadas en ambos proveedores.
- Probar el selector de calidad de fotos en 800, 1280, 1920 y 2560 píxeles.
- Confirmar descarga y subida normal, rápida y ultra en Wi-Fi y red lenta.
- Probar proximidad y mejoras de voz en un teléfono físico; MEmu no reproduce fielmente sensores ni procesamiento acústico.
- Verificar notificaciones, ejecución en segundo plano y servicio residente en Android 13 a 16.

### P1: preparación para distribuir

- Sustituir `google-services.json`, Firebase/Crashlytics y configuración de notificaciones por recursos propios de MDGram.
- Usar un almacén de firma de producción propio y documentar su custodia fuera de Git.
- Confirmar si `com.mdgram.messenger` será el ID definitivo de publicación; la variante standalone actual ya instala con ese paquete y puede coexistir con las dos APK de referencia.
- Revisar iconos, textos y enlaces heredados que aún mencionan Cherrygram. Los créditos GPL deben conservar la atribución; los textos de producto restantes sí deben migrarse a MDGram.
- Actualizar el README principal, capturas, licencia, créditos y política de privacidad para la distribución de MDGram.
- Validar una actualización real desde una versión anterior hacia la siguiente release firmada de GitHub, conservando datos y sesión. El canal y las releases firmadas ya existen.
- Regenerar las credenciales de Telegram si alguno de los archivos privados llega a compartirse.

### P2: mantenimiento técnico

- Añadir pruebas unitarias para la conversión texto/HTML del traductor, especialmente entidades cruzadas y caracteres escapados.
- Añadir una prueba de humo automatizada para arranque y pantalla de acceso.
- Eliminar el ruido de finales de línea en `MediaController.java` antes de preparar el commit, para que la revisión muestre solo los cambios funcionales.
- Atender la advertencia de Gradle sobre `android.defaults.buildfeatures.buildconfig`; declarar `buildFeatures.buildConfig` únicamente en los módulos que lo necesiten.
- Revisar la advertencia de empaquetado nativo y alinear `jniLibs.useLegacyPackaging` con `android:extractNativeLibs`.
- Migrar APIs obsoletas antes de Gradle 9 y Android Gradle Plugin 10.
- Ejecutar lint y una compilación `standalone` minificada antes de producir una versión candidata.

## Riesgos y límites conocidos

- Los endpoints no oficiales de Google y Yandex pueden cambiar, limitar solicitudes o bloquear clientes. Debe conservarse una ruta de error clara y, si corresponde, un proveedor configurable.
- Las pruebas en MEmu no sustituyen sensores, llamadas, notificaciones ni audio de un teléfono físico.
- El APK standalone actual está firmado y minificado, pero todavía no debe publicarse hasta cerrar la auditoría visual, las pruebas funcionales y las validaciones pendientes en teléfono físico.
- El repositorio contiene una base grande de Telegram y Cherrygram; cada actualización ascendente puede producir conflictos en clases centrales como `MediaController`, `ApplicationLoader` y `TranslateController`.
- La rama tiene cambios locales todavía sin commit. Deben revisarse por bloques funcionales antes de integrarlos.

## Criterio para considerar lista la siguiente versión

La versión candidata estará lista cuando compile en limpio, se instale sobre una versión anterior, permita autenticación y persistencia de sesión, complete mensajería y multimedia básica, conserve formato al traducir con ambos proveedores, respete todos los ajustes visibles, pase las pruebas en un teléfono físico y se firme con credenciales de producción propias.

- Cierre de Otros Mods: APK firmado compilado, verificado e instalado; log .test-artifacts/build-ui-other-mods-v21-final.log y captura .test-artifacts/ui-audit/current/other-mods-v21-final.png.

- Decisión del usuario (7 de octubre): conservar Actualizar con su diseño One UI actual; no migrar esta pantalla a V21.

- Explorar temas: tinte azul durante cambio claro/oscuro corregido y probado en MEmu; build-ui-themes-live-accent.log correcto, APK verificado themes-live-accent.apk. Quedan las diferencias de colores de algunas burbujas respecto a V21.

- Chats: sticker original V21 incorporado a la vista previa, Linotte regular en encabezados/etiquetas propias y tamaño de imagen conectado al ajuste. Build firmado build-ui-sticker-preview-v21.log correcto e instalado en MEmu; tres niveles comprobados y restaurado 14. Quedan encuadre y detalles de composición, celdas comunes y verificación en mensajes reales. General/Material You sigue pendiente.

- Material You: recuperado vector adaptativo V21 y colores sistema Android 12+, comprobado en General y launcher. Selección de launcher restaurada a Por defecto. Chats: encuadre y espaciado de sticker afinados y nivel 14 restaurado. APK firmado build-ui-material-sticker-final.log correcto e instalado; capturas general-material-final.png/sticker-spacing-final.png. Pendientes visuales próximos: interruptores y tipografía de filas comunes; conservar Actualizar One UI.

- Nueva dirección visual del 10 de octubre: ver NEW_VISUAL_REFERENCES.md y el visor de 388 fotogramas en .test-artifacts/new-reference-20261010/viewer.html. Próximo bloque: navegación Chats/Contactos/Ajustes/Perfil, luego tarjeta de cuenta y menú modal. Código/APK aún sin cambios de esta nueva referencia; mantener Actualizar One UI.

## Avance 10 de octubre de 2026 — ambos estilos de navegación

Implementado interruptor persistente «Usar nueva navegación» en Pantalla principal. La opción clásica queda por defecto y conserva su orden guardado; la nueva usa Chats/Contactos/Ajustes/Perfil con avatar, hub MDGram como destino Ajustes y perfil propio como destino Perfil. Se cambia al regresar al inicio sin reiniciar. Primera compilación, firma, instalación y recorridos comprobados en MEmu; ajuste final de tamaño de cápsula en compilación. Detalles y capturas en UI_AUDIT.md. Próximo bloque: tarjeta de cuenta del hub y menú modal, después vista previa completa de Inicio. Actualizar conserva One UI.

Cierre del bloque de navegación: compilación final 3m 21s, firma e instalación verificadas, altura compacta y persistencia comprobadas tras reabrir el proceso. APK `.test-artifacts/MDGram-new-navigation.apk`. Nueva barra activada en MEmu para revisión. Registrado ANR de arranque posterior a actualización sin ventana enfocada, recuperado reiniciando MEmu; causa raíz pendiente. No se considera cerrada la estabilización global.

## Tarjeta de cuenta y menú modal — 10 de octubre de 2026

Completado bloque visual opcional: nueva navegación incluye tarjeta de cuenta en el hub (avatar/cámara, nombre/teléfono, gestión, QR y acceso a sugerencias) y menú modal centrado. OFF mantiene cajón lateral clásico. Compilación final 4m 15s, firma e instalación incremental verificadas, pruebas en MEmu de claro/oscuro, cierre con Atrás, cambio de estilos, apertura de Nuevo grupo, Dispositivos y QR. APK `.test-artifacts/MDGram-account-modal.apk`; capturas y límites en UI_AUDIT.md. Actualizar permanece One UI. Cámara abre perfil y Sugerencias abre ajustes de cuenta; no se validaron modificaciones reales de foto/cuenta ni creación/vinculación. Próximo bloque: vista previa completa de Inicio y modos de título del video.

## Vista previa de Inicio y modos de título — 10 de octubre de 2026

Completada vista previa fija con avatar, búsqueda, carpetas, conversación de muestra, FAB y barra inferior. Selector persistente MDGram / Nombre completo / Primer nombre / Chats conectado al encabezado real al volver a Chats. Compilación firmada correcta en 5m 24s, firma e instalación incremental verificadas. MEmu: cuatro modos, persistencia tras reabrir, interruptor OFF/ON, desplazamiento de controles y ocultación de búsqueda/FAB en la muestra comprobados. APK `.test-artifacts/MDGram-home-preview.apk`. Se restauran título personalizado OFF, búsqueda y FAB visibles, nueva navegación ON y Day; modo Chats recordado. Pantalla negra de arranque recuperada reiniciando MEmu sin borrar datos; causa pendiente. Capturas y límites en UI_AUDIT.md. Próximo bloque: estilos de carpetas, contadores e historias de la referencia. Mantener Actualizar One UI.

- Cierre: `build-folder-styles-final.log` terminó con BUILD SUCCESSFUL en 5m 40s. Firma verificada e instalación incremental correcta; APK guardado en `.test-artifacts/MDGram-folder-styles.apk`.
- Los cinco estilos se comprobaron en la muestra y en Chats reales, con una carpeta temporal que contenía únicamente Mensajes guardados. Selección de carpeta comprobada; carpeta MDGramTest eliminada al finalizar, sin eliminar chats ni enviar mensajes (`folders-test-removed.png`). Pills también comprobado en tema oscuro (`folders-pills-dark-real.png`, `folders-pills-dark-preview.png`).
- Ocultar contadores comprobado en muestra y Chats. Limitación: la preferencia heredada tabsNoUnread también oculta el contador de Chats de la navegación inferior; pendiente separar ambos controles. Mostrar historias OFF elimina el acceso de cámara tras reiniciar; ON lo restaura. No había historias activas para verificar su contenido y no se publicó ninguna.
- Preferencias restauradas: Rounded, contadores visibles, historias visibles, tema Day y navegación nueva activa. Los cambios de estilos, contadores e historias todavía requieren reiniciar para aplicarse completamente en Chats; la muestra cambia inmediatamente.
- Incidencia de MEmu: pantalla negra y reloj de SystemUI detenido con ambas APK, incluso con jerarquía de la app activa. Reiniciar únicamente com.android.systemui recuperó la imagen y conservó las sesiones. No apareció salida AndroidRuntime en las consultas de esta pasada; la causa del bloqueo del sistema sigue pendiente.
- Próximo bloque: aplicar ajustes al volver a Chats sin reinicio y separar el contador de carpetas del contador inferior. Actualizar conserva su interfaz One UI. No se declara paridad visual completa ni cierre de la auditoría funcional.
## Ajustes de Inicio sin reinicio — 10 de octubre de 2026
- Cierre: build-live-settings-final.log terminó correctamente en 6m 40s; firma verificada, APK instalado incrementalmente y copiado a `.test-artifacts/MDGram-live-settings.apk`.
- MEmu: Text Only, Pills y retorno a Rounded se aplicaron al volver a Chats, sin reiniciar. Selección de la carpeta conservada (`folders-live-selection.png`). Contadores de carpetas ocultos mientras Chats inferior mantenía 496; al restaurarlos aparecieron ambos (`folders-live-restored.png`).
- Historias ON/OFF/ON comprobadas con el mismo PID 11404, sin cerrar el proceso: botón de cámara aparece/desaparece (`stories-live-on-final.png`, `stories-live-off-final.png`). Primera prueba detectó que updateStoriesPosting no actualizaba visibilidad; se añadió updateFloatingButtonVisibility y se repitió tras la compilación final.
- También se conectó el control heredado de carpetas en FoldersPreferencesEntry a hide_folder_counters, evitando volver a compartir la preferencia inferior. No se migró automáticamente el valor antiguo, porque no distinguía ambas intenciones; contadores de carpetas visibles por defecto.
- Carpeta temporal MDGramTest, con sólo Mensajes guardados, eliminada al finalizar. Restaurados Rounded, contadores visibles, historias ON, tema Day y navegación nueva ON. Sin envíos de mensajes ni publicación de historias. Buffer de crashes vacío en la consulta final.
- Límites: no había historias activas para validar contenido; los otros ajustes de Inicio conservan sus requisitos previos. No se cierra auditoría global ni se modifica Actualizar One UI.

## Controles de Inicio y selector de contenido — 10 de octubre de 2026
- Cierre: build-home-controls.log correcto en 6m 42s, firma verificada, instalación incremental correcta. APK `.test-artifacts/MDGram-home-controls.apk`.
- MEmu: ocultar buscador elimina su superficie y reserva de altura y deja lupa superior; ocultar FAB retira composición y cámara. Preferencias actualizadas en el proceso 13201. Restaurados ambos visibles; al estar desplazada la cabecera, el buscador respeta su colapso por scroll y vuelve al deslizar hacia abajo (`home-controls-restored.png`). Campo de búsqueda abierto y detectado como EditText.
- Selector propio con radios y Cancelar (`home-content-selector.png`). Sólo iconos, Sólo texto e Iconos y texto comprobados en carpetas reales; restaurado Iconos y texto con Rounded. Carpeta temporal sólo con Mensajes guardados eliminada; sin envío de mensajes ni reacciones.
- Incidencia de la automatización: una secuencia de Atrás salió de la app actual y mostró la original, y se abrió una conversación y su menú sin ejecutar acciones. Se regresó explícitamente a com.mdgram.messenger antes de repetir la validación; no usar esa captura como evidencia del selector.
- Tema Day, nueva navegación ON, historias ON y contadores visibles conservados. Buffer de crashes vacío al cierre. Selector oscuro no comprobado en esta pasada. Actualizar One UI intacto.
- Próximos pendientes: interruptores y tipografía de filas comunes, detalles de composición respecto a V21 y opción de estilo inferior iOS, cuyo selector no aparece abierto en el video. Persisten verificación de historias activas y auditoría funcional global.

## Cierre de tipografía e interruptores — 12 de octubre de 2026

- Compilación del 10 de octubre confirmada en build-settings-style.log: BUILD SUCCESSFUL en 3m 29s. Firma verificada e instalación incremental correcta; APK .test-artifacts/MDGram-settings-style.apk. No se recompiló el 12: se retomó la verificación y documentación.
- Verificados General, Conversación, Inicio, Otros mods y Traductor en claro; Inicio y Traductor en oscuro. Capturas settings-style-*.png en .test-artifacts/ui-audit/current. Vista previa clásica oscura sin fondo blanco fijo. Ocultar buscador comprobado y restaurado.
- Linotte y colores del tema aplicados explícitamente a las filas TextCheckCell/TextSettingsCell propias. La fuente personalizada y la opción de fuente del sistema están contempladas en código, pero no se comprobaron en dispositivo en este bloque. TextInfoPrivacyCell conserva su tipografía anterior.
- Se conserva la cápsula como dirección visual iOS y el interruptor clásico/nuevo. Actualizar One UI intacto. No se afirma paridad visual completa.
- Restaurados navegación nueva ON, pestañas inferiores ON, buscador visible y FAB visible; seleccionado Modo diurno. El proceso 3735 permaneció activo durante la comprobación final. Sin mensajes ni reacciones enviados.
- Incidencia del 12: crash nativo SIGFPE en RenderThread, libhwui/BlobCache/ShaderCache de Android en MEmu. Registro .test-artifacts/memu-render-crash-20261012.log. Relaunch correcto; reinicio de SystemUI recuperó la composición de pantalla. Causa pendiente de diagnóstico: no se atribuye todavía a un cambio de la app. Esto impide dar por cerrada la estabilidad global.
- Pendientes: diagnóstico de la incidencia nativa, fuentes alternativas, composición restante respecto a las referencias y auditoría funcional. Historias con contenido activo continúan sin comprobar.

## Menú flotante — 12 de octubre de 2026

El cambio claro/oscuro conserva el menú abierto y actualiza sus colores. Compilación firmada e instalación incremental verificadas; ambos sentidos comprobados en MEmu. APK .test-artifacts/MDGram-modal-theme.apk. Referencia V21 confirma contador de Chats con fondo rojo rosado y cifras blancas; no se modificó el contador actual en esta pasada. Detalles en UI_AUDIT.md.


## Píldora de cuenta y contador — 12 de octubre de 2026

- Píldora Gestionar tu cuenta: superficie elevada en contraste y borde claro en oscuro; apariencia clara conservada. Se recalcula al cambiar tema sin cerrar modal.
- Contadores de pestañas principales: rojo rosado #E83E6D y números blancos. No cambia contadores de carpetas ni de filas de conversaciones.
- BUILD SUCCESSFUL en 5m 39s, firma verificada e instalación incremental Success. APK .test-artifacts/MDGram-pill-counter.apk. Verificados claro y oscuro en com.mdgram.messenger; captura pill-counter-dark-final.png muestra píldora y contador. Menú sigue abierto al alternar tema.
- Incidencia MEmu: SurfaceFlinger SIGABRT y Chrome DeadSystemException durante revisión; reinicio interno de Android. Se recuperó sin borrar datos y se repitió la comprobación, verificando paquete actual. Capturas anteriores que mostraban app.mdgram.android no son evidencia de este cambio. Log pill-counter-crash.log. Estabilidad global sigue pendiente.

## Botón de tema superior — 12 de octubre de 2026

- Pulsación normal alterna claro/oscuro directamente. Pulsación larga conserva menú de opciones. Icono luna en claro y sol en oscuro; descripción accesible sincronizada en la actualización de colores de DialogsActivity. Reutilizada lógica de temas guardados con decisión basada en oscuridad efectiva, no sólo igualdad del nombre.
- BUILD SUCCESSFUL en 6m 19s, firma verificada e instalación incremental correcta. APK .test-artifacts/MDGram-header-theme.apk. Cambio directo en ambos sentidos verificado con jerarquía y capturas header-day-final.png/header-dark-final.png.
- Primera pasada interrumpida por nuevo SIGABRT de SurfaceFlinger en MEmu; app recuperada y pruebas repetidas. Registro header-theme-crash.log. Estabilidad global pendiente; no atribuir aún causa a la app.

## Recorridos del menú flotante — 12 de octubre de 2026

- Verificados en com.mdgram.messenger: Gestionar tu cuenta → Cuenta/Ajustes; Dispositivos vinculados → Dispositivos; Llamadas recientes → Llamadas; Contactos → Contactos; Ajustes de MDGram → hub MDGram Messenger/General. Evidencia route-account.xml, route-devices.xml, route-calls.xml, route-contacts.xml y route-settings-final.xml en .test-artifacts.
- Cierre con X, Atrás y toque exterior: jerarquías finales sin Cerrar menú y paquete actual conservado (route-close-x/back/outside.xml). Retorno a Chats comprobado. Sin modificar sesiones, iniciar llamadas ni enviar mensajes.
- No se comprobó creación de grupos/canales ni enlaces externos en este bloque; no se afirma recorrido completo de todas las acciones.
- MEmu necesitó detener/iniciar su VM mediante memuc sin borrar datos. ADB reboot no respondió. Nuevo fallo SurfaceFlinger SIGABRT: Abort message captureScreenCommon: Buffer failed to allocate: 2. Evidencia route-crash.log. Identifica la operación que falla, no la causa raíz de la asignación. Tras recuperación se completó la comprobación por jerarquía. Estabilidad y capturas siguen requiriendo diagnóstico.
- Sin cambios de código ni nuevo APK en este bloque; continúa instalado MDGram-header-theme.apk. Pantalla Actualizar One UI conservada.

## Diagnóstico y preparación de publicación — 12 de octubre de 2026

- Publicación final todavía no recomendada. Informe RELEASE_READINESS.md con evidencia y pendientes; no se publicó en GitHub.
- Motor gráfico alternativo no persistió (write 1 aceptado, read 0 tras reinicio). Sin atribuir mejora al cambio. MemAvailable ~2,5GiB.
- Tras reinicio, 6 capturas y 4 alternancias de tema correctas, SurfaceFlinger PID217 conservado; crash buffer vacío en consulta final. Prueba corta, incidencia intermitente no cerrada.
- Nuevo grupo abre selector de miembros; Nuevo canal abre formulario. Sin crear nada. Ayuda abre Preguntas frecuentes telegram.org. Privacidad no abrió de forma comprobable en esta pasada; Condiciones pendiente. No se considera auditado todo el flujo.
- APK instalado sin cambios de código: MDGram-header-theme.apk. Keystore/properties ignorados por Git. Evidencia y siguientes requisitos en RELEASE_READINESS.md.

## Enlaces y traducción real — 12 de octubre de 2026

- Condiciones de servicio abrió Terms of Service en telegram.org: terms-after.xml. Privacidad recibe el toque, pero SurfaceFlinger vuelve a abortar al capturar pantalla; privacy-crash.log registra captureScreenCommon: Buffer failed to allocate: 2. No se da Privacidad por validada ni la estabilidad por resuelta.
- Traducción manual real en Mensajes guardados: Bonjour → Buen día, Francés → Español. Evidencia translation-french-menu.xml, translation-french-result.xml y translation-result-current.png. Resultado recibido y mostrado en TranslateAlert2.
- Configuración previa excluye en/es. Por ello Good morning no ofrecía Traducir: comportamiento coherente con las exclusiones, no defecto confirmado. Se probó francés conservándolas. Mostrar botón Traducir se alternó durante diagnóstico y se restauró a ON, su valor inicial.
- Se enviaron sólo dos textos de prueba a Mensajes guardados: Good morning y Bonjour. Permanecen allí; no hubo envíos a terceros. La entrada ADB perdía caracteres; MEMUC input proporcionó texto correcto antes del envío.
- No se valida aún formato complejo, todos los proveedores, automática, chats enteros, tipo o Mantener Markdown. No se cambió código ni se generó otro APK. Publicación final sigue pendiente.

## Controles del traductor — 12 de octubre de 2026

- No traducir abre el selector real de idiomas excluidos y resume sus valores. Comprobado en MEmu; se conservaron inglés y español.
- Mantener Markdown está conectado al motor Google/Yandex mediante translator_keep_formatting, ON por defecto. OFF solicita y devuelve texto sin entidades. Telegram administra su propio formato; el control se deshabilita para ese proveedor.
- Prueba en Mensajes guardados: un nuevo Bonjour en negrita. OFF devolvió Buen día sin negrita. Se restauró ON. La primera prueba ON reveló que Google eliminaba las etiquetas al recibir format=html; se corrigió la petición. Sondas posteriores conservan negritas, cursivas y anidamiento (google-format-probe.txt).
- Selector: exclusions-selector-final.xml; OFF: formatting-off.xml y formatting-off-result.png. No se modificaron idiomas excluidos ni se enviaron mensajes a terceros.
- Chats enteros, tipo y traducción automática siguen pendientes. Yandex y formato complejo completo no se consideran validados. Actualizar One UI permanece intacto. Publicación final pendiente de las comprobaciones de RELEASE_READINESS.md.

### Verificación del APK corregido

- BUILD SUCCESSFUL en 4m38s: .test-artifacts/build-translator-format-fix.log. Firma verificada con apksigner; instalación incremental Success, sesión conservada.
- APK: .test-artifacts/MDGram-translator-format.apk.
- Prueba visual real con Google en Mensajes guardados: Bonjour en negrita → Hola en negrita con Mantener Markdown ON. Captura format-corrected-result.png. OFF anterior produjo Buen día sin negrita. La eliminación de format=html corrigió la pérdida del formato.
- Diferencia de texto entre resultados del proveedor registrada; no se exige equivalencia literal de ambas respuestas. Exclusions en/es intactas; formato ON restaurado.
- Yandex, enlaces/spoilers y formato complejo completo siguen pendientes de validación en el dispositivo. La estabilidad intermitente de MEmu y Privacidad siguen abiertas; no se publica una versión final en este bloque.

## Traducción de chats y proveedores — 12 de octubre de 2026

- Traducir chats enteros deja de ser visual: usa isChatTranslateEnabled/setChatTranslateEnabled y notifica updateSearchSettings. Mostrar botón Traducir también envía esa notificación, como LanguageSelectActivity.
- Se respeta la disponibilidad del servidor y Premium. Cuenta no Premium: abre PremiumFeatureBottomSheet de traducción en tiempo real y permanece OFF. No se pulsa Suscribirme ni se realiza compra. Evidencia whole-control-premium.xml y whole-control-after.xml.
- La traducción completa nativa excluye Mensajes guardados. Puede aparecer una promoción de traducción en ese chat; ver ChatActivity.showTranslate y dialog_show_translate_count. La promoción no demuestra traducción completa. No se prueban otros chats por la restricción del usuario; recorrido con Premium permanece pendiente.
- Yandex: el mismo Bonjour en negrita devuelve Buenos días en negrita en Mensajes guardados (yandex-test-result.png). La respuesta del motor propio no tiene fallback entre proveedores en TranslateAlert2; confirma respuesta de Yandex en esta prueba concreta.
- Telegram: Mantener Markdown deshabilitado en su contenedor (telegram-format-disabled.xml, parent enabled=false), con explicación de que Telegram administra formato. No se valida traducción real con Telegram en este bloque.
- Google restaurado y Mantener Markdown ON (providers-restored.xml). Sin nuevos mensajes, llamadas, cambios de sesiones ni envíos a terceros. Actualizar One UI intacto.
- Tipo de traducción y Traducir automáticamente continúan pendientes. El nuevo control no levanta las restricciones de traducción nativa ni resuelve la estabilidad intermitente de MEmu.

APK final de este bloque: .test-artifacts/MDGram-whole-chat-control.apk. BUILD SUCCESSFUL en 4m58s (build-whole-chat-final.log), firma verificada, adb install -r -d Success. Ajustes tras actualización: whole-control-final.xml; Google seleccionado, Mantener Markdown ON, chats enteros OFF. Sesión conservada.

## Traducción por chat sin Premium — 12 de octubre de 2026

- Google/Yandex permiten la función de traducción del chat sin Premium; Telegram conserva sus requisitos. El interruptor habilita la barra, la traducción sólo se activa por chat y mantiene el original. Chats cifrados excluidos.
- Primera versión: texto y pies de foto; no encuestas, artículos, resúmenes ni transcripciones. Detección previa de idioma; se omiten idiomas excluidos, idioma destino e idioma desconocido. En Mensajes guardados se incluyen textos propios.
- Fallos del motor propio no hacen fallback silencioso a Telegram. Al apagar el chat o cambiar destino se descartan resultados de texto que llegan después; peticiones HTTP ya iniciadas no se abortan físicamente.
- Primer APK compilado en 8m35s, firma verificada e instalación incremental Success (.test-artifacts/build-chat-without-premium.log). Comprobado: interruptor ON sin Premium, barra en Mensajes guardados, Bonjour → Buen día y Bonjour en negrita → Hola en negrita; Good morning se conserva por exclusión en. Evidencia chat-free-enabled.xml y chat-free-translated.png/xml.
- Un mensaje nuevo de prueba **Bonsoir mon ami** se envió sólo a Mensajes guardados. Tradujo a Buenas noches amigo al reabrir (chat-free-reopen.xml); no se probó recepción de terceros. OFF recupera los originales (chat-free-off-check.xml).
- La primera captura tras instalar falló; se reinició MEmu mediante memuc sin borrar datos. Las capturas posteriores funcionaron. Estabilidad general sigue pendiente.
- Se corrige también la carga del estado guardado para chats con idioma detectado excluido/desconocido y se solicita revisión de mensajes visibles tras recepción/confirmación de envío. Validación del APK final pendiente al registrar este bloque.

### Cierre de la validación sin Premium

- APK final .test-artifacts/MDGram-chat-without-premium.apk, BUILD SUCCESSFUL en 6m44s (build-chat-without-premium-final.log); firma apksigner verificada, instalación incremental Success.
- Google: Bonne soirée → Buenas noches sin salir del chat (free-live-result.xml). Activación y traducciones conservadas tras force-stop/reabrir la app (free-persisted.xml). OFF devuelve Bonne soirée y Bonsoir mon ami (free-final-original.xml).
- Yandex: nuevo Merci beaucoup → Muchas gracias con negrita sin salir del chat (free-yandex-live.xml/png). OFF devuelve Merci beaucoup (free-yandex-original.xml). Usa mensaje nuevo para evitar caché del proveedor anterior.
- Estado final: Google, Mantener Markdown ON, barra de chats habilitada, traducción de Mensajes guardados OFF. free-final-settings.xml. Sólo tres nuevos mensajes de prueba en este bloque: Bonsoir mon ami, Bonne soirée, Merci beaucoup, todos en Mensajes guardados.
- Límite de la infraestructura reutilizada: comprueba mensajes visibles y una ventana cercana de hasta 7 IDs; también puede incluir el mensaje fijado y referencias de respuesta. No carga ni traduce toda la historia de una vez. Peticiones HTTP en curso pueden terminar después de OFF; sus resultados de texto se descartan.
- Pendientes: etiqueta de idioma en barra para chats multilingües (puede mostrar inglés por la detección previa aunque traduzca francés), cambio de proveedor con traducciones ya almacenadas en caché, medios especiales, fallos/red y cancelación durante una petición, estilos/tipo y política automática global. Pruebas en grupos/canales y recepción de terceros no realizadas. Hubo navegación accidental a un canal durante el retorno; no se activó allí la traducción ni se enviaron mensajes.
- Actualizar One UI intacto. No se publica versión final: límites del traductor y estabilidad de MEmu siguen documentados.

## Caché, etiquetas y estabilidad — 12 de octubre de 2026

- Traducciones de texto llevan mdgramTranslationKey local (proveedor y conservación de formato). Persistida mediante FLAG_14 al final de MessageCustomParamsHelper, copiada en los recorridos de mensajes y validada antes de reutilizar el resultado. No se envía a Telegram. Datos anteriores se leen sin ese campo; Google/Yandex los renuevan una vez. Las cachés heredadas sin clave se conservan cuando se usa Telegram, porque no se conoce su origen; pendiente migración más amplia si se necesita ese proveedor.
- Respuestas pendientes con proveedor/formato diferente al actual se descartan. La traducción propia evita preprocess de Telegram para respetar formato OFF. Cambiar proveedor o Markdown no borra originales ni sesiones.
- Google/Yandex muestran Mostrar original sin atribuir idioma único a chats mixtos. Se suprime la exclusión rápida basada en ese idioma ambiguo del menú propio; sigue disponible No traducir en Ajustes.
- Mismo Bonjour en negrita: Google Hola (cache-google.xml); cambiar a Yandex devuelve Buenos días (cache-yandex.xml/png). Formato OFF quita negrita (cache-format-off.png), ON restaurado y Google seleccionado (cache-restored-settings.xml, cache-google-restored.xml). Sin nuevos mensajes en este bloque.
- Persistencia: seis registros de texto contienen mdgram-v1:0:true en custom_params tras detener la app (cache-persistence-counts.txt). Reabrir conserva Hola y Mostrar original (cache-persisted.xml). Prueba offline por UID no disponible: iptables no tiene owner; no se creó regla. No se afirma prueba completa sin conexión.
- APK .test-artifacts/MDGram-translation-cache.apk: BUILD SUCCESSFUL 6m14s, firma apksigner verificada e instalación incremental Success. build-translation-cache.log. Código compilado revisado con javap para comprobación de invalidación.
- Prueba acotada de 15,9 minutos: caché/proveedores/formato/reinicio, ocho cambios de tema con ocho capturas, cuatro alternancias con el menú flotante conservado, recorridos Ajustes/Traducir/Actualizar y apertura de Privacidad. SurfaceFlinger mantuvo PID193; app mantuvo PID5229 durante la ronda posterior al reinicio intencional. Buffer crash final vacío. stability-cache-themes.txt, stability-cache-modal.txt, stability-cache-duration.txt y stability-cache-final-crash.log. No prueba reparación de la incidencia intermitente previa del emulador.
- Privacidad validada: Telegram Privacy Policy abrió y captura correcta (stability-cache-privacy.xml/png); retorno al menú correcto. Actualizar One UI intacto. Estado final Google, Markdown ON, traducción de Mensajes guardados OFF, barra habilitada.
- Copias temporales de base de datos en .test-artifacts/cache-db-check excluidas de Git; la eliminación fue rechazada por la revisión automática sin motivo concreto. No publicarlas ni adjuntarlas. Sólo conteos de claves guardados en el informe, sin contenido de mensajes.
- Quedan pruebas de errores/red/cancelación, medios especiales y recorridos fuera de Mensajes guardados; Tipo y política automática global siguen pendientes. No se publicó en GitHub.

## 12-10-2026 — Red, cancelación y controles de traducción

- Tipo de traducción conectado a Ventana flotante / Dentro del mensaje para Google y Yandex; Telegram conserva su recorrido nativo. Traducción inline conserva el original, formato y estado local mediante FLAG_15, sin modificar el texto enviado a Telegram.
- Política automática conectada a Nunca / Al abrir chats, con explicación de envío de textos visibles/cercanos al proveedor. Respeta idiomas excluidos y desactivación manual por chat. Por defecto Nunca.
- Cancelación propia con MDGramTranslationRequest: desconecta HTTP activo, impide iniciar solicitudes canceladas y descarta resultados pendientes en UI. Proveedor/formato capturados al encolar. Error de proveedor en chats enteros apaga la traducción y muestra aviso, evitando reintentos continuos.
- Prueba Java de cancelación PASS: en cola, conexión activa, idempotencia y detach antiguo. Evidencia cancellation-test.log. No sustituye la validación Android.
- Primera compilación firmada e instalada sin perder sesión, 7m02s (build-network-controls.log). Selectores y traducción inline Merci beaucoup -> Muchas gracias comprobados en Mensajes guardados. Política automática ON respeta el chat manualmente OFF. Se detectó sombra de menú persistente al traducir inline; cierre corregido y APK final en recompilación. Pruebas de retorno/red/cancelación Android aún pendientes a este punto.
- Actualizar One UI no modificado. Ningún mensaje nuevo ni envío a terceros en este bloque.

### Resultado del APK final — red/cancelación y controles

- APK final .test-artifacts/MDGram-network-controls.apk; BUILD SUCCESSFUL en 4m33s (build-network-controls-verified.log), firma apksigner verificada e instalación incremental Success. Correcciones adicionales: menú inline cierra la sombra y ofrece Mostrar original aun traducido/pendiente; TranslateAlert2 cancela HTTP al cerrar/cambiar idioma y muestra errores propios, sin dejar la ventana esperando indefinidamente.
- Prueba HTTP real local PASS (http-cancellation-test.log): lectura bloqueada liberada al cancelar, además del harness de cuatro casos previo.
- MEmu, sólo Mensajes guardados y sin nuevos envíos: inline Merci beaucoup -> Muchas gracias; persistencia tras reinicio/reinstalación; Mostrar original restaura Merci. Menú vuelve a abrir normalmente. Fondo blanco comprobado después del cierre, sin sombra persistente.
- Inline bajo REJECT de HTTPS: original conservado y Traducir vuelve a estar disponible. Bajo DROP, menú Mostrar original disponible durante la petición; cancelación conserva original también al restaurar la red. Petición posterior online traduce correctamente (net-cancel-pending-menu2, net-cancel-after-network, net-recovery-translated/original).
- Ventana flotante bajo DROP: cierre devuelve al chat y no muestra respuesta tardía (net-window-pending/dismissed/after-network). Bajo REJECT: ventana se cierra y aparece Traducción fallida (net-window-error-pass2). Recuperación online Muchas gracias; cerrar deja original (net-window-online1/closed).
- Chat completo: bajo REJECT pasó de Mostrar original a Traducir al español sin traducir el texto, y permaneció apagado mientras se mantenía el bloqueo (net-batch-final-error1..5). Bajo DROP se apagó manualmente y conservó original después de restaurar red (net-batch-cancel-pending/off/after). Recuperación online tradujo Muchas gracias; OFF final devuelve Merci (net-batch-online1, net-final-saved-original).
- Reglas temporales HTTPS eliminadas en finally; OUTPUT restaurado (network-rules-restored.txt). Crash buffer vacío al terminar; app PID2997, SurfaceFlinger PID194. Pasada acotada: no demuestra resolver los fallos históricos intermitentes de MEmu.
- Preferencias de tipo/automática persistieron tras reinicio: Dentro del mensaje / Al abrir chats. Automática respetó Saved manualmente OFF; no se abrieron otros chats con esa política. Restaurados Ventana flotante / Nunca, Google / Markdown ON, Saved OFF. Falta comprobar activación automática en un chat sin override y cambio de proveedor durante una petición; no se consideran validados.
- Actualizar One UI intacto. No publicado en GitHub.

## 13-10-2026 — Fondo inferior de navegación plana

- MainTabsActivity cubre con el fondo del tema el espacio entre el borde inferior de las pestañas planas y el borde de la ventana. Se dibuja después del pager para que las filas no se transparenten por el inset de navegación del sistema. Botones mantienen su zona segura; cápsula y Actualizar One UI intactos.
- APK .test-artifacts/MDGram-flat-navigation-inset.apk: compilación correcta 8m15s (build-flat-navigation-inset.log), firma verificada e instalación incremental Success conservando sesión.
- MEmu, estilo plano: franja y=1880..1920 (1080x1920) uniforme en todos sus 43200 píxeles: claro RGB255/255/255, oscuro RGB32/46/61. Evidencia flat-chats-light.png / flat-chats-dark.png. Crash buffer vacío en esta pasada. Restaurado tema claro y la navegación nueva previamente activada.
- MEmu sólo tiene navegación de sistema modo0; no dispone del overlay gestural. No se modificó ese modo. Falta confirmar en Galaxy A36 5G con su inset real de One UI; no se presenta la prueba del emulador como equivalente a un teléfono físico.

### 13-10-2026 — Altura excesiva de la barra plana corregida

- La captura del Galaxy A36 mostró que cubrir el inset no corregía el contenedor demasiado alto. Causa confirmada: MAIN_TABS_MARGIN = dp(6), combinado después con alturas en dp y convertido otra vez por dp()/LayoutHelper. El tamaño dependía incorrectamente de la densidad.
- Constante corregida a 6 dp; MAIN_TABS_HEIGHT_WITH_MARGINS queda 68 dp, con una sola conversión en sus consumidores. Se revisaron todos los usos del margen: dp() y LayoutHelper reciben dp. No se elimina el inset seguro de Android.
- APK .test-artifacts/MDGram-flat-navigation-height.apk; compilación 4m42s, firma verificada e instalación incremental Success. En MEmu, etiquetas antes y=1804..1837 y después y=1840..1873: bajan 36 px. Capturas height-flat-light/dark.png. Franja inferior uniforme blanca/oscura; navegación a Ajustes y retorno, claro/oscuro y restauración de estilo previo comprobados. Crash buffer vacío. Actualizar One UI intacto.
- Falta confirmar la nueva altura en Galaxy A36/One UI; MEmu no ofrece navegación por gestos. El APK de esta corrección sustituye al de sólo fondo/inset.

## 13-10-2026 — Traducción y candidata de publicación

- Usuario confirmó barra plana en Galaxy A36 y autorizó pruebas físicas sólo en Saved. Informe completo RELEASE_VERIFICATION_2026-10-13.md.
- Cerrados proveedor durante petición y automática sin override; preferencias/rules restauradas. Galaxy: formatos comunes, spoiler, traducción manual/inline y foto con caption. Dos nuevos mensajes de prueba en Saved, sin terceros.
- Corregidos spoiler visible en ventana (SpoilersTextView), language=null del bloque Pre (vacío válido) y estado inline de primera petición sin caché cuando cambia proveedor/original. Pruebas de formato PASS, última guardia probada en MEmu.
- Ronda física 14m18s, 24 cambios de tema/12 retornos de segundo plano, mismo PID, crash filtrado vacío y memoria final menor. El último APK añade la guardia de inline y quedó instalado; no se repitió todo el soak físico tras esa última instalación porque se bloqueó el teléfono.
- APK candidato .test-artifacts/MDGram-release-stability.apk, firma verificada, build4m44s, SHA FF59E831CEAF5A84211B35182B0F2B5A788E88F654CD8EA0528E9DBFEE8DCB1A. Actualizar One UI intacto.
- Notas y version.json actualizados; automatización heredada GitLab retirada. Canal vivo sigue V5/code7, coherente con GitHub. No se publicó ni se hizo commit/push.

- Preparación de fuentes: quitadas de .gitignore las exclusiones explícitas de Java/Kotlin de Cherrygram que ahora son fuentes locales necesarias. SESSION_NOTES.md se conserva sólo local, por su contexto de credenciales; documentación pública curada en PROJECT_STATUS/RELEASE_NOTES. No se alteró contenido de archivos fuente con este ajuste ni se realizó staging.

## 13-10-2026 — Verificación fuera de Saved y APK definitivo

- Autorización ampliada a Galaxy y otros chats. Canal público/traducción, comentarios vinculados, foro, historial de grupo, pestañas y recepción de notificación real comprobados sin enviar a terceros.
- APK definitivo FF59E831… instalado verificado por hash; mismo PID13450 durante la pasada. Detalles y límites en RELEASE_VERIFICATION_2026-10-13.md.
- Recomendación actual: V5.1 estable sin beta, con alcance explícito en RELEASE_READINESS.md. Sustituye la recomendación previa de candidata/beta; aún no publicada ni subida a GitHub.

## 13-10-2026 — Publicación V5.1 estable

- Release https://github.com/jair1c/MDGram/releases/tag/V5.1 publicada sin draft ni prerelease. APK MDGram-universal.apk, 57537694 bytes, y SHA256SUMS.txt.
- Código de la versión: snapshot 2b19922018f829c8138d68d4070b0492cf4e055d en codex/release-v5.1 y tag V5.1. Snapshot sobre el historial de main porque el clon de Cherrygram era superficial; no hubo force-push. Conservadas licencias/créditos. Extra.kt público tiene marcadores APP_ID=0 y APP_HASH vacío; configuración privada conservada sólo local.
- Descarga pública GitHub y descarga a través de /dl: SHA256 FF59E831CEAF5A84211B35182B0F2B5A788E88F654CD8EA0528E9DBFEE8DCB1A, idéntico al APK probado. apksigner verifica firma v2; paquete com.mdgram.messenger, code70389, base12.10.1, minSDK24, target36, ABI arm64-v8a/armeabi-v7a.
- Manifest main/version.json y https://mdgram-web.vercel.app/api/latest confirmados V5.1/code8. README principal anuncia versión disponible y advierte instalación nueva/cambio de paquete. Fuente migrada disponible mediante su enlace; main conserva la base anterior.
- Detectados enlaces /dl/arm64 y /dl/armv7a que esperan assets con esos nombres. Se publican copias idénticas del APK universal, etiquetadas y documentadas como compatibilidad web; no variantes reducidas. Conservan la firma y el APK exacto probado.
