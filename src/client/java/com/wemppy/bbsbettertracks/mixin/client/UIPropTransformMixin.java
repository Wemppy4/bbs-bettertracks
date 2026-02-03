package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import mchorse.bbs_mod.ui.framework.elements.input.UIPropTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(value = UIPropTransform.class, remap = false)
public abstract class UIPropTransformMixin {
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        // Set local mode based on config
        if (BetterTracksConfig.localTransformByDefault().get()) {
            try {
                // Call the private toggleLocal() method to properly initialize local mode
                Method toggleLocal = UIPropTransform.class.getDeclaredMethod("toggleLocal");
                toggleLocal.setAccessible(true);
                toggleLocal.invoke((UIPropTransform)(Object)this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
