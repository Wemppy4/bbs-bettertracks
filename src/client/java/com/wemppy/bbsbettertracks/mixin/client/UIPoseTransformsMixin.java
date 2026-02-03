package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import mchorse.bbs_mod.ui.film.UIFilmPanel;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories.UIPoseKeyframeFactory;
import mchorse.bbs_mod.utils.Axis;
import mchorse.bbs_mod.utils.keyframes.Keyframe;
import mchorse.bbs_mod.utils.pose.Pose;
import mchorse.bbs_mod.utils.pose.PoseTransform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Environment(EnvType.CLIENT)
@Mixin(value = UIPoseKeyframeFactory.UIPoseTransforms.class, remap = false)
public abstract class UIPoseTransformsMixin {
    
    @Shadow
    private UIPoseKeyframeFactory.UIPoseFactoryEditor editor;

    @Inject(method = "setT", at = @At("HEAD"))
    private void onSetT(Axis axis, double x, double y, double z, CallbackInfo ci) {
        checkAutoKeyframe();
    }

    @Inject(method = "setS", at = @At("HEAD"))
    private void onSetS(Axis axis, double x, double y, double z, CallbackInfo ci) {
        checkAutoKeyframe();
    }

    @Inject(method = "setR", at = @At("HEAD"))
    private void onSetR(Axis axis, double x, double y, double z, CallbackInfo ci) {
        checkAutoKeyframe();
    }

    @Inject(method = "setR2", at = @At("HEAD"))
    private void onSetR2(Axis axis, double x, double y, double z, CallbackInfo ci) {
        checkAutoKeyframe();
    }

    private void checkAutoKeyframe() {
        if (!BetterTracksConfig.realtimeKeyframeRecording().get()) {
            return;
        }

        try {
            // Access private fields using reflection
            Field editorField = this.editor.getClass().getDeclaredField("editor");
            editorField.setAccessible(true);
            UIKeyframes uiKeyframes = (UIKeyframes) editorField.get(this.editor);
            
            Field keyframeField = this.editor.getClass().getDeclaredField("keyframe");
            keyframeField.setAccessible(true);
            Keyframe<Pose> currentKeyframe = (Keyframe<Pose>) keyframeField.get(this.editor);

            UIFilmPanel filmPanel = uiKeyframes.getParent(UIFilmPanel.class);

            if (filmPanel != null && currentKeyframe != null) {
                int cursor = filmPanel.getCursor();

                if (cursor != currentKeyframe.getTick()) {
                    UIKeyframeSheet sheet = uiKeyframes.getGraph().getSheet(currentKeyframe);

                    if (sheet != null) {
                        // Use interpolated pose at current cursor position instead of copying previous keyframe
                        Pose pose = (Pose) sheet.channel.interpolate(cursor);
                        String currentBone = this.editor.groups.getCurrentFirst();
                        int index = sheet.channel.insert(cursor, pose);
                        Keyframe<Pose> newKeyframe = sheet.channel.get(index);

                        // Clear selection and select the new keyframe FIRST
                        sheet.selection.clear();
                        sheet.selection.add(index);  // Use index, not keyframe object!

                        // Update keyframe using reflection
                        keyframeField.set(this.editor, newKeyframe);
                        
                        // Get pose group key using reflection
                        Method getPoseGroupKeyMethod = this.editor.getClass().getDeclaredMethod("getPoseGroupKey");
                        getPoseGroupKeyMethod.setAccessible(true);
                        String poseGroupKey = (String) getPoseGroupKeyMethod.invoke(this.editor);
                        
                        this.editor.setPose(newKeyframe.getValue(), poseGroupKey);

                        if (currentBone != null) {
                            this.editor.groups.setCurrentScroll(currentBone);
                        }

                        try {
                            // Call refreshCurrentBone() using reflection
                            Method refreshMethod = this.editor.getClass().getDeclaredMethod("refreshCurrentBone");
                            refreshMethod.setAccessible(true);
                            refreshMethod.invoke(this.editor);
                        } catch (Exception e) {
                            // Fallback: call pickBone using reflection
                            try {
                                Method pickBoneMethod = this.editor.getClass().getDeclaredMethod("pickBone", String.class);
                                pickBoneMethod.setAccessible(true);
                                pickBoneMethod.invoke(this.editor, currentBone);
                            } catch (Exception ex) {
                                // Ignore
                            }
                        }

                        // Explicitly update the transform editor's reference to the new keyframe's bone data
                        // This prevents the 'accumulation' bug where dx/dy/dz are calculated against the OLD keyframe
                        // but applied to the NEW keyframe, leading to exponential growth.
                        if (currentBone != null) {
                            PoseTransform pt = newKeyframe.getValue().get(currentBone);
                            if (pt != null) {
                                // Call setTransform on UIPoseTransforms using reflection
                                try {
                                    Method setTransformMethod = this.getClass().getMethod("setTransform", PoseTransform.class);
                                    setTransformMethod.invoke(this, pt);
                                } catch (Exception e) {
                                    // Ignore
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Silently ignore errors
        }
    }
}
