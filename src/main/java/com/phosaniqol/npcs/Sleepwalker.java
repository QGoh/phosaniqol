package com.phosaniqol.npcs;

import com.phosaniqol.HighlightStyle;
import com.phosaniqol.OverlayType;
import com.phosaniqol.PhosaniQolConfig;
import java.awt.Color;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.client.config.FontType;

@Getter
@Setter
public class Sleepwalker implements PhosaniNpc
{
	private NPC npc;
	private int currentHealth;
	private int totalHealth;
	private PhosaniQolConfig config;

	public Sleepwalker(NPC npc, PhosaniQolConfig config)
	{
		this.npc = npc;
		this.config = config;
	}

	public int getHealthValue()
	{
		return this.currentHealth;
	}

	public int getHealthPercent()
	{
		return (int) ((double) this.currentHealth / this.totalHealth * 100);
	}

	public HighlightStyle getHighlightStyle()
	{
		return config.highlightSleepwalkers();
	}

	public Color getBorderColor()
	{
		return config.sleepwalkerBorderColor();
	}

	public Color getFillColor()
	{
		return config.sleepwalkerFillColor();
	}

	public Double getBorderWidth()
	{
		return config.addsBorderWidth();
	}

	public OverlayType getOverlayType()
	{
		return OverlayType.NONE;
	}

	public Color getOverlayColor()
	{
		return new Color(255, 255, 0);
	}

	public int getOverlayOffset()
	{
		return 0;
	}

	public FontType getOverlayFont()
	{
		return FontType.REGULAR;
	}
}
