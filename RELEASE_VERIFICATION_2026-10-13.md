# Verificación de MDGram — 13 de octubre de 2026

## Artefacto candidato

- APK: .test-artifacts/MDGram-release-stability.apk.
- SHA-256: FF59E831CEAF5A84211B35182B0F2B5A788E88F654CD8EA0528E9DBFEE8DCB1A.
- Compilación final: 4m44s, build-release-inline-final.log. Firma apksigner verificada; instalado con Success en MEmu y Galaxy A36, conservando datos.
- Paquete com.mdgram.messenger; Android versionCode 70389, versionName 12.10.1; proyecto MDGram V5.1/code8. ARM32/ARM64, Android7+.

## Casos cerrados

1. Barra plana: usuario confirmó en Galaxy A36 con gestos la altura/fondo corregidos. La evidencia anterior queda sustituida por su captura final.
2. Cambio Google->Yandex durante petición bloqueada: se conservó el original y una petición posterior con Yandex funcionó. Red MEmu restaurada en finally.
3. Automática en Saved sin override previo: configuración temporal sólo del único override de Saved; al abrir, Yandex tradujo Bonjour, Bonsoir y Merci sin tocar la barra. Inglés excluido. Se restauró exactamente el override previo y Nunca/Google/Ventana flotante.
4. Galaxy: un mensaje nuevo de prueba con negrita/cursiva/tachado/spoiler/enlace. Google tradujo en ventana y dentro del mensaje; originales conservados. No se pulsó el enlace externo ni se considera validada su navegación.
5. Defecto de spoiler en la ventana reproducido y corregido usando SpoilersTextView. Comprobado oculto y revelado por toque en Galaxy; inline ya lo ocultaba correctamente. Fotos de evidencia recortadas no contienen chats privados.
6. Prueba de conversión sobre el código real de MDGramTranslator, con tipos externos mínimos: superposición de entidades, rangos UTF16 de emoji, URL escapada, spoiler, code/pre. Falló inicialmente por language=null en Pre; corregido a cadena vacía. PASS en format-unit/result.log. No equivale a una prueba física de bloques de código.
7. Foto generada MDGRAM QA PHOTO enviada sólo a Saved con Bonjour mon ami. Ventana devolvió Hola mi amigo, cerró y dejó imagen/caption original. Sin envíos a terceros. Archivo público de prueba en Pictures/MDGramQA del teléfono y los dos mensajes de QA se conservaron.
8. Caso de primera traducción sin caché: revisión detectó que el cambio de proveedor podía dejar inline marcado. Callback ahora limpia/persiste/notifica al cambiar ajustes u original. APK final probado en MEmu con caption recién recibido sin caché: DROP activo, cambio a Yandex, original y menú Traducir recuperados; nueva traducción y Mostrar original correctos.

## Estabilidad y límites de evidencia

- Galaxy A36 ARM64: ocho cambios de tema con capturas en la primera pasada, cuatro más en el APK con spoilers/pre y después 12 rondas (24 cambios de tema y 12 retornos de segundo plano). PID5291 conservado durante 14m18s; crash buffer filtrado por ese PID vacío. PSS de 228085 a 219756KiB; RSS de 301988 a 256376KiB. No demuestra ausencia de toda fuga o crash futuro.
- Esa ronda usó el APK conservado como MDGram-release-soak-verified.apk (SHA8FF2087FD677021CBA8ACB40682102FFE7CAC06E78C35669AE9176CB9A078274). El APK definitivo agrega únicamente la limpieza de inline sin caché; validada después en MEmu e instalada en Galaxy. No hubo una nueva ronda completa física después de esa última instalación: el teléfono se bloqueó automáticamente.
- MEmu volvió a fallar en SurfaceFlinger: captureScreenCommon: Buffer failed to allocate:2 (release-stability-crash.log). No se atribuye causa a MDGram ni se anuncia reparación del emulador. La VM se recuperó y la prueba final no dependió de capturas gráficas.
- Pruebas sólo en Saved; no recepción de terceros, grupos/canales, llamadas o cuenta Premium. Artículos, encuestas, resúmenes/transcripciones no se habilitan en traducción completa propia.

## Preparación de publicación

- RELEASE_NOTES_V5.1.md actualizado con cambios, nuevo paquete y límites. version.json conserva code8/V5.1 y reemplaza el anuncio de servicio ilimitado por un alcance comprobable.
- Repositorio origin apunta a jair1c/MDGram. Retirada la automatización heredada mirror.yml, que copiaba Cherrygram a GitLab ajeno al proyecto al subir main. No se publicó ni se hizo push/commit en este bloque.
- Canal /api/latest y GitHub latest coinciden actualmente en V5/code7; los assets publicados son de V5. No anunciar V5.1 antes de subir su APK y confirmar que el canal descarga ese asset y conserva paquete/firma.
- Propiedades de firma y diagnósticos .test-artifacts ignorados por Git. release.keystore del upstream no está modificado. Publicar código correspondiente al APK junto con notas y asset; no bases de datos, capturas privadas o propiedades de firma.
- Preparación de fuentes: quitadas de .gitignore las exclusiones explícitas de Java/Kotlin de Cherrygram que ahora son fuentes locales necesarias. SESSION_NOTES.md se conserva sólo local, por su contexto de credenciales; documentación pública curada en PROJECT_STATUS/RELEASE_NOTES. No se alteró contenido de archivos fuente con este ajuste ni se realizó staging.

## 13-10-2026 — Ampliación física para publicación estable

Esta pasada sustituye el límite anterior de pruebas sólo en Saved y la falta de repetición física tras la última corrección. El usuario autorizó pruebas fuera de Saved en su Galaxy A36.

- Se comprobó SHA-256 del base.apk instalado: idéntico al APK definitivo FF59E831CEAF5A84211B35182B0F2B5A788E88F654CD8EA0528E9DBFEE8DCB1A.
- Canal público Amr Gamal Store (Cloud), enlace t.me/AMRGAMAL_STORE verificado: apertura, desplazamiento de historial, traducción de publicación inglesa a español en ventana y original conservado al cerrar.
- Sección de 31 comentarios vinculados: respuestas e historial; foro Communication Chat de InstaPrime y otro grupo abierto sin enviar mensajes, reaccionar o modificar membresías.
- Contactos, lista de llamadas, Chats y Ajustes: apertura y retorno correctos. No se realizaron llamadas.
- Recepción real en primer plano: apareció una notificación de canal y se actualizaron vista previa y contador. Evidencia local stable-dark.png, capturada durante la llegada; contiene conversaciones privadas y no debe publicarse.
- Seis rondas de dos pulsaciones de tema y retorno desde segundo plano; PID13450 conservado. Se verifican además colores finales antes de cerrar la pasada.
- Recepción dentro de grupo comprobada: en la octava muestra apareció una fila nueva al final (11:17), conservando tres filas anteriores y desplazando la más antigua. Sin envío de QA; evidencia stable-group-receive-before.xml / stable-group-receive-8.xml y huellas SHA locales. En el foro anterior no llegaron mensajes durante la observación.
- Las capturas/dumps/huellas quedan sólo en .test-artifacts, ignorado por Git. No hubo envíos a terceros ni cambios de preferencias de traducción.

Recomendación: publicar V5.1 como estable, sin beta, para el alcance comprobado. Esto no equivale a validar todos los recorridos de Telegram. Llamadas reales, funciones Premium y medios especiales restantes no ejercitados. Firma/paquete/canal de descarga deben verificarse después de subir el asset definitivo.

Resultado final de esta pasada: 17,7 minutos desde el primer dump del APK definitivo, PID13450 conservado, crash buffer filtrado vacío (0 líneas). Capturas stable-theme1/2 confirman oscuro/claro e iconos sol/luna; tema claro restaurado. Seis retornos de segundo plano y 14 pulsaciones de tema en total, además de la navegación descrita. La muestra PSS final fue 315728 KiB/RSS354368 KiB tras cargar grupos e imágenes; la anterior fue 254623/203512 KiB, con swap128717 KiB. Las cargas/swap cambiaron: estos números no permiten afirmar ausencia de fugas. No hubo cierre ni reinicio observado. Hash del APK instalado verificado nuevamente al finalizar.

## 13-10-2026 — Publicación V5.1 estable

- Release https://github.com/jair1c/MDGram/releases/tag/V5.1 publicada sin draft ni prerelease. APK MDGram-universal.apk, 57537694 bytes, y SHA256SUMS.txt.
- Código de la versión: snapshot 2b19922018f829c8138d68d4070b0492cf4e055d en codex/release-v5.1 y tag V5.1. Snapshot sobre el historial de main porque el clon de Cherrygram era superficial; no hubo force-push. Conservadas licencias/créditos. Extra.kt público tiene marcadores APP_ID=0 y APP_HASH vacío; configuración privada conservada sólo local.
- Descarga pública GitHub y descarga a través de /dl: SHA256 FF59E831CEAF5A84211B35182B0F2B5A788E88F654CD8EA0528E9DBFEE8DCB1A, idéntico al APK probado. apksigner verifica firma v2; paquete com.mdgram.messenger, code70389, base12.10.1, minSDK24, target36, ABI arm64-v8a/armeabi-v7a.
- Manifest main/version.json y https://mdgram-web.vercel.app/api/latest confirmados V5.1/code8. README principal anuncia versión disponible y advierte instalación nueva/cambio de paquete. Fuente migrada disponible mediante su enlace; main conserva la base anterior.
- Detectados enlaces /dl/arm64 y /dl/armv7a que esperan assets con esos nombres. Se publican copias idénticas del APK universal, etiquetadas y documentadas como compatibilidad web; no variantes reducidas. Conservan la firma y el APK exacto probado.
