package org.telegram.ui;

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

import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.R;
import uz.unnarsx.cherrygram.core.configs.CherrygramAppearanceConfig;
import uz.unnarsx.cherrygram.core.configs.CherrygramCoreConfig;
import uz.unnarsx.cherrygram.preferences.folders.cells.FoldersPreviewCell;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.glass.GlassTabView;

// MDGram: pantalla "Pantalla principal" del hub (réplica del original "Página Principal").
// Opciones de la pantalla principal y de las pestañas de carpetas.
public class HomeSettingsActivity extends BaseFragment {

    private TextSettingsCell tabTitleStyleCell;
    private TextSettingsCell tabStyleCell;
    private FrameLayout bottomPreview;
    private FrameLayout homePreview;
    private TextSettingsCell homeTitleStyleCell;
    private FoldersPreviewCell folderPreview;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle("Página Principal");
        actionBar.setForceLeftTitle(true);
        actionBar.setCastShadows(false);
        actionBar.setItemsColor(0xFF319DFA, false);
        actionBar.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
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
        LinearLayout page = new LinearLayout(context);
        page.setOrientation(LinearLayout.VERTICAL);
        root.addView(page, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        homePreview = new FrameLayout(context);
        page.addView(homePreview, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 272));
        ScrollView scroll = new ScrollView(context);
        scroll.setVerticalScrollBarEnabled(false);
        page.addView(scroll, new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 0, 1f));
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(content, new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout g1 = group(context, content);
        addCheck(context, g1, "Título de inicio", "Personalizar el título de la pantalla principal", MDGramConfig.homeTitleAsName(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHomeTitleAsName(c);
            homeTitleStyleCell.setVisibility(c ? View.VISIBLE : View.GONE);
            refreshHomePreview(context);
        });
        homeTitleStyleCell = addValue(context, g1, "Estilo del título", titleModeName(), true, () -> showHomeTitleDialog(context));
        homeTitleStyleCell.setVisibility(MDGramConfig.homeTitleAsName() ? View.VISIBLE : View.GONE);
        addCheck(context, g1, "Ocultar barra de búsqueda", "Se aplica al volver a Chats", MDGramConfig.hideSearchBar(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHideSearchBar(c);
            refreshHomePreview(context);
        });

        addHeader(context, content, "Historias");
        LinearLayout storiesGroup = group(context, content);
        addCheck(context, storiesGroup, "Mostrar historias", "Se aplica al volver a Chats", !CherrygramCoreConfig.INSTANCE.getHideStories(), false, cell -> {
            boolean enabled = !cell.isChecked();
            cell.setChecked(enabled);
            CherrygramCoreConfig.INSTANCE.setHideStories(!enabled);
        });
        addHeader(context, content, "Carpeta");
        LinearLayout g2 = group(context, content);
        folderPreview = new FoldersPreviewCell(context);
        g2.addView(folderPreview, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 82));
        tabTitleStyleCell = addValue(context, g2, "Contenido de las carpetas", tabModeName(), true, () -> showTabModeDialog(context));
        tabStyleCell = addValue(context, g2, "Estilo de carpetas", folderStyleName(), true, () -> showTabStyleDialog(context));
        addCheck(context, g2, "Ocultar la carpeta \"Todos\"", null, CherrygramAppearanceConfig.INSTANCE.getTabsHideAllChats(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            CherrygramAppearanceConfig.INSTANCE.setTabsHideAllChats(c);
            refreshHomePreview(context);
            toast("Reinicia la app para aplicar");
        });

        addCheck(context, g2, "Ocultar contadores de carpetas", "Se aplica al volver a Chats", MDGramConfig.hideFolderCounters(), false, cell -> {
            boolean hidden = !cell.isChecked();
            cell.setChecked(hidden);
            MDGramConfig.setHideFolderCounters(hidden);
            refreshHomePreview(context);
        });
        addHeader(context, content, "Bottom Tab");
        LinearLayout g3 = group(context, content);
        bottomPreview = new FrameLayout(context);
        bottomPreview.addView(buildBottomPreview(context), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        g3.addView(bottomPreview, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, MDGramConfig.useNewNavigation() ? 60 : 92));
        addCheck(context, g3, "Activar Bottom Tab", null, CherrygramAppearanceConfig.INSTANCE.getShowMainTabs(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            CherrygramAppearanceConfig.INSTANCE.setShowMainTabs(c);
            refreshHomePreview(context);
            toast("Reinicia MDGram para aplicar");
        });
        addCheck(context, g3, "Usar nueva navegación", "Chats, Contactos, Ajustes y Perfil. Se aplica al volver al inicio.", MDGramConfig.useNewNavigation(), false, cell -> {
            boolean enabled = !cell.isChecked();
            cell.setChecked(enabled);
            MDGramConfig.setUseNewNavigation(enabled);
            bottomPreview.removeAllViews();
            bottomPreview.addView(buildBottomPreview(context), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
            android.view.ViewGroup.LayoutParams previewParams = bottomPreview.getLayoutParams();
            previewParams.height = org.telegram.messenger.AndroidUtilities.dp(enabled ? 60 : 92);
            bottomPreview.setLayoutParams(previewParams);
            refreshHomePreview(context);
        });

        addHeader(context, content, "Botón flotante");
        LinearLayout g4 = group(context, content);
        addCheck(context, g4, "Ocultar botón flotante", "Sólo en Chats. Se aplica al volver a Chats", MDGramConfig.hideFab(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHideFab(c);
            refreshHomePreview(context);
        });

        refreshHomePreview(context);
        fragmentView = root;
        return fragmentView;
    }

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(org.telegram.messenger.AndroidUtilities.dp(radius));
        return drawable;
    }

    private String titleModeName() {
        return new String[]{"MDGram", "Nombre completo", "Primer nombre", "Chats"}[MDGramConfig.homeTitleMode()];
    }

    private void showHomeTitleDialog(Context context) {
        org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(context);
        builder.setTitle("Estilo del título");
        LinearLayout choices = new LinearLayout(context);
        choices.setOrientation(LinearLayout.VERTICAL);
        String[] titles = {"MDGram", "Nombre completo", "Primer nombre", "Chats"};
        for (int i = 0; i < titles.length; i++) {
            final int mode = i;
            org.telegram.ui.Cells.RadioColorCell choice = new org.telegram.ui.Cells.RadioColorCell(context, getResourceProvider());
            choice.setTextAndValue(titles[i], i == MDGramConfig.homeTitleMode());
            choice.setOnClickListener(v -> {
                MDGramConfig.setHomeTitleMode(mode);
                homeTitleStyleCell.setTextAndValue("Estilo del título", titleModeName(), true);
                refreshHomePreview(context);
                dismissCurrentDialog();
            });
            choices.addView(choice);
        }
        builder.setView(choices);
        builder.setNegativeButton(org.telegram.messenger.LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    private TextView previewText(Context context, String text, int size, boolean bold) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextSize(size);
        view.setSingleLine(true);
        view.setEllipsize(android.text.TextUtils.TruncateAt.END);
        view.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        if (bold) view.setTypeface(org.telegram.messenger.AndroidUtilities.bold());
        return view;
    }

    private void refreshHomePreview(Context context) {
        if (folderPreview != null) {
            folderPreview.updateTabIcons(false);
            folderPreview.updateTabTitle(false);
            folderPreview.updateTabCounter(false);
            folderPreview.updateAllChatsTabName(false);
        }
        if (homePreview == null) return;
        homePreview.removeAllViews();
        FrameLayout scene = new FrameLayout(context);
        scene.setBackground(rounded(getThemedColor(Theme.key_windowBackgroundGray), 20));
        homePreview.addView(scene, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 12, 8, 12, 8));
        org.telegram.tgnet.TLRPC.User user = getUserConfig().getCurrentUser();
        org.telegram.ui.Components.BackupImageView avatar = new org.telegram.ui.Components.BackupImageView(context);
        avatar.setRoundRadius(org.telegram.messenger.AndroidUtilities.dp(16));
        avatar.setForUserOrChat(user, new org.telegram.ui.Components.AvatarDrawable(user));
        scene.addView(avatar, LayoutHelper.createFrame(32, 32, Gravity.TOP | Gravity.LEFT, 12, 6, 0, 0));
        TextView title = previewText(context, MDGramConfig.homeTitle(currentAccount), 20, true);
        title.setGravity(Gravity.CENTER);
        scene.addView(title, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 44, Gravity.TOP, 52, 0, 52, 0));
        TextView menu = previewText(context, "⋮", 25, true);
        menu.setGravity(Gravity.CENTER);
        scene.addView(menu, LayoutHelper.createFrame(40, 44, Gravity.TOP | Gravity.RIGHT));
        int folderTop = 44;
        if (!MDGramConfig.hideSearchBar()) {
            TextView search = previewText(context, "⌕  Busca chats…", 14, false);
            search.setGravity(Gravity.CENTER_VERTICAL);
            search.setPadding(org.telegram.messenger.AndroidUtilities.dp(12), 0, 0, 0);
            search.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
            search.setBackground(rounded(getThemedColor(Theme.key_windowBackgroundWhite), 18));
            scene.addView(search, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 34, Gravity.TOP, 12, 46, 12, 0));
            folderTop = 84;
        }
        FrameLayout folders = new FrameLayout(context);
        folders.addView(buildFolderPreview(context), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 82, Gravity.CENTER));
        scene.addView(folders, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 46, Gravity.TOP, 4, folderTop, 4, 0));
        int chatTop = folderTop + 48;
        FrameLayout chat = new FrameLayout(context);
        android.widget.ImageView dino = new android.widget.ImageView(context);
        dino.setImageResource(R.drawable.dino_pic);
        dino.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        dino.setBackground(rounded(getThemedColor(Theme.key_windowBackgroundWhite), 12));
        dino.setClipToOutline(true);
        chat.addView(dino, LayoutHelper.createFrame(44, 44, Gravity.TOP | Gravity.LEFT, 12, 8, 0, 0));
        TextView name = previewText(context, "Dino", 15, true);
        chat.addView(name, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 24, Gravity.TOP, 66, 6, 60, 0));
        TextView message = previewText(context, "Hola, ¿cómo estás?", 13, false);
        message.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        chat.addView(message, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 24, Gravity.TOP, 66, 30, 60, 0));
        TextView time = previewText(context, "12:30", 11, false);
        time.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        chat.addView(time, LayoutHelper.createFrame(44, 22, Gravity.TOP | Gravity.RIGHT, 0, 8, 8, 0));
        scene.addView(chat, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 64, Gravity.TOP, 0, chatTop, 0, 0));
        if (!MDGramConfig.hideFab()) {
            TextView fab = previewText(context, "+", 24, true);
            fab.setGravity(Gravity.CENTER);
            fab.setTextColor(0xFFFFFFFF);
            fab.setBackground(rounded(getThemedColor(Theme.key_windowBackgroundWhiteBlueText), 12));
            scene.addView(fab, LayoutHelper.createFrame(36, 36, Gravity.BOTTOM | Gravity.RIGHT, 0, 0, 16,
                    CherrygramAppearanceConfig.INSTANCE.getShowMainTabs() ? 65 : 12));
        }
        if (CherrygramAppearanceConfig.INSTANCE.getShowMainTabs()) {
            scene.addView(buildBottomPreview(context), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 56, Gravity.BOTTOM, 8, 0, 8, 4));
        }
        scene.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        homePreview.setContentDescription("Vista previa de la pantalla principal: " + MDGramConfig.homeTitle(currentAccount));
    }

    private View buildFolderPreview(Context context) {
        return new FoldersPreviewCell(context);
    }

    private View buildBottomPreview(Context context) {
        LinearLayout row = new LinearLayout(context);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
        GlassTabView.TabAnimation[] animations = {
                GlassTabView.TabAnimation.CONTACTS, GlassTabView.TabAnimation.CALLS,
                GlassTabView.TabAnimation.CHATS, GlassTabView.TabAnimation.SETTINGS
        };
        int[] titles = {R.string.MainTabsContacts, R.string.Calls, R.string.MainTabsChats, R.string.Settings};
        boolean modern = MDGramConfig.useNewNavigation();
        if (modern) {
            animations = new GlassTabView.TabAnimation[]{GlassTabView.TabAnimation.CHATS,
                    GlassTabView.TabAnimation.CONTACTS, GlassTabView.TabAnimation.SETTINGS};
            titles = new int[]{R.string.MainTabsChats, R.string.MainTabsContacts, R.string.Settings};
            row.setBackground(rounded(getThemedColor(Theme.key_windowBackgroundGray), 30));
        }
        for (int i = 0; i < 4; i++) {
            GlassTabView tab = modern && i == 3
                    ? GlassTabView.createAvatar(context, getResourceProvider(), currentAccount, R.string.MainTabsProfile)
                    : GlassTabView.createMainTab(context, getResourceProvider(), animations[i], titles[i]);
            tab.setSelected(i == (modern ? 0 : 2), false);
            row.addView(tab, new LinearLayout.LayoutParams(0, LayoutHelper.MATCH_PARENT, 1));
        }
        return row;
    }

    private void addHeader(Context context, LinearLayout parent, String text) {
        HeaderCell h = new HeaderCell(context);
        h.setText(text);
        parent.addView(h);
    }

    private LinearLayout group(Context context, LinearLayout parent) {
        LinearLayout g = new LinearLayout(context);
        g.setOrientation(LinearLayout.VERTICAL);
        g.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        parent.addView(g);
        return g;
    }

    private interface CheckClick {
        void onClick(TextCheckCell cell);
    }

    private void addCheck(Context context, LinearLayout group, String text, String subtitle, boolean checked, boolean divider, CheckClick l) {
        TextCheckCell cell = new TextCheckCell(context);
        cell.applyMDGramSettingsStyle();
        if (subtitle != null) {
            cell.setTextAndValueAndCheck(text, subtitle, checked, true, divider);
        } else {
            cell.setTextAndCheck(text, checked, divider);
        }
        cell.setOnClickListener(v -> l.onClick(cell));
        group.addView(cell);
    }

    private TextSettingsCell addValue(Context context, LinearLayout group, String text, String value, boolean divider, Runnable onClick) {
        TextSettingsCell cell = new TextSettingsCell(context);
        cell.applyMDGramSettingsStyle();
        if (value != null) {
            cell.setTextAndValue(text, value, divider);
        } else {
            cell.setText(text, divider);
        }
        cell.setOnClickListener(v -> onClick.run());
        group.addView(cell);
        return cell;
    }

    private String tabModeName() {
        switch (CherrygramAppearanceConfig.INSTANCE.getTabMode()) {
            case CherrygramAppearanceConfig.TAB_TYPE_TEXT: return "Sólo texto";
            case CherrygramAppearanceConfig.TAB_TYPE_ICON: return "Sólo iconos";
            default: return "Iconos y texto";
        }
    }

    private void showTabModeDialog(Context context) {
        String[] modes = {"Iconos y texto", "Sólo texto", "Sólo iconos"};
        org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(getParentActivity(), getResourceProvider());
        builder.setTitle("Contenido de las carpetas");
        LinearLayout choices = new LinearLayout(context);
        choices.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < modes.length; i++) {
            final int mode = i;
            org.telegram.ui.Cells.RadioColorCell cell = new org.telegram.ui.Cells.RadioColorCell(context, getResourceProvider());
            cell.setTextAndValue(modes[i], CherrygramAppearanceConfig.INSTANCE.getTabMode() == i);
            choices.addView(cell);
            cell.setOnClickListener(v -> {
                CherrygramAppearanceConfig.INSTANCE.setTabMode(mode);
                tabTitleStyleCell.setTextAndValue("Contenido de las carpetas", tabModeName(), true);
                refreshHomePreview(context);
                dismissCurrentDialog();
            });
        }
        builder.setView(choices);
        builder.setNegativeButton(org.telegram.messenger.LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    private void showTabStyleDialog(Context context) {
        String[] styles = {"Default", "Rounded", "Text Only", "Chips", "Pills"};
        org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(getParentActivity(), getResourceProvider());
        builder.setTitle("Estilo de carpetas");
        LinearLayout choices = new LinearLayout(context);
        choices.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < styles.length; i++) {
            final int style = i;
            org.telegram.ui.Cells.RadioColorCell cell = new org.telegram.ui.Cells.RadioColorCell(context, getResourceProvider());
            cell.setTextAndValue(styles[i], MDGramConfig.folderStyle() == i || MDGramConfig.folderStyle() == -1 && i == 1);
            choices.addView(cell);
            cell.setOnClickListener(v -> {
                MDGramConfig.setFolderStyle(style);
                tabStyleCell.setTextAndValue("Estilo de carpetas", folderStyleName(), true);
                refreshHomePreview(context);
                dismissCurrentDialog();
                toast("Se aplica al volver a Chats");
            });
        }
        builder.setView(choices);
        builder.setNegativeButton(org.telegram.messenger.LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    private String folderStyleName() {
        int style = MDGramConfig.folderStyle();
        return style < 0 ? (CherrygramAppearanceConfig.INSTANCE.getTabStyleStroke() ? "Outlined (anterior)" : "Rounded")
                : new String[]{"Default", "Rounded", "Text Only", "Chips", "Pills"}[style];
    }

    private void toast(String msg) {
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
