package game.library.dialog;

import java.util.Objects;

/**
 * The {@code DialogOption} class represents an entityOption to be chosen within
 * a OptionPage. An action must be set to this {@code DialogOption} so it may be
 * executed upon this being selected in an option page
 * 
 * @author Albert Beaupre
 */
public class DialogOption {

	/**
	 * This is the name displayed for the entityOption name
	 */
	private final String name;

	/**
	 * This is the action taken when the entityOption is selected, which cannot be
	 * null. It must either end or continue the dialog in some way.
	 */
	private final Runnable action;

	/**
	 * Constructs a new {@code DialogOption} with the entityOption name as the
	 * specified {@code name} and uses the specified {@code action} to execute when
	 * the entityOption is selected.
	 * 
	 * @param name   the entityOption name
	 * @param action the action taken when the entityOption is selected
	 */
	public DialogOption(String name, Runnable action) {
		this.name = name;
		this.action = Objects.requireNonNull(action, "The action of the entityOption cannot equal null");
	}

	/**
	 * Returns the action of this option
	 * 
	 * @return the action
	 */
	public Runnable getAction() {
		return action;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

}