/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.l10n.keys.IKey
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.api.BetterTracksSheet;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIKeyframeSheet.class}, remap=false)
public class UIKeyframeSheetMixin
implements BetterTracksSheet {
    @Shadow
    public IKey title;
    @Shadow
    public int color;
    @Unique
    private String bbsbettertracks$customName;
    @Unique
    private Integer bbsbettertracks$customColor;

    @Override
    public void bbsbettertracks$setCustomName(String customName) {
        this.bbsbettertracks$customName = customName;
    }

    @Override
    public String bbsbettertracks$getCustomName() {
        return this.bbsbettertracks$customName;
    }

    @Override
    public String bbsbettertracks$getDisplayName() {
        return this.bbsbettertracks$customName != null && !this.bbsbettertracks$customName.isEmpty() ? this.bbsbettertracks$customName : this.title.get();
    }

    @Override
    public void bbsbettertracks$setCustomColor(Integer customColor) {
        this.bbsbettertracks$customColor = customColor;
    }

    @Override
    public Integer bbsbettertracks$getCustomColor() {
        return this.bbsbettertracks$customColor;
    }

    @Override
    public int bbsbettertracks$getDisplayColor() {
        return this.bbsbettertracks$customColor != null ? this.bbsbettertracks$customColor : this.color;
    }
}

