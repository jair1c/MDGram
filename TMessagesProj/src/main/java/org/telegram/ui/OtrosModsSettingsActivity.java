package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.MDGramResidentService;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: pantalla "Otros Mods" del hub. Réplica del original: una sola opción (notificación persistente).
public class OtrosModsSettingsActivity extends BaseFragment {

    private void showAdditionalOptions(Context context) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Opciones adicionales")
                .setMultiChoiceItems(new String[]{"Ocultar pantallas de Premium"},
                        new boolean[]{MDGramConfig.hidePremiumScreens()},
                        (dialog, which, checked) -> MDGramConfig.setHidePremiumScreens(checked))
                .setPositiveButton("Cerrar", null)
                .show();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setForceLeftTitle(true);
        actionBar.setCastShadows(false);
        actionBar.setItemsColor(0xFF319DFA, false);
        actionBar.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        actionBar.createMenu().addItem(1, R.drawable.ic_ab_other);
        actionBar.setTitle("Otros Mods");
        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == 1) {
                    showAdditionalOptions(context);
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

        LinearLayout group = new LinearLayout(context);
        group.setOrientation(LinearLayout.VERTICAL);
        group.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        org.telegram.ui.Components.MDGramAppearance.settingsGroup(group, getResourceProvider());
        content.addView(group);

        // Notificación persistente (FUNCIONAL → MDGramResidentService)
        TextCheckCell resident = new TextCheckCell(context);
        resident.applyMDGramSettingsStyle();
        resident.setTextAndCheck("Mostrar notificación persistente", MDGramConfig.residentNotification(), true);
        resident.setOnClickListener(v -> {
            boolean c = !resident.isChecked();
            resident.setChecked(c);
            MDGramConfig.setResidentNotification(c);
            MDGramResidentService.update();
        });
        group.addView(resident);

        fragmentView = root;
        return fragmentView;
    }
}
