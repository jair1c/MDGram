package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.ColorUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramUpdater;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: celda "Actualizar" rediseñada estilo One UI 8.5 "Software update" — fondo plano,
// resplandor radial (aurora) teal/ámbar detrás del logo, botón píldora "Buscar actualización".
// Theme-adaptive: paletas para claro y oscuro (la app tiene su propio día/noche, ver Theme.isCurrentThemeDark).
// El chequeo real usa MDGramUpdater (manifiesto JSON remoto). La descarga abre el APK en el navegador
// (v1); la instalación in-app vía PackageInstaller queda para cuando esté el servidor (ver SESSION_NOTES).
public class UpdateActivity extends BaseFragment {

    private TextView actionButton;
    private TextView statusView;
    private boolean checking;

    @Override
    public View createView(Context context) {
        final boolean dark = Theme.getActiveTheme() != null && Theme.getActiveTheme().isDark();

        final int bgColor = dark ? 0xFF0D0D0D : 0xFFF2F3F5;
        final int textColor = dark ? 0xFFFFFFFF : 0xFF0D0D0D;
        final int subTextColor = dark ? 0xB3FFFFFF : 0x99000000;
        final int statusColor = dark ? 0x99FFFFFF : 0x80000000;
        final int iconsColor = dark ? 0xFFFFFFFF : 0xFF1A1A1A;
        final int iconsBgColor = dark ? 0x22FFFFFF : 0x14000000;
        final int buttonBg = dark ? 0xFFD8D8D8 : 0xFF1A1A1A;
        final int buttonBgPressed = dark ? 0xFFBFBFBF : 0xFF333333;
        final int buttonText = dark ? 0xFF101010 : 0xFFFFFFFF;

        actionBar.setBackButtonDrawable(new BackDrawable(false));
        actionBar.setTitle("");
        actionBar.setCastShadows(false);
        actionBar.setItemsColor(iconsColor, false);
        actionBar.setItemsBackgroundColor(iconsBgColor, false);
        actionBar.setBackgroundColor(bgColor);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == 1) {
                    showChangelog();
                }
            }
        });
        actionBar.createMenu().addItem(1, R.drawable.ic_ab_other).setIconColor(iconsColor);

        FrameLayout root = new FrameLayout(context);
        root.setBackgroundColor(bgColor);

        AuroraView aurora = new AuroraView(context, dark);
        root.addView(aurora, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        LinearLayout center = new LinearLayout(context);
        center.setOrientation(LinearLayout.VERTICAL);
        center.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView logo = new ImageView(context);
        logo.setImageResource(R.drawable.mdgram_icon_3tone);
        center.addView(logo, LayoutHelper.createLinear(180, 180, Gravity.CENTER_HORIZONTAL));

        TextView title = new TextView(context);
        title.setText("MDGram");
        title.setTextColor(textColor);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        title.setTypeface(AndroidUtilities.bold());
        title.setGravity(Gravity.CENTER);
        center.addView(title, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 6, 0, 0));

        TextView version = new TextView(context);
        version.setText("MDGram " + MDGramUpdater.MD_VERSION_NAME);
        version.setTextColor(subTextColor);
        version.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        version.setGravity(Gravity.CENTER);
        center.addView(version, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 6, 0, 0));

        statusView = new TextView(context);
        statusView.setText("");
        statusView.setTextColor(statusColor);
        statusView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        statusView.setGravity(Gravity.CENTER);
        center.addView(statusView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 24, 22, 24, 0));

        root.addView(center, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        actionButton = new TextView(context);
        actionButton.setText("Buscar actualización");
        actionButton.setTextColor(buttonText);
        actionButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        actionButton.setTypeface(AndroidUtilities.bold());
        actionButton.setGravity(Gravity.CENTER);
        actionButton.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp(28), buttonBg, buttonBgPressed));
        actionButton.setOnClickListener(v -> doCheck());
        root.addView(actionButton, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 56, Gravity.BOTTOM, 20, 0, 20, 24));

        fragmentView = root;
        return fragmentView;
    }

    private void doCheck() {
        if (checking) {
            return;
        }
        checking = true;
        actionButton.setText("Buscando actualización…");
        actionButton.setAlpha(0.7f);
        statusView.setText("");
        MDGramUpdater.checkForUpdate((info, error) -> {
            checking = false;
            actionButton.setText("Buscar actualización");
            actionButton.setAlpha(1f);
            if (info != null) {
                if (info.isNewer) {
                    statusView.setText("Actualización disponible: MDGram " + info.versionName);
                    showUpdateDialog(info);
                } else {
                    statusView.setText("Estás en la última versión.");
                }
            } else {
                statusView.setText("No se pudo comprobar. Intenta más tarde.");
            }
        });
    }

    private void showUpdateDialog(MDGramUpdater.UpdateInfo info) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Actualización disponible");
        String msg = "MDGram " + info.versionName;
        if (info.changelog != null && info.changelog.length() > 0) {
            msg += "\n\n" + info.changelog;
        }
        b.setMessage(msg);
        b.setPositiveButton("Descargar", (d, w) -> {
            if (info.apkUrl != null && info.apkUrl.length() > 0 && getParentActivity() != null) {
                try {
                    getParentActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(info.apkUrl)));
                } catch (Exception ignored) {
                }
            }
        });
        b.setNegativeButton("Ahora no", null);
        showDialog(b.create());
    }

    // Menú de 3 puntos → notas de la versión (changelog del manifiesto remoto).
    private void showChangelog() {
        if (checking) {
            return;
        }
        statusView.setText("Cargando notas…");
        MDGramUpdater.checkForUpdate((info, error) -> {
            statusView.setText("");
            if (getParentActivity() == null) {
                return;
            }
            if (info != null && info.changelog != null && info.changelog.length() > 0) {
                AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
                b.setTitle("Notas de la versión");
                String vn = (info.versionName != null && info.versionName.length() > 0) ? (" " + info.versionName) : "";
                b.setMessage("MDGram" + vn + "\n\n" + info.changelog);
                b.setPositiveButton("OK", null);
                showDialog(b.create());
            } else if (info != null) {
                statusView.setText("No hay notas disponibles.");
            } else {
                statusView.setText("No se pudo obtener las notas.");
            }
        });
    }

    // Resplandor radial suave (aurora/nebulosa): teal arriba + ámbar abajo, muy difuminado, con
    // aparición al entrar (fade) y un drift lento. Paleta según tema (más sutil en claro).
    private static class AuroraView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final boolean dark;
        private float appear = 0f;   // 0..1 entrada
        private float phase = 0f;    // drift continuo
        private ValueAnimator appearAnim;
        private ValueAnimator driftAnim;

        AuroraView(Context context, boolean dark) {
            super(context);
            this.dark = dark;
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            appearAnim = ValueAnimator.ofFloat(0f, 1f);
            appearAnim.setDuration(900);
            appearAnim.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            appearAnim.addUpdateListener(a -> {
                appear = (float) a.getAnimatedValue();
                invalidate();
            });
            appearAnim.start();

            driftAnim = ValueAnimator.ofFloat(0f, (float) (2 * Math.PI));
            driftAnim.setDuration(14000);
            driftAnim.setRepeatCount(ValueAnimator.INFINITE);
            driftAnim.setInterpolator(null);
            driftAnim.addUpdateListener(a -> {
                phase = (float) a.getAnimatedValue();
                invalidate();
            });
            driftAnim.start();
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (appearAnim != null) { appearAnim.cancel(); appearAnim = null; }
            if (driftAnim != null) { driftAnim.cancel(); driftAnim = null; }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int w = getWidth();
            int h = getHeight();
            float cx = w / 2f;
            float cy = h / 2f;
            float dx = (float) Math.sin(phase) * dp(18);
            float dy = (float) Math.cos(phase * 0.8f) * dp(14);
            float base = Math.min(w, h);

            int tealColor = dark ? 0xFF3E7C8C : 0xFF5E9DAE;
            int amberColor = dark ? 0xFF9A5A24 : 0xFFD9A05A;
            int tealAlpha = dark ? 150 : 85;
            int amberAlpha = dark ? 130 : 70;

            // Blob teal/azul (arriba)
            float r1 = base * 0.46f;
            int tealCore = ColorUtils.setAlphaComponent(tealColor, (int) (tealAlpha * appear));
            paint.setShader(new RadialGradient(cx + dx, cy - dp(60) + dy, r1,
                    new int[]{tealCore, Color.TRANSPARENT}, new float[]{0f, 1f}, Shader.TileMode.CLAMP));
            canvas.drawCircle(cx + dx, cy - dp(60) + dy, r1, paint);

            // Blob ámbar/naranja (abajo)
            float r2 = base * 0.40f;
            int amberCore = ColorUtils.setAlphaComponent(amberColor, (int) (amberAlpha * appear));
            paint.setShader(new RadialGradient(cx - dx * 0.6f, cy + dp(90) - dy, r2,
                    new int[]{amberCore, Color.TRANSPARENT}, new float[]{0f, 1f}, Shader.TileMode.CLAMP));
            canvas.drawCircle(cx - dx * 0.6f, cy + dp(90) - dy, r2, paint);

            paint.setShader(null);
        }
    }
}
