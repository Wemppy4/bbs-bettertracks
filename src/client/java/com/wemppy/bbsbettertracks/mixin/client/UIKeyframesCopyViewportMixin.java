/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.util.BetterTracksAccess;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIKeyframes.class}, remap=false)
public class UIKeyframesCopyViewportMixin {
    @Inject(method={"copyViewport"}, at={@At(value="TAIL")}, remap=false)
    private void bbsbettertracks$copyCustom(UIKeyframes lastEditor, CallbackInfo ci) {
        BetterTracksAccess.dope(((UIKeyframes)(Object)this).getDopeSheet()).bbsbettertracks$copyCustomTrackData(lastEditor.getDopeSheet());
    }
}

