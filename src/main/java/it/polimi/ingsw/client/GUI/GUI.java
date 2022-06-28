package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessage.ErrorType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.*;

import static javafx.scene.paint.Color.web;

/**
 * Class GUI controls the client graphic content
 */
public class GUI extends Application implements View {

    /**
     * The GUI scenes
     */
    private final String CONNECTION = "/Scene/connectionScene.fxml";
    private final String LOBBY = "/Scene/lobbyScene.fxml";
    private final String NAME = "/Scene/nameScene.fxml";
    private final String PIANIFICATION = "/Scene/pianificationScene.fxml";
    private final String PLAYERBOARD = "/Scene/playerBoardScene.fxml";
    private final String ISLANDS = "/Scene/islandsScene.fxml";
    private final String ACTION = "/Scene/actionScene.fxml";
    private final String MOVE = "/Scene/moveScene.fxml";
    private final String CLOUDS = "/Scene/cloudsScene.fxml";
    private final String CHARACTER = "/Scene/characterScene.fxml";

    private ServerConnection connectionToServer;
    private ClientModel clientModel;
    private ActionMovesHandler actionMovesHandler;
    private String clientNickname;
    private String gameMode;


    private Map<String, GUIController> controllerMap = new HashMap<>();
    private Map<String, Scene> sceneMap = new HashMap<>();
    private Stage stage;
    private Scene currentScene;
    private boolean isEnd = false;

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Method setup initializes the connection between the client and the server
     * @param serverIp the server IP
     * @param serverPort the server port
     */
    public void setup(String serverIp, int serverPort) {
        clientModel = new ClientModel();
        actionMovesHandler = new ActionMovesHandler();
        connectionToServer = new ServerConnection(serverIp, serverPort, new ServerMessageHandler(clientModel, this, actionMovesHandler));
        if(!connectionToServer.setupConnection()){
            alertMessage("Unable to reach the server at: ("+serverIp+","+serverPort+")");
            currentScene = sceneMap.get(CONNECTION);
        }else{
            (new Thread(connectionToServer)).start();
            changeScene(NAME);
        }
    }

    /**
     * Initializes the sceneMap and the controllerMap
     */
    public void init() {
        List<String> scenes = new ArrayList<>(Arrays.asList(CONNECTION, NAME, LOBBY, PIANIFICATION, PLAYERBOARD, ISLANDS, ACTION, MOVE, CLOUDS, CHARACTER));
        for (String scene : scenes) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scene));
            try {
                sceneMap.put(scene, new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            GUIController controller = loader.getController();
            controller.setGui(this);
            controllerMap.put(scene, controller);
            currentScene = sceneMap.get(CONNECTION);
        }
    }


    @Override
    public void start(Stage stage) throws Exception {
        init();
        stage.setResizable(false);
        this.stage = stage;

        Image icon = new Image("Images/Eriantys.png");
        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");
        LinearGradient background= new LinearGradient(
                0, 0, 1, 1, true,    //sizing
                CycleMethod.NO_CYCLE,                 //cycling
                new Stop(0, web("#10208b")),
                //new Stop(1,LIGHTSKYBLUE)  ,   //colors
                new Stop(1, web("#87ceeb"))
        );
        currentScene.setFill(background);

        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * Changes the scene
     * @param newScene the new scene
     */
    public void changeScene(String newScene) {
        if (newScene.equals(PIANIFICATION)) {
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setResizable(false);
        }
        currentScene = sceneMap.get(newScene);
        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * Sends message to the server
     * @param message the client message to be sent to the server
     */
    public void sendMessage(ClientMessage message) {
        connectionToServer.sendMessageToServer(message);
    }

    /**
     * Sets the clientNickname
     * @param clientNickname the client nickname
     */
    public void setClientNickname(String clientNickname) {
        this.clientNickname = clientNickname;
    }

    /**
     * Sets the gameMode of the match
     * @param gameMode the gameMode of the match
     */
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Gets the gameMode of the match
     * @return the gameMode of the match
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Method lobbySetup is used to create or join a lobby
     * @param attendingLobbiesNumberOfPlayerMap map of lobby id and number of player for that lobby
     * @param attendingLobbiesGameModeMap map of lobby id and game difficulty
     */
    @Override
    public void lobbySetup(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        ((LobbyController) controllerMap.get(LOBBY)).setup(attendingLobbiesNumberOfPlayerMap, attendingLobbiesGameModeMap);
        changeScene(LOBBY);
    }

    /**
     * Method joiningLobby update the lobby scene with the number of players remaining
     * @param player the name of the joining player
     * @param playerRemaining the number of the player remaining
     */
    public void joiningLobby(String player, int playerRemaining) {
        ((LobbyController) controllerMap.get(LOBBY)).playerJoining(player, playerRemaining);
    }

    /**
     * Method startGame starts the game
     * @param players list of player for the game
     * @param gameMode game mode choice
     * @param towerColorMap map of player and his/her tower color
     */
    @Override
    public void startGame(List<String> players, String gameMode, Map<String, TowerColor> towerColorMap) {
        System.out.println("The game is Starting....");
        System.out.println("Participants : " + players);

        actionMovesHandler.setGameMode(gameMode);
        actionMovesHandler.setNumberOfPlayer(players.size());

        ((PianificationController) controllerMap.get(PIANIFICATION)).initButton(players);
        ((ActionController) controllerMap.get(ACTION)).initActionButton(players);
        ((CloudsController) controllerMap.get(CLOUDS)).initClouds(players.size());

        clientModel.initGame(players, gameMode, towerColorMap);
    }

    /**
     *  Method pianificationPhase carries out the operations of the pianification phase
     * @param targetPlayer player's pianification turn
     */
    @Override
    public void pianificationPhase(String targetPlayer) {
        List<AssistantName> assistantToPlay = clientModel.getAssistants();
        List<AssistantName> assistantPlayed = clientModel.getAssistantsPlayed();
        PianificationController controller = ((PianificationController) controllerMap.get(PIANIFICATION));

        controller.update(assistantToPlay, assistantPlayed, targetPlayer);
        changeScene(PIANIFICATION);
    }

    /**
     * Method showPlayerBoard shows the chosen player playerBoard
     * @param nickname the chosen player
     */
    public void showPlayerBoard(String nickname) {
        Scene playerBoard = sceneMap.get(PLAYERBOARD);
        Stage newStage = new Stage();
        newStage.setTitle(nickname + "'s PlayerBoard");
        newStage.setScene(playerBoard);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(stage);
        newStage.setResizable(false);

        ((PlayerBoardController) controllerMap.get(PLAYERBOARD)).clearBoard();
        ((PlayerBoardController) controllerMap.get(PLAYERBOARD)).updatePlayerBoard(nickname);

        newStage.show();
    }

    /**
     * Method showIslands shows the islands scene
     */
    public void showIslands() {
        Scene islands = sceneMap.get(ISLANDS);
        Stage newStage = new Stage();
        newStage.setTitle("Islands");
        newStage.setScene(islands);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(stage);
        newStage.setResizable(false);

        ((IslandsController) controllerMap.get(ISLANDS)).clear();
        ((IslandsController) controllerMap.get(ISLANDS)).update();

        newStage.show();
    }

    /**
     * Method showClouds shows the clouds scene
     * @param choice true if you want that the player chose one cloud, false otherwise
     */
    public void showClouds(boolean choice) {
        Scene clouds = sceneMap.get(CLOUDS);
        Stage newStage = new Stage();
        newStage.setTitle("Clouds");
        newStage.setScene(clouds);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(stage);
        newStage.setResizable(false);

        ((CloudsController) controllerMap.get(CLOUDS)).clear();
        ((CloudsController) controllerMap.get(CLOUDS)).update();

        if(choice)
            ((CloudsController) controllerMap.get(CLOUDS)).initCloudsButton();

        newStage.show();
    }

    /**
     * Method showCharacters shows the character scene
     * @param choice true if you want that the player chose to activate one character card, false otherwise
     */
    public void showCharacters(boolean choice) {
        Scene characters = sceneMap.get(CHARACTER);
        Stage newStage = new Stage();
        newStage.setTitle("Characters");
        newStage.setScene(characters);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(stage);
        newStage.setResizable(false);

        ((CharacterController) controllerMap.get(CHARACTER)).clear();
        ((CharacterController) controllerMap.get(CHARACTER)).update();

        if(choice)
            ((CharacterController) controllerMap.get(CHARACTER)).initCharacterButton();

        newStage.show();
    }

    /**
     * actionPhase method carries out the operations of the action phase
     * @param targetPlayer player's action turn
     */
    @Override
    public void actionPhase(String targetPlayer) {
        ((ActionController) controllerMap.get(ACTION)).clearAvailableActions();
        ((ActionController) controllerMap.get(ACTION)).clearBoard();
        ((ActionController) controllerMap.get(ACTION)).updateTurnMessage(targetPlayer);
        ((ActionController) controllerMap.get(ACTION)).updatePlayerBoard(clientNickname);
        ((MoveController) controllerMap.get(MOVE)).clear();

        if(clientNickname.equals(targetPlayer))
            ((ActionController) controllerMap.get(ACTION)).initAvailableActions(actionMovesHandler.getAvailableActions());
        changeScene(ACTION);
    }

    /**
     * Method updateMyPlayerBoard update the playerBoard on the scene
     */
    public void updateMyPlayerBoard() {
        ((ActionController) controllerMap.get(ACTION)).clearBoard();
        ((ActionController) controllerMap.get(ACTION)).updatePlayerBoard(clientNickname);
    }

    /**
     * method moveStudents moves the selected student from the Entrance to the DiningRoom or to an Island
     */
    public void moveStudents() {
        ((MoveController)controllerMap.get(MOVE)).update();
        ((MoveController)controllerMap.get(MOVE)).moveStudents();
        changeScene(MOVE);
    }

    /**
     * Allows the user to select an island in order to activate a character card
     */
    public void islandSelection(CharacterName name) {
        ((MoveController)controllerMap.get(MOVE)).initIslandSelection(name);
        changeScene(MOVE);
    }

    /**
     * Handles the activation of the princess
     */
    public void handlePrincessActivation() {
        CharacterCardView princess = clientModel.getCards().get(CharacterName.PRINCESS);
        ((MoveController)controllerMap.get(MOVE)).initPrincessActivation(princess.getStudents());
        changeScene(MOVE);
    }

    /**
     * Handles the activation of the monk
     */
    public void handleMonkActivation() {
        CharacterCardView monk = clientModel.getCards().get(CharacterName.MONK);
        ((MoveController)controllerMap.get(MOVE)).initMonkActivation(monk.getStudents());
        changeScene(MOVE);
    }

    /**
     * Handles the activation of the jester
     */
    public void handleJesterActivation() {
        CharacterCardView jester = clientModel.getCards().get(CharacterName.JESTER);
        ((MoveController)controllerMap.get(MOVE)).initJesterActivation(jester.getStudents());
        changeScene(MOVE);
    }

    /**
     * Handles the activation of the musician
     */
    public void handleMusicianActivation() {
        ((MoveController)controllerMap.get(MOVE)).initMusicianActivation();
        changeScene(MOVE);
    }

    /**
     * Handles the activation of the postman
     */
    public void handlePostmanActivation() {
        ((ActionController)controllerMap.get(ACTION)).updateMotherNatureMove();
    }

    /**
     * Consume one action from the client
     * @param action the action to consume
     */
    public void consumeAction(ActionMove action) {
        actionMovesHandler.consumeAction(action);
    }

    @Override
    public void nicknameSetup() {

    }

    /**
     * Method lobbyRefresh updates the list of lobbies
     */
    @Override
    public void lobbyRefresh() {
        ((LobbyController) controllerMap.get(LOBBY)).lobbyRefresh();
    }


    @Override
    public String getClientNickname() {
        return clientNickname;
    }

    /**
     * Choose the correct end game scene for the player, according to the game ending
     * @param isADraw a boolean telling if the game is ended in a draw
     * @param winner the nickname of the winner of the game ( ignore if there isn't a winner )
     */
    @Override
    public void endGame(boolean isADraw, String winner) {
        isEnd = true;
        ((ActionController) controllerMap.get(ACTION)).clearAvailableActions();
        ((ActionController) controllerMap.get(ACTION)).clearBoard();
        ((ActionController) controllerMap.get(ACTION)).updatePlayerBoard(clientNickname);
        ((ActionController) controllerMap.get(ACTION)).endGameVisual(isADraw, winner);
    }

    /**
     * Handle the error message from the server
     * @param type the type of the error
     * @param errorText the text of the error
     */
    public void handleError(ErrorType type, String errorText) {
        switch (type) {
            case NICKNAME_ALREADY_TAKEN, NOT_YOUR_TURN -> {
                alertMessage(errorText);
            }
            case LOBBY_ERROR -> {
                lobbyRefresh();
                alertMessage(errorText);
            }
            case ASSISTANT_NOT_PLAYABLE -> {
                clientModel.getAssistants().add(clientModel.getLastAssistantPlayed());
                pianificationPhase(clientNickname);
                alertMessage(errorText);
            }
            case INVALID_INPUT -> {
                actionMovesHandler.handleError(ActionMove.MOVE_STUDENTS);
                alertMessage(errorText);
            }
            case CARD_REQUIREMENTS_ERROR -> {
                actionMovesHandler.handleError(ActionMove.ACTIVATE_CARD);
                alertMessage(errorText);
            }
            case SELECTED_CLOUD_ERROR -> {
                actionMovesHandler.handleError(ActionMove.SELECT_CLOUD);
                alertMessage(errorText);
            }
            case MOTHER_NATURE_MOVE_INVALID -> {
                actionMovesHandler.handleError(ActionMove.MOVE_MOTHER_NATURE);
                alertMessage(errorText);
            }

        }
    }

    /**
     * Returns the image of the corresponding student color
     * @param color the color of the student
     * @return the image of the corresponding student color
     */
    public Image getStudentImage(Color color) {
        return new Image(String.valueOf(getClass().getResource("/Images/" + color.name().toLowerCase() + ".png")));
    }

    /**
     * Returns the image of the corresponding professor color
     * @param color the color of the professor
     * @return  the image of the corresponding professor color
     */
    public Image getProfImage(Color color) {
        return new Image(String.valueOf(getClass().getResource("/Images/" + color.name().toLowerCase() + "_prof.png")));
    }

    /**
     * Returns the image of the tower of the corresponding player
     * @param player the name of the player
     * @return the image of the tower of the corresponding player
     */
    public Image getTowerImage(String player) {
        TowerColor color = clientModel.getTowerColorMap().get(player);
        return new Image(String.valueOf(getClass().getResource("/Images/" + color.name().toLowerCase() + "_tower.png")));
    }

    /**
     * Returns the image of the corresponding character card
     * @param name the name of the character card
     * @return the image of the corresponding character card
     */
    public Image getCharacterImage(CharacterName name) {
        return new Image(String.valueOf(getClass().getResource("/Images/" + name.name().toLowerCase() + ".jpg")));
    }

    /**
     * Gets the client model
     * @return the client model
     */
    public ClientModel getClientModel() {
        return clientModel;
    }

    /**
     * Show an alert message to the client
     * @param errorText the text of the error
     */
    public void alertMessage(String errorText) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error!");
        error.setContentText(errorText);
        error.showAndWait();
    }

    /**
     * Notify to the client the server disconnection
     */
    public void notifyServerDisconnection() {
        String errorText = "Server connection lost... The application will close...";
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error!");
        error.setContentText(errorText);
        error.showAndWait();
        stop();
    }

    /**
     * Notify to the client the disconnection of one player
     */
    public void notifyPlayerDisconnection(String playerDisconnected) {
        String errorText = playerDisconnected + " has lost his connection... The application will close...";
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error!");
        error.setContentText(errorText);
        error.showAndWait();
        stop();
    }

    /**
     * Says whether the game is over
     * @return true if the game is over, false otherwise
     */
    public boolean isEnd() {
        return isEnd;
    }

    /**
     * Close the application
     */
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
