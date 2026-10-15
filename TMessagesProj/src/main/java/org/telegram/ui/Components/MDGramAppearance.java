package org.telegram.ui.Components;

import android.view.View;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.ui.ActionBar.Theme;

/** Cherrygram's native section geometry: 12dp inset, 16dp corners. */
public final class MDGramAppearance {
    private MDGramAppearance() {}

    public static void settingsPage(View root, LinearLayout content, Theme.ResourcesProvider resources) {
        if (!MDGramConfig.useRoundedInterface()) return;
        root.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray, resources));
        content.setPadding(AndroidUtilities.dp(12), content.getPaddingTop(),
                AndroidUtilities.dp(12), content.getPaddingBottom());
    }

    public static void settingsGroup(View group, Theme.ResourcesProvider resources) {
        if (!MDGramConfig.useRoundedInterface()) return;
        group.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(16),
                Theme.getColor(Theme.key_windowBackgroundWhite, resources)));
        group.setClipToOutline(true);
    }
}
