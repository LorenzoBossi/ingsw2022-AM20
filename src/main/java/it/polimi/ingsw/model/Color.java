package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.Banker;

/**
 * Enumeration of the colors of the students
 */
public enum Color {
    YELLOW,
    BLUE,
    GREEN,
    RED,
    PINK;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[93m";//"\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PINK = "\u001B[95m";

    /**
     * Override of the method toString in order to print colored text
     * @return
     */
    @Override
    public String toString() {
        String ANSI_COLOR=ANSI_RESET;
        switch (this) {
            case YELLOW:
                ANSI_COLOR = ANSI_YELLOW;
                break;
            case BLUE:
                ANSI_COLOR = ANSI_BLUE;
                break;
            case GREEN:
                ANSI_COLOR = ANSI_GREEN;
                break;
            case RED:
                ANSI_COLOR = ANSI_RED;
                break;
            case PINK:
                ANSI_COLOR = ANSI_PINK;
                break;
        }
        return ANSI_COLOR + super.toString() + ANSI_RESET;
    }

}
