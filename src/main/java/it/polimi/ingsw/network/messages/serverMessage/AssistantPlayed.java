package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.AssistantName;

/**
 * Message sent by the server to notify the client the assistant played by one player
 */
public class AssistantPlayed implements UpdateMessage {
    private final String player;
    private final String assistantName;

    /**
     * Constructor
     * @param player the player that played the assistant
     * @param assistantName the name of the assistant
     */
    public AssistantPlayed(String player, AssistantName assistantName) {
        this.player = player;
        this.assistantName = assistantName.name();
    }

    /**
     * Gets the player that played the assistant
     * @return the player that played the assistant
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Gets the assistant name
     * @return the assistant name
     */
    public String getAssistantName() {
        return assistantName;
    }
}
