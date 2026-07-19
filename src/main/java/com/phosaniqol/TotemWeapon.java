package com.phosaniqol;

import java.util.HashSet;
import java.util.function.Function;
import lombok.Getter;
import net.runelite.api.gameval.ItemID;

@Getter
public enum TotemWeapon
{
	POWERED_STAFF(new int[]{
		ItemID.WARPED_SCEPTRE,
		ItemID.TOTS_CHARGED, ItemID.TOTS_I_CHARGED, ItemID.TOTS_CHARGED_ORN, ItemID.TOTS_I_CHARGED_ORN,
		ItemID.WILD_CAVE_SCEPTRE_CHARGED, ItemID.WILD_CAVE_SCEPTRE_CHARGED_RECOL,
		ItemID.TOXIC_TOTS_CHARGED, ItemID.TOXIC_TOTS_I_CHARGED, ItemID.TOXIC_TOTS_CHARGED_ORN, ItemID.TOXIC_TOTS_I_CHARGED_ORN,
		ItemID.SANGUINESTI_STAFF, ItemID.SANGUINESTI_STAFF_OR
	}, (d) -> 1 + ((1 + d) / 3)),
	EYE_OF_AYAK(new int[]{ItemID.EYE_OF_AYAK}, (d) -> d < 3 ? 1 : 2),
	TUMEKENS_SHADOW(new int[]{ItemID.TUMEKENS_SHADOW}, (d) -> 2 + ((1 + d) / 3)),
	SPELLBOOK(new int[]{}, (d) -> 1 + ((1 + d) / 3)),
	BOW(new int[]{}, (d) -> 1 + ((3 + d) / 6)),
	THROWN(new int[]{}, (d) -> 1 + (d / 6)),
	MELEE(new int[]{}, (d) -> 1);

	private final HashSet<Integer> itemIds;
	private final Function<Integer, Integer> hitDelay;

	TotemWeapon(int[] itemIds, Function<Integer, Integer> hitDelay)
	{
		this.itemIds = new HashSet<>();
		for (Integer itemId: itemIds)
		{
			this.itemIds.add(itemId);
		}
		this.hitDelay = hitDelay;
	}

	public int getHitDelay(final int distance)
	{
		// All attacks have one server cycle of additional delay beyond any projectile travel time for the weapon.
		return getHitDelay().apply(distance) + 1;
	}

	public boolean contains(int itemId)
	{
		return this.itemIds.contains(itemId);
	}
}
