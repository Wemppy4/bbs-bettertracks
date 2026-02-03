package com.wemppy.bbsbettertracks.mixin.client;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import com.wemppy.bbsbettertracks.bettertracks.ui.ToggleValueSync;

import mchorse.bbs_mod.settings.ui.UIValueFactory;
import mchorse.bbs_mod.settings.values.numeric.ValueBoolean;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIToggle;

@Mixin(value = UIValueFactory.class, remap = false)
public abstract class UIValueFactoryMixin
{
    @Inject(method = "booleanUI", at = @At("RETURN"))
    private static void bbsbt$syncInsertKeyframeOnBoneClick(ValueBoolean value, Consumer<UIToggle> callback, CallbackInfoReturnable<UIToggle> cir)
    {
        UIToggle toggle = cir.getReturnValue();

        if (toggle == null || value == null)
        {
            return;
        }

        ValueBoolean target = BetterTracksConfig.insertKeyframeOnBoneClick();

        if (target == value)
        {
            ToggleValueSync.bind(value, toggle);
        }
    }
}
