package org.telegram.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SeekBarView;

// MDGram: pantalla Chats del hub, ordenada como la referencia V21.
public class ConversationSettingsActivity extends BaseFragment {

    private TextSettingsCell stickerSizeCell;
    private TextSettingsCell photoQualityCell;
    private TextSettingsCell actionBarStyleCell;
    private TextSettingsCell bubbleStyleCell;
    private View blurIntensityCell;
    private ImageView stickerPreviewImage;
    private TextView stickerPreviewTime;
    private TextCheckCell blurEffectToggle;
    private TextCheckCell contextualBlurToggle;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle("Chats");
        actionBar.setForceLeftTitle(true);
        actionBar.setCastShadows(false);
        actionBar.setItemsColor(0xFF319DFA, false);
        actionBar.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
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
        root.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        ScrollView scroll = new ScrollView(context);
        scroll.setVerticalScrollBarEnabled(false);
        root.addView(scroll, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(content, new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        addHeader(context, content, "STICKER SIZE.");
        content.addView(buildStickerSlider(context));
        content.addView(buildStickerPreview(context), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 367));

        addHeader(context, content, "Emojis");
        LinearLayout emojiGroup = group(context, content);
        addValue(context, emojiGroup, "Fuente de emoji personalizada", null, true, () -> presentFragment(new EmojiPacksActivity()));
        addCheck(context, emojiGroup, "Usar emoji predeterminado", "Para aplicar Desactiva Emoji Personalizado", org.telegram.messenger.SharedConfig.useSystemEmoji, false, cell -> {
            org.telegram.messenger.SharedConfig.toggleUseSystemEmoji();
            cell.setChecked(org.telegram.messenger.SharedConfig.useSystemEmoji);
        });
        addValue(context, emojiGroup, "Download More Emojis", null, false, () -> presentFragment(new EmojiPacksActivity()));

        addHeader(context, content, "Fotos");
        content.addView(buildPhotoSlider(context));

        addHeader(context, content, "Estilos de Bubble Chats");
        content.addView(buildBubbleSelector(context));

        addHeader(context, content, "Estilos de ActionBar Chats");
        content.addView(buildActionBarSelector(context));

        addHeader(context, content, "Media");
        LinearLayout mediaGroup = group(context, content);
        addCheck(context, mediaGroup, "Deshabilitar sensor de proximidad", null, MDGramConfig.disableProximitySensor(), true, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setDisableProximitySensor(c);
        });
        blurEffectToggle = addCheck(context, mediaGroup, "Blur Effect", "Enable Blur in chat", MDGramConfig.blurMenu(), false, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setBlurMenu(c);
            if (contextualBlurToggle != null) contextualBlurToggle.setChecked(c);
        });
        blurIntensityCell = buildBlurIntensitySlider(context);
        mediaGroup.addView(blurIntensityCell);
        addCheck(context, mediaGroup, "Ir al siguiente canal", null, MDGramConfig.goToNextChannel(), false, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setGoToNextChannel(c);
        });

        addHeader(context, content, "Entrada de chat");
        LinearLayout inputGroup = group(context, content);
        addCheck(context, inputGroup, "Ocultar texto de Referencia en la entrada de Texto", "Ocultar texto de pista de la entrada del chat", MDGramConfig.hideChatHint(), false, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setHideChatHint(c);
        });
        addCheck(context, inputGroup, "Ocultar enviar como canal", null, MDGramConfig.hideSendAsChannel(), false, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setHideSendAsChannel(c);
        });
        addHeader(context, content, "Menú contextual");
        contextualBlurToggle = addCheck(context, group(context, content), "Activar menú desenfoque contextual", null, MDGramConfig.blurMenu(), false, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setBlurMenu(c);
            if (blurEffectToggle != null) blurEffectToggle.setChecked(c);
        });
        addHeader(context, content, "Opciones adicionales");
        addCheck(context, group(context, content), "Mejoras de voz", "Supresión de ruido y normalización de voz", MDGramConfig.voiceEnhancement(), false, cell -> {
            boolean c = !cell.isChecked(); cell.setChecked(c); MDGramConfig.setVoiceEnhancement(c);
        });

        fragmentView = root;
        return fragmentView;
    }

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(AndroidUtilities.dp(radius));
        return drawable;
    }

    private TextView label(Context context, String value, int size, int color) {
        TextView text = new TextView(context);
        text.setText(value);
        text.setTypeface(AndroidUtilities.getTypeface("fonts/LinotteFon.ttf"));
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        text.setTextColor(color);
        text.setGravity(Gravity.CENTER_VERTICAL);
        return text;
    }

    private View buildStickerSlider(Context context) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(12), AndroidUtilities.dp(16), 0);
        LinearLayout line = new LinearLayout(context);
        SeekBar seek = new SeekBar(context);
        seek.setMax(2);
        seek.setProgress(MDGramConfig.stickerSizeLevel());
        seek.setProgressTintList(ColorStateList.valueOf(0xFF2589FA));
        seek.setThumbTintList(ColorStateList.valueOf(0xFF2589FA));
        line.addView(seek, new LinearLayout.LayoutParams(0, AndroidUtilities.dp(44), 1));
        TextView amount = label(context, "14", 16, 0xFF2589FA);
        amount.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        line.addView(amount, LayoutHelper.createLinear(35, 44));
        box.addView(line);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                amount.setText(String.valueOf(10 + progress * 4));
                if (fromUser) {
                    MDGramConfig.setStickerSizeLevel(progress);
                    updateStickerPreviewSize();
                }
            }
            @Override public void onStartTrackingTouch(SeekBar bar) { }
            @Override public void onStopTrackingTouch(SeekBar bar) { }
        });
        amount.setText(String.valueOf(10 + seek.getProgress() * 4));
        return box;
    }

    private void updateStickerPreviewSize() {
        if (stickerPreviewImage == null) return;
        float multiplier = MDGramConfig.stickerSizeMultiplier();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) stickerPreviewImage.getLayoutParams();
        params.width = AndroidUtilities.dp(Math.round(240 * multiplier));
        params.height = AndroidUtilities.dp(Math.round(238 * multiplier));
        stickerPreviewImage.setLayoutParams(params);
        if (stickerPreviewTime != null) {
            FrameLayout.LayoutParams timeParams = (FrameLayout.LayoutParams) stickerPreviewTime.getLayoutParams();
            timeParams.topMargin = params.topMargin + params.height - AndroidUtilities.dp(23);
            stickerPreviewTime.setLayoutParams(timeParams);
        }
    }

    private View buildStickerPreview(Context context) {
        FrameLayout preview = new FrameLayout(context);
        preview.setClipChildren(true);
        TextView sender = label(context, "RicharC", 14, 0xFF2589FA);
        preview.addView(sender, LayoutHelper.createFrame(120, 22, Gravity.TOP | Gravity.LEFT, 27, 21, 0, 0));
        TextView question = label(context, "¿Yo?", 14, 0xFF8C8F94);
        preview.addView(question, LayoutHelper.createFrame(120, 22, Gravity.TOP | Gravity.LEFT, 27, 41, 0, 0));
        stickerPreviewImage = new ImageView(context);
        stickerPreviewImage.setImageResource(R.drawable.mdgram_v21_sticker_preview);
        stickerPreviewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        preview.addView(stickerPreviewImage, LayoutHelper.createFrame(240, 238, Gravity.TOP | Gravity.RIGHT, 0, 15, 17, 0));
        updateStickerPreviewSize();

        TextView time = stickerPreviewTime = label(context, "09:38 ✓✓", 12, 0xFF333333);
        time.setGravity(Gravity.CENTER);
        time.setBackground(rounded(0xAAFFFFFF, 10));
        preview.addView(time, LayoutHelper.createFrame(76, 18, Gravity.TOP | Gravity.RIGHT, 0, 245, 22, 0));

        updateStickerPreviewSize();
        LinearLayout bubble = new LinearLayout(context);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(AndroidUtilities.dp(10), AndroidUtilities.dp(9), AndroidUtilities.dp(10), 0);
        bubble.setBackground(rounded(0xFFF1F1F1, 15));
        TextView reply = label(context, "▏ JAIR∞\n🐱 Sticker", 14, 0xFF2589FA);
        reply.setPadding(AndroidUtilities.dp(8), 0, 0, 0);
        reply.setBackground(rounded(0xFFDDE7F1, 10));
        bubble.addView(reply, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 42));
        TextView message = label(context, "Sticker Grande.  09:59", 14, 0xFF171717);
        bubble.addView(message, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 26));
        preview.addView(bubble, LayoutHelper.createFrame(170, 78, Gravity.LEFT | Gravity.BOTTOM, 11, 0, 0, 20));
        return preview;
    }
    private View buildBlurIntensitySlider(Context context) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16));
        TextView title = label(context, "Valor Intensidad de desenfoque", 15, 0xFF2589FA);
        box.addView(title, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 34));
        LinearLayout row = new LinearLayout(context);
        SeekBar bar = new SeekBar(context);
        bar.setMax(100);
        bar.setProgress(MDGramConfig.blurMenuIntensity());
        bar.setProgressTintList(ColorStateList.valueOf(0xFF8BC6FF));
        bar.setThumbTintList(ColorStateList.valueOf(0xFF8BC6FF));
        row.addView(bar, new LinearLayout.LayoutParams(0, AndroidUtilities.dp(44), 1));
        TextView number = label(context, String.valueOf(Math.round(bar.getProgress() * 2.55f)), 15, 0xFF2589FA);
        number.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        row.addView(number, LayoutHelper.createLinear(33, 44));
        box.addView(row);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                number.setText(String.valueOf(Math.round(progress * 2.55f)));
                if (fromUser) MDGramConfig.setBlurMenuIntensity(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        return box;
    }

    private View buildPhotoSlider(Context context) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(10), AndroidUtilities.dp(16), AndroidUtilities.dp(14));
        LinearLayout labels = new LinearLayout(context);
        String[] names = {"800px", "1280px", "2560px"};
        TextView[] values = new TextView[3];
        int initial = MDGramConfig.photoQualitySize();
        int selected = initial <= 800 ? 0 : initial >= 1920 ? 2 : 1;
        for (int i = 0; i < 3; i++) {
            TextView value = label(context, names[i], 14, 0xFF90949A);
            value.setGravity(i == 0 ? Gravity.LEFT : i == 2 ? Gravity.RIGHT : Gravity.CENTER);
            values[i] = value;
            labels.addView(value, new LinearLayout.LayoutParams(0, AndroidUtilities.dp(32), 1));
        }
        box.addView(labels);
        SeekBar seek = new SeekBar(context);
        seek.setMax(2);
        seek.setProgress(selected);
        seek.setProgressTintList(ColorStateList.valueOf(0xFF2589FA));
        seek.setThumbTintList(ColorStateList.valueOf(0xFF2589FA));
        box.addView(seek, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 38));
        Runnable refresh = () -> {
            for (int i = 0; i < 3; i++) values[i].setTextColor(i == seek.getProgress() ? 0xFF2589FA : 0xFF90949A);
        };
        refresh.run();
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                refresh.run();
                if (fromUser) MDGramConfig.setPhotoQualitySize(new int[]{800, 1280, 2560}[progress]);
            }
            @Override public void onStartTrackingTouch(SeekBar bar) { }
            @Override public void onStopTrackingTouch(SeekBar bar) { }
        });
        return box;
    }

    private View buildBubbleSelector(Context context) {
        LinearLayout row = new LinearLayout(context);
        row.setPadding(AndroidUtilities.dp(12), AndroidUtilities.dp(8), AndroidUtilities.dp(12), AndroidUtilities.dp(14));
        LinearLayout preview = new LinearLayout(context);
        preview.setOrientation(LinearLayout.VERTICAL);
        preview.setPadding(AndroidUtilities.dp(18), AndroidUtilities.dp(12), 0, 0);
        GradientDrawable border = rounded(0xFFFFFFFF, 8);
        border.setStroke(AndroidUtilities.dp(1), 0xFFD5D9DC);
        preview.setBackground(border);
        for (int i = 0; i < 4; i++) {
            TextView bubble = label(context, "", 12, 0);
            bubble.setBackground(rounded(i < 2 ? 0xFFB5B9BE : 0xFF2589FA, 9));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AndroidUtilities.dp(54), AndroidUtilities.dp(14));
            lp.gravity = i < 2 ? Gravity.LEFT : Gravity.RIGHT;
            lp.bottomMargin = AndroidUtilities.dp(2);
            preview.addView(bubble, lp);
        }
        row.addView(preview, new LinearLayout.LayoutParams(0, AndroidUtilities.dp(95), 1.1f));
        LinearLayout options = new LinearLayout(context);
        options.setOrientation(LinearLayout.VERTICAL);
        String[] names = {"Burbujas Redondos TG", "Bubbles Messenger", "Burbujas de ios"};
        TextView[] choices = new TextView[3];
        Runnable refresh = () -> {
            for (int i = 0; i < 3; i++) choices[i].setTextColor(i == MDGramConfig.bubbleStyle() ? 0xFF232323 : 0xFFB8B8B8);
        };
        for (int i = 0; i < 3; i++) {
            final int index = i;
            TextView choice = label(context, names[i], 13, 0xFFB8B8B8);
            choice.setGravity(Gravity.CENTER);
            choices[i] = choice;
            options.addView(choice, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 31));
            choice.setOnClickListener(v -> { MDGramConfig.setBubbleStyle(index); refresh.run(); });
        }
        row.addView(options, new LinearLayout.LayoutParams(0, LayoutHelper.WRAP_CONTENT, 0.9f));
        refresh.run();
        return row;
    }

    private View buildActionBarSelector(Context context) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(AndroidUtilities.dp(12), AndroidUtilities.dp(6), AndroidUtilities.dp(12), AndroidUtilities.dp(8));
        TextView[] captions = new TextView[2];
        TextView[] radios = new TextView[2];
        FrameLayout[] cards = new FrameLayout[2];
        Runnable refresh = () -> {
            for (int i = 0; i < 2; i++) {
                boolean selected = MDGramConfig.actionBarStyle() == i;
                GradientDrawable bg = rounded(i == 0 ? 0xFFFFFFFF : 0xFFF4F4F4, 6);
                if (selected) bg.setStroke(AndroidUtilities.dp(1), 0xFFD5D9DC);
                cards[i].setBackground(bg);
                captions[i].setTextColor(selected ? 0xFF232323 : 0xFF777777);
                radios[i].setText(selected ? "◉" : "○");
                radios[i].setTextColor(selected ? 0xFF2589FA : 0xFFB4B8BC);
            }
        };
        for (int i = 0; i < 2; i++) {
            final int index = i;
            FrameLayout card = new FrameLayout(context);
            cards[i] = card;
            TextView back = label(context, "‹", 34, 0xFFB8BDC2);
            card.addView(back, LayoutHelper.createFrame(25, 55, Gravity.LEFT | Gravity.CENTER_VERTICAL, 8, 0, 0, 0));
            TextView title = label(context, i == 0 ? "Chats  ━━━━━" : "▢  ━━━━━", 20, 0xFFB8BDC2);
            card.addView(title, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 55, Gravity.CENTER_VERTICAL | Gravity.LEFT, 45, 0, 0, 0));
            TextView radio = label(context, "◯", 25, 0xFF2589FA);
            radios[i] = radio;
            card.addView(radio, LayoutHelper.createFrame(30, 55, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0, 0, 8, 0));
            box.addView(card, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 60));
            TextView caption = label(context, i == 0 ? "Stilo iOS." : "Material D.", 15, 0xFF333333);
            captions[i] = caption;
            caption.setGravity(Gravity.CENTER);
            box.addView(caption, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 38));
            card.setOnClickListener(v -> { MDGramConfig.setActionBarStyle(index); refresh.run(); });
        }
        refresh.run();
        return box;
    }

    private void addHeader(Context context, LinearLayout parent, String text) {
        HeaderCell h = new HeaderCell(context);
        h.setText(text);
        h.getTextView().setTypeface(AndroidUtilities.getTypeface("fonts/LinotteFon.ttf"));
        h.getTextView().setTextColor(0xFF2589FA);
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

    private TextCheckCell addCheck(Context context, LinearLayout group, String text, String subtitle, boolean checked, boolean divider, CheckClick l) {
        TextCheckCell cell = new TextCheckCell(context);
        cell.applyMDGramSettingsStyle();
        if (subtitle != null) {
            cell.setTextAndValueAndCheck(text, subtitle, checked, true, divider);
        } else {
            cell.setTextAndCheck(text, checked, divider);
        }
        cell.setOnClickListener(v -> l.onClick(cell));
        group.addView(cell);
        return cell;
    }

    private interface SliderChange {
        void onChange(float progress);
    }

    // Slider (SeekBarView en percents) con etiqueta arriba. Se sube el requestDisallowIntercept en ACTION_DOWN
    // porque la pantalla vive dentro de un ScrollView (si no, arrastrar el slider desplazaría la página).
    private View addSlider(Context context, LinearLayout group, String title, float initialProgress, SliderChange l) {
        LinearLayout cell = new LinearLayout(context);
        cell.setOrientation(LinearLayout.VERTICAL);

        TextView label = new TextView(context);
        label.setText(title);
        label.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        label.setPadding(AndroidUtilities.dp(21), AndroidUtilities.dp(11), AndroidUtilities.dp(21), 0);
        cell.addView(label, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        SeekBarView seekBar = new SeekBarView(context, true, null) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onTouchEvent(event);
            }
        };
        seekBar.setReportChanges(true);
        seekBar.setProgress(initialProgress);
        seekBar.setDelegate(new SeekBarView.SeekBarViewDelegate() {
            @Override
            public void onSeekBarDrag(boolean stop, float progress) {
                l.onChange(progress);
            }

            @Override
            public void onSeekBarPressed(boolean pressed) {
            }

            @Override
            public CharSequence getContentDescription() {
                return " ";
            }
        });
        cell.addView(seekBar, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 38, 15, 4, 15, 10));

        group.addView(cell);
        return cell;
    }

    private void addValue(Context context, LinearLayout group, String text, String value, boolean divider, Runnable onClick) {
        TextSettingsCell cell = new TextSettingsCell(context);
        cell.applyMDGramSettingsStyle();
        if (value != null) {
            cell.setTextAndValue(text, value, divider);
        } else {
            cell.setText(text, divider);
        }
        cell.setOnClickListener(v -> onClick.run());
        group.addView(cell);
    }

    private String stickerSizeName(int level) {
        switch (level) {
            case 0: return "Pequeño";
            case 2: return "Grande";
            default: return "Normal";
        }
    }

    private void showStickerSizePicker() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Tamaño de sticker");
        b.setItems(new CharSequence[]{"Pequeño", "Normal", "Grande"}, (d, which) -> {
            MDGramConfig.setStickerSizeLevel(which);
            stickerSizeCell.setTextAndValue("Tamaño de sticker", stickerSizeName(which), true);
        });
        showDialog(b.create());
    }

    private String photoQualityName(int px) {
        switch (px) {
            case 800: return "800 px (Baja)";
            case 1600: return "1600 px (Media)";
            case 1920: return "1920 px (Full HD)";
            case 2560: return "2560 px (Ultra HD)";
            default: return "1280 px (Predeterminada)";
        }
    }

    private void showPhotoQualityPicker() {
        if (getParentActivity() == null) {
            return;
        }
        final int[] sizes = {800, 1280, 1600, 1920, 2560};
        CharSequence[] items = new CharSequence[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            items[i] = photoQualityName(sizes[i]);
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Calidad de fotos al enviar");
        b.setItems(items, (d, which) -> {
            int chosen = sizes[which];
            MDGramConfig.setPhotoQualitySize(chosen);
            photoQualityCell.setTextAndValue("Calidad de fotos", photoQualityName(chosen), true);
        });
        showDialog(b.create());
    }

    private String bubbleStyleName(int style) {
        switch (style) {
            case MDGramConfig.BUBBLE_MESSENGER: return "Bubbles Messenger";
            case MDGramConfig.BUBBLE_IOS: return "Burbujas de iOS";
            default: return "Redondas TG";
        }
    }

    private void showBubbleStylePicker() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Estilos de burbuja");
        // Los índices coinciden con las constantes: 0=TG, 1=Messenger, 2=iOS
        b.setItems(new CharSequence[]{"Redondas TG", "Bubbles Messenger", "Burbujas de iOS"}, (d, which) -> {
            MDGramConfig.setBubbleStyle(which);
            bubbleStyleCell.setTextAndValue("Estilos de burbuja", bubbleStyleName(which), true);
            if (getParentActivity() != null) {
                Toast.makeText(getParentActivity(), "Reabre el chat para aplicar", Toast.LENGTH_SHORT).show();
            }
        });
        showDialog(b.create());
    }

    private String actionBarStyleName(int style) {
        return style == MDGramConfig.ACTIONBAR_MATERIAL ? "Material" : "Estilo iOS";
    }

    private void showActionBarStylePicker() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Estilos de barra superior");
        b.setItems(new CharSequence[]{"Estilo iOS", "Material"}, (d, which) -> {
            // iOS = índice 0 = ACTIONBAR_IOS; Material = índice 1 = ACTIONBAR_MATERIAL
            MDGramConfig.setActionBarStyle(which);
            actionBarStyleCell.setTextAndValue("Estilos de barra superior", actionBarStyleName(which), false);
            if (getParentActivity() != null) {
                Toast.makeText(getParentActivity(), "Reabre el chat para aplicar", Toast.LENGTH_SHORT).show();
            }
        });
        showDialog(b.create());
    }

    private void soon(String what) {
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), what + ": próximamente", Toast.LENGTH_SHORT).show();
        }
    }
}
