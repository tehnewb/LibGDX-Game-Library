package game.library.loot;

import java.util.Collection;

import game.library.event.GameEvent;

public class LootableItemDropEvent<R> extends GameEvent {

	private final Collection<? extends LootableItem> items;
	private final R reciever;

	public LootableItemDropEvent(Collection<? extends LootableItem> items, R reciever) {
		this.items = items;
		this.reciever = reciever;
	}

	public Collection<? extends LootableItem> getItems() {
		return items;
	}

	public R getReciever() {
		return reciever;
	}

}