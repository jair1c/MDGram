package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
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
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: pantalla General inspirada en V21, con ajustes conectados a sus consumidores.
public class GeneralSettingsActivity extends BaseFragment {

    private BackupImageView previewAvatar;
    private BackupImageView compactAvatar;
    private View previewDarken;
    private View previewGradient;
    private TextView previewName;
    private TextView previewPhone;
    private TextCheckCell blurCell;
    private TextCheckCell darkenCell;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle("General");
        actionBar.setForceLeftTitle(true);
        actionBar.setCastShadows(false);
        actionBar.setItemsColor(0xFF319DFA, false);
        actionBar.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
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

        // ---- App Icono Launcher (selector de íconos alternativos) ----
        HeaderCell iconHeader = new HeaderCell(context);
        iconHeader.setText("App Icono Launcher");
        content.addView(iconHeader);
        content.addView(buildV21IconSelector(context), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 115));

        // ---- preview en vivo de la cabecera del cajón lateral ----
        content.addView(buildPreviewHeader(context), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 140));

        LinearLayout profileGroup = new LinearLayout(context);
        profileGroup.setOrientation(LinearLayout.VERTICAL);
        profileGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        org.telegram.ui.Components.MDGramAppearance.settingsGroup(profileGroup, getResourceProvider());
        content.addView(profileGroup);

        // Foto de perfil como fondo (FUNCIONAL)
        addCheck(context, profileGroup, "Foto de perfil como fondo", MDGramConfig.profilePhotoAsBackground(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setProfilePhotoAsBackground(c);
            blurCell.setVisibility(c ? View.VISIBLE : View.GONE);
            darkenCell.setVisibility(c ? View.VISIBLE : View.GONE);
            refreshPreview();
        });
        // Difuminar foto de perfil (FUNCIONAL — blur nativo del BackupImageView)
        blurCell = addCheck(context, profileGroup, "Difuminar foto de perfil", MDGramConfig.blurProfilePhoto(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setBlurProfilePhoto(c);
            refreshPreview();
        });
        // Foto de perfil oscurecida (FUNCIONAL)
        darkenCell = addCheck(context, profileGroup, "Foto de perfil oscurecida", MDGramConfig.darkenProfilePhoto(), true, cell -> {
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
        blurCell.setVisibility(MDGramConfig.profilePhotoAsBackground() ? View.VISIBLE : View.GONE);
        darkenCell.setVisibility(MDGramConfig.profilePhotoAsBackground() ? View.VISIBLE : View.GONE);

        // ---- Estilos de fuentes ----
        HeaderCell fontsHeader = new HeaderCell(context);
        fontsHeader.setText("Estilos de fuentes");
        content.addView(fontsHeader, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 10, 0, 0));

        LinearLayout fontsGroup = new LinearLayout(context);
        fontsGroup.setOrientation(LinearLayout.VERTICAL);
        fontsGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        org.telegram.ui.Components.MDGramAppearance.settingsGroup(fontsGroup, getResourceProvider());
        content.addView(fontsGroup);

        // Estilos de fuentes (FUNCIONAL → selector de fuentes)
        TextSettingsCell fontStyles = new TextSettingsCell(context);
        fontStyles.applyMDGramSettingsStyle();
        fontStyles.setText("Estilos de fuentes", true);
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
        boostHeader.setText("Acelerador de velocidad de descarga");
        content.addView(boostHeader);
        LinearLayout boostGroup = new LinearLayout(context);
        boostGroup.setOrientation(LinearLayout.VERTICAL);
        boostGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        org.telegram.ui.Components.MDGramAppearance.settingsGroup(boostGroup, getResourceProvider());
        content.addView(boostGroup);
        // Acelerador de descarga (FUNCIONAL → FileLoadOperation), como el control de tres puntos de V21.
        boostGroup.addView(buildDownloadBoostSlider(context));
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
        org.telegram.ui.Components.MDGramAppearance.settingsGroup(genGroup, getResourceProvider());
        content.addView(genGroup);

        // Redondeo de números (FUNCIONAL — hook en LocaleController.formatShortNumber)
        TextCheckCell rounding = new TextCheckCell(context);
        rounding.applyMDGramSettingsStyle();
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

    private View buildV21IconSelector(Context context) {
        LauncherIconController.LauncherIcon[] icons = {
                LauncherIconController.LauncherIcon.MDGRAM_DEFAULT,
                LauncherIconController.LauncherIcon.MDGRAM_MATERIAL,
                LauncherIconController.LauncherIcon.MDGRAM_ORANGE,
                LauncherIconController.LauncherIcon.MDGRAM_GREEN,
                LauncherIconController.LauncherIcon.MDGRAM_YELLOW
        };
        int[] images = {
                R.mipmap.mdgram_v21_default, R.mipmap.mdgram_v21_material,
                R.mipmap.mdgram_v21_orange, R.mipmap.mdgram_v21_green,
                R.mipmap.mdgram_v21_yellow
        };
        String[] names = {"Por defecto", "Material You", "Naranja", "Verde", "Icono amarillo"};
        FrameLayout[] frames = new FrameLayout[icons.length];
        TextView[] labels = new TextView[icons.length];
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.TOP);

        Runnable refresh = () -> {
            for (int i = 0; i < icons.length; i++) {
                boolean selected = LauncherIconController.isEnabled(icons[i]);
                GradientDrawable border = new GradientDrawable();
                border.setColor(getThemedColor(Theme.key_windowBackgroundWhite));
                border.setCornerRadius(dp(23));
                border.setStroke(dp(selected ? 2 : 1), selected ? 0xFF1686FF : 0xFFE7E8EB);
                frames[i].setBackground(border);
                labels[i].setTextColor(selected ? 0xFF1686FF : getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
            }
        };

        for (int i = 0; i < icons.length; i++) {
            final int position = i;
            LinearLayout item = new LinearLayout(context);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER_HORIZONTAL);
            item.setPadding(0, dp(8), 0, 0);
            row.addView(item, new LinearLayout.LayoutParams(0, LayoutHelper.MATCH_PARENT, 1f));

            FrameLayout frame = new FrameLayout(context);
            frames[i] = frame;
            item.addView(frame, LayoutHelper.createLinear(68, 68, Gravity.CENTER_HORIZONTAL));
            ImageView image = new ImageView(context);
            image.setImageResource(images[i]);
            if (icons[i] == LauncherIconController.LauncherIcon.MDGRAM_MATERIAL) {
                image.setOutlineProvider(new android.view.ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, android.graphics.Outline outline) {
                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    }
                });
                image.setClipToOutline(true);
            }
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            frame.addView(image, LayoutHelper.createFrame(60, 60, Gravity.CENTER));

            TextView label = new TextView(context);
            labels[i] = label;
            label.setText(names[i]);
            label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            label.setGravity(Gravity.CENTER);
            label.setSingleLine(true);
            item.addView(label, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 5, 0, 0));
            item.setOnClickListener(v -> {
                try {
                    LauncherIconController.setIcon(icons[position]);
                    refresh.run();
                } catch (Exception e) {
                    Toast.makeText(context, "No se pudo cambiar el icono", Toast.LENGTH_SHORT).show();
                }
            });
        }
        refresh.run();
        return row;
    }

    // cabecera de preview: replica la del cajón lateral para ver el efecto de los toggles en vivo
    private FrameLayout buildPreviewHeader(Context context) {
        FrameLayout header = new FrameLayout(context);
        header.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));

        previewAvatar = new BackupImageView(context);
        previewAvatar.setRoundRadius(0);
        header.addView(previewAvatar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        previewGradient = new View(context);
        previewGradient.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x00000000, 0x11000000, 0xB3000000}));
        header.addView(previewGradient, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        previewDarken = new View(context);
        previewDarken.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x22000000, 0x66000000}));
        header.addView(previewDarken, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        compactAvatar = new BackupImageView(context);
        compactAvatar.setRoundRadius(dp(32));
        header.addView(compactAvatar, LayoutHelper.createFrame(64, 64, Gravity.TOP | Gravity.LEFT, 16, 12, 0, 0));

        LinearLayout headerText = new LinearLayout(context);
        headerText.setOrientation(LinearLayout.VERTICAL);

        previewName = new TextView(context);
        previewName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        previewName.setTypeface(AndroidUtilities.bold());
        previewName.setMaxLines(1);

        previewPhone = new TextView(context);
        previewPhone.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        previewPhone.setMaxLines(1);

        TLRPC.User user = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        if (user != null) {
            previewName.setText(UserObject.getUserName(user));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setInfo(currentAccount, user);
            compactAvatar.setForUserOrChat(user, avatarDrawable);
            if (user.phone != null && user.phone.length() > 0) {
                previewPhone.setText(PhoneFormat.getInstance().format("+" + user.phone));
            }
        }

        headerText.addView(previewName, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
        headerText.addView(previewPhone, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 6, 0, 0));
        header.addView(headerText, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM, 16, 0, 44, 8));

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
        compactAvatar.setVisibility(photoBg ? View.GONE : View.VISIBLE);
        previewGradient.setVisibility(photoBg ? View.VISIBLE : View.GONE);
        previewDarken.setVisibility(photoBg && MDGramConfig.darkenProfilePhoto() ? View.VISIBLE : View.GONE);
        previewName.setTextColor(photoBg ? 0xFFFFFFFF : getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        previewPhone.setTextColor(photoBg ? 0xCCFFFFFF : getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        previewPhone.setVisibility(MDGramConfig.hidePhoneNumber() ? View.GONE : View.VISIBLE);
    }

    private interface CheckClick {
        void onClick(TextCheckCell cell);
    }

    private TextCheckCell addCheck(Context context, LinearLayout group, String text, boolean checked, boolean divider, CheckClick l) {
        TextCheckCell cell = new TextCheckCell(context);
        cell.applyMDGramSettingsStyle();
        cell.setTextAndCheck(text, checked, divider);
        cell.setOnClickListener(v -> l.onClick(cell));
        group.addView(cell);
        return cell;
    }

    private View buildDownloadBoostSlider(Context context) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(16), dp(8), dp(16), dp(4));
        LinearLayout labels = new LinearLayout(context);
        String[] names = {"No", "Rápida", "Ultra"};
        TextView[] values = new TextView[names.length];
        for (int i = 0; i < names.length; i++) {
            TextView label = new TextView(context);
            label.setText(names[i]);
            label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            label.setGravity(i == 0 ? Gravity.LEFT : i == 2 ? Gravity.RIGHT : Gravity.CENTER);
            labels.addView(label, new LinearLayout.LayoutParams(0, LayoutHelper.WRAP_CONTENT, 1f));
            values[i] = label;
        }
        box.addView(labels, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        SeekBar slider = new SeekBar(context);
        slider.setMax(2);
        slider.setProgress(MDGramConfig.downloadSpeedBoost());
        slider.setProgressTintList(ColorStateList.valueOf(0xFF319DFA));
        slider.setThumbTintList(ColorStateList.valueOf(0xFF319DFA));
        GradientDrawable tick = new GradientDrawable();
        tick.setShape(GradientDrawable.OVAL);
        tick.setColor(0xFFACB0B5);
        tick.setSize(dp(5), dp(5));
        slider.setTickMark(tick);
        box.addView(slider, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 38, 0, 6, 0, 0));
        Runnable refresh = () -> {
            for (int i = 0; i < values.length; i++) {
                values[i].setTextColor(i == slider.getProgress() ? 0xFF1686FF : getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
            }
        };
        refresh.run();
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MDGramConfig.setDownloadSpeedBoost(progress);
                    refresh.run();
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        return box;
    }

    private void soon(String what) {
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), what + ": próximamente", Toast.LENGTH_SHORT).show();
        }
    }
}
