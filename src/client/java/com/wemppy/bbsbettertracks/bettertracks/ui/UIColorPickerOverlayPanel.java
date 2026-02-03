/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.l10n.keys.IKey
 *  mchorse.bbs_mod.ui.framework.elements.IUIElement
 *  mchorse.bbs_mod.ui.framework.elements.UIElement
 *  mchorse.bbs_mod.ui.framework.elements.input.UIColor
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIMessageBarOverlayPanel
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.wemppy.bbsbettertracks.bettertracks.ui;

import java.util.function.Consumer;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.ui.framework.elements.IUIElement;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.input.UIColor;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIMessageBarOverlayPanel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class UIColorPickerOverlayPanel
extends UIMessageBarOverlayPanel {
    public UIColor color;
    public Consumer<Integer> callback;

    public UIColorPickerOverlayPanel(IKey title, IKey message, Consumer<Integer> callback) {
        super(title, message);
        this.callback = callback;
        this.color = new UIColor(c -> {});
        this.color.withAlpha();
        this.bar.prepend((IUIElement)this.color);
    }

    protected void onAdd(UIElement parent) {
        super.onAdd(parent);
        this.color.picker.resize();
    }

    public void confirm() {
        super.confirm();
        if (this.callback != null) {
            this.callback.accept(this.color.picker.color.getARGBColor());
        }
    }
}

