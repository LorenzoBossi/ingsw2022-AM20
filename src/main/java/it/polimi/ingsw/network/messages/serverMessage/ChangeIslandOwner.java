package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify the change of the island owner
 */
public class ChangeIslandOwner implements UpdateMessage{
    private final String newOwner;
    private final String oldOwner;
    private final int islandPosition;
    private final int numberOfTower;

    /**
     * Constructor
     * @param newOwner the new owner
     * @param oldOwner the old owner
     * @param islandPosition the island position
     * @param numberOfTower the number of the tower on the island
     */
    public ChangeIslandOwner(String newOwner, String oldOwner, int islandPosition, int numberOfTower) {
        this.newOwner = newOwner;
        this.oldOwner = oldOwner;
        this.islandPosition = islandPosition;
        this.numberOfTower = numberOfTower;
    }

    /**
     * Gets the new owner
     * @return the new owner
     */
    public String getNewOwner() {
        return newOwner;
    }

    /**
     * Gets the old owner
     * @return the old owner
     */
    public String getOldOwner() {
        return oldOwner;
    }

    /**
     * Gets the island position
     * @return the island position
     */
    public int getIslandPosition() {
        return islandPosition;
    }

    /**
     * Gets the number of tower
     * @return the number of tower
     */
    public int getNumberOfTower() {
        return numberOfTower;
    }
}
