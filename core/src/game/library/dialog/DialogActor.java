package game.library.dialog;

/**
 * The {@code DialogActor} is supposed to be any entity that can handle dialog.
 * 
 * @author Albert Beaupre
 */
public interface DialogActor {

	/**
	 * This method is called when the dialog is initialized for the first time.
	 * 
	 * @param dialog the dialog initialized
	 */
	public abstract void initializeDialog(GameDialog dialog);

	/**
	 * Displays the given {@code OptionPage} to the screen of the DialogActor.
	 * 
	 * @param page the page to display
	 */
	public abstract void displayOptions(OptionPage page);

	/**
	 * Displays the given {@code Page} to the screen of the DialogActor.
	 * 
	 * @param page the page to display
	 */
	public abstract void displayDialogPage(DialogPage page);

	/**
	 * Displays the given {@code InformationPage} to the screen of the DialogActor.
	 * 
	 * @param page the page to display
	 */
	public abstract void displayInformation(InformationPage page);

	/**
	 * Executes any actions to exit the dialog, if any, that this
	 * {@code DialogActor} is in.
	 */
	public abstract void exitDialog();

}