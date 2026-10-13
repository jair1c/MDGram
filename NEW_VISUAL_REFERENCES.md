# Nuevas referencias visuales de MDGram — 10 de octubre de 2026

## Alcance y evidencia

Revisión visual de los 388 fotogramas decodificados del video `C:/Users/PC/Downloads/video5143366509694813708.mp4`, incluidos en 17 hojas de contacto. Duración de la pista: 18,106 segundos; resolución: 588 × 1280. Se ampliaron los estados y selectores principales para leer sus textos. Las dos imágenes adjuntas se conservaron en `.test-artifacts/new-reference-20261010/`.

El video muestra **la pantalla de ajustes de Inicio y su vista previa**, no un recorrido completo por todas las pantallas. La tarjeta de cuenta del centro de ajustes oscuro y el menú de cuenta claro provienen de las imágenes estáticas. Estas no permiten conocer sus animaciones ni las pantallas de destino.

Esta referencia actualiza la dirección visual en los componentes descritos a continuación. V21 sigue sirviendo para las pantallas no mostradas. **Actualizar conserva su diseño One UI por decisión expresa del usuario.** No se ha modificado el código ni generado un APK con esta nueva dirección durante esta revisión.

## Inventario visual

| Componente | Lo que se ve | Diferencia con el proyecto actual |
| --- | --- | --- |
| Navegación principal | Chats, Contactos, Ajustes, Perfil; contenedor redondeado con márgenes, selección en cápsula, avatar de Perfil | Actualmente Contactos, Llamadas, Chats, Ajustes a ancho completo. Se requiere cambiar orden, destinos, avatar y contenedor. Llamadas se conserva como acceso del menú. |
| Centro de ajustes | MDGram Messenger, subtítulo Setting, tarjeta superior de cuenta, cuadrícula 2×2 y filas anchas | El hub actual ya tiene la cuadrícula, pero carece de tarjeta de cuenta y navegación persistente. |
| Tarjeta de cuenta | Avatar con cámara, nombre, teléfono parcialmente visible, dos iconos de acción, flecha, divisor y Sugerencias de la cuenta | Se necesita un componente nuevo conectado al usuario actual. El significado del primer icono junto al QR no queda confirmado por la foto. |
| Menú de cuenta | Modal centrado grande, esquinas redondeadas, fondo atenuado, X, título MDGram y luna | MDGramSideDrawer es un panel lateral que entra por traslación; no equivale a este modal. |
| Cabecera del modal | Avatar, nombre, @usuario y botón Manage Your Account con borde/sombra | Debe usar datos actuales, no los de Axel de la referencia. |
| Acciones del modal | New Group, New Channel, Linked Devices, Recent Call, Contacts, Settings MDGRAM, Help and Feedback | Reutilizar destinos existentes y comprobar cada ruta. Privacy Policy y Terms of Service forman el pie. |
| Ajustes de Inicio | Cabecera Home y una vista previa amplia arriba; controles debajo en secciones Toolbar, Stories, Carpeta, Bottom Tab | HomeSettingsActivity separa actualmente las vistas previas de carpeta y navegación; falta una composición de Inicio completa. |
| Vista previa de Inicio | Avatar izquierdo, título centrado, menú vertical, buscador en cápsula, carpetas con iconos/badges, fila de chat de muestra, FAB cuadrado y navegación inferior | Debe reaccionar a preferencias; los textos grises y el dinosaurio son ejemplos, no mensajes del usuario. |
| Título de Inicio | Activación revela Toolbar Title Style: MDGram, Nombre de Usuario, Primer Nombre, Chats | MDGramConfig tiene solo homeTitleAsName boolean. Se necesita modo de título y migración compatible. |
| Historias | Sección Stories con interruptor | El video no demuestra el efecto sobre historias reales. No asumir que el control funciona solo por aparecer. |
| Carpetas | Tab Title Style, Tab Style, ocultar Todos, ocultar badges | Se necesita ampliar y revisar el consumidor de cada opción. |
| Estilos de carpeta | Default, Rounded, Text Only, Chips, Pills | El selector actual ofrece menos estilos; no basta guardar un valor nuevo. |
| Barra inferior | BottomView Style muestra iOS | El video no abre este selector ni muestra todas sus variantes. La opción iOS actual sigue pendiente de integrar. |
| Diálogos | Fondo acorde al tema, esquinas redondeadas, radios de selección y Cancelar al pie; fondo de pantalla atenuado | Conservar selección actual al abrir, actualizar la vista previa al elegir y verificar animación/dimensiones. |

## Secuencia observada del video

Los tiempos corresponden a la pista, no al reloj de la barra del teléfono.

| Tiempo aproximado | Fotogramas relevantes | Observación |
| --- | --- | --- |
| 0–3,3 s | 0–71 | Vista inicial; desplazamiento de controles bajo la vista previa. Stories recibe un toque. |
| 3,3–5,2 s | 72–107 | Se activa Home title y aparece Toolbar Title Style. |
| 5,2–6,4 s | 108–129 | Selector de título, elección de Nombre de Usuario; la cabecera pasa a Axel Herz. |
| 7,4–8,6 s | 148–171 | Selector de título y elección de Primer Nombre; la cabecera pasa a Axel. |
| 9,4–10 s | 188–207 | Elección de Chats; cabecera actualizada. |
| 11,2–11,6 s | 224–239 | Se desactiva Home title; vuelve MDGram y se oculta el selector. |
| 11,7–13,2 s | 240–291 | Desplazamiento hacia Carpeta, ocultar Todos/badges y Bottom Tab. |
| 13,2–14,2 s | 292–327 | Interacción/desplazamiento; se ve BottomView Style iOS. No se demuestra cambio de estilo inferior. |
| 14,2–16,1 s | 328–349 | Tab Style: cinco alternativas; se elige Chips. |
| 17,3–18,1 s | 368–387 | Se reabre Tab Style y se elige Pills; ese valor aparece al final. |

**Nombre de Usuario significa aquí nombre visible completo (Axel Herz), no @Axel_Herz**, según el resultado observado. Las franjas verdes de las filas y los círculos de toque son feedback de interacción; no deben quedar pintados permanentemente. El último cambio termina justo al final del clip, por lo que no se puede evaluar su estado prolongado ni persistencia.

## Color, tipografía y composición

- Imagen oscura: fondo azul grisáceo, tarjetas ligeramente más claras, texto blanco, secundario gris y acento celeste. Se conserva la familia redondeada Linotte y los iconos de color del hub.
- Video e imagen clara: superficie casi blanca con tinte verde suave; acentos verdes y valores secundarios azulados. Es una variante de tema: no reemplazar todos los azules de MDGram por verde fijo ni fijar los fondos claros dentro del modo oscuro.
- La foto del teléfono tiene perspectiva e iluminación externa. No usar sus píxeles como medidas exactas o colores finales; tomar composición y proporciones, y validar con capturas directas.
- El menú claro utiliza iconos de contorno negro. La navegación mezcla contorno en elementos inactivos y relleno en el activo.
- En el video, el avatar de Perfil se superpone con Ajustes dentro de la vista previa. Implementar cuatro espacios independientes; no reproducir el solapamiento accidental ni la truncación de etiquetas de la foto como objetivo.
- No copiar barra de estado, batería, reproductor musical, controles del sistema ni la capa de toque de la grabación.

## Mapa de implementación y orden

1. Navegación Chats / Contactos / Ajustes / Perfil: `DialogsActivity`, `Components/glass/GlassTabView`, coordinación con `LaunchActivity`; mover Llamadas al menú, conservar su recorrido.
2. Hub: `MDGramSettingsActivity`, tarjeta de cuenta y tratamiento claro/oscuro. Definir que Ajustes abre este centro y Perfil abre los ajustes de cuenta existentes, conservando acceso a opciones de Telegram.
3. Menú modal: adaptar o reemplazar la presentación de `Components/MDGramSideDrawer`, reutilizando acciones existentes. Revisar cierre por X, atrás y toque exterior, accesibilidad y pantalla pequeña.
4. Inicio: `HomeSettingsActivity`, nueva vista previa compuesta y selector de cuatro títulos. `MDGramConfig` y `DialogsActivity` deben compartir el mismo consumidor; mantener compatibilidad con homeTitleAsName.
5. Carpetas y barra inferior: integrar cada estilo real y el ocultamiento de badges; después aplicar cambios en vivo donde sea viable. No presentar opciones guardadas como implementadas si no alteran el componente real.
6. Verificar cada bloque con compilación firmada e instalación incremental en MEmu, capturas claras/oscuras y restauración de ajustes de prueba. Mantener las pruebas de mensajes en Mensajes guardados y excluir las que requieren teléfono físico.

## Archivos de consulta

- Fotogramas individuales: `.test-artifacts/new-reference-20261010/frames/0000.png` hasta `0387.png`.
- Tiempos por fotograma: `.test-artifacts/new-reference-20261010/frames.json`.
- Hojas de contacto: `sheet-00.jpg` hasta `sheet-16.jpg`.
- Imágenes: `settings-dark.png`, `account-modal-light.png`.
- Visor local: `.test-artifacts/new-reference-20261010/viewer.html` (flechas izquierda/derecha para avanzar de uno en uno).
## Estado actualizado — 10 de octubre de 2026

El inventario anterior describe el punto de partida. Navegación opcional, tarjeta de cuenta, modal, vista previa de Inicio, cuatro títulos y cinco estilos de carpetas ya están implementados y cuentan con comprobaciones en MEmu registradas en UI_AUDIT.md. Estilos, contadores, historias, buscador y FAB se aplican al volver a Chats. Se conserva la cápsula actual como presentación iOS, con cambio clásico/nuevo mediante el interruptor existente, por recomendación aceptada por el usuario. Tipografía e interruptores de filas propias en verificación; paridad completa y auditoría funcional siguen abiertas.

## Avance — 12 de octubre de 2026

Tipografía e interruptores de filas propias compilados, firmados, instalados y comprobados según UI_AUDIT.md. Las fuentes alternativas, textos informativos y paridad completa siguen pendientes. Se conserva la cápsula iOS y Actualizar One UI. Se registró una incidencia nativa de renderizado en MEmu; la app volvió a abrir, pero su causa sigue pendiente.
