package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.network.messages.clientMessage.CreateLobby;
import it.polimi.ingsw.network.messages.clientMessage.GetLobbies;
import it.polimi.ingsw.network.messages.clientMessage.JoinLobby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class LobbyController implements GUIController, Initializable {
    private GUI gui;
    @FXML
    private ListView<String> lobbies;
    @FXML
    private Button joinButton;
    @FXML
    private Button createButton;
    @FXML
    private ChoiceBox<Integer> numberPlayers;
    @FXML
    private ChoiceBox<String> gamemode;
    @FXML
    private Label joinConfirm;
    @FXML
    private Label errorPlayer;
    @FXML
    private Label errorGamemode;
    @FXML
    private Label waitingMessage;
    @FXML
    private Label playerText;
    @FXML
    private Label playerRemaining;
    @FXML
    private Button refreshButton;
    @FXML
    private Label playerJoined;


    public void setup(Map<Integer, Integer> playersMap, Map<Integer, String> gameModeMap) {
        waitingMessage.setOpacity(0);
        playerText.setOpacity(0);
        playerRemaining.setOpacity(0);
        playerJoined.setOpacity(0);

        refreshButton.setDisable(false);
        createButton.setDisable(false);
        joinConfirm.setOpacity(0);
        joinButton.setDisable(true);
        errorPlayer.setOpacity(0);
        errorGamemode.setOpacity(0);
        int i = 0;
        for (Integer lobbyId : playersMap.keySet()) {
            lobbies.getItems().add(i, lobbyId + "                     " + playersMap.get(lobbyId) + "                      " + gameModeMap.get(lobbyId));
        }
    }

    public void join(ActionEvent e) {
        String choice = lobbies.getSelectionModel().getSelectedItem();
        int lobbyId = Integer.parseInt(choice.substring(0, 1));
        int numberOfPlayer = Integer.parseInt(choice.substring(22, 23));
        if(choice.contains("experts"))
            gui.setGameMode("experts");
        else
            gui.setGameMode("beginners");

        System.out.println(lobbyId + "   " + numberOfPlayer);

        joinButton.setDisable(true);
        createButton.setDisable(true);
        refreshButton.setDisable(true);

        gui.sendMessage(new JoinLobby(lobbyId));
    }


    public void createLobby(ActionEvent e) {
        if (gamemode.getValue() == null || numberPlayers.getValue() == null) {
            if (gamemode.getValue() == null && numberPlayers.getValue() != null) {
                errorGamemode.setOpacity(1);
                errorPlayer.setOpacity(0);
            } else if (gamemode.getValue() != null && numberPlayers.getValue() == null) {
                errorPlayer.setOpacity(1);
                errorGamemode.setOpacity(0);
            } else if (gamemode.getValue() == null && numberPlayers.getValue() == null) {
                errorPlayer.setOpacity(1);
                errorGamemode.setOpacity(1);
            }
        } else {
            gui.setGameMode(gamemode.getValue());

            joinButton.setDisable(true);
            createButton.setDisable(true);
            refreshButton.setDisable(true);

            gui.setGameMode(gamemode.getValue());
            gui.sendMessage(new CreateLobby(numberPlayers.getValue(), gamemode.getValue()));
        }
    }

    public void lobbyRefresh() {
        lobbies.getItems().clear();
        gui.sendMessage(new GetLobbies());
    }

    public void playerJoining(String playerJoined, int playersRemaining) {
        this.playerRemaining.setText(String.valueOf(playersRemaining));
        this.playerJoined.setText(playerJoined + " joining the lobby");
        this.playerRemaining.setOpacity(1);
        this.playerJoined.setOpacity(1);
        waitingMessage.setOpacity(1);
        playerText.setOpacity(1);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbies.getSelectionModel().selectedItemProperty().addListener((observableValue, selectionMode, t1) -> {
            joinConfirm.setOpacity(1);
            joinButton.setDisable(false);
        });
        numberPlayers.getItems().add(0, 2);
        numberPlayers.getItems().add(1, 3);
        gamemode.getItems().add(0, "beginners");
        gamemode.getItems().add(1, "experts");
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
