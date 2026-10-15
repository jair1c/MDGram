package org.telegram.ui.Components;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Canvas;
import android.os.Build;
import org.telegram.ui.MainTabsActivity;
import org.telegram.ui.Components.blur3.BlurredBackgroundDrawableViewFactory;
import org.telegram.ui.Components.blur3.drawable.BlurredBackgroundDrawable;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceRenderNode;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.CallLogActivity;
import org.telegram.ui.ChannelCreateActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ContactsActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.SettingsActivity;

// MDGram: cajón de navegación lateral (réplica del drawer del MDGram original). Panel deslizable propio
// (no usa el DrawerLayoutContainer, que en este fork quedó vaciado). Es un FrameLayout de pantalla
// completa: scrim de fondo + panel a la izquierda que entra/sale con translación.
public class MDGramSideDrawer extends FrameLayout {

    private final LaunchActivity activity;
    private final View scrim;
    private final FrameLayout panel;
    private FrameLayout header;
    private View darkenOverlay;
    private BackupImageView avatarView;
    private TextView nameView;
    private TextView phoneView;
    private ImageView chevron;
    private LinearLayout accountsSection;
    private boolean accountsExpanded;
    private View divider;
    private final java.util.ArrayList<ImageView> itemIcons = new java.util.ArrayList<>();
    private final java.util.ArrayList<TextView> itemTexts = new java.util.ArrayList<>();
    private final int panelWidth;

    private boolean open;
    private AnimatorSet animator;
    private boolean modalStyle;
    private final java.util.ArrayList<TextView> modalTexts = new java.util.ArrayList<>();
    private ImageView modalMoon, modalDismiss;
    private TextView modalUsername, modalManage;

    private GradientDrawable accountPillBackground() {
        int surface = Theme.getColor(Theme.key_windowBackgroundWhite);
        int text = Theme.getColor(Theme.key_windowBackgroundWhiteBlackText);
        GradientDrawable background = new GradientDrawable();
        background.setCornerRadius(dp(24));
        background.setColor(Theme.isCurrentThemeDark() ? androidx.core.graphics.ColorUtils.blendARGB(surface, text, 0.10f) : surface);
        background.setStroke(dp(1), Theme.isCurrentThemeDark() ? androidx.core.graphics.ColorUtils.blendARGB(surface, text, 0.35f) : Theme.getColor(Theme.key_divider));
        return background;
    }

    private TextView modalText(String text, int size) {
        TextView view = new TextView(activity);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        view.setTypeface(AndroidUtilities.bold());
        modalTexts.add(view);
        return view;
    }

    private BlurredBackgroundDrawable glassBackdrop;
    private View glassSourceView;
    private final int[] glassPanelPosition = new int[2];
    private final int[] glassRootPosition = new int[2];

    private void updateModalMaterial() {
        glassBackdrop = null;
        glassSourceView = null;
        if (org.telegram.messenger.MDGramConfig.useLiquidGlass() && Build.VERSION.SDK_INT >= 31
                && activity.getActionBarLayout().getLastFragment() instanceof MainTabsActivity) {
            MainTabsActivity home = (MainTabsActivity) activity.getActionBarLayout().getLastFragment();
            home.setMDGramGlassPanelVisible(true);
            BlurredBackgroundSourceRenderNode source = home.getMDGramGlassSource();
            if (source != null) {
                BlurredBackgroundDrawableViewFactory factory = new BlurredBackgroundDrawableViewFactory(source);
                factory.setLiquidGlassEffectAllowed(MDGramGlass.useLens());
                glassBackdrop = factory.create(panel, MDGramGlass.material(null, false));
                glassBackdrop.setRadius(dp(30));
                glassBackdrop.setPadding(0);
                glassSourceView = home.getFragmentView();
                panel.setBackground(glassBackdrop);
                return;
            }
        }
        panel.setBackground(Theme.createRoundRectDrawable(dp(30), Theme.getColor(Theme.key_windowBackgroundWhite)));
    }

    private void buildModalContent() {
        modalTexts.clear();
        panel.removeAllViews();
        itemIcons.clear();
        itemTexts.clear();
        panel.setTranslationX(0);
        updateModalMaterial();
        int width = Math.min(dp(420), AndroidUtilities.displaySize.x - dp(40));
        panel.setLayoutParams(new FrameLayout.LayoutParams(width, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        ScrollView scroll = new ScrollView(activity) {
            @Override protected void onMeasure(int widthSpec, int heightSpec) {
                super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y - dp(100), MeasureSpec.AT_MOST));
            }
        };
        scroll.setVerticalScrollBarEnabled(false);
        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(18));
        FrameLayout heading = new FrameLayout(activity);
        TextView title = modalText("MDGram", 22);
        title.setGravity(Gravity.CENTER);
        heading.addView(title, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48));
        ImageView dismiss = new ImageView(activity);
        modalDismiss = dismiss;
        dismiss.setImageResource(R.drawable.ic_close_white);
        dismiss.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        dismiss.setScaleType(ImageView.ScaleType.CENTER);
        dismiss.setContentDescription("Cerrar menú");
        dismiss.setOnClickListener(v -> close());
        heading.addView(dismiss, LayoutHelper.createFrame(48, 48, Gravity.LEFT));
        ImageView moon = new ImageView(activity);
        modalMoon = moon;
        moon.setImageResource(Theme.isCurrentThemeDark() ? R.drawable.menu_day_mode_24 : R.drawable.menu_night_mode_24);
        moon.setScaleType(ImageView.ScaleType.CENTER);
        moon.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        moon.setContentDescription("Cambiar tema claro u oscuro");
        moon.setOnClickListener(v -> {
            boolean dark = !Theme.isCurrentThemeDark();
            Theme.ThemeInfo theme = Theme.getTheme(dark ? "Dark Blue" : "Day");
            AndroidUtilities.runOnUIThread(() -> NotificationCenter.getGlobalInstance().postNotificationName(
                    NotificationCenter.needSetDayNightTheme, theme, false,
                    null, -1, dark, null, null, null, true));
        });
        heading.addView(moon, LayoutHelper.createFrame(48, 48, Gravity.RIGHT));
        content.addView(heading);
        TLRPC.User user = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        LinearLayout identity = new LinearLayout(activity);
        identity.setGravity(Gravity.CENTER_VERTICAL);
        identity.setPadding(dp(16), 0, dp(16), 0);
        BackupImageView avatar = new BackupImageView(activity);
        avatar.setRoundRadius(dp(28));
        avatar.setForUserOrChat(user, new AvatarDrawable(user));
        identity.addView(avatar, LayoutHelper.createLinear(56, 56, Gravity.CENTER_VERTICAL, 0, 0, 16, 0));
        LinearLayout labels = new LinearLayout(activity);
        labels.setOrientation(LinearLayout.VERTICAL);
        TextView name = modalText(UserObject.getUserName(user), 18);
        name.setSingleLine(true);
        name.setEllipsize(android.text.TextUtils.TruncateAt.END);
        labels.addView(name);
        TextView username = modalText(user != null && user.username != null && !user.username.isEmpty() ? "@" + user.username : "Mi cuenta", 15);
        modalUsername = username;
        username.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        labels.addView(username);
        identity.addView(labels, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));
        content.addView(identity, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 88));
        TextView manage = modalText("Gestionar tu cuenta", 16);
        modalManage = manage;
        manage.setGravity(Gravity.CENTER);
        manage.setBackground(accountPillBackground());
        manage.setElevation(dp(2));
        manage.setOnClickListener(v -> {
            close();
            AndroidUtilities.runOnUIThread(() -> present(new SettingsActivity(new android.os.Bundle())), 220);
        });
        content.addView(manage, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 44, 16, 0, 16, 24));
        addItem(content, R.drawable.msg_groups, "Nuevo grupo", this::newGroup);
        addItem(content, R.drawable.msg_channel, "Nuevo canal", this::newChannel);
        addItem(content, R.drawable.msg2_devices, "Dispositivos vinculados", () -> present(new org.telegram.ui.SessionsActivity(0)));
        addItem(content, R.drawable.msg_calls, "Llamadas recientes", () -> present(new CallLogActivity()));
        addItem(content, R.drawable.msg_contacts, "Contactos", () -> present(new ContactsActivity(new android.os.Bundle())));
        View gap = new View(activity);
        content.addView(gap, LayoutHelper.createLinear(1, 16));
        addItem(content, R.drawable.msg_settings, "Ajustes de MDGram", () -> present(new org.telegram.ui.MDGramSettingsActivity()));
        addItem(content, R.drawable.msg2_help, "Ayuda y comentarios", () -> org.telegram.messenger.browser.Browser.openUrl(activity, "https://telegram.org/faq"));
        LinearLayout footer = new LinearLayout(activity);
        footer.setGravity(Gravity.CENTER);
        TextView privacy = modalText("Privacidad", 12);
        TextView terms = modalText("  ·  Condiciones de servicio", 12);
        privacy.setOnClickListener(v -> org.telegram.messenger.browser.Browser.openUrl(activity, "https://telegram.org/privacy"));
        terms.setOnClickListener(v -> org.telegram.messenger.browser.Browser.openUrl(activity, "https://telegram.org/tos"));
        footer.addView(privacy);
        footer.addView(terms);
        content.addView(footer, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 44));
        scroll.addView(content);
        panel.addView(scroll);
    }

    public MDGramSideDrawer(LaunchActivity activity) {
        super(activity);
        this.activity = activity;
        panelWidth = Math.min(dp(300), (int) (AndroidUtilities.displaySize.x * 0.86f));
        setVisibility(GONE);

        scrim = new View(activity);
        scrim.setBackgroundColor(0x66000000);
        scrim.setAlpha(0f);
        scrim.setOnClickListener(v -> close());
        addView(scrim, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        panel = new FrameLayout(activity) {
            @Override public void draw(Canvas canvas) {
                if (glassBackdrop != null && glassSourceView != null) {
                    getLocationOnScreen(glassPanelPosition);
                    glassSourceView.getLocationOnScreen(glassRootPosition);
                    glassBackdrop.setSourceOffset(glassPanelPosition[0] - glassRootPosition[0],
                            glassPanelPosition[1] - glassRootPosition[1]);
                }
                super.draw(canvas);
            }
        };
        panel.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        panel.setTranslationX(-panelWidth);
        panel.setElevation(dp(8));
        panel.setOnClickListener(v -> {}); // consume toques para que no cierren el drawer
        addView(panel, new FrameLayout.LayoutParams(panelWidth, LayoutHelper.MATCH_PARENT, Gravity.LEFT | Gravity.TOP));

        buildContent();
    }

    private void buildContent() {
        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);

        // ---- cabecera de perfil (foto de fondo + nombre + teléfono) ----
        header = new FrameLayout(activity);

        avatarView = new BackupImageView(activity);
        avatarView.setRoundRadius(0);
        header.addView(avatarView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        View gradient = new View(activity);
        gradient.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x00000000, 0x11000000, 0xB3000000}));
        header.addView(gradient, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        // overlay de oscurecido (opcional, controlado por General → "Foto de perfil oscurecida")
        darkenOverlay = new View(activity);
        darkenOverlay.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x22000000, 0x66000000}));
        header.addView(darkenOverlay, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        LinearLayout headerText = new LinearLayout(activity);
        headerText.setOrientation(LinearLayout.VERTICAL);

        nameView = new TextView(activity);
        nameView.setTextColor(0xFFFFFFFF);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        nameView.setTypeface(AndroidUtilities.bold());
        nameView.setMaxLines(1);
        headerText.addView(nameView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        phoneView = new TextView(activity);
        phoneView.setTextColor(0xCCFFFFFF);
        phoneView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        phoneView.setMaxLines(1);
        headerText.addView(phoneView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 0));

        header.addView(headerText, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM, 16, 0, 44, 12));

        // chevron: expande/colapsa el selector de cuentas (igual que el original)
        chevron = new ImageView(activity);
        chevron.setImageResource(R.drawable.arrow_more);
        chevron.setColorFilter(new PorterDuffColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_IN));
        chevron.setScaleType(ImageView.ScaleType.CENTER);
        chevron.setBackground(Theme.createSelectorDrawable(0x33FFFFFF));
        chevron.setOnClickListener(v -> toggleAccounts());
        header.addView(chevron, LayoutHelper.createFrame(40, 40, Gravity.BOTTOM | Gravity.RIGHT, 0, 0, 6, 8));

        content.addView(header, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 150));

        // sección de cuentas (oculta hasta tocar el chevron): cuentas activas + "Añadir cuenta" + divisor
        accountsSection = new LinearLayout(activity);
        accountsSection.setOrientation(LinearLayout.VERTICAL);
        accountsSection.setVisibility(View.GONE);
        content.addView(accountsSection, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        // ---- opciones ----
        addItem(content, R.drawable.msg_openprofile, "Mi perfil", this::openMyProfile);
        addItem(content, R.drawable.msg_payment_card, "Billetera", () -> soon("Billetera"));
        content.addView(makeDivider());
        addItem(content, R.drawable.msg_groups, "Nuevo grupo", this::newGroup);
        addItem(content, R.drawable.msg_channel, "Nuevo canal", this::newChannel);
        addItem(content, R.drawable.msg_contacts, "Contactos", () -> present(new ContactsActivity(new android.os.Bundle())));
        addItem(content, R.drawable.msg_calls, "Llamadas", () -> present(new CallLogActivity()));
        addItem(content, R.drawable.msg_location, "Personas cerca", () -> soon("Personas cerca"));
        addItem(content, R.drawable.msg_archive, "Chats archivados", this::openArchived);
        addItem(content, R.drawable.msg_saved, "Mensajes guardados", this::openSaved);
        addItem(content, R.drawable.md_sett_general, "Ajustes de MDGram", () -> present(new org.telegram.ui.MDGramSettingsActivity()));
        addItem(content, R.drawable.msg_settings, "Ajustes", () -> present(new SettingsActivity(new android.os.Bundle())));

        ScrollView scroll = new ScrollView(activity);
        scroll.setVerticalScrollBarEnabled(false);
        scroll.addView(content, new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        panel.addView(scroll, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    private View makeDivider() {
        divider = new View(activity);
        divider.setBackgroundColor(Theme.getColor(Theme.key_divider));
        LinearLayout.LayoutParams lp = LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1, 0, 8, 0, 8);
        divider.setLayoutParams(lp);
        return divider;
    }

    private void addItem(LinearLayout parent, int icon, String text, Runnable onClick) {
        LinearLayout row = new LinearLayout(activity);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackground(Theme.getSelectorDrawable(false));
        row.setPadding(dp(19), 0, dp(16), 0);

        ImageView iv = new ImageView(activity);
        iv.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
        iv.setImageResource(icon);
        itemIcons.add(iv);
        row.addView(iv, LayoutHelper.createLinear(24, 24, Gravity.CENTER_VERTICAL, 0, 0, 32, 0));

        TextView tv = new TextView(activity);
        tv.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setTypeface(AndroidUtilities.bold());
        tv.setText(text);
        itemTexts.add(tv);
        row.addView(tv, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));

        row.setOnClickListener(v -> {
            close();
            // navega tras cerrar el drawer para una transición limpia
            AndroidUtilities.runOnUIThread(onClick, 200);
        });
        parent.addView(row, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50));
    }

    private void updateHeader() {
        TLRPC.User user = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        if (user == null) {
            return;
        }
        // "Foto de perfil como fondo" (General): con foto → avatar; sin foto → color de acento sólido
        boolean photoBg = org.telegram.messenger.MDGramConfig.profilePhotoAsBackground();
        if (photoBg) {
            avatarView.setVisibility(View.VISIBLE);
            loadProfilePhotoFull(avatarView, user); // resolución completa: no se pixela al estirarse de fondo
            // "Difuminar foto de perfil" (General): blur suave y regulable vía RenderEffect (Android 12+)
            applyProfileBlur(avatarView, org.telegram.messenger.MDGramConfig.blurProfilePhoto());
            header.setBackgroundColor(0);
        } else {
            avatarView.setVisibility(View.GONE);
            header.setBackgroundColor(ThemeColors.TELEGRAM_COLOR);
        }
        // "Foto de perfil oscurecida" (General): overlay oscuro solo si hay foto
        darkenOverlay.setVisibility(photoBg && org.telegram.messenger.MDGramConfig.darkenProfilePhoto() ? View.VISIBLE : View.GONE);

        nameView.setText(UserObject.getUserName(user));
        // "Ocultar mi número de teléfono" (General)
        if (org.telegram.messenger.MDGramConfig.hidePhoneNumber()) {
            phoneView.setVisibility(View.GONE);
        } else {
            phoneView.setVisibility(View.VISIBLE);
            if (user.phone != null && user.phone.length() > 0) {
                phoneView.setText(PhoneFormat.getInstance().format("+" + user.phone));
            } else {
                phoneView.setText("");
            }
        }
    }

    // carga la foto de perfil en resolución COMPLETA (photo_big, filtro null = nativa) para que no se
    // pixele al usarse como fondo estirado; si el usuario no tiene foto, cae al avatar de iniciales.
    public static void loadProfilePhotoFull(BackupImageView v, TLRPC.User user) {
        org.telegram.messenger.ImageLocation big = org.telegram.messenger.ImageLocation.getForUserOrChat(user, org.telegram.messenger.ImageLocation.TYPE_BIG);
        if (big != null) {
            org.telegram.messenger.ImageLocation small = org.telegram.messenger.ImageLocation.getForUserOrChat(user, org.telegram.messenger.ImageLocation.TYPE_SMALL);
            v.setImage(big, null, small, "50_50", 0, user);
        } else {
            v.setForUserOrChat(user, new AvatarDrawable(user));
        }
    }

    // blur regulable de la foto de perfil vía RenderEffect (Android 12+; en versiones viejas queda nítida)
    public static void applyProfileBlur(android.view.View v, boolean on) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (on) {
                float r = dp(org.telegram.messenger.MDGramConfig.PROFILE_BLUR_RADIUS_DP);
                v.setRenderEffect(android.graphics.RenderEffect.createBlurEffect(r, r, android.graphics.Shader.TileMode.CLAMP));
            } else {
                v.setRenderEffect(null);
            }
        }
    }

    // refresca los colores del panel según el tema activo (para que tras un toggle se vea correcto)
    public void updateColors() {
        if (panel != null) {
            if (modalStyle) {
                updateModalMaterial();
            } else {
                panel.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }
        if (modalStyle) {
            int color = Theme.getColor(Theme.key_windowBackgroundWhiteBlackText);
            for (TextView text : modalTexts) text.setTextColor(color);
            modalUsername.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            modalDismiss.setColorFilter(color);
            modalMoon.setColorFilter(color);
            modalMoon.setImageResource(Theme.isCurrentThemeDark() ? R.drawable.menu_day_mode_24 : R.drawable.menu_night_mode_24);
            modalManage.setBackground(accountPillBackground());
        }
        int iconColor = Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon);
        for (ImageView iv : itemIcons) {
            iv.setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY));
        }
        int textColor = Theme.getColor(Theme.key_windowBackgroundWhiteBlackText);
        for (TextView tv : itemTexts) {
            tv.setTextColor(textColor);
            ((View) tv.getParent()).setBackground(Theme.getSelectorDrawable(false));
        }
        if (divider != null) {
            divider.setBackgroundColor(Theme.getColor(Theme.key_divider));
        }
    }

    // expande/colapsa el selector de cuentas (avatar+nombre+✓ por cuenta activa, y "Añadir cuenta")
    private void toggleAccounts() {
        accountsExpanded = !accountsExpanded;
        if (accountsExpanded) {
            buildAccountsSection();
        }
        accountsSection.setVisibility(accountsExpanded ? View.VISIBLE : View.GONE);
        chevron.setRotation(accountsExpanded ? 180f : 0f);
    }

    private void buildAccountsSection() {
        accountsSection.removeAllViews();
        int textColor = Theme.getColor(Theme.key_windowBackgroundWhiteBlackText);
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            UserConfig uc = UserConfig.getInstance(a);
            if (!uc.isClientActivated()) {
                continue;
            }
            TLRPC.User user = uc.getCurrentUser();
            if (user == null) {
                continue;
            }
            final int account = a;

            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setBackground(Theme.getSelectorDrawable(false));
            row.setPadding(dp(15), 0, dp(16), 0);

            BackupImageView av = new BackupImageView(activity);
            av.setRoundRadius(dp(16));
            av.setForUserOrChat(user, new AvatarDrawable(user));
            row.addView(av, LayoutHelper.createLinear(32, 32, Gravity.CENTER_VERTICAL, 0, 0, 28, 0));

            TextView tv = new TextView(activity);
            tv.setTextColor(textColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tv.setTypeface(AndroidUtilities.bold());
            tv.setMaxLines(1);
            tv.setText(UserObject.getUserName(user));
            LinearLayout.LayoutParams nameLp = new LinearLayout.LayoutParams(0, LayoutHelper.WRAP_CONTENT, 1f);
            nameLp.gravity = Gravity.CENTER_VERTICAL;
            row.addView(tv, nameLp);

            if (account == UserConfig.selectedAccount) {
                ImageView check = new ImageView(activity);
                check.setImageResource(R.drawable.account_check);
                check.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4), PorterDuff.Mode.MULTIPLY));
                check.setScaleType(ImageView.ScaleType.CENTER);
                row.addView(check, LayoutHelper.createLinear(24, 24, Gravity.CENTER_VERTICAL));
            }

            row.setOnClickListener(v -> {
                close();
                if (account != UserConfig.selectedAccount) {
                    AndroidUtilities.runOnUIThread(() -> activity.switchToAccount(account, true), 200);
                }
            });
            accountsSection.addView(row, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50));
        }

        // "Añadir cuenta"
        LinearLayout addRow = new LinearLayout(activity);
        addRow.setOrientation(LinearLayout.HORIZONTAL);
        addRow.setGravity(Gravity.CENTER_VERTICAL);
        addRow.setBackground(Theme.getSelectorDrawable(false));
        addRow.setPadding(dp(19), 0, dp(16), 0);
        ImageView addIcon = new ImageView(activity);
        addIcon.setImageResource(R.drawable.msg_addbot);
        addIcon.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
        addIcon.setScaleType(ImageView.ScaleType.CENTER);
        addRow.addView(addIcon, LayoutHelper.createLinear(24, 24, Gravity.CENTER_VERTICAL, 0, 0, 32, 0));
        TextView addTv = new TextView(activity);
        addTv.setTextColor(textColor);
        addTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        addTv.setTypeface(AndroidUtilities.bold());
        addTv.setText("Añadir cuenta");
        addRow.addView(addTv, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        addRow.setOnClickListener(v -> {
            close();
            AndroidUtilities.runOnUIThread(this::addAccount, 200);
        });
        accountsSection.addView(addRow, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50));

        // divisor bajo la sección de cuentas
        View sep = new View(activity);
        sep.setBackgroundColor(Theme.getColor(Theme.key_divider));
        accountsSection.addView(sep, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1, 0, 8, 0, 8));
    }

    private void addAccount() {
        int freeAccount = -1;
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (!UserConfig.getInstance(a).isClientActivated()) {
                freeAccount = a;
                break;
            }
        }
        if (freeAccount >= 0) {
            present(new org.telegram.ui.LoginActivity(freeAccount));
        }
    }

    public void open() {
        if (open) {
            return;
        }
        open = true;
        modalStyle = org.telegram.messenger.MDGramConfig.useNewNavigation();
        if (modalStyle) {
            buildModalContent();
            panel.setAlpha(0);
        } else {
            glassBackdrop = null;
            glassSourceView = null;
            panel.removeAllViews();
            itemIcons.clear();
            itemTexts.clear();
            panel.setAlpha(1);
            panel.setTranslationX(-panelWidth);
            panel.setLayoutParams(new FrameLayout.LayoutParams(panelWidth, LayoutHelper.MATCH_PARENT, Gravity.LEFT | Gravity.TOP));
            buildContent();
            updateHeader();
            updateColors();
        }
        setVisibility(VISIBLE);
        bringToFront();
        if (animator != null) {
            animator.cancel();
        }
        animator = new AnimatorSet();
        animator.playTogether(
            ObjectAnimator.ofFloat(panel, modalStyle ? View.ALPHA : View.TRANSLATION_X, modalStyle ? panel.getAlpha() : panel.getTranslationX(), modalStyle ? 1 : 0),
            ObjectAnimator.ofFloat(scrim, View.ALPHA, scrim.getAlpha(), 1f)
        );
        animator.setDuration(260);
        animator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        animator.start();
    }

    public boolean close() {
        if (!open) {
            return false;
        }
        open = false;
        if (animator != null) {
            animator.cancel();
        }
        animator = new AnimatorSet();
        animator.playTogether(
            ObjectAnimator.ofFloat(panel, modalStyle ? View.ALPHA : View.TRANSLATION_X, modalStyle ? panel.getAlpha() : panel.getTranslationX(), modalStyle ? 0 : -panelWidth),
            ObjectAnimator.ofFloat(scrim, View.ALPHA, scrim.getAlpha(), 0f)
        );
        animator.setDuration(220);
        animator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!open) {
                    setVisibility(GONE);
                    if (activity.getActionBarLayout().getLastFragment() instanceof MainTabsActivity) {
                        ((MainTabsActivity) activity.getActionBarLayout().getLastFragment()).setMDGramGlassPanelVisible(false);
                    }
                    glassBackdrop = null;
                    glassSourceView = null;
                }
            }
        });
        animator.start();
        return true;
    }

    public boolean isOpen() {
        return open;
    }

    private void present(org.telegram.ui.ActionBar.BaseFragment fragment) {
        activity.presentFragment(fragment);
    }

    private void openMyProfile() {
        android.os.Bundle args = new android.os.Bundle();
        args.putLong("user_id", UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId());
        present(new ProfileActivity(args));
    }

    private void newGroup() {
        android.os.Bundle args = new android.os.Bundle();
        args.putInt("chatType", ChatObject.CHAT_TYPE_MEGAGROUP);
        present(new GroupCreateActivity(args));
    }

    private void newChannel() {
        android.os.Bundle args = new android.os.Bundle();
        args.putInt("step", 0);
        present(new ChannelCreateActivity(args));
    }

    private void openArchived() {
        android.os.Bundle args = new android.os.Bundle();
        args.putInt("folderId", 1);
        present(new DialogsActivity(args));
    }

    private void openSaved() {
        android.os.Bundle args = new android.os.Bundle();
        args.putLong("user_id", UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId());
        present(new ChatActivity(args));
    }

    private void soon(String what) {
        Toast.makeText(activity, what + ": próximamente", Toast.LENGTH_SHORT).show();
    }
}
