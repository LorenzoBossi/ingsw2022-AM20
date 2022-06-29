package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.serverMessage.CardActivated;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.serverMessage.IncreaseCardPrice;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class which define the common structure of every character card
 */
public abstract class CharacterCard extends ObservableSubject {
    private CharacterName name;
    private int coinsRequired;
    private boolean firstTimePlayed;
    private CharacterCardType characterCardType;

    /**
     * Constructor
     * @param name the CharacterName of the card to create
     * @param coinsRequired the coins required to activate the card
     * @param characterCardType the CharacterCardType of the card to create
     */
    public CharacterCard(CharacterName name, int coinsRequired, CharacterCardType characterCardType) {
        this.characterCardType = characterCardType;
        this.name = name;
        this.coinsRequired = coinsRequired;
        firstTimePlayed = true;
    }

    /**
     * Gets the name of the card
     * @return CharacterName of the card
     */
    public CharacterName getName() {
        return name;
    }

    /**
     * Method increaseCoinsRequired increases the cost to activate the card
     */
    public void increaseCoinsRequired() {
        coinsRequired++;
    }

    /**
     * gets the price of the card
     * @return the price of the card
     */
    public int getCoinsRequired() {
        return coinsRequired;
    }

    /**
     * Method endActivation increases the cost to activate the card if it's the first time that the card is played
     */
    public void endActivation(String player) {
        if (firstTimePlayed) {
            increaseCoinsRequired();
            firstTimePlayed = false;
            notifyObserver(new IncreaseCardPrice(getName()));
        }
        notifyObserver(new CardActivated(player, name));
    }

    /**
     * Gets the students on the card if present.
     * @return a list of color if the card has students on it, null otherwise
     */
    public List<Color> getStudents() {
        return null;
    }

    /**
     * gets the type of character card
     * @return a CharacterCardType
     */
    public CharacterCardType getCharacterCardType() {
        return characterCardType;
    }

    /**
     * checks if the card is playable and if the students selected by the player are correct (if required by the card)
     * @param currPlayer the Player who wants to activate the card (the current player of the game)
     * @return  true if the requirements are satisfied
     *          false if the requirements are not satisfied
     */
    public boolean checkRequirements(Player currPlayer){return true;}

    /**
     * Apply the effect of the card when a player activate it
     * @param game the game in which is activated
     */
    public abstract void activateEffect(Game game);

}
