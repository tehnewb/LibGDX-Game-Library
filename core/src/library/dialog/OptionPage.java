package library.dialog;

/**
 * The {@code OptionPage} will hold the {@link game.dialog.DialogOption} options
 * in a page and handle the execution of the actions of them.
 * 
 * @author Albert Beaupre
 */
public class OptionPage extends DialogPage {

	/**
	 * The options within this {@code OptionPage}.
	 */
	private final DialogOption[] options;

	/**
	 * Constructs a new {@code OptionPage}.
	 * 
	 * @param title   the title of the page
	 * @param options the options
	 */
	public OptionPage(String title, DialogOption... options) {
		super(title);
		this.options = options;
	}

	/**
	 * Executes the option at the specified {@code optionIndex}.
	 * 
	 * @param optionIndex the index of the option to execute
	 */
	public final void select(int optionIndex) {
		if (options.length <= optionIndex) throw new ArrayIndexOutOfBoundsException("The DialogOption selected does not exist in the page");
		options[optionIndex].getAction().run();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < options.length; index++)
			sb.append(String.format("[%s: %s], ", index, options[index].toString()));
		return sb.toString();
	}
}