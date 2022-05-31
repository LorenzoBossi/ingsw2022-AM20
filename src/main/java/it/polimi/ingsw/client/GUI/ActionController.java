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

import java.net.URL;
import java.util.*;

public class ActionController extends BoardUpdater implements GUIController, Initializable {
    private boolean isMnnActive;

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

    private void initCharacterButton() {
        Button button = new Button();
        button.setPrefSize(90, 50);
        button.setLayoutX(943);
        button.setLayoutY(40);
        button.setText("Characters");
        topPane.getChildren().add(button);

        button.setOnAction(actionEvent -> gui.showCharacters(false));
    }

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
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            gui.showCharacters(true);
                        }
                    });
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

    public void clearAvailableActions() {
        availableActions.getChildren().clear();
    }

    public void showIslands() {
        gui.showIslands();
    }

    public void showClouds() {
        gui.showClouds(false);
    }

    public void activeMotherNatureMove() {
        if(isMnnActive)
            return;
        int maxMotherNatureMove = gui.getClientModel().getMaxMotherNatureMove();
        Label message = new Label("Select how much you want to move MotherNature");
        ChoiceBox<Integer> options = new ChoiceBox<>();
        Button confirm = new Button();

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
        for (int i = 1; i <= maxMotherNatureMove; i++) {
            options.getItems().add(i);
        }

        isMnnActive = true;
        topPane.getChildren().addAll(Arrays.asList(message, options, confirm));
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
