package it.polimi.ingsw.utils;

import java.io.IOException;

public class Screen {
    public static void clear(){
       // "\f is printed only in Intellij console, not visible in the jar"
        try {
            if (System.getProperty("os.name").contains("Windows"))

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }
}

