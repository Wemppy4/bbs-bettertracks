/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.l10n.L10n
 *  mchorse.bbs_mod.l10n.keys.IKey
 *  mchorse.bbs_mod.ui.film.replays.UIReplaysEditor
 *  mchorse.bbs_mod.ui.framework.UIContext
 *  mchorse.bbs_mod.ui.framework.elements.IUIElement
 *  mchorse.bbs_mod.ui.framework.elements.UIElement
 *  mchorse.bbs_mod.ui.framework.elements.UIScrollView
 *  mchorse.bbs_mod.ui.framework.elements.buttons.UIButton
 *  mchorse.bbs_mod.ui.framework.elements.buttons.UIToggle
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel
 *  mchorse.bbs_mod.ui.utils.UI
 *  mchorse.bbs_mod.ui.utils.icons.Icon
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.wemppy.bbsbettertracks.bettertracks.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import mchorse.bbs_mod.l10n.L10n;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.ui.film.replays.UIReplaysEditor;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.IUIElement;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.UIScrollView;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIButton;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIToggle;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel;
import mchorse.bbs_mod.ui.utils.UI;
import mchorse.bbs_mod.ui.utils.icons.Icon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class UIFormTrackFilterOverlayPanel
extends UIOverlayPanel {
    private final List<UIToggle> toggles = new ArrayList<UIToggle>();
    private final Set<String> disabled;
    private final Set<String> keys;
    private final mchorse.bbs_mod.forms.forms.Form formTarget;
    private mchorse.bbs_mod.settings.values.ui.ValueStringKeys disabledValue;

    public UIFormTrackFilterOverlayPanel(Set<String> disabled, Set<String> keys, mchorse.bbs_mod.forms.forms.Form form, mchorse.bbs_mod.settings.values.ui.ValueStringKeys disabledValue) {
        super(L10n.lang((String)"bbs.ui.forms.editors.general.filter_tracks"));
        this.disabled = disabled;
        this.keys = keys;
        this.formTarget = form;
        this.disabledValue = disabledValue;
        UIButton disableAll = new UIButton(L10n.lang((String)"bbs.ui.forms.editors.general.disable_all"), b -> this.disableAll());
        disableAll.relative(this.content).w(1.0f).h(20);
        UIScrollView scrollView = UI.scrollView((int)4, (int)6, (UIElement[])new UIElement[0]);
        scrollView.relative(this.content).y(25).w(1.0f).h(1.0f, -25);
        this.content.add(new IUIElement[]{disableAll, scrollView});
        for (String key : keys) {
            UICoolToggle toggle = new UICoolToggle(key, IKey.constant((String)key), b -> {
                if (disabled.contains(key)) {
                    disabled.remove(key);
                } else {
                    disabled.add(key);
                }
            });
            toggle.h(20);
            toggle.setValue(!disabled.contains(key));
            scrollView.add((IUIElement)toggle);
            this.toggles.add(toggle);
        }
    }

    private void disableAll() {
        for (UIToggle toggle : this.toggles) {
            toggle.setValue(false);
        }
        this.disabled.clear();
        this.disabled.addAll(this.keys);
    }

    @Override
    public void close() {
        this.saveChanges();
        super.close();
    }

    private void saveChanges() {
        if (this.disabledValue != null) {
            try {
                this.disabledValue.postNotify(0);
            } catch (Throwable ignored) {}
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class UICoolToggle
    extends UIToggle {
        private final String key;

        public UICoolToggle(String key, IKey label, Consumer<UIToggle> callback) {
            super(label, callback);
            this.key = key;
        }

        protected void renderSkin(UIContext context) {
            int x = this.area.x;
            int y = this.area.y;
            int w = this.area.w;
            int h = this.area.h;
            Icon icon = UIReplaysEditor.getIcon((String)this.key);
            int color = UIReplaysEditor.getColor((String)this.key);
            context.batcher.box((float)x, (float)y, (float)(x + 2), (float)(y + h), 0xFF000000 | color);
            context.batcher.gradientHBox((float)(x + 2), (float)y, (float)(x + 24), (float)(y + h), 0x44000000 | color, color);
            context.batcher.icon(icon, (float)(x + 2), (float)(y + h / 2), 0.0f, 0.5f);
            this.area.x += 20;
            this.area.w -= 20;
            super.renderSkin(context);
            this.area.x = x;
            this.area.w = w;
        }
    }
}

