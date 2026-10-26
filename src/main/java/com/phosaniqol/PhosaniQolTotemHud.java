package com.phosaniqol;

import com.phosaniqol.npcs.PhosaniTotem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Slf4j
public class PhosaniQolTotemHud extends Overlay
{
	private final int PADDING = 6;
	private final int TEXT_BAR_GAP = 2;
	private final int OUTLINE_WIDTH = 1;

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
		//if (config.totemHud() != TotemHudStyle.CUSTOM) return null;
		//if (plugin.getTotems().isEmpty()) return null;

		Color textColor = config.totemHudTextColor();
		Color barBackground = config.totemHudBarBackground();
		Color barForeground = config.totemHudBarForeground();
		Color barFull = config.totemHudBarFull();
		Color outlineColor = config.totemHudOutlineColor();
		Font font = config.totemHudFont().getFont();
		int barWidth = config.totemHudBarWidth();
		int barHeight = config.totemHudBarHeight();

		FontMetrics fontMetrics = graphics2D.getFontMetrics(font);
		int cellWidth = Math.max(fontMetrics.stringWidth("North West"), barWidth) + PADDING;
		int cellHeight = fontMetrics.getAscent() + TEXT_BAR_GAP + barHeight + PADDING;

		Map<Integer, PhosaniTotem> totems = plugin.getTotems();

		int corner = 0;
		for (var entry: PhosaniQolPlugin.TOTEMS_READY.entrySet())
		{
			int npcId = entry.getKey();
			String name = entry.getValue();
			int column = corner % 2;
			int row = corner / 2;
			int x = (column * cellWidth);
			int y = (row * cellHeight) + fontMetrics.getAscent() + (PADDING / 2);
			int textPadding = ((cellWidth - fontMetrics.stringWidth(name)) / 2);
			int barPadding = ((cellWidth - barWidth) / 2);

			drawText(graphics2D, font, textColor, OUTLINE_WIDTH, outlineColor, x + textPadding, y, name);

			graphics2D.setColor(outlineColor);
			graphics2D.fillRect(x + barPadding - OUTLINE_WIDTH, y + TEXT_BAR_GAP - OUTLINE_WIDTH, barWidth + (OUTLINE_WIDTH * 2), barHeight + (OUTLINE_WIDTH * 2));

			graphics2D.setColor(barBackground);
			graphics2D.fillRect(x + barPadding, y + OUTLINE_WIDTH + TEXT_BAR_GAP, barWidth, barHeight);

			if (totems.containsKey(npcId) && totems.get(npcId).getHealthPercent() < 1.0)
			{
				graphics2D.setColor(barForeground);
				graphics2D.fillRect(x + barPadding, y + TEXT_BAR_GAP, (int) (barWidth * totems.get(npcId).getHealthPercent()), barHeight);
			}
			else
			{
				graphics2D.setColor(barFull);
				graphics2D.fillRect(x + barPadding, y + TEXT_BAR_GAP, barWidth, barHeight);
			}
			corner += 1;
		}

		return new Dimension(cellWidth * 2, cellHeight * 2);
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
