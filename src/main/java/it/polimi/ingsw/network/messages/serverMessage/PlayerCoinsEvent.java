package it.polimi.ingsw.network.messages.serverMessage;

public class PlayerCoinsEvent implements UpdateMessage {
    private int number;

    public PlayerCoinsEvent(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
