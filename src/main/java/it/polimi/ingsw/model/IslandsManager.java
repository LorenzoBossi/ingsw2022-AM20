package it.polimi.ingsw.model;

import it.polimi.ingsw.network.messages.serverMessage.*;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.*;

/**
 * Contains every island of the game and manages every operation regarding them, like unite multiple islands.
 */
public class IslandsManager extends ObservableSubject {
    private List<Island> islands;
    private int motherNature;
    private EndGameObserver endGameObserver;

    /**
     * Constructor
     */
    public IslandsManager() {
        super();
        islands = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            islands.add(new Island());
        motherNature = 0;
    }

    /**
     * Method changeIslandOwner changes the owner of the selected island
     *
     * @param newOwner the new island's owner
     * @param island   the selected island
     */
    public void changeIslandOwner(Player newOwner, Island island) {
        int islandPosition = islands.indexOf(island);
        String oldOwner = null;
        int numberOfTower;

        if (island.getOwner() != null) {
            oldOwner = island.getOwner().getNickname();
        }

        numberOfTower = island.getNumberOfTowers();
        island.setOwner(newOwner);

        notifyObserver(new ChangeIslandOwner(newOwner.getNickname(), oldOwner, islandPosition, numberOfTower));
    }

    /**
     * Add a new student on the specified island
     * @param student the color of the student to add
     * @param islandPosition the id of the island to add the student on
     * @param playerNickname The nickname of the player who moved the student, null if is moved by the game mechanics.
     */
    public void addStudentOnIsland(Color student, int islandPosition, String playerNickname) {
        Island island = islands.get(islandPosition);
        List<Color> studentToAdd = new ArrayList<>();
        studentToAdd.add(student);

        island.addStudent(student);

        if(playerNickname != null)
            notifyObserver(new MoveStudents(GameComponent.ENTRANCE, GameComponent.ISLAND, studentToAdd, playerNickname, islandPosition));
        else
            notifyObserver(new MoveStudents(GameComponent.BAG, GameComponent.ISLAND, studentToAdd, null, islandPosition));
    }

    /**
     * Adds a ban card on the specified island
     * @param island the island to put the ban card on
     */
    public void addBanCardOnIsland(Island island) {
        island.addBanCard();
        notifyObserver(new BanCardEvent(getPositionByIsland(island), "ADD"));
    }

    /**
     * Removes a ban card on the specified island
     * @param island the island to remove the ban card from
     */
    public void removeBanCardOnIsland(Island island) {
        island.removeBanCard();
        notifyObserver(new BanCardEvent(getPositionByIsland(island), "REMOVE"));
    }

    /**
     * Gets the id of the specified island
     * @param island the island to the id of
     */
    public int getPositionByIsland(Island island) {
        return islands.indexOf(island);
    }


    /**
     * Method moveMotherNature moves MotherNature on the right island
     *
     * @param motherNatureMove the number of moves that MotherNature makes
     * @return the island where MotherNature stops
     */
    public Island moveMotherNature(int motherNatureMove) {
        if (motherNature + motherNatureMove > islands.size() - 1) {
            motherNature = motherNatureMove + this.motherNature - islands.size();
        } else {
            motherNature = motherNatureMove + this.motherNature;
        }
        notifyObserver(new MotherNatureMove(motherNature));
        return getIsland(motherNature);
    }

    /**
     * Gets the id of the island on which mother nature is
     * @return id of the island
     */
    public int getMotherNature() {
        return motherNature;
    }

    /**
     * Moves mother nature on the specified island
     * @param isl island to move mother nature on
     */
    public void moveMotherNatureOnIsland(Island isl) {
        motherNature = getPositionByIsland(isl);
        notifyObserver(new MotherNatureMove(motherNature));
    }

    /**
     * Gets the island from its id.
     * @param islandPosition the id of the island to get
     * @return the Island in the specified position (id)
     */
    public Island getIsland(int islandPosition) {
        return islands.get(islandPosition);
    }


    /**
     * Method getNeighbouringIslands returns the neighbouring islands of the checked island
     *
     * @param island the island that I want to check
     * @return the List of the neighbouring islands
     */
    public List<Island> getNeighbouringIslands(Island island) {
        List<Island> nearIslands = new ArrayList<>();
        if (islands.indexOf(island) == 0) {
            nearIslands.add(getIsland(islands.size() - 1));
            nearIslands.add(getIsland(1));
        } else if (islands.indexOf(island) == islands.size() - 1) {
            nearIslands.add(getIsland(islands.size() - 2));
            nearIslands.add(getIsland(0));
        } else {
            nearIslands.add(getIsland(islands.indexOf(island) - 1));
            nearIslands.add(getIsland(islands.indexOf(island) + 1));
        }
        return nearIslands;
    }


    /**
     * Method mergeIslands checks the neighbouring islands of the chosen island and merge them if they have the same owner.
     *
     * @param island the island that I want to check
     */
    public void mergeIslands(Island island) {
        List<Island> nearIslands = getNeighbouringIslands(island);

        for (Island isl : nearIslands) {
            if (island.hasSameOwner(isl)) {
                island.setNumberOfTowers(island.getNumberOfTowers() + isl.getNumberOfTowers());
                island.setBanCards(island.getBanCards() + isl.getBanCards());
                for (Color c : Color.values()) {
                    island.addStudents(c, isl.getSelectedStudents(c));
                }

                int currIslandPosition = islands.indexOf(island);
                int islandToMergePosition = islands.indexOf(isl);

                islands.remove(isl);

                notifyObserver(new MergeIslands(currIslandPosition, islandToMergePosition));

                moveMotherNatureOnIsland(island);

                if (getNumberOfIslands() <= 3) {
                    endGameObserver.notifyEndGame();
                }
            }
        }
    }


    /**
     * Returns the number of the remaining islands
     *
     * @ Return the number of the remaining islands
     */
    public int getNumberOfIslands() {
        return islands.size();
    }


    /**
     * Method getNumberOfBanCards checks all the islands and returns the number of ban cards on the islands
     *
     * @return the total number of ban cards on the islands
     */
    public int getNumberOfBanCards() {
        int banCards = 0;
        for (Island isl : islands)
            banCards += isl.getBanCards();
        return banCards;
    }

    /**
     * Attatch a EndgameObserver to the island manager in order to know when only 3 or less island are remaining to end the game.
     * @param endGameObserver Observer of the island manager
     */
    public void attach(EndGameObserver endGameObserver) {
        this.endGameObserver = endGameObserver;
    }
}
