package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.blur3.BlurredBackgroundDrawableViewFactory;
import org.telegram.ui.Components.blur3.drawable.BlurredBackgroundDrawable;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceColor;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceRenderNode;
import java.util.IdentityHashMap;
import java.util.Map;

/** Small live preview: capture only the sample content, never the glass itself. */
public final class MDGramGlassPreview extends FrameLayout {
    private final Theme.ResourcesProvider resources;
    private final BlurredBackgroundSourceRenderNode source;
    private final BlurredBackgroundDrawableViewFactory factory;
    private final Map<View, BlurredBackgroundDrawable> panels = new IdentityHashMap<>();

    public MDGramGlassPreview(Context context, Theme.ResourcesProvider resources) {
        super(context);
        this.resources = resources;
        BlurredBackgroundSourceColor fallback = new BlurredBackgroundSourceColor();
        fallback.setColor(Theme.getColor(Theme.key_windowBackgroundGray, resources));
        if (Build.VERSION.SDK_INT >= 31 && MDGramConfig.useLiquidGlass()) {
            source = new BlurredBackgroundSourceRenderNode(fallback);
            source.setBlur(AndroidUtilities.dp(3));
            factory = new BlurredBackgroundDrawableViewFactory(source);
        } else {
            source = null;
            factory = new BlurredBackgroundDrawableViewFactory(fallback);
        }
        factory.setLiquidGlassEffectAllowed(MDGramGlass.useLens());
        setClipChildren(false);
    }

    public void glass(View view, int radius) {
        if (!MDGramConfig.useLiquidGlass()) return;
        BlurredBackgroundDrawable drawable = factory.create(view, MDGramGlass.material(resources, false));
        drawable.setRadius(AndroidUtilities.dp(radius));
        drawable.setPadding(0);
        panels.put(view, drawable);
        view.setBackground(drawable);
    }

    @Override protected void dispatchDraw(Canvas canvas) {
        if (source != null && getWidth() > 0 && getHeight() > 0 && canvas.isHardwareAccelerated()) {
            Canvas backdrop = source.beginRecording(getWidth(), getHeight());
            backdrop.drawColor(Theme.getColor(Theme.key_windowBackgroundGray, resources));
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE && !panels.containsKey(child)) {
                    drawChild(backdrop, child, getDrawingTime());
                }
            }
            source.endRecording();
        }
        for (Map.Entry<View, BlurredBackgroundDrawable> panel : panels.entrySet()) {
            panel.getValue().setSourceOffset(panel.getKey().getX(), panel.getKey().getY());
        }
        super.dispatchDraw(canvas);
    }
}
