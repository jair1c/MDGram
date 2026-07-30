package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ScrollView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

public class AboutMDGramActivity extends UniversalFragment {

    @Override
    protected CharSequence getTitle() {
        return "Acerca de MDGram";
    }

    private View wrapAsCard(Context context, View content) {
        FrameLayout wrapper = new FrameLayout(context);
        content.setBackground(Theme.createRoundRectDrawable(dp(12), getThemedColor(Theme.key_windowBackgroundWhite)));
        wrapper.addView(content, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        wrapper.setPadding(dp(12), dp(6), dp(12), dp(6));
        return wrapper;
    }

    private View createCreatorCard(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(0, dp(24), 0, dp(24));

        BackupImageView avatarView = new BackupImageView(context);
        avatarView.setRoundRadius(dp(80));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        TLRPC.User user = UserConfig.getInstance(currentAccount).getCurrentUser();
        avatarDrawable.setInfo(currentAccount, user);
        avatarView.setForUserOrChat(user, avatarDrawable);
        layout.addView(avatarView, LayoutHelper.createLinear(80, 80, Gravity.CENTER_HORIZONTAL));

        TextView nameView = new TextView(context);
        String firstName = "Gabriel ";
        String lastName = "Jair";
        android.text.SpannableString nameText = new android.text.SpannableString(firstName + lastName);
        nameText.setSpan(new android.text.style.ForegroundColorSpan(getThemedColor(Theme.key_windowBackgroundWhiteBlueText2)), firstName.length(), firstName.length() + lastName.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameView.setText(nameText);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        nameView.setTypeface(AndroidUtilities.bold());
        nameView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        nameView.setGravity(Gravity.CENTER);
        layout.addView(nameView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 14, 0, 0));

        TextView subtitleView = new TextView(context);
        subtitleView.setText("Creador de MDGram");
        subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        subtitleView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        subtitleView.setGravity(Gravity.CENTER);
        layout.addView(subtitleView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 4, 0, 0));

        return layout;
    }

    private void addRow(Context context, LinearLayout container, int iconRes, int tintColor, String title, String subtitle, boolean divider, Runnable onClick) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(18), dp(10), dp(18), dp(10));
        row.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        row.setOnClickListener(v -> {
            if (onClick != null) {
                onClick.run();
            } else if (getParentActivity() != null) {
                Toast.makeText(getParentActivity(), "Próximamente", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView iconView = new ImageView(context);
        iconView.setImageResource(iconRes);
        if (tintColor != 0) {
            iconView.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        }
        row.addView(iconView, LayoutHelper.createLinear(28, 28, Gravity.CENTER_VERTICAL, 0, 0, 18, 0));

        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        titleView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        textLayout.addView(titleView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        TextView subtitleView = new TextView(context);
        subtitleView.setText(subtitle);
        subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        subtitleView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        textLayout.addView(subtitleView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 0));

        row.addView(textLayout, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));
        container.addView(row, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        if (divider) {
            View div = new View(context);
            div.setBackgroundColor(getThemedColor(Theme.key_divider));
            container.addView(div, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1, 68, 0, 0, 0));
        }
    }

    private View createSectionCard(Context context, String header, Object[][] rows) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(0, dp(14), 0, dp(6));

        TextView headerView = new TextView(context);
        headerView.setText(header);
        headerView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        headerView.setTypeface(AndroidUtilities.bold());
        headerView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlueHeader));
        headerView.setPadding(dp(18), 0, dp(18), dp(10));
        card.addView(headerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        for (int i = 0; i < rows.length; i++) {
            Object[] r = rows[i];
            Runnable onClick = r.length > 4 ? (Runnable) r[4] : null;
            addRow(context, card, (int) r[0], (int) r[1], (String) r[2], (String) r[3], i != rows.length - 1, onClick);
        }

        return card;
    }

    private TextView creditsBullet(Context context, String text) {
        TextView view = new TextView(context);
        view.setText("•  " + text);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        view.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        view.setLineSpacing(dp(3), 1f);
        return view;
    }

    private void showCreditsDialog(Context context) {
        int accent = ThemeColors.TELEGRAM_COLOR;

        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(24), dp(8), dp(24), dp(8));

        TextView appName = new TextView(context);
        appName.setText("MDGram");
        appName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
        appName.setTypeface(AndroidUtilities.bold());
        appName.setTextColor(accent);
        appName.setGravity(Gravity.CENTER);
        content.addView(appName, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 4, 0));

        TextView madeBy = new TextView(context);
        madeBy.setText("Hecho por Gabriel Jair");
        madeBy.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        madeBy.setTypeface(AndroidUtilities.bold());
        madeBy.setTextColor(accent);
        madeBy.setGravity(Gravity.CENTER);
        content.addView(madeBy, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 20, 0));

        content.addView(creditsBullet(context, "Agradecimientos especiales a TELEGRAM org."), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 6, 0));
        content.addView(creditsBullet(context, "NekoX-Dev/NekoX — código base del proyecto (GPL)"), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 6, 0));
        content.addView(creditsBullet(context, "Cherrygram — funciones de Conversación y General portadas (GPL)"), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 6, 0));
        content.addView(creditsBullet(context, "OwlGram — base del traductor de mensajes (GPL)"), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 6, 0));
        content.addView(creditsBullet(context, "OctoGram — referencia de mods y traductor (GPL)"), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 6, 0));
        content.addView(creditsBullet(context, "MDGram original de Richar Correa (inspiración y diseño de referencia)"), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 20, 0));

        TextView footer = new TextView(context);
        footer.setText("MDGram");
        footer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        footer.setTypeface(AndroidUtilities.bold());
        footer.setTextColor(accent);
        footer.setGravity(Gravity.CENTER);
        content.addView(footer, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 8, 0, 0, 0));

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(content, LayoutHelper.createScroll(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP));

        AlertDialog.Builder builder = new AlertDialog.Builder(context, resourceProvider);
        builder.setTitle("Créditos");
        builder.setView(scrollView);
        builder.setPositiveButton("CERRAR", null);
        showDialog(builder.create());
    }

    private View createFooter(Context context) {
        TextView footer = new TextView(context);
        footer.setText("MDGram V1");
        footer.setTypeface(AndroidUtilities.bold());
        footer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        footer.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(20), 0, dp(20));
        return footer;
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        items.add(UItem.asCustomShadow(wrapAsCard(context, createCreatorCard(context)), LayoutHelper.WRAP_CONTENT));

        items.add(UItem.asCustomShadow(wrapAsCard(context, createSectionCard(context, "APOYAR AL DESARROLLADOR", new Object[][]{
            {R.drawable.mdgram_ic_donate, 0, "Donaciones", "Si te gusta mi trabajo, puedes dejar tu apoyo aquí"},
            {R.drawable.mdgram_ic_share, 0, "Compartir MDGram", "Comparte esta aplicación con tus amigos y familiares"},
        })), LayoutHelper.WRAP_CONTENT));

        items.add(UItem.asCustomShadow(wrapAsCard(context, createSectionCard(context, "SOCIAL Y WEB", new Object[][]{
            {R.drawable.mdgram_ic_website, 0, "Sitio web oficial", "Ingresa al sitio oficial de MDGram para mantenerte actualizado"},
            {R.drawable.mdgram_ic_twitter, 0, "Twitter", "Síguenos para saber más noticias"},
            {R.drawable.mdgram_ic_facebook, 0, "Facebook", "Síguenos para saber más noticias"},
            {R.drawable.mdgram_ic_telegram, 0, "Telegram", "Únete al canal para conocer los temas y actualizaciones"},
        })), LayoutHelper.WRAP_CONTENT));

        items.add(UItem.asCustomShadow(wrapAsCard(context, createSectionCard(context, "OTROS", new Object[][]{
            {R.drawable.mdgram_ic_changelog, 0, "Lista de cambios", "Descubre todos los cambios que trae esta versión"},
            {R.drawable.mdgram_ic_credits, 0, "Créditos", "Gracias y créditos a otros modders", (Runnable) () -> showCreditsDialog(context)},
            {R.drawable.mdgram_ic_version, 0, "Versión de MDGram", "Versión base MOD // Versión base de Telegram", (Runnable) () -> presentFragment(new VersionInfoActivity())},
        })), LayoutHelper.WRAP_CONTENT));

        items.add(UItem.asCustom(createFooter(context), LayoutHelper.WRAP_CONTENT));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
