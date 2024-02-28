package com.pressurerisk.events;

import com.pressurerisk.PressureRisk;
import com.pressurerisk.utils.ModConstants;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ModEvents {
    public static final ServerPressureNightCallback serverEvents = new ServerPressureNightCallback();

    public static void registerEvents(){
        PressureRisk.LOGGER.info("Registering Events "+ ModConstants.MOD_ID);
        ServerPlayConnectionEvents.JOIN.register(serverEvents);
        ServerLivingEntityEvents.AFTER_DEATH.register(serverEvents);
        ServerTickEvents.END_WORLD_TICK.register(serverEvents);
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(serverEvents);
    }
}
