/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.FormUtilsClient
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.l10n.keys.IKey
 *  mchorse.bbs_mod.ui.framework.elements.IFocusedUIElement
 *  mchorse.bbs_mod.ui.framework.elements.IUIElement
 *  mchorse.bbs_mod.ui.framework.elements.UIElement
 *  mchorse.bbs_mod.ui.framework.elements.input.list.UIStringList
 *  mchorse.bbs_mod.ui.framework.elements.input.text.UITextbox
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIMessageBarOverlayPanel
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.wemppy.bbsbettertracks.bettertracks.ui;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import mchorse.bbs_mod.forms.FormUtilsClient;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.ui.framework.elements.IFocusedUIElement;
import mchorse.bbs_mod.ui.framework.elements.IUIElement;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.input.list.UIStringList;
import mchorse.bbs_mod.ui.framework.elements.input.text.UITextbox;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIMessageBarOverlayPanel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class UITrackRenameOverlayPanel
extends UIMessageBarOverlayPanel {
    public UITextbox text;
    public UIStringList boneList;
    public Consumer<String> callback;
    private boolean hasBones;

    public UITrackRenameOverlayPanel(IKey title, IKey message, Consumer<String> callback, String trackId, Form form) {
        super(title, message);
        List bones;
        this.callback = callback;
        this.text = new UITextbox(null);
        this.hasBones = this.isPoseTrack(trackId);
        if (this.hasBones && form != null && !(bones = FormUtilsClient.getBones((Form)form)).isEmpty()) {
            this.boneList = new UIStringList(list -> this.text.setText((String)list.get(0)));
            this.boneList.add((Collection)bones);
            this.boneList.sort();
            this.boneList.h(100);
            this.boneList.relative(this.content).x(6).y(32).w(1.0f, -12);
            this.content.add((IUIElement)this.boneList);
            this.bar.y(1.0f, -6);
        }
        this.bar.prepend((IUIElement)this.text);
    }

    private boolean isPoseTrack(String trackId) {
        if (trackId == null) {
            return false;
        }
        String[] parts = trackId.split("/");
        if (parts.length == 0) {
            return false;
        }
        String lastPart = parts[parts.length - 1];
        return lastPart.equals("pose") || lastPart.startsWith("pose_overlay");
    }

    protected void onAdd(UIElement parent) {
        super.onAdd(parent);
        this.text.textbox.moveCursorToEnd();
        parent.getContext().focus((IFocusedUIElement)this.text);
    }

    public void confirm() {
        super.confirm();
        if (this.callback != null) {
            this.callback.accept(this.text.getText());
        }
    }
}

