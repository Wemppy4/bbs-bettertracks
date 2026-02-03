/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.settings.values.base.BaseValue
 *  mchorse.bbs_mod.settings.values.ui.ValueStringKeys
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin;

import java.lang.reflect.Field;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.settings.values.ui.ValueStringKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Form.class})
public class FormMixin {
    @Unique
    private ValueStringKeys bbsbettertracks$disabledSheets;

    @Inject(method={"<init>"}, at={@At(value="TAIL")}, remap=false)
    private void bbsbettertracks$init(CallbackInfo ci) {
        try {
            Form.class.getDeclaredField("disabledSheets");
            return;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            this.bbsbettertracks$disabledSheets = new ValueStringKeys("disabled_sheets");
            ((Form)(Object)this).add(this.bbsbettertracks$disabledSheets);
            this.bbsbettertracks$disabledSheets.invisible();
            return;
        }
    }

    @Unique
    public ValueStringKeys bbsbettertracks$getDisabledSheets() {
        try {
            Field f = Form.class.getDeclaredField("disabledSheets");
            f.setAccessible(true);
            Object value = f.get(this);
            if (value instanceof ValueStringKeys) {
                ValueStringKeys v = (ValueStringKeys)value;
                return v;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return this.bbsbettertracks$disabledSheets;
    }
}

