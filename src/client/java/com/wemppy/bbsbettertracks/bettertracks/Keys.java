/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.l10n.L10n
 *  mchorse.bbs_mod.ui.utils.keys.KeyCombo
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.wemppy.bbsbettertracks.bettertracks;

import mchorse.bbs_mod.l10n.L10n;
import mchorse.bbs_mod.ui.utils.keys.KeyCombo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class Keys {
    public static final KeyCombo TOGGLE_INSERT_KEYFRAME = new KeyCombo("toggle_insert_keyframe", L10n.lang((String)"bbs.config.appearance.insert_keyframe_on_bone_click"), new int[]{74}).categoryKey("keyframes");
}

