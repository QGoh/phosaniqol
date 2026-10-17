package com.phosaniqol;

import java.awt.Color;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GameObject;
import net.runelite.client.config.FontType;

@Getter
@Setter
public class Spore
{
	private GameObject gameObject;
	private PhosaniQolConfig config;

	public Spore(GameObject gameObject, PhosaniQolConfig config)
	{
		this.gameObject = gameObject;
		this.config = config;
	}

	public HighlightStyle getHighlightStyle()
	{
		return config.highlightSpores();
	}

	public Color getBorderColor()
	{
		return config.sporesBorderColor();
	}

	public Color getFillColor()
	{
		return config.sporesFillColor();
	}

	public Double getBorderWidth()
	{
		return config.sporesBorderWidth();
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