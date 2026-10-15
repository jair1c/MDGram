# Validación de Liquid Glass — 15-10-2026

Dispositivo: Galaxy A36, Android16/API36, ADB inalámbrico. Capturas/XML privados en .test-artifacts. No publicación.

## APK

Archivo: .test-artifacts/MDGram-liquid-glass-validated.apk
SHA256: 41679D14A026D6091F9FBE33A72E3BC20BD087F7F2A0883F609956EBB4912B39
Build: build-release-sheet-scroll.log, BUILD SUCCESSFUL4m33s. Firma apksigner verificada, instalación incremental Success y arranque frío correcto.

## Cambios encontrados durante la revisión

- Fondo legible mientras se prepara la primera captura del aviso.
- Recaptura al cambiar tamaño/orientación.
- Cancelación de callbacks al separar la vista.
- Corrección física de scroll: el vidrio permanece en el viewport al desplazar el contenido. Se encontró y corrigió el defecto con el aviso horizontal, donde Cancelar quedaba fuera del fondo.

## Matriz y recorridos

Se ejercitaron navegación plana y píldora con Glass ON/OFF, claro y oscuro, mediante ajustes reales y regreso a inicio. Capturas release-flat*, release-pill*, release-final*. La forma plana usa Contactos/Llamadas/Chats/Ajustes y píldora Chats/Contactos/Ajustes/Perfil: comportamiento existente conservado.

Teclado y avión blanco transparente en oscuro: release-keyboard.png. Borrador propio MDGRAM_UI_QA eliminado, campo verificado Mensaje. Emojis: release-emoji.png. Adjuntos: release-attach.png, regreso sin envío. Composer sin Glass: release-off-saved.png. Contactos y Ajustes: release-contacts.png, release-settings.png, release-final-settings-dark.png; vista previa: release-preview-light.png. Inicio horizontal y retorno vertical: release-landscape.png. Segundo plano y retorno: release-background-return.xml/png.

Para abrir el aviso sin crear crashes se construyó una instrumentación separada, firmada con la misma clave, que instancia el diálogo real por reflexión. No modifica el APK de producción, ni pulsa enviar/cancelar. Se cierra mediante dismiss. Esta herramienta temporal se retira al terminar. Los informes no se fabrican ni se envían.

Aviso sin Glass claro: release-sheet-off-light.png. Con Glass claro: release-sheet-on-light.png. APK final con Glass oscuro: release-signed-sheet-dark.png; horizontal desplazado corregido: release-signed-sheet-land.png. APK final sin Glass oscuro: release-signed-sheet-off-dark.png. La ilustración animada original y los textos MDGram permanecen intactos.

## Rendimiento observado

Pasadas cortas de cuatro pares de desplazamientos con gfxinfo; NO son un ensayo de batería ni un benchmark controlado. Se descartó una primera muestra con0frames al quedar el launcher delante.

| Perfil | Frames | Janky | P50 | P90 | P95 |
| --- | ---: | ---: | ---: | ---: | ---: |
| Plano + Glass | 533 | 8.44% | 12ms | 24ms | 31ms |
| Plano sin Glass | 523 | 1.72% | 9ms | 13ms | 16ms |
| Píldora sin Glass | 573 | 1.22% | 9ms | 12ms | 14ms |
| Píldora + Glass | 542 | 4.61% | 11ms | 18ms | 22ms |

Glass tiene un coste visible; se mantiene opcional y no se promete igualdad con OFF. Una muestra PSS de sesión era278046KB; aviso instrumentado sin Glass215607KB. No prueban ausencia de fugas. No se modificó ahorro de energía del sistema; rotación original lock0 debe quedar restaurada.

La prueba cubre este Galaxy y los recorridos indicados. No equivale a certificación de todas las pantallas, cuentas, fabricantes o estabilidad prolongada. Actualizar One UI no fue modificado.

APK final también revisado en claro: release-signed-sheet-light.png y release-signed-sheet-land-light.png. Vidrio fijo, botones dentro del panel después del scroll. Instrumentación terminó con INSTRUMENTATION_CODE -1 en las pasadas completadas; no son crashes reales. Buffer crash del PID vivo en la última pasada vacío (release-final-live-crash.txt).

Cierre: instrumentación com.mdgram.glassqa desinstalada (Success) y captura temporal del directorio externo eliminada. App normal arranca en frío sin instrumentación; Contactos/Ajustes/Chats comprobados. Búsqueda MDGRAM_UI_QA verificada y borrada; salida de búsqueda probada. Preferencias iniciales píldora ON, Glass ON, tema claro restauradas. Rotación lock0 restaurada. Buffer crash del proceso normal final: release-normal-final-crash.txt.

## APK de publicación V5.2
El APK anterior validaba el cambio visual con numeración V5.1. Para publicar se recompiló con MDGRAM_VERSION_CODE9/V5.2 y APP_VERSION_CODE7039 (APK70399), base12.10.1. BUILD SUCCESSFUL5m46s (build-v52-release.log). Artefacto final MDGram-universal.apk SHA256 7f4d37f25985932f73b83fb48798e70c27090db83ec72c71e71ac954ba937944. Firma idéntica a V5.1, paquete com.mdgram.messenger, minSdk24 y ARM32/ARM64 verificados por apksigner/aapt. Instalación incremental Success conserva sesión; arranque frío correcto y pantalla Actualizar muestra MDGram V5.2. Regreso a Chats y crash buffer filtrado final vacío. El delta respecto al APK de matriz es la numeración; no se repite una prueba prolongada ni se confunden ambos hashes.
