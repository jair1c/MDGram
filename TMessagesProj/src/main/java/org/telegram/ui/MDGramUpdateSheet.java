package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.MDGramUpdater;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

import java.io.File;

// MDGram: hoja inferior del instalador in-app (reutilizable). Se muestra desde la celda Actualizar
// (chequeo manual) o automáticamente al abrir la app (DialogsActivity) si hay versión nueva.
// "Actualización disponible" → progreso multicolor de descarga → instala vía FileProvider. Sin navegador.
public class MDGramUpdateSheet {

    // fromManual=true: chequeo manual (siempre se muestra). fromManual=false: auto (respeta "Ahora no").
    public static void show(Activity act, MDGramUpdater.UpdateInfo info, boolean fromManual) {
        if (act == null || info == null) {
            return;
        }
        final boolean dark = Theme.getActiveTheme() != null && Theme.getActiveTheme().isDark();
        final int titleColor = Theme.getColor(Theme.key_dialogTextBlack);
        final int subColor = Theme.getColor(Theme.key_dialogTextGray3);
        final int btnBg = dark ? 0xFFE8E8E8 : 0xFF1A1A1A;
        final int btnText = dark ? 0xFF101010 : 0xFFFFFFFF;
        final int ghostBorder = dark ? 0x33FFFFFF : 0x22000000;
        final int trackColor = dark ? 0x22FFFFFF : 0x14000000;
        final int sheetBg = dark ? 0xFF16181B : 0xFFFFFFFF;
        final int[] gradientColors = dark
                ? new int[]{0xFF3E9C9C, 0xFF4C8FD6, 0xFFCF8A3E}
                : new int[]{0xFF4FA8B8, 0xFF3C82D0, 0xFFE0A24E};

        LinearLayout content = new LinearLayout(act);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(22), dp(18), dp(22), dp(18));

        TextView title = new TextView(act);
        title.setText("Actualización disponible");
        title.setTextColor(titleColor);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setTypeface(AndroidUtilities.bold());
        content.addView(title, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView version = new TextView(act);
        version.setText("MDGram " + info.versionName);
        version.setTextColor(subColor);
        version.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        content.addView(version, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 3, 0, 0));

        if (info.changelog != null && info.changelog.length() > 0) {
            TextView changelog = new TextView(act);
            changelog.setText(info.changelog);
            changelog.setTextColor(subColor);
            changelog.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            changelog.setLineSpacing(dp(3), 1f);
            content.addView(changelog, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 14, 0, 0));
        }

        final LinearLayout actions = new LinearLayout(act);
        actions.setOrientation(LinearLayout.VERTICAL);
        content.addView(actions, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 20, 0, 0));

        final TextView installBtn = new TextView(act);
        installBtn.setText("Descargar e instalar");
        installBtn.setTextColor(btnText);
        installBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        installBtn.setTypeface(AndroidUtilities.bold());
        installBtn.setGravity(Gravity.CENTER);
        installBtn.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp(26), btnBg, btnBg));
        actions.addView(installBtn, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 52));

        final TextView laterBtn = new TextView(act);
        laterBtn.setText("Ahora no");
        laterBtn.setTextColor(subColor);
        laterBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        laterBtn.setGravity(Gravity.CENTER);
        actions.addView(laterBtn, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 46, 0, 6, 0, 0));

        final LinearLayout progressBox = new LinearLayout(act);
        progressBox.setOrientation(LinearLayout.VERTICAL);
        progressBox.setVisibility(View.GONE);
        content.addView(progressBox, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 20, 0, 0));

        final TextView downloadingLabel = new TextView(act);
        downloadingLabel.setText("Descargando actualización…");
        downloadingLabel.setTextColor(titleColor);
        downloadingLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        progressBox.addView(downloadingLabel, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 14));

        final GradientProgressView progressView = new GradientProgressView(act, gradientColors, trackColor);
        progressBox.addView(progressView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 7));

        final LinearLayout progInfo = new LinearLayout(act);
        progInfo.setOrientation(LinearLayout.HORIZONTAL);
        progressBox.addView(progInfo, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 10, 0, 0));
        final TextView pctText = new TextView(act);
        pctText.setText("0%");
        pctText.setTextColor(subColor);
        pctText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        progInfo.addView(pctText, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));
        final TextView mbText = new TextView(act);
        mbText.setTextColor(subColor);
        mbText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        progInfo.addView(mbText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        final TextView cancelBtn = new TextView(act);
        cancelBtn.setText("Cancelar");
        cancelBtn.setTextColor(titleColor);
        cancelBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        cancelBtn.setTypeface(AndroidUtilities.bold());
        cancelBtn.setGravity(Gravity.CENTER);
        GradientDrawable ghostBg = new GradientDrawable();
        ghostBg.setCornerRadius(dp(26));
        ghostBg.setStroke(dp(1), ghostBorder);
        cancelBtn.setBackground(ghostBg);
        progressBox.addView(cancelBtn, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50, 0, 18, 0, 0));

        BottomSheet.Builder builder = new BottomSheet.Builder(act);
        builder.setApplyBottomPadding(true);
        builder.setCustomView(content);
        final BottomSheet sheet = builder.create();
        sheet.setBackgroundColor(sheetBg);

        laterBtn.setOnClickListener(v -> {
            MDGramConfig.setLastDismissedUpdate(info.versionCode); // no volver a molestar por esta versión (auto)
            sheet.dismiss();
        });

        installBtn.setOnClickListener(v -> {
            if (!MDGramUpdater.canInstall(act)) {
                MDGramUpdater.requestInstallPermission(act);
                Toast.makeText(act, "Permite instalar apps de esta fuente y vuelve a intentar", Toast.LENGTH_LONG).show();
                return;
            }
            actions.setVisibility(View.GONE);
            progressBox.setVisibility(View.VISIBLE);
            MDGramUpdater.downloadApk(info.apkUrl, new MDGramUpdater.DownloadListener() {
                @Override
                public void onProgress(long downloaded, long total) {
                    if (total > 0) {
                        float p = downloaded / (float) total;
                        progressView.setProgress(p);
                        pctText.setText(Math.round(p * 100) + "%");
                        mbText.setText(downloaded / 1048576 + " / " + total / 1048576 + " MB");
                    } else {
                        pctText.setText(downloaded / 1048576 + " MB");
                    }
                }

                @Override
                public void onComplete(File apk) {
                    sheet.dismiss();
                    MDGramUpdater.installApk(act, apk);
                }

                @Override
                public void onError(String error) {
                    downloadingLabel.setText("Error al descargar. Toca Reintentar.");
                    progressBox.setVisibility(View.GONE);
                    actions.setVisibility(View.VISIBLE);
                    installBtn.setText("Reintentar");
                }
            });
        });

        cancelBtn.setOnClickListener(v -> {
            MDGramUpdater.cancelDownload = true;
            sheet.dismiss();
        });

        sheet.show();
    }

    // Barra de progreso multicolor: track redondeado + relleno con gradiente (teal→azul→ámbar) que
    // abarca la porción llena, así siempre se ven los 3 colores (como la aurora de Actualizar).
    private static class GradientProgressView extends View {
        private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final int[] colors;
        private float progress = 0f;

        GradientProgressView(Context context, int[] colors, int trackColor) {
            super(context);
            this.colors = colors;
            trackPaint.setColor(trackColor);
        }

        void setProgress(float p) {
            progress = Math.max(0f, Math.min(1f, p));
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int w = getWidth();
            int h = getHeight();
            float r = h / 2f;
            canvas.drawRoundRect(new RectF(0, 0, w, h), r, r, trackPaint);
            if (progress > 0f) {
                float fw = Math.max(h, w * progress);
                fillPaint.setShader(new LinearGradient(0, 0, fw, 0, colors, null, Shader.TileMode.CLAMP));
                canvas.drawRoundRect(new RectF(0, 0, fw, h), r, r, fillPaint);
            }
        }
    }
}
