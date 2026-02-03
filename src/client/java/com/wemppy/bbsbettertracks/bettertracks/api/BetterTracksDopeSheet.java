/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.wemppy.bbsbettertracks.bettertracks.api;

import java.util.List;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public interface BetterTracksDopeSheet {
    public void bbsbettertracks$onRemoveAllSheets(List<UIKeyframeSheet> var1);

    public void bbsbettertracks$onAddSheet(UIKeyframeSheet var1);

    public void bbsbettertracks$setCustomTrackName(String var1, String var2);

    public void bbsbettertracks$setCustomTrackColor(String var1, Integer var2);

    public void bbsbettertracks$resetAllCustomTrackData();

    public void bbsbettertracks$copyCustomTrackData(UIKeyframeDopeSheet var1);
}

