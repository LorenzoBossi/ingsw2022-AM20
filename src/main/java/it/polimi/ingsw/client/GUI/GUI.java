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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;

import static javafx.scene.paint.Color.LIGHTSKYBLUE;
import static javafx.scene.paint.Color.web;

public class GUI extends Application implements View {
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

    public void setup(String serverIp, int serverPort) {
        clientModel = new ClientModel();
        actionMovesHandler = new ActionMovesHandler();
        connectionToServer = new ServerConnection(serverIp, serverPort, new ServerMessageHandler(clientModel, this, actionMovesHandler));
        connectionToServer.setupConnection();
        (new Thread(connectionToServer)).start();
        changeScene(NAME);
    }

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

        Image icon = new Image("Imagines/Eriantys.png");
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

    public void sendMessage(ClientMessage message) {
        connectionToServer.sendMessageToServer(message);
    }

    public void setClientNickname(String clientNickname) {
        this.clientNickname = clientNickname;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    @Override
    public void lobbySetup(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        ((LobbyController) controllerMap.get(LOBBY)).setup(attendingLobbiesNumberOfPlayerMap, attendingLobbiesGameModeMap);
        changeScene(LOBBY);
    }

    public void joiningLobby(String player, int playerRemaining) {
        ((LobbyController) controllerMap.get(LOBBY)).playerJoining(player, playerRemaining);
    }

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

    @Override
    public void pianificationPhase(String targetPlayer) {
        List<AssistantName> assistantToPlay = clientModel.getAssistants();
        List<AssistantName> assistantPlayed = clientModel.getAssistantsPlayed();
        PianificationController controller = ((PianificationController) controllerMap.get(PIANIFICATION));

        controller.update(assistantToPlay, assistantPlayed, targetPlayer);
        changeScene(PIANIFICATION);
    }

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

    public void updateMyPlayerBoard() {
        ((ActionController) controllerMap.get(ACTION)).clearBoard();
        ((ActionController) controllerMap.get(ACTION)).updatePlayerBoard(clientNickname);
    }

    public void moveStudents() {
        ((MoveController)controllerMap.get(MOVE)).update();
        ((MoveController)controllerMap.get(MOVE)).moveStudents();
        changeScene(MOVE);
    }

    public void islandSelection(CharacterName name) {
        ((MoveController)controllerMap.get(MOVE)).initIslandSelection(name);
        changeScene(MOVE);
    }

    public void handlePrincessActivation() {
        CharacterCardView princess = clientModel.getCards().get(CharacterName.PRINCESS);
        ((MoveController)controllerMap.get(MOVE)).initPrincessActivation(princess.getStudents());
        changeScene(MOVE);
    }

    public void handleMonkActivation() {
        CharacterCardView monk = clientModel.getCards().get(CharacterName.MONK);
        ((MoveController)controllerMap.get(MOVE)).initMonkActivation(monk.getStudents());
        changeScene(MOVE);
    }

    public void handleJesterActivation() {
        CharacterCardView jester = clientModel.getCards().get(CharacterName.JESTER);
        ((MoveController)controllerMap.get(MOVE)).initJesterActivation(jester.getStudents());
        changeScene(MOVE);
    }

    public void handleMusicianActivation() {
        ((MoveController)controllerMap.get(MOVE)).initMusicianActivation();
        changeScene(MOVE);
    }

    public void handlePostmanActivation() {
        ((ActionController)controllerMap.get(ACTION)).updateMotherNatureMove();
    }

    public void consumeAction(ActionMove action) {
        actionMovesHandler.consumeAction(action);
    }

    @Override
    public void nicknameSetup() {

    }

    @Override
    public void lobbyRefresh() {
        ((LobbyController) controllerMap.get(LOBBY)).lobbyRefresh();
    }

    @Override
    public String getClientNickname() {
        return clientNickname;
    }

    @Override
    public void endGame(boolean aDraw, String winner) {
        isEnd = true;
        ((ActionController) controllerMap.get(ACTION)).clearAvailableActions();
        ((ActionController) controllerMap.get(ACTION)).clearBoard();
        ((ActionController) controllerMap.get(ACTION)).updatePlayerBoard(clientNickname);
        ((ActionController) controllerMap.get(ACTION)).endGameVisual(aDraw, winner);
    }

    public void handleError(ErrorType type, String errorText) {
        switch (type) {
            case NICKNAME_ALREADY_TAKEN -> {
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
        }
    }

    public Image getStudentImage(Color color) {
        return new Image(String.valueOf(getClass().getResource("/Imagines/" + color.name().toLowerCase() + ".png")));
    }

    public Image getProfImage(Color color) {
        return new Image(String.valueOf(getClass().getResource("/Imagines/" + color.name().toLowerCase() + "_prof.png")));
    }

    public Image getTowerImage(String player) {
        TowerColor color = clientModel.getTowerColorMap().get(player);
        return new Image(String.valueOf(getClass().getResource("/Imagines/" + color.name().toLowerCase() + "_tower.png")));
    }

    public Image getCharacterImage(CharacterName name) {
        return new Image(String.valueOf(getClass().getResource("/Imagines/" + name.name().toLowerCase() + ".jpg")));
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

    public void alertMessage(String errorText) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error!");
        error.setContentText(errorText);
        error.showAndWait();
    }

    public void notifyServerDisconnection() {
        String errorText = "Server connection lost... The application will close...";
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error!");
        error.setContentText(errorText);
        error.showAndWait();
        stop();
    }

    public void notifyPlayerDisconnection(String playerDisconnected) {
        String errorText = playerDisconnected + " has lost his connection... The application will close...";
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Error!");
        error.setContentText(errorText);
        error.showAndWait();
        stop();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
