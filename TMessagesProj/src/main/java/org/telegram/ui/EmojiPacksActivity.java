package org.telegram.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiPackManager;
import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;

// MDGram: pantalla "Fuente de emoji personalizada" (Conversación → Emojis). Permite elegir el set de emoji
// activo (Predeterminado/Apple + packs instalados) y descargar packs nuevos desde el manifiesto remoto.
public class EmojiPacksActivity extends BaseFragment {

    private LinearLayout currentGroup;     // set actual (radio)
    private LinearLayout downloadGroup;     // packs descargables
    private HeaderCell downloadHeader;
    private TextInfoPrivacyCell downloadInfo;
    private ArrayList<EmojiPackManager.PackInfo> remotePacks = new ArrayList<>();
    private String downloadingId = null;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Fuente de emoji");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) finishFragment();
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

        HeaderCell h1 = new HeaderCell(context);
        h1.setText("Set actual");
        content.addView(h1);
        currentGroup = new LinearLayout(context);
        currentGroup.setOrientation(LinearLayout.VERTICAL);
        currentGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(currentGroup);

        TextInfoPrivacyCell info1 = new TextInfoPrivacyCell(context);
        info1.setText("Elige el estilo de los emojis en los mensajes. Los emojis que falten en un pack se muestran con el estilo predeterminado.");
        content.addView(info1);

        downloadHeader = new HeaderCell(context);
        downloadHeader.setText("Descargar");
        content.addView(downloadHeader);
        downloadGroup = new LinearLayout(context);
        downloadGroup.setOrientation(LinearLayout.VERTICAL);
        downloadGroup.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        content.addView(downloadGroup);

        downloadInfo = new TextInfoPrivacyCell(context);
        downloadInfo.setText("Buscando packs disponibles…");
        content.addView(downloadInfo);

        fragmentView = root;

        renderCurrent();
        renderDownloads();
        loadManifest();
        return fragmentView;
    }

    // ---- Set actual (radio): Predeterminado + instalados ----
    private void renderCurrent() {
        currentGroup.removeAllViews();
        ArrayList<String> installed = EmojiPackManager.listInstalled();
        addRadioRow("Predeterminado (Apple)", "default", !installed.isEmpty());
        for (int i = 0; i < installed.size(); i++) {
            String id = installed.get(i);
            addRadioRow(nameFor(id), id, i < installed.size() - 1);
        }
    }

    private String nameFor(String id) {
        for (EmojiPackManager.PackInfo p : remotePacks) {
            if (p.id.equals(id)) return p.name;
        }
        return id;
    }

    private void addRadioRow(String title, String id, boolean divider) {
        TextSettingsCell cell = new TextSettingsCell(getParentActivity() != null ? getParentActivity() : fragmentView.getContext());
        boolean active = MDGramConfig.emojiPack().equals(id);
        cell.setTextAndValue(title, active ? "✓ Activo" : "", divider);
        cell.setOnClickListener(v -> {
            if (!MDGramConfig.emojiPack().equals(id)) {
                MDGramConfig.setEmojiPack(id);
                Emoji.reloadEmoji();
                renderCurrent();
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), "Emojis actualizados", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!id.equals("default")) {
            cell.setOnLongClickListener(v -> {
                confirmDelete(id);
                return true;
            });
        }
        currentGroup.addView(cell);
    }

    private void confirmDelete(String id) {
        if (getParentActivity() == null) return;
        AlertDialog.Builder b = new AlertDialog.Builder(getParentActivity());
        b.setTitle("Eliminar pack");
        b.setMessage("¿Eliminar \"" + nameFor(id) + "\"? Podrás volver a descargarlo.");
        b.setPositiveButton("Eliminar", (d, w) -> {
            EmojiPackManager.deletePack(id);
            renderCurrent();
            renderDownloads();
        });
        b.setNegativeButton("Cancelar", null);
        showDialog(b.create());
    }

    // ---- Packs descargables (los del manifiesto que no estén instalados) ----
    private void renderDownloads() {
        downloadGroup.removeAllViews();
        int shown = 0;
        for (int i = 0; i < remotePacks.size(); i++) {
            EmojiPackManager.PackInfo p = remotePacks.get(i);
            if (EmojiPackManager.isInstalled(p.id)) continue;
            addDownloadRow(p);
            shown++;
        }
        boolean any = shown > 0;
        downloadHeader.setVisibility(any ? View.VISIBLE : View.GONE);
        downloadGroup.setVisibility(any ? View.VISIBLE : View.GONE);
    }

    private void addDownloadRow(EmojiPackManager.PackInfo p) {
        TextSettingsCell cell = new TextSettingsCell(getParentActivity() != null ? getParentActivity() : fragmentView.getContext());
        String value = downloadingId != null && downloadingId.equals(p.id)
                ? "Descargando…"
                : (p.count > 0 ? p.count + " emojis · " : "") + (p.sizeMB > 0 ? p.sizeMB + " MB" : "Descargar");
        cell.setTextAndValue(p.name, value, false);
        cell.setOnClickListener(v -> startDownload(p, cell));
        downloadGroup.addView(cell);
    }

    private void startDownload(EmojiPackManager.PackInfo p, TextSettingsCell cell) {
        if (downloadingId != null) {
            return; // una descarga a la vez
        }
        downloadingId = p.id;
        cell.setTextAndValue(p.name, "0%", false);
        EmojiPackManager.downloadAndInstall(p, new EmojiPackManager.DownloadListener() {
            @Override
            public void onProgress(long downloaded, long total) {
                int pct = total > 0 ? (int) (downloaded * 100 / total) : 0;
                cell.setTextAndValue(p.name, pct + "%", false);
            }

            @Override
            public void onComplete() {
                downloadingId = null;
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), p.name + " instalado", Toast.LENGTH_SHORT).show();
                }
                // activarlo automáticamente al instalar
                MDGramConfig.setEmojiPack(p.id);
                Emoji.reloadEmoji();
                renderCurrent();
                renderDownloads();
            }

            @Override
            public void onError(String error) {
                downloadingId = null;
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), "No se pudo descargar: " + error, Toast.LENGTH_LONG).show();
                }
                renderDownloads();
            }
        });
    }

    private void loadManifest() {
        EmojiPackManager.fetchManifest((packs, error) -> {
            if (packs != null) {
                remotePacks = packs;
                renderCurrent(); // por si actualiza nombres de instalados
                renderDownloads();
                boolean anyToDownload = false;
                for (EmojiPackManager.PackInfo p : packs) {
                    if (!EmojiPackManager.isInstalled(p.id)) { anyToDownload = true; break; }
                }
                downloadInfo.setText(anyToDownload
                        ? "Se descargan de GitHub una sola vez y se guardan en el dispositivo."
                        : "No hay más packs para descargar por ahora.");
            } else {
                downloadInfo.setText("No se pudo obtener la lista de packs (" + error + ").");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        renderCurrent();
    }
}
