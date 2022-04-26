package it.polimi.ingsw.network.messages.serverMessage;

public class MotherNatureMove implements UpdateMessage {
    private int newMotherNaturePosition;

    public MotherNatureMove(int newMotherNaturePosition) {
        this.newMotherNaturePosition = newMotherNaturePosition;
    }

    public int getNewMotherNaturePosition() {
        return newMotherNaturePosition;
    }
}
