package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

/**
 * Message sent by the client to choose one color
 */
public class SelectedColor extends CharacterCardMessage{
    private final Color color;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param color the color chosen by the player
     */
    public SelectedColor(String nickname, Color color) {
        super(nickname);
        this.color = color;
    }

    /**
     * Gets the color chosen by the player
     * @return the color chosen by the player
     */
    public Color getColor() {
        return color;
    }
}
