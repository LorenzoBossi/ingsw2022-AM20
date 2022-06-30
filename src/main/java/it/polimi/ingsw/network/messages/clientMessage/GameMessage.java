package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message send by the client during the game
 */
public class GameMessage implements ClientMessage{
    private final String nickname;

    /**
     * Constructor
     * @param nickname the nickname of the player
     */
    public GameMessage(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the player nickname
     * @return the player nickname
     */
    public String getNickname() {
        return nickname;
    }
}
