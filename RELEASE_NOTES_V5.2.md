# MDGram V5.2 — Liquid Glass opcional

- Liquid Glass en navegación, cabeceras de chats e inicio, historias, controles de escritura, emojis, adjuntos y vista previa de ajustes.
- Estilos plano y píldora independientes del efecto de vidrio; se conservan los colores e identidad de MDGram.
- Mejor contraste del acento azul en el tema oscuro integrado; los chats silenciados conservan su distinción visual.
- Cabecera de inicio con segmentos separados, título centrado y espacio entre cabecera e historias.
- Avión de envío negro en claro y blanco en oscuro, con fondo transparente.
- Aviso de cierre con ilustración animada original y textos MDGram. Su fondo de vidrio conserva la posición al desplazar el contenido y se actualiza al cambiar de tamaño.
- La pantalla Actualizar conserva su diseño One UI.

## Activación y compatibilidad

Liquid Glass está desactivado por defecto. Se activa en Ajustes de MDGram → Pantalla principal → Liquid Glass. La opción Interfaz de píldora cambia las formas de la interfaz por separado.

El APK universal contiene ARM32 y ARM64 y requiere Android 7 o posterior. El desenfoque moderno necesita Android 12 y la refracción Android 13; en versiones anteriores se usa una apariencia compatible. El ahorro de energía limita la lente. El efecto requiere más recursos de dibujo que el modo normal; puede desactivarse.

## Instalación

V5.2 mantiene el paquete `com.mdgram.messenger` y la firma de V5.1; puede instalarse encima conservando los datos. Para usuarios de V5 y anteriores (`com.mdgram.messenger.web`), se instala como otra aplicación: es necesario iniciar sesión de nuevo y comprobar los chats antes de retirar la anterior. No se promete migración de chats secretos o archivos locales.

Versión del proyecto V5.2/código9; base Android12.10.1/código70399. Las descargas arm64 y armv7a son copias de compatibilidad del mismo APK universal, no variantes reducidas.

## Validación

Revisión física en Galaxy A36: estilos plano/píldora, Glass ON/OFF, claro/oscuro, teclado, emojis, adjuntos, búsqueda, ajustes, rotación y retornos desde segundo plano. Se corrigió un defecto del vidrio al desplazar el aviso de cierre. El diálogo se abrió mediante una herramienta de prueba separada, sin provocar fallos ni enviar registros; la herramienta fue retirada.

No aparecieron cierres nuevos en la pasada final. Las pruebas son acotadas: no constituyen un ensayo prolongado de batería ni garantizan todos los fabricantes o recorridos Premium. Ver [informe de validación](LIQUID_GLASS_VALIDATION.md).
