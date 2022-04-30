package it.polimi.ingsw.network.messages.clientMessage;

public class ChosenMotherNatureMove extends ActionPhaseMessage {
    private int motherNatureMove;

    public ChosenMotherNatureMove(String nickname, int motherNatureMove) {
        super(nickname);
        this.motherNatureMove = motherNatureMove;
    }

    public int getMotherNatureMove() {
        return motherNatureMove;
    }
}
