package com.phosaniqol;

import com.phosaniqol.npcs.PhosaniTotem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.FontType;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Slf4j
public class PhosaniQolTotemHud extends Overlay
{
	private final int COLUMN_GAP = 7;
	private final int ROW_GAP = 7;
	private final int TEXT_BAR_GAP = 1;
	private final int PADDING = 10;
	private final int OUTLINE_WIDTH = 1;

	private final int BAR_WIDTH = 60;
	private final int BAR_HEIGHT = 5;
	private final int CELL_HEIGHT = 18;

	private final Font font = FontType.SMALL.getFont();

	private final PhosaniQolPlugin plugin;
	private final PhosaniQolConfig config;

	@Inject
	public PhosaniQolTotemHud(PhosaniQolPlugin plugin, PhosaniQolConfig config)
	{
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.TOP_LEFT);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics2D)
	{
		if (config.totemHud() != TotemHudStyle.CUSTOM) return null;
		if (plugin.getTotems().isEmpty()) return null;

		Color textColor = config.totemHudTextColor();
		Color barBackground = config.totemHudBarBackground();
		Color barForeground = config.totemHudBarForeground();
		Color barFull = config.totemHudBarFull();
		Color outlineColor = config.totemHudOutlineColor();

		Map<Integer, PhosaniTotem> totems = plugin.getTotems();

		int corner = 0;
		for (var entry: PhosaniQolPlugin.TOTEMS_READY.entrySet())
		{
			int npcId = entry.getKey();
			String name = entry.getValue();
			int column = corner % 2;
			int row = corner / 2;
			int x = column * (BAR_WIDTH + COLUMN_GAP);
			int y = (row * (CELL_HEIGHT + ROW_GAP)) + PADDING;

			int leftPadding = ((BAR_WIDTH - graphics2D.getFontMetrics(font).stringWidth(name)) / 2) + 1;
			drawText(graphics2D, font, textColor, OUTLINE_WIDTH, outlineColor, x + leftPadding, y, name);

			graphics2D.setColor(outlineColor);
			graphics2D.fillRect(x, y + TEXT_BAR_GAP, BAR_WIDTH + (OUTLINE_WIDTH * 2), BAR_HEIGHT + (OUTLINE_WIDTH * 2));

			graphics2D.setColor(barBackground);
			graphics2D.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH + TEXT_BAR_GAP, BAR_WIDTH, BAR_HEIGHT);

			if (totems.containsKey(npcId) && totems.get(npcId).getHealthPercent() < 1.0)
			{
				graphics2D.setColor(barForeground);
				graphics2D.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH + TEXT_BAR_GAP, (int) (BAR_WIDTH * totems.get(npcId).getHealthPercent()), BAR_HEIGHT);
			}
			else
			{
				graphics2D.setColor(barFull);
				graphics2D.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH + TEXT_BAR_GAP, BAR_WIDTH, BAR_HEIGHT);
			}
			corner += 1;
		}

		/*
		for (int corner = 0; corner < 4; corner++)
		{
			final int column = corner % 2;
			final int row = corner / 2;
			final int x = column * (BAR_WIDTH + COLUMN_GAP);
			final int y = (row * (CELL_HEIGHT + ROW_GAP)) + TOP_PADDING;
			final String text = LABELS[corner];

			int leftPadding = ((BAR_WIDTH - graphics2D.getFontMetrics(font).stringWidth(text)) / 2) + 1;
			drawText(graphics2D, font, textColor, OUTLINE_WIDTH, outlineColor, x + leftPadding, y, text);

			graphics2D.setColor(outlineColor);
			graphics2D.fillRect(x, y + GAP, BAR_WIDTH + (OUTLINE_WIDTH * 2), BAR_HEIGHT + (OUTLINE_WIDTH * 2));

			graphics2D.setColor(barBackground);
			graphics2D.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH + GAP, BAR_WIDTH, BAR_HEIGHT);

			PhosaniTotem totem = plugin.getTotems().getOrDefault([corner], null);
			if (totem != null && totem.getHealthPercent() < 1.0)
			{
				graphics2D.setColor(barForeground);
				graphics2D.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH + GAP, (int) (BAR_WIDTH * totem.getHealthPercent()), BAR_HEIGHT);
			}
			else
			{
				graphics2D.setColor(barFull);
				graphics2D.fillRect(x + OUTLINE_WIDTH, y + OUTLINE_WIDTH + GAP, BAR_WIDTH, BAR_HEIGHT);
			}
		}
		 */

		return new Dimension(BAR_WIDTH * 2 + COLUMN_GAP, CELL_HEIGHT * 2 + ROW_GAP + PADDING);
	}

	private void drawText(Graphics2D graphics2D, Font font, Color color, int outlineWidth, Color outlineColor, int x, int y, String text)
	{
		graphics2D.setFont(font);
		if (outlineWidth > 0)
		{
			graphics2D.setColor(outlineColor);
			for (int dx = -outlineWidth; dx <= outlineWidth; dx++)
			{
				for (int dy = -outlineWidth; dy <= outlineWidth; dy++)
				{
					if ((dx != 0 || dy != 0) && dx * dx + dy * dy <= outlineWidth * outlineWidth + 1)
					{
						graphics2D.drawString(text, x + dx, y + dy);
					}
				}
			}
		}
		graphics2D.setColor(color);
		graphics2D.drawString(text, x, y);
	}
}
