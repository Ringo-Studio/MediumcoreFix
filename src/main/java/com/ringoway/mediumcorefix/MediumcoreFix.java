package com.ringoway.mediumcorefix;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mediumcore_fix")
public class MediumcoreFix {
    public static final Logger LOGGER = LogManager.getLogger();

    public MediumcoreFix() {
        LOGGER.info("Mediumcore Fix by Ringoway loaded - fixing migration bug!");
    }
}
