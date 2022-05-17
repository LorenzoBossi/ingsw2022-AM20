package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.*;

/**
 * Class ClientModel contains a simplify version of the game's model
 */
public class ClientModel {

    private final int TOWERS_3P = 6;
    private final int TOWERS_2P = 8;

    private Map<String, List<Color>> entrances;
    private Map<String, List<Integer>> diningRooms;
    private Map<String, List<Color>> professors;
    private Map<String, Integer> towers;
    private Map<Integer, IslandView> islandsViewMap;
    private Map<Integer, List<Color>> clouds;
    private Map<CharacterName, CharacterCardView> cards;
    private Map<String, Integer> coins;

    private int motherNaturePosition;
    private List<AssistantName> assistants;
    private List<AssistantName> assistantsPlayed;
    private AssistantName lastAssistantPlayed;
    private boolean postmanActivation = false;

    /**
     * Constructor
     */
    public ClientModel() {
        professors = new HashMap<>();
        towers = new HashMap<>();
        entrances = new HashMap<>();
        diningRooms = new HashMap<>();
        islandsViewMap = new HashMap<>();
        clouds = new HashMap<>();
        assistants = new ArrayList<>();
        assistantsPlayed = new ArrayList<>();
        cards = new HashMap<>();
        coins = new HashMap<>();
    }

    /**
     * Method initGame initializes the clientModel
     *
     * @param players  the players in the game
     * @param gameMode the game's mode
     */
    public void initGame(List<String> players, String gameMode) {
        int cloudIndex = 0;

        for (String player : players) {
            clouds.put(cloudIndex, new ArrayList<>());
            entrances.put(player, new ArrayList<>());
            diningRooms.put(player, Arrays.asList(0, 0, 0, 0, 0));
            professors.put(player, new ArrayList<>());
            coins.put(player, 0);
            if (players.size() == 3) {
                towers.put(player, TOWERS_3P);
            } else {
                towers.put(player, TOWERS_2P);
            }
            cloudIndex++;
        }

        for (int i = 0; i < 12; i++) {
            islandsViewMap.put(i, new IslandView());
        }

        motherNaturePosition = 0;

        assistants = new ArrayList<>(Arrays.asList(AssistantName.values()));

    }

    /**
     * Method addStudentsToGameComponent adds the students to the right Game Component
     *
     * @param gameComponent the Game Component where adding students
     * @param nickname      the nickname Game Component's owner
     * @param students      the students to add on the Game Component
     */
    public void addStudentsToGameComponent(GameComponent gameComponent, String nickname, List<Color> students) {
        switch (gameComponent) {
            case ENTRANCE:
                entrances.get(nickname).addAll(students);
                break;
            case DINING_ROOM:
                for (Color student : students) {
                    int colorIndex = student.ordinal();
                    int numberOfStudents = diningRooms.get(nickname).get(colorIndex);
                    diningRooms.get(nickname).set(colorIndex, numberOfStudents + 1);
                }
                break;
            case CARD:
                CharacterName name = CharacterName.valueOf(nickname);
                cards.get(name).addStudents(students);
                break;
        }
    }

    /**
     * Method addStudentsToGameComponent adds the students to the right Game Component
     *
     * @param gameComponent the Game Component where adding students
     * @param index         the index of the  Game Component
     * @param students      the students to add on the Game Component
     */
    public void addStudentsToGameComponent(GameComponent gameComponent, Integer index, List<Color> students) {
        switch (gameComponent) {
            case CLOUD:
                clouds.get(index).addAll(students);
                break;
            case ISLAND:
                islandsViewMap.get(index).addStudents(students);
                break;
        }
    }


    /**
     * Method removeStudentsFromGameComponent adds the students to the right Game Component
     *
     * @param gameComponent the Game Component where removing students
     * @param nickname      the nickname Game Component's owner
     * @param students      the students to remove on the Game Component
     */
    public void removeStudentsFromGameComponent(GameComponent gameComponent, String nickname, List<Color> students) {
        switch (gameComponent) {
            case ENTRANCE:
                for (Color student : students)
                    entrances.get(nickname).remove(student);
                break;
            case DINING_ROOM:
                for (Color student : students) {
                    int colorIndex = student.ordinal();
                    int numberOfStudents = diningRooms.get(nickname).get(colorIndex);
                    diningRooms.get(nickname).set(colorIndex, numberOfStudents - 1);
                }
                break;
            case CARD:
                cards.get(CharacterName.valueOf(nickname)).removeStudents(students);
                break;
        }
    }

    /**
     * Method removeStudentsFromGameComponent adds the students to the right Game Component
     *
     * @param gameComponent the Game Component where removing students
     * @param index         the index of the Game Component
     * @param students      the students to remove on the Game Component
     */
    public void removeStudentsFromGameComponent(GameComponent gameComponent, Integer index, List<Color> students) {
        switch (gameComponent) {
            case CLOUD:
                for (Color student : students)
                    clouds.get(index).remove(student);
                break;
            case ISLAND:
                for (Color student : students)
                    islandsViewMap.get(index).removeStudent(student);
                break;
        }
    }

    /**
     * Method changeProfessorOwner changes the professor's owner
     *
     * @param newOwner  the new professor's owner
     * @param oldOwner  the old professor's owner
     * @param professor the color of the professor
     */
    public void changeProfessorOwner(String newOwner, String oldOwner, Color professor) {
        if (oldOwner != null) {
            professors.get(oldOwner).remove(professor);
        }
        professors.get(newOwner).add(professor);
    }

    /**
     * Method moveMotherNature moves Mother Nature
     *
     * @param newMotherNaturePosition the new Mother Nature position
     */
    public void moveMotherNature(int newMotherNaturePosition) {
        motherNaturePosition = newMotherNaturePosition;
    }

    /**
     * @param newOwner
     * @param oldOwner
     * @param islandId
     * @param numberOfTower
     */
    public void changeIslandOwner(String newOwner, String oldOwner, int islandId, int numberOfTower) {
        numberOfTower = islandsViewMap.get(islandId).getNumberOfTower();

        if (oldOwner == null) {
            towers.replace(newOwner, towers.get(newOwner) - 1);
            islandsViewMap.get(islandId).setOwner(newOwner);
            islandsViewMap.get(islandId).addTowers(1);
        } else {
            towers.replace(newOwner, towers.get(newOwner) - numberOfTower);
            towers.replace(oldOwner, towers.get(oldOwner) + numberOfTower);
            islandsViewMap.get(islandId).setOwner(newOwner);
        }
    }

    /**
     * Method mergeIsland merges the two island
     *
     * @param currIsland    the current island
     * @param islandToMerge the island to merge
     */
    public void mergeIsland(int currIsland, int islandToMerge) {
        List<Color> students;
        int towersToAdd;
        int banCards;
        int nextIsl;
        int thisIsl;
        IslandView island;

        if (currIsland > islandToMerge) {
            nextIsl = currIsland + 1;
            thisIsl = currIsland;
            students = islandsViewMap.get(currIsland).getStudents();
            towersToAdd = islandsViewMap.get(currIsland).getNumberOfTower();
            banCards = islandsViewMap.get(currIsland).getBanCards();
            islandsViewMap.get(islandToMerge).addStudents(students);
            islandsViewMap.get(islandToMerge).addBanCards(banCards);
            islandsViewMap.get(islandToMerge).addTowers(towersToAdd);

        } else {
            nextIsl = islandToMerge + 1;
            thisIsl = islandToMerge;
            students = islandsViewMap.get(islandToMerge).getStudents();
            towersToAdd = islandsViewMap.get(islandToMerge).getNumberOfTower();
            banCards = islandsViewMap.get(islandToMerge).getBanCards();
            islandsViewMap.get(currIsland).addStudents(students);
            islandsViewMap.get(currIsland).addBanCards(banCards);
            islandsViewMap.get(currIsland).addTowers(towersToAdd);
        }


        for (int i = thisIsl, j = nextIsl; i < islandsViewMap.size() - 1; i++, j++) {
            island = islandsViewMap.get(j);
            islandsViewMap.replace(i, island);
        }

        islandsViewMap.remove(islandsViewMap.size() - 1);
    }

    public void coinsMovement(String nickname, int movement) {
        coins.replace(nickname, coins.get(nickname) + movement);
    }

    public void increasePaymentCard(CharacterName name) {
        cards.get(name).addCoin();
    }

    public void postmanActivation() {
        postmanActivation = true;
    }

    public void resetPostmanActivation() {
        postmanActivation = false;
    }

    public int getMaxMotherNatureMove() {
        int maxMotherNatureMove = lastAssistantPlayed.getMotherNatureMove();
        if (postmanActivation) {
            maxMotherNatureMove = maxMotherNatureMove + 2;
        }
        return maxMotherNatureMove;
    }

    public void handleBanCardEvent(int islandId, String action) {
        IslandView isl = islandsViewMap.get(islandId);
        switch (action) {
            case "ADD" -> {
                isl.addBanCards(1);
                cards.get(CharacterName.HERBALIST).removeBanCard();
            }
            case "REMOVE" -> {
                isl.removeBanCards(1);
                cards.get(CharacterName.HERBALIST).addBanCard();
            }
        }
    }

    /**
     * Method removeAssistant removes the assistant played by the player
     *
     * @param name the name of the assistant played
     */
    public void removeAssistant(AssistantName name) {
        assistants.remove(name);
    }

    /**
     * Method setLastAssistantPlayed sets the last assistant played by the player
     *
     * @param lastAssistantPlayed the last assistant played by the player
     */
    public void setLastAssistantPlayed(AssistantName lastAssistantPlayed) {
        this.lastAssistantPlayed = lastAssistantPlayed;
    }

    /**
     * Method addCharacterCard adds new Character card to the ClientModel
     *
     * @param name     the name of the card
     * @param coins    the coins required to activate
     * @param type     the type of the card
     * @param students the students on the card
     */
    public void addCharacterCard(CharacterName name, int coins, CharacterCardType type, List<Color> students) {
        cards.put(name, new CharacterCardView(name, type, coins, students));
    }

    public boolean isPresentEntrance(String nickname, List<Color> students) {
        List<Color> studentsEntrance = new ArrayList<>(entrances.get(nickname));
        for (Color student : students) {
            if (studentsEntrance.contains(student))
                studentsEntrance.remove(student);
            return false;
        }
        return true;
    }

    /**
     * Method getEntrances gets the game's entrances
     *
     * @return the entrances
     */
    public Map<String, List<Color>> getEntrances() {
        return entrances;
    }

    /**
     * Method getDiningRooms gets the game's Dining Rooms
     *
     * @return the dining rooms
     */
    public Map<String, List<Integer>> getDiningRooms() {
        return diningRooms;
    }

    /**
     * Method getAssistants gets the player's assistants
     *
     * @return the assistants
     */
    public List<AssistantName> getAssistants() {
        return assistants;
    }

    public AssistantName getLastAssistantPlayed() {
        return lastAssistantPlayed;
    }

    public List<AssistantName> getAssistantsPlayed() {
        return assistantsPlayed;
    }

    public Map<String, List<Color>> getProfessors() {
        return professors;
    }

    public Map<String, Integer> getTowers() {
        return towers;
    }

    public Map<CharacterName, CharacterCardView> getCards() {
        return cards;
    }

    /**
     * Method getIslandsViewMap gets the game's islands
     *
     * @return the islands
     */
    public Map<Integer, IslandView> getIslandsViewMap() {
        return islandsViewMap;
    }

    public Map<Integer, List<Color>> getClouds() {
        return clouds;
    }

    public Map<String, Integer> getCoins() {
        return coins;
    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }
}
