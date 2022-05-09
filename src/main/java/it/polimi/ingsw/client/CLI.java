package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
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
    private ActionMovesHandler actionMovesHandler;
    private String clientNickname;
    private String gameMode;


    public CLI(String serverIp, int serverPort) {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientModel = new ClientModel();
        this.actionMovesHandler = new ActionMovesHandler();
        connectionToServer = new ServerConnection(serverIp, serverPort, new ServerMessageHandler(clientModel, this, actionMovesHandler));
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
            System.out.println("Insert your nickname : ");
            nickname = input.nextLine();
        } while (nickname == null);
        System.out.println("Checking nickname validity...");
        ClientMessage message = new NicknameRequest(nickname);
        clientNickname = nickname;
        connectionToServer.sendMessageToServer(message);
    }

    public void lobbyRefresh() {
        connectionToServer.sendMessageToServer(new GetLobbies());
    }

    public void lobbySetup(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        Set<Integer> attendingLobbies = attendingLobbiesNumberOfPlayerMap.keySet();

        printLobbies(attendingLobbiesNumberOfPlayerMap, attendingLobbiesGameModeMap);

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
                this.gameMode = gameMode;
                actionMovesHandler.setGameMode(gameMode);
                actionMovesHandler.setNumberOfPlayer(numberOfPlayer);
                connectionToServer.sendMessageToServer(new CreateLobby(numberOfPlayer, gameMode));
                break;

            case "join":
                if (attendingLobbies.isEmpty()) {
                    System.out.println("There are not available lobbies");
                    System.out.println("Lobbies refreshing...");
                    lobbyRefresh();
                    return;
                }

                System.out.println("Select lobbyId : ");
                int lobbyId = getInt();
                if (!attendingLobbies.contains(lobbyId)) {
                    System.out.println("The chosen LobbyId is not valid");
                    System.out.println("Lobbies refreshing...");
                    input.nextLine();
                    lobbyRefresh();
                    return;
                }

                this.gameMode = attendingLobbiesGameModeMap.get(lobbyId);
                numberOfPlayer = attendingLobbiesNumberOfPlayerMap.get(lobbyId);
                actionMovesHandler.setGameMode(this.gameMode);
                actionMovesHandler.setNumberOfPlayer(numberOfPlayer);

                connectionToServer.sendMessageToServer(new JoinLobby(lobbyId));
                break;
        }
    }

    public void startGame(List<String> players, String gameMode) {
        System.out.println("The game is Starting....");
        System.out.println("Participants : " + players);
        clientModel.initGame(players, gameMode);
    }

    public void pianificationPhase(String targetPlayer) {
        if (!clientNickname.equals(targetPlayer)) {
            System.out.println(targetPlayer + " pianification turn");
            return;
        }
        printPLayerBoards();
        String check = StringCheckAssistants();
        printAssistants();
        System.out.println("Assistants played during this turn " + clientModel.getAssistantsPlayed());
        System.out.println("Chose one of your assistants : " + "[" + check + "]");
        String choice = getString(check);
        choice = choice.toUpperCase();
        AssistantName name = AssistantName.valueOf(choice);
        clientModel.setLastAssistantPlayed(name);
        clientModel.removeAssistant(name);
        connectionToServer.sendMessageToServer(new ChosenAssistant(clientNickname, name));
    }

    public void actionPhase(String targetPlayer) {
        if (!clientNickname.equals(targetPlayer)) {
            System.out.println(targetPlayer + " action turn");
            return;
        }
        String availableActions = actionMovesHandler.getAvailableActions();
        System.out.println("My turn");

        printPLayerBoards();
        printLegendActionPhaseCommand();

        System.out.println("Select one of the available actions : [" + availableActions + "]");

        String action = getString(availableActions);

        switch (action) {
            case "ms":
                moveStudents();
                break;
            case "mmn":
                moveMotherNature();
                break;
            case "sc":
                selectCloud();
                break;
            case "et":
                System.out.println("Ending your action turn");
                actionMovesHandler.consumeAction(ActionMove.END_TURN);
                connectionToServer.sendMessageToServer(new EndActionPhase(clientNickname));
                break;
            case "ac":
                activateCharacterCard();
        }

    }

    public void moveStudents() {
        printEntrances();
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);

        System.out.println("Insert one student from your entrance [yellow/blue/green/red/pink]");
        String student = selectColor();
        Color color = Color.valueOf(student);
        while (!clientModel.getEntrances().get(clientNickname).contains(color)) {
            printEntrances();
            System.out.println("The color chosen is not in your entrance");
            System.out.println("Insert one student from your entrance [yellow/blue/green/red/pink]");
            student = selectColor();
            color = Color.valueOf(student);
        }

        System.out.println("[md] to move the student to your dining room");
        System.out.println("[mi] to move the student to one island");
        System.out.println("Choose one these actions [md/mi]");
        String choice = getString("md/mi");
        switch (choice) {
            case "md":
                System.out.println("Moving the student to your dining room...");
                connectionToServer.sendMessageToServer(new MoveStudentOnDiningRoom(clientNickname, color));
                break;
            case "mi":
                int islandId = islandSelection();
                System.out.println("Moving student to the selected island ID " + islandId + "...");
                connectionToServer.sendMessageToServer(new MoveStudentToIsland(clientNickname, islandId, color));
        }
    }

    public void moveMotherNature() {
        int choice;
        int motherNatureMove = clientModel.getMaxMotherNatureMove();
        actionMovesHandler.consumeAction(ActionMove.MOVE_MOTHER_NATURE);

        printIslands();
        System.out.println("You can move mother nature of " + motherNatureMove + " position");
        System.out.println("Insert how much do you want to move Mother Nature :");
        choice = getInt();
        while (choice <= 0 || choice > motherNatureMove) {
            System.out.println("Error... You can move mother nature of " + motherNatureMove + " position");
            System.out.println("Insert how much do you want to move Mother Nature :");
            choice = getInt();
        }

        System.out.println("Moving Mother Nature...");
        connectionToServer.sendMessageToServer(new ChosenMotherNatureMove(clientNickname, choice));
    }

    public void selectCloud() {
        Map<Integer, List<Color>> clouds = clientModel.getClouds();
        actionMovesHandler.consumeAction(ActionMove.SELECT_CLOUD);

        printClouds();
        System.out.println("Insert the ID of the selected cloud :");

        int cloudId = getInt();
        while (!clouds.containsKey(cloudId)) {
            System.out.println("The cloudID chosen is not valid");
            printClouds();
            System.out.println("Insert the ID of the selected cloud :");
            cloudId = getInt();
        }
        System.out.println("Filling entrance...");
        connectionToServer.sendMessageToServer(new SelectedCloud(clientNickname, cloudId));
    }

    public void activateCharacterCard() {
        printCoins();
        printCharacterCards();

        String check = StringCheckCharacterCard();
        System.out.println("Insert the name of the Character card you want to activate: [" + check + "]");
        String choice = getString(check);

        CharacterCardView card = clientModel.getCards().get(CharacterName.valueOf(choice.toUpperCase()));

        if (card.getCoinsRequired() > clientModel.getCoins().get(clientNickname)) {
            System.out.println("You haven't enough coins");
            actionPhase(clientNickname);
            return;
        }

        CharacterName name = CharacterName.valueOf(choice.toUpperCase());
        CharacterCardType type = card.getType();
        actionMovesHandler.consumeAction(ActionMove.ACTIVATE_CARD);
        System.out.println(type);
        switch (type) {
            case NORMAL:
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case ISLAND_SELECTION:
                int islandId = islandSelection();
                connectionToServer.sendMessageToServer(new SelectedIsland(clientNickname, islandId));
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case COLOR_SELECTION:
                String color = selectColor();
                connectionToServer.sendMessageToServer(new SelectedColor(clientNickname, Color.valueOf(color)));
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
        }
    }

    public int islandSelection() {
        printIslands();
        int numberOfIsland = clientModel.getIslandsViewMap().size();
        System.out.println("Insert the ID of the selected island :");

        int islandId = getInt();
        while (islandId < 0 || islandId >= numberOfIsland) {
            System.out.println("The islandID chosen is not valid");
            printIslands();
            System.out.println("Insert the ID of the selected island :");
            islandId = getInt();
        }
        return islandId;
    }

    public String selectColor() {
        String color = getString("yellow/blue/green/red/pink");
        return color.toUpperCase();
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
            if (!connectionToServer.isConnected())
                break;
            if (!choice.equals(""))
                System.out.println("Insert one of these options : [" + check + "]");
            choice = input.nextLine().toLowerCase();
        }
        input.reset();
        return choice;
    }

    public int getInt() {
        input.reset();
        int number = -1;
        while (input.hasNext() && connectionToServer.isConnected()) {
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

    public String getClientNickname() {
        return clientNickname;
    }

    private void printLobbies(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        Set<Integer> attendingLobbies = attendingLobbiesNumberOfPlayerMap.keySet();
        System.out.println("----Lobbies----");
        for (Integer lobbyId : attendingLobbies) {
            System.out.println("LobbyId : " + lobbyId + " --NumberOfPlayer : " + attendingLobbiesNumberOfPlayerMap.get(lobbyId) + " --GameMode : " + attendingLobbiesGameModeMap.get(lobbyId));
            System.out.println("----------------");
        }
    }

    private void printPLayerBoards() {
        System.out.println("\n");
        printClouds();
        System.out.println("\n");
        printEntrances();
        System.out.println("\n");
        printDiningRooms();
        System.out.println("\n");
        printProfessors();
        System.out.println("\n");
        printTowers();
        System.out.println("\n");
        if (gameMode.equals("experts")) {
            printCoins();
            printCharacterCards();
            System.out.println("\n");
        }
        printIslands();
        System.out.println("");
    }

    private void printDiningRooms() {
        Map<String, List<Integer>> diningRooms = clientModel.getDiningRooms();

        System.out.println("----DiningRooms----");
        for (String player : diningRooms.keySet()) {
            if (player.equals(clientNickname))
                System.out.println("Your----DiningRoom----");
            else
                System.out.println(player + "----DiningRoom----");
            for (Color color : Color.values()) {
                System.out.println(color + ": " + diningRooms.get(player).get(color.ordinal()));
            }
        }
        System.out.println("----------------");
    }

    private void printEntrances() {
        Map<String, List<Color>> entrances = clientModel.getEntrances();

        System.out.println("----Entrances----");
        for (String player : entrances.keySet()) {
            if (player.equals(clientNickname)) {
                System.out.println("Your----Entrance----");
            } else {
                System.out.println(player + "----Entrance----");
            }
            System.out.println(entrances.get(player));
        }
        System.out.println("----------------");
    }

    private void printIslands() {
        Map<Integer, IslandView> islands = clientModel.getIslandsViewMap();
        int motherNaturePosition = clientModel.getMotherNaturePosition();

        System.out.println("----Islands----");
        for (int index : islands.keySet()) {
            System.out.println("Island ID : " + index);
            if (index == motherNaturePosition)
                System.out.println("((MOTHER_NATURE))");
            System.out.println("students : " + islands.get(index).getStudents());
            System.out.println("owner : " + islands.get(index).getOwner());
            System.out.println("number of towers : " + islands.get(index).getNumberOfTower());
            if (clientModel.getCards().containsKey(CharacterName.HERBALIST)) {
                System.out.println("ban cards : " + islands.get(index).getBanCards());
            }
            System.out.println("----------------");
        }
    }

    private void printProfessors() {
        Map<String, List<Color>> professors = clientModel.getProfessors();

        System.out.println("----Professors----");
        for (String player : professors.keySet()) {
            if (player.equals(clientNickname))
                System.out.println("Your----Professor----");
            else
                System.out.println(player + "----Professor----");
            System.out.println(professors.get(player));
        }
        System.out.println("----------------");
    }

    private void printTowers() {
        Map<String, Integer> towers = clientModel.getTowers();

        System.out.println("----Towers----");
        for (String player : towers.keySet()) {
            if (player.equals(clientNickname))
                System.out.println("You have " + towers.get(player) + " towers");
            else
                System.out.println(player + " has " + towers.get(player) + " towers");
        }
        System.out.println("----------------");
    }

    private void printClouds() {
        Map<Integer, List<Color>> clouds = clientModel.getClouds();

        System.out.println("----Clouds----");
        for (int cloudId : clouds.keySet()) {
            System.out.println("Cloud ID : " + cloudId);
            System.out.println(clouds.get(cloudId));
            System.out.println("----------------");
        }
    }

    private String StringCheckAssistants() {
        List<AssistantName> assistants = clientModel.getAssistants();
        StringBuilder check = new StringBuilder();
        for (AssistantName assistant : assistants) {
            check.append("/").append(assistant);
        }
        check = new StringBuilder(check.substring(1, check.length()));
        return check.toString().toLowerCase();
    }

    public void printAssistants() {
        List<AssistantName> assistants = clientModel.getAssistants();

        System.out.println("----Assistants----");
        for (AssistantName assistant : assistants) {
            System.out.println(assistant);
            System.out.println("MotherNatureMove : " + assistant.getMotherNatureMove());
            System.out.println("PlayerPriority : " + assistant.getValue());
            System.out.println("----------------");
        }
    }

    public void printLegendActionPhaseCommand() {
        System.out.println("[ms] to move students from entrance");
        System.out.println("[mmn] to move Mother Nature ");
        if (gameMode.equals("experts"))
            System.out.println("[ac] to activate a character card");
        System.out.println("[sc] to select a cloud");
        System.out.println("[et] to end the action phase");
    }

    public void printCoins() {
        Map<String, Integer> coins = clientModel.getCoins();

        System.out.println("----Coins----");
        for (String player : coins.keySet()) {
            if (player.equals(clientNickname))
                System.out.println("You have " + coins.get(player) + " coins");
            else
                System.out.println(player + " has " + coins.get(player) + " coins");
            System.out.println("----------------");
        }
    }

    private String StringCheckCharacterCard() {
        Set<CharacterName> cards = clientModel.getCards().keySet();
        StringBuilder check = new StringBuilder();
        for (CharacterName name : cards) {
            check.append("/").append(name);
        }
        check = new StringBuilder(check.substring(1, check.length()));
        return check.toString().toLowerCase();
    }

    public void printCharacterCards() {
        Map<CharacterName, CharacterCardView> cards = clientModel.getCards();
        CharacterCardView card;

        System.out.println("----CharacterCards----");
        for (CharacterName name : cards.keySet()) {
            card = cards.get(name);
            System.out.println(name);
            System.out.println("Coins required : " + card.getCoinsRequired());
            if (card.getStudents() != null)
                System.out.println("Students : " + card.getStudents());
            if (name == CharacterName.HERBALIST)
                System.out.println("Number of BanCards : " + card.getBanCards());
            System.out.println("----------------");
        }
    }

}
