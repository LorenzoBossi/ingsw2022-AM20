package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify the mother nature movement
 */
public class MotherNatureMove implements UpdateMessage {
    private final int newMotherNaturePosition;

    /**
     * Constructor
     * @param newMotherNaturePosition the new mother nature position
     */
    public MotherNatureMove(int newMotherNaturePosition) {
        this.newMotherNaturePosition = newMotherNaturePosition;
    }

    /**
     * Gets the new mother nature position
     * @return the new mother nature position
     */
    public int getNewMotherNaturePosition() {
        return newMotherNaturePosition;
    }
}
