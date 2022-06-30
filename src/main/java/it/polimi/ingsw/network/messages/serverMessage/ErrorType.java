package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Enum with all the different type of error
 */
public enum ErrorType {
    NICKNAME_ALREADY_TAKEN,
    LOBBY_ERROR,
    NOT_YOUR_TURN,
    ASSISTANT_NOT_PLAYABLE,
    INVALID_INPUT,
    MOTHER_NATURE_MOVE_INVALID,
    SELECTED_CLOUD_ERROR,
    CARD_REQUIREMENTS_ERROR,
}
