package it.polimi.ingsw.network.messages.serverMessage;

public class NextMove implements ServerMessage {
    private String currPlayer;

    public NextMove(String currPlayer) {
        this.currPlayer = currPlayer;
    }

    public String getCurrPlayer() {
        return currPlayer;
    }
}
