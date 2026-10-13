# Preparación de publicación — 13 de octubre de 2026

## Decisión

V5.1 firmada supera la verificación de las áreas modificadas y la pasada física del APK definitivo. Se recomienda publicarla como versión estable, sin etiqueta beta, con el alcance y límites detallados abajo. Publicada en GitHub como V5.1 estable; ver la comprobación de publicación al final.

APK final: [.test-artifacts/MDGram-release-stability.apk](.test-artifacts/MDGram-release-stability.apk). Compilado en 4m44s, firma verificada e instalado en Galaxy A36 y MEmu conservando sesión. SHA-256 `FF59E831CEAF5A84211B35182B0F2B5A788E88F654CD8EA0528E9DBFEE8DCB1A`.

## Cerrado en esta ronda

- [x] Barra plana confirmada por el usuario en Galaxy A36 con navegación por gestos.
- [x] Errores de HTTPS, cancelación y recuperación de traducción manual/inline/chat completo (MEmu).
- [x] Cambio de proveedor durante una petición, con caché y primera traducción sin caché: descarta resultado anterior, retorna al original y permite nueva petición.
- [x] Tipo Ventana flotante / Dentro del mensaje, y automática Nunca / Al abrir chats para Google/Yandex. Automática sin override de Saved probada; override manual y exclusiones respetados; ajustes restaurados.
- [x] Galaxy A36: negrita/cursiva/tachado/spoiler/enlace conservados visualmente, traducción y retorno al original; spoiler de ventana corregido y revelación por toque comprobada.
- [x] Foto generada con caption francés: envío sólo a Saved, traducción en ventana y original intacto.
- [x] Conversión de entidades sobre código real: superposición, UTF16, enlace escapado, spoiler, code/pre. Pre con language vacío válido para su serialización; PASS. Bloques de código no probados físicamente.
- [x] Ronda física acotada: PID estable durante 14m18s, 24 cambios de tema y 12 retornos de segundo plano; registro de crashes filtrado vacío y memoria final menor. Fue el APK con spoilers/pre; el definitivo agrega la limpieza inline sin caché, comprobada luego en MEmu e instalada en Galaxy. No se repitió otro soak completo físico tras esa última instalación.
- [x] Propiedades de firma, credenciales, diario local y diagnósticos privados excluidos de Git. Eliminadas exclusiones de fuentes Java/Kotlin necesarias para publicar código compilable.
- [x] Automatización heredada que copiaba Cherrygram a otro GitLab retirada.
- [x] Notas de V5.1 y version.json preparados con alcance real, requisitos de migración y sin promesa de servicio ilimitado. Actualizar conserva One UI.

## Límites y pendientes

- [x] Pruebas autorizadas fuera de Saved: canal público, comentarios vinculados, foro, historial de grupo, recepción de mensaje nuevo dentro de grupo, notificación real y actualización de vistas previas/contadores. Lista de llamadas abierta; no se realizaron llamadas ni envíos a terceros.
- [ ] Formato/medios especiales restantes y recorridos con cuenta Premium. La traducción completa propia omite artículos, encuestas, resúmenes y transcripciones; Telegram conserva sus requisitos nativos.
- [x] Pasada adicional del APK definitivo en Galaxy A36: hash instalado idéntico, navegación fuera de Saved, 14 pulsaciones de tema y seis retornos desde segundo plano conservando PID13450. 17,7 minutos de pasada, crash buffer filtrado vacío y claro/oscuro confirmados por captura. Resultado final y duración en RELEASE_VERIFICATION_2026-10-13.md.
- [ ] Fallo histórico de MEmu: SurfaceFlinger aborta al capturar por error de reserva de buffer. Registro conservado; causa no atribuida a MDGram y no declarada resuelta. APK ARM32/ARM64, no x86 nativo.
- [x] Código correspondiente publicado en codex/release-v5.1 y tag V5.1. Release pública sin draft/prerelease. APK descargado desde GitHub y /dl con hash idéntico; firma v2 verificada, paquete com.mdgram.messenger/code70389. main/version.json y /api/latest responden V5.1/code8.

## Versión y actualizaciones

Proyecto V5.1/code8; Android 12.10.1/code70389, paquete com.mdgram.messenger. La versión anterior usa com.mdgram.messenger.web y requiere instalación nueva; no migrar automáticamente sesión/datos.

En releases futuras se deben incrementar tanto MDGRAM_VERSION_CODE como APP_VERSION_CODE: el actualizador también exige que el versionCode Android del APK descargado sea mayor y que paquete/firma coincidan.

Informe con evidencia y diferencias entre APKs: [RELEASE_VERIFICATION_2026-10-13.md](RELEASE_VERIFICATION_2026-10-13.md). Sólo APK, código y notas revisados son entregables; no adjuntar diagnósticos, bases de datos o capturas privadas.
