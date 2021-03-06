package game.library.dialog;

import java.util.Objects;

/**
 * The {@code Page} class represents a page within a dialog (written
 * conversation). This class is used by the {@link game.GameDialog.Dialog} class
 * to create a written conversation used by NPCs or Players within the game.
 * 
 * @author Albert Beaupre
 */
public class DialogPage {

	/**
	 * This is the text displayed on this {@code Page}.
	 */
	private final String text;

	/**
	 * This is the id of the npc that is talking. If an NPC is not talking and a
	 * Player is, then this value is set to -1.
	 * 
	 * <p>
	 * <b> This value is set to -1 by default.</b>
	 */
	private int npcId = -1;

	/**
	 * This is an action taken once this page has been opened
	 */
	private Runnable action;

	/**
	 * Constructs a new {@code Page} from the specified {@code text}. If the text is
	 * null or has no length to it, then the text value is set to "..."
	 * 
	 * @param text the text displayed on this page
	 */
	public DialogPage(String text) {
		if (Objects.isNull(text) || text.isEmpty()) text = "...";
		this.text = text;
	}

	/**
	 * Sets the given action to this page
	 * 
	 * @param action the action to set
	 * @return this page for chaining
	 */
	public DialogPage action(Runnable action) {
		this.action = action;
		return this;
	}

	/**
	 * Sets the npc id of this page.
	 * 
	 * @param npcId the npc id to set
	 * @return this page for chaining
	 */
	public DialogPage npcId(int npcId) {
		this.npcId = npcId;
		return this;
	}

	/**
	 * Returns the action of this page
	 * 
	 * @return the action
	 */
	public Runnable getAction() {
		return action;
	}

	/**
	 * Returns the npc ID of the npc face to display on the dialog. If the id is -1,
	 * then the player face is displayed.
	 * 
	 * @return the npc id
	 */
	public int getNPCId() {
		return npcId;
	}

	@Override
	public String toString() {
		return "Page Text: " + text;
	}

}