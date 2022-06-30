package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message sent by the client to choose one island
 */
public class SelectedIsland extends CharacterCardMessage{
    private final int islandPosition;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param islandPosition the island position
     */
    public SelectedIsland(String nickname, int islandPosition) {
        super(nickname);
        this.islandPosition = islandPosition;
    }

    /**
     * Gets the island position
     * @return the island position
     */
    public int getIslandPosition() {
        return islandPosition;
    }
}
