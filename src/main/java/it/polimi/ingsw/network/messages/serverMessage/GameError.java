package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify one error to the client
 */
public class GameError implements ServerMessage {
    private final ErrorType errorType;
    private final String errorText;

    /**
     * Constructor
     * @param errorType the type of the error
     * @param errorText the text of the error
     */
    public GameError(ErrorType errorType, String errorText) {
        this.errorType = errorType;
        this.errorText = errorText;
    }

    /**
     * Gets the type of the error
     * @return the type of the error
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Gets the text of the error
     * @return the text of the error
     */
    public String getErrorText() {
        return errorText;
    }
}
