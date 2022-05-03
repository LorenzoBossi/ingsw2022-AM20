package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;

import java.util.*;

/**
 * Class ClientModel contains a simplify version of the game's model
 */
public class ClientModel {
    private String clientNickname;
    private Map<String, List<Color>> entrances;
    private Map<String, List<Integer>> diningRooms;
    private Map<Integer, List<Color>> islands;
    private Map<Integer, List<Color>> clouds;
    private List<AssistantName> assistants;
    private List<AssistantName> assistantsPlayed;
    private AssistantName lastAssistantPlayed;
    private int coins;

    /**
     * Constructor
     */
    public ClientModel() {
        entrances = new HashMap<>();
        diningRooms = new HashMap<>();
        islands = new HashMap<>();
        clouds = new HashMap<>();
        assistants = new ArrayList<>();
        assistantsPlayed = new ArrayList<>();
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
            diningRooms.put(player, new ArrayList<>());
            cloudIndex++;
        }

        for (int i = 0; i < 12; i++) {
            islands.put(i, new ArrayList<>());
        }

        assistants = new ArrayList<>(Arrays.asList(AssistantName.values()));

        if (gameMode.equals("expert")) {
            coins = 1;
        }
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
                islands.get(index).addAll(students);
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
                    islands.get(index).remove(student);
                break;
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
     * Method getIslands gets the game's islands
     *
     * @return the islands
     */
    public Map<Integer, List<Color>> getIslands() {
        return islands;
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
}
