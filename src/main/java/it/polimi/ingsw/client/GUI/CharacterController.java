package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.client.CharacterCardView;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.ActiveEffect;
import it.polimi.ingsw.network.messages.clientMessage.SelectedColor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class CharacterController implements GUIController, Initializable {
    private GUI gui;

    private List<Node> elements;
    private List<AnchorPane> grids;
    private List<ImageView> characters;
    private List<Label> coins;
    private List<RadioButton> buttons;

    @FXML
    private AnchorPane grid1;
    @FXML
    private AnchorPane grid2;
    @FXML
    private AnchorPane grid3;
    @FXML
    private ImageView character1;
    @FXML
    private ImageView character2;
    @FXML
    private ImageView character3;
    @FXML
    private Label coins1;
    @FXML
    private Label coins2;
    @FXML
    private Label coins3;
    @FXML
    private ToggleGroup cards;
    @FXML
    private RadioButton card1;
    @FXML
    private RadioButton card2;
    @FXML
    private RadioButton card3;
    @FXML
    private Button activateButton;


    public void update() {
        Map<CharacterName, CharacterCardView> cards = gui.getClientModel().getCards();
        int i = 0;

        for (CharacterName name : cards.keySet()) {
            buttons.get(i).setUserData(name);
            buildCard(grids.get(i), name, cards.get(name), i);
            i++;
        }
    }

    public void clear() {
        for (AnchorPane grid : grids) {
            for (Node element : elements) {
                grid.getChildren().remove(element);
            }
        }

        for (RadioButton button : buttons) {
            button.setDisable(true);
            button.setOpacity(0);
        }

        activateButton.setDisable(true);
        activateButton.setOpacity(0);
    }

    public void initCharacterButton() {
        Map<CharacterName, CharacterCardView> cardViewMap = gui.getClientModel().getCards();

        if (cards.getSelectedToggle() != null)
            cards.getSelectedToggle().setSelected(false);

        for (RadioButton button : buttons) {
            button.setDisable(false);
            button.setOpacity(1);
        }

        activateButton.setDisable(true);
        activateButton.setOpacity(1);
    }

    public void activateCard() {
        CharacterName name = (CharacterName) cards.getSelectedToggle().getUserData();
        AnchorPane pane = searchGrid();
        CharacterCardView card = gui.getClientModel().getCards().get(name);
        CharacterCardType type = card.getType();


        if (!checkActivation(name))
            return;

        switch (type) {
            case COLOR_SELECTION -> handleColorSelection(pane, name);
            case NORMAL -> {
                if(name.equals(CharacterName.POSTMAN))
                    gui.getClientModel().postmanActivation();
                closeWindows();
                gui.sendMessage(new ActiveEffect(gui.getClientNickname(), name));
            }
            case ISLAND_SELECTION -> {
                closeWindows();
                gui.islandSelection(name);
            }
            case PRINCESS -> {
                closeWindows();
                gui.handlePrincessActivation();
            }
            case MONK -> {
                closeWindows();
                gui.handleMonkActivation();
            }
            case JESTER -> {
                closeWindows();
                gui.handleJesterActivation();
            }
            case MUSICIAN -> {
                closeWindows();
                gui.handleMusicianActivation();
            }
        }
    }

    private void closeWindows() {
        Stage stage = (Stage) activateButton.getScene().getWindow();
        stage.close();
    }


    private boolean checkActivation(CharacterName name) {
        String nickname = gui.getClientNickname();
        ClientModel model = gui.getClientModel();
        CharacterCardView card = model.getCards().get(name);
        int cardCost = card.getCoinsRequired();

        if (model.getCoins().get(nickname) < cardCost) {
            activateButton.setDisable(true);
            gui.alertMessage("You don't have enough money");
            return false;
        }

        if (name.equals(CharacterName.MUSICIAN) && !checkMusicianActivation(gui.getClientNickname())) {
            activateButton.setDisable(true);
            gui.alertMessage("You don't have any students in your dining room");
            return false;
        }


        return true;
    }

    private boolean checkMusicianActivation(String nickname) {
        int numberOfStudents = 0;
        List<Integer> dining = gui.getClientModel().getDiningRooms().get(nickname);

        for (int student : dining)
            numberOfStudents += student;

        return numberOfStudents != 0;
    }

    private void handleColorSelection(AnchorPane pane, CharacterName name) {
        ChoiceBox<String> choiceBox = new ChoiceBox<String>();
        Label message = new Label("Select one of the following color");
        Button confirmButton = new Button();

        choiceBox.setLayoutX(109);
        choiceBox.setLayoutY(440);
        choiceBox.setPrefWidth(150);

        message.setLayoutX(98);
        message.setLayoutY(405);

        confirmButton.setText("Confirm");
        confirmButton.setLayoutX(155);
        confirmButton.setLayoutY(496);
        confirmButton.setOnAction(actionEvent -> {
            if (choiceBox.getValue() == null) {
                gui.alertMessage("Please Select one color");
                return;
            }
            Color color = Color.valueOf(choiceBox.getValue().toUpperCase());
            gui.consumeAction(ActionMove.ACTIVATE_CARD);
            gui.sendMessage(new SelectedColor(gui.getClientNickname(), color));
            gui.sendMessage(new ActiveEffect(gui.getClientNickname(), name));
            closeWindows();
        });

        activateButton.setDisable(true);

        for (RadioButton button : buttons)
            button.setDisable(true);

        for (Color color : Color.values())
            choiceBox.getItems().add(color.name().toLowerCase());

        elements.addAll(Arrays.asList(choiceBox, confirmButton, message));
        pane.getChildren().addAll(Arrays.asList(choiceBox, confirmButton, message));
    }

    private AnchorPane searchGrid() {
        for (AnchorPane grid : grids) {
            if (grid.getChildren().contains(cards.getSelectedToggle()))
                return grid;
        }
        return null;
    }


    private void buildCard(AnchorPane grid, CharacterName name, CharacterCardView card, int index) {
        int numberOfCoins = card.getCoinsRequired();
        List<Color> students = card.getStudents();

        characters.get(index).setImage(gui.getCharacterImage(name));

        addCoins(numberOfCoins, index);

        if (students != null)
            addStudents(grid, students);
        if (name.equals(CharacterName.HERBALIST))
            addBanCards(grid, card.getBanCards());
    }

    private void addCoins(int numberOfCoins, int index) {
        Label text = coins.get(index);
        text.setText(numberOfCoins + " x");
    }

    private void addStudents(AnchorPane grid, List<Color> students) {
        ImageView student;
        TilePane pane = new TilePane();
        pane.setPrefSize(165, 99);
        pane.setLayoutX(101);
        pane.setLayoutY(368);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(10);
        pane.setVgap(10);

        grid.getChildren().add(pane);
        elements.add(pane);

        for (Color color : students) {
            student = new ImageView(gui.getStudentImage(color));
            student.setPreserveRatio(true);
            student.smoothProperty();
            student.setFitWidth(48);
            student.setFitHeight(48);
            pane.getChildren().add(student);
        }

    }

    private void addBanCards(AnchorPane grid, int bancards) {
        ImageView banCard;
        TilePane pane = new TilePane();
        pane.setPrefSize(165, 99);
        pane.setLayoutX(101);
        pane.setLayoutY(368);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(10);
        pane.setVgap(10);

        grid.getChildren().add(pane);
        elements.add(pane);

        for (int i = 0; i < bancards; i++) {
            banCard = new ImageView(new Image(String.valueOf(getClass().getResource("/Imagines/ban_card.png"))));
            banCard.setPreserveRatio(true);
            banCard.smoothProperty();
            banCard.setFitWidth(48);
            banCard.setFitHeight(48);
            pane.getChildren().add(banCard);
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grids = new ArrayList<>(Arrays.asList(grid1, grid2, grid3));
        characters = new ArrayList<>(Arrays.asList(character1, character2, character3));
        coins = new ArrayList<>(Arrays.asList(coins1, coins2, coins3));
        buttons = new ArrayList<>(Arrays.asList(card1, card2, card3));
        elements = new ArrayList<>();

        activateButton.setDisable(true);
        activateButton.setOpacity(0);

        cards.selectedToggleProperty().addListener((observableValue, toggle, t1) -> activateButton.setDisable(false));
    }
}
