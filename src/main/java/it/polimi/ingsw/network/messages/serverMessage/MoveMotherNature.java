package it.polimi.ingsw.network.messages.serverMessage;

public class MoveMotherNature implements UpdateMessage {
    private int newMotherNaturePosition;

    public MoveMotherNature(int newMotherNaturePosition) {
        this.newMotherNaturePosition = newMotherNaturePosition;
    }

    public int getNewMotherNaturePosition() {
        return newMotherNaturePosition;
    }
}
