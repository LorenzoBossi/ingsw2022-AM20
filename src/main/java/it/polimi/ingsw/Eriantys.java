package it.polimi.ingsw;

import it.polimi.ingsw.client.CLI;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.Screen;
import javafx.application.Application;


public class Eriantys {
    public static void main(String[] args) {
        Screen.clear();
        printTitle();
        int port = 26000;
        String ip = "localhost";
        boolean startServer = false, startCli = false;
        int numberOfArgs = args.length;
        String arg;

        //more checks on parameters to be added (intValue() not needed)
        try {
            for (int i = 0; i < numberOfArgs; i++) {
                arg = args[i];
                if (arg.equals("--cli") || arg.equals("-c")) {
                    startCli = true;
                } else if (arg.equals("--server") || arg.equals("-s")) {
                    startServer = true;
                } else if (arg.equals("--ip") || arg.equals("-i")) {
                    if (numberOfArgs >= i + 1) {
                        ip = args[i + 1];
                    }
                } else if (arg.equals("--port") || arg.equals("-p")) {
                    if (numberOfArgs >= i + 1) {
                        port = Integer.parseInt(args[i + 1]);
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Invalid parameters");
            System.exit(-1);
        }


        if (startServer) {
            System.out.println("STARTING SERVER on port: " + port);
            startServer(port);

        } else if (startCli) {
            System.out.println("STARTING CLIENT (CLI) , connecting to : (" + ip + " , " + port + ")");
            startCLI(ip, port);
        } else {
            System.out.println("STARTING CLIENT (GUI)");
            //GUI gui= new GUI(ip,port);fitdkcf
            startGUI(ip, port);
        }

    }

    private static void startServer(int port) {

        System.out.println("Server running on port : " + port);
        Server server = new Server(port);
        server.startConnections();
    }

    private static void startCLI(String ip, int port) {
        try {
            CLI cli = new CLI(ip, port);
            cli.nicknameSetup();
        } catch (Exception e) {
            System.out.println("Invalid args");
            System.exit(0);
        }
    }

    private static void startGUI(String ip, int port) {

        GUI.main(new String[0]);

        //Not implemented yet
        /*
        try {
            GUI gui = new GUI(ip, port);
            gui.nicknameSetup();
        } catch (Exception e) {
            System.out.println("Invalid args");
            System.exit(0);
        }

         */
    }


    private static void printTitle() {
        System.out.println("ERIANTYS");
    }
}

