package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.client.CharacterCardView;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.ActiveEffect;
import it.polimi.ingsw.network.messages.clientMessage.SelectedColor;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Class CharacterController the controller of the Character Scene
 */
public class CharacterController implements GUIController{
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
    @FXML
    private Label descrLabel;
    @FXML
    private Button descButton1;
    @FXML
    private Button descButton2;
    @FXML
    private Button descButton3;


    /**
     * Updates the character scene
     */
    public void update() {
        Map<CharacterName, CharacterCardView> cards = gui.getClientModel().getCards();
        int i = 0;

        for (CharacterName name : cards.keySet()) {
            buttons.get(i).setUserData(name);
            buildCard(grids.get(i), name, cards.get(name), i);
            i++;
        }
    }

    /**
     * Clears the character scene
     */
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

    /**
     * Initializes the character card button to activate the cards
     */
    public void initCharacterButton() {

        if (cards.getSelectedToggle() != null)
            cards.getSelectedToggle().setSelected(false);

        for (RadioButton button : buttons) {
            button.setDisable(false);
            button.setOpacity(1);
        }

        activateButton.setDisable(true);
        activateButton.setOpacity(1);
    }

    /**
     * Handles the character cards activation
     */
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
                if(name.equals(CharacterName.POSTMAN)) {
                    gui.getClientModel().postmanActivation();
                    gui.handlePostmanActivation();
                }
                closeWindows();
                gui.consumeAction(ActionMove.ACTIVATE_CARD);
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

    /**
     * Closes the stage of the character scene
     */
    private void closeWindows() {
        Stage stage = (Stage) activateButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Check if the client can activate the character card
     * @param name the name of the character card
     * @return true if the client can activate the cards, false otherwise
     */
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

        if(name.equals(CharacterName.HERBALIST) && !checkBanCharacterActivation(card)) {
            activateButton.setDisable(true);
            gui.alertMessage("You don't have any ban cards on this card");
            return false;
        }

        return true;
    }

    /**
     * Checks if the client can activate the ban character
     * @param card the character card
     * @return true if the client can activate the ban character, false otherwise
     */
    private boolean checkBanCharacterActivation(CharacterCardView card) {
        return card.getBanCards() != 0;
    }

    /**
     * Checks if the client can activate the musician character card
     * @param nickname the nickname of the player
     * @return true if the client can activate the musician character, false otherwise
     */
    private boolean checkMusicianActivation(String nickname) {
        int numberOfStudents = 0;
        List<Integer> dining = gui.getClientModel().getDiningRooms().get(nickname);

        for (int student : dining)
            numberOfStudents += student;

        return numberOfStudents != 0;
    }

    /**
     * Handles the color selection to activate a card of the type COLOR_SELECTION
     * @param pane the pane where the character card is
     * @param name the name of the character card
     */
    private void handleColorSelection(AnchorPane pane, CharacterName name) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
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
            gui.sendMessage(new SelectedColor(gui.getClientNickname(), color));
            gui.consumeAction(ActionMove.ACTIVATE_CARD);
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

    /**
     * Searches the pane where the card activated is
     * @return the pane where the card activated is
     */
    private AnchorPane searchGrid() {
        for (AnchorPane grid : grids) {
            if (grid.getChildren().contains(cards.getSelectedToggle()))
                return grid;
        }
        return null;
    }


    /**
     * Adds the character cards into the scene
     * @param grid the grids where the character card is
     * @param name the name of the character card
     * @param card the card
     * @param index the index of the character card
     */
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

    /**
     * Set to the label how much coins the card required
     * @param numberOfCoins the number of coins required to activate the card
     * @param index the index of the card
     */
    private void addCoins(int numberOfCoins, int index) {
        Label text = coins.get(index);
        text.setText(numberOfCoins + " x");
    }

    /**
     * Adds students under the character card
     * @param grid the grid where the character card is
     * @param students the students to add under the character cards
     */
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

    /**
     * Adds ban cards under the character card
     * @param grid the grid where the character card is
     * @param bancards the ban cards to add under the card
     */
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
            banCard = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/ban_card.png"))));
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

    /**
     * Shows the description of the character card
     */
    public void showDescription() {
        Map<CharacterName, CharacterCardView> cardmap = gui.getClientModel().getCards();
        int i = 0;
        int num = 0;
        String description = null;

        if (descButton1.isHover())
            num = 0;
        if (descButton2.isHover())
            num = 1;
        if (descButton3.isHover())
            num = 2;

        for (CharacterName name : cardmap.keySet()) {
            if (i == num) {
                JSONParser parser = new JSONParser();
                try {
                    InputStream inputstream = getClass().getResourceAsStream("/json/descriptionGUI.json");
                    JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(inputstream, "UTF-8"));
                    description = (String) jsonObject.get(name.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
        }

        descrLabel.setText(description);
        descrLabel.setAlignment(Pos.TOP_CENTER);
        descrLabel.setTextAlignment(TextAlignment.CENTER);
        descrLabel.setFont(Font.font("Regular", 15));
        descrLabel.setWrapText(true);

    }

    /**
     * Hide the character card description
     */
    public void hideDescription() {
        descrLabel.setText("");
    }

}
