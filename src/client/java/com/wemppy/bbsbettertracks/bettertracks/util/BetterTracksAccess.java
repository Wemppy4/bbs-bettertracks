/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.wemppy.bbsbettertracks.bettertracks.util;

import com.wemppy.bbsbettertracks.bettertracks.api.BetterTracksDopeSheet;
import com.wemppy.bbsbettertracks.bettertracks.api.BetterTracksSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class BetterTracksAccess {
    public static BetterTracksSheet sheet(UIKeyframeSheet sheet) {
        return (BetterTracksSheet)sheet;
    }

    public static BetterTracksDopeSheet dope(UIKeyframeDopeSheet dopeSheet) {
        return (BetterTracksDopeSheet)dopeSheet;
    }
}

