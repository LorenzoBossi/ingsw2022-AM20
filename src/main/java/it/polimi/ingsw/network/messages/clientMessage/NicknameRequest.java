package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message sent by the client to choose one nickname
 */
public class NicknameRequest implements ClientMessage {
    private final String nickname;

    /**
     * Constructor
     * @param nickname the nickname chosen by the client
     */
    public NicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the nickname chosen by the client
     * @return the nickname chosen by the client
     */
    public String getNickname() {
        return nickname;
    }
}
