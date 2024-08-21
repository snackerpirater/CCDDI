package com.snackpirate.ccddi.client;

import com.snackpirate.ccddi.Config;
import net.minecraft.block.vault.VaultClientData;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.block.entity.VaultBlockEntityRenderer;

import java.util.ArrayList;

public class DamageIndicator {
	public static ArrayList<DamageIndicator> indicators = new ArrayList<>();
	private final float yaw;
	private float ticks;
	public DamageIndicator(float yaw1) {
		this.yaw = yaw1;
		this.ticks = Config.persistenceTime*20;
		indicators.add(this);
	}

	public float getYaw() {
		return yaw;
	}
	public float ticks(RenderTickCounter counter) {
		if (ticks < 0) indicators.remove(this);
		return ticks-=counter.getLastFrameDuration();
	}
}
