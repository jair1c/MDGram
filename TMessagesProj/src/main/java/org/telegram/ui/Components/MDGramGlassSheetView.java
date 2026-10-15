package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.widget.ScrollView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MainTabsActivity;
import org.telegram.ui.Components.blur3.BlurredBackgroundDrawableViewFactory;
import org.telegram.ui.Components.blur3.drawable.BlurredBackgroundDrawable;
import org.telegram.ui.Components.blur3.source.BlurredBackgroundSourceRenderNode;

/** Sheet backdrop shares the home capture; sheet content is never captured into itself. */
public final class MDGramGlassSheetView extends ScrollView {
    private final BaseFragment fragment;
    private final BottomSheet sheet;
    private final Theme.ResourcesProvider resources;
    private MainTabsActivity home;
    private View sourceView;
    private BlurredBackgroundDrawable glass;
    private BlurredBackgroundSourceRenderNode backdrop;
    private final Runnable captureTask = this::captureBackdrop;
    private Bitmap snapshot;
    private final Paint snapshotPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final int[] panelPosition = new int[2];
    private final int[] sourcePosition = new int[2];

    public MDGramGlassSheetView(Context context, BaseFragment fragment, BottomSheet sheet,
            Theme.ResourcesProvider resources) {
        super(context);
        this.fragment = fragment;
        this.sheet = sheet;
        this.resources = resources;
        setVerticalScrollBarEnabled(false);
        setWillNotDraw(false);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!MDGramConfig.useLiquidGlass() || Build.VERSION.SDK_INT < 31) return;
        BaseFragment candidate = fragment;
        if (!(candidate instanceof MainTabsActivity) && fragment.getParentActivity() instanceof LaunchActivity) {
            LaunchActivity activity = (LaunchActivity) fragment.getParentActivity();
            if (activity.getActionBarLayout() != null) candidate = activity.getActionBarLayout().getLastFragment();
        }
        if (!(candidate instanceof MainTabsActivity)) return;
        home = (MainTabsActivity) candidate;
        home.setMDGramGlassPanelVisible(true);
        BlurredBackgroundSourceRenderNode source = home.getMDGramGlassSource();
        if (source == null || home.getFragmentView() == null) {
            home.setMDGramGlassPanelVisible(false);
            home = null;
            return;
        }
        sourceView = home.getFragmentView();
        // A large sheet also needs frost in its interior: the lens only refracts edges.
        backdrop = new BlurredBackgroundSourceRenderNode(source.getFallbackSource());
        backdrop.setBlur(AndroidUtilities.dp(16));
        BlurredBackgroundDrawableViewFactory factory = new BlurredBackgroundDrawableViewFactory(backdrop);
        factory.setLiquidGlassEffectAllowed(MDGramGlass.useLens());
        glass = factory.create(this, MDGramGlass.material(resources, false));
        glass.setRadius(AndroidUtilities.dp(28));
        glass.setPadding(0);
        sheet.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        postDelayed(captureTask, 250);
        postDelayed(captureTask, 1500);
        postDelayed(captureTask, 4000);
        invalidate();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (glass != null && w > 0 && h > 0 && (w != oldw || h != oldh)) {
            postDelayed(captureTask, 250);
        }
    }

    private void captureBackdrop() {
        if (sourceView == null || !isAttachedToWindow()) return;
        int width = sourceView.getWidth(), height = sourceView.getHeight();
        if (width <= 0 || height <= 0) return;
        // RenderNodes belong to the app window's renderer. Copy only the app view
        // into a small bitmap before recording it in this dialog's renderer.
        snapshot = Bitmap.createBitmap(Math.max(1, width / 3), Math.max(1, height / 3), Bitmap.Config.ARGB_8888);
        Canvas copy = new Canvas(snapshot);
        copy.drawColor(Theme.getColor(Theme.key_windowBackgroundWhite, resources));
        copy.scale(snapshot.getWidth() / (float) width, snapshot.getHeight() / (float) height);
        sourceView.draw(copy);
        Canvas recording = backdrop.beginRecording(width, height);
        try {
            recording.drawBitmap(snapshot, null, new android.graphics.Rect(0, 0, width, height), snapshotPaint);
        } finally {
            backdrop.endRecording();
        }
        if (glass instanceof org.telegram.ui.Components.blur3.drawable.BlurredBackgroundDrawableRenderNode) {
            ((org.telegram.ui.Components.blur3.drawable.BlurredBackgroundDrawableRenderNode) glass).invalidateDisplayList();
        }
        invalidate();
    }

    @Override public void draw(Canvas canvas) {
        if (glass != null && sourceView != null) {
            int save = canvas.save();
            // ScrollView scrolls its content canvas; the backdrop must stay in the viewport.
            canvas.translate(getScrollX(), getScrollY());
            if (snapshot == null) {
                snapshotPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, resources));
                canvas.drawRoundRect(0, 0, getWidth(), getHeight(), AndroidUtilities.dp(28), AndroidUtilities.dp(28), snapshotPaint);
            }
            getLocationOnScreen(panelPosition);
            sourceView.getLocationOnScreen(sourcePosition);
            glass.setSourceOffset(panelPosition[0] - sourcePosition[0], panelPosition[1] - sourcePosition[1]);
            glass.setBounds(0, 0, getWidth(), getHeight());
            glass.draw(canvas);
            canvas.restoreToCount(save);
        }
        super.draw(canvas);
    }

    @Override protected void onDetachedFromWindow() {
        if (home != null) home.setMDGramGlassPanelVisible(false);
        glass = null;
        backdrop = null;
        removeCallbacks(captureTask);
        snapshot = null;
        sourceView = null;
        home = null;
        super.onDetachedFromWindow();
    }
}
