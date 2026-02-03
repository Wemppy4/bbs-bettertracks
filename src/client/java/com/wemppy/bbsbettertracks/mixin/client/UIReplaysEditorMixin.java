/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.BBSSettings
 *  mchorse.bbs_mod.forms.FormUtils
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.settings.values.base.BaseValue
 *  mchorse.bbs_mod.settings.values.ui.ValueStringKeys
 *  mchorse.bbs_mod.ui.film.replays.UIReplaysEditor
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes
 *  mchorse.bbs_mod.utils.StringUtils
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.LocalCapture
 */
package com.wemppy.bbsbettertracks.mixin.client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import mchorse.bbs_mod.BBSSettings;
import mchorse.bbs_mod.forms.FormUtils;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.settings.values.ui.ValueStringKeys;
import mchorse.bbs_mod.ui.film.replays.UIReplaysEditor;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes;
import mchorse.bbs_mod.utils.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIReplaysEditor.class}, remap=false)
public class UIReplaysEditorMixin {
    @Shadow
    public Set<String> keys;

    @Inject(method={"updateChannelsList"}, at={@At(value="INVOKE", target="Ljava/util/Set;clear()V", shift=At.Shift.AFTER)}, locals=LocalCapture.CAPTURE_FAILSOFT, remap=false)
    private void bbsbettertracks$filterPerForm(CallbackInfo ci, UIKeyframes lastEditor, List<UIKeyframeSheet> sheets) {
        if (sheets == null) {
            return;
        }
        for (UIKeyframeSheet sheet : List.copyOf(sheets)) {
            this.keys.add(StringUtils.fileName((String)sheet.id));
        }
        sheets.removeIf(v -> {
            Form propertyOwner;
            String fileName = StringUtils.fileName((String)v.id);
            if (BBSSettings.disabledSheets != null && UIReplaysEditorMixin.isTrackDisabled(fileName, v.id, (Set)BBSSettings.disabledSheets.get())) {
                return true;
            }
            Form form = propertyOwner = v.property != null ? FormUtils.getForm((BaseValue)v.property) : null;
            if (propertyOwner != null) {
                Set disabled = null;
                try {
                    Field f = Form.class.getDeclaredField("disabledSheets");
                    f.setAccessible(true);
                    Object value = f.get(propertyOwner);
                    if (value instanceof ValueStringKeys) {
                        ValueStringKeys keys = (ValueStringKeys)value;
                        disabled = (Set)keys.get();
                    }
                }
                catch (Throwable f) {
                    // empty catch block
                }
                if (disabled == null) {
                    try {
                        Method m = propertyOwner.getClass().getMethod("bbsbettertracks$getDisabledSheets", new Class[0]);
                        Object keys = m.invoke(propertyOwner, new Object[0]);
                        if (keys instanceof ValueStringKeys) {
                            ValueStringKeys vKeys = (ValueStringKeys)keys;
                            disabled = (Set)vKeys.get();
                        }
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                if (disabled != null) {
                    return UIReplaysEditorMixin.isTrackDisabled(fileName, v.id, disabled);
                }
            }
            return false;
        });
    }

    private static boolean isTrackDisabled(String fileName, String fullPath, Set<String> disabledTracks) {
        for (String filter : disabledTracks) {
            if (!fileName.equals(filter) && !fullPath.equals(filter) && !fullPath.endsWith("/" + filter)) continue;
            return true;
        }
        return false;
    }
}

