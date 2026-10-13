package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

/** Visual recreation of the welcome panel displayed by MDGram V21 after the first login. */
public final class MDGramV21WelcomeSheet {

    private static final int ACCENT = 0xFF2088FF;
    private static final int BUTTON_BG = 0xFFEAF3FF;
    private static final int CANCEL_BG = 0xFFF1F1F1;

    private MDGramV21WelcomeSheet() {
    }

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(18));

        TextView title = new TextView(activity);
        title.setText("MDGram V21");
        title.setTextColor(ACCENT);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        title.setTypeface(AndroidUtilities.bold());
        title.setGravity(Gravity.CENTER);
        content.addView(title, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 44));

        TextView message = new TextView(activity);
        message.setText("Felicidades Bienvenido a MDGram recuerda para seguirnos y saber mas de nuestro trabajo y estar siempre al dia actualizado puedes unirte a nuestro grupo de Telegram y canal de telegram y seguirnos en Facebook y Visitar nuestro pagina Web Oficial");
        message.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        message.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        message.setGravity(Gravity.CENTER);
        message.setLineSpacing(dp(2), 1f);
        content.addView(message, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 8, 5, 8, 14));

        addAction(content, activity, R.drawable.mdgram_ic_telegram, "Channel (Telegram)", false);
        addAction(content, activity, R.drawable.msg_groups, "Group (Telegram)", false);
        addAction(content, activity, R.drawable.mdgram_ic_facebook, "Facebook (Page)", false);
        addAction(content, activity, R.drawable.mdgram_ic_website, "WebSite Official", false);
        View cancel = addAction(content, activity, R.drawable.msg_close, "Cancel", true);

        BottomSheet.Builder builder = new BottomSheet.Builder(activity);
        builder.setApplyBottomPadding(true);
        builder.setCustomView(content);
        BottomSheet sheet = builder.create();
        sheet.setBackgroundColor(Color.WHITE);
        cancel.setOnClickListener(v -> sheet.dismiss());
        sheet.show();
    }

    private static View addAction(LinearLayout parent, Activity activity, int iconRes, String label, boolean cancel) {
        LinearLayout row = new LinearLayout(activity);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(dp(16), 0, dp(16), 0);
        row.setBackground(Theme.createSimpleSelectorRoundRectDrawable(dp(24), cancel ? CANCEL_BG : BUTTON_BG, cancel ? 0xFFE6E6E6 : 0xFFDCEBFF));

        ImageView icon = new ImageView(activity);
        icon.setImageResource(iconRes);
        icon.setColorFilter(ACCENT, PorterDuff.Mode.SRC_IN);
        row.addView(icon, LayoutHelper.createLinear(24, 24));

        TextView text = new TextView(activity);
        text.setText(label);
        text.setTextColor(ACCENT);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        text.setTypeface(AndroidUtilities.bold());
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setPadding(dp(10), 0, 0, 0);
        row.addView(text, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));

        parent.addView(row, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48, 0, 5, 0, 0));
        return row;
    }
}
