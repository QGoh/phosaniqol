package com.phosaniqol.npcs;

import com.phosaniqol.HighlightStyle;
import com.phosaniqol.OverlayType;
import java.awt.Color;
import net.runelite.api.NPC;
import net.runelite.client.config.FontType;

public interface PhosaniNpc
{
	NPC getNpc();

	int getHealthValue();

	double getHealthPercent();

	HighlightStyle getHighlightStyle();

	Double getBorderWidth();

	Color getBorderColor();

	Color getFillColor();

	OverlayType getOverlayType();

	FontType getOverlayFont();

	int getOverlayOffset();

	Color getOverlayColor();

	int getOverlayOutline();
}
