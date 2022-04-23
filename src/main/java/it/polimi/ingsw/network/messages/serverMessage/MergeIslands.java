package it.polimi.ingsw.network.messages.serverMessage;

public class MergeIslands implements UpdateMessage {
    private int currIslandPosition;
    private int islandToMergePosition;
    private int newMotherNaturePosition;

    public MergeIslands(int currIslandPosition, int islandToMergePosition, int newMotherNaturePosition) {
        this.currIslandPosition = currIslandPosition;
        this.islandToMergePosition = islandToMergePosition;
        this.newMotherNaturePosition = newMotherNaturePosition;
    }

    public int getCurrIslandPosition() {
        return currIslandPosition;
    }

    public int getIslandToMergePosition() {
        return islandToMergePosition;
    }
}
