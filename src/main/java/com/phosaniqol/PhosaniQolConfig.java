package com.phosaniqol;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.FontType;
import net.runelite.client.config.Range;

@ConfigGroup(PhosaniQolConfig.CONFIG_GROUP)
public interface PhosaniQolConfig extends Config
{
	String CONFIG_GROUP = "PhosaniQol";

	@ConfigSection(
		position = 0,
		name = "Totem Settings",
		description = "Settings related to the totems",
		closedByDefault = true
	)
	String totemSettings = "totemSettings";

	@ConfigItem(
		position = 0,
		keyName = "highlightChargedTotems",
		name = "Highlight Charged",
		description = "Sets the highlight style for totems that are fully charged",
		section = totemSettings
	)
	default HighlightStyle highlightChargedTotems()
	{
		return HighlightStyle.TILE;
	}

	@Alpha
	@ConfigItem(
		position = 1,
		keyName = "chargedBorderColor",
		name = " - Border Color",
		description = "Sets the border color for charged totems",
		section = totemSettings
	)
	default Color chargedBorderColor()
	{
		return new Color(0, 225, 255);
	}

	@Alpha
	@ConfigItem(
		position = 2,
		keyName = "chargedFillColor",
		name = " - Fill Color",
		description = "Sets the fill color for charged totems",
		section = totemSettings
	)
	default Color chargedFillColor()
	{
		return new Color(0, 225, 255, 30);
	}

	@ConfigItem(
		position = 3,
		keyName = "chargedBorderWidth",
		name = " - Border Width",
		description = "Sets the width of the lines for charged totems",
		section = totemSettings
	)
	default double chargedBorderWidth()
	{
		return 1.0;
	}

	@ConfigItem(
		position = 4,
		keyName = "totemChargeOverlay",
		name = "Charge Overlay",
		description = "Draws an overlay over totems showing their current charge",
		section = totemSettings
	)
	default OverlayType totemChargeOverlay()
	{
		return OverlayType.NONE;
	}

	@Alpha
	@ConfigItem(
		position = 5,
		keyName = "totemChargeOverlayColor",
		name = " - Color",
		description = "Sets the color to use for drawing totem charge overlays",
		section = totemSettings
	)
	default Color totemChargeOverlayColor()
	{
		return new Color(255, 255, 0);
	}

	@ConfigItem(
		position = 6,
		keyName = "totemChargeOverlayOffset",
		name = " - Z-Offset",
		description = "Sets the z-offset for drawing totem charge overlays",
		section = totemSettings
	)
	default int totemChargeOverlayOffset()
	{
		return 0;
	}

	@ConfigItem(
		position = 7,
		keyName = "totemChargeOverlayFont",
		name = " - Font",
		description = "Sets the font to use for drawing totem charge overlays",
		section = totemSettings
	)
	default FontType totemChargeOverlayFont()
	{
		return FontType.REGULAR;
	}

	@Range(min = 0, max = 5)
	@ConfigItem(
		position = 8,
		keyName = "totemChargeOverlayOutline",
		name = " - Outline",
		description = "Sets the width of the outline around totem charge overlay",
		section = totemSettings
	)
	default int totemChargeOverlayOutline()
	{
		return 0;
	}

	@ConfigItem(
		position = 9,
		keyName = "totemHud",
		name = "Totem HUD",
		description = "Sets the display style of the totem HUD",
		section = totemSettings
	)
	default TotemHudStyle totemHud()
	{
		return TotemHudStyle.DEFAULT;
	}

	@Alpha
	@ConfigItem(
		position = 10,
		keyName = "totemHudTextColor",
		name = " - Text Color",
		description = "Sets the text color of the totem HUD",
		section = totemSettings
	)
	default Color totemHudTextColor()
	{
		return new Color(230, 150, 30);
	}

	@Alpha
	@ConfigItem(
		position = 11,
		keyName = "totemHudBarForeground",
		name = " - Bar Foreground",
		description = "Sets the bar foreground color of the totem HUD",
		section = totemSettings
	)
	default Color totemHudBarForeground()
	{
		return new Color(255, 255, 0);
	}

	@Alpha
	@ConfigItem(
		position = 12,
		keyName = "totemHudBarBackground",
		name = " - Bar Background",
		description = "Sets the bar background color of the totem HUD",
		section = totemSettings
	)
	default Color totemHudBarBackground()
	{
		return new Color(45, 45, 0);
	}

	@Alpha
	@ConfigItem(
		position = 13,
		keyName = "totemHudBarFull",
		name = " - Bar Full",
		description = "Sets the bar filled color of the totem HUD",
		section = totemSettings
	)
	default Color totemHudBarFull()
	{
		return new Color(255, 255, 255);
	}

	@Alpha
	@ConfigItem(
		position = 14,
		keyName = "totemHudOutlineColor",
		name = " - Outline Color",
		description = "Sets the outline color of the totem HUD",
		section = totemSettings
	)
	default Color totemHudOutlineColor()
	{
		return new Color(0, 0, 0);
	}

	@ConfigItem(
		position = 15,
		keyName = "totemHudFont",
		name = " - Font",
		description = "Sets the font of the totem HUD",
		section = totemSettings
	)
	default FontType totemHudFont()
	{
		return FontType.REGULAR;
	}

	@ConfigItem(
		position = 16,
		keyName = "totemHudBarWidth",
		name = " - Bar Width",
		description = "Sets the bar width of the totem HUD",
		section = totemSettings
	)
	default int totemHudBarWidth()
	{
		return 60;
	}

	@ConfigItem(
		position = 17,
		keyName = "totemHudBarHeight",
		name = " - Bar Height",
		description = "Sets the bar height of the totem HUD",
		section = totemSettings
	)
	default int totemHudBarHeight()
	{
		return 5;
	}

	@ConfigSection(
		position = 1,
		name = "Boss Settings",
		description = "Settings related to the boss",
		closedByDefault = true
	)
	String bossSettings = "bossSettings";

	@ConfigItem(
		position = 0,
		keyName = "phosaniShieldOverlay",
		name = "Shield Overlay",
		description = "Draws an overlay over Phosani showing her current shield amount",
		section = bossSettings
	)
	default OverlayType phosaniShieldOverlay()
	{
		return OverlayType.NONE;
	}

	@Alpha
	@ConfigItem(
		position = 1,
		keyName = "phosaniShieldOverlayColor",
		name = " - Color",
		description = "Sets the color to use for Phosani's shield overlay",
		section = bossSettings
	)
	default Color phosaniShieldOverlayColor()
	{
		return new Color(255, 255, 0);
	}

	@ConfigItem(
		position = 2,
		keyName = "phosaniShieldOverlayOffset",
		name = " - Z-Offset",
		description = "Sets the z-offset for Phosani's shield overlay",
		section = bossSettings
	)
	default int phosaniShieldOverlayOffset()
	{
		return 0;
	}

	@ConfigItem(
		position = 3,
		keyName = "phosaniShieldOverlayFont",
		name = " - Font",
		description = "Sets the font to use for Phosani's shield overlay",
		section = bossSettings
	)
	default FontType phosaniShieldOverlayFont()
	{
		return FontType.REGULAR;
	}

	@Range(min = 0, max = 5)
	@ConfigItem(
		position = 4,
		keyName = "phosaniShieldeOverlayOutline",
		name = " - Outline",
		description = "Sets the width of the outline around Phosani's shield overlay",
		section = bossSettings
	)
	default int phosaniShieldOverlayOutline()
	{
		return 0;
	}

	@ConfigItem(
		position = 5,
		keyName = "hideHealthOverlay",
		name = "Hide Jagex HP HUD",
		description = "Force hides the in-game boss HP HUD",
		section = bossSettings
	)
	default boolean hideHealthOverlay()
	{
		return false;
	}

	@ConfigSection(
		position = 2,
		name = "Adds Settings",
		description = "Settings related to adds spawned by the boss",
		closedByDefault = true
	)
	String addsSettings = "addsSettings";

	@ConfigItem(
		position = 0,
		keyName = "highlightRangedHusk",
		name = "Highlight Ranged Husk",
		description = "Sets the highlight style for the ranged husk",
		section = addsSettings
	)
	default HighlightStyle highlightRangedHusk()
	{
		return HighlightStyle.CLICKBOX;
	}

	@Alpha
	@ConfigItem(
		position = 1,
		keyName = "rangedHuskBorderColor",
		name = " - Border Color",
		description = "Sets the border color the ranged husk",
		section = addsSettings
	)
	default Color rangedHuskBorderColor()
	{
		return new Color(0, 225, 0);
	}

	@Alpha
	@ConfigItem(
		position = 2,
		keyName = "rangedHuskFillColor",
		name = " - Fill Color",
		description = "Sets the fill color for the ranged husk",
		section = addsSettings
	)
	default Color rangedHuskFillColor()
	{
		return new Color(0, 225, 0, 30);
	}

	@ConfigItem(
		position = 3,
		keyName = "highlightMagicHusk",
		name = "Highlight Magic Husk",
		description = "Sets the highlight style for the magic husk",
		section = addsSettings
	)
	default HighlightStyle highlightMagicHusk()
	{
		return HighlightStyle.CLICKBOX;
	}

	@Alpha
	@ConfigItem(
		position = 4,
		keyName = "magicHuskBorderColor",
		name = " - Border Color",
		description = "Sets the border color for the magic husk",
		section = addsSettings
	)
	default Color magicHuskBorderColor()
	{
		return new Color(0, 225, 255);
	}

	@Alpha
	@ConfigItem(
		position = 5,
		keyName = "magicHuskFillColor",
		name = " - Fill Color",
		description = "Sets the fill color for the magic husk",
		section = addsSettings
	)
	default Color magicHuskFillColor()
	{
		return new Color(0, 225, 255, 30);
	}

	@ConfigItem(
		position = 6,
		keyName = "highlightParasite",
		name = "Highlight Parasite",
		description = "Sets the highlight style for the parasite",
		section = addsSettings
	)
	default HighlightStyle highlightParasite()
	{
		return HighlightStyle.CLICKBOX;
	}

	@Alpha
	@ConfigItem(
		position = 7,
		keyName = "parasiteBorderColor",
		name = " - Border Color",
		description = "Sets the border color for the parasite",
		section = addsSettings
	)
	default Color parasiteBorderColor()
	{
		return new Color(226, 138, 255);
	}

	@Alpha
	@ConfigItem(
		position = 8,
		keyName = "parasiteFillColor",
		name = " - Fill Color",
		description = "Sets the fill color for the parasite",
		section = addsSettings
	)
	default Color parasiteFillColor()
	{
		return new Color(226, 138, 255, 30);
	}

	@ConfigItem(
		position = 9,
		keyName = "highlightSleepwalkers",
		name = "Highlight Sleepwalkers",
		description = "Sets the highlight style for sleepwalkers",
		section = addsSettings
	)
	default HighlightStyle highlightSleepwalkers()
	{
		return HighlightStyle.CLICKBOX;
	}

	@Alpha
	@ConfigItem(
		position = 10,
		keyName = "sleepwalkerBorderColor",
		name = " - Border Color",
		description = "Sets the border color for sleepwalkers",
		section = addsSettings
	)
	default Color sleepwalkerBorderColor()
	{
		return new Color(0, 225, 255);
	}

	@Alpha
	@ConfigItem(
		position = 11,
		keyName = "sleepwalkerFillColor",
		name = " - Fill Color",
		description = "Sets the fill color for sleepwalkers",
		section = addsSettings
	)
	default Color sleepwalkerFillColor()
	{
		return new Color(0, 225, 255, 30);
	}

	@ConfigItem(
		position = 12,
		keyName = "addsBorderWidth",
		name = "Border Width",
		description = "Sets the width of the lines highlighting adds",
		section = addsSettings
	)
	default double addsBorderWidth()
	{
		return 1.0;
	}

	@ConfigSection(
		position = 3,
		name = "Other Settings",
		description = "Additional settings and features",
		closedByDefault = true
	)
	String otherSettings = "otherSettings";

	@ConfigItem(
		position = 0,
		keyName = "highlightSpores",
		name = "Highlight Spores",
		description = "Choose how spores are highlighted",
		section = otherSettings
	)
	default HighlightStyle highlightSpores()
	{
		return HighlightStyle.TILE;
	}

	@Alpha
	@ConfigItem(
		position = 1,
		keyName = "sporesBorderColor",
		name = " - Color",
		description = "Sets the border color highlighting spores",
		section = otherSettings
	)
	default Color sporesBorderColor()
	{
		return new Color(255, 255, 0);
	}

	@Alpha
	@ConfigItem(
		position = 2,
		keyName = "sporesFillColor",
		name = " - Fill Color",
		description = "Sets the fill color highlighting spores",
		section = otherSettings
	)
	default Color sporesFillColor()
	{
		return new Color(255, 255, 0, 30);
	}

	/*
	@Alpha
	@ConfigItem(
		position = 4,
		keyName = "totemHudTextColor",
		name = " - Text Color",
		description = "Sets the text color for custom totem HUD",
		section = otherSettings
	)
	default Color totemHudOverlayColor()
	{
		return new Color(230, 150, 30);
	}

	@ConfigItem(
		position = 5,
		keyName = "totemHudTextFont",
		name = " - Font",
		description = "Sets the font for custom totem HUD",
		section = otherSettings
	)
	default FontType totemHudOverlayFont()
	{
		return FontType.SMALL;
	}

	@Range(min = 0, max = 5)
	@ConfigItem(
		position = 6,
		keyName = "totemHudTextOutline",
		name = " - Text Outline",
		description = "Sets text outline width for custom totem HUD",
		section = otherSettings
	)
	default int totemHudOverlayOutline()
	{
		return 1;
	}
	 */
}
