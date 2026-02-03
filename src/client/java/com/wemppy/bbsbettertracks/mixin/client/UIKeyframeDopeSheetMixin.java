/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.data.DataStorageUtils
 *  mchorse.bbs_mod.data.types.BaseType
 *  mchorse.bbs_mod.data.types.MapType
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.api.BetterTracksDopeSheet;
import com.wemppy.bbsbettertracks.bettertracks.util.BetterTracksAccess;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mchorse.bbs_mod.data.DataStorageUtils;
import mchorse.bbs_mod.data.types.BaseType;
import mchorse.bbs_mod.data.types.MapType;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframeSheet;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.graphs.UIKeyframeDopeSheet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIKeyframeDopeSheet.class}, remap=false)
public class UIKeyframeDopeSheetMixin
implements BetterTracksDopeSheet {
    @Unique
    private final Map<String, String> bbsbettertracks$customTrackNames = new HashMap<String, String>();
    @Unique
    private final Map<String, Integer> bbsbettertracks$customTrackColors = new HashMap<String, Integer>();

    @Inject(method={"<init>"}, at={@At(value="TAIL")}, remap=false)
    private void bbsbettertracks$init(CallbackInfo ci) {
        this.bbsbettertracks$load();
    }

    @Inject(method={"removeAllSheets"}, at={@At(value="HEAD")}, remap=false)
    private void bbsbettertracks$beforeRemoveAllSheets(CallbackInfo ci) {
        UIKeyframeDopeSheet self = (UIKeyframeDopeSheet)(Object)this;
        this.bbsbettertracks$onRemoveAllSheets(self.getSheets());
    }

    @Inject(method={"addSheet"}, at={@At(value="HEAD")}, remap=false)
    private void bbsbettertracks$beforeAddSheet(UIKeyframeSheet sheet, CallbackInfo ci) {
        this.bbsbettertracks$onAddSheet(sheet);
    }

    @Override
    public void bbsbettertracks$onRemoveAllSheets(List<UIKeyframeSheet> sheets) {
        for (UIKeyframeSheet sheet : sheets) {
            Integer color;
            String name = BetterTracksAccess.sheet(sheet).bbsbettertracks$getCustomName();
            if (name != null && !name.isEmpty()) {
                this.bbsbettertracks$customTrackNames.put(sheet.id, name);
            }
            if ((color = BetterTracksAccess.sheet(sheet).bbsbettertracks$getCustomColor()) == null) continue;
            this.bbsbettertracks$customTrackColors.put(sheet.id, color);
        }
    }

    @Override
    public void bbsbettertracks$onAddSheet(UIKeyframeSheet sheet) {
        if (this.bbsbettertracks$customTrackNames.containsKey(sheet.id)) {
            BetterTracksAccess.sheet(sheet).bbsbettertracks$setCustomName(this.bbsbettertracks$customTrackNames.get(sheet.id));
        }
        if (this.bbsbettertracks$customTrackColors.containsKey(sheet.id)) {
            BetterTracksAccess.sheet(sheet).bbsbettertracks$setCustomColor(this.bbsbettertracks$customTrackColors.get(sheet.id));
        }
    }

    @Override
    public void bbsbettertracks$setCustomTrackName(String trackId, String customName) {
        if (customName != null && !customName.isEmpty()) {
            this.bbsbettertracks$customTrackNames.put(trackId, customName);
        } else {
            this.bbsbettertracks$customTrackNames.remove(trackId);
        }
        this.bbsbettertracks$save();
    }

    @Override
    public void bbsbettertracks$setCustomTrackColor(String trackId, Integer customColor) {
        if (customColor != null) {
            this.bbsbettertracks$customTrackColors.put(trackId, customColor);
        } else {
            this.bbsbettertracks$customTrackColors.remove(trackId);
        }
        this.bbsbettertracks$save();
    }

    @Override
    public void bbsbettertracks$resetAllCustomTrackData() {
        this.bbsbettertracks$customTrackNames.clear();
        this.bbsbettertracks$customTrackColors.clear();
        this.bbsbettertracks$save();
        UIKeyframeDopeSheet self = (UIKeyframeDopeSheet)(Object)this;
        for (UIKeyframeSheet sheet : self.getSheets()) {
            BetterTracksAccess.sheet(sheet).bbsbettertracks$setCustomName(null);
            BetterTracksAccess.sheet(sheet).bbsbettertracks$setCustomColor(null);
        }
    }

    @Override
    public void bbsbettertracks$copyCustomTrackData(UIKeyframeDopeSheet source) {
        if (!(source instanceof BetterTracksDopeSheet)) {
            return;
        }
        BetterTracksDopeSheet duck = (BetterTracksDopeSheet)source;
        UIKeyframeDopeSheetMixin src = (UIKeyframeDopeSheetMixin)(Object)source;
        this.bbsbettertracks$customTrackNames.clear();
        this.bbsbettertracks$customTrackNames.putAll(src.bbsbettertracks$customTrackNames);
        this.bbsbettertracks$customTrackColors.clear();
        this.bbsbettertracks$customTrackColors.putAll(src.bbsbettertracks$customTrackColors);
        UIKeyframeDopeSheet self = (UIKeyframeDopeSheet)(Object)this;
        for (UIKeyframeSheet sheet : self.getSheets()) {
            this.bbsbettertracks$onAddSheet(sheet);
        }
    }

    @Unique
    private void bbsbettertracks$load() {
        try {
            File configDir = new File("config/bbs");
            File file = new File(configDir, "custom_track_names.json");
            if (!file.exists()) {
                return;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            MapType root = (MapType)DataStorageUtils.readFromBytes((byte[])bytes);
            this.bbsbettertracks$customTrackNames.clear();
            this.bbsbettertracks$customTrackColors.clear();
            if (root.has("names")) {
                MapType names = root.getMap("names");
                for (String key : names.keys()) {
                    this.bbsbettertracks$customTrackNames.put(key, names.getString(key));
                }
            } else {
                for (String key : root.keys()) {
                    this.bbsbettertracks$customTrackNames.put(key, root.getString(key));
                }
            }
            if (root.has("colors")) {
                MapType colors = root.getMap("colors");
                for (String key : colors.keys()) {
                    this.bbsbettertracks$customTrackColors.put(key, colors.getInt(key));
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Unique
    private void bbsbettertracks$save() {
        try {
            File configDir = new File("config/bbs");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            File file = new File(configDir, "custom_track_names.json");
            MapType names = new MapType();
            for (Map.Entry<String, String> entry : this.bbsbettertracks$customTrackNames.entrySet()) {
                names.putString(entry.getKey(), entry.getValue());
            }
            MapType colors = new MapType();
            for (Map.Entry<String, Integer> entry : this.bbsbettertracks$customTrackColors.entrySet()) {
                colors.putInt(entry.getKey(), entry.getValue().intValue());
            }
            MapType mapType = new MapType();
            mapType.put("names", (BaseType)names);
            mapType.put("colors", (BaseType)colors);
            byte[] byArray = DataStorageUtils.writeToBytes((BaseType)mapType);
            Files.write(file.toPath(), byArray, new OpenOption[0]);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

