package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramTranslator;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.ui.Components.TranslateAlert2;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: pantalla "Traducir" del hub (réplica del original). Enlaza con nuestro motor MDGramTranslator.
// Etapa 1: "Mostrar botón Traducir" y "Proveedor" funcionales; el resto visual (a llenar gradualmente).
public class TranslateSettingsActivity extends BaseFragment {

    private TextSettingsCell providerCell;
    private TextSettingsCell idiomaCell;

    private static final String[] LANG_NAMES = {"Por defecto", "Español", "Inglés", "Portugués", "Francés", "Alemán", "Italiano", "Ruso", "Chino", "Japonés", "Coreano", "Árabe", "Hindi", "Turco"};
    private static final String[] LANG_CODES = {null, "es", "en", "pt", "fr", "de", "it", "ru", "zh", "ja", "ko", "ar", "hi", "tr"};

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Traducir");
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
        scroll.addView(content, new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        HeaderCell header = new HeaderCell(context);
        header.setText("Traducir mensajes");
        content.addView(header);

        LinearLayout group = new LinearLayout(context);
        group.setOrientation(LinearLayout.VERTICAL);
        group.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(group);

        // Mostrar botón "Traducir" (FUNCIONAL → pref translate_button de Telegram)
        TextCheckCell showButton = new TextCheckCell(context);
        showButton.setTextAndCheck("Mostrar botón “Traducir”", getMessagesController().getTranslateController().isContextTranslateEnabled(), true);
        showButton.setOnClickListener(v -> {
            boolean c = !showButton.isChecked();
            showButton.setChecked(c);
            getMessagesController().getTranslateController().setContextTranslateEnabled(c);
        });
        group.addView(showButton);

        // Traducir chats enteros (visual por ahora)
        TextCheckCell wholeChats = new TextCheckCell(context);
        wholeChats.setTextAndValueAndCheck("Traducir chats enteros", "Muestra la barra de traducción bajo el título del chat", false, true, true);
        wholeChats.setOnClickListener(v -> soon("Traducir chats enteros"));
        group.addView(wholeChats);

        // Tipo de traducción (visual)
        addValue(context, group, "Tipo de traducción", "En el mensaje", () -> soon("Tipo de traducción"), true);

        // Proveedor (FUNCIONAL → MDGramTranslator)
        providerCell = new TextSettingsCell(context);
        providerCell.setTextAndValue("Proveedor", providerName(), true);
        providerCell.setOnClickListener(v -> showProviderPicker());
        group.addView(providerCell);

        // Idioma de traducción (FUNCIONAL → idioma destino de la traducción)
        idiomaCell = new TextSettingsCell(context);
        idiomaCell.setTextAndValue("Idioma de traducción", currentTargetName(), true);
        idiomaCell.setOnClickListener(v -> showLanguagePicker());
        group.addView(idiomaCell);

        // No traducir / Traducir automáticamente (visual)
        addValue(context, group, "No traducir", "Por defecto", () -> soon("No traducir"), true);
        addValue(context, group, "Traducir automáticamente", "Nunca", () -> soon("Traducir automáticamente"), true);

        // Mantener Markdown (visual por ahora — el motor MVP aún traduce solo texto)
        TextCheckCell keepMd = new TextCheckCell(context);
        keepMd.setTextAndValueAndCheck("Mantener Markdown", "Al traducir un mensaje, mantener el formato en markdown.", true, true, false);
        keepMd.setOnClickListener(v -> soon("Mantener Markdown"));
        group.addView(keepMd);

        // footer
        TextInfoPrivacyCell info = new TextInfoPrivacyCell(context);
        info.setText("El botón “Traducir” aparecerá al realizar un solo toque en un mensaje de texto.\n\nEl proveedor elegido puede tener acceso a los mensajes que traduzcas.");
        content.addView(info);

        fragmentView = root;
        return fragmentView;
    }

    private void addValue(Context context, LinearLayout group, String text, String value, Runnable onClick, boolean divider) {
        TextSettingsCell cell = new TextSettingsCell(context);
        cell.setTextAndValue(text, value, divider);
        cell.setOnClickListener(v -> onClick.run());
        group.addView(cell);
    }

    private String providerName() {
        switch (MDGramTranslator.getProvider()) {
            case MDGramTranslator.PROVIDER_YANDEX: return "Yandex";
            case MDGramTranslator.PROVIDER_TELEGRAM: return "Telegram";
            default: return "Traductor de Google";
        }
    }

    private void showProviderPicker() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Proveedor");
        b.setItems(new CharSequence[]{"Traductor de Google", "Yandex", "Telegram"}, (d, which) -> {
            int p = which == 0 ? MDGramTranslator.PROVIDER_GOOGLE : which == 1 ? MDGramTranslator.PROVIDER_YANDEX : MDGramTranslator.PROVIDER_TELEGRAM;
            MDGramTranslator.setProvider(p);
            providerCell.setTextAndValue("Proveedor", providerName(), true);
        });
        showDialog(b.create());
    }

    // idioma destino de la traducción (pref translate_to_language de Telegram)
    private String currentTargetName() {
        String code = MessagesController.getGlobalMainSettings().getString("translate_to_language", null);
        if (code == null) {
            return "Por defecto";
        }
        for (int i = 1; i < LANG_CODES.length; i++) {
            if (code.equals(LANG_CODES[i])) {
                return LANG_NAMES[i];
            }
        }
        return code.toUpperCase();
    }

    private void showLanguagePicker() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Idioma de traducción");
        b.setItems(LANG_NAMES, (d, which) -> {
            if (which == 0) {
                TranslateAlert2.resetToLanguage();
            } else {
                TranslateAlert2.setToLanguage(LANG_CODES[which]);
            }
            idiomaCell.setTextAndValue("Idioma de traducción", currentTargetName(), true);
        });
        showDialog(b.create());
    }

    private void soon(String what) {
        Toast.makeText(getParentActivity(), what + ": próximamente", Toast.LENGTH_SHORT).show();
    }
}
