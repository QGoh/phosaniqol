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
	private PhosaniQolConfig.SporeHighlightStyle highlightStyle;
	private Color color;

	public PhosaniSpore(GameObject gameObject, PhosaniQolConfig config)
	{
		this.gameObject = gameObject;
		setHighlightConfig(config);
	}

	public void setHighlightConfig(PhosaniQolConfig config)
	{
		this.highlightStyle = config.highlightSpores();
		this.color = config.sporesColor();
	}
}