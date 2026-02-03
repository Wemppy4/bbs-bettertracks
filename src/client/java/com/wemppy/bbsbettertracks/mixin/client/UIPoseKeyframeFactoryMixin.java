/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.cubic.IModel
 *  mchorse.bbs_mod.forms.FormUtils
 *  mchorse.bbs_mod.forms.FormUtilsClient
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.forms.forms.MobForm
 *  mchorse.bbs_mod.forms.forms.ModelForm
 *  mchorse.bbs_mod.forms.renderers.ModelFormRenderer
 *  mchorse.bbs_mod.settings.values.base.BaseValue
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories.UIPoseKeyframeFactory
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories.UIPoseKeyframeFactory$UIPoseFactoryEditor
 *  mchorse.bbs_mod.utils.keyframes.Keyframe
 *  mchorse.bbs_mod.utils.pose.Pose
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import com.wemppy.bbsbettertracks.bettertracks.util.BetterTracksAccess;
import java.util.Collection;
import java.util.List;
import mchorse.bbs_mod.cubic.IModel;
import mchorse.bbs_mod.forms.FormUtils;
import mchorse.bbs_mod.forms.FormUtilsClient;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.forms.forms.MobForm;
import mchorse.bbs_mod.forms.forms.ModelForm;
import mchorse.bbs_mod.forms.renderers.ModelFormRenderer;
import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories.UIPoseKeyframeFactory;
import mchorse.bbs_mod.utils.keyframes.Keyframe;
import mchorse.bbs_mod.utils.pose.Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIPoseKeyframeFactory.class}, remap=false)
public class UIPoseKeyframeFactoryMixin {
    @Shadow
    public UIPoseKeyframeFactory.UIPoseFactoryEditor poseEditor;

    @Inject(method={"<init>"}, at={@At(value="TAIL")}, remap=false)
    private void bbsbettertracks$autoPickBone(Keyframe<Pose> keyframe, UIKeyframes editor, CallbackInfo ci) {
        if (BetterTracksConfig.autoSelectBoneByTrackName() == null || !((Boolean)BetterTracksConfig.autoSelectBoneByTrackName().get()).booleanValue()) {
            return;
        }
        UIKeyframeSheet sheet = editor.getGraph().getSheet(keyframe);
        if (sheet == null || sheet.property == null) {
            return;
        }
        String trackName = BetterTracksAccess.sheet(sheet).bbsbettertracks$getDisplayName();
        if (trackName == null || trackName.isEmpty()) {
            return;
        }
        Form form = FormUtils.getForm((BaseValue)sheet.property);
        if (form instanceof ModelForm) {
            Collection bones;
            ModelForm modelForm = (ModelForm)form;
            var modelInstance = ((ModelFormRenderer)FormUtilsClient.getRenderer((Form)modelForm)).getModel();
            if (modelInstance != null && (bones = modelInstance.model.getAllGroupKeys()).contains(trackName)) {
                this.poseEditor.selectBone(trackName);
            }
        } else {
            MobForm mobForm;
            List bones;
            Form mobFormObj = FormUtils.getForm((BaseValue)sheet.property);
            if (mobFormObj instanceof MobForm && (bones = FormUtilsClient.getRenderer((Form)(mobForm = (MobForm)mobFormObj)).getBones()).contains(trackName)) {
                this.poseEditor.selectBone(trackName);
            }
        }
    }
}

