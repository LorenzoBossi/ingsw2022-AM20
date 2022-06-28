package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.clientMessage.ChosenMotherNatureMove;
import it.polimi.ingsw.network.messages.clientMessage.EndActionPhase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.*;

/**
 * Class ActionsController is the controller of the Action scene
 */
public class ActionController extends BoardUpdater implements GUIController {

    private boolean isMnnActive;
    private ChoiceBox<Integer> options = new ChoiceBox<>();

    @FXML
    private AnchorPane centralPane;
    @FXML
    private Label turnMessage;
    @FXML
    private AnchorPane topPane;
    @FXML
    private HBox availableActions;
    @FXML
    private HBox playerBoards;
    @FXML
    private ImageView student0;
    @FXML
    private ImageView student1;
    @FXML
    private ImageView student2;
    @FXML
    private ImageView student3;
    @FXML
    private ImageView student4;
    @FXML
    private ImageView student5;
    @FXML
    private ImageView student6;
    @FXML
    private ImageView student7;
    @FXML
    private ImageView student8;
    @FXML
    private TilePane greenDining;
    @FXML
    private TilePane pinkDining;
    @FXML
    private TilePane redDining;
    @FXML
    private TilePane blueDining;
    @FXML
    private TilePane yellowDining;
    @FXML
    private ImageView greenProf;
    @FXML
    private ImageView pinkProf;
    @FXML
    private ImageView redProf;
    @FXML
    private ImageView yellowProf;
    @FXML
    private ImageView blueProf;
    @FXML
    private TilePane towers;
    @FXML
    private Label coinsNumber;
    @FXML
    private ImageView coin;

    /**
     * Init the button to show the character cards
     */
    private void initCharacterButton() {
        Button button = new Button();
        button.setPrefSize(90, 50);
        button.setLayoutX(943);
        button.setLayoutY(40);
        button.setText("Characters");
        topPane.getChildren().add(button);

        button.setOnAction(actionEvent -> gui.showCharacters(false));
    }

    /**
     * Init all the button of the Action scene
     * @param players
     */
    public void initActionButton(List<String> players) {
        List<String> opponents = new ArrayList<>(players);
        opponents.remove(gui.getClientNickname());
        Button button;

        for (String player : opponents) {
            button = new Button();
            button.setPrefSize(130, 40);
            button.setText(player);
            playerBoards.getChildren().add(button);
            button.setOnAction(actionEvent -> gui.showPlayerBoard(player));
        }

        if(gui.getGameMode().equals("experts"))
            initCharacterButton();
    }

    /**
     * Init the button in order to perform the available actions of the client
     * @param availableActions the available actions
     */
    public void initAvailableActions(String availableActions) {
        String[] actions = availableActions.split("/");
        Button button = null;

        for (String action : actions) {
            switch (action) {
                case "ms" -> {
                    button = new Button();
                    button.setPrefSize(130, 40);
                    button.setText("Move Students");
                    button.setOnAction(actionEvent -> gui.moveStudents());
                }
                case "ac" -> {
                    button = new Button();
                    button.setPrefSize(130, 40);
                    button.setAlignment(Pos.CENTER);
                    button.setText("     Activate \nCharacterCard");
                    button.setOnAction(actionEvent -> gui.showCharacters(true));
                }
                case "mmn" -> {
                    button = new Button();
                    button.setPrefSize(130, 40);
                    button.setText("Move Mother Nature");
                    button.setOnAction(actionEvent -> activeMotherNatureMove());
                }
                case "et" -> {
                    button = new Button();
                    button.setPrefSize(130, 40);
                    button.setText("End Turn");
                    button.setOnAction(actionEvent -> {
                        gui.getClientModel().resetPostmanActivation();
                        gui.consumeAction(ActionMove.END_TURN);
                        gui.sendMessage(new EndActionPhase(gui.getClientNickname()));
                    });
                }
                case "sc" -> {
                    button = new Button();
                    button.setPrefSize(130, 40);
                    button.setText("Select cloud");
                    button.setOnAction(actionEvent -> gui.showClouds(true));
                }
            }
            this.availableActions.getChildren().add(button);
        }
    }

    /**
     * Clears the available actions
     */
    public void clearAvailableActions() {
        availableActions.getChildren().clear();
    }

    /**
     * Shows the islands
     */
    public void showIslands() {
        gui.showIslands();
    }

    /**
     * Shows the clouds
     */
    public void showClouds() {
        gui.showClouds(false);
    }

    /**
     * Updates how much you can move mother nature
     */
    public void updateMotherNatureMove() {
        int maxMotherNatureMove = gui.getClientModel().getMaxMotherNatureMove();
        options.getItems().clear();
        for (int i = 1; i <= maxMotherNatureMove; i++) {
            options.getItems().add(i);
        }
    }

    /**
     * Update the label that says whose turn it is
     * @param player the current player
     */
    public void updateTurnMessage(String player) {
        if(player.equals(gui.getClientNickname())) {
            turnMessage.setText("It's your turn");
        } else {
            turnMessage.setText("It's "+ player + " turn");
        }
    }

    /**
     * activates the possibility to move mother nature
     */
    public void activeMotherNatureMove() {
        Label message = new Label("Select how much you want to move MotherNature");
        Button confirm = new Button();

        if(isMnnActive) {
            return;
        }

        message.setLayoutX(506);
        message.setLayoutY(17);

        confirm.setLayoutX(611);
        confirm.setLayoutY(90);
        confirm.setDisable(true);
        confirm.setText("Confirm");
        confirm.setOnAction(actionEvent -> {
            isMnnActive = false;
            gui.consumeAction(ActionMove.MOVE_MOTHER_NATURE);
            topPane.getChildren().removeAll(Arrays.asList(message, options, confirm));
            gui.sendMessage(new ChosenMotherNatureMove(gui.getClientNickname(), options.getValue()));
        });

        options.setLayoutX(565);
        options.setLayoutY(59);
        options.setPrefWidth(150);
        options.setOnAction(actionEvent -> confirm.setDisable(false));
        updateMotherNatureMove();

        isMnnActive = true;
        topPane.getChildren().addAll(Arrays.asList(message, options, confirm));
    }

    /**
     * Updates the view with the end message of the game
     * @param isDraw true if the game finishes with a draw, false otherwise
     * @param winner the nickname of the winner
     */
    public void endGameVisual(boolean isDraw, String winner) {
        Button closeButton = new Button();
        Label closeMessage = new Label("Press the button to close the application");
        Label winnerMessage = new Label();

        closeButton.setText("Close");
        closeButton.setLayoutX(617);
        closeButton.setLayoutY(77);
        closeButton.setOnAction(actionEvent -> gui.stop());

        closeMessage.setLayoutX(533);
        closeMessage.setLayoutY(50);

        winnerMessage.setAlignment(Pos.CENTER);
        winnerMessage.setPrefWidth(519);
        winnerMessage.setTextAlignment(TextAlignment.CENTER);
        winnerMessage.setLayoutX(380);
        winnerMessage.setLayoutY(0);
        winnerMessage.setFont(new Font("system", 30));
        if(isDraw) {
            winnerMessage.setText("There is a Draw");
        } else {
            winnerMessage.setText("The winner is " + winner);
        }

        centralPane.getChildren().clear();
        centralPane.getChildren().addAll(closeButton, closeMessage, winnerMessage);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.students = new ArrayList<>(Arrays.asList(student0, student1, student2, student3, student4, student5, student6, student7, student8));
        this.profs = new ArrayList<>(Arrays.asList(greenProf, redProf, yellowProf, pinkProf, blueProf));
        this.diningRooms = new ArrayList<>(Arrays.asList(greenDining, redDining, yellowDining, pinkDining, blueDining));
        this.towersPane = towers;
        this.coinsLabel = coinsNumber;
        this.coinView = coin;
        this.towerX = 48;
        this.towerY = 44;
        this.studentX = 35;
        this.studentY = 35;
    }
}
