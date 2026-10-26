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
public class RangedHusk implements PhosaniNpc
{
	private NPC npc;
	private int currentHealth;
	private int totalHealth;
	private PhosaniQolConfig config;

	public RangedHusk(NPC npc, PhosaniQolConfig config)
	{
		this.npc = npc;
		this.config = config;
		this.currentHealth = npc.getComposition().getStats()[3];
		this.totalHealth = this.currentHealth;
	}

	public int getHealthValue()
	{
		return this.currentHealth;
	}

	public double getHealthPercent()
	{
		return (double) currentHealth / totalHealth;
	}

	public HighlightStyle getHighlightStyle()
	{
		return config.highlightRangedHusk();
	}

	public Color getBorderColor()
	{
		return config.rangedHuskBorderColor();
	}

	public Color getFillColor()
	{
		return config.rangedHuskFillColor();
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

	public int getOverlayOutline()
	{
		return 0;
	}
}
