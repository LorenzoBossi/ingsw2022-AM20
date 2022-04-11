package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Jester extends WithStudents{

    /**
     * Constructor
     * @param bag the bag of the game to put students on the card
     */
    public Jester(Bag bag){
        super(1);
        numberOfStudents=6;
        maxSelection=3;
        setStudents(bag.getStudents(getNumberOfStudents()));
    }

    /**
     * Swaps selected students on the card with the selected students in the entrance
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){

        PlayerChoice playerChoice= game.getCurrPlayer().getPlayerChoice();
        Entrance entrance= game.getCurrPlayer().getPlayerBoard().getEntrance();
        int i;
        int numberOfStudents;

        List<Color> fromEntrance=  playerChoice.getSelectedStudentFromEntrance();
        List<Color> students= playerChoice.getSelectedStudents();
        numberOfStudents= fromEntrance.size();

        for(i=0;i<numberOfStudents;i++){
            entrance.removeStudent(fromEntrance.get(i));
            entrance.addStudent(students.get(i));
        }

        remove(students);
        add(fromEntrance);

    }
}
