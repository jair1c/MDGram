package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.Cells.AppIconsSelectorCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: pantalla "General" del hub "Ajustes de MDGram" (réplica del original).
// Increment 1: cabecera de perfil (foto como fondo / oscurecida / ocultar número) FUNCIONAL — conecta
// con MDGramSideDrawer vía MDGramConfig, con un preview en vivo — y "Estilos de fuentes" (→ FontSelect).
// Diferidos (Toast "próximamente", patrón ya usado en TranslateSettingsActivity): Difuminar, Usar fuente
// del sistema, Redondeo de números, Preguntar antes de llamar. Pendientes de sección completa: los íconos
// de launcher (arriba) y el Acelerador de descarga/carga (medio).
public class GeneralSettingsActivity extends BaseFragment {

    private BackupImageView previewAvatar;
    private View previewDarken;
    private TextView previewPhone;
    private TextSettingsCell dlBoostCell;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("General");
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

        // ---- App Icono Launcher (selector de íconos alternativos) ----
        HeaderCell iconHeader = new HeaderCell(context);
        iconHeader.setText("Ícono de la app");
        content.addView(iconHeader);
        LinearLayout iconGroup = new LinearLayout(context);
        iconGroup.setOrientation(LinearLayout.VERTICAL);
        iconGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        iconGroup.addView(new AppIconsSelectorCell(context, this, currentAccount),
                LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        content.addView(iconGroup);

        // ---- preview en vivo de la cabecera del cajón lateral ----
        content.addView(buildPreviewHeader(context), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 150));

        LinearLayout profileGroup = new LinearLayout(context);
        profileGroup.setOrientation(LinearLayout.VERTICAL);
        profileGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(profileGroup);

        // Foto de perfil como fondo (FUNCIONAL)
        addCheck(context, profileGroup, "Foto de perfil como fondo", MDGramConfig.profilePhotoAsBackground(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setProfilePhotoAsBackground(c);
            refreshPreview();
        });
        // Difuminar foto de perfil (FUNCIONAL — blur nativo del BackupImageView)
        addCheck(context, profileGroup, "Difuminar foto de perfil", MDGramConfig.blurProfilePhoto(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setBlurProfilePhoto(c);
            refreshPreview();
        });
        // Foto de perfil oscurecida (FUNCIONAL)
        addCheck(context, profileGroup, "Foto de perfil oscurecida", MDGramConfig.darkenProfilePhoto(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setDarkenProfilePhoto(c);
            refreshPreview();
        });
        // Ocultar mi número de teléfono (FUNCIONAL)
        addCheck(context, profileGroup, "Ocultar mi número de teléfono", MDGramConfig.hidePhoneNumber(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHidePhoneNumber(c);
            refreshPreview();
        });

        // ---- Estilos de fuentes ----
        HeaderCell fontsHeader = new HeaderCell(context);
        fontsHeader.setText("Estilos de fuentes");
        content.addView(fontsHeader);

        LinearLayout fontsGroup = new LinearLayout(context);
        fontsGroup.setOrientation(LinearLayout.VERTICAL);
        fontsGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(fontsGroup);

        // Estilos de fuentes (FUNCIONAL → selector de fuentes)
        TextSettingsCell fontStyles = new TextSettingsCell(context);
        fontStyles.setText("Fuentes de la app", true);
        fontStyles.setOnClickListener(v -> presentFragment(new FontSelectActivity()));
        fontsGroup.addView(fontStyles);
        // Usar fuente del sistema (FUNCIONAL — gana sobre Linotte/fuente elegida en AndroidUtilities.bold())
        addCheck(context, fontsGroup, "Usar fuente del sistema", SharedConfig.useSystemBoldFont, false, cell -> {
            SharedConfig.toggleUseSystemBoldFont();
            cell.setChecked(SharedConfig.useSystemBoldFont);
            if (getParentActivity() != null) {
                Toast.makeText(getParentActivity(), "Reinicia MDGram para aplicar la fuente", Toast.LENGTH_SHORT).show();
            }
        });

        // ---- Acelerador de velocidad (descarga/carga) ----
        HeaderCell boostHeader = new HeaderCell(context);
        boostHeader.setText("Acelerador de velocidad");
        content.addView(boostHeader);
        LinearLayout boostGroup = new LinearLayout(context);
        boostGroup.setOrientation(LinearLayout.VERTICAL);
        boostGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(boostGroup);
        // Acelerador de descarga (FUNCIONAL → FileLoadOperation) — picker No/Rápida/Ultra
        dlBoostCell = new TextSettingsCell(context);
        dlBoostCell.setTextAndValue("Acelerador de descarga", boostName(MDGramConfig.downloadSpeedBoost()), true);
        dlBoostCell.setOnClickListener(v -> showDownloadBoostPicker());
        boostGroup.addView(dlBoostCell);
        // Acelerador de carga (FUNCIONAL → FileUploadOperation)
        addCheck(context, boostGroup, "Acelerador de carga", MDGramConfig.uploadSpeedBoost(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setUploadSpeedBoost(c);
        });
        TextInfoPrivacyCell boostInfo = new TextInfoPrivacyCell(context);
        boostInfo.setText("El acelerador usa fragmentos más grandes para subir/bajar más rápido. En conexiones lentas o inestables puede causar problemas al descargar archivos o reproducir videos.");
        content.addView(boostInfo);

        // ---- General ----
        HeaderCell genHeader = new HeaderCell(context);
        genHeader.setText("General");
        content.addView(genHeader);

        LinearLayout genGroup = new LinearLayout(context);
        genGroup.setOrientation(LinearLayout.VERTICAL);
        genGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(genGroup);

        // Redondeo de números (FUNCIONAL — hook en LocaleController.formatShortNumber)
        TextCheckCell rounding = new TextCheckCell(context);
        rounding.setTextAndValueAndCheck("Redondeo de números", "35,702 > 35.7k", MDGramConfig.roundNumbers(), true, true);
        rounding.setOnClickListener(v -> {
            boolean c = !rounding.isChecked();
            rounding.setChecked(c);
            MDGramConfig.setRoundNumbers(c);
            if (getParentActivity() != null) {
                Toast.makeText(getParentActivity(), "Reinicia MDGram para aplicarlo en todos lados", Toast.LENGTH_SHORT).show();
            }
        });
        genGroup.addView(rounding);
        // Preguntar antes de llamar (FUNCIONAL — hook en VoIPHelper.startCall)
        addCheck(context, genGroup, "Preguntar antes de llamar", MDGramConfig.askBeforeCalling(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setAskBeforeCalling(c);
        });

        // footer
        TextInfoPrivacyCell info = new TextInfoPrivacyCell(context);
        info.setText("La foto de perfil, el difuminado y el oscurecido se aplican a la cabecera del menú lateral (la hamburguesa de la lista de chats).");
        content.addView(info);

        fragmentView = root;
        return fragmentView;
    }

    // cabecera de preview: replica la del cajón lateral para ver el efecto de los toggles en vivo
    private FrameLayout buildPreviewHeader(Context context) {
        FrameLayout header = new FrameLayout(context);
        header.setBackgroundColor(ThemeColors.TELEGRAM_COLOR);

        previewAvatar = new BackupImageView(context);
        previewAvatar.setRoundRadius(0);
        header.addView(previewAvatar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        View gradient = new View(context);
        gradient.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x00000000, 0x11000000, 0xB3000000}));
        header.addView(gradient, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        previewDarken = new View(context);
        previewDarken.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x22000000, 0x66000000}));
        header.addView(previewDarken, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        LinearLayout headerText = new LinearLayout(context);
        headerText.setOrientation(LinearLayout.VERTICAL);

        TextView nameView = new TextView(context);
        nameView.setTextColor(0xFFFFFFFF);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        nameView.setTypeface(AndroidUtilities.bold());
        nameView.setMaxLines(1);

        previewPhone = new TextView(context);
        previewPhone.setTextColor(0xCCFFFFFF);
        previewPhone.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        previewPhone.setMaxLines(1);

        TLRPC.User user = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        if (user != null) {
            nameView.setText(UserObject.getUserName(user));
            if (user.phone != null && user.phone.length() > 0) {
                previewPhone.setText(PhoneFormat.getInstance().format("+" + user.phone));
            }
        }

        headerText.addView(nameView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
        headerText.addView(previewPhone, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 0));
        header.addView(headerText, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM, 16, 0, 44, 12));

        refreshPreview();
        return header;
    }

    private void refreshPreview() {
        boolean photoBg = MDGramConfig.profilePhotoAsBackground();
        TLRPC.User user = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        if (photoBg && user != null) {
            previewAvatar.setVisibility(View.VISIBLE);
            // resolución completa (no pixelada) + mismo blur RenderEffect suave que el cajón lateral
            org.telegram.ui.Components.MDGramSideDrawer.loadProfilePhotoFull(previewAvatar, user);
            org.telegram.ui.Components.MDGramSideDrawer.applyProfileBlur(previewAvatar, MDGramConfig.blurProfilePhoto());
        } else {
            previewAvatar.setVisibility(View.GONE);
        }
        previewDarken.setVisibility(photoBg && MDGramConfig.darkenProfilePhoto() ? View.VISIBLE : View.GONE);
        previewPhone.setVisibility(MDGramConfig.hidePhoneNumber() ? View.GONE : View.VISIBLE);
    }

    private interface CheckClick {
        void onClick(TextCheckCell cell);
    }

    private TextCheckCell addCheck(Context context, LinearLayout group, String text, boolean checked, boolean divider, CheckClick l) {
        TextCheckCell cell = new TextCheckCell(context);
        cell.setTextAndCheck(text, checked, divider);
        cell.setOnClickListener(v -> l.onClick(cell));
        group.addView(cell);
        return cell;
    }

    private String boostName(int level) {
        switch (level) {
            case MDGramConfig.BOOST_AVERAGE: return "Rápida";
            case MDGramConfig.BOOST_EXTREME: return "Ultra";
            default: return "No";
        }
    }

    private void showDownloadBoostPicker() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Acelerador de descarga");
        b.setItems(new CharSequence[]{"No", "Rápida", "Ultra"}, (d, which) -> {
            MDGramConfig.setDownloadSpeedBoost(which);
            dlBoostCell.setTextAndValue("Acelerador de descarga", boostName(which), true);
        });
        showDialog(b.create());
    }

    private void soon(String what) {
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), what + ": próximamente", Toast.LENGTH_SHORT).show();
        }
    }
}
