package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.clientMessage.*;

import java.io.PrintStream;
import java.util.*;

public class CLI {
    private String serverIp;
    private int serverPort;
    private ServerConnection connectionToServer;
    private Scanner input;
    private PrintStream output;
    private ClientModel clientModel;
    private String clientNickname;


    public CLI(String serverIp, int serverPort) {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientModel = new ClientModel();
        connectionToServer = new ServerConnection(serverIp, serverPort, new ServerMessageHandler(clientModel, this));
        connectionToServer.setupConnection();
        (new Thread(connectionToServer)).start();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert server ip :");
        String ip = scanner.next();
        System.out.println("Insert server port :");
        try {
            int port = scanner.nextInt();
            CLI cli = new CLI(ip, port);
            cli.nicknameSetup();
        } catch (InputMismatchException e) {
            System.out.println("Integer requested for the server port");
            System.exit(0);
        }
    }

    public void nicknameSetup() {
        String nickname = null;
        do {
            System.out.println("Insert game's nickname : ");
            nickname = input.nextLine();
        } while (nickname == null);
        ClientMessage message = new NicknameRequest(nickname);
        clientNickname = nickname;
        connectionToServer.sendMessageToServer(message);
    }

    public void lobbyRefresh() {
        connectionToServer.sendMessageToServer(new GetLobbies());
    }

    public void lobbySetup(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        Set<Integer> attendingLobbies = attendingLobbiesNumberOfPlayerMap.keySet();
        for (Integer lobbyId : attendingLobbies) {
            System.out.println("LobbyId : " + lobbyId + " NumberOfPlayer : " + attendingLobbiesNumberOfPlayerMap.get(lobbyId));
        }

        System.out.println("Create lobby or join at existing one [create/join]");
        String choice = getString("create/join");

        switch (choice) {
            case "create":
                System.out.println("Select number of player [2/3]");
                int numberOfPlayer = getInt();
                while (numberOfPlayer < 2 || numberOfPlayer > 3) {
                    System.out.println("The number of players must be 2 or 3");
                    System.out.println("Select number of player [2/3]");
                    numberOfPlayer = getInt();
                }
                System.out.println("Choose the gameMode [beginners/experts]");
                String gameMode = getString("beginners/experts");
                connectionToServer.sendMessageToServer(new CreateLobby(numberOfPlayer, gameMode));
                break;

            case "join":
                if (attendingLobbies.isEmpty()) {
                    System.out.println("There are not available lobbies");
                    lobbyRefresh();
                    return;
                }

                System.out.println("Select lobbyId : ");
                int lobbyId = getInt();
                if (!attendingLobbies.contains(lobbyId)) {
                    System.out.println("The chosen LobbyId is not valid");
                    input.nextLine();
                    lobbyRefresh();
                    return;
                }
                connectionToServer.sendMessageToServer(new JoinLobby(lobbyId));
                break;
        }
    }

    public void startGame(List<String> players, String gameMode) {
        System.out.println("The game is Starting....");
        System.out.println("Participants : " + players);
        clientModel.initGame(players, gameMode);
    }

    public void PianificationPhase(String targetPlayer) {
        if(!clientNickname.equals(targetPlayer)) {
            System.out.println(targetPlayer + " pianification turn");
            return;
        }
        Map<String, List<Color>> entrances = clientModel.getEntrances();
        for(String player : entrances.keySet()) {
            System.out.println(player + "----entrance----");
            System.out.println(entrances.get(player));
        }
    }

    public boolean checkString(String input, String check) {
        String[] tokens = check.split("/");
        for (String token : tokens) {
            if (input.equals(token))
                return true;
        }
        return false;
    }

    public String getString(String check) {
        input.reset();
        String choice = input.nextLine().toLowerCase();
        while (!checkString(choice, check)) {
            System.out.println("Insert one of these options : " + check);
            choice = input.nextLine().toLowerCase();
        }
        return choice;
    }

    public int getInt() {
        input.reset();
        int number = -1;
        while (input.hasNext()) {
            if (input.hasNextInt()) {
                number = input.nextInt();
                break;
            } else {
                System.out.println("Invalid format, please insert an Integer");
                input.next();
            }
        }
        return number;
    }

}
