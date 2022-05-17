package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.List;

public class Musician extends CharacterCard {

    private final int MAX_SELECTION = 2;

    /**
     * Constructor
     */
    public Musician() {
        super(CharacterName.MUSICIAN, 1, CharacterCardType.MUSICIAN);
    }


    /**
     * Swap the students selected from the entrance and from the dining room of the current player
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        Player currPlayer = game.getCurrPlayer();
        PlayerChoice playerChoice = currPlayer.getPlayerChoice();
        PlayerBoard playerBoard = currPlayer.getPlayerBoard();
        ProfessorManager profManager = game.getProfessorManager();
        Color entrance;
        Color dining;
        int i;
        int numberOfStudents;

        List<Color> fromEntrance = playerChoice.getSelectedStudentFromEntrance();
        List<Color> fromDiningRoom = playerChoice.getSelectedStudents();

        numberOfStudents = fromEntrance.size();

        for (i = 0; i < numberOfStudents; i++) {
            entrance = fromEntrance.get(i);
            dining = fromDiningRoom.get(i);

            playerBoard.getEntrance().removeStudent(entrance);
            playerBoard.getDiningRoom().removeStudent(dining);

            playerBoard.getEntrance().addStudent(dining);
            playerBoard.getDiningRoom().addStudent(entrance);

            if (profManager.canTakeProfessor(currPlayer, entrance)) {
                profManager.takeProfessor(currPlayer, entrance);
            }
        }

        notifyObserver(new MoveStudents(GameComponent.ENTRANCE, GameComponent.DINING_ROOM, fromEntrance, currPlayer.getNickname(), currPlayer.getNickname()));
        notifyObserver(new MoveStudents(GameComponent.DINING_ROOM, GameComponent.ENTRANCE, fromDiningRoom, currPlayer.getNickname(), currPlayer.getNickname()));

        endActivation(currPlayer.getNickname());
    }

    /**
     * checks if the card contains at least the max selection number of students,
     * if the player selected students from his dining room,
     * if the player selected students from his entrance,
     * if the player selected the right number of students
     *
     * @param currPlayer the current player of the game
     * @return true if the requirements are satisfied
     * false otherwise
     */
    @Override
    public boolean checkRequirements(Player currPlayer) {
        List<Color> selectedInEntrance = currPlayer.getPlayerChoice().getSelectedStudentFromEntrance();
        List<Color> selectedInDiningRoom = currPlayer.getPlayerChoice().getSelectedStudents();
        Entrance entrance = currPlayer.getPlayerBoard().getEntrance();
        DiningRoom diningRoom = currPlayer.getPlayerBoard().getDiningRoom();

        if (selectedInEntrance.size() != selectedInDiningRoom.size())
            return false;
        if (selectedInEntrance.size() > MAX_SELECTION || selectedInEntrance.size() <= 0)
            return false;
        if (!entrance.isPresent(selectedInEntrance))
            return false;
        if (!diningRoom.isPresent(selectedInDiningRoom))
            return false;

        return true;
    }
}
