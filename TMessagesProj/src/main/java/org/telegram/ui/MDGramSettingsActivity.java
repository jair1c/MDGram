package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: hub "Ajustes de MDGram" — réplica del hub del mod original: grid 2x2 + 3 filas + sección
// TELEGRAM anidada (Ajustes de chats, Carpetas, Privacidad, Datos, Notificaciones). Íconos extraídos
// del APK original (plantillas blancas) tintados con el color de cada sección.
public class MDGramSettingsActivity extends BaseFragment {

    // colores de acento por ícono — muestreados EXACTO del hub del original (screenshot)
    private static final int C_GENERAL = 0xFF069DAC;   // teal
    private static final int C_CONV = 0xFFCE2A64;       // rosa
    private static final int C_HOME = 0xFFD2A408;       // dorado
    private static final int C_MODS = 0xFF5720D4;       // morado
    private static final int C_THEMES = 0xFFBC4D75;     // rosa
    private static final int C_TRANSLATE = 0xFF0E56E6;  // azul
    private static final int C_UPDATE = 0xFF06A74A;     // verde
    private static final int C_CHAT = 0xFFC87231;       // naranja
    private static final int C_FOLDERS = 0xFF662AD4;    // morado
    private static final int C_PRIV = 0xFF2255D4;       // azul
    private static final int C_DATA = 0xFF0BBD86;       // verde-teal
    private static final int C_NOTIFY = 0xFFB40958;     // rosa-rojo

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Ajustes de MDGram");
        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        FrameLayout root = new FrameLayout(context);
        root.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));

        ScrollView scroll = new ScrollView(context);
        scroll.setVerticalScrollBarEnabled(false);
        root.addView(scroll, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12), 0, dp(12), dp(16));
        scroll.addView(content, new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        // ---- cabecera "MDGram Messenger" / "Ajuste" ----
        TextView title = new TextView(context);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        title.setTypeface(AndroidUtilities.bold());
        android.text.SpannableStringBuilder header = new android.text.SpannableStringBuilder("MDGram Messenger");
        header.setSpan(new android.text.style.ForegroundColorSpan(getThemedColor(Theme.key_windowBackgroundWhiteBlueText2)), 0, 6, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        header.setSpan(new android.text.style.ForegroundColorSpan(getThemedColor(Theme.key_windowBackgroundWhiteBlackText)), 6, header.length(), android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(header);
        content.addView(title, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 20, 0, 0));

        TextView subtitle = new TextView(context);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        subtitle.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        subtitle.setText("Ajuste");
        content.addView(subtitle, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 18));

        // ---- grid 2x2 ----
        LinearLayout row1 = new LinearLayout(context);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(makeGridCard(context, R.drawable.md_sett_general, C_GENERAL, "General", this::openGeneral), gridLp(true));
        row1.addView(makeGridCard(context, R.drawable.md_sett_conv, C_CONV, "Conversación", () -> presentFragment(new ConversationSettingsActivity())), gridLp(false));
        content.addView(row1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout row2 = new LinearLayout(context);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.addView(makeGridCard(context, R.drawable.md_sett_home, C_HOME, "Pantalla principal", () -> soon("Pantalla principal")), gridLp(true));
        row2.addView(makeGridCard(context, R.drawable.md_sett_others, C_MODS, "Otros mods", () -> presentFragment(new OtrosModsSettingsActivity())), gridLp(false));
        content.addView(row2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 10, 0, 0));

        // ---- 3 filas completas ----
        content.addView(makeRow(context, R.drawable.md_themes_explorer, C_THEMES, "Explorar temas", () -> presentFragment(new ThemeActivity(ThemeActivity.THEME_TYPE_THEMES_BROWSER))), rowLp());
        content.addView(makeRow(context, R.drawable.md_translate, C_TRANSLATE, "Traducir mensajes", () -> presentFragment(new TranslateSettingsActivity())), rowLp());
        content.addView(makeRow(context, R.drawable.md_sett_update, C_UPDATE, "Actualizar", () -> presentFragment(new UpdateActivity())), rowLp());

        // ---- sección TELEGRAM (anidada, como el original) ----
        TextView tgHeader = new TextView(context);
        tgHeader.setText("TELEGRAM");
        tgHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        tgHeader.setTypeface(AndroidUtilities.bold());
        tgHeader.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlueHeader));
        content.addView(tgHeader, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 20, 0, 8));

        LinearLayout tgGroup = new LinearLayout(context);
        tgGroup.setOrientation(LinearLayout.VERTICAL);
        tgGroup.setBackground(Theme.createRoundRectDrawable(dp(12), getThemedColor(Theme.key_windowBackgroundWhite)));
        addGroupedRow(context, tgGroup, R.drawable.md_sett_chat, C_CHAT, "Ajustes de chats", () -> presentFragment(new ThemeActivity(ThemeActivity.THEME_TYPE_BASIC)), true);
        addGroupedRow(context, tgGroup, R.drawable.md_sett_filter, C_FOLDERS, "Carpetas de chats", () -> presentFragment(new FiltersSetupActivity()), false);
        addGroupedRow(context, tgGroup, R.drawable.md_sett_priv, C_PRIV, "Privacidad y seguridad", () -> presentFragment(new PrivacySettingsActivity()), false);
        addGroupedRow(context, tgGroup, R.drawable.md_sett_data, C_DATA, "Datos y almacenamiento", () -> presentFragment(new DataSettingsActivity()), false);
        addGroupedRow(context, tgGroup, R.drawable.md_sett_notify, C_NOTIFY, "Notificaciones y sonidos", () -> presentFragment(new NotificationsSettingsActivity()), false);
        content.addView(tgGroup, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        fragmentView = root;
        return fragmentView;
    }

    private LinearLayout.LayoutParams gridLp(boolean left) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(96), 1f);
        if (left) lp.rightMargin = dp(5);
        else lp.leftMargin = dp(5);
        return lp;
    }

    private LinearLayout.LayoutParams rowLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, dp(58));
        lp.topMargin = dp(10);
        return lp;
    }

    private ImageView icon(Context context, int res, int tint, int size) {
        ImageView iv = new ImageView(context);
        iv.setImageResource(res);
        iv.setColorFilter(new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN));
        return iv;
    }

    // tarjeta del grid
    private View makeGridCard(Context context, int res, int tint, String text, Runnable onClick) {
        FrameLayout card = new FrameLayout(context);
        card.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp(12), getThemedColor(Theme.key_windowBackgroundWhite), getThemedColor(Theme.key_listSelector)));

        card.addView(icon(context, res, tint, 30), LayoutHelper.createFrame(30, 30, Gravity.TOP | Gravity.LEFT, 16, 16, 0, 0));

        TextView tv = new TextView(context);
        tv.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setTypeface(AndroidUtilities.bold());
        tv.setMaxLines(1);
        tv.setText(text);
        card.addView(tv, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT, 16, 0, 30, 14));

        card.addView(chevron(context), LayoutHelper.createFrame(16, 16, Gravity.BOTTOM | Gravity.RIGHT, 0, 0, 12, 16));
        card.setOnClickListener(v -> onClick.run());
        return card;
    }

    // fila completa (tarjeta propia)
    private View makeRow(Context context, int res, int tint, String text, Runnable onClick) {
        LinearLayout row = new LinearLayout(context);
        row.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp(12), getThemedColor(Theme.key_windowBackgroundWhite), getThemedColor(Theme.key_listSelector)));
        fillRow(context, row, res, tint, text);
        row.setOnClickListener(v -> onClick.run());
        return row;
    }

    // fila dentro de un grupo (sin fondo propio; el grupo pone el fondo redondeado)
    private void addGroupedRow(Context context, LinearLayout group, int res, int tint, String text, Runnable onClick, boolean first) {
        if (!first) {
            View divider = new View(context);
            divider.setBackgroundColor(getThemedColor(Theme.key_divider));
            LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1);
            dlp.leftMargin = dp(56);
            group.addView(divider, dlp);
        }
        LinearLayout row = new LinearLayout(context);
        row.setBackground(Theme.getSelectorDrawable(false));
        fillRow(context, row, res, tint, text);
        row.setOnClickListener(v -> onClick.run());
        group.addView(row, new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, dp(52)));
    }

    private void fillRow(Context context, LinearLayout row, int res, int tint, String text) {
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), 0, dp(14), 0);
        row.setMinimumHeight(dp(52));
        row.addView(icon(context, res, tint, 26), LayoutHelper.createLinear(26, 26, Gravity.CENTER_VERTICAL, 0, 0, 18, 0));
        TextView tv = new TextView(context);
        tv.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setTypeface(AndroidUtilities.bold());
        tv.setText(text);
        row.addView(tv, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f, Gravity.CENTER_VERTICAL));
        row.addView(chevron(context), LayoutHelper.createLinear(16, 16, Gravity.CENTER_VERTICAL));
    }

    private ImageView chevron(Context context) {
        ImageView chevron = new ImageView(context);
        chevron.setImageResource(R.drawable.msg_arrowright);
        chevron.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhiteGrayText), PorterDuff.Mode.MULTIPLY));
        return chevron;
    }

    private void openGeneral() {
        presentFragment(new GeneralSettingsActivity());
    }

    private void soon(String what) {
        Toast.makeText(getParentActivity(), what + ": próximamente", Toast.LENGTH_SHORT).show();
    }
}
