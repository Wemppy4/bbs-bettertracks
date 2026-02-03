package com.wemppy.bbsbettertracks;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "bbs-bettertracks";
    public static final Logger LOGGER = LoggerFactory.getLogger("bbs-bettertracks");

    @Override
    public void onInitialize() {
        LOGGER.info("BBS Better Tracks loaded");
    }
}
