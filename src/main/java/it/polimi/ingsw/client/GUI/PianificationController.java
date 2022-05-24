package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.clientMessage.ChosenAssistant;
import it.polimi.ingsw.network.messages.serverMessage.AssistantPlayed;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PianificationController implements GUIController, Initializable {
    private GUI gui;

    private ToggleGroup group = new ToggleGroup();

    @FXML
    public HBox buttonContainer;
    @FXML
    public TilePane assistantsToPlay;
    @FXML
    public ImageView assistantSelect;
    @FXML
    public Button confirmButton;
    @FXML
    public TilePane assistantsPlayed;
    @FXML
    private Label playerTurn;

    public void initButton(List<String> players) {
        String nickname = gui.getClientNickname();
        Button button;

        for (String player : players) {
            button = new Button();
            button.setMinSize(100, 40);
            if (player.equals(nickname))
                button.setText("Your PlayerBoard");
            else
                button.setText(player + " PlayerBoard");
            button.setOnAction(actionEvent -> gui.showPlayerBoard(player));
            buttonContainer.getChildren().add(button);
        }
    }

    public void clear() {
        confirmButton.setDisable(true);
        group.getToggles().clear();
        assistantsToPlay.getChildren().clear();
        assistantSelect.setImage(null);
        assistantsPlayed.getChildren().clear();
    }

    public void update(List<AssistantName> assistants, List<AssistantName> assistantsPlayed, String currPlayer) {
        ToggleButton toggle;

        clear();
        for (AssistantName name : assistants) {
            ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/Imagines/" + name.name().toLowerCase() + ".png"))));
            imageView.setFitWidth(110);
            imageView.setFitHeight(146);
            imageView.smoothProperty();
            imageView.setPreserveRatio(true);
            toggle = new ToggleButton();
            toggle.setGraphic(imageView);
            toggle.setUserData(name.name().toLowerCase());
            group.getToggles().add(toggle);
            assistantsToPlay.getChildren().add(toggle);
        }
        for (AssistantName name : assistantsPlayed) {
            ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/Imagines/" + name.name().toLowerCase() + ".png"))));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.smoothProperty();
            imageView.setPreserveRatio(true);
            this.assistantsPlayed.getChildren().add(imageView);
        }

        if (gui.getClientNickname().equals(currPlayer)) {
            playerTurn.setText("It's your turn");

            group.getToggles().forEach(t -> {
                Node node = (Node) t;
                node.setDisable(false);
            });
        } else {
            playerTurn.setText("It's " + currPlayer + " turn");

            group.getToggles().forEach(t -> {
                Node node = (Node) t;
                node.setDisable(true);
            });
        }
    }


    public void playAssistant() {
        String choice = (String) group.getSelectedToggle().getUserData();
        AssistantName name = AssistantName.valueOf(choice.toUpperCase());
        gui.sendMessage(new ChosenAssistant(gui.getClientNickname(), name));
    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmButton.setDisable(true);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if (group.getSelectedToggle() != null) {
                    confirmButton.setDisable(false);
                    assistantSelect.setImage(new Image(String.valueOf(getClass().getResource("/Imagines/" + ((String) group.getSelectedToggle().getUserData()) + ".png"))));
                }
            }
        });
    }
}
