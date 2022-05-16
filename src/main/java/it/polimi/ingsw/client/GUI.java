package it.polimi.ingsw.client;

import java.awt.*;
import java.io.PrintStream;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class GUI extends Application{


    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.PINK);

        Image icon = new Image("Eriantys.png");
        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");

        stage.setScene(scene);
        stage.show();
    }
}
