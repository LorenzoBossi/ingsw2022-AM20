package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify the merging of two islands
 */
public class MergeIslands implements UpdateMessage {
    private final int currIslandPosition;
    private final int islandToMergePosition;

    /**
     * Constructor
     * @param currIslandPosition the current island position
     * @param islandToMergePosition the position of the island to merge
     */
    public MergeIslands(int currIslandPosition, int islandToMergePosition) {
        this.currIslandPosition = currIslandPosition;
        this.islandToMergePosition = islandToMergePosition;
    }

    /**
     * Gets the current island position
     * @return the current island position
     */
    public int getCurrIslandPosition() {
        return currIslandPosition;
    }

    /**
     * Gets the position of the island to merge
     * @return the position of the island to merge
     */
    public int getIslandToMergePosition() {
        return islandToMergePosition;
    }
}
