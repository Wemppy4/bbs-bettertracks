/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.settings.values.numeric.ValueInt
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.IUIKeyframeGraph
 *  mchorse.bbs_mod.utils.colors.Color
 *  mchorse.bbs_mod.utils.keyframes.Keyframe
 *  mchorse.bbs_mod.utils.keyframes.KeyframeSegment
 *  mchorse.bbs_mod.utils.keyframes.KeyframeShape
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import mchorse.bbs_mod.settings.values.numeric.ValueInt;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.IUIKeyframeGraph;
import mchorse.bbs_mod.utils.colors.Color;
import mchorse.bbs_mod.utils.keyframes.Keyframe;
import mchorse.bbs_mod.utils.keyframes.KeyframeSegment;
import mchorse.bbs_mod.utils.keyframes.KeyframeShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(value=EnvType.CLIENT)
@Mixin(value={IUIKeyframeGraph.class}, remap=false)
public interface IUIKeyframeGraphMixin {
    @Overwrite
    default public Keyframe addKeyframe(UIKeyframeSheet sheet, float tick, Object value) {
        KeyframeSegment segment = sheet.channel.find(tick);
        Keyframe extra = null;
        if (value == null) {
            if (segment != null) {
                value = segment.createInterpolated();
                extra = segment.a;
            } else {
                value = sheet.property != null ? sheet.channel.getFactory().copy(sheet.property.get()) : sheet.channel.getFactory().createEmpty();
            }
        }
        int index = sheet.channel.insert(tick, value);
        Keyframe keyframe = sheet.channel.get(index);
        if (extra != null) {
            keyframe.copyOverExtra(extra);
        } else {
            ValueInt colorValue;
            ValueInt shapeValue = BetterTracksConfig.defaultKeyframeShape();
            if (shapeValue != null) {
                int ordinal = Math.max(0, Math.min(KeyframeShape.values().length - 1, (Integer)shapeValue.get()));
                keyframe.setShape(KeyframeShape.values()[ordinal]);
            }
            if ((colorValue = BetterTracksConfig.defaultKeyframeColor()) != null && (Integer)colorValue.get() != 0) {
                keyframe.setColor(new Color().set(((Integer)colorValue.get()).intValue()));
            }
        }
        IUIKeyframeGraph self = (IUIKeyframeGraph)this;
        self.clearSelection();
        self.pickKeyframe(keyframe);
        sheet.selection.add(index);
        return keyframe;
    }
}

