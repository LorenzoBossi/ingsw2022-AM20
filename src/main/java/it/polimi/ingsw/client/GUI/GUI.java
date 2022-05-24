package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessage.ErrorType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.*;

public class GUI extends Application implements View {
    private final String CONNECTION_SCENE = "/Scene/connectionScene.fxml";
    private final String LOBBY_SCENE = "/Scene/lobbyScene.fxml";
    private final String NAME_SCENE = "/Scene/nameScene.fxml";
    private final String PIANIFICATION = "/Scene/pianificationScene.fxml";
    private final String PLAYERBOARD = "/Scene/playerBoardScene.fxml";

    private ServerConnection connectionToServer;
    private ClientModel clientModel;
    private ActionMovesHandler actionMovesHandler;
    private String clientNickname;
    private String gameMode;

    private Map<String, GUIController> controllerMap = new HashMap<>();
    private Map<String, Scene> sceneMap = new HashMap<>();
    private Stage stage;
    private Scene currentScene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void setup(String serverIp, int serverPort) {
        clientModel = new ClientModel();
        actionMovesHandler = new ActionMovesHandler();
        connectionToServer = new ServerConnection(serverIp, serverPort, new ServerMessageHandler(clientModel, this, actionMovesHandler));
        connectionToServer.setupConnection();
        (new Thread(connectionToServer)).start();
        changeScene(NAME_SCENE);
    }

    public void init() {
        List<String> scenes = new ArrayList<>(Arrays.asList(CONNECTION_SCENE, NAME_SCENE, LOBBY_SCENE, PIANIFICATION, PLAYERBOARD));
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
            currentScene = sceneMap.get(CONNECTION_SCENE);
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

        stage.setScene(currentScene);
        stage.show();
    }

    public void changeScene(String newScene) {
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

    @Override
    public void lobbySetup(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        ((LobbyController) controllerMap.get(LOBBY_SCENE)).setup(attendingLobbiesNumberOfPlayerMap, attendingLobbiesGameModeMap);
        changeScene(LOBBY_SCENE);
    }

    public void joiningLobby(String player, int playerRemaining) {
        ((LobbyController) controllerMap.get(LOBBY_SCENE)).playerJoining(player, playerRemaining);
    }

    @Override
    public void startGame(List<String> players, String gameMode) {
        System.out.println("The game is Starting....");
        System.out.println("Participants : " + players);
        Platform.runLater(() -> ((PianificationController)controllerMap.get(PIANIFICATION)).initButton(players));
        clientModel.initGame(players, gameMode);
    }

    @Override
    public void pianificationPhase(String targetPlayer) {
        List<AssistantName> assistantToPlay = clientModel.getAssistants();
        List<AssistantName> assistantPlayed = clientModel.getAssistantsPlayed();
        PianificationController controller = ((PianificationController) controllerMap.get(PIANIFICATION));
        ((PlayerBoardController)controllerMap.get(PLAYERBOARD)).clear();

        controller.update(assistantToPlay, assistantPlayed, targetPlayer);
        changeScene(PIANIFICATION);
    }

    public void showPlayerBoard(String nickname) {
        ((PlayerBoardController)controllerMap.get(PLAYERBOARD)).setPreviousScene(currentScene);
        ((PlayerBoardController)controllerMap.get(PLAYERBOARD)).update(nickname);
        changeScene(PLAYERBOARD);
    }

    @Override
    public void actionPhase(String targetPlayer) {

    }

    @Override
    public void nicknameSetup() {

    }

    @Override
    public void lobbyRefresh() {
        ((LobbyController) controllerMap.get(LOBBY_SCENE)).lobbyRefresh();
    }

    @Override
    public String getClientNickname() {
        return clientNickname;
    }

    @Override
    public void endGame(boolean aDraw, String winner) {

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
                pianificationPhase(clientNickname);
                alertMessage(errorText);
            }
        }
    }

    public Image getStudentPath(Color color) {
        return new Image(String.valueOf(getClass().getResource("/Imagines/" + color.name().toLowerCase() + ".png")));
    }

    public String getProfPath(Color color) {
        return String.valueOf(getClass().getResource("Imagines/" + color.name().toLowerCase() + "_prof.png"));
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


    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
