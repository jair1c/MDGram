package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.telegram.messenger.MDGramConfig;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;

// MDGram: pantalla "Pantalla principal" del hub (réplica del original "Página Principal").
// FUNCIONALES: Título de inicio (nombre en vez de "MDGram"), Ocultar botón flotante.
// Placeholders: ocultar barra de búsqueda (acoplada al scroll, se hará con cuidado aparte),
// Carpeta (estilos de tabs de carpetas) y Bottom Tab (tocan la barra píldora, que NO se toca).
public class HomeSettingsActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("Pantalla principal");
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

        // ---- General (título + búsqueda) ----
        addHeader(context, content, "General");
        LinearLayout g1 = group(context, content);
        addCheck(context, g1, "Título de inicio", "Muestra tu nombre en vez de \"MDGram\"", MDGramConfig.homeTitleAsName(), true, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHomeTitleAsName(c);
            toast("Reinicia la app para aplicar");
        });
        addCheck(context, g1, "Ocultar barra de búsqueda", "En la lista principal de chats", MDGramConfig.hideSearchBar(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHideSearchBar(c);
            toast("Reinicia la app para aplicar");
        });

        // ---- Carpeta (tabs de carpetas) — placeholders ----
        addHeader(context, content, "Carpeta");
        LinearLayout g2 = group(context, content);
        addValue(context, g2, "TabTitleStyle", null, true, () -> soon("TabTitleStyle"));
        addValue(context, g2, "TabStyle", null, true, () -> soon("TabStyle"));
        addValue(context, g2, "Ocultar la carpeta \"Todos\"", null, false, () -> soon("Ocultar la carpeta Todos"));

        // ---- Botón flotante ----
        addHeader(context, content, "Botón flotante");
        LinearLayout g3 = group(context, content);
        addCheck(context, g3, "Ocultar botón flotante", "Solo en la lista de chats", MDGramConfig.hideFab(), false, cell -> {
            boolean c = !cell.isChecked();
            cell.setChecked(c);
            MDGramConfig.setHideFab(c);
            toast("Reinicia la app para aplicar");
        });

        TextInfoPrivacyCell info = new TextInfoPrivacyCell(context);
        info.setText("Los estilos de pestañas (Carpeta / Bottom Tab) tocan la barra de navegación y se agregarán con cuidado más adelante.");
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

    private void soon(String what) {
        toast(what + ": próximamente");
    }

    private void toast(String msg) {
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
