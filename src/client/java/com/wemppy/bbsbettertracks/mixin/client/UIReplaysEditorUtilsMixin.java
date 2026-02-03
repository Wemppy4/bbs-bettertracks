/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.forms.FormUtils
 *  mchorse.bbs_mod.forms.forms.Form
 *  mchorse.bbs_mod.ui.film.ICursor
 *  mchorse.bbs_mod.ui.film.replays.UIReplaysEditorUtils
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeEditor
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories.UIKeyframeFactory
 *  mchorse.bbs_mod.utils.StringUtils
 *  mchorse.bbs_mod.utils.keyframes.Keyframe
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import com.wemppy.bbsbettertracks.bettertracks.util.BetterTracksAccess;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mchorse.bbs_mod.forms.FormUtils;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.ui.film.ICursor;
import mchorse.bbs_mod.ui.film.replays.UIReplaysEditorUtils;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeEditor;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories.UIKeyframeFactory;
import mchorse.bbs_mod.utils.StringUtils;
import mchorse.bbs_mod.utils.keyframes.Keyframe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIReplaysEditorUtils.class}, remap=false)
public class UIReplaysEditorUtilsMixin {
    
    @Inject(method = "pickForm", at = @At("HEAD"), cancellable = true, remap = false)
    private static void bbsbettertracks$pickForm(UIKeyframeEditor keyframeEditor, ICursor cursor, Form form, String bone, CallbackInfo ci) {
        String path = FormUtils.getPath((Form)form);
        if (keyframeEditor == null || bone == null || bone.isEmpty()) {
            ci.cancel();
            return;
        }
        
        boolean insertKeyframe = BetterTracksConfig.insertKeyframeOnBoneClick() != null && (Boolean)BetterTracksConfig.insertKeyframeOnBoneClick().get() != false;
        boolean insertOnOverlays = BetterTracksConfig.insertKeyframeOnOverlays() != null && (Boolean)BetterTracksConfig.insertKeyframeOnOverlays().get() != false;
        boolean autoSelect = BetterTracksConfig.autoSelectTrackByBoneName() != null && ((Boolean)BetterTracksConfig.autoSelectTrackByBoneName().get()).booleanValue();

        String key = null;
        UIKeyframeSheet matchingSheet = null;

        if (insertKeyframe && insertOnOverlays) {
            matchingSheet = UIReplaysEditorUtilsMixin.findSheetByBoneName(keyframeEditor, form, bone);
        } else if (autoSelect) {
            matchingSheet = UIReplaysEditorUtilsMixin.findSheetByBoneName(keyframeEditor, form, bone);
        }
        
        if (matchingSheet != null) {
            int currentTick = cursor.getCursor();
            boolean hasKeyframeNearby = UIReplaysEditorUtilsMixin.hasKeyframeNearTick(matchingSheet, currentTick, 5);
            
            if (hasKeyframeNearby || insertKeyframe) {
                String selectedTrackId;
                key = matchingSheet.id;
                Keyframe selected = keyframeEditor.view.getGraph().getSelected();
                if (selected != null && !(selectedTrackId = selected.getParent().getId()).equals(key)) {
                    keyframeEditor.view.getGraph().clearSelection();
                }
            }
        } 
        
        if (key == null) {
            Keyframe selected = keyframeEditor.view.getGraph().getSelected();
            String type = "pose";

            if (selected != null) {
                String id = selected.getParent().getId();
                int index = id.indexOf("pose_overlay");

                if (index >= 0) {
                    type = id.substring(index);
                }
            } else {
                UIKeyframeSheet lastSheet = keyframeEditor.view.getGraph().getLastSheet();

                if (lastSheet != null && lastSheet.property != null) {
                    if (FormUtils.getPath((Form) lastSheet.property.getParent()).equals(FormUtils.getPath(form)) && lastSheet.property.getId().startsWith("pose") && !lastSheet.channel.isEmpty()) {
                        type = lastSheet.id;
                    }
                }
            }

            UIKeyframeSheet sheet = keyframeEditor.view.getGraph().getSheet(StringUtils.combinePaths(path, type));

            if (sheet != null && sheet.channel.isEmpty()) {
                type = "pose";
            }
            
            key = StringUtils.combinePaths(path, type);
        }

        UIReplaysEditorUtilsMixin.bbsbettertracks$invokePickProperty(keyframeEditor, cursor, bone, key, insertKeyframe);
        if (insertKeyframe) {
            UIReplaysEditorUtilsMixin.bbsbettertracks$selectBoneInEditor(keyframeEditor, bone);
        }
        
        ci.cancel();
    }

    private static void bbsbettertracks$selectBoneInEditor(UIKeyframeEditor keyframeEditor, String bone) {
        try {
            UIKeyframeFactory editor = keyframeEditor.editor;
            if (editor == null) {
                return;
            }
            if (!editor.getClass().getSimpleName().equals("UIPoseKeyframeFactory")) {
                return;
            }
            Field poseEditorField = editor.getClass().getField("poseEditor");
            Object poseEditor = poseEditorField.get(editor);
            Method selectBoneMethod = poseEditor.getClass().getMethod("selectBone", String.class);
            selectBoneMethod.invoke(poseEditor, bone);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void bbsbettertracks$invokePickProperty(UIKeyframeEditor keyframeEditor, ICursor cursor, String bone, String key, boolean insert) {
        try {
            Method m = UIReplaysEditorUtils.class.getDeclaredMethod("pickProperty", UIKeyframeEditor.class, ICursor.class, String.class, String.class, Boolean.TYPE);
            m.setAccessible(true);
            m.invoke(null, keyframeEditor, cursor, bone, key, insert);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static UIKeyframeSheet findSheetByBoneName(UIKeyframeEditor keyframeEditor, Form form, String boneName) {
        if (keyframeEditor == null || boneName == null || boneName.isEmpty()) {
            return null;
        }
        String formPath = FormUtils.getPath((Form)form);
        for (UIKeyframeSheet sheet : keyframeEditor.view.getGraph().getSheets()) {
            if (sheet.property == null || !FormUtils.getPath((Form)((Form)sheet.property.getParent())).equals(formPath) || !sheet.property.getId().startsWith("pose")) continue;
            String displayName = BetterTracksAccess.sheet(sheet).bbsbettertracks$getDisplayName();
            if (boneName.equals(displayName)) {
                return sheet;
            }
            String originalName = form.getTrackName(sheet.property.getId());
            int slash = originalName.lastIndexOf(47);
            String trackBoneName = slash == -1 ? originalName : originalName.substring(slash + 1);
            if (!boneName.equals(trackBoneName)) continue;
            return sheet;
        }
        return null;
    }
    
    private static boolean hasKeyframeNearTick(UIKeyframeSheet sheet, int tick, int threshold) {
        if (sheet == null || sheet.channel == null || sheet.channel.isEmpty()) {
            return false;
        }
        
        for (Object obj : sheet.channel.getKeyframes()) {
            Keyframe kf = (Keyframe) obj;
            float kfTick = kf.getTick();
            if (Math.abs(kfTick - tick) <= threshold) {
                return true;
            }
        }
        
        return false;
    }
}
