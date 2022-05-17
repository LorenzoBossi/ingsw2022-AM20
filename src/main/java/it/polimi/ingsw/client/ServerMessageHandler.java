package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.serverMessage.*;

import java.util.List;
import java.util.Map;

/**
 * Class ServerMessageHandler handles the messages receive from the Server
 */
public class ServerMessageHandler {
    private ClientModel model;
    private CLI view;
    private ActionMovesHandler actionMovesHandler;

    /**
     * Constructor
     *
     * @param model the client's model representation
     * @param view  the view
     */
    public ServerMessageHandler(ClientModel model, CLI view, ActionMovesHandler actionMovesHandler) {
        this.model = model;
        this.view = view;
        this.actionMovesHandler = actionMovesHandler;
    }

    public void setModel(ClientModel model) {
        this.model = model;
    }

    /**
     * Method handleMessage receives all the Server messages and dispatches them
     *
     * @param message the Server message
     */
    public synchronized void handleMessage(ServerMessage message) {
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
        } else if (message instanceof StartPianificationPhase) {
            String currPlayer = ((StartPianificationPhase) message).getTargetPlayer();
            view.pianificationPhase(currPlayer);
        } else if (message instanceof StartActionPhase) {
            actionMovesHandler.initializeAction();
            model.getAssistantsPlayed().clear();
            view.actionPhase(((StartActionPhase) message).getTargetPlayer());
        } else if (message instanceof UpdateMessage) {
            handleUpdateMessage(message);
        } else if (message instanceof NextMove) {
            view.actionPhase(view.getClientNickname());
        }
    }

    /**
     * Method handleUpdateMessage consumes the updateMessage from the Server
     *
     * @param message the UpdateMessage from the server
     */
    public void handleUpdateMessage(ServerMessage message) {
        if (message instanceof AssistantPlayed) {
            String player = ((AssistantPlayed) message).getPlayer();
            String name = ((AssistantPlayed) message).getAssistantName();

            model.getAssistantsPlayed().add(AssistantName.valueOf(name));
            System.out.println(player + " has played " + name);
        } else if (message instanceof MoveStudents) {
            handleStudentsMove(message);
        } else if (message instanceof ExtractedCard) {
            CharacterName name = ((ExtractedCard) message).getName();
            int coins = ((ExtractedCard) message).getCoinRequired();
            CharacterCardType type = ((ExtractedCard) message).getType();
            List<Color> students = ((ExtractedCard) message).getStudents();

            model.addCharacterCard(name, coins, type, students);
        } else if (message instanceof ChangeProfessor) {
            String newOwner = ((ChangeProfessor) message).getNewOwner();
            String oldOwner = ((ChangeProfessor) message).getOldOwner();
            Color professor = ((ChangeProfessor) message).getProfessorColor();

            model.changeProfessorOwner(newOwner, oldOwner, professor);
        } else if (message instanceof MotherNatureMove) {
            int newMotherNaturePosition = ((MotherNatureMove) message).getNewMotherNaturePosition();

            model.moveMotherNature(newMotherNaturePosition);
        } else if (message instanceof ChangeIslandOwner) {
            String newOwner = ((ChangeIslandOwner) message).getNewOwner();
            String oldOwner = ((ChangeIslandOwner) message).getOldOwner();
            int islandId = ((ChangeIslandOwner) message).getIslandPosition();
            int numberOfTowers = ((ChangeIslandOwner) message).getNumberOfTower();

            model.changeIslandOwner(newOwner, oldOwner, islandId, numberOfTowers);
        } else if (message instanceof MergeIslands) {
            int currIsland = ((MergeIslands) message).getCurrIslandPosition();
            int islToMerge = ((MergeIslands) message).getIslandToMergePosition();

            model.mergeIsland(currIsland, islToMerge);
        } else if (message instanceof PlayerCoinsEvent) {
            int coins = ((PlayerCoinsEvent) message).getNumber();
            String nickname = ((PlayerCoinsEvent) message).getNickname();

            model.coinsMovement(nickname, coins);
        } else if (message instanceof CardActivated) {
            String player = ((CardActivated) message).getPlayer();
            CharacterName name = ((CardActivated) message).getName();

            System.out.println(player + " has activated " + name + " card");
        } else if (message instanceof IncreaseCardPrice) {
            CharacterName name = ((IncreaseCardPrice) message).getCharacterName();

            model.increasePaymentCard(name);
        } else if(message instanceof BanCardEvent) {
            int islandId = ((BanCardEvent) message).getIslandPosition();
            String action = ((BanCardEvent) message).getAction();

            model.handleBanCardEvent(islandId, action);
        }
    }

    /**
     * Method handleStudentsMove consumes the MoveStudents message from the server
     *
     * @param message the MoveStudents message
     */
    public void handleStudentsMove(ServerMessage message) {
        MoveStudents updateMessage = (MoveStudents) message;
        GameComponent source = updateMessage.getSource();
        GameComponent destination = updateMessage.getDestination();
        List<Color> students = updateMessage.getStudents();
        Object indexSource = updateMessage.getIndexSource();
        Object indexDestination = updateMessage.getIndexDestination();


        System.out.println(source + "--->" + destination);
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
                    case CARD:
                        model.removeStudentsFromGameComponent(source, index, students);
                        model.addStudentsToGameComponent(destination, (String) indexDestination, students);
                }
                break;

            case DINING_ROOM:
                switch (destination) {
                    case BAG:
                        model.removeStudentsFromGameComponent(source, (String) indexSource, students);
                        break;
                    case ENTRANCE:
                        model.addStudentsToGameComponent(destination, (String) indexDestination, students);
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
                        break;
                    case ISLAND:
                        model.addStudentsToGameComponent(destination, (int) indexDestination, students);
                        break;
                    case CARD:
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
            case CARD:
                switch (destination) {
                    case ENTRANCE:
                        model.addStudentsToGameComponent(destination, (String) indexDestination, students);
                        model.removeStudentsFromGameComponent(source, (String) indexSource, students);
                        break;
                    case ISLAND:
                        model.addStudentsToGameComponent(destination, (int) indexDestination, students);
                        model.removeStudentsFromGameComponent(source, (String) indexSource, students);
                        break;
                    case DINING_ROOM:
                        model.addStudentsToGameComponent(destination, (String) indexDestination, students);
                        model.removeStudentsFromGameComponent(source, (String) indexSource, students);
                        break;
                }
        }

    }

    /**
     * Method handleError consumes the Error message from the Server
     *
     * @param message the error message
     */
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
            case ASSISTANT_NOT_PLAYABLE:
                System.out.println(errorText);
                model.getAssistants().add(model.getLastAssistantPlayed());
                view.pianificationPhase(view.getClientNickname());
                break;
            case SELECTED_CLOUD_ERROR:
                System.out.println(errorText);
                actionMovesHandler.handleError(ActionMove.SELECT_CLOUD);
                view.actionPhase(view.getClientNickname());
        }
    }

    public void Disconnection() {
        System.out.println("Disconnection");
        System.exit(0);
    }
}
