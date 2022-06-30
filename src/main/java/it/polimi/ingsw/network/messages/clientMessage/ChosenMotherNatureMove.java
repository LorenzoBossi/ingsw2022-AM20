package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message sent by the client to choose how much he wants to move mother nature
 */
public class ChosenMotherNatureMove extends ActionPhaseMessage {
    private final int motherNatureMove;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param motherNatureMove how much the client wants to move mother nature
     */
    public ChosenMotherNatureMove(String nickname, int motherNatureMove) {
        super(nickname);
        this.motherNatureMove = motherNatureMove;
    }

    /**
     * Gets how much the client wants to move mother nature
     * @return how much the client wants to move mother nature
     */
    public int getMotherNatureMove() {
        return motherNatureMove;
    }
}
