package game.library.loot;

import java.util.Collection;

import game.library.event.GameEvent;

public class LootableDropEvent extends GameEvent {

	private final DropTableHolder holder;
	private final Collection<? extends LootableItem> items;

	public LootableDropEvent(DropTableHolder holder, Collection<? extends LootableItem> items) {
		this.holder = holder;
		this.items = items;
	}

	public DropTableHolder getHolder() {
		return holder;
	}

	public Collection<? extends LootableItem> getItems() {
		return items;
	}

}
