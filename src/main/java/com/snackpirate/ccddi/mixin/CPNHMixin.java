package com.snackpirate.ccddi.mixin;

import com.snackpirate.ccddi.client.DamageIndicator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.DamageTiltS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientPlayNetworkHandler.class)
public class CPNHMixin {
	@Inject(method = "onDamageTilt", at = @At("TAIL"))
	private void onDamageTilt(DamageTiltS2CPacket packet, CallbackInfo ci) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && MinecraftClient.getInstance().targetedEntity != null) {
			new DamageIndicator(player.getDamageTiltYaw() + MinecraftClient.getInstance().cameraEntity.getYaw());
		}
	}

}
