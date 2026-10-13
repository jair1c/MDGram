# MDGram V5.1 — notas preparadas para publicación

## Cambio de paquete: instalación nueva necesaria

V5.1 usa `com.mdgram.messenger` en lugar de `com.mdgram.messenger.web`. Android la instala como una aplicación distinta. V5 y anteriores pueden permanecer instaladas; su sesión, ajustes y archivos locales no se transfieren automáticamente.

Instala V5.1, inicia sesión y comprueba tus datos antes de quitar la versión anterior. Los chats que Telegram guarda en la nube se recuperan al iniciar sesión; no se promete migración de chats secretos o datos locales.

## Cambios

- Identidad e iconos de MDGram, interfaz clásica y nueva navegación con interruptor.
- Barra plana con altura corregida y fondo hasta la zona de navegación de Android; comprobada en Galaxy A36.
- Menú flotante que permanece abierto al alternar claro/oscuro; contador inferior rosado con números blancos.
- Google y Yandex para traducción manual y textos de chats sin Premium. Tipo: ventana flotante o dentro del mensaje. Automática: Nunca o Al abrir chats, respetando exclusiones y desactivaciones manuales.
- Cancelación de solicitudes y recuperación ante errores de red; caché distinguida por proveedor y conservación del formato.
- Formato conservado, spoilers ocultos en la ventana hasta tocarlos y bloques de código con metadatos válidos para guardarse en caché.
- Pantalla Actualizar conserva su diseño One UI.

## Alcance y límites

Se comprobó en Galaxy A36 el APK definitivo: Mensajes guardados, canal público y traducción, comentarios vinculados, foro, historial de grupo, recepción de notificación, navegación y retornos desde segundo plano. La cuenta no es Premium. No se realizaron llamadas, envíos a terceros ni pruebas de traducción nativa con Premium o de todos los medios especiales. La traducción completa propia cubre texto y pies de foto; omite encuestas, artículos, resúmenes y transcripciones. Google/Yandex pueden imponer límites o fallar; no se promete servicio ilimitado. La traducción envía el texto elegido al proveedor.

APK ARM de 32 y 64 bits, Android 7 o posterior. El número de base mostrado por Android es 12.10.1; la versión del proyecto es V5.1.

## Publicación

Publicar sólo el APK firmado final con estas notas, tras cerrar las comprobaciones de RELEASE_READINESS.md. Actualizar el anuncio del canal cuando exista el asset de V5.1; actualmente la release pública sigue en V5. No adjuntar capturas privadas, bases de datos, propiedades de firma ni diagnósticos.
