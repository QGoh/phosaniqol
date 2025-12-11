package com.phosaniqol;

import java.awt.Color;
import java.awt.Font;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.ui.FontManager;

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

	@ConfigItem(
		position = 1,
		keyName = "chargedBorderWidth",
		name = "Border Width",
		description = "Sets the width of the lines highlighting charged totems",
		section = totemSettings
	)
	default double chargedBorderWidth()
	{
		return 1.0;
	}

	@Alpha
	@ConfigItem(
		position = 2,
		keyName = "chargedBorderColor",
		name = "Charged Border Color",
		description = "Sets the border color highlighting charged totems",
		section = totemSettings
	)
	default Color chargedBorderColor()
	{
		return new Color(0, 225, 255);
	}

	@Alpha
	@ConfigItem(
		position = 3,
		keyName = "chargedFillColor",
		name = "Charged Fill Color",
		description = "Sets the fill color for highlighting charged totems",
		section = totemSettings
	)
	default Color chargedFillColor()
	{
		return new Color(0, 225, 255, 30);
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

	@ConfigItem(
		position = 5,
		keyName = "totemChargeOverlayFont",
		name = "Overlay Font",
		description = "Sets the font to use for drawing totem charge overlays",
		section = totemSettings
	)
	default Fonts totemChargeOverlayFont()
	{
		return Fonts.REGULAR;
	}

	@ConfigItem(
		position = 6,
		keyName = "totemChargeOverlayOffset",
		name = "Overlay Z-Offset",
		description = "Sets the z-offset for drawing totem charge overlays",
		section = totemSettings
	)
	default int totemChargeOverlayOffset()
	{
		return 0;
	}

	@Alpha
	@ConfigItem(
		position = 7,
		keyName = "totemChargeOverlayColor",
		name = "Overlay Color",
		description = "Sets the color to use for drawing totem charge overlays",
		section = totemSettings
	)
	default Color totemChargeOverlayColor()
	{
		return new Color(255, 255, 0);
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

	@ConfigItem(
		position = 1,
		keyName = "phosaniShieldOverlayFont",
		name = "Overlay Font",
		description = "Sets the font to use for drawing Phosani's shield overlay",
		section = bossSettings
	)
	default Fonts phosaniShieldOverlayFont()
	{
		return Fonts.REGULAR;
	}

	@ConfigItem(
		position = 2,
		keyName = "phosaniShieldOverlayOffset",
		name = "Overlay Z-Offset",
		description = "Sets the z-offset for drawing Phosani's shield overlay",
		section = bossSettings
	)
	default int phosaniShieldOverlayOffset()
	{
		return 0;
	}

	@Alpha
	@ConfigItem(
		position = 3,
		keyName = "phosaniShieldOverlayColor",
		name = "Overlay Color",
		description = "Sets the color to use for drawing Phosani's shield overlay",
		section = bossSettings
	)
	default Color phosaniShieldOverlayColor()
	{
		return new Color(255, 255, 0);
	}

	@ConfigItem(
		position = 4,
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

	@ConfigItem(
		position = 1,
		keyName = "highlightMagicHusk",
		name = "Highlight Magic Husk",
		description = "Sets the highlight style for the magic husk",
		section = addsSettings
	)
	default HighlightStyle highlightMagicHusk()
	{
		return HighlightStyle.CLICKBOX;
	}

	@ConfigItem(
		position = 2,
		keyName = "highlightParasite",
		name = "Highlight Parasite",
		description = "Sets the highlight style for parasites",
		section = addsSettings
	)
	default HighlightStyle highlightParasite()
	{
		return HighlightStyle.CLICKBOX;
	}

	@ConfigItem(
		position = 3,
		keyName = "highlightSleepwalkers",
		name = "Highlight Sleepwalkers",
		description = "Sets the highlight style for sleepwalkers",
		section = addsSettings
	)
	default HighlightStyle highlightSleepwalkers()
	{
		return HighlightStyle.CLICKBOX;
	}

	@ConfigItem(
		position = 4,
		keyName = "addsBorderWidth",
		name = "Border Width",
		description = "Sets the width of the lines highlighting adds.",
		section = addsSettings
	)
	default double addsBorderWidth()
	{
		return 1.0;
	}

	@Alpha
	@ConfigItem(
		position = 5,
		keyName = "addsBorderColor",
		name = "Border Color",
		description = "Sets the border color highlighting adds",
		section = addsSettings
	)
	default Color addsBorderColor()
	{
		return new Color(0, 225, 255);
	}

	@Alpha
	@ConfigItem(
		position = 6,
		keyName = "addsFillColor",
		name = "Fill Color",
		description = "Sets the fill color for highlighting adds",
		section = addsSettings
	)
	default Color addsFillColor()
	{
		return new Color(0, 225, 255, 30);
	}

	@Getter
	@RequiredArgsConstructor
	enum HighlightStyle
	{
		NONE("None"),
		TILE("Tile"),
		HULL("Hull"),
		OUTLINE("Outline"),
		CLICKBOX("Clickbox");

		private final String highlightStyle;
	}

	@Getter
	@RequiredArgsConstructor
	enum Fonts
	{
		SMALL(FontManager.getRunescapeSmallFont()),
		REGULAR(FontManager.getRunescapeFont()),
		BOLD(FontManager.getRunescapeBoldFont());

		private final Font font;
	}

	@Getter
	@RequiredArgsConstructor
	enum OverlayType
	{
		NONE("None"),
		VALUE("Value"),
		PERCENTAGE("Percentage");

		private final String overlayType;
	}
}
