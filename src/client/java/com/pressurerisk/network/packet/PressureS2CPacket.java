package com.pressurerisk.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class PressureS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworking,
                               PacketByteBuf buf, PacketSender responseSender){
        if(buf.readString().equals("RUNNING")){
            client.inGameHud.setOverlayMessage(Text.literal("And the dark rises on. Good luck!"),false);
        }
        else{
            client.inGameHud.setOverlayMessage(Text.literal("And the dark rest off."),false);
        }

    }
}
