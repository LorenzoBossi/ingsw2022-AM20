package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;

import java.util.List;

public abstract class CharacterCard {
    private int coinsRequired;
    private boolean firstTimePlayed;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public CharacterCard(int coinsRequired) {
        this.coinsRequired = coinsRequired;
        firstTimePlayed = true;
    }

    /**
     * Method increaseCoinsRequired increases the cost to activate the card
     */
    public void increaseCoinsRequired() {
        coinsRequired++;
    }

    public boolean isFirstTimePlayed() {
        return firstTimePlayed;
    }

    public int getCoinsRequired() {
        return coinsRequired;
    }

    /**
     * Method endActivation increases the cost to activate the card if it's the first time that the card is played
     */
    public void endActivation() {
        if(isFirstTimePlayed()){
            increaseCoinsRequired();
            firstTimePlayed = false;
        }
    }

    public List<Color> getStudents(){return null;}

    public abstract void activateEffect(Game game);

}
