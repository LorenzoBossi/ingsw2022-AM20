package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ProfessorManager {
    private List<Integer> maxStudents;
    private List<Player> owners;
    private Comparator<Integer> comparator;
    private Comparator<Integer> standardComparator;

    public ProfessorManager(){
        owners=new ArrayList<>();
        maxStudents=new ArrayList<>();
        for(Color c : Color.values()){
            maxStudents.add(0);
            owners.add(null);
        }
        standardComparator= (x,y)-> x>y ? 1 : 0;
        comparator=standardComparator;
    }

    /**
     * gets the number of students in the dining room of the owner of the professor of the specified color.
     * If the professor is not owned by anyone it returns 0.
     * @param color specifies the color of students and professor.
     * @return a number between 0 and 10 corresponding to the number of students in the dining room of the owner of the professor of the specified color.
     */
    public int getMaxStudentsOf(Color color){
        return maxStudents.get(color.ordinal());
    }


    /**
     * Sets the standard comparator used to compare the number of students of the current player with the owner of professors.
     * The standard comparator's compare(x,y) return 1 if x>y, 0 otherwise.
     */
    private void resetComparator(){
        comparator= standardComparator;
    }


    /**
     * Changes the comparator used to compare the number of students of the current player with the owner of professors.
     * @param comparator specifies which comparator to set.
     */
    public void setComparator(Comparator<Integer> comparator){
        this.comparator=comparator;
    }


    /**
     * Tells if the specified player can take control of the professor of the specified color using the set comparator.
     * @param player specify which player to evaluate
     * @param color specify the color of the professor to evaluate
     * @return {@code true} if they the player can take control of the professor,
     *         {@code false} if they the player can not take control of the professor
     */
    public boolean canTakeProfessor(Player player, Color color){
        Player currentOwner=owners.get(color.ordinal());
        //updating maxStudents
        if(currentOwner!=null){
            maxStudents.set(color.ordinal(),currentOwner.getPlayerBoard().getDiningRoom().getNumberOfStudent(color));
        }

        int studentsOfThePlayer;
        studentsOfThePlayer= player.getPlayerBoard().getDiningRoom().getNumberOfStudent(color);
        if(comparator.compare(studentsOfThePlayer,getMaxStudentsOf(color))==1)
            return true;
        return false;
    }


    /**
     * Sets the specified player as the owner of the professor of the specified color and update the maximum number of students of that color
     * @param player the player to set as the owner
     * @param color the color of the professor
     */
    public void takeProfessor(Player player,Color color){
        Integer studentsOfThePlayer= player.getPlayerBoard().getDiningRoom().getNumberOfStudent(color);
        maxStudents.set(color.ordinal(), studentsOfThePlayer);
        owners.set(color.ordinal(),player);
    }


    /**
     * Returns the list of the colors of the professors owned by the specified player
     * @param player the player to evaluate
     * @return a list of Color corresponding to the owned professors
     */
    public List<Color> getProfessorsOwnedBy(Player player){
        List<Color> professors = new LinkedList<>();
        if(player!=null){
            Color[] colors = Color.values();

            int i = 0;
            for (Player owner : owners) {
                if (player.equals(owner))
                    professors.add(colors[i]);
                i++;
            }
        }
        return professors;
    }


}
