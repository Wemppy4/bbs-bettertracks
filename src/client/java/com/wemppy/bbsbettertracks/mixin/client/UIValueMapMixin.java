/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mchorse.bbs_mod.l10n.keys.IKey
 *  mchorse.bbs_mod.settings.ui.UIValueFactory
 *  mchorse.bbs_mod.settings.ui.UIValueMap
 *  mchorse.bbs_mod.settings.values.base.BaseValue
 *  mchorse.bbs_mod.settings.values.numeric.ValueInt
 *  mchorse.bbs_mod.settings.values.numeric.ValueInt$Subtype
 *  mchorse.bbs_mod.ui.UIKeys
 *  mchorse.bbs_mod.ui.framework.elements.UIElement
 *  mchorse.bbs_mod.ui.framework.elements.buttons.UICirculate
 *  mchorse.bbs_mod.ui.framework.elements.buttons.UIIcon
 *  mchorse.bbs_mod.ui.framework.elements.input.UIColor
 *  mchorse.bbs_mod.ui.framework.elements.input.UITrackpad
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.shapes.IKeyframeShapeRenderer
 *  mchorse.bbs_mod.ui.framework.elements.input.keyframes.shapes.KeyframeShapeRenderers
 *  mchorse.bbs_mod.ui.utils.icons.Icons
 *  mchorse.bbs_mod.utils.keyframes.KeyframeShape
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.wemppy.bbsbettertracks.mixin.client;

import com.wemppy.bbsbettertracks.bettertracks.BetterTracksConfig;
import java.util.Arrays;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.settings.ui.UIValueFactory;
import mchorse.bbs_mod.settings.ui.UIValueMap;
import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.settings.values.numeric.ValueInt;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.buttons.UICirculate;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIIcon;
import mchorse.bbs_mod.ui.framework.elements.input.UIColor;
import mchorse.bbs_mod.ui.framework.elements.input.UITrackpad;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.shapes.IKeyframeShapeRenderer;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.shapes.KeyframeShapeRenderers;
import mchorse.bbs_mod.ui.utils.icons.Icons;
import mchorse.bbs_mod.utils.keyframes.KeyframeShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={UIValueMap.class}, remap=false)
public class UIValueMapMixin {
    @Inject(method={"<clinit>"}, at={@At(value="TAIL")}, remap=false)
    private static void bbsbettertracks$overrideValueIntFactory(CallbackInfo ci) {
        UIValueMap.register(ValueInt.class, (value, ui) -> {
            if (value.getSubtype() == ValueInt.Subtype.COLOR || value.getSubtype() == ValueInt.Subtype.COLOR_ALPHA) {
                UIColor color = UIValueFactory.colorUI((ValueInt)value, null);
                color.w(90);
                return Arrays.asList(UIValueFactory.column((UIElement)color, (BaseValue)value));
            }
            if (value.getSubtype() == ValueInt.Subtype.MODES) {
                ValueInt defaultShape = BetterTracksConfig.defaultKeyframeShape();
                if (defaultShape != null && value == defaultShape) {
                    UIIcon[] buttonRef = new UIIcon[1];
                    buttonRef[0] = new UIIcon(Icons.SHAPES, b -> {
                        KeyframeShape currentShape = KeyframeShape.values()[(Integer)value.get()];
                        b.getContext().replaceContextMenu(menu -> {
                            for (KeyframeShape shape : KeyframeShape.values()) {
                                IKeyframeShapeRenderer shapeRenderer = (IKeyframeShapeRenderer)KeyframeShapeRenderers.SHAPES.get(shape);
                                menu.action(shapeRenderer.getIcon(), shapeRenderer.getLabel(), shape == currentShape, () -> {
                                    value.set(shape.ordinal());
                                    buttonRef[0].both(shapeRenderer.getIcon());
                                });
                            }
                        });
                    });
                    KeyframeShape currentShape = KeyframeShape.values()[(Integer)value.get()];
                    IKeyframeShapeRenderer shapeRenderer = (IKeyframeShapeRenderer)KeyframeShapeRenderers.SHAPES.get(currentShape);
                    buttonRef[0].both(shapeRenderer.getIcon());
                    buttonRef[0].tooltip(UIKeys.KEYFRAMES_CHANGE_SHAPE);
                    buttonRef[0].w(90);
                    return Arrays.asList(UIValueFactory.column((UIElement)buttonRef[0], (BaseValue)value));
                }
                UICirculate button = new UICirculate(null);
                for (IKey key : value.getLabels()) {
                    button.addLabel(key);
                }
                button.callback = b -> value.set(button.getValue());
                button.setValue(((Integer)value.get()).intValue());
                button.w(90);
                return Arrays.asList(UIValueFactory.column((UIElement)button, (BaseValue)value));
            }
            UITrackpad trackpad = UIValueFactory.intUI((ValueInt)value, null);
            trackpad.w(90);
            return Arrays.asList(UIValueFactory.column((UIElement)trackpad, (BaseValue)value));
        });
    }
}

