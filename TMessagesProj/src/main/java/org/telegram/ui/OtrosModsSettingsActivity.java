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
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: pantalla "Otros Mods" del hub. Réplica del original: una sola opción (notificación persistente).
public class OtrosModsSettingsActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Otros Mods");
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

        LinearLayout group = new LinearLayout(context);
        group.setOrientation(LinearLayout.VERTICAL);
        group.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(group);

        // Notificación persistente (FUNCIONAL → MDGramResidentService)
        TextCheckCell resident = new TextCheckCell(context);
        resident.setTextAndCheck("Mostrar notificación persistente", MDGramConfig.residentNotification(), true);
        resident.setOnClickListener(v -> {
            boolean c = !resident.isChecked();
            resident.setChecked(c);
            MDGramConfig.setResidentNotification(c);
            MDGramResidentService.update();
        });
        group.addView(resident);

        // Ocultar pantallas de comprar Premium (FUNCIONAL → gatea PremiumPreviewFragment + PremiumFeatureBottomSheet)
        TextCheckCell hidePremium = new TextCheckCell(context);
        hidePremium.setTextAndValueAndCheck("Ocultar pantallas de Premium", "Bloquea las pantallas de comprar Telegram Premium", MDGramConfig.hidePremiumScreens(), true, false);
        hidePremium.setOnClickListener(v -> {
            boolean c = !hidePremium.isChecked();
            hidePremium.setChecked(c);
            MDGramConfig.setHidePremiumScreens(c);
        });
        group.addView(hidePremium);

        TextInfoPrivacyCell info = new TextInfoPrivacyCell(context);
        info.setText("La notificación persistente mantiene MDGram en segundo plano. Ocultar pantallas de Premium bloquea las pantallas de compra (no afecta los límites del servidor).");
        content.addView(info);

        fragmentView = root;
        return fragmentView;
    }
}
