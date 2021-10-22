package game.library.loot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * The RouletteTable is used for a type of Roulette selection between
 * {@code LootableItem} objects.
 * 
 * @author Albert Beaupre
 *
 * @param <L> The DropTableItem type
 */
public class RoulettePicker<L extends LootableItem> {

	private ArrayList<L> loot = new ArrayList<>();
	private double chanceValueTotal;

	/**
	 * Constructs a new {@code RoulettePicker} with the given argument as selectable
	 * loot.
	 * 
	 * @param loot the loot to fill the table with
	 */
	public RoulettePicker(@SuppressWarnings("unchecked") L... loot) {
		for (L l : loot)
			this.add(l);
	}

	/**
	 * Constructs a new {@code RoulettePicker} with the given collection of
	 * {@code LootableItem}.
	 * 
	 * @param collection the collection of lootable items to fill this roulette
	 *                   picker with
	 */
	public RoulettePicker(Collection<L> collection) {
		this.loot = new ArrayList<>(collection);
	}

	/**
	 * Constructs a new, empty {@code RoulettePicker}.
	 */
	public RoulettePicker() {
		this.loot = new ArrayList<>();
	}

	/**
	 * Adds a new {@code LootableItem} object to this {@code RoulettePicker} for
	 * selection.
	 * 
	 * @param item the item to add to the table
	 */
	public void add(L item) {
		if (item.getChance() < 0) throw new IllegalArgumentException("Lootable item cannot have a chance rate of <= 0");
		if (this.loot.add(item)) this.chanceValueTotal += item.getChance();
	}

	/**
	 * Returns the next randomly selected {@code LootableItem} object in the table.
	 * 
	 * @return the selected item
	 */
	public L next(boolean remove) {
		if (loot.isEmpty()) throw new IllegalStateException("No loot available to select: table empty");

		double value = Math.random() * this.chanceValueTotal;

		Iterator<L> iterator = this.loot.iterator();
		while (iterator.hasNext()) {
			L item = iterator.next();
			value -= item.getChance();
			if (value <= 0) {
				if (remove) iterator.remove();
				return item;
			}
		}
		return null;
	}

	/**
	 * Returns the amount of {@code LootableItem} items within this
	 * {@code RoulettePicker}.
	 * 
	 * @return the amount of items
	 */
	public int size() {
		return this.loot.size();
	}
}