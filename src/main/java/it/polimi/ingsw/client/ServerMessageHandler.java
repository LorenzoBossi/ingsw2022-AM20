package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;
import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessage.*;

import java.util.List;
import java.util.Map;

public class ServerMessageHandler {
    private ClientModel model;
    private CLI view;

    public ServerMessageHandler(ClientModel model, CLI view) {
        this.model = model;
        this.view = view;
    }

    public void setModel(ClientModel model) {
        this.model = model;
    }


    public void handleMessage(ServerMessage message) {
        if (message instanceof SendLobbies) {
            Map<Integer, Integer> numberPlayerMap = ((SendLobbies) message).getAttendingLobbiesNumberPlayerMap();
            Map<Integer, String> gameModeMap = ((SendLobbies) message).getAttendingLobbiesGameModeMap();
            view.lobbySetup(numberPlayerMap, gameModeMap);
        } else if (message instanceof GameError) {
            handleError(message);
        } else if (message instanceof JoiningLobby) {
            String joiningPlayer = ((JoiningLobby) message).getJoiningPlayer();
            System.out.println(joiningPlayer + " join the lobby");
            int playerRemainingToStartTheGame = ((JoiningLobby) message).getPlayerRemainingToStartTheGame();
            System.out.println("Remaining " + playerRemainingToStartTheGame + " players to start the game");
        } else if (message instanceof GameStarting) {
            List<String> players = ((GameStarting) message).getPlayers();
            String gameMode = ((GameStarting) message).getGameMode();
            view.startGame(players, gameMode);
        } else if (message instanceof MoveStudents) {
            handleStudentsMove(message);
        } else if (message instanceof StartPianificationPhase) {
            String currPlayer = ((StartPianificationPhase) message).getTargetPlayer();
            view.PianificationPhase(currPlayer);
        }
    }

    public void handleStudentsMove(ServerMessage message) {
        MoveStudents updateMessage = (MoveStudents) message;
        GameComponent source = updateMessage.getSource();
        GameComponent destination = updateMessage.getDestination();
        List<Color> students = updateMessage.getStudents();
        Object indexSource = updateMessage.getIndexSource();
        Object indexDestination = updateMessage.getIndexDestination();

        System.out.println(source +"--->" + destination);
        System.out.println(students);
        System.out.println(indexSource + "--->" + indexDestination);

        switch (source) {
            case ENTRANCE:
                String index = (String) indexSource;
                switch (destination) {
                    case DINING_ROOM:
                        model.removeStudentsFromGameComponent(source, index, students);
                        model.addStudentsToGameComponent(destination, index, students);
                        break;
                    case ISLAND:
                        model.removeStudentsFromGameComponent(source, index, students);
                        model.addStudentsToGameComponent(destination, (int) indexDestination, students);
                        break;
                }
                break;

            case DINING_ROOM:
                switch (destination) {
                    case BAG:
                        model.removeStudentsFromGameComponent(source, (String) indexSource, students);
                        break;
                }
                break;

            case BAG:
                switch (destination) {
                    case CLOUD:
                        model.addStudentsToGameComponent(destination, (int) indexDestination, students);
                        break;
                    case ENTRANCE:
                        model.addStudentsToGameComponent(destination, (String) indexDestination, students);
                }
                break;

            case CLOUD:
                switch (destination) {
                    case ENTRANCE:
                        model.addStudentsToGameComponent(destination, (String) indexDestination, students);
                        model.removeStudentsFromGameComponent(source, (int) indexSource, students);
                        break;
                }
                break;
        }

    }

    public void handleError(ServerMessage message) {
        ErrorType errorType = ((GameError) message).getErrorType();
        String errorText = ((GameError) message).getErrorText();
        switch (errorType) {
            case NICKNAME_ALREADY_TAKEN:
                System.out.println(errorText);
                view.nicknameSetup();
                break;
            case LOBBY_ERROR:
                System.out.println(errorText);
                view.lobbyRefresh();
                break;

        }
    }
}
