package com.pressurerisk.network;

import com.pressurerisk.utils.ModConstants;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModServerNetwork {

    public static final Identifier PRESSURE_EVENT_ALERT = new Identifier(ModConstants.MOD_ID,"pressure_event");

    public static final Identifier PRESSURE_XP = new Identifier(ModConstants.MOD_ID,"pressure_xp");


}
