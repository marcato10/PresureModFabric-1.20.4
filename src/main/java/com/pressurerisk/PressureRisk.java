package com.pressurerisk;

import com.pressurerisk.blocks.ModBlocks;
import com.pressurerisk.core.NightPressureManager;
import com.pressurerisk.events.ModEvents;
import com.pressurerisk.events.ServerPressureNightCallback;
import com.pressurerisk.utils.ModConstants;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PressureRisk implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModConstants.MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.blocksToGame();
		ModEvents.registerEvents();

		LOGGER.info("Hello "+LOGGER.getName());
	}
}