package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;

import java.util.*;

public class ClientModel {
    private String clientNickname;
    private Map<String, List<Color>> entrances;
    private Map<String, List<Integer>> diningRooms;
    private Map<Integer, List<Color>> islands;
    private Map<Integer, List<Color>> clouds;
    private List<AssistantName> assistants;
    private int coins;

    public ClientModel() {
        entrances = new HashMap<>();
        diningRooms = new HashMap<>();
        islands = new HashMap<>();
        clouds = new HashMap<>();
        assistants = new ArrayList<>();
    }

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

    public void addStudentsToGameComponent(GameComponent gameComponent, Integer index, List<Color> students) {
        switch (gameComponent) {
            case CLOUD:
                clouds.get(index).addAll(students);
                break;
            case ISLAND:
                islands.get(index).addAll(students);
        }
    }

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

    public void removeStudentsFromGameComponent(GameComponent gameComponent, Integer index, List<Color> students) {
        switch (gameComponent) {
            case CLOUD:
                for(Color student : students)
                    clouds.get(index).remove(student);
                break;
            case ISLAND:
                for(Color student : students)
                    islands.get(index).remove(student);
                break;
        }
    }


    public Map<String, List<Color>> getEntrances() {
        return entrances;
    }

    public Map<String, List<Integer>> getDiningRooms() {
        return diningRooms;
    }

    public Map<Integer, List<Color>> getIslands() {
        return islands;
    }

    public List<AssistantName> getAssistants() {
        return assistants;
    }
}
