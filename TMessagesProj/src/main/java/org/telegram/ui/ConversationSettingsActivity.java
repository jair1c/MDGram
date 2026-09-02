package org.telegram.ui;

import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

// MDGram: pantalla "Conversación" del hub (réplica del original). Increment 1: estructura + "Deshabilitar
// sensor de proximidad" FUNCIONAL (MediaController). El resto (mejoras de voz, blur, emoji, burbujas,
// calidad de foto, sticker size) son placeholders "próximamente" — se llenan por increments (Cherrygram).
public class ConversationSettingsActivity extends BaseFragment {

    private TextSettingsCell stickerSizeCell;
    private TextSettingsCell photoQualityCell;
    private TextSettingsCell actionBarStyleCell;
    private TextSettingsCell bubbleStyleCell;
    private View blurIntensityCell;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Conversación");
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

        // ---- Multimedia ----
        addHeader(context, content, "Multimedia");
        LinearLayout mediaGroup = group(context, content);
        // Mejoras de voz (FUNCIONAL — NoiseSuppressor + AutomaticGainControl en la grabación)
        addCheck(context, mediaGroup, "Mejoras de voz", "Supresión de ruido y normalización de voz", MDGramConfig.voiceEnhancement(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setVoiceEnhancement(c);
        });
        // Deshabilitar sensor de proximidad (FUNCIONAL)
        addCheck(context, mediaGroup, "Deshabilitar sensor de proximidad", null, MDGramConfig.disableProximitySensor(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setDisableProximitySensor(c);
        });
        // Efecto de desenfoque (FUNCIONAL — RenderEffect al abrir el menú de un mensaje)
        addCheck(context, mediaGroup, "Efecto de desenfoque", "Difumina el fondo al abrir el menú de un mensaje", MDGramConfig.blurMenu(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setBlurMenu(c);
            if (blurIntensityCell != null) {
                blurIntensityCell.setVisibility(c ? View.VISIBLE : View.GONE);
            }
        });
        // Intensidad del desenfoque (slider) — anidado, visible solo con el efecto activado
        blurIntensityCell = addSlider(context, mediaGroup, "Intensidad del desenfoque", MDGramConfig.blurMenuIntensity() / 100f, progress -> {
            MDGramConfig.setBlurMenuIntensity(Math.round(progress * 100));
        });
        blurIntensityCell.setVisibility(MDGramConfig.blurMenu() ? View.VISIBLE : View.GONE);
        // Ir al siguiente canal (FUNCIONAL)
        addCheck(context, mediaGroup, "Ir al siguiente canal", null, MDGramConfig.goToNextChannel(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setGoToNextChannel(c);
        });

        // ---- Entrada de chat ----
        addHeader(context, content, "Entrada de chat");
        LinearLayout inputGroup = group(context, content);
        addCheck(context, inputGroup, "Ocultar texto de referencia en la entrada", null, MDGramConfig.hideChatHint(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHideChatHint(c);
        });
        addCheck(context, inputGroup, "Ocultar enviar como canal", null, MDGramConfig.hideSendAsChannel(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHideSendAsChannel(c);
        });

        // ---- Estilos y multimedia (placeholders, se llenan por increments) ----
        addHeader(context, content, "Estilos");
        LinearLayout styleGroup = group(context, content);
        // Tamaño de sticker (FUNCIONAL → multiplicador en ChatMessageCell)
        stickerSizeCell = new TextSettingsCell(context);
        stickerSizeCell.setTextAndValue("Tamaño de sticker", stickerSizeName(MDGramConfig.stickerSizeLevel()), true);
        stickerSizeCell.setOnClickListener(v -> showStickerSizePicker());
        styleGroup.addView(stickerSizeCell);
        // Calidad de fotos (FUNCIONAL → 800..2560px en AndroidUtilities.getPhotoSize)
        photoQualityCell = new TextSettingsCell(context);
        photoQualityCell.setTextAndValue("Calidad de fotos", photoQualityName(MDGramConfig.photoQualitySize()), true);
        photoQualityCell.setOnClickListener(v -> showPhotoQualityPicker());
        styleGroup.addView(photoQualityCell);
        // Estilos de burbuja (FUNCIONAL → forma de la burbuja en Theme.MessageDrawable.generatePath)
        bubbleStyleCell = new TextSettingsCell(context);
        bubbleStyleCell.setTextAndValue("Estilos de burbuja", bubbleStyleName(MDGramConfig.bubbleStyle()), true);
        bubbleStyleCell.setOnClickListener(v -> showBubbleStylePicker());
        styleGroup.addView(bubbleStyleCell);
        // Estilos de barra superior (FUNCIONAL → gatea el header centrado iOS vs Material en ChatActivity)
        actionBarStyleCell = new TextSettingsCell(context);
        actionBarStyleCell.setTextAndValue("Estilos de barra superior", actionBarStyleName(MDGramConfig.actionBarStyle()), false);
        actionBarStyleCell.setOnClickListener(v -> showActionBarStylePicker());
        styleGroup.addView(actionBarStyleCell);

        // ---- Emojis ----
        addHeader(context, content, "Emojis");
        LinearLayout emojiGroup = group(context, content);
        // Usar emoji del sistema (FUNCIONAL → Emoji.replaceEmoji respeta SharedConfig.useSystemEmoji)
        addCheck(context, emojiGroup, "Usar emoji del sistema", "Renderiza los emojis con la fuente del teléfono", org.telegram.messenger.SharedConfig.useSystemEmoji, true, cell -> {
            org.telegram.messenger.SharedConfig.toggleUseSystemEmoji();
            cell.setChecked(org.telegram.messenger.SharedConfig.useSystemEmoji);
        });
        // Fuente de emoji personalizada / descargar más (FUNCIONAL → EmojiPacksActivity)
        addValue(context, emojiGroup, "Fuente de emoji personalizada", null, false, () -> presentFragment(new EmojiPacksActivity()));

        TextInfoPrivacyCell info = new TextInfoPrivacyCell(context);
        info.setText("Más opciones de conversación (fuente de emoji, burbujas, barra superior) se irán agregando gradualmente.");
        content.addView(info);

        fragmentView = root;
        return fragmentView;
    }

    private void addHeader(Context context, LinearLayout parent, String text) {
        HeaderCell h = new HeaderCell(context);
        h.setText(text);
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

    private void addCheck(Context context, LinearLayout group, String text, String subtitle, boolean checked, boolean divider, CheckClick l) {
        TextCheckCell cell = new TextCheckCell(context);
        if (subtitle != null) {
            cell.setTextAndValueAndCheck(text, subtitle, checked, true, divider);
        } else {
            cell.setTextAndCheck(text, checked, divider);
        }
        cell.setOnClickListener(v -> l.onClick(cell));
        group.addView(cell);
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
