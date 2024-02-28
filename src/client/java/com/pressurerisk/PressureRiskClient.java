package com.pressurerisk;

import com.pressurerisk.network.ModClientNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PressureRiskClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModClientNetwork.registerS2CPacket();
	}
}