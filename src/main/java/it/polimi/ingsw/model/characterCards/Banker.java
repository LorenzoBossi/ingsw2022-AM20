package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.ArrayList;
import java.util.List;

public class Banker extends CharacterCard {

    /**
     * Constructor
     */
    public Banker() {
        super(CharacterName.BANKER, 1, CharacterCardType.COLOR_SELECTION);
    }

    /**
     * Removes from every dining room 3 or all the students of the color selected by the current player
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {

        ProfessorManager professorManager = game.getProfessorManager();
        Color selectedColor = game.getCurrPlayer().getPlayerChoice().getSelectedColor();
        Bag bag = game.getBag();
        List<Color> removedStudents = new ArrayList<>();
        int numberOfStudents;
        int i;
        DiningRoom diningRoom;
        List<Player> players = game.getPlayers();


        for (Player p : players) {
            diningRoom = p.getPlayerBoard().getDiningRoom();
            numberOfStudents = diningRoom.getNumberOfStudent(selectedColor);

            if (numberOfStudents > 3)
                numberOfStudents = 3;

            for (i = 0; i < numberOfStudents; i++) {
                diningRoom.removeStudent(selectedColor);
                removedStudents.add(selectedColor);
            }

            notifyObserver(new MoveStudents(GameComponent.DINING_ROOM, GameComponent.BAG, removedStudents, p.getNickname(), null));
            bag.addStudents(removedStudents);
            removedStudents.clear();
        }


        //reset the max number of students of the chosen color
        if (professorManager.ownerOf(selectedColor) != null) {
            professorManager.takeProfessor(professorManager.ownerOf(selectedColor), selectedColor);
        }

        endActivation(game.getCurrPlayer().getNickname());
    }
}

