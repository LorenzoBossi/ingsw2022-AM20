package it.polimi.ingsw.network.messages.serverMessage;

public class BanCardEvent implements UpdateMessage {
    int islandPosition;
    private String action;

    public BanCardEvent(int islandPosition, String action) {
        this.islandPosition = islandPosition;
        this.action = action;
    }

    public int getIslandPosition() {
        return islandPosition;
    }
}
