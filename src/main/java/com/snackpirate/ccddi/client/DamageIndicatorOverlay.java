package com.snackpirate.ccddi.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.snackpirate.ccddi.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathConstants;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.Iterator;
import java.util.Objects;

public class DamageIndicatorOverlay implements LayeredDrawer.Layer {
	public static final DamageIndicatorOverlay instance = new DamageIndicatorOverlay();
	public void render(DrawContext context, RenderTickCounter tickCounter) {
//		MinecraftClient.getInstance().player.sendMessage(Text.literal("tick counter: " + tickCounter.getLastDuration()), true);
		if (MinecraftClient.getInstance().options.hudHidden || (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.isSpectator()) || !Config.showIndicator) {
			return;
		}
		for (Iterator<DamageIndicator> iter = DamageIndicator.indicators.iterator(); iter.hasNext();) {
			DamageIndicator d = iter.next();
			float ticks = d.ticks(tickCounter);
			drawIndicator(context, MathHelper.wrapDegrees(d.getYaw() - MinecraftClient.getInstance().cameraEntity.getYaw()) + 90, ticks /(Config.persistenceTime*20)*Config.indAlpha);

			if (ticks < 0) iter.remove();
		}
	}
	private void drawIndicator(DrawContext context, float deg, float alpha) {
		Identifier texture = getTexture();
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		Vector2f center = new Vector2f((float) context.getScaledWindowWidth() / 2, (float) context.getScaledWindowHeight() / 2);
		int scale = MinecraftClient.getInstance().options.getGuiScale().getValue();
		Vector2f p1 = new Vector2f(-5,-5).mul(scale);
		Vector2f p2 = new Vector2f(-5,5).mul(scale);
		Vector2f p3 = new Vector2f(5, 5).mul(scale);
		Vector2f p4 = new Vector2f(5, -5).mul(scale);
		float rad = (Config.spriteAngleoffset + (Config.shouldBeOffset() ? 45 : 0)) * MathConstants.RADIANS_PER_DEGREE;
		p1 = rotationTransform(p1, rad);
		p2 = rotationTransform(p2, rad);
		p3 = rotationTransform(p3, rad);
		p4 = rotationTransform(p4, rad);
		rad = deg * MathConstants.RADIANS_PER_DEGREE;
		p1.add(0f, Config.rotationDistance);
		p2.add(0f, Config.rotationDistance);
		p3.add(0f, Config.rotationDistance);
		p4.add(0f, Config.rotationDistance);
		p1 = rotationTransform(p1, rad);
		p2 = rotationTransform(p2, rad);
		p3 = rotationTransform(p3, rad);
		p4 = rotationTransform(p4, rad);
		p1.add(center);
		p2.add(center);
		p3.add(center);
		p4.add(center);
		int u1 = 0, v1 = 0, u2 = 1, v2 = 1;
		float red = Config.indColor.getRed()/255f;
		float green = Config.indColor.getGreen()/255f;
		float blue = Config.indColor.getBlue()/255f;
		bufferBuilder.vertex(matrix4f, p1.x, p1.y, 0).texture(u1, v1).color(red, green, blue, alpha);
		bufferBuilder.vertex(matrix4f, p2.x, p2.y, 0).texture(u1, v2).color(red, green, blue, alpha);
		bufferBuilder.vertex(matrix4f, p3.x, p3.y, 0).texture(u2, v2).color(red, green, blue, alpha);
		bufferBuilder.vertex(matrix4f, p4.x, p4.y, 0).texture(u2, v1).color(red, green, blue, alpha);
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
		RenderSystem.disableBlend();
	}

	private static Vector2f rotationTransform(Vector2f point, float rad) {
		float x = point.x;
		float y = point.y;
		float cos = MathHelper.cos(rad);
		float sin = MathHelper.sin(rad);
		return new Vector2f(((x*cos) - (y*sin)), ((y*cos) + (x*sin)));
	}
	private static Identifier getTexture() {
		if (Objects.requireNonNull(Config.style) == Config.IndicatorStyle.CUSTOM) {
			return Config.customResource;
		}
		return Identifier.of("ccddi", "textures/gui/" + Config.style.resource + ".png");
	}
}
