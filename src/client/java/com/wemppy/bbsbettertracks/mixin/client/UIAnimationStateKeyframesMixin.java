/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.ui.forms.editors.UIFormEditor
 *  mchorse.bbs_mod.ui.forms.editors.states.keyframes.UIAnimationStateKeyframes
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package com.wemppy.bbsbettertracks.mixin.client;

import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.ui.forms.editors.UIFormEditor;
import mchorse.bbs_mod.ui.forms.editors.states.keyframes.UIAnimationStateKeyframes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIAnimationStateKeyframes.class}, remap=false)
public class UIAnimationStateKeyframesMixin {
    @Shadow
    public UIFormEditor editor;

    public Form bbsbettertracks$getForm() {
        return this.editor != null ? this.editor.form : null;
    }
}

