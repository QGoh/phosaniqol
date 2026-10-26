package com.phosaniqol;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import com.phosaniqol.npcs.MagicHusk;
import com.phosaniqol.npcs.Parasite;
import com.phosaniqol.npcs.PhosaniBoss;
import com.phosaniqol.npcs.PhosaniNpc;
import com.phosaniqol.npcs.PhosaniTotem;
import com.phosaniqol.npcs.RangedHusk;
import com.phosaniqol.npcs.Sleepwalker;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
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
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.components.DragAndDropReorderPane;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.GameObject;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;

@Slf4j
@PluginDescriptor(name = "Phosani QoL")
public class PhosaniQolPlugin extends Plugin
{
	// 1 = SW, 2 = SE, 3 = NW, 4 = NE
	private static final Set<Integer> TOTEMS_DORMANT = ImmutableSet.of(
		NpcID.NIGHTMARE_TOTEM_1_DORMANT,
		NpcID.NIGHTMARE_TOTEM_2_DORMANT,
		NpcID.NIGHTMARE_TOTEM_3_DORMANT,
		NpcID.NIGHTMARE_TOTEM_4_DORMANT
	);

	public static final ImmutableMap<Integer, String> TOTEMS_READY = ImmutableMap.of(
		NpcID.NIGHTMARE_TOTEM_3_READY, "North West",
		NpcID.NIGHTMARE_TOTEM_4_READY, "North East",
		NpcID.NIGHTMARE_TOTEM_1_READY, "South West",
		NpcID.NIGHTMARE_TOTEM_2_READY, "South East"
	);

	private static final Set<Integer> TOTEMS_CHARGED = ImmutableSet.of(
		NpcID.NIGHTMARE_TOTEM_1_CHARGED,
		NpcID.NIGHTMARE_TOTEM_2_CHARGED,
		NpcID.NIGHTMARE_TOTEM_3_CHARGED,
		NpcID.NIGHTMARE_TOTEM_4_CHARGED
	);

	private static final Set<Integer> PHOSANI_ACTIVE = ImmutableSet.of(
		NpcID.NIGHTMARE_CHALLENGE_PHASE_01,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_02,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_03,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_04,
		//NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_01,
		//NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_02,
		//NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_03,
		//NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_04,
		NpcID.NIGHTMARE_CHALLENGE_PHASE_05
	);

	private static final Set<Integer> PHOSANI_INACTIVE = ImmutableSet.of(
		NpcID.NIGHTMARE_CHALLENGE_INITIAL,
		NpcID.NIGHTMARE_CHALLENGE_DYING,
		NpcID.NIGHTMARE_CHALLENGE_DEAD,
		NpcID.NIGHTMARE_CHALLENGE_BLAST
	);

	private static final Set<Integer> PHOSANI_ADDS = ImmutableSet.of(
		NpcID.NIGHTMARE_CHALLENGE_PARASITE,
		NpcID.NIGHTMARE_CHALLENGE_PARASITE_WEAK,
		NpcID.NIGHTMARE_CHALLENGE_HUSK_RANGED,
		NpcID.NIGHTMARE_CHALLENGE_HUSK_MAGIC,
		NpcID.NIGHTMARE_CHALLENGE_SLEEPWALKER
	);

	private static final int HPBAR_HUD = 6099;
	private static final int SPORE_GROWING_ID = 37738;
	private static final int SPORE_OBJECT_ID = 37739;

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

	@Inject
	private PhosaniQolTotemHud totemHud;

	@Getter
	private final Map<Integer, PhosaniTotem> totems = new HashMap<>();
	@Getter
	private PhosaniBoss phosaniBoss = null;
	@Getter
	private final Map<Integer, PhosaniNpc> adds = new HashMap<>();
	@Getter
	private final Set<Spore> spores = new HashSet<>();

	private final Set<Skill> xpDrops = new HashSet<>();

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		overlayManager.add(totemHud);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		overlayManager.remove(totemHud);
		clearAll();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			clearAll();
			overlayManager.remove(overlay);
			overlayManager.remove(totemHud);
		}
		else
		{
			overlayManager.add(overlay);
			overlayManager.add(totemHud);
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

			if (skill != Skill.HITPOINTS) return;

			Player player = client.getLocalPlayer();
			Actor target = player.getInteracting();
			if (target instanceof NPC)
			{
				int npcId = ((NPC) target).getId();
				if (!TOTEMS_READY.containsKey(npcId)) return;

				PhosaniTotem targetTotem = totems.get(npcId);

				int hit = (event.getXp() == 0) ? 1 : (int) Math.round(event.getXp() * (3.0d / 4.0d));
				//int multiplier = (xpDrops.contains(Skill.MAGIC)) ? 2 : 1
				// melee & ranged hits on totems do normal damage but xp gained is halved
				int multiplier = 2;
				int expireTick = attackTick + getHitDelay(player, target);
				targetTotem.addHit(expireTick, hit * multiplier);
				targetTotem.incrementCharge(hit * multiplier);
			}
			xpDrops.clear();
		});
	}

	private int getHitDelay(Player player, Actor target)
	{
		if (target == null)
		{
			return 1;
		}

		if (player == null)
		{
			return 1;
		}

		WorldPoint playerWp = player.getWorldLocation();
		if (playerWp == null)
		{
			return 1;
		}

		WorldArea targetArea = target.getWorldArea();
		if (targetArea == null)
		{
			return 1;
		}

		final int distance = targetArea.distanceTo(playerWp);

		for (AttackType attackType: AttackType.values())
		{
			if (attackType.contains(player.getAnimation()))
			{
				return attackType.apply(distance) + 1;
			}
		}

		return 1;
	}

	/*
	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		if (statChanged.getSkill() != Skill.HITPOINTS) return;

		final int attackTick = client.getTickCount();
		clientThread.invokeLater(() ->
		{
			Player player = client.getLocalPlayer();
			Actor actor = player.getInteracting();
			if (actor instanceof NPC)
			{
				int animationId = client.getLocalPlayer().getAnimation();
				AttackType totemWeapon = SPELL_ANIMATIONS.contains(animationId) ? AttackType.SPELLBOOK : usedWeapon();
				int hitDelay = getHitDelay(totemWeapon, actor);
				int expireTick = attackTick + hitDelay;
				log.info(totemWeapon.getName() + " delay " + hitDelay + " expire " + expireTick);
			}
		});
	}
	 */

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		int hitsplatTick = client.getTickCount();
		clientThread.invokeLater(() ->
		{
			Actor actor = event.getActor();
			if (actor instanceof NPC)
			{
				int npcId = ((NPC) actor).getId();

				//int hitsplatType = event.getHitsplat().getHitsplatType();
				//int hitsplatAmount = event.getHitsplat().getAmount();

				if (!TOTEMS_READY.containsKey(npcId)) return;

				totems.get(npcId).recalculateCharge(hitsplatTick);
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
		if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1 && phosaniBoss != null)
		{
			int shield = client.getVarbitValue(HPBAR_HUD);
			phosaniBoss.setCurrentHealth(shield);
		}
	}

	@Subscribe
	public void onNpcChanged(NpcChanged event)
	{
		NPCComposition oldNpc = event.getOld();
		NPC newNpc = event.getNpc();
		int oldNpcId = oldNpc.getId();
		int newNpcId = newNpc.getId();

		if (TOTEMS_READY.containsKey(newNpcId))
		{
			totems.put(newNpcId, new PhosaniTotem(newNpc, 0, 200, config));
		}
		else if (TOTEMS_CHARGED.contains(newNpcId))
		{
			totems.remove(oldNpcId);
		}
		else if (PHOSANI_ACTIVE.contains(newNpcId))
		{
			if (newNpcId == NpcID.NIGHTMARE_CHALLENGE_PHASE_05)
			{
				phosaniBoss = new PhosaniBoss(newNpc, 150, 150, config);
			}
			else
			{
				phosaniBoss = new PhosaniBoss(newNpc, 400, 400, config);
			}
		}
		else if (PHOSANI_INACTIVE.contains(newNpcId) || PHOSANI_ACTIVE.contains(oldNpcId))
		{
			phosaniBoss = null;
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		int npcId = npc.getId();
		int index = npc.getIndex();
		int key = -(npcId + index);

		if (npcId == NpcID.NIGHTMARE_CHALLENGE_PARASITE || npcId == NpcID.NIGHTMARE_CHALLENGE_PARASITE_WEAK)
		{
			adds.put(key, new Parasite(npc, config));
		}
		else if (npcId == NpcID.NIGHTMARE_CHALLENGE_HUSK_RANGED)
		{
			adds.put(key, new RangedHusk(npc, config));
		}
		else if (npcId == NpcID.NIGHTMARE_CHALLENGE_HUSK_MAGIC)
		{
			adds.put(key, new MagicHusk(npc, config));
		}
		else if (npcId == NpcID.NIGHTMARE_CHALLENGE_SLEEPWALKER)
		{
			adds.put(key, new Sleepwalker(npc, config));
		}
		else if (PHOSANI_INACTIVE.contains(npcId))
		{
			phosaniBoss = null;
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1)
		{
			NPC npc = event.getNpc();
			int npcId = npc.getId();
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
			Widget healthBarWidget = client.getWidget(InterfaceID.HpbarHud.HP);
			if (healthBarWidget != null)
			{
				healthBarWidget.setHidden(config.hideHealthOverlay());
			}

			Widget totemHudWidget = client.getWidget(InterfaceID.NightmareTotems.TOTEMS);
			if (totemHudWidget != null && config.totemHud() != TotemHudStyle.DEFAULT)
			{
				totemHudWidget.setHidden(true);
			}
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1)
		{
			GameObject object = event.getGameObject();

			if (object.getId() == SPORE_GROWING_ID || object.getId() == SPORE_OBJECT_ID)
			{
				spores.add(new Spore(object, config));
			}
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{

		if (client.getVarbitValue(VarbitID.PLAYER_IS_IN_NIGHTMARE_CHALLENGE) == 1)
		{
			GameObject object = event.getGameObject();
			spores.removeIf(spore -> spore.getGameObject() == object);
		}
	}

	private void clearAll()
	{
		totems.clear();
		phosaniBoss = null;
		adds.clear();
		spores.clear();
		xpDrops.clear();
	}

	@Provides
	PhosaniQolConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PhosaniQolConfig.class);
	}
}
