package library.container;

/**
 * This item is used for the {@code ItemContainer} class.
 *
 * @author Albert Beaupre
 * 
 * @see library.container.ItemContainer
 */
public class Item {

	private final int id;
	private int amount;
	private boolean stackable;

	/**
	 * Constructs a new {@code Item} with the given id having an amount of 1. This
	 * item is not set as stackable.
	 * 
	 * @param id the id of the item
	 */
	public Item(int id) {
		this(id, 1, false);
	}

	/**
	 * Constructs a new {@code Item} with the given id and amount. This item is not
	 * set as stackable.
	 * 
	 * @param id     the id of the item
	 * @param amount the amount of the item
	 */
	public Item(int id, int amount) {
		this(id, amount, false);
	}

	/**
	 * Constructs a new {@code Item} with the given id and amount. The item
	 * stackable flag is set to the given argument.
	 * 
	 * @param id        the id of the item
	 * @param amount    the amount of the item
	 * @param stackable the stackable flag
	 */
	public Item(int id, int amount, boolean stackable) {
		this.id = id;
		this.amount = amount;
		this.stackable = stackable;
	}

	/**
	 * Sets the amount of this item to the given argument and returns itself for
	 * chaining.
	 * 
	 * @param amount the amount to set
	 * @return this item instance
	 */
	public Item amount(int amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Sets the stackable flag to the given argument and returns itself for
	 * chaining.
	 * 
	 * @param stackable the stackable flag to set
	 * @return this item instance
	 */
	public Item stackable(boolean stackable) {
		this.stackable = stackable;
		return this;
	}

	/**
	 * Returns the id of this item.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the amount of this item.
	 * 
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Returns true if this item is stackable, otherwise false is returned
	 * 
	 * @return true if stackable; return false otherwise
	 */
	public boolean isStackable() {
		return stackable;
	}

	@Override
	public String toString() {
		return String.format("Item[Id=%s, Amt=%s, Stackable=%s]", this.id, this.amount, this.stackable);
	}

}
