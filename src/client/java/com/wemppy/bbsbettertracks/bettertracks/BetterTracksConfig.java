/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.BBSSettings
 *  mchorse.bbs_mod.l10n.keys.IKey
 *  mchorse.bbs_mod.settings.SettingsBuilder
 *  mchorse.bbs_mod.settings.values.numeric.ValueBoolean
 *  mchorse.bbs_mod.settings.values.numeric.ValueInt
 */
package com.wemppy.bbsbettertracks.bettertracks;

import java.lang.reflect.Field;
import mchorse.bbs_mod.BBSSettings;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.settings.SettingsBuilder;
import mchorse.bbs_mod.settings.values.numeric.ValueBoolean;
import mchorse.bbs_mod.settings.values.numeric.ValueInt;

public class BetterTracksConfig {
    private static ValueBoolean autoSelectBoneByTrackNameFallback;
    private static ValueBoolean autoSelectTrackByBoneNameFallback;
    private static ValueBoolean insertKeyframeOnBoneClickFallback;
    private static ValueBoolean insertKeyframeOnOverlaysFallback;
    private static ValueBoolean localTransformByDefaultFallback;
    private static ValueBoolean realtimeKeyframeRecordingFallback;
    private static ValueInt defaultKeyframeShapeFallback;
    private static ValueInt defaultKeyframeColorFallback;

    public static ValueBoolean autoSelectBoneByTrackName() {
        ValueBoolean existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "autoSelectBoneByTrackName", ValueBoolean.class);
        return existing != null ? existing : autoSelectBoneByTrackNameFallback;
    }

    public static ValueBoolean autoSelectTrackByBoneName() {
        ValueBoolean existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "autoSelectTrackByBoneName", ValueBoolean.class);
        return existing != null ? existing : autoSelectTrackByBoneNameFallback;
    }

    public static ValueBoolean insertKeyframeOnBoneClick() {
        ValueBoolean existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "insertKeyframeOnBoneClick", ValueBoolean.class);
        return existing != null ? existing : insertKeyframeOnBoneClickFallback;
    }

    public static ValueBoolean insertKeyframeOnOverlays() {
        ValueBoolean existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "insertKeyframeOnOverlays", ValueBoolean.class);
        return existing != null ? existing : insertKeyframeOnOverlaysFallback;
    }

    public static ValueInt defaultKeyframeShape() {
        ValueInt existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "defaultKeyframeShape", ValueInt.class);
        return existing != null ? existing : defaultKeyframeShapeFallback;
    }

    public static ValueInt defaultKeyframeColor() {
        ValueInt existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "defaultKeyframeColor", ValueInt.class);
        return existing != null ? existing : defaultKeyframeColorFallback;
    }

    public static ValueBoolean localTransformByDefault() {
        ValueBoolean existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "localTransformByDefault", ValueBoolean.class);
        return existing != null ? existing : localTransformByDefaultFallback;
    }

    public static ValueBoolean realtimeKeyframeRecording() {
        ValueBoolean existing = BetterTracksConfig.reflectStatic(BBSSettings.class, "realtimeKeyframeRecording", ValueBoolean.class);
        return existing != null ? existing : realtimeKeyframeRecordingFallback;
    }

    public static void ensureRegistered(SettingsBuilder builder) {
        if (BetterTracksConfig.reflectStatic(BBSSettings.class, "autoSelectBoneByTrackName", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "autoSelectTrackByBoneName", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "insertKeyframeOnBoneClick", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "insertKeyframeOnOverlays", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "localTransformByDefault", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "realtimeKeyframeRecording", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "defaultKeyframeShape", Object.class) != null && BetterTracksConfig.reflectStatic(BBSSettings.class, "defaultKeyframeColor", Object.class) != null) {
            return;
        }
        
        builder.category("bettertracks");
        
        if (autoSelectBoneByTrackNameFallback == null) {
            autoSelectBoneByTrackNameFallback = builder.getBoolean("auto_select_bone_by_track_name", true);
        }
        if (autoSelectTrackByBoneNameFallback == null) {
            autoSelectTrackByBoneNameFallback = builder.getBoolean("auto_select_track_by_bone_name", false);
        }
        if (insertKeyframeOnBoneClickFallback == null) {
            insertKeyframeOnBoneClickFallback = builder.getBoolean("insert_keyframe_on_bone_click", false);
        }
        if (insertKeyframeOnOverlaysFallback == null) {
            insertKeyframeOnOverlaysFallback = builder.getBoolean("insert_keyframe_on_overlays", true);
        }
        if (localTransformByDefaultFallback == null) {
            localTransformByDefaultFallback = builder.getBoolean("local_transform_by_default", false);
        }
        if (realtimeKeyframeRecordingFallback == null) {
            realtimeKeyframeRecordingFallback = builder.getBoolean("realtime_keyframe_recording", false);
        }
        if (defaultKeyframeShapeFallback == null) {
            defaultKeyframeShapeFallback = builder.getInt("default_keyframe_shape", 0, 0, 7).modes(new IKey[]{IKey.raw((String)"bbs.ui.keyframes.shapes.square"), IKey.raw((String)"bbs.ui.keyframes.shapes.circle"), IKey.raw((String)"bbs.ui.keyframes.shapes.triangle"), IKey.raw((String)"bbs.ui.keyframes.shapes.diamond"), IKey.raw((String)"bbs.ui.keyframes.shapes.tri_star"), IKey.raw((String)"bbs.ui.keyframes.shapes.four_star"), IKey.raw((String)"bbs.ui.keyframes.shapes.five_star"), IKey.raw((String)"bbs.ui.keyframes.shapes.six_star")});
        }
        if (defaultKeyframeColorFallback == null) {
            defaultKeyframeColorFallback = builder.getInt("default_keyframe_color", 0).colorAlpha();
        }
    }

    private static <T> T reflectStatic(Class<?> clazz, String fieldName, Class<T> expected) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            Object value = f.get(null);
            if (value == null) {
                return null;
            }
            return expected.isInstance(value) ? (T)expected.cast(value) : null;
        }
        catch (Throwable t) {
            return null;
        }
    }
}

