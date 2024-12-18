package it.polimi.ingsw.client;

/**
 * Class ActionMove enumerates the possible actions of the client
 */
public enum ActionMove {
    MOVE_STUDENTS("ms"),
    MOVE_MOTHER_NATURE("mmn"),
    ACTIVATE_CARD("ac"),
    SELECT_CLOUD("sc"),
    END_TURN("et");

    private final String abbreviation;

    ActionMove(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
