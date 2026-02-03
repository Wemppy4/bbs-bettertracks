package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import mchorse.bbs_mod.BBSSettings;
import mchorse.bbs_mod.settings.SettingsBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BBSSettings.class, remap = false)
public class BBSSettingsMixin {
    @Inject(method = "register", at = @At(value = "INVOKE", target = "Lmchorse/bbs_mod/settings/values/ui/ValueColors;<init>(Ljava/lang/String;)V", shift = At.Shift.AFTER), remap = false)
    private static void bbsbettertracks$register(SettingsBuilder builder, CallbackInfo ci) {
        BetterTracksConfig.ensureRegistered(builder);
    }
}
