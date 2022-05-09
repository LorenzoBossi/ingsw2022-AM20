package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.ArrayList;
import java.util.List;

public class Jester extends WithStudents {
    /*
    private final int NUMBER_OF_STUDENT = 6;
    private final int MAX_SELECTION = 3;
    */

    /**
     * Constructor
     *
     * @param bag the bag of the game to put students on the card
     */
    public Jester(Bag bag) {
        super(CharacterName.JESTER, 1, CharacterCardType.JESTER, 3);
        List<Color> students;
        setStudents(bag.getStudents(6));
    }

    /**
     * Swaps selected students on the card with the selected students in the entrance
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        Player currPlayer = game.getCurrPlayer();
        PlayerChoice playerChoice = currPlayer.getPlayerChoice();
        Entrance entrance = currPlayer.getPlayerBoard().getEntrance();
        int i;
        int numberOfStudents;

        List<Color> fromEntrance = playerChoice.getSelectedStudentFromEntrance();
        List<Color> students = playerChoice.getSelectedStudents();
        numberOfStudents = fromEntrance.size();

        for (i = 0; i < numberOfStudents; i++) {
            entrance.removeStudent(fromEntrance.get(i));
            entrance.addStudent(students.get(i));
        }

        remove(students);
        add(fromEntrance);

        notifyObserver(new MoveStudents(GameComponent.ENTRANCE, GameComponent.CARD, fromEntrance, currPlayer.getNickname(), getName().name()));
        notifyObserver(new MoveStudents(GameComponent.CARD, GameComponent.ENTRANCE, students, getName().name(), currPlayer.getNickname()));
        endActivation(currPlayer.getNickname());
    }

    /**
     * checks if the card contains at least the max selection number of students,
     *        if the player selected the right number of students and if those students are on the card,
     *        if the player selected students in his entrance
     * @param currPlayer the current player of the game
     * @return true if the requirements are satisfied
     *         false otherwise
     */
    @Override
    public boolean checkRequirements(Player currPlayer) {
        PlayerChoice playerChoice=currPlayer.getPlayerChoice();
        List<Color> selectedOnTheCard=playerChoice.getSelectedStudents();
        Entrance entrance=currPlayer.getPlayerBoard().getEntrance();
        List<Color> selectedInEntrance=playerChoice.getSelectedStudentFromEntrance();

        if(!super.checkRequirements(currPlayer))
            return false;

        if(selectedOnTheCard.size()!=selectedInEntrance.size())
            return false;
        if(!entrance.isPresent(selectedInEntrance))
            return false;

        return true;
    }



}
