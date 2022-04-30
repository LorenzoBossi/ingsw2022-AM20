package it.polimi.ingsw.network.messages.clientMessage;

public class NicknameRequest implements ClientMessage {
    private String nickname;

    public NicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
