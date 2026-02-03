/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.FormUtils
 *  mchorse.bbs_mod.forms.FormUtilsClient
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.l10n.L10n
 *  mchorse.bbs_mod.settings.values.base.BaseValue
 *  mchorse.bbs_mod.ui.UIKeys
 *  mchorse.bbs_mod.ui.framework.UIContext
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlay
 *  mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel
 *  mchorse.bbs_mod.ui.utils.icons.Icons
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import com.wemppy.bbsbettertracks.bettertracks.Keys;
import java.util.Comparator;
import com.wemppy.bbsbettertracks.bettertracks.ui.UIColorPickerOverlayPanel;
import com.wemppy.bbsbettertracks.bettertracks.ui.UITrackRenameOverlayPanel;
import com.wemppy.bbsbettertracks.bettertracks.util.BetterTracksAccess;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import mchorse.bbs_mod.forms.FormUtils;
import mchorse.bbs_mod.forms.FormUtilsClient;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.l10n.L10n;
import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlay;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel;
import mchorse.bbs_mod.ui.utils.icons.Icons;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIKeyframes.class}, remap=false)
public abstract class UIKeyframesMixin {
    @Shadow
    private boolean single;
    @Shadow
    private UIKeyframeDopeSheet dopeSheet;

    @Shadow
    public abstract boolean isEditing();

    @Inject(method={"<init>"}, at={@At(value="TAIL")}, remap=false)
    private void bbsbettertracks$init(Consumer<?> callback, CallbackInfo ci) {
        ((UIKeyframes)(Object)this).context(menu -> {
            if (this.single) {
                return;
            }
            UIContext context = ((UIKeyframes)(Object)this).getContext();
            if (context == null) {
                return;
            }
            if (!this.isEditing()) {
                UIKeyframeSheet sheet = this.dopeSheet.getSheet(context.mouseY);
                if (sheet != null) {
                    menu.action(Icons.EDIT, UIKeys.GENERAL_RENAME, () -> this.bbsbettertracks$renameTrack(sheet));
                    menu.action(Icons.POSE, L10n.lang((String)"bbs.ui.keyframes.change_color"), () -> this.bbsbettertracks$changeTrackColor(sheet));
                }
                menu.action(Icons.REFRESH, L10n.lang((String)"bbs.ui.keyframes.reset_track_names"), this::bbsbettertracks$resetTrackNames);
                menu.action(Icons.POSE, L10n.lang((String)"bbs.ui.keyframes.auto_name_overlays"), this::bbsbettertracks$autoNameOverlays);
                
                UIKeyframeSheet poseSheet = this.dopeSheet.getSheet(context.mouseY);
                if (poseSheet != null && poseSheet.channel.getFactory() == mchorse.bbs_mod.utils.keyframes.factories.KeyframeFactories.POSE 
                    && poseSheet.id.equals("pose") && !poseSheet.selection.getSelected().isEmpty()) {
                    menu.action(Icons.CONVERT, L10n.lang((String)"bbs.ui.keyframes.distribute_to_overlays"), this::bbsbettertracks$distributePoseToOverlays);
                }
            }
        });
        ((UIKeyframes)(Object)this).keys().register(Keys.TOGGLE_INSERT_KEYFRAME, () -> {
            if (BetterTracksConfig.insertKeyframeOnBoneClick() != null) {
                mchorse.bbs_mod.settings.values.numeric.ValueBoolean setting = BetterTracksConfig.insertKeyframeOnBoneClick();
                boolean newValue = (Boolean)setting.get() == false;
                setting.preNotify(0);
                setting.set(newValue);
                setting.postNotify(0);
                String status = newValue ? "ON" : "OFF";
                ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.toggle_insert_keyframe").format(new Object[]{status}));
            }
        });
    }

    @Unique
    private void bbsbettertracks$renameTrack(UIKeyframeSheet sheet) {
        Form form = null;
        if (sheet.property != null) {
            form = FormUtils.getForm((BaseValue)sheet.property);
        }
        UITrackRenameOverlayPanel panel = new UITrackRenameOverlayPanel(UIKeys.GENERAL_RENAME, UIKeys.GENERAL_RENAME, newName -> {
            BetterTracksAccess.sheet(sheet).bbsbettertracks$setCustomName((String)newName);
            BetterTracksAccess.dope(this.dopeSheet).bbsbettertracks$setCustomTrackName(sheet.id, (String)newName);
        }, sheet.id, form);
        panel.text.setText(BetterTracksAccess.sheet(sheet).bbsbettertracks$getDisplayName());
        UIOverlay.addOverlay((UIContext)((UIKeyframes)(Object)this).getContext(), (UIOverlayPanel)panel);
    }

    @Unique
    private void bbsbettertracks$resetTrackNames() {
        BetterTracksAccess.dope(this.dopeSheet).bbsbettertracks$resetAllCustomTrackData();
    }

    @Unique
    private void bbsbettertracks$autoNameOverlays() {
        Form form = null;
        for (UIKeyframeSheet sheet : this.dopeSheet.getSheets()) {
            if (sheet.property == null || (form = FormUtils.getForm((BaseValue)sheet.property)) == null) continue;
            break;
        }
        if (form == null) {
            ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.no_named_pose_tracks"));
            return;
        }
        Form rootForm = FormUtils.getRoot(form);

        java.util.Comparator<String> groupOrder = (a, b) -> {
            boolean aEmpty = a == null || a.isEmpty();
            boolean bEmpty = b == null || b.isEmpty();
            if (aEmpty && !bEmpty) {
                return -1;
            }
            if (!aEmpty && bEmpty) {
                return 1;
            }
            if (a == null) {
                return b == null ? 0 : -1;
            }
            return b == null ? 1 : a.compareTo(b);
        };
        java.util.Map<String, java.util.List<UIKeyframeSheet>> overlaysByOwnerPath = new java.util.TreeMap<>(groupOrder);

        for (UIKeyframeSheet sheet : this.dopeSheet.getSheets()) {
            String trackId = sheet.id;
            int overlayIndex = trackId.indexOf("pose_overlay");
            if (overlayIndex < 0) {
                continue;
            }

            int slashBefore = trackId.lastIndexOf('/', overlayIndex);
            String ownerPath = slashBefore > 0 ? trackId.substring(0, slashBefore) : "";

            overlaysByOwnerPath.computeIfAbsent(ownerPath, k -> new java.util.ArrayList<>()).add(sheet);
        }

        if (overlaysByOwnerPath.isEmpty()) {
            ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.no_overlay_tracks"));
            return;
        }

        int totalRenamed = 0;
        Random random = new Random();

        for (java.util.Map.Entry<String, java.util.List<UIKeyframeSheet>> entry : overlaysByOwnerPath.entrySet()) {
            String ownerPath = entry.getKey();
            java.util.List<UIKeyframeSheet> overlays = entry.getValue();

            Form targetForm = ownerPath == null || ownerPath.isEmpty() ? rootForm : FormUtils.getForm(rootForm, ownerPath);
            if (targetForm == null) {
                continue;
            }

            List boneNames = FormUtilsClient.getBones(targetForm);
            if (boneNames.isEmpty()) {
                continue;
            }
            boneNames.sort(Comparator.naturalOrder());

            int renamed = Math.min(boneNames.size(), overlays.size());
            for (int i = 0; i < renamed; ++i) {
                UIKeyframeSheet overlay = (UIKeyframeSheet)overlays.get(i);
                String boneName = (String)boneNames.get(i);
                BetterTracksAccess.sheet(overlay).bbsbettertracks$setCustomName(boneName);
                BetterTracksAccess.dope(this.dopeSheet).bbsbettertracks$setCustomTrackName(overlay.id, boneName);
                float hue = random.nextFloat();
                float saturation = 0.7f + random.nextFloat() * 0.3f;
                float brightness = 0.8f + random.nextFloat() * 0.2f;
                int rgb = Color.HSBtoRGB(hue, saturation, brightness);
                int color = rgb & 0xFFFFFF;
                BetterTracksAccess.sheet(overlay).bbsbettertracks$setCustomColor(color);
                BetterTracksAccess.dope(this.dopeSheet).bbsbettertracks$setCustomTrackColor(overlay.id, color);
            }

            totalRenamed += renamed;
        }

        if (totalRenamed == 0) {
            ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.no_named_pose_tracks"));
            return;
        }

        ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.overlays_named_all").format(new Object[]{totalRenamed}));
    }

    @Unique
    private void bbsbettertracks$changeTrackColor(UIKeyframeSheet sheet) {
        UIColorPickerOverlayPanel panel = new UIColorPickerOverlayPanel(L10n.lang((String)"bbs.ui.keyframes.change_track_color"), L10n.lang((String)"bbs.ui.keyframes.change_track_color_tooltip"), color -> {
            BetterTracksAccess.sheet(sheet).bbsbettertracks$setCustomColor((Integer)color);
            BetterTracksAccess.dope(this.dopeSheet).bbsbettertracks$setCustomTrackColor(sheet.id, (Integer)color);
        });
        panel.color.picker.setColor(BetterTracksAccess.sheet(sheet).bbsbettertracks$getDisplayColor());
        UIOverlay.addOverlay((UIContext)((UIKeyframes)(Object)this).getContext(), (UIOverlayPanel)panel);
    }

    @Unique
    private void bbsbettertracks$distributePoseToOverlays() {
        java.util.Map<String, UIKeyframeSheet> poseSheetsByPath = new java.util.HashMap<>();
        
        for (UIKeyframeSheet sheet : this.dopeSheet.getSheets()) {
            if (sheet.id.endsWith("/pose") && sheet.channel.getFactory() == mchorse.bbs_mod.utils.keyframes.factories.KeyframeFactories.POSE) {
                String modelPath = sheet.id.substring(0, sheet.id.lastIndexOf("/pose"));
                poseSheetsByPath.put(modelPath, sheet);
            } else if (sheet.id.equals("pose") && sheet.channel.getFactory() == mchorse.bbs_mod.utils.keyframes.factories.KeyframeFactories.POSE) {
                poseSheetsByPath.put("", sheet);
            }
        }
        
        if (poseSheetsByPath.isEmpty()) {
            ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.no_selected_pose_keyframes"));
            return;
        }

        java.util.Map<String, java.util.Map<String, UIKeyframeSheet>> overlaysByModel = new java.util.HashMap<>();
        
        for (UIKeyframeSheet sheet : this.dopeSheet.getSheets()) {
            if (sheet.id.contains("pose_overlay")) {
                int overlayIndex = sheet.id.lastIndexOf("/pose_overlay");
                String modelPath;
                
                if (overlayIndex == -1) {
                    modelPath = "";
                } else {
                    modelPath = sheet.id.substring(0, overlayIndex);
                }
                
                String displayName = BetterTracksAccess.sheet(sheet).bbsbettertracks$getDisplayName();
                overlaysByModel.computeIfAbsent(modelPath, k -> new java.util.HashMap<>()).put(displayName, sheet);
            }
        }

        int totalProcessedKeyframes = 0;
        int totalMovedBones = 0;
        java.util.Set<UIKeyframeSheet> modifiedSheets = new java.util.HashSet<>();

        for (java.util.Map.Entry<String, UIKeyframeSheet> poseEntry : poseSheetsByPath.entrySet()) {
            String modelPath = poseEntry.getKey();
            UIKeyframeSheet poseSheet = poseEntry.getValue();
            
            if (poseSheet.selection.getSelected().isEmpty()) {
                continue;
            }

            java.util.Map<String, UIKeyframeSheet> boneTrackMap = overlaysByModel.get(modelPath);
            if (boneTrackMap == null || boneTrackMap.isEmpty()) {
                continue;
            }

            for (Object obj : poseSheet.selection.getSelected()) {
                mchorse.bbs_mod.utils.keyframes.Keyframe<mchorse.bbs_mod.utils.pose.Pose> poseKeyframe = (mchorse.bbs_mod.utils.keyframes.Keyframe<mchorse.bbs_mod.utils.pose.Pose>) obj;
                mchorse.bbs_mod.utils.pose.Pose originalPose = poseKeyframe.getValue();
                
                if (originalPose == null || originalPose.transforms.isEmpty()) {
                    continue;
                }

                java.util.List<String> bonesToRemove = new java.util.ArrayList<>();

                for (java.util.Map.Entry<String, mchorse.bbs_mod.utils.pose.PoseTransform> entry : originalPose.transforms.entrySet()) {
                    String boneName = entry.getKey();
                    mchorse.bbs_mod.utils.pose.PoseTransform boneTransform = entry.getValue();
                    
                    UIKeyframeSheet targetSheet = boneTrackMap.get(boneName);
                    
                    if (targetSheet != null) {
                        mchorse.bbs_mod.utils.pose.Pose newPose = new mchorse.bbs_mod.utils.pose.Pose();
                        newPose.transforms.put(boneName, (mchorse.bbs_mod.utils.pose.PoseTransform) boneTransform.copy());
                        
                        targetSheet.channel.insert(poseKeyframe.getTick(), newPose);
                        int insertedIndex = targetSheet.channel.getKeyframes().size() - 1;
                        mchorse.bbs_mod.utils.keyframes.Keyframe newKeyframe = targetSheet.channel.get(insertedIndex);
                        
                        if (newKeyframe != null) {
                            newKeyframe.getInterpolation().copy(poseKeyframe.getInterpolation());
                            if (poseKeyframe.getShape() != null) {
                                newKeyframe.setShape(poseKeyframe.getShape());
                            }
                            if (poseKeyframe.getColor() != null) {
                                newKeyframe.setColor(poseKeyframe.getColor());
                            }
                        }
                        bonesToRemove.add(boneName);
                        totalMovedBones++;
                        modifiedSheets.add(targetSheet);
                    }
                }

                for (String boneName : bonesToRemove) {
                    originalPose.transforms.remove(boneName);
                }

                totalProcessedKeyframes++;
            }
            
            modifiedSheets.add(poseSheet);
        }

        if (totalProcessedKeyframes > 0) {
            for (UIKeyframeSheet sheet : modifiedSheets) {
                sheet.channel.postNotify();
            }
            
            ((UIKeyframes)(Object)this).getContext().notifyInfo(
                L10n.lang((String)"bbs.ui.keyframes.distributed_bones")
                    .format(new Object[]{totalMovedBones, totalProcessedKeyframes})
            );
        } else {
            ((UIKeyframes)(Object)this).getContext().notifyInfo(L10n.lang((String)"bbs.ui.keyframes.no_bones_distributed"));
        }
    }
}

