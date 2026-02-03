/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeGraph
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.util.BetterTracksAccess;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeGraph;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIKeyframeGraph.class}, remap=false)
public class UIKeyframeGraphRenderMixin {
    @Redirect(method={"renderGraph"}, at=@At(value="FIELD", target="Lmchorse/bbs_mod/ui/framework/elements/input/keyframes/UIKeyframeSheet;color:I"), require=0, remap=false)
    private int bbsbettertracks$displayColor(UIKeyframeSheet sheet) {
        return BetterTracksAccess.sheet(sheet).bbsbettertracks$getDisplayColor();
    }
}

