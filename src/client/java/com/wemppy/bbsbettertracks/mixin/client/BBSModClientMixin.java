/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.BBSModClient
 *  mchorse.bbs_mod.l10n.L10n
 *  mchorse.bbs_mod.resources.Link
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import java.util.Collections;
import mchorse.bbs_mod.BBSModClient;
import mchorse.bbs_mod.l10n.L10n;
import mchorse.bbs_mod.resources.Link;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={BBSModClient.class}, remap=false)
public abstract class BBSModClientMixin {
    @Shadow
    private static L10n l10n;
    @Unique
    private static boolean bbsbettertracks$registeredLang;

    @Inject(method={"onInitializeClient"}, at={@At(value="INVOKE", target="Lmchorse/bbs_mod/l10n/L10n;reload()V", shift=At.Shift.BEFORE)}, remap=false)
    private void bbsbettertracks$registerLang(CallbackInfo ci) {
        if (bbsbettertracks$registeredLang) {
            return;
        }
        bbsbettertracks$registeredLang = true;
        if (l10n != null) {
            l10n.register(lang -> Collections.singletonList(Link.assets((String)("strings/bettertracks/" + lang + ".json"))));
        }
    }
}

