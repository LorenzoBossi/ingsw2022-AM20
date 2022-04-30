package it.polimi.ingsw.network.messages.clientMessage;

public class GameMessage implements ClientMessage{
    private String nickname;

    public GameMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
