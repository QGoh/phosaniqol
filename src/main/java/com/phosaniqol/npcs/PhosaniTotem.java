package com.phosaniqol.npcs;

import com.phosaniqol.HighlightStyle;
import com.phosaniqol.OverlayType;
import com.phosaniqol.PhosaniQolConfig;
import java.awt.Color;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.client.config.FontType;

@Getter
@Setter
@Slf4j
public class PhosaniTotem implements PhosaniNpc
{
	private NPC npc;
	private int currentCharge;
	private int maxCharge;
	private HashMap<Integer, Integer> queuedHits;
	private PhosaniQolConfig config;

	public PhosaniTotem(NPC npc, int currentCharge, int maxCharge, PhosaniQolConfig config)
	{
		this.npc = npc;
		this.currentCharge = currentCharge;
		this.maxCharge = maxCharge;
		this.queuedHits = new HashMap<>();
		this.config = config;
	}

	public int getHealthValue()
	{
		return currentCharge;
	}

	public double getHealthPercent()
	{
		return (double) currentCharge / maxCharge;
	}

	public HighlightStyle getHighlightStyle()
	{
		return config.highlightChargedTotems();
	}

	public Color getBorderColor()
	{
		return config.chargedBorderColor();
	}

	public Color getFillColor()
	{
		return config.chargedFillColor();
	}

	public Double getBorderWidth()
	{
		return config.chargedBorderWidth();
	}

	public OverlayType getOverlayType()
	{
		return config.totemChargeOverlay();
	}

	public Color getOverlayColor()
	{
		return config.totemChargeOverlayColor();
	}

	public int getOverlayOffset()
	{
		return config.totemChargeOverlayOffset();
	}

	public FontType getOverlayFont()
	{
		return config.totemChargeOverlayFont();
	}

	public int getOverlayOutline()
	{
		return config.totemChargeOverlayOutline();
	}

	public void addHit(int expireTick, int hit)
	{
		if (expireTick != -1)
		{
			this.queuedHits.put(expireTick, hit);
			//StringBuilder out = new StringBuilder("ADD PROJECTILE QUEUE -> ");
			//this.queue.forEach((key, val) -> out.append("{").append(key).append(": ").append(val).append("},"));
			//log.info(out.toString());
		}
	}

	public void incrementCharge(int hit)
	{
		currentCharge = Math.min(currentCharge + hit, 200);
	}

	// copied from opponentInfo plugin
	public void recalculateCharge(int tick)
	{
		int chargeRatio = npc.getHealthRatio();
		int chargeScale = npc.getHealthScale();
		int recalculatedCharge = -1;
		if (chargeRatio >= 0 && chargeScale > 0)
		{
			// This is the reverse of the calculation of chargeRatio done by the server
			// which is: chargeRatio = 1 + (chargeScale - 1) * charge / maxCharge (if charge > 0, 0 otherwise)
			// It's able to recover the exact charge if maxCharge <= chargeScale.
			int floor = 0;
			int ceiling;
			if (chargeRatio > 1)
			{
				// This doesn't apply if chargeRatio = 1, because of the special case in the server calculation that
				// charge = 0 forces chargeRatio = 0 instead of the expected chargeRatio = 1
				floor = (maxCharge * (chargeRatio - 1) + chargeScale - 2) / (chargeScale - 1);
			}
			ceiling = (maxCharge * chargeRatio - 1) / (chargeScale - 1);
			if (ceiling > maxCharge)
			{
				ceiling = maxCharge;
			}
			// Take the average of min and max possible charges
			recalculatedCharge = ((floor + ceiling + 1) / 2);
		}

		queuedHits.keySet().removeIf(expireTick -> expireTick <= tick);
		currentCharge = Math.min(200, recalculatedCharge + sumQueuedHits());
	}

	private int sumQueuedHits()
	{
		int sum = 0;
		for (Integer hit: queuedHits.values())
		{
			sum += hit;
		}

		return sum;
	}
}
