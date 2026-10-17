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
public class PhosaniBoss implements PhosaniNpc
{
	private NPC npc;
	private int currentHealth;
	private int maxHealth;
	private PhosaniQolConfig config;

	public PhosaniBoss(NPC npc, int currentHealth, int maxHealth, PhosaniQolConfig config)
	{
		this.npc = npc;
		this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		this.config = config;
	}

	public int getHealthValue()
	{
		return this.currentHealth;
	}

	public int getHealthPercent()
	{
		return (int) ((double) this.currentHealth / this.maxHealth * 100);
	}

	public HighlightStyle getHighlightStyle()
	{
		return HighlightStyle.NONE;
	}

	public Color getBorderColor()
	{
		return new Color(255, 255, 0);
	}

	public Color getFillColor()
	{
		return new Color(255, 255, 0, 30);
	}

	public Double getBorderWidth()
	{
		return 0.0;
	}

	public OverlayType getOverlayType()
	{
		return config.phosaniShieldOverlay();
	}

	public Color getOverlayColor()
	{
		return config.phosaniShieldOverlayColor();
	}

	public int getOverlayOffset()
	{
		return config.phosaniShieldOverlayOffset();
	}

	public FontType getOverlayFont()
	{
		return config.phosaniShieldOverlayFont();
	}
}
