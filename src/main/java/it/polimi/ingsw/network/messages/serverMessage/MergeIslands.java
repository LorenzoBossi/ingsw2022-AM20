package it.polimi.ingsw.network.messages.serverMessage;

public class MergeIslands implements UpdateMessage {
    private int currIslandPosition;
    private int islandToMergePosition;

    public MergeIslands(int currIslandPosition, int islandToMergePosition) {
        this.currIslandPosition = currIslandPosition;
        this.islandToMergePosition = islandToMergePosition;
    }

    public int getCurrIslandPosition() {
        return currIslandPosition;
    }

    public int getIslandToMergePosition() {
        return islandToMergePosition;
    }
}
