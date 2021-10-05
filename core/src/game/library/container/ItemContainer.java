package game.library.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This is a container that holds a definite amount of items given by the
 * specified capacity set on the constructor. This class supports functions such
 * a adding, removing, shifting, and checking items. Items can individually be
 * selected and set as well.
 * 
 * @author Albert Beaupre
 * 
 * @see game.library.container.Item
 */
public class ItemContainer {

	private Item[] items;
	private final int capacity;
	private int size;

	/**
	 * Constructs a new {@code ItemContainer} with a definite capacity of items
	 * available to add specified with the given argument.
	 * 
	 * @param capacity the definite capacity
	 */
	public ItemContainer(int capacity) {
		this.capacity = capacity;
		this.items = new Item[capacity];
	}

	/**
	 * Transfer all items from this {@code ItemContainer} to the given
	 * {@code toTransfer} container.
	 * 
	 * @param toTransfer the container to transfer to
	 * @throws NullPointerException if toTransfer is null
	 */
	public void transferToContainer(ItemContainer toTransfer) {
		if (toTransfer == null) throw new NullPointerException("NULL ItemContainer cannot be transfered");

		for (int index = 0; index < this.capacity; index++) {
			Item item = this.get(index);
			if (item == null) continue;
			this.set(index, null);
			toTransfer.addItem(item);
		}
	}

	/**
	 * Adds the given {@code item} to this {@code ItemContainer}. If the item is
	 * stackable, then the item amount is just added to an existing item with the
	 * same id, otherwise it takes a new slot.
	 * 
	 * @param item the item to add
	 * @return true if the item was added; return false otherwise
	 * @throws NullPointerException if the item argument is null
	 */
	public boolean addItem(Item item) {
		if (item == null) throw new NullPointerException("NULL Item cannot be added to ItemContainer");

		int searchIndex = this.indexOf(item.getId());
		if (searchIndex == -1) { // this container does not have an item with that ID
			if (!this.hasEmptySlots()) { // checking if this container has any more slots available for a new item
				return false; // cannot add anymore items due to capacity of container
			}
			int freeIndex = this.getFreeIndex(); // find a free index to place the new item at
			this.items[freeIndex] = item; // set the item in the array to the given item argument
			this.size++; // increase size of item count
		} else { // this container has an item with that id, next we need to check if it is stackable
			if (item.isStackable()) { // checking if item is stackable
				Item currentItem = this.get(searchIndex); // use the index found with the same item id
				currentItem.amount(currentItem.getAmount() + item.getAmount()); // add the amount of the current item plus the adding item
			} else {
				if (!this.hasEmptySlots()) { // checking if this container has any more slots available for a new item
					return false; // cannot add anymore items due to capacity of container
				}
				int freeIndex = this.getFreeIndex(); // find a free index to place the new item at
				this.items[freeIndex] = item; // set the item in the array to the given item argument
				this.size++; // increase size of item count
			}
		}
		return true;
	}

	/**
	 * Removes the given {@code item} from this [{@code ItemContainer}. If this item
	 * does not exist, then false is returned as it is an unsuccessful removal. If
	 * the item is stackable and an item with the same id already exists, then the
	 * item amount is removed from the existing item.
	 * 
	 * @param item the item to remove
	 * @return true if item was removed; return false otherwise
	 * @throws NullPointerException if the item argument is null
	 */
	public boolean removeItem(Item item) {
		if (item == null) throw new NullPointerException("NULL Item cannot be removed from ItemContainer");

		int searchIndex = this.indexOf(item.getId());
		if (searchIndex == -1) { // check if the item id is found within the container
			return false; // no item is found within the container to remove
		} else { // item has been found with a returned search index
			if (item.isStackable()) { // if item is stackable, we just want to remove the amount of given item
				Item currentItem = this.get(searchIndex); // use the index found with the same item id
				currentItem.amount(currentItem.getAmount() - item.getAmount());
				if (currentItem.getAmount() <= 0) { // check if the item still has an amount available to it, if not, then we want it removed
					this.items[searchIndex] = null; // remove item from container completely
					this.size--; // decrease size of item count
				}
			} else {
				this.items[searchIndex] = null; // remove item from container completely
				this.size--; // decrease size of item count
			}
		}
		return true;
	}

	/**
	 * Swaps the items at the fromIndex and toIndex.
	 * 
	 * @param fromIndex the index to swap from
	 * @param toIndex   the index to swap to
	 */
	public void swap(int fromIndex, int toIndex) {
		Item old = this.get(toIndex);
		this.set(toIndex, get(fromIndex));
		this.set(fromIndex, old);
	}

	/**
	 * Sets the given item at the specified index.
	 * 
	 * @param index the index to set the item at
	 * @param item  the item to set
	 * @return the previous item at the index
	 */
	public Item set(int index, Item item) {
		Item oldItem = this.items[index];
		this.items[index] = item;
		if (item == null && oldItem != null) { // check if new item we're setting is null and the old item isn't
			this.size--; // since the new item is null, this is considered removal of the item at the given index, so we decrease item count
		}

		if (item != null && oldItem == null) { // check if new item isn't null and old item is
			this.size++; // since the new item isn't null, this is considered adding a new item, so we increase the item count
		}
		return oldItem;
	}

	/**
	 * Returns the item in this {@code ItemContainer} at the given index. If there
	 * is no item at the given index, then null is returned.
	 * 
	 * @param index the index of the item
	 * @return the item at the given index
	 */
	public Item get(int index) {
		return this.items[index];
	}

	/**
	 * Sorts this {@code ItemContainer} to rearrange the items based on the given
	 * comparator.
	 * 
	 * @param comparator the comparator used for sorting
	 */
	public void sort(Comparator<Item> comparator) {
		this.shift();
		int size = this.size - this.getFreeSlots();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (comparator.compare(this.get(i), this.get(j)) < 0) {
					Item tmp = this.get(j);
					this.set(j, this.get(i));
					this.set(i, tmp);
				}
			}
		}
	}

	/**
	 * Shifts all items within this {@code ItemContainer} to the top of the
	 * container (array).
	 */
	public void shift() {
		ArrayList<Item> shifted = new ArrayList<Item>();
		Arrays.asList(items).stream().filter(n -> n != null).forEach(n -> shifted.add(n));
		this.items = shifted.toArray(new Item[capacity]);
	}

	/**
	 * Returns the index of the first item with the given {@code itemId}. If no item
	 * is found with that id, then -1 is returned.
	 * 
	 * @param itemId the item id to search
	 * @return the index of the item; return -1 if not found
	 */
	public int indexOf(int itemId) {
		for (int index = 0; index < capacity; index++) {
			Item item = this.items[index];
			if (item == null) continue;
			if (item.getId() == itemId) return index;
		}
		return -1;
	}

	/**
	 * Returns the first available index within this {@code ItemContainer}. If no
	 * index is available, then -1 is returned.
	 * 
	 * @return the first available index; return -1 is none are available
	 */
	public int getFreeIndex() {
		for (int index = 0; index < capacity; index++) {
			Item item = this.items[index];
			if (item == null) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the amount of free slots available to use within this
	 * {@code ItemContainer}.
	 * 
	 * @return amount of free slots
	 */
	public int getFreeSlots() {
		return this.capacity - this.size;
	}

	/**
	 * Returns true if this {@code ItemContainer} has any empty slots available for
	 * use.
	 * 
	 * @return true if empty space; return false otherwise
	 */
	public boolean hasEmptySlots() {
		return this.size < this.capacity;
	}

	/**
	 * Returns the array of items used within this {@code ItemContainer}.
	 * 
	 * @return the array of items
	 */
	public Item[] getItems() {
		return this.items;
	}

	@Override
	public String toString() {
		return Arrays.toString(getItems());
	}

}
