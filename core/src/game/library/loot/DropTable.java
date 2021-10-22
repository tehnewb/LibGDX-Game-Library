package game.library.loot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A DropTable is used to hold any {@code LootableItem} for selection based on
 * their own given chances.
 * 
 * @author Albert Beaupre
 *
 * @param <L> The LootableItem type
 */
public class DropTable<L extends LootableItem> {

	/**
	 * TODO add support for random values such as (Feather drops 1-15 items) or (Air
	 * Rune drops 20-2000)
	 */
	private class LootableItemWrapper {
		private final L parent;
		private final boolean alwaysDrops;

		public LootableItemWrapper(L parent, boolean alwaysDrops) {
			this.parent = parent;
			this.alwaysDrops = alwaysDrops;
		}
	}

	private ArrayList<LootableItemWrapper> drops = new ArrayList<>();
	private final int itemDropFrequency;

	/**
	 * Constructs a new {@code DropTable} with the given {@code itemDropFrequency},
	 * which is the amount of items that can be selected to drop aside from any
	 * items always drop.
	 * 
	 * @param itemDropFrequency the amount of items that can be dropped aside from
	 *                          ALWAYS category
	 */
	public DropTable(int itemDropFrequency) {
		this.itemDropFrequency = itemDropFrequency;
	}

	public <R> Collection<L> selectNextDropItems(R reciever) {
		Collection<L> selected = selectItems();
		LootableItemDropEvent<R> event = new LootableItemDropEvent<R>(selected, reciever);
		event.call();
		if (event.isCancelled()) return Collections.emptyList();
		return selected;
	}

	/**
	 * Adds the given {@code LootableItem} to this {@code DropTable} as a drop that
	 * will <b>always</b> be selected as a drop.
	 * 
	 * @param item the item to add to always drop
	 */
	public DropTable<L> addDropAlways(L item) {
		if (item == null) throw new IllegalArgumentException("Cannot add null item to CustomDropTable");
		drops.add(new LootableItemWrapper(item, true));
		return this;
	}

	/**
	 * Adds the given {@code LootableItem} to this {@code DropTable} to be selected
	 * based on it's chance.
	 * 
	 * @param item the item to add to be selectable for a drop
	 */
	public DropTable<L> add(L item) {
		if (item == null) throw new IllegalArgumentException("Cannot add null item to CustomDropTable");
		drops.add(new LootableItemWrapper(item, false));
		return this;
	}

	/**
	 * Selects any {@code LootableItem} that has been added to this
	 * {@code DropTable} however many times the item drop frequency was defined
	 * based on the chances of those items. This method also adds every item that is
	 * selected to always drop.
	 * 
	 * @return the items to be selected
	 */
	public Collection<L> selectItems() {
		ArrayList<L> selectedItems = new ArrayList<>();
		RoulettePicker<L> table = new RoulettePicker<>();

		for (LootableItemWrapper item : this.drops) {
			if (item.alwaysDrops) {
				selectedItems.add(item.parent);
			} else {
				table.add(item.parent);
			}
		}
		if (table.size() > 0) {
			for (int i = 0; i < this.itemDropFrequency; i++) {
				L nextItem = table.next(false);
				if (nextItem != null) selectedItems.add(nextItem);
			}
		}
		return selectedItems;
	}
}