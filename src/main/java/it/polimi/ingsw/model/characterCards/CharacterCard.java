package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;

import java.util.List;

public abstract class CharacterCard {
    private CharacterName name;
    private int coinsRequired;
    private boolean firstTimePlayed;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public CharacterCard(CharacterName name, int coinsRequired) {
        this.name = name;
        this.coinsRequired = coinsRequired;
        firstTimePlayed = true;
    }

    public CharacterName getName() {
        return name;
    }

    /**
     * Method increaseCoinsRequired increases the cost to activate the card
     */
    public void increaseCoinsRequired() {
        coinsRequired++;
    }

    public int getCoinsRequired() {
        return coinsRequired;
    }

    /**
     * Method endActivation increases the cost to activate the card if it's the first time that the card is played
     */
    public void endActivation() {
        if(firstTimePlayed){
            increaseCoinsRequired();
            firstTimePlayed = false;
        }
    }

    public List<Color> getStudents(){return null;}

    public abstract void activateEffect(Game game);

}
