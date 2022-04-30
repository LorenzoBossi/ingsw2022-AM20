package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

public class SelectedColor extends CharacterCardMessage{
    private Color color;

    public SelectedColor(String nickname, Color color) {
        super(nickname);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
