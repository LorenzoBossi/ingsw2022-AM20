package it.polimi.ingsw.network.messages.serverMessage;

public class ChangeIslandOwner implements UpdateMessage{
    private String newOwner;
    private String oldOwner;
    private int islandPosition;
    private int numberOfTower;

    public ChangeIslandOwner(String newOwner, String oldOwner, int islandPosition, int numberOfTower) {
        this.newOwner = newOwner;
        this.oldOwner = oldOwner;
        this.islandPosition = islandPosition;
        this.numberOfTower = numberOfTower;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public String getOldOwner() {
        return oldOwner;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public int getNumberOfTower() {
        return numberOfTower;
    }
}
