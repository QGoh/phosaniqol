package com.phosaniqol;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.FakeXpDrop;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(name = "Phosani QoL")
public class PhosaniQolPlugin extends Plugin
{
	// 1 = SW, 2 = SW, 3 = NW, 4 = NE
	private final Set<Integer> dormantTotems = ImmutableSet.of(
		NpcID.NIGHTMARE_TOTEM_1_DORMANT,
		NpcID.NIGHTMARE_TOTEM_2_DORMANT,
		NpcID.NIGHTMARE_TOTEM_3_DORMANT,
		NpcID.NIGHTMARE_TOTEM_4_DORMANT
	);

	@Getter
	private final Set<Integer> readyTotems = ImmutableSet.of(
		NpcID.NIGHTMARE_TOTEM_1_READY,
		NpcID.NIGHTMARE_TOTEM_2_READY,
		NpcID.NIGHTMARE_TOTEM_3_READY,
		NpcID.NIGHTMARE_TOTEM_4_READY
	);

	private final Set<Integer> chargedTotems = ImmutableSet.of(
		NpcID.NIGHTMARE_TOTEM_1_CHARGED,
		NpcID.NIGHTMARE_TOTEM_2_CHARGED,
		NpcID.NIGHTMARE_TOTEM_3_CHARGED,
		NpcID.NIGHTMARE_TOTEM_4_CHARGED
	);

	private final Set<Integer> phosaniPhases = ImmutableSet.of(
		NpcID.NIGHTMARE_CHALLENGE_PHASE_01,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_02,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_03,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_04,
		NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_01,
		NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_02,
		NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_03,
		NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_04,
		NpcID.NIGHTMARE_CHALLENGE_INITIAL,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_05,
		NpcID.NIGHTMARE_CHALLENGE_DYING,
		NpcID.NIGHTMARE_CHALLENGE_DEAD,
		NpcID.NIGHTMARE_CHALLENGE_BLAST
	);

	private final Set<Integer> phosaniAdds = ImmutableSet.of(
		NpcID.NIGHTMARE_CHALLENGE_PARASITE,
		NpcID.NIGHTMARE_CHALLENGE_PARASITE_WEAK,
		NpcID.NIGHTMARE_CHALLENGE_HUSK_RANGED,
		NpcID.NIGHTMARE_CHALLENGE_HUSK_MAGIC,
		NpcID.NIGHTMARE_CHALLENGE_SLEEPWALKER
	);

	private final int HPBAR_HUD = 6099;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private PhosaniQolConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private NPCManager npcManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private PhosaniQolOverlay overlay;

	@Getter
	private final Map<Integer, PhosaniTotem> totems = new HashMap<>();
	@Getter
	private PhosaniBoss phosaniBoss = null;
	@Getter
	private final Map<Integer, PhosaniAdd> adds = new HashMap<>();
	private final Set<Skill> xpDrops = new HashSet<>();

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		clearAll();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(PhosaniQolConfig.CONFIG_GROUP))
		{
			switch (event.getKey())
			{
				case "highlightChargedTotems":
				case "chargedBorderWidth":
				case "chargedBorderColor":
				case "chargedFillColor":
				case "totemChargeOverlay":
				case "totemChargeOverlayFont":
				case "totemChargeOverlayOffset":
				case "totemChargeOverlayColor":
					totems.forEach((npcId, totem) -> totem.setHighlightConfig(config));
					break;
				case "phosaniShieldOverlay":
				case "phosaniShieldOverlayFont":
				case "phosaniShieldOverlayOffset":
				case "phosaniShieldOverlayColor":
					if (phosaniBoss != null)
					{
						phosaniBoss.setHighlightConfig(config);
					}
					break;
				case "highlightRangedHusk":
				case "highlightMagicHusk":
				case "highlightParasite":
				case "highlightSleepwalkers":
				case "addsBorderWidth":
				case "addsBorderColor":
				case "addsFillColor":
					adds.forEach((npcId, add) -> add.setHighlightConfig(config));
					break;
				case "hideHealthOverlay":
					Widget healthBar = client.getWidget(InterfaceID.HpbarHud.HP);
					if (healthBar != null)
					{
						healthBar.setHidden(config.hideHealthOverlay());
					}
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			clearAll();
		}
	}

	@Subscribe
	public void onFakeXpDrop(FakeXpDrop event)
	{
		final int attackTick = client.getTickCount();
		clientThread.invokeLater(() ->
		{
			Skill skill = event.getSkill();
			xpDrops.add(skill);

			TotemWeapon totemWeapon = usedWeapon();

			if (skill == Skill.HITPOINTS)
			{
				Player player = client.getLocalPlayer();
				Actor actor = player.getInteracting();
				if (actor instanceof NPC)
				{
					int npcId = ((NPC) actor).getId();
					if (!readyTotems.contains(npcId)) return;

					int hit = (event.getXp() == 0) ? 1 : (int) Math.round(event.getXp() * (3.0d / 4.0d));
					int multiplier = (xpDrops.contains(Skill.MAGIC)) ? 2 : 1;

					if (totemWeapon != null)
					{
						int expireTick = attackTick + getHitDelay(totemWeapon, actor);
						totems.get(npcId).addHit(expireTick, hit * multiplier);
					}

					totems.get(npcId).incrementCharge(hit * multiplier);
				}
				xpDrops.clear();
			}
		});
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		int hitsplatTick = client.getTickCount();
		clientThread.invokeLater(() ->
		{
			Actor actor = event.getActor();
			if (actor instanceof NPC)
			{
				usedWeapon();
				int npcId = ((NPC) actor).getId();

				//int hitsplatType = event.getHitsplat().getHitsplatType();
				//int hitsplatAmount = event.getHitsplat().getAmount();

				if (!readyTotems.contains(npcId)) return;

				PhosaniTotem targetTotem = totems.get(npcId);
				int recalculatedCharge = targetTotem.recalculateCharge();
				targetTotem.removeHits(hitsplatTick);
				int queuedHits = targetTotem.sumQueue();
				//log.info("RECALCULATED " + recalculatedCharge + " QUEUE " + queuedHits);
				targetTotem.setCharge(recalculatedCharge + queuedHits);
			}
		});
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarbitId() != HPBAR_HUD)
		{
			return;
		}
		if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1 && phosaniBoss != null && phosaniBoss.getShield() > 0)
		{
			int shield = client.getVarbitValue(HPBAR_HUD);
			phosaniBoss.setShield(shield);
		}
	}

	@Subscribe
	public void onNpcChanged(NpcChanged event)
	{
		NPCComposition oldNpc = event.getOld();
		NPC newNpc = event.getNpc();
		int oldNpcId = oldNpc.getId();
		int newNpcId = newNpc.getId();

		if (dormantTotems.contains(oldNpcId) || readyTotems.contains(oldNpcId) || chargedTotems.contains(oldNpcId))
		{
			totems.remove(oldNpcId);
			int charge = (chargedTotems.contains(newNpcId)) ? 200
				: (readyTotems.contains(newNpcId)) ? 0
				: -1;
			totems.put(newNpcId, new PhosaniTotem(newNpc, charge, config));
		}
		else if (phosaniPhases.contains(newNpcId))
		{
			phosaniBoss.setNpc(newNpc);
			int shield = -1;
			switch (newNpcId)
			{
				case NpcID.NIGHTMARE_CHALLENGE_PHASE_01:
					shield = 400;
					break;
				case NpcID.NIGHTMARE_CHALLENGE_PHASE_02:
					shield = 400;
					break;
				case NpcID.NIGHTMARE_CHALLENGE_PHASE_03:
					shield = 400;
					break;
				case NpcID.NIGHTMARE_CHALLENGE_PHASE_04:
					shield = 400;
					break;
				case NpcID.NIGHTMARE_CHALLENGE_PHASE_05:
					shield = 150;
					break;
			}
			phosaniBoss.setShield(shield);
			phosaniBoss.setHighlightConfig(config);
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		int npcId = npc.getId();
		if (dormantTotems.contains(npcId))
		{
			totems.put(npcId, new PhosaniTotem(npc, -1, config));
		}
		else if (readyTotems.contains(npcId))
		{
			totems.put(npcId, new PhosaniTotem(npc, 0, config));
		}
		else if (chargedTotems.contains(npcId))
		{
			totems.put(npcId, new PhosaniTotem(npc, 200, config));
		}
		else if (phosaniPhases.contains(npcId))
		{
			int shield = -1;
			if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1 && npcId != NpcID.NIGHTMARE_CHALLENGE_INITIAL)
			{
				shield = client.getVarbitValue(HPBAR_HUD);
			}
			phosaniBoss = new PhosaniBoss(npc, shield, config);
		}
		else if (phosaniAdds.contains(npcId))
		{
			int index = npc.getIndex();
			int key = -(npcId + index);
			adds.put(key, new PhosaniAdd(npc, config));
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		NPC npc = event.getNpc();
		int npcId = npc.getId();
		if (phosaniAdds.contains(npcId))
		{
			int index = npc.getIndex();
			int key = -(npcId + index);
			adds.remove(key);
		}
	}

	@Subscribe
	public void onBeforeRender(BeforeRender beforeRender)
	{
		if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1)
		{
			Widget healthBar = client.getWidget(InterfaceID.HpbarHud.HP);
			if (healthBar != null)
			{
				healthBar.setHidden(config.hideHealthOverlay());
			}
		}
	}

	private void clearAll()
	{
		totems.clear();
		phosaniBoss = null;
		xpDrops.clear();
	}

	private int getHitDelay(TotemWeapon totemWeapon, Actor target)
	{
		if (target == null)
			return 1;

		Player player = client.getLocalPlayer();
		if (player == null)
			return 1;

		WorldPoint playerWp = player.getWorldLocation();
		if (playerWp == null)
			return 1;

		WorldArea targetArea = target.getWorldArea();
		if (targetArea == null)
			return 1;

		final int distance = targetArea.distanceTo(playerWp);

		return totemWeapon.getHitDelay(distance);
	}

	private TotemWeapon usedWeapon()
	{
		// TODO: manually casted spells
		// WIDGET_TARGET_ON_NPC
		// Client::getSelectedWidget
		if (client.getVarbitValue(VarbitID.AUTOCAST_SPELL) != 0)
		{
			return TotemWeapon.SPELLBOOK;
		}

		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		if (equipment == null)
		{
			return null;
		}

		Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon == null)
		{
			return null;
		}

		ItemStats weaponStats = itemManager.getItemStats(weapon.getId());
		if (weaponStats != null && weaponStats.getEquipment().getRstr() > 0)
		{
			if (weaponStats.getEquipment().getAspeed() <= 3)
			{
				return TotemWeapon.THROWN;
			}
			else
			{
				return TotemWeapon.BOW;
			}
		}

		for (TotemWeapon totemWeapon : TotemWeapon.values())
		{
			if (totemWeapon.contains(weapon.getId()))
			{
				return totemWeapon;
			}
		}

		return TotemWeapon.MELEE;
	}

	@Provides
	PhosaniQolConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PhosaniQolConfig.class);
	}
}
