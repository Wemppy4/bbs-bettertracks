/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.ui.forms.editors.panels.UIFormPanel
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package com.wemppy.bbsbettertracks.mixin.client;

import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.ui.forms.editors.panels.UIFormPanel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIFormPanel.class}, remap=false)
public interface UIFormPanelAccessor {
    @Accessor(value="form")
    public Form bbsbettertracks$getForm();
}

