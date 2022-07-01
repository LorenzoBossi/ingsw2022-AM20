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
        int numberOfIsland = archipelago.getNumberOfIslands();
        int motherNaturePosition = archipelago.getMotherNature();
        Island island = game.getCurrPlayer().getPlayerChoice().getSelectedIsland();
        int selectedIslandId = archipelago.getPositionByIsland(island);

        game.updateInfluence(island);

        if(numberOfIsland != archipelago.getNumberOfIslands()) {
            if(motherNaturePosition > selectedIslandId)
                motherNaturePosition--;
        }
        Island isl = archipelago.getIsland(motherNaturePosition);
        archipelago.moveMotherNatureOnIsland(isl);

        endActivation(game.getCurrPlayer().getNickname());
    }
}

