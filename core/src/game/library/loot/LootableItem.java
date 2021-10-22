package game.library.loot;

import game.library.container.Item;

/**
 * Represents an item used to be selected for loot.
 * 
 * @author Albert Beaupre
 */
public class LootableItem extends Item {

	private final int itemId;
	private final double chance;

	private final int minAmount, maxAmount;

	/**
	 * Constructs a new {@code LootableItem} with a given chance.
	 * 
	 * @param itemId the id of the item
	 * @param amount the amount of the item
	 * @param chance the chance of the item to be recieved
	 */
	public LootableItem(int itemId, int amount, double chance) {
		super(itemId, amount);
		this.itemId = itemId;
		this.minAmount = amount;
		this.maxAmount = amount;
		this.chance = chance;
	}

	/**
	 * Constructs a new {@code LootableItem} with a given chance.
	 * 
	 * @param itemId the id of the item
	 * @param amount the amount of the item
	 * @param chance the chance of the item to be recieved
	 */
	public LootableItem(int itemId, int minAmount, int maxAmount, double chance) {
		super(itemId, minAmount + (int) (maxAmount * Math.random()));
		this.itemId = itemId;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.chance = chance;
	}

	/**
	 * Constructs a new {@code LootableItem} with a chance of 100%.
	 * 
	 * @param itemId the id of the item
	 * @param amount the amount of the item
	 */
	public LootableItem(int itemId, int amount) {
		this(itemId, amount, 100);
	}

	public int getItemId() {
		return itemId;
	}

	public double getChance() {
		return chance;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}
}