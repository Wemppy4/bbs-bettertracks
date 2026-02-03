package com.wemppy.bbsbettertracks.bettertracks.ui;

import java.lang.ref.WeakReference;
import java.util.IdentityHashMap;
import java.util.Map;

import mchorse.bbs_mod.settings.values.numeric.ValueBoolean;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIToggle;

public final class ToggleValueSync
{
    private static final Map<ValueBoolean, WeakReference<UIToggle>> TOGGLES = new IdentityHashMap<>();
    private static final Map<ValueBoolean, Boolean> HOOKED = new IdentityHashMap<>();

    private ToggleValueSync()
    {}

    public static void bind(ValueBoolean value, UIToggle toggle)
    {
        if (value == null || toggle == null)
        {
            return;
        }

        synchronized (ToggleValueSync.class)
        {
            TOGGLES.put(value, new WeakReference<>(toggle));

            if (!HOOKED.containsKey(value))
            {
                HOOKED.put(value, Boolean.TRUE);

                value.postCallback((changed, flag) ->
                {
                    UIToggle current = null;

                    synchronized (ToggleValueSync.class)
                    {
                        WeakReference<UIToggle> ref = TOGGLES.get(value);

                        current = ref == null ? null : ref.get();
                    }

                    if (current != null)
                    {
                        current.setValue(value.get());
                    }
                });
            }
        }

        toggle.setValue(value.get());
    }
}
