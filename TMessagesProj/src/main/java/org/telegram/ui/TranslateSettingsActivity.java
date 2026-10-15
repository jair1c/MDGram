package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramTranslator;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
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
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;

// MDGram: pantalla "Traducir" del hub (réplica del original). Enlaza con nuestro motor MDGramTranslator.
// Botón, proveedor, idioma, exclusiones y conservación de formato están conectados.
public class TranslateSettingsActivity extends BaseFragment {

    private TextSettingsCell providerCell;
    private TextSettingsCell idiomaCell;
    private TextInfoPrivacyCell providerInfoCell;
    private TextSettingsCell excludedLanguagesCell;
    private TextCheckCell keepFormattingCell;
    private TextCheckCell wholeChatsCell;
    private TextSettingsCell translationTypeCell;
    private TextSettingsCell automaticCell;

    private static final String[] LANG_NAMES = {"Por defecto", "Español", "Inglés", "Portugués", "Francés", "Alemán", "Italiano", "Ruso", "Chino", "Japonés", "Coreano", "Árabe", "Hindi", "Turco"};
    private static final String[] LANG_CODES = {null, "es", "en", "pt", "fr", "de", "it", "ru", "zh", "ja", "ko", "ar", "hi", "tr"};

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setForceLeftTitle(true);
        actionBar.setCastShadows(false);
        actionBar.setItemsColor(0xFF319DFA, false);
        actionBar.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
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
        root.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));

        ScrollView scroll = new ScrollView(context);
        scroll.setVerticalScrollBarEnabled(false);
        root.addView(scroll, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(content, new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        org.telegram.ui.Components.MDGramAppearance.settingsPage(root, content, getResourceProvider());

        HeaderCell header = new HeaderCell(context);
        header.setText("Traducir mensajes");
        content.addView(header);

        LinearLayout group = new LinearLayout(context);
        group.setOrientation(LinearLayout.VERTICAL);
        group.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        org.telegram.ui.Components.MDGramAppearance.settingsGroup(group, getResourceProvider());
        content.addView(group);

        // Mostrar botón "Traducir" (FUNCIONAL → pref translate_button de Telegram)
        TextCheckCell showButton = new TextCheckCell(context);
        showButton.applyMDGramSettingsStyle();
        showButton.setTextAndCheck("Mostrar botón “Traducir”", getMessagesController().getTranslateController().isContextTranslateEnabled(), true);
        showButton.setOnClickListener(v -> {
            boolean c = !showButton.isChecked();
            showButton.setChecked(c);
            getMessagesController().getTranslateController().setContextTranslateEnabled(c);
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateSearchSettings);
        });
        group.addView(showButton);

        // Usa la disponibilidad y la preferencia de traducción de chats de Telegram.
        TextCheckCell wholeChats = new TextCheckCell(context);
        wholeChats.applyMDGramSettingsStyle();
        wholeChatsCell = wholeChats;
        updateWholeChatsControl();
        wholeChats.setOnClickListener(v -> {
            boolean enabled = !wholeChats.isChecked();
            if (enabled && !MDGramTranslator.enabled() && !getUserConfig().isPremium()) {
                showDialog(new PremiumFeatureBottomSheet(this, PremiumPreviewFragment.PREMIUM_FEATURE_TRANSLATIONS, false));
                return;
            }
            getMessagesController().getTranslateController().setChatTranslateEnabled(enabled);
            updateWholeChatsControl();
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateSearchSettings);
        });
        group.addView(wholeChats);

        translationTypeCell = new TextSettingsCell(context);
        translationTypeCell.applyMDGramSettingsStyle();
        translationTypeCell.setOnClickListener(v -> showTypePicker());
        group.addView(translationTypeCell);

        // Proveedor (FUNCIONAL → MDGramTranslator)
        providerCell = new TextSettingsCell(context);
        providerCell.applyMDGramSettingsStyle();
        providerCell.setTextAndValue("Proveedor", providerName(), true);
        providerCell.setOnClickListener(v -> showProviderPicker());
        group.addView(providerCell);

        // Idioma de traducción (FUNCIONAL → idioma destino de la traducción)
        idiomaCell = new TextSettingsCell(context);
        idiomaCell.applyMDGramSettingsStyle();
        idiomaCell.setTextAndValue("Idioma de traducción", currentTargetName(), true);
        idiomaCell.setOnClickListener(v -> showLanguagePicker());
        group.addView(idiomaCell);

        // Exclusiones compartidas con el menú de traducción de Telegram.
        excludedLanguagesCell = new TextSettingsCell(context);
        excludedLanguagesCell.applyMDGramSettingsStyle();
        updateExcludedLanguages();
        excludedLanguagesCell.setOnClickListener(v -> presentFragment(new RestrictedLanguagesSelectActivity()));
        group.addView(excludedLanguagesCell);
        automaticCell = new TextSettingsCell(context);
        automaticCell.applyMDGramSettingsStyle();
        automaticCell.setOnClickListener(v -> showAutomaticPicker());
        group.addView(automaticCell);
        updateTranslationControls();

        // Conservación de entidades para los motores propios.
        TextCheckCell keepMd = new TextCheckCell(context);
        keepMd.applyMDGramSettingsStyle();
        keepFormattingCell = keepMd;
        updateFormattingControl();
        keepMd.setOnClickListener(v -> {
            if (MDGramTranslator.enabled()) {
                MDGramTranslator.setKeepFormatting(!keepMd.isChecked());
                updateFormattingControl();
                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateSearchSettings);
            }
        });
        group.addView(keepMd);

        // footer
        providerInfoCell = new TextInfoPrivacyCell(context);
        updateProviderInfo();
        content.addView(providerInfoCell);

        fragmentView = root;
        return fragmentView;
    }

    private void addValue(Context context, LinearLayout group, String text, String value, Runnable onClick, boolean divider) {
        TextSettingsCell cell = new TextSettingsCell(context);
        cell.applyMDGramSettingsStyle();
        cell.setTextAndValue(text, value, divider);
        cell.setOnClickListener(v -> onClick.run());
        group.addView(cell);
    }

    private void updateProviderInfo() {
        if (providerInfoCell != null) {
            providerInfoCell.setText("El botón “Traducir” aparecerá al realizar un solo toque en un mensaje de texto.\n\n"
                    + providerName() + " puede tener acceso a los mensajes que traduzcas.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateExcludedLanguages();
        updateFormattingControl();
        updateWholeChatsControl();
        updateTranslationControls();
    }

    private void updateWholeChatsControl() {
        if (wholeChatsCell == null) return;
        boolean available = MDGramTranslator.enabled() || getMessagesController().isTranslationsAutoEnabled();
        wholeChatsCell.setTextAndValueAndCheck("Traducir chats enteros", MDGramTranslator.enabled()
                ? "Sin Premium. Activa la traducción desde la barra de cada chat; los textos visibles se envían al proveedor elegido."
                : available
                ? "Traducción completa en chats compatibles con Telegram Premium. No traduce Mensajes guardados."
                : "La traducción de chats no está disponible para esta cuenta en Telegram.",
                getMessagesController().getTranslateController().isChatTranslateEnabled(), true, true);
        wholeChatsCell.setEnabled(available);
        wholeChatsCell.setAlpha(available ? 1f : 0.5f);
    }

    private void updateExcludedLanguages() {
        if (excludedLanguagesCell == null) return;
        java.util.ArrayList<String> codes = new java.util.ArrayList<>(RestrictedLanguagesSelectActivity.getRestrictedLanguages());
        java.util.Collections.sort(codes);
        java.util.ArrayList<String> names = new java.util.ArrayList<>();
        for (String code : codes) {
            String name = code.toUpperCase(java.util.Locale.ROOT);
            for (int i = 1; i < LANG_CODES.length; i++) {
                if (code.equals(LANG_CODES[i])) { name = LANG_NAMES[i]; break; }
            }
            names.add(name);
        }
        excludedLanguagesCell.setTextAndValue("No traducir", names.isEmpty() ? "Ninguno" : android.text.TextUtils.join(", ", names), true);
    }

    private void updateFormattingControl() {
        if (keepFormattingCell == null) return;
        boolean available = MDGramTranslator.enabled();
        keepFormattingCell.setTextAndValueAndCheck("Mantener Markdown", available
                ? "Conservar negritas, cursivas, enlaces y otros formatos al traducir."
                : "Telegram administra el formato; este ajuste se aplica a Google y Yandex.", MDGramTranslator.keepFormatting(), true, false);
        keepFormattingCell.setEnabled(available);
        keepFormattingCell.setAlpha(available ? 1f : 0.5f);
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
            updateProviderInfo();
            updateFormattingControl();
            updateWholeChatsControl();
            updateTranslationControls();
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateSearchSettings);
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

    private void updateTranslationControls() {
        boolean available = MDGramTranslator.enabled();
        if (translationTypeCell != null) {
            translationTypeCell.setTextAndValue("Tipo de traducción", MDGramTranslator.inlineMode() ? "Dentro del mensaje" : "Ventana flotante", true);
            translationTypeCell.setEnabled(available);
            translationTypeCell.setAlpha(available ? 1f : 0.5f);
        }
        if (automaticCell != null) {
            automaticCell.setTextAndValue("Traducir automáticamente", MDGramTranslator.autoChats() ? "Al abrir chats" : "Nunca", true);
            automaticCell.setEnabled(available);
            automaticCell.setAlpha(available ? 1f : 0.5f);
        }
    }

    private void showTypePicker() {
        if (getParentActivity() == null || !MDGramTranslator.enabled()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle("Tipo de traducción");
        builder.setItems(new CharSequence[]{"Ventana flotante", "Dentro del mensaje"}, (dialog, which) -> {
            MDGramTranslator.setInlineMode(which == 1);
            updateTranslationControls();
        });
        showDialog(builder.create());
    }

    private void showAutomaticPicker() {
        if (getParentActivity() == null || !MDGramTranslator.enabled()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle("Traducir automáticamente");
        builder.setMessage("Al abrir chats, los textos visibles y cercanos se enviarán al proveedor elegido. Se respetan los idiomas excluidos y los chats donde hayas apagado la traducción.");
        builder.setItems(new CharSequence[]{"Nunca", "Al abrir chats"}, (dialog, which) -> {
            MDGramTranslator.setAutoChats(which == 1);
            if (which == 1) getMessagesController().getTranslateController().setChatTranslateEnabled(true);
            updateWholeChatsControl();
            updateTranslationControls();
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateSearchSettings);
        });
        showDialog(builder.create());
    }
}
