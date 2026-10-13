# Auditoría visual MDGram

Fecha de inicio: 16 de septiembre de 2026

## Referencias permitidas en esta etapa

- Original: `APK/MDGram_V21_Universal.apk` (`app.mdgram.android`).
- Migración actual: `com.mdgram.messenger`.
- `APK/MDGram.apk` (`com.mdgram.messenger.web`) queda fuera de la comparación hasta indicación expresa.

## Orden de trabajo

1. Igualar estructura, colores, iconos, tipografía y espaciado.
2. Validar cada pantalla en MEmu con capturas equivalentes.
3. Incorporar y probar las funciones una por una después de cerrar la fase visual.

## Matriz de pantallas

| Pantalla | Referencia V21 | Estado de la migración |
| --- | --- | --- |
| Bienvenida posterior al primer login | `.test-artifacts/reference-v21/first-login-reference.png` | Diálogo V21 incorporado y validado visualmente. Los enlaces se activarán en la fase funcional. |
| Inicio / lista de chats | `.test-artifacts/ui-audit/original/home-clean.png` | Estructura V21 validada en `.test-artifacts/ui-audit/current/home-v21-final.png`: cuatro pestañas a ancho completo, buscador, iconos clásicos, hamburguesa y cabecera compacta de historias sin el wordmark de Cherrygram. |
| Ajustes | `.test-artifacts/ui-audit/original/settings.png` | Composición V21 portada y validada en `.test-artifacts/ui-audit/current/settings-v21-final.png`: perfil compacto, cuenta, teléfono, usuario, biografía, accesos MDGram y filas planas. |
| Ajustes de MDGram | `.test-artifacts/ui-audit/original/mdgram-settings-hub-next.png` | Hub V21 ajustado y verificado en `.test-artifacts/ui-audit/current/mdgram-settings-hub-v21-final.png`: fondo blanco, cabecera sin título duplicado, tarjetas gris `#F4F4F4`, iconos y espaciados alineados. Sus seis rutas principales abren y regresan en MEmu. |
| General | `.test-artifacts/ui-audit/original/general-next.png` | Estructura V21 portada y comprobada en `.test-artifacts/ui-audit/current/general-v21-final.png`: cinco iconos MDGram en una fila, perfil compacto, opciones de foto y teléfono, fuentes y acelerador de tres niveles. El icono Material You conserva el PNG rosa extraído de V21; falta reproducir el tinte dinámico azul que muestra la referencia en este emulador. |
| Chats (ajustes de conversación) | `.test-artifacts/ui-audit/original/conversation-v21-top.png`, `conversation-v21-lower.png` y `conversation-v21-bottom.png` | Estructura V21 portada y comprobada en `.test-artifacts/ui-audit/current/conversation-v21-final.png` y `conversation-v21-final-lower.png`: título Chats, tamaño de sticker y vista previa, emojis, fotos, estilos de burbujas y ActionBar, Media y entrada. La vista previa del sticker y las tarjetas de estilo son recreaciones simplificadas; falta pulir su dibujo y probar el efecto real de cada ajuste en mensajes y medios. |
| Conversación / Mensajes guardados | `.test-artifacts/ui-audit/original/saved-messages-original.png` | Composición V21 portada y validada en `.test-artifacts/ui-audit/current/saved-messages-v21-final-badge.png`: barra blanca plana, título centrado, marcador a la derecha, badge azul, fondo blanco, burbujas celestes y compositor inferior plano. |
| Contactos | `.test-artifacts/ui-audit/original/contacts.png` | Lista plana V21 aplicada y comprobada en `.test-artifacts/ui-audit/current/contacts-v21-final.png`: cabecera blanca, controles azules, tres acciones con iconos grises, sección gris y contactos sin tarjetas. |
| Llamadas | `.test-artifacts/ui-audit/original/calls.png` | Composición V21 comprobada en `.test-artifacts/ui-audit/current/calls-v21-final.png`: cabecera y fondo blancos, título a la izquierda, sección `Videochats activos`, llamada activa sin tarjeta y botón flotante cuadrado. |

## Diferencias confirmadas en Inicio antes de la primera corrección

- V21 presenta `Contactos`, `Llamadas`, `Chats` y `Ajustes`; la migración mostraba solo `Ajustes`, `Chats` y un botón separado de búsqueda.
- V21 mantiene el buscador `Busca chats...` debajo del encabezado; la migración lo ocultaba.
- V21 utiliza hamburguesa a la izquierda y luna a la derecha; la migración mostraba un menú de tres puntos.
- V21 usa título oscuro; la migración usaba el color de acento.
- V21 conserva separadores finos entre chats e iconografía clásica; la migración usaba iconos Solar y ocultaba divisores.
- La barra inferior de V21 ocupa todo el ancho; la migración utilizaba una cápsula flotante compacta.

## Cambios visuales aplicados en Inicio

- La configuración visual base se migra una sola vez sin borrar la sesión ni los datos del usuario.
- Orden inferior V21: `Contactos`, `Llamadas`, `Chats`, `Ajustes`; búsqueda separada retirada.
- Barra inferior a ancho completo y selector reducido alrededor del icono activo.
- Buscador superior restaurado con el texto `Busca chats...`.
- Título MDGram oscuro y centrado, hamburguesa a la izquierda y luna a la derecha.
- Iconos Solar desactivados por defecto y separadores restaurados.
- Ajustes vuelve a mostrar engranaje en vez del avatar del usuario.
- Diálogo de bienvenida V21 incorporado y verificado en `.test-artifacts/ui-audit/current/ui-pass-1.png`.
- Se recuperaron de V21 los paquetes tipográficos originales; Linotte vuelve a ser la fuente destacada predeterminada.
- `Mensajes guardados` se fuerza al marcador clásico de V21 en vez del icono nuevo de Mis notas.
- La navegación inferior inactiva usa el gris de V21 y conserva la misma familia tipográfica al seleccionar una pestaña.
- Se corrigió el dibujo de avatares especiales: el icono se renderiza también cuando el fondo y el contenido están al 100 % de opacidad.
- La barra inferior ahora mide todo el ancho disponible y distribuye uniformemente las cuatro pestañas.
- La cabecera colapsada de historias recupera el comportamiento histórico de MDGram: avatar + `Chats`, centrados y sin cubrir la hamburguesa.

## Cambios visuales aplicados en Ajustes

- Se retiró de la lista la cabecera grande y las tarjetas propias de Cherrygram.
- La barra superior vuelve a mostrar flecha, avatar, nombre y estado, con búsqueda y menú en azul MDGram.
- Se restauraron las secciones planas `Cuenta` y `Ajustes`, con teléfono, usuario y biografía.
- `Ajustes de MDGram`, `Ahorro de energía`, `Dispositivos`, `Idioma` y `Acerca de MDGram` usan la jerarquía e iconos de V21.
- Se conservaron los accesos Premium, Business y regalo visibles en la referencia original.
- `Ajustes de MDGram` continúa conectado al centro de configuración actual; se verificaron `General`, `Conversación` y `Pantalla principal` mediante la jerarquía UI.
- El centro `Ajustes de MDGram` también se comparó directamente con V21: cabecera y tarjetas recolocadas, grises y radios ajustados, sección `TELEGRAM` integrada en su tarjeta. El APK firmado compiló (`.test-artifacts/build-ui-hub-v21-final.log`) y se instaló sin borrar la sesión. Se reabrieron General, Conversación, Pantalla principal, Otros mods, Traducir mensajes y Actualizar.

## Cambios visuales aplicados en General

- Se extrajeron del APK original V21 las cinco imágenes del selector, en cinco densidades, y se declararon cuatro aliases de launcher para Material You, Naranja, Verde e Icono amarillo. El icono predeterminado usa el alias existente de MDGram.
- El selector de Cherrygram de varias filas se reemplazó por una fila compacta. Se verificó en MEmu que seleccionar Naranja activa su alias y desactiva el predeterminado; después se restauró Por defecto.
- La vista previa del perfil vuelve a ser compacta cuando la foto como fondo está desactivada. Ese es ahora el valor inicial para instalaciones sin preferencia guardada; una elección previa del usuario se conserva. Las opciones de difuminado y oscurecimiento aparecen al activar la foto de fondo.
- El acceso a Estilos de fuentes abrió el selector real. El acelerador de descarga usa un deslizador No/Rápida/Ultra; se comprobó Rápida y se restauró No. La foto de fondo se activó y desactivó durante la prueba.
- Compilación firmada `afatStandalone` correcta en `.test-artifacts/build-ui-general-v21-final2.log`, instalación incremental y captura final `.test-artifacts/ui-audit/current/general-v21-final.png`. Queda por igualar el tinte dinámico de Material You y seguir con las demás pantallas del hub.

## Cambios visuales aplicados en Conversación

- El estilo iOS de MDGram ya no activa las cápsulas de vidrio de Cherrygram en la barra del chat.
- `Mensajes guardados` centra el título y coloca el marcador a la derecha, como en V21.
- La flecha y el marcador usan azul MDGram; el contador de no leídos vuelve a ser blanco sobre azul.
- La migración visual v3 selecciona una sola vez la base clara `Day`, con fondo blanco, sin borrar la sesión ni impedir que el usuario cambie el tema después.
- Los extremos del degradado de las burbujas salientes se ajustaron a los colores medidos en el APK original. En puntos equivalentes, V21 renderiza `#80CFE5` a `#5BA3D4` y la migración `#80CFE4` a `#5BA3D5`.
- El compositor conserva la barra plana de ancho completo ya integrada, sin la isla exterior de vidrio.

## Cambios visuales aplicados en Chats (ajustes)

- La pantalla se reordenó siguiendo las capturas completas de V21: controles visibles de sticker y foto, secciones Emojis, Estilos de Bubble Chats, Estilos de ActionBar Chats, Media, Entrada de chat y Menú contextual. «Mejoras de voz», que no figura en la referencia, queda en Opciones adicionales.
- Los deslizadores de sticker y calidad de foto conservan sus preferencias actuales y se probaron en MEmu, devolviéndolos a sus niveles iniciales. Las tarjetas de burbujas y ActionBar permiten cambiar de estilo; se comprobaron y restauraron los valores previos.
- «Blur Effect» y «Activar menú desenfoque contextual» corresponden al mismo ajuste implementado en esta base y ahora muestran el mismo estado al cambiar cualquiera de los dos. Se activaron y desactivaron durante la verificación. La intensidad se ve como escala 0–255 y el valor inicial sin preferencia guardada equivale a 191.
- La muestra superior usa una ilustración genérica de MDGram, no el sticker concreto de la conversación de referencia. Los dibujos de las burbujas y barras superiores son esquemáticos; la fase visual aún debe afinarlos. No se ha probado en este paso la salida real de fotos, stickers, emoji, proximidad ni grabación de voz.
- Compilación firmada e instalación incremental correctas: `.test-artifacts/build-ui-conversation-v21-final2.log`; capturas finales en `.test-artifacts/ui-audit/current/conversation-v21-final.png` y `conversation-v21-final-lower.png`.

## Cambios visuales aplicados en Contactos

- El tab principal vuelve al fondo blanco y a la lista plana de V21, sin el buscador permanente ni las tarjetas de Cherrygram.
- Se recuperaron `Nuevo grupo`, `Nuevo chat secreto` y `Nuevo canal` con iconos grises, además de la banda `Ordenado por última vez`.
- El título y los controles de la cabecera siguen la referencia V21; la búsqueda se abre al tocar la lupa.
- La flecha del tab principal vuelve a Chats sin cerrar la aplicación. El selector de contactos usado en otros recorridos conserva su presentación propia.
- Se comprobaron en MEmu la búsqueda de un contacto y la apertura de los tres accesos. No se creó ningún grupo, chat secreto ni canal durante la prueba.

## Cambios visuales aplicados en Llamadas

- Se retiró del tab principal la tarjeta `Nueva llamada`; el botón flotante conserva ese acceso.
- La lista usa fondo blanco, cabecera plana con flecha azul y título a la izquierda, y la sección `Videochats activos` precede a las llamadas grupales activas.
- El botón flotante recuperó la silueta cuadrada redondeada y el icono de teléfono de V21.
- Se comprobó en MEmu que el botón abre el selector `Nueva llamada`, sin iniciar una llamada real.
- La banda inferior de actualización provenía del descargador de APK de Telegram y se desactivó para `com.mdgram.messenger`; su ausencia se verificó en `.test-artifacts/ui-audit/current/home-no-telegram-update.png`. El canal propio ya existe: `MDGramUpdater` consulta el manifiesto publicado desde el repositorio `jair1c/MDGram` mediante `/api/latest`, y obtiene el APK de GitHub Releases mediante `/dl`. Su interfaz independiente está en Ajustes de MDGram → Actualizar. La release V5 usa el paquete anterior `com.mdgram.messenger.web`, por lo que no puede actualizar esta instalación; el descargador ahora rechaza APKs con paquete, firma o `versionCode` incompatible.

## Evidencia

- Capturas originales: `.test-artifacts/ui-audit/original/`.
- Capturas de la migración: `.test-artifacts/ui-audit/current/`.
- Jerarquías UI: archivos XML junto a las capturas cuando están disponibles.
- Compilaciones de cierre: `.test-artifacts/build-ui-pass-8.log`, `.test-artifacts/build-ui-settings-pass-3.log`, `.test-artifacts/build-ui-saved-final-resume.log`, `.test-artifacts/build-ui-contacts-v21.log` y `.test-artifacts/build-ui-calls-v21.log`.

## Página Principal — 7 de octubre de 2026

- Referencia: `.test-artifacts/ui-audit/original/home-settings-v21-top.png`. Resultado instalado: `.test-artifacts/ui-audit/current/home-settings-v21-final.png`.
- Se recuperaron el título alineado a la izquierda, el fondo blanco y el orden Título de inicio, búsqueda, Carpeta, Bottom Tab y Boton Flotante de V21. Las vistas previas reutilizan los componentes reales de carpetas y navegación de la base actual.
- Título de inicio ahora tiene consumidor en DialogsActivity: se comprobó el nombre del usuario tras reiniciar, y se restauró el ajuste a desactivado. Se abrieron los selectores TabTitleStyle y TabStyle sin alterar las preferencias. Activar Bottom Tab está conectado a showMainTabs; búsqueda y botón flotante conservan sus conexiones.
- Bottom Bar ios sigue pendiente: la fila informa que falta integrar el estilo y no guarda una selección ficticia. Queda afinar la altura de la vista previa inferior, la forma de los interruptores, los colores y la tipografía de las filas. La vista previa de carpetas refleja la preferencia actual (Icon and text), mientras V21 estaba configurado como Icons only.
- Compilación firmada correcta en `.test-artifacts/build-ui-home-v21-final.log`; instalación incremental en MEmu conservando la sesión. El siguiente bloque visual pendiente es Otros mods.

## Otros Mods — 7 de octubre de 2026

- Referencia V21: `.test-artifacts/ui-audit/original/other-mods-v21.png`. Se recuperó el fondo blanco, la cabecera plana con título a la izquierda y flecha azul y la fila única de notificación persistente. Se conserva el texto en español de la migración; V21 mostraba esta fila en inglés.
- La opción adicional de ocultar las pantallas Premium se conserva en el menú de tres puntos → Opciones adicionales, cuya apertura se comprobó en MEmu. No se cambió su preferencia durante la prueba.
- Se activó y desactivó la notificación persistente en Android 12: dumpsys confirmó MDGramResidentService en primer plano con foregroundId=38452 al activarlo y su ausencia al desactivarlo. Se restauró apagado. No se comprobó aquí su comportamiento con restricciones de Android más recientes.
- Persisten diferencias en fuente, forma del interruptor y el menú adicional que no existe en V21. La siguiente sección a comparar es Traducir mensajes.
- Cierre: compilación firmada correcta en `.test-artifacts/build-ui-other-mods-v21-final.log`, firma verificada e instalación incremental sin borrar la sesión. Captura final: `.test-artifacts/ui-audit/current/other-mods-v21-final.png`.

## Traducir — 7 de octubre de 2026

- Referencia original: `.test-artifacts/ui-audit/original/translate-v21.png`; estado previo: `.test-artifacts/ui-audit/current/translate-before.png`.
- Se recuperó el fondo blanco de la cabecera, encabezado y pie, el título alineado a la izquierda y la flecha azul. Se conservaron los textos en español y el orden de las filas.
- El aviso del pie ahora identifica el proveedor seleccionado y se actualiza al cambiarlo.
- Solo botón contextual, proveedor e idioma destino están conectados. Chats enteros, tipo de traducción, idiomas excluidos, automatización y el interruptor Mantener Markdown continúan pendientes: no se deben presentar como funcionales. El motor tiene soporte de entidades independiente de ese interruptor.
- Persisten diferencias de fuente, color, interruptores y altura del encabezado respecto a V21. La traducción real de mensajes se verificará en la fase funcional, en Mensajes guardados.
- Cierre: APK firmado compilado y verificado (.test-artifacts/build-ui-translate-v21.log), instalado sobre MEmu conservando sesión. Se eligió Yandex y se comprobó su aviso; se restauró Google. Se eligió Español y se restauró Por defecto; el botón contextual se apagó y encendió para restaurar su estado inicial. Captura final: .test-artifacts/ui-audit/current/translate-v21-final.png. Estas pruebas verifican controles, no una traducción real.

## Explorar temas — 7 de octubre de 2026

- Referencia: `.test-artifacts/ui-audit/original/themes-v21.png`. Estado previo: `.test-artifacts/ui-audit/current/themes-before.png`.
- Se ajustó únicamente el navegador de temas: fondo blanco, título a la izquierda, flecha y selector día/noche azules, sin marcos de secciones redondeados. Los demás modos de ThemeActivity conservan su configuración.
- Las nueve tarjetas siguen cargando los temas reales de Telegram. En la migración sus fondos aparecen blancos y sus burbujas difieren de V21; la causa aún no está confirmada. No se considera terminada la equivalencia visual de esta pantalla.
- Pendientes: fondos y patrones de las tarjetas, forma/color de burbujas de muestra, tipografía y espaciado. No se cambia el tema activo durante la comprobación visual.
- Cierre parcial: ambas compilaciones firmadas fueron correctas (.test-artifacts/build-ui-themes-v21.log y build-ui-themes-background.log), firma verificada e instalación incremental conservando sesión. Captura: .test-artifacts/ui-audit/current/themes-background-check.png. La normalización RGB a colores opacos NO resolvió los fondos blancos; la causa principal sigue abierta. Se comprobó regresar al hub y reabrir Explorar temas. El tema activo no se cambió.

- Investigación siguiente: DefaultThemesPreviewCell elegía el índice 1 (Day) al estar activo el tema Day. El navegador ahora utiliza índice 0 (Blue) para modo claro e índice 3 para oscuro. La geometría de las burbujas de la cuadrícula usa cápsulas redondeadas como V21. Verificación instalada correcta: build-ui-themes-patterned.log.

- Resolución verificada: las variantes Blue restauran los nueve fondos y patrones reales. Captura instalada `.test-artifacts/ui-audit/current/themes-patterned-final.png`; compilación firmada `.test-artifacts/build-ui-themes-patterned.log` correcta, firma verificada e instalación incremental con sesión conservada. El problema de fondos blancos queda resuelto. Persisten diferencias de color en algunas burbujas de muestra, fuente, icono de luna y espaciado; el tema activo no se cambió. Las notas previas sobre causa abierta describen las pruebas anteriores.
### Afinado de Explorar temas — 7 de octubre de 2026

- La cabecera utiliza la animación sun rellena y Elige un tema usa Linotte regular con azul MDGram. Las cápsulas de la cuadrícula reducen su anchura y ajustan sus márgenes a la captura V21. Las variantes reales de los temas mantienen sus colores; aún difieren de V21 en varias burbujas. Verificación instalada correcta: build-ui-themes-polish.log.

- APK firmado compilado, firma verificada e instalado sin borrar la sesión. Captura final: .test-artifacts/ui-audit/current/themes-polish-final.png. Se probó modo oscuro y se restauró claro; los fondos de ambas variantes aparecen. Se detectó que cambiar el modo en vivo restaura el tinte gris de la cabecera y pierde el azul hasta reabrir: pendiente de corregir. Las burbujas aún conservan colores diferentes a V21. Los emojis de las tarjetas terminaron de descargarse durante esta prueba.
## Actualizaciones — decisión del 7 de octubre de 2026

- Por indicación expresa del usuario, se conserva íntegramente el diseño One UI: logo, aurora animada, versión central y botón inferior Buscar actualización.
- La adaptación visual a V21 se retiró del código antes de instalarla. La referencia `.test-artifacts/ui-audit/original/updater-v21.png` se conserva solo como evidencia; esta pantalla es una excepción a la equivalencia V21.
- Captura del diseño conservado: `.test-artifacts/ui-audit/current/updater-before.png`.
### Cabecera de temas al cambiar modo — verificación del 7 de octubre

- Corregida la actualización de colores de ThemeActivity: el navegador conserva los controles azules durante el cambio y adapta el fondo a windowBackgroundWhite. Los demás modos conservan sus colores de tema.
- Build firmado correcto: .test-artifacts/build-ui-themes-live-accent.log (4m 56s), firma verificada e instalación incremental. Capturas verificadas themes-live-dark.png y themes-live-clear.png; se restauró claro sin reabrir la pantalla.
- APK verificado conservado en .test-artifacts/themes-live-accent.apk. El APK experimental de Actualizar a V21 se descartó y no se instaló; UpdateActivity vuelve al diseño One UI original por solicitud del usuario.


## Vista previa de stickers — 7 de octubre de 2026

- Extraído sin modificar drawable/sticker de MDGram_V21_Universal.apk, entrada res/gJr.png, como drawable-nodpi/mdgram_v21_sticker_preview.png. SHA256 3841A0CE7893C10917FF19C71B94D232E6FCC5E601B99A605A138C9AEA4B7E61.
- Sustituye el logo provisional de la vista previa de Chats. Se recuperan el remitente, la pregunta, la hora y la composición de respuesta; encabezados y etiquetas propias usan Linotte regular azul.
- El slider ahora cambia el tamaño de la imagen usando stickerSizeMultiplier, en vez de modificar el texto de ejemplo. Mantiene los tres niveles de la migración; no reproduce todavía el rango continuo V21.
- Verificado: build-ui-sticker-preview-v21.log correcto (5m 35s), firma verificada e instalación incremental conservando sesión. Capturas sticker-preview-v21-final.png, sticker-preview-small.png, sticker-preview-large.png y sticker-preview-restored.png. Se probaron niveles 10/14/18 y se restauró 14; la imagen cambia y el texto de respuesta conserva su tamaño.


- Diferencias restantes: el encuadre del sticker, el estilo del slider, la tipografía de las celdas comunes y los detalles de hora/respuesta no son todavía idénticos a V21. Las pruebas aquí corresponden a la vista previa; el tamaño en mensajes reales queda para la fase funcional.

## Material You y encuadre de sticker — 7 de octubre de 2026

- Recuperado el vector ic_materialyou_foreground de V21 (res/YgA.xml) y sus tres paths con transparencias originales. El adaptive icon res/99B.xml usa materialyou_icon y materialyou_icon_background; se trasladaron sus colores por versión de Android.
- Android 12+: neutral2_700/accent1_100 en claro y accent1_100/neutral1_800 en oscuro. En Android anteriores se conserva el azul base V21. Se aplica al selector de General y al alias Material You del launcher, con PNG legado para versiones sin adaptive icons.
- Chats: la imagen de muestra mantiene su proporción con CENTER_CROP, se ajustan posiciones y altura de la sección, y se amplía la hora para contener las marcas.
- Compilación e instalación pendientes de verificación.


- Primera verificación: build-ui-material-sticker-polish.log correcto (5m 49s), firma e instalación verificadas. Material You usa ya celeste/gris en el selector y el launcher. Se activó el alias MDGramMaterialIcon y se restauró DefaultIcon, confirmado por dumpsys. Captura launcher-material-adaptive.png.
- Detectado en la muestra de General un fondo cuadrado: se agregó recorte circular únicamente a Material You. Chats mostró demasiado espacio inferior, ajustado en la pasada final. Instalación final pendiente.


- Cierre verificado: build-ui-material-sticker-final.log correcto (4m 13s), firma verificada e instalación incremental sin borrar sesión. Material You circular y colores sistema comprobados en general-material-final.png; encuadre/espaciado en sticker-spacing-final.png.
- Se recorrieron otra vez los tres niveles del slider y se restauró 14, confirmado al reabrir Chats (sticker-spacing-restored.png). La opción de launcher queda en DefaultIcon. No se cambiaron las preferencias de fondo de perfil.
- Quedan forma de interruptores, tipografía de celdas comunes, detalles de respuesta/hora y rango continuo del slider; no se considera cerrada toda la equivalencia visual ni validado el tamaño de mensajes reales.


## Nueva referencia del 10 de octubre de 2026

- Revisados los 388 fotogramas del nuevo video y las dos imágenes. El inventario, tiempos, límites observados y mapa de implementación están en NEW_VISUAL_REFERENCES.md.
- La nueva dirección incluye navegación Chats/Contactos/Ajustes/Perfil, tarjeta de cuenta en el hub, menú modal centrado y vista previa completa de Inicio con cuatro modos de título. Actualizar conserva One UI. Estas incorporaciones están analizadas, aún no implementadas.


## Navegación opcional nueva — 10 de octubre de 2026

- Añadido «Usar nueva navegación» en Ajustes de MDGram → Pantalla principal → Bottom Tab. Preferencia `mdgramconfig/use_new_navigation`, desactivada por defecto.
- OFF conserva la configuración previa de pestañas (base V21: Contactos/Llamadas/Chats/Ajustes). ON usa Chats/Contactos/Ajustes/Perfil, avatar de cuenta y cápsula lateralmente separada; Ajustes abre el hub MDGram y Perfil el perfil propio.
- El cambio se aplica al regresar al inicio, sin reiniciar. Se destruyen los estados de páginas anteriores antes de volver a asociar sus posiciones: evita mezclar destinos y observadores. La vista previa cambia con el interruptor. La selección persiste al cerrar el proceso.
- Primera compilación firmada: `build-new-navigation.log`, correcta en 6m 10s; firma e instalación incremental verificadas. Activación, destinos Contactos/Ajustes/Perfil, cierre/reapertura, desactivación y retorno a Llamadas comprobados en MEmu.
- Ajuste final de altura: cápsula 60dp con margen inferior 8dp; compilación final y verificación visual pendientes. Capturas preliminares `nav-new.png`, `nav-profile-new.png`, `nav-persisted.png`, `nav-classic-restored.png` en `.test-artifacts/ui-audit/current/`.
- MEmu mostraba pantalla negra en ambos APK y «UI del sistema no responde» antes de instalar. Se reinició la VM conservando sesiones; visualización recuperada.
- Sigue pendiente incorporar la tarjeta de cuenta del nuevo hub, el menú modal centrado y la vista previa completa de Inicio. Esta entrega implementa la navegación opcional, no todas las nuevas referencias. Actualizar permanece One UI.

- Cierre final: `build-new-navigation-final.log` correcto en 3m 21s, firma verificada e instalación incremental completada. Cápsula compacta comprobada en `nav-final.png` y selección persistente tras cierre del proceso en `nav-final-persisted.png`; recorridos Contactos/hub/Perfil/Mensajes guardados y regreso a Chats repetidos. Nueva navegación queda activada en MEmu para revisión; instalaciones nuevas siguen con OFF por defecto.
- Tras actualizar surgió un ANR de arranque («Application does not have a focused window»). Otro reinicio de MEmu recuperó el arranque; cierre/reapertura posterior funciona. Incidencia registrada, no se declara resuelta su causa raíz ni estabilización general del emulador.
- APK final conservado en `.test-artifacts/MDGram-new-navigation.apk`, SHA256 `4F3C2C7847E1DDD352C3E9E06A8E15176FBF359E1ECCD32A1C9B20D74848DD16`.

## Tarjeta de cuenta y menú modal — 10 de octubre de 2026

- Implementación en curso: tarjeta de cuenta en el hub cuando está activa la nueva navegación. Avatar con acceso al perfil, cámara con acceso a la pantalla donde se puede cambiar la foto, nombre/teléfono respetando ocultación del número, gestión de cuenta, QR y acceso a ajustes/sugerencias.
- Nuevo menú centrado ligado al estilo nuevo; estilo clásico conserva panel lateral. Contenido: cerrar, cambiar tema, identidad de cuenta, gestionar cuenta, grupos, canales, dispositivos, llamadas, contactos, Ajustes de MDGram, ayuda y enlaces legales. Reutiliza destinos existentes. Menú se reconstruye al abrir para actualizar usuario y colores.
- Primer icono de la tarjeta junto al QR no identificado en la referencia: se usa gestión de cuenta. Sugerencias abre SettingsActivity, donde ya se procesan las sugerencias reales; no se inventan recomendaciones.
- Compilación y verificación de MEmu pendientes. Actualizar sigue One UI. La vista previa completa de Inicio del video permanece pendiente.

- Primera compilación `build-account-modal.log` correcta en 5m 23s; firma e instalación verificadas. Modal claro/oscuro, cambio de tema y acceso a Dispositivos comprobados. QR de cuenta abre el usuario actual (`account-qr.png`).
- Detectados y corregidos para compilación final: identidad del modal sin ancho por sobrecarga entera de LayoutHelper (peso ahora 1f); cabecera del hub cubierta por ActionBar del contenedor de pestañas (oculta en hub integrado y margen superior explícito). Iconos de acciones reducidos, conservando sus áreas de toque. Pendiente instalación y revisión final.

- Cierre: `build-account-modal-final.log` correcto en 4m 15s; firma e instalación incremental verificadas. `account-hub-final.png` y `account-modal-final.png` muestran encabezado e identidad corregidos; `account-hub-dark-final.png` confirma tema oscuro. Nuevo grupo abre selección de miembros (sin crear grupo), Dispositivos y QR abren sus destinos. Cierre con Atrás y cambio de tema comprobados. Sin errores AndroidRuntime en la revisión final.
- OFF vuelve al cajón lateral (`account-classic-drawer.png`); ON vuelve al modal. Se restaura Day y se deja la nueva navegación activa. APK guardado en `.test-artifacts/MDGram-account-modal.apk`.
- Límites: cámara abre el perfil para cambiar foto, no lanza directamente el editor; Sugerencias abre ajustes de cuenta. Ayuda/Privacidad/Condiciones están enlazados pero no se verificaron destinos web en esta pasada. No se completaron creaciones de grupos/canales, cambios de foto ni vinculaciones. Falta la vista previa completa de Inicio del video y afinar detalles visuales; no se afirma igualdad exacta ni cierre funcional global.

- Incidencia durante la restauración de preferencias: volvió a aparecer «UI del sistema no responde» en MEmu. Se cerró el proceso de UI del sistema desde su diálogo y se repitió la navegación; no fue necesario borrar datos ni reiniciar la VM. La causa de esos bloqueos del sistema continúa pendiente. La primera cadena de restauración quedó interrumpida y se repitió antes de cerrar la revisión.

## Vista previa fija de Inicio y cuatro modos de título — 10 de octubre de 2026

- Implementación en curso: vista previa fija sobre los ajustes desplazables con avatar/título/búsqueda/carpetas/chat de muestra/FAB/navegación. Cambios de título, ocultación de búsqueda/FAB, carpetas y barra inferior actualizan la muestra.
- Título: MDGram / Nombre completo / Primer nombre / Chats. `home_title_mode` conserva selección al apagar `home_title_name`; configuraciones previas activas sin modo nuevo conservan Nombre completo (modo 1). Se comparte resolución de texto entre muestra y DialogsActivity, actualizado en creación y retorno a la lista principal.
- Selector del título usa radios, selección actual y Cancelar. Los demás ajustes conservan sus consumidores y avisos previos de reinicio; la vista previa no valida por sí sola sus efectos reales. Estilos nuevos de carpeta (Chips/Pills), historias y badges quedan pendientes.
- Primer intento falló por usar `dialogsType` fuera de su ámbito; corregido a `initialDialogsType`. Compilación final y verificación en MEmu pendientes.

- Cierre: `build-home-preview-final.log` terminó correctamente en 5m 24s. Firma verificada e instalación incremental correcta, con sesión conservada. APK guardado en `.test-artifacts/MDGram-home-preview.apk`.
- MEmu: comprobados los cuatro modos en el encabezado real y en la muestra. Nombre completo y Primer nombre coinciden visualmente en esta cuenta sin apellido; no se modificó el perfil. Selector con radios y selección actual (`home-title-selector.png`). Persistencia de Chats tras apagar el interruptor y reabrir el proceso (`home-title-persist-confirmed.png`).
- Vista previa fija comprobada al desplazar controles (`home-preview-fixed-scroll.png`); ocultación de búsqueda y FAB comprobada en la muestra (`home-preview-hide-search.png`, `home-preview-hide-fab.png`). No se toma esa muestra como validación de sus consumidores reales. Preferencias restauradas: título personalizado OFF, búsqueda visible, FAB visible, nueva navegación ON y tema Day (`home-preview-restored.png`). El modo Chats queda recordado para la próxima activación.
- Al arrancar después de instalar apareció pantalla negra; force-stop no la recuperó y fue necesario reiniciar MEmu, sin borrar datos. Después se completaron los recorridos; sin salida de AndroidRuntime en la consulta final. La causa del bloqueo de arranque continúa pendiente. En esta pasada no se validó la muestra en tema oscuro ni se cerró la auditoría global.
- Próximo bloque: estilos de carpetas de la referencia, contadores e historias. Actualizar sigue con One UI.

## Estilos de carpetas, contadores e historias — 10 de octubre de 2026

- En curso: selector de cinco estilos persistentes (Default, Rounded, Text Only, Chips, Pills) aplicado en FilterTabsView y FoldersPreviewCell. Default y Text Only usan subrayado; Rounded conserva selección redondeada; Chips añade superficies con radio menor y Pills superficies redondeadas en cada pestaña. Text Only fuerza texto sin cambiar el modo de títulos guardado. Es una implementación de los estilos observados; no se declara igualdad exacta con el video.
- Preferencias anteriores Rounded/Outlined se preservan mientras no se elija un estilo nuevo (`folder_style=-1`). Contadores e historias reutilizan `tabsNoUnread` y `hideStories`. Opciones indican reinicio para aplicar al componente real; la muestra responde inmediatamente a estilos/contadores.
- Historias: actualización de permiso de publicación respeta ocultación; cálculo de visibilidad deja de retornar conservando estado anterior. No se agrega una fila ficticia de historias a la muestra.
- Primera compilación falló por captura de variable reasignada en la animación; corregido el cálculo de visibilidad para asignar una sola vez. Compilación final y pruebas pendientes.
- Preparada carpeta temporal `MDGramTest` únicamente con Mensajes guardados para probar el componente real. Retirar al finalizar.

- Cierre: `build-folder-styles-final.log` terminó con BUILD SUCCESSFUL en 5m 40s. Firma verificada e instalación incremental correcta; APK guardado en `.test-artifacts/MDGram-folder-styles.apk`.
- Los cinco estilos se comprobaron en la muestra y en Chats reales, con una carpeta temporal que contenía únicamente Mensajes guardados. Selección de carpeta comprobada; carpeta MDGramTest eliminada al finalizar, sin eliminar chats ni enviar mensajes (`folders-test-removed.png`). Pills también comprobado en tema oscuro (`folders-pills-dark-real.png`, `folders-pills-dark-preview.png`).
- Ocultar contadores comprobado en muestra y Chats. Limitación: la preferencia heredada tabsNoUnread también oculta el contador de Chats de la navegación inferior; pendiente separar ambos controles. Mostrar historias OFF elimina el acceso de cámara tras reiniciar; ON lo restaura. No había historias activas para verificar su contenido y no se publicó ninguna.
- Preferencias restauradas: Rounded, contadores visibles, historias visibles, tema Day y navegación nueva activa. Los cambios de estilos, contadores e historias todavía requieren reiniciar para aplicarse completamente en Chats; la muestra cambia inmediatamente.
- Incidencia de MEmu: pantalla negra y reloj de SystemUI detenido con ambas APK, incluso con jerarquía de la app activa. Reiniciar únicamente com.android.systemui recuperó la imagen y conservó las sesiones. No apareció salida AndroidRuntime en las consultas de esta pasada; la causa del bloqueo del sistema sigue pendiente.
- Próximo bloque: aplicar ajustes al volver a Chats sin reinicio y separar el contador de carpetas del contador inferior. Actualizar conserva su interfaz One UI. No se declara paridad visual completa ni cierre de la auditoría funcional.

## Ajustes de Inicio sin reinicio — 10 de octubre de 2026

- En curso: onResume de Chats refresca modo de pestañas, reconstruye las carpetas conservando su selección y actualiza contadores e historias. Los controles correspondientes indican aplicación al volver a Chats.
- Contadores de carpetas separados mediante hide_folder_counters, por defecto visibles. La preferencia heredada tabsNoUnread queda para navegación inferior; su consumidor ahora limpia el badge al ocultarlo en vez de retornar dejando un contador anterior.
- Compilación y comprobación en MEmu pendientes. Actualizar One UI no se modifica.

- Cierre: build-live-settings-final.log terminó correctamente en 6m 40s; firma verificada, APK instalado incrementalmente y copiado a `.test-artifacts/MDGram-live-settings.apk`.
- MEmu: Text Only, Pills y retorno a Rounded se aplicaron al volver a Chats, sin reiniciar. Selección de la carpeta conservada (`folders-live-selection.png`). Contadores de carpetas ocultos mientras Chats inferior mantenía 496; al restaurarlos aparecieron ambos (`folders-live-restored.png`).
- Historias ON/OFF/ON comprobadas con el mismo PID 11404, sin cerrar el proceso: botón de cámara aparece/desaparece (`stories-live-on-final.png`, `stories-live-off-final.png`). Primera prueba detectó que updateStoriesPosting no actualizaba visibilidad; se añadió updateFloatingButtonVisibility y se repitió tras la compilación final.
- También se conectó el control heredado de carpetas en FoldersPreferencesEntry a hide_folder_counters, evitando volver a compartir la preferencia inferior. No se migró automáticamente el valor antiguo, porque no distinguía ambas intenciones; contadores de carpetas visibles por defecto.
- Carpeta temporal MDGramTest, con sólo Mensajes guardados, eliminada al finalizar. Restaurados Rounded, contadores visibles, historias ON, tema Day y navegación nueva ON. Sin envíos de mensajes ni publicación de historias. Buffer de crashes vacío en la consulta final.
- Límites: no había historias activas para validar contenido; los otros ajustes de Inicio conservan sus requisitos previos. No se cierra auditoría global ni se modifica Actualizar One UI.

## Controles de Inicio y selector de contenido — 10 de octubre de 2026

- En curso: actualizar visibilidad y espacio del buscador al volver a Chats; el FAB utiliza la actualización de visibilidad integrada en el bloque anterior. Retirados avisos de reinicio de estos dos controles.
- Selector Contenido de las carpetas: diálogo propio de Telegram con radios, selección actual y Cancelar, en español. Modos Iconos y texto / Sólo texto / Sólo iconos, sin cambiar sus valores persistentes. Text Only conserva su prioridad como estilo.
- Compilación y verificación real pendientes. Actualizar One UI se conserva.

- Cierre: build-home-controls.log correcto en 6m 42s, firma verificada, instalación incremental correcta. APK `.test-artifacts/MDGram-home-controls.apk`.
- MEmu: ocultar buscador elimina su superficie y reserva de altura y deja lupa superior; ocultar FAB retira composición y cámara. Preferencias actualizadas en el proceso 13201. Restaurados ambos visibles; al estar desplazada la cabecera, el buscador respeta su colapso por scroll y vuelve al deslizar hacia abajo (`home-controls-restored.png`). Campo de búsqueda abierto y detectado como EditText.
- Selector propio con radios y Cancelar (`home-content-selector.png`). Sólo iconos, Sólo texto e Iconos y texto comprobados en carpetas reales; restaurado Iconos y texto con Rounded. Carpeta temporal sólo con Mensajes guardados eliminada; sin envío de mensajes ni reacciones.
- Incidencia de la automatización: una secuencia de Atrás salió de la app actual y mostró la original, y se abrió una conversación y su menú sin ejecutar acciones. Se regresó explícitamente a com.mdgram.messenger antes de repetir la validación; no usar esa captura como evidencia del selector.
- Tema Day, nueva navegación ON, historias ON y contadores visibles conservados. Buffer de crashes vacío al cierre. Selector oscuro no comprobado en esta pasada. Actualizar One UI intacto.
- Próximos pendientes: interruptores y tipografía de filas comunes, detalles de composición respecto a V21 y opción de estilo inferior iOS, cuyo selector no aparece abierto en el video. Persisten verificación de historias activas y auditoría funcional global.

## Tipografía e interruptores de ajustes — 10 de octubre de 2026

- En curso: estilo explícito sólo en filas TextCheckCell/TextSettingsCell de General, Conversación, Inicio, Otros mods y Traductor. Linotte regular por defecto; respeta fuente seleccionada y opción de fuente del sistema. Títulos, valores y subtítulos usan la misma resolución de tipografía.
- Interruptores conservan geometría, animación y accesibilidad existentes y toman el acento del tema mediante windowBackgroundWhiteValueText. No se cambia globalmente TextCheckCell ni Actualizar One UI.
- Vista previa de navegación clásica usa superficie del tema en vez de fondo claro fijo.
- Decisión del usuario delegada: conservar cápsula actual como presentación iOS de la nueva navegación y mantener interruptor clásico/nuevo. No añadir selector redundante ni variante plana sin referencia visual. iOS aquí describe la dirección visual, no igualdad exacta con UIKit.
- Compilación y verificación en MEmu pendientes.

## Cierre de tipografía e interruptores — 12 de octubre de 2026

- Compilación del 10 de octubre confirmada en build-settings-style.log: BUILD SUCCESSFUL en 3m 29s. Firma verificada e instalación incremental correcta; APK .test-artifacts/MDGram-settings-style.apk. No se recompiló el 12: se retomó la verificación y documentación.
- Verificados General, Conversación, Inicio, Otros mods y Traductor en claro; Inicio y Traductor en oscuro. Capturas settings-style-*.png en .test-artifacts/ui-audit/current. Vista previa clásica oscura sin fondo blanco fijo. Ocultar buscador comprobado y restaurado.
- Linotte y colores del tema aplicados explícitamente a las filas TextCheckCell/TextSettingsCell propias. La fuente personalizada y la opción de fuente del sistema están contempladas en código, pero no se comprobaron en dispositivo en este bloque. TextInfoPrivacyCell conserva su tipografía anterior.
- Se conserva la cápsula como dirección visual iOS y el interruptor clásico/nuevo. Actualizar One UI intacto. No se afirma paridad visual completa.
- Restaurados navegación nueva ON, pestañas inferiores ON, buscador visible y FAB visible; seleccionado Modo diurno. El proceso 3735 permaneció activo durante la comprobación final. Sin mensajes ni reacciones enviados.
- Incidencia del 12: crash nativo SIGFPE en RenderThread, libhwui/BlobCache/ShaderCache de Android en MEmu. Registro .test-artifacts/memu-render-crash-20261012.log. Relaunch correcto; reinicio de SystemUI recuperó la composición de pantalla. Causa pendiente de diagnóstico: no se atribuye todavía a un cambio de la app. Esto impide dar por cerrada la estabilidad global.
- Pendientes: diagnóstico de la incidencia nativa, fuentes alternativas, composición restante respecto a las referencias y auditoría funcional. Historias con contenido activo continúan sin comprobar.

## Menú flotante y referencia del contador — 12 de octubre de 2026

- V21 verificado en MEmu: contador de Chats con fondo rojo rosado y cifras blancas. Captura .test-artifacts/ui-audit/original-counter.png. Los contadores de la lista de chats usan otros colores. El contador inferior actual no se modificó en esta solicitud de confirmación.
- Retirada llamada close() del botón de tema del modal. Actualización de colores del mismo panel desde didSetNewTheme: fondo redondeado, textos, iconos, selectores y borde de Gestionar tu cuenta; icono sol/luna sincronizado. Cambio sin captura/reveal circular para mantener el panel y desplazamiento.
- Compilación en curso en build-modal-theme.log; no considerar instalada ni validada hasta el cierre siguiente.

- Cierre: BUILD SUCCESSFUL en 7m 8s; firma verificada, instalación incremental Success. APK .test-artifacts/MDGram-modal-theme.apk.
- MEmu: claro → oscuro → claro comprobado sin cerrar menú; controles conservan bounds [868,265][976,373] y [103,265][211,373]. Fondo, textos, iconos sol/luna y borde actualizados visualmente. Capturas modal-theme-dark.png y modal-theme-light.png. Proceso activo 7107; dejado en claro con menú abierto para revisión. Sin envíos de mensajes ni acciones en conversaciones.

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
