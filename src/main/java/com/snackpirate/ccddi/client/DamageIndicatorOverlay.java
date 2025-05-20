package com.snackpirate.ccddi.client;

import com.snackpirate.ccddi.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathConstants;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;

import java.util.Iterator;
import java.util.Objects;

public class DamageIndicatorOverlay implements LayeredDrawer.Layer {
  public static final DamageIndicatorOverlay instance = new DamageIndicatorOverlay();

  private static Vector2f rotationTransform(Vector2f point, float rad) {
    float x = point.x;
    float y = point.y;
    float cos = MathHelper.cos(rad);
    float sin = MathHelper.sin(rad);
    return new Vector2f(((x * cos) - (y * sin)), ((y * cos) + (x * sin)));
  }

  private static Identifier getTexture() {
    if (Objects.requireNonNull(Config.style) == Config.IndicatorStyle.CUSTOM) {
      return Config.customResource;
    }
    return Identifier.of("ccddi", "textures/gui/" + Config.style.resource + ".png");
  }

  public void render(DrawContext context, RenderTickCounter tickCounter) {
    if (MinecraftClient.getInstance().options.hudHidden || (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.isSpectator()) || !Config.showIndicator) {
      return;
    }

    for (Iterator<DamageIndicator> iter = DamageIndicator.indicators.iterator(); iter.hasNext(); ) {
      DamageIndicator d = iter.next();
      float ticks = d.ticks(tickCounter);
      drawIndicator(context, MathHelper.wrapDegrees(d.getYaw() - MinecraftClient.getInstance().cameraEntity.getYaw()) + 90, ticks / (Config.persistenceTime * 20) * Config.indAlpha);
      if (ticks < 0) iter.remove();
    }
  }

  private void drawIndicator(DrawContext context, float deg, float alpha) {
    var matrices = context.getMatrices().peek();
    var vertices = context.vertexConsumers.getBuffer(RenderLayer.getGuiTexturedOverlay(getTexture()));

    var scale = MinecraftClient.getInstance().options.getGuiScale().getValue() * Config.scale;
    var center = new Vector2f(
        context.getScaledWindowWidth() * 0.5F,
        context.getScaledWindowHeight() * 0.5F
    );

    var rad = (Config.spriteAngleoffset + (Config.shouldBeOffset() ? 45 : 0)) * MathConstants.RADIANS_PER_DEGREE;

    var p1 = rotationTransform(new Vector2f(-5, -5).mul(scale), rad);
    var p2 = rotationTransform(new Vector2f(-5, 5).mul(scale), rad);
    var p3 = rotationTransform(new Vector2f(5, 5).mul(scale), rad);
    var p4 = rotationTransform(new Vector2f(5, -5).mul(scale), rad);

    rad = deg * MathConstants.RADIANS_PER_DEGREE;

    p1 = rotationTransform(p1.add(0.0F, Config.rotationDistance), rad).add(center);
    p2 = rotationTransform(p2.add(0.0F, Config.rotationDistance), rad).add(center);
    p3 = rotationTransform(p3.add(0.0F, Config.rotationDistance), rad).add(center);
    p4 = rotationTransform(p4.add(0.0F, Config.rotationDistance), rad).add(center);

    var color = new float[]{
        Config.indColor.getRed() / 255.0F,
        Config.indColor.getGreen() / 255.0F,
        Config.indColor.getBlue() / 255.0F,
    };

    vertices.vertex(matrices, p1.x, p1.y, 0).texture(0, 0).color(color[0], color[1], color[2], alpha);
    vertices.vertex(matrices, p2.x, p2.y, 0).texture(0, 1).color(color[0], color[1], color[2], alpha);
    vertices.vertex(matrices, p3.x, p3.y, 0).texture(1, 1).color(color[0], color[1], color[2], alpha);
    vertices.vertex(matrices, p4.x, p4.y, 0).texture(1, 0).color(color[0], color[1], color[2], alpha);
  }
}
