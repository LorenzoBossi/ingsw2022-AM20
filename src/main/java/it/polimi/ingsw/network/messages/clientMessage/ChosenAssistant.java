package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.AssistantName;

/**
 * Message used by the client to choose one assistant
 */
public class ChosenAssistant extends GameMessage{
    private final AssistantName name;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param name the name of the assistant
     */
    public ChosenAssistant(String nickname, AssistantName name) {
        super(nickname);
        this.name = name;
    }

    /**
     * Gets the assistant name
     * @return the assistant name
     */
    public AssistantName getName() {
        return name;
    }
}
