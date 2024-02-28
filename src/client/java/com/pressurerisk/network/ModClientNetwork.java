package com.pressurerisk.network;

import com.pressurerisk.network.packet.BonusS2CPacket;
import com.pressurerisk.network.packet.PressureS2CPacket;
import com.pressurerisk.utils.ModConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModClientNetwork {
    public static final Identifier PRESSURE_EVENT_ALERT = new Identifier(ModConstants.MOD_ID,"pressure_event");

    public static final Identifier PRESSURE_XP = new Identifier(ModConstants.MOD_ID,"pressure_xp");

    public static void registerS2CPacket(){
        ClientPlayNetworking.registerGlobalReceiver(PRESSURE_EVENT_ALERT, PressureS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(PRESSURE_XP, BonusS2CPacket::receive);
    }

}
