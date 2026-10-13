package com.phosaniqol;

import java.awt.Color;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GameObject;

@Getter
@Setter
public class PhosaniSpore
{
	private GameObject gameObject;
	private PhosaniQolConfig.HighlightStyle highlightStyle;
	private Double borderWidth;
	private Color borderColor;
	private Color fillColor;

	public PhosaniSpore(GameObject gameObject, PhosaniQolConfig config)
	{
		this.gameObject = gameObject;
		setHighlightConfig(config);
	}

	public void setHighlightConfig(PhosaniQolConfig config)
	{
		this.highlightStyle = config.highlightSpores();
		this.borderWidth = config.sporesBorderWidth();
		this.borderColor = config.sporesBorderColor();
		this.fillColor = config.sporesFillColor();
	}
}