package com.snackpirate.ccddi.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;


public class CCDDIClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(DamageIndicatorOverlay.instance::render);
		//test stuff for another mod in the future maybe
//		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
//			if (client.targetedEntity != null && client.world != null && client.options.attackKey.isPressed()) {
////				LogUtils.getLogger().info("cooldown: {}", client.player.getAttackCooldownProgress(0f));
//				if (client.player.getAttackCooldownProgress(0f) == 1f) {
//					client.player.swingHand(Hand.MAIN_HAND, false);
//					client.attackCooldown = 0;
//					client.interactionManager.attackEntity(client.player, ((EntityHitResult)client.crosshairTarget).getEntity());
//
//				}
//			}
//		});
	}
	/*
	TODO: Make scale option work
	TODO: More customization?
		- Disable camera tracking option
	TODO: Port to Neo/older versions of MC?
	TODO: Indicators disappear after death
	 */
}
