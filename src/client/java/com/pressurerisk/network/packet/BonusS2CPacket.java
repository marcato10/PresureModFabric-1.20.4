package com.pressurerisk.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class BonusS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworking,
                               PacketByteBuf buf, PacketSender responseSender){

        client.inGameHud.setOverlayMessage(Text.literal("XP: "+buf.readInt()),false);

    }
}
