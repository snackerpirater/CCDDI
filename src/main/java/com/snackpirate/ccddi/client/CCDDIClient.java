package com.snackpirate.ccddi.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.enums.VaultState;
import net.minecraft.client.render.block.BlockModelRenderer;

public class CCDDIClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(DamageIndicatorOverlay.instance::render);
	}
	/*
	TODO: Make scale option work
	TODO: More customization?
		- Disable camera tracking option
	TODO: Port to Neo/older versions of MC?
	TODO: Indicators disappear after death
	 */
}
