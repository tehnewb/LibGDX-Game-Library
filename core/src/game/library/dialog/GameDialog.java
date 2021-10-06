package game.library.dialog;

import java.util.ArrayDeque;

/**
 * The {@code Dialog} class is used to create a conversation between any player
 * or NPC using the DialogPage to create pages as a written conversation.
 * 
 * @author Albert Beaupre
 */
public class GameDialog {

	/**
	 * A queue containing any upcoming {@code Page}.
	 */
	protected final ArrayDeque<DialogPage> pages = new ArrayDeque<>();

	/**
	 * The transactor that will converse within this dialog.
	 */
	protected final DialogHandler transactor;

	private DialogPage currentPage;
	private boolean finished;

	/**
	 * Constructs a new {@code Dialog} based on the {@code DialogTransactor}.
	 * 
	 * @param transactor the transactor
	 */
	public GameDialog(DialogHandler transactor) {
		this.transactor = transactor;
	}

	/**
	 * 
	 * Displays 2 options with 2 actions.
	 */
	public GameDialog options(String optionName, Runnable i, String optionName2, Runnable i2) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		pages.add(new OptionPage("Choose an Option", option1, option2));
		return this;
	}

	/**
	 * 
	 * Displays 3 options with 3 actions.
	 */
	public GameDialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		DialogOption option3 = new DialogOption(optionName3, i3);
		pages.add(new OptionPage("Choose an Option", option1, option2, option3));
		return this;
	}

	/**
	 * 
	 * Displays 4 options with 4 actions.
	 */
	public GameDialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3, String optionName4, Runnable i4) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		DialogOption option3 = new DialogOption(optionName3, i3);
		DialogOption option4 = new DialogOption(optionName4, i4);
		pages.add(new OptionPage("Choose an Option", option1, option2, option3, option4));
		return this;
	}

	/**
	 * Displays 5 options with 5 actions.
	 */
	public GameDialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3, String optionName4, Runnable i4, String optionName5, Runnable i5) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		DialogOption option3 = new DialogOption(optionName3, i3);
		DialogOption option4 = new DialogOption(optionName4, i4);
		DialogOption option5 = new DialogOption(optionName5, i5);
		pages.add(new OptionPage("Choose an Option", option1, option2, option3, option4, option5));
		return this;
	}

	/**
	 * Writes an action to the last {@code Page} written to this {@code Dialog}.
	 * 
	 * @param consumer the action to execute when the {@code Page} is opened.
	 * @return a chain of this instance
	 */
	public GameDialog then(Runnable action) {
		pages.peekLast().action(action);
		return this;
	}

	/**
	 * Displays an {@code InformationPage} with the specified {@code text}.
	 * 
	 * @param the text to display on the page
	 * @return a chain of this instance
	 */
	public GameDialog info(String text) {
		pages.add(new InformationPage(text));
		return this;
	}

	/**
	 * Adds an npc text page to this dialog.
	 * 
	 * @param text the text of the npc
	 */
	public GameDialog npc(int npcId, String text) {
		DialogPage page = new DialogPage(text);
		page.npcId(npcId);
		pages.add(page);
		return this;
	}

	/**
	 * Writes a {@code Page} to this {@code Dialog} that is meant to be written for
	 * a player to speak.
	 * 
	 * @param expression the expression id value of the player speaking
	 * @param name       the name that is spoken
	 * @return a chain of this instance
	 */
	public GameDialog player(String text) {
		DialogPage page = new DialogPage(text);
		page.npcId(-1);
		pages.add(page);
		return this;
	}

	/**
	 * Selects the entityOption from an {@code OptionPage} and executes the action
	 * within the entityOption.
	 * 
	 * @param index the index of the entityOption to select
	 * 
	 * @throws UnsupportedOperationException if an {@code OptionPage} is not the
	 *                                       current page or if the specified index
	 *                                       of the entityOption is not available
	 * @return a chain of this instance
	 */
	public GameDialog selectOption(int index) {
		DialogPage last = pages.poll();
		if (!(last instanceof OptionPage)) throw new UnsupportedOperationException("The page selected is not an OptionPage");
		OptionPage page = (OptionPage) last;
		page.select(index);
		if (pages.isEmpty()) {
			finish();
			transactor.exitDialog();
			return null;
		}
		return this;
	}

	/**
	 * Starts this {@code Dialog}.
	 */
	public void start() {
		transactor.initializeDialog(this);
	}

	/**
	 * Continues the dialog to the next page.
	 * 
	 * <p>
	 * The {@code Page} returned is the next page within this {@code Dialog}, if
	 * existing, If there is not another page, the
	 * {@link DialogHandler#exitDialog()} method is called and the dialog is stopped
	 * and null is returned.
	 * 
	 * @return the next page; return null otherwise
	 */
	public DialogPage continueDialog() {
		if (this.finished) return null;

		if (pages.isEmpty()) {
			finish();
			transactor.exitDialog();
			return null;
		}

		DialogPage page = pages.poll();
		this.currentPage = page;
		if (page instanceof InformationPage) {
			transactor.displayInformation((InformationPage) page);
		} else if (page instanceof OptionPage) {
			transactor.displayOptions((OptionPage) page);
		} else {
			transactor.displayDialogPage(page);
		}

		if (page.getAction() != null) page.getAction().run();

		return page;
	}

	/**
	 * Finalizes anything with this {@code Dialog}.
	 */
	public final void finish() {
		this.pages.clear();
		this.finished = true;
	}

	/**
	 * Returns the current dialog page that this dialog is on.
	 * 
	 * @return the current page
	 */
	public DialogPage getCurrentPage() {
		return this.currentPage;
	}

}