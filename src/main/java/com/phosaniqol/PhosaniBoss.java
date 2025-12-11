package com.phosaniqol;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;

@Getter
@Setter
public class PhosaniBoss extends PhosaniNpc
{
	private int shield;

	public PhosaniBoss(NPC npc, int shield, PhosaniQolConfig config)
	{
		super.setNpc(npc);
		this.shield = shield;
		setHighlightConfig(config);
	}
 
	public void setHighlightConfig(PhosaniQolConfig config)
	{
		super.setHighlightConfig(
			null,
			null,
			null,
			null,
			config.phosaniShieldOverlay(),
			config.phosaniShieldOverlayFont().getFont(),
			config.phosaniShieldOverlayOffset(),
			config.phosaniShieldOverlayColor()
		);
	}
}
