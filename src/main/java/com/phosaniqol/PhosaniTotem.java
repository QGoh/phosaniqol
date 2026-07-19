package com.phosaniqol;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;

@Getter
@Setter
@Slf4j
public class PhosaniTotem extends PhosaniNpc
{
	private int charge;
	private HashMap<Integer, Integer> queue;

	public PhosaniTotem(NPC npc, int charge, PhosaniQolConfig config)
	{
		super.setNpc(npc);
		this.charge = charge;
		this.queue = new HashMap<>();
		setHighlightConfig(config);
	}

	public void setHighlightConfig(PhosaniQolConfig config)
	{
		super.setHighlightConfig(
			(charge >= 0) ? config.highlightChargedTotems() : PhosaniQolConfig.HighlightStyle.NONE,
			config.chargedBorderWidth(),
			config.chargedBorderColor(),
			config.chargedFillColor(),
			config.totemChargeOverlay(),
			config.totemChargeOverlayFont().getFont(),
			config.totemChargeOverlayOffset(),
			config.totemChargeOverlayColor()
		);
	}

	public void addHit(int expireTick, int hit)
	{
		if (expireTick != -1)
		{
			this.queue.put(expireTick, hit);
			StringBuilder out = new StringBuilder("ADD PROJECTILE QUEUE -> ");
			this.queue.forEach((key, val) -> out.append("{").append(key).append(": ").append(val).append("},"));
			log.info(out.toString());
		}
	}

	public void removeHits(int tick)
	{
		this.queue.keySet().removeIf(expireTick -> expireTick <= tick);
		StringBuilder out = new StringBuilder("REMOVE PROJECTILE QUEUE -> ");
		this.queue.forEach((key, val) -> out.append("{").append(key).append(": ").append(val).append("},"));
		log.info(out.toString());
	}

	public int sumQueue()
	{
		int sum = 0;
		for (Integer hit: queue.values())
		{
			sum += hit;
		}

		return sum;
	}

	public void incrementCharge(int hit)
	{
		this.charge = Math.min(this.charge + hit, 200);
	}

	public void setCharge(int charge)
	{
		this.charge = Math.min(charge, 200);
	}

	// copied from opponentInfo plugin
	public int recalculateCharge()
	{
		NPC totem = super.getNpc();

		int chargeRatio = totem.getHealthRatio();
		int chargeScale = totem.getHealthScale();
		int maxCharge = 200;
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
			this.charge = ((floor + ceiling + 1) / 2);
		}

		return this.charge;
	}
}
