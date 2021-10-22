package game.library.loot;

import java.util.Collection;
import java.util.Collections;

/**
 * A DropTableHolder holds a {@code DropTable} for it's own use.
 * 
 * @author Albert Beaupre
 */
public interface DropTableHolder {

	/**
	 * Selects {@code LootableItem} drops from the {@code DropTable} of this
	 * {@code DropTableHolder} and calls the {@code LootableItemDropEvent} once the
	 * items are selected.
	 * 
	 * @return the selected items from the drop table
	 */
	public default <T extends LootableItem> Collection<T> selectNextDropItems() {
		DropTable<T> dropTable = this.getDropTable();
		Collection<T> selected = dropTable.selectItems();
		LootableDropEvent event = new LootableDropEvent(this, selected);
		event.call();
		if (event.isCancelled()) return Collections.emptyList();
		return selected;
	}

	/**
	 * Returns the {@code DropTable} of this {@code DropTableHolder}.
	 * 
	 * @return the table
	 */
	public <T extends LootableItem> DropTable<T> getDropTable();

}
