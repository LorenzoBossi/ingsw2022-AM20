package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsManager;

public class Vassal extends CharacterCard {

    /**
     * Constructor
     */
    public Vassal() {
        super(CharacterName.VASSAL, 3, CharacterCardType.ISLAND_SELECTION);
    }


    /**
     * Method activateEffect calls the Game's method updateInfluence on the player's selected island
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        IslandsManager archipelago = game.getArchipelago();
        int motherNaturePosition = archipelago.getMotherNature();
        Island isl = archipelago.getIsland(motherNaturePosition);
        Island island = game.getCurrPlayer().getPlayerChoice().getSelectedIsland();

        game.updateInfluence(island);
        archipelago.moveMotherNatureOnIsland(isl);

        endActivation(game.getCurrPlayer().getNickname());
    }
}

