package org.telegram.ui;

import android.view.View;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FontSelectActivity extends UniversalFragment {

    private static final LinkedHashMap<String, String> FONTS = new LinkedHashMap<>();
    static {
        FONTS.put("Predeterminada", null);
        FONTS.put("Google Sans", "GoogleSans-Bold.ttf");
        FONTS.put("Samsung One UI", "SamsungOneUI-Bold.ttf");
        FONTS.put("Expletus Sans", "ExpletusSans-Bold.ttf");
        FONTS.put("Rubrik", "Rubrik-Bold.ttf");
        FONTS.put("SciFly Sans", "SciFlySans-Bold.ttf");
        FONTS.put("Ubuntu Titling", "UbuntuTitling-Bold.ttf");
    }

    @Override
    protected CharSequence getTitle() {
        return "Fuente de la app";
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asShadow("Elige la fuente que se usará para los títulos, nombres y botones en toda la app. Requiere reiniciar MDGram para aplicarse por completo."));
        int id = 0;
        for (Map.Entry<String, String> entry : FONTS.entrySet()) {
            UItem item = UItem.asRadio(id, entry.getKey());
            item.checked = java.util.Objects.equals(SharedConfig.selectedFontAsset, entry.getValue());
            items.add(item);
            id++;
        }
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        int index = 0;
        for (Map.Entry<String, String> entry : FONTS.entrySet()) {
            if (index == item.id) {
                SharedConfig.setSelectedFontAsset(entry.getValue());
                listView.adapter.update(true);
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), "Reinicia MDGram para aplicar la fuente", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            index++;
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
