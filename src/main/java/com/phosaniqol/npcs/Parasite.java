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
public class Parasite implements PhosaniNpc
{
	private NPC npc;
	private int currentHealth;
	private int totalHealth;
	private PhosaniQolConfig config;

	public Parasite(NPC npc, PhosaniQolConfig config)
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
		return config.highlightParasite();
	}

	public Color getBorderColor()
	{
		return config.parasiteBorderColor();
	}

	public Color getFillColor()
	{
		return config.parasiteFillColor();
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
