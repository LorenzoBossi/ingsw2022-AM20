package it.polimi.ingsw.network.messages.serverMessage;

public class GameError implements ServerMessage {
    private ErrorType errorType;
    private String errorText;

    public GameError(ErrorType errorType, String errorText) {
        this.errorType = errorType;
        this.errorText = errorText;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getErrorText() {
        return errorText;
    }
}
