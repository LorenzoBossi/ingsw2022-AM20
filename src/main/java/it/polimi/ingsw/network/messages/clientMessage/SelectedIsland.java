package it.polimi.ingsw.network.messages.clientMessage;

public class SelectedIsland extends CharacterCardMessage{
    private int islandPosition;

    public SelectedIsland(String nickname, int islandPosition) {
        super(nickname);
        this.islandPosition = islandPosition;
    }

    public int getIslandPosition() {
        return islandPosition;
    }
}
