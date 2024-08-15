package com.snackpirate.ccddi;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class Config implements ModMenuApi {
	public static boolean showIndicator = true;
	public static Color indColor = Color.RED;
	public static float indAlpha = 1f;
	public static int xOffset = 0;
	public static int yOffset = 0;
	public static int rotationDistance = 30;
	public static float scale = 1f;
	public static float spriteAngleoffset = 0f;
	public static float persistenceTime = 5f;
	public static IndicatorStyle style = IndicatorStyle.ANGULAR;
	public static Identifier customResource = Identifier.of("minecraft:textures/block/sand.png");
	public enum IndicatorStyle implements NameableEnum {
		ANGULAR("angular"),
		LINEAR("linear"),
		BRACE("brace"),
		CURVED("curved"),
		BLADE("blade"),
		ARROW("arrow"),
		SHARP_ANGULAR("sharp"),
		CUSTOM("custom");

		public final String resource;

		IndicatorStyle(String resource) {
			this.resource = resource;
		}

		@Override
		public Text getDisplayName() {
			return Text.literal(resource.substring(0,1).toUpperCase() + resource.substring(1));
		}
	}
	public static boolean shouldBeOffset() {
		return style == IndicatorStyle.BLADE || style == IndicatorStyle.ARROW ||style == IndicatorStyle.SHARP_ANGULAR;
	}
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> YetAnotherConfigLib.createBuilder()
				.title(Text.literal("Damage Indicator"))
				.category(ConfigCategory.createBuilder()
						.name(Text.literal("Damage Indicator Configuration"))
						.tooltip(Text.literal("Settings to configure your damage indicator."))
						.option(Option.<Boolean>createBuilder()
								.name(Text.literal("Enable Indicator"))
								.description(OptionDescription.of(Text.literal("Whether or not the indicator shows up at all.")))
								.binding(true, () -> showIndicator, newVal -> showIndicator = newVal)
								.controller(TickBoxControllerBuilder::create)
								.build())
						.group(OptionGroup.createBuilder()
								.name(Text.literal("Appearance"))
								.description(OptionDescription.of(Text.literal("Settings to configure the appearance of the indicator.")))
								.option(Option.<Color>createBuilder()
										.name(Text.literal("Color"))
										.description(OptionDescription.of(Text.literal("The color of the indicator. Set to #FFFFFF to use indicator textures of varying colors.")))
										.binding(Color.RED, () -> indColor, newVal -> indColor = newVal)
										.controller(ColorControllerBuilder::create)
										.build())
								.option(Option.<Float>createBuilder()
										.name(Text.literal("Opacity"))
										.description(OptionDescription.of(Text.literal("The opacity of the indicator.")))
										.binding(1f, () -> indAlpha, newVal -> indAlpha = newVal)
										.controller(opt -> FloatSliderControllerBuilder.create(opt)
												.range(0f,1f)
												.step(0.01f)
												.formatValue(value -> Text.literal(Math.round(value*100) + "%")))
										.build())
								.option(Option.<Float>createBuilder()
										.name(Text.literal("Longevity"))
										.description(OptionDescription.of(Text.literal("How long the indicator exists on the screen.")))
										.binding(5f, () -> persistenceTime, newVal -> persistenceTime = newVal)
										.controller(opt -> FloatSliderControllerBuilder.create(opt)
												.range(1f,30f)
												.step(0.5f)
												.formatValue(value -> Text.literal(value + " seconds")))
										.build())
								.option(Option.<IndicatorStyle>createBuilder()
										.name(Text.literal("Indicator Style"))
										.description(OptionDescription.of(Text.literal("The style of the indicator. To use Custom, make sure to change the resource string in the \"Custom Indicator Resource\" field.")))
										.controller(opt -> EnumControllerBuilder.create(opt).enumClass(IndicatorStyle.class))
										.binding(IndicatorStyle.ANGULAR, () -> style, newVal -> style = newVal)
										.build())
								.option(Option.<String>createBuilder()
										.name(Text.literal("Custom Indicator Resource"))
										.description(OptionDescription.of(Text.literal("The resource path of the texture that the custom indicator uses. Example String: \"minecraft:textures/block/sand.png\"")))
										.controller(StringControllerBuilder::create)
										.binding("minecraft:textures/block/sand.png", () -> customResource.toString(), newVal -> customResource = Identifier.of(newVal))
										.build())
								.build())
						.group(OptionGroup.createBuilder()
							.name(Text.literal("Positioning"))
							.description(OptionDescription.of(Text.literal("Settings to configure the position of the indicator.")))
							.option(Option.<Integer>createBuilder()
									.name(Text.literal("X-Offset"))
									.description(OptionDescription.of(Text.literal("Horizontally offsets the center around which the damage indicators rotate. By default, this is the crosshair.")))
									.binding(0, () -> xOffset, newVal -> xOffset = newVal)
									.controller(opt -> IntegerFieldControllerBuilder.create(opt)
											.range(Integer.MIN_VALUE, Integer.MAX_VALUE)
											.formatValue(value -> Text.literal(value + "px")))
									.build())
							.option(Option.<Integer>createBuilder()
									.name(Text.literal("Y-Offset"))
									.description(OptionDescription.of(Text.literal("Vertically offsets the center around which the damage indicators rotate. By default, this is the crosshair.")))
									.binding(0, () -> yOffset, newVal -> yOffset = newVal)
									.controller(opt -> IntegerFieldControllerBuilder.create(opt)
											.range(Integer.MIN_VALUE, Integer.MAX_VALUE)
											.formatValue(value -> Text.literal(value + "px")))
									.build())
							.option(Option.<Integer>createBuilder()
									.name(Text.literal("Rotation Distance"))
									.description(OptionDescription.of(Text.literal("The distance at which the indicator rotates around the center.")))
									.binding(30, () -> rotationDistance, newVal -> rotationDistance = newVal)
									.controller(opt -> IntegerFieldControllerBuilder.create(opt)
											.range(0, Integer.MAX_VALUE)
											.formatValue(value -> Text.literal(value + "px")))
									.build())
							.option(Option.<Float>createBuilder()
									.name(Text.literal("Sprite Rotation Offset"))
									.description(OptionDescription.of(Text.literal("The rotation of the sprite.")))
									.binding(0f, () -> spriteAngleoffset, newVal -> spriteAngleoffset = newVal)
									.controller(opt -> FloatSliderControllerBuilder.create(opt)
											.range(-180f,180f)
											.step(1f)
											.formatValue(value -> Text.literal(Math.round(value) + "°")))
									.build())
							.option(Option.<Float>createBuilder()
									.name(Text.literal("Scale"))
									.description(OptionDescription.of(Text.literal("The size scaling of the indicator. This automatically changes with GUI scale in vanilla options.")))
									.binding(1f, () -> scale, newVal -> scale = newVal)
									.controller(opt -> FloatSliderControllerBuilder.create(opt)
											.range(0.05f,10f)
											.step(0.01f)
											.formatValue(value -> Text.literal(Math.round(value*100) + "%")))
									.build())
								.build())
						.build())
				.build()
				.generateScreen(parent);
	}
}
