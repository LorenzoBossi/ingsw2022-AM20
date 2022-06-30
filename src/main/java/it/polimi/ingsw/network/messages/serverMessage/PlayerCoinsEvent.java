package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the client to notify a coin event
 */
public class PlayerCoinsEvent implements UpdateMessage {
    private final int number;
    private final String nickname;

    /**
     * Constructor
     * @param number the number of coin
     * @param nickname the nickname of the player
     */
    public PlayerCoinsEvent(int number, String nickname) {
        this.number = number;
        this.nickname = nickname;
    }

    /**
     * Gets the nickname of the player
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the number of coins
     * @return number of coins
     */
    public int getNumber() {
        return number;
    }
}
