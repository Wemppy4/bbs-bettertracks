/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.FormUtils
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.l10n.L10n
 *  mchorse.bbs_mod.settings.values.ui.ValueStringKeys
 *  mchorse.bbs_mod.ui.forms.editors.panels.UIFormPanel
 *  mchorse.bbs_mod.ui.forms.editors.panels.UIGeneralFormPanel
 *  mchorse.bbs_mod.ui.framework.UIContext
 *  mchorse.bbs_mod.ui.framework.elements.IUIElement
 *  mchorse.bbs_mod.ui.framework.elements.buttons.UIButton
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlay
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.ui.UIFormTrackFilterOverlayPanel;
import com.wemppy.bbsbettertracks.mixin.client.UIFormPanelAccessor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import mchorse.bbs_mod.forms.FormUtils;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.l10n.L10n;
import mchorse.bbs_mod.settings.values.ui.ValueStringKeys;
import mchorse.bbs_mod.ui.forms.editors.panels.UIFormPanel;
import mchorse.bbs_mod.ui.forms.editors.panels.UIGeneralFormPanel;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.IUIElement;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIButton;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlay;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIGeneralFormPanel.class}, remap=false)
public abstract class UIGeneralFormPanelMixin {
    @Unique
    private UIButton bbsbettertracks$filterTracks;

    @Inject(method={"<init>"}, at={@At(value="TAIL")}, remap=false)
    private void bbsbettertracks$init(CallbackInfo ci) {
        this.bbsbettertracks$filterTracks = new UIButton(L10n.lang((String)"bbs.ui.forms.editors.general.filter_tracks"), b -> this.bbsbettertracks$openFilterTracksOverlay());
        this.bbsbettertracks$filterTracks.tooltip(L10n.lang((String)"bbs.ui.forms.editors.general.filter_tracks-tooltip"));
        ((UIFormPanel)(Object)this).options.addAfter((IUIElement)((UIGeneralFormPanel)(Object)this).animatable, (IUIElement)this.bbsbettertracks$filterTracks);
    }

    @Unique
    private void bbsbettertracks$openFilterTracksOverlay() {
        HashSet<String> disabled;
        Form targetForm = ((UIFormPanelAccessor)((Object)this)).bbsbettertracks$getForm();
        if (targetForm == null) {
            return;
        }
        LinkedHashSet<String> allTracks = new LinkedHashSet<String>();
        for (String path : FormUtils.collectPropertyPaths((Form)targetForm)) {
            if (path.matches("^\\d+/.*")) continue;
            allTracks.add(path);
        }
        if (allTracks.isEmpty()) {
            return;
        }
        ValueStringKeys disabledValue = null;
        try {
            Object v = Form.class.getDeclaredField("disabledSheets").get(targetForm);
            if (v instanceof ValueStringKeys) {
                disabledValue = (ValueStringKeys)v;
                disabled = (HashSet<String>)disabledValue.get();
            } else {
                disabled = new HashSet();
            }
        }
        catch (Throwable t) {
            try {
                Method m = targetForm.getClass().getMethod("bbsbettertracks$getDisabledSheets", new Class[0]);
                Object keys = m.invoke(targetForm, new Object[0]);
                if (keys instanceof ValueStringKeys) {
                    disabledValue = (ValueStringKeys)keys;
                    disabled = (HashSet<String>)disabledValue.get();
                } else {
                    disabled = new HashSet();
                }
            }
            catch (Throwable ignored) {
                disabled = new HashSet<String>();
            }
        }
        UIFormTrackFilterOverlayPanel panel = new UIFormTrackFilterOverlayPanel(disabled, allTracks, targetForm, disabledValue);
        UIOverlay.addOverlay((UIContext)((UIGeneralFormPanel)(Object)this).getContext(), (UIOverlayPanel)panel, (int)240, (float)0.9f);
    }
}

