package it.polimi.ingsw.network.messages.serverMessage;

public class PlayerCoinsEvent implements UpdateMessage {
    private int number;
    private String nickname;

    public PlayerCoinsEvent(int number, String nickname) {
        this.number = number;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumber() {
        return number;
    }
}
