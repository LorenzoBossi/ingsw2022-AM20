package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server when there is a ban card event
 */
public class BanCardEvent implements UpdateMessage {
    private final int islandPosition;
    private final String action;

    /**
     * Constructor
     * @param islandPosition the island position
     * @param action the add or remove action of the ban card
     */
    public BanCardEvent(int islandPosition, String action) {
        this.islandPosition = islandPosition;
        this.action = action;
    }

    /**
     * Gets the island position
     * @return the island position
     */
    public int getIslandPosition() {
        return islandPosition;
    }

    /**
     * Gets the add or remove action of the ban card
     * @return the add or remove action of the ban card
     */
    public String getAction() {
        return action;
    }
}
