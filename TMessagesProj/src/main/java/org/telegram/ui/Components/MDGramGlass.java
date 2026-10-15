package org.telegram.ui.Components;

import android.os.Build;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MDGramConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.blur3.drawable.color.BlurredBackgroundProvider;
import org.telegram.ui.Components.blur3.drawable.color.BlurredBackgroundProviderBuilder;
import org.telegram.ui.Components.blur3.drawable.color.impl.BlurredBackgroundProviderImpl;

/** Shared optical material. Foreground text/icons are drawn outside the backdrop. */
public final class MDGramGlass {
    private MDGramGlass() {}

    public static boolean useLens() {
        return Build.VERSION.SDK_INT >= 33 && !LiteMode.isPowerSaverApplied()
                && (MDGramConfig.useLiquidGlass() || LiteMode.isEnabled(LiteMode.FLAG_LIQUID_GLASS));
    }

    public static BlurredBackgroundProvider material(Theme.ResourcesProvider resources, boolean topPanel) {
        if (!MDGramConfig.useLiquidGlass()) {
            return topPanel ? BlurredBackgroundProviderImpl.topPanel(resources)
                    : BlurredBackgroundProviderImpl.mainTabs(resources);
        }
        return new BlurredBackgroundProviderBuilder(resources)
                .setBackgroundColor((r, dark) -> ColorUtils.setAlphaComponent(
                        Theme.getColor(Theme.key_windowBackgroundWhite, r),
                        Build.VERSION.SDK_INT < 31 ? 235 : dark ? 135 : 105))
                .setStrokeColorTop(0xA6FFFFFF, 0x66FFFFFF)
                .setStrokeColorBottom(0x24000000, 0x26000000)
                .setShadowColor(0x26000000, 0x38000000)
                .setStrokeWidth(AndroidUtilities.dpf2(0.8f), AndroidUtilities.dpf2(0.5f))
                .setShadowLayer(AndroidUtilities.dpf2(4), 0, AndroidUtilities.dpf2(1))
                .build();
    }

    public static BlurredBackgroundProvider chatPanel(Theme.ResourcesProvider resources) {
        return MDGramConfig.useLiquidGlass() ? material(resources, true)
                : BlurredBackgroundProviderImpl.topPanelChatActivity(resources);
    }

    public static int chatTint(Theme.ResourcesProvider resources, int colorKey) {
        boolean dark = resources != null ? resources.isDark() : Theme.isCurrentThemeDark();
        return ColorUtils.setAlphaComponent(Theme.getColor(colorKey, resources),
                Build.VERSION.SDK_INT < 31 ? 235 : dark ? 170 : 160);
    }
}
