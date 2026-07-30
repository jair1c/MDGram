package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class VersionInfoActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonDrawable(new BackDrawable(false));
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Acerca de MDGram");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        FrameLayout contentView = new FrameLayout(context);
        contentView.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView iconView = new ImageView(context);
        iconView.setImageResource(R.mipmap.icon_foreground_sa);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0f);
        iconView.setColorFilter(new ColorMatrixColorFilter(matrix));
        layout.addView(iconView, LayoutHelper.createLinear(150, 150, Gravity.CENTER_HORIZONTAL, 0, 0, 0, 0));

        TextView titleView = new TextView(context);
        titleView.setText("MDGram");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
        titleView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        titleView.setGravity(Gravity.CENTER);
        layout.addView(titleView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 24, 0, 0));

        TextView versionView = new TextView(context);
        String tgVersion = "12.4.1";
        try {
            PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            tgVersion = pInfo.versionName;
        } catch (Exception ignored) {
        }
        versionView.setText("MDGram: V1 || TG: " + tgVersion);
        versionView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        versionView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        versionView.setGravity(Gravity.CENTER);
        layout.addView(versionView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 8, 0, 0));

        TextView madeByLabel = new TextView(context);
        madeByLabel.setText("Made By:");
        madeByLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        madeByLabel.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        madeByLabel.setGravity(Gravity.CENTER);
        layout.addView(madeByLabel, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 90, 0, 0));

        TextView creatorNameView = new TextView(context);
        creatorNameView.setText("Gabriel Jair");
        creatorNameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        creatorNameView.setTypeface(AndroidUtilities.bold());
        creatorNameView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        creatorNameView.setGravity(Gravity.CENTER);
        layout.addView(creatorNameView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 6, 0, 0));

        contentView.addView(layout, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 120, 0, 0));

        fragmentView = contentView;
        return fragmentView;
    }
}
