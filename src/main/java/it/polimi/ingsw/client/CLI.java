package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.*;
import it.polimi.ingsw.utils.Screen;

import java.io.PrintStream;
import java.util.*;

public class CLI implements View {

    private String serverIp;
    private int serverPort;
    private final ServerConnection connectionToServer;
    private final Scanner input;
    private PrintStream output;

    private final ClientModel clientModel;
    private final ActionMovesHandler actionMovesHandler;
    private String clientNickname;
    private String gameMode;


    /**
     * Constructor
     * @param serverIp server ip address
     * @param serverPort server port
     */
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

    /**
     * method nicknameSetup sets Cli nickname
     */
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

    /**
     * method lobbyRefresh updates the list of lobbies
     */
    public void lobbyRefresh() {
        connectionToServer.sendMessageToServer(new GetLobbies());
    }

    /**
     * method lobbySetup is used to create or join a lobby
     * @param attendingLobbiesNumberOfPlayerMap map of lobby id and number of player for that lobby
     * @param attendingLobbiesGameModeMap map of lobby id and game difficulty
     */
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

    /**
     * method startGame starts the game
     * @param players list of player for the game
     * @param gameMode game mode choice
     * @param towerColorMap map of player and his/her tower color
     */
    public void startGame(List<String> players, String gameMode, Map<String, TowerColor> towerColorMap) {
        System.out.println("The game is Starting....");
        System.out.println("Participants : " + players);
        clientModel.initGame(players, gameMode, towerColorMap);
    }

    /**
     * pianificationPhase method carries out the operations of the pianification phase
     * @param targetPlayer player's pianification turn
     */
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

    /**
     * actionPhase method carries out the operations of the action phase
     * @param targetPlayer player's action turn
     */
    public void actionPhase(String targetPlayer) {
        if (!clientNickname.equals(targetPlayer)) {
            System.out.println(targetPlayer + " action turn");
            return;
        }
        String availableActions = actionMovesHandler.getAvailableActions();
        System.out.println("\n");

        //printPLayerBoards();
        printLegendActionPhaseCommand();

        if (gameMode.equals("experts"))
            availableActions = "pc/gc/".concat(availableActions);
        availableActions = "pe/pd/pt/pi/pp/pcl/".concat(availableActions);
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
                clientModel.resetPostmanActivation();
                System.out.println("Ending your action turn...");
                actionMovesHandler.consumeAction(ActionMove.END_TURN);
                connectionToServer.sendMessageToServer(new EndActionPhase(clientNickname));
                break;
            case "ac":
                activateCharacterCard();
                break;
            case "pe":
                printEntrances();
                actionPhase(clientNickname);
                break;
            case "pd":
                printDiningRooms();
                actionPhase(clientNickname);
                break;
            case "pt":
                printTowers();
                actionPhase(clientNickname);
                break;
            case "pi":
                printIslands();
                actionPhase(clientNickname);
                break;
            case "pc":
                printCharacterCards();
                actionPhase(clientNickname);
                break;
            case "gc":
                printCoins();
                actionPhase(clientNickname);
                break;
            case "pcl":
                printClouds();
                actionPhase(clientNickname);
                break;
            case "pp":
                printProfessors();
                actionPhase(clientNickname);
                break;
        }

    }

    /**
     * method moveStudents moves the selected student from the Entrance to the DiningRoom or to an Island
     */
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

    /**
     * method moveMotherNature moves mother nature than indicated
     */
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

    /**
     * method selectCloud selects a Cloud from which to take the students and place them in the Entrance
     */
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

    /**
     * method activateCharacterCard activates the effect of a Character Card
     */
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
        List<Color> students;
        actionMovesHandler.consumeAction(ActionMove.ACTIVATE_CARD);

        switch (type) {
            case NORMAL:
                if (name == CharacterName.POSTMAN)
                    clientModel.postmanActivation();
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case ISLAND_SELECTION:
                if (name == CharacterName.HERBALIST && card.getBanCards() == 0) {
                    System.out.println("There are no ban card on these card...");
                    actionMovesHandler.handleError(ActionMove.ACTIVATE_CARD);
                    actionPhase(clientNickname);
                    return;
                }
                int islandId = islandSelection();
                connectionToServer.sendMessageToServer(new SelectedIsland(clientNickname, islandId));
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case COLOR_SELECTION:
                System.out.println("Select one color [yellow/blue/green/red/pink]");
                Color color = Color.valueOf(selectColor());
                connectionToServer.sendMessageToServer(new SelectedColor(clientNickname, color));
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case PRINCESS:
                students = selectStudentsOnCard(card, 1);
                connectionToServer.sendMessageToServer(new SelectedStudentsFromCard(clientNickname, students, name));
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case MONK:
                students = selectStudentsOnCard(card, 1);
                connectionToServer.sendMessageToServer(new SelectedStudentsFromCard(clientNickname, students, name));
                int islandPosition = islandSelection();
                connectionToServer.sendMessageToServer(new SelectedIsland(clientNickname, islandPosition));
                connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, name));
                break;
            case JESTER:
                handleJesterActivation(card);
                break;
            case MUSICIAN:
                handleMusicianActivation();
        }
    }

    /**
     * handleJesterActivation handle Jester Character activation
     * @param card get the Jester Card with its information
     */
    private void handleJesterActivation(CharacterCardView card) {
        System.out.println("Students on card : " + card.getStudents());
        System.out.println("Select the number of students you want to swap");
        int swaps = getInt();
        while (swaps <= 0 || swaps > 3) {
            System.out.println("The number of swaps must be at least 3");
            swaps = getInt();
        }
        List<Color> selectedStudents = selectStudentsOnCard(card, swaps);
        List<Color> studentsFromEntrance = selectStudentsFromEntrance(swaps);
        connectionToServer.sendMessageToServer(new SelectedStudentsFromCard(clientNickname, selectedStudents, CharacterName.JESTER));
        connectionToServer.sendMessageToServer(new SelectedStudentsFromEntrance(clientNickname, studentsFromEntrance));
        connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, CharacterName.JESTER));
    }

    /**
     * handleMusicianActivation handle Musician Character activation
     */
    private void handleMusicianActivation() {
        System.out.println("Select the number of students you want to swap");
        int swaps = getInt();
        while (swaps <= 0 || swaps > 2) {
            System.out.println("The number of swaps must be at least 2");
            swaps = getInt();
        }
        List<Color> studentsFromDiningRoom = selectStudentsFromDiningRoom(swaps);
        if (studentsFromDiningRoom == null) {
            System.out.println("Not enough students in the dining room");
            actionMovesHandler.handleError(ActionMove.ACTIVATE_CARD);
            actionPhase(clientNickname);
            return;
        }
        List<Color> studentsFromEntrance = selectStudentsFromEntrance(swaps);
        connectionToServer.sendMessageToServer(new SelectedStudentsFromEntrance(clientNickname, studentsFromEntrance));
        connectionToServer.sendMessageToServer(new SelectedStudentsFromCard(clientNickname, studentsFromDiningRoom, CharacterName.MUSICIAN));
        connectionToServer.sendMessageToServer(new ActiveEffect(clientNickname, CharacterName.MUSICIAN));
    }

    /**
     * selectStudentsFromDiningRoom returns swaps number of students
     * @param swaps number of students to select
     * @return selected students
     */
    private List<Color> selectStudentsFromDiningRoom(int swaps) {
        List<Integer> diningRoom = new ArrayList<>(clientModel.getDiningRooms().get(clientNickname));
        List<Color> selectedStudents = new ArrayList<>();

        int numberOfStudents = 0;
        for (Color color : Color.values()) {
            numberOfStudents += diningRoom.get(color.ordinal());
        }
        if (numberOfStudents < swaps) {
            return null;
        }

        for (int i = 0; i < swaps; i++) {
            System.out.println("DiningRoom----YELLOW : " + diningRoom.get(0) + "----BLUE : " + diningRoom.get(1) + "----GREEN : " + diningRoom.get(2) + "----RED : " + diningRoom.get(3) + "----PINK : " + diningRoom.get(4));
            System.out.println("Select color from dining room [yellow/blue/green/red/pink]");
            Color color = Color.valueOf(selectColor());
            while (diningRoom.get(color.ordinal()) <= 0) {
                System.out.println("The student of the chosen color are not present in the diningRoom");
                System.out.println("DiningRoom----YELLOW : " + diningRoom.get(0) + "----BLUE : " + diningRoom.get(1) + "----GREEN : " + diningRoom.get(2) + "----RED : " + diningRoom.get(3) + "----PINK : " + diningRoom.get(4));
                System.out.println("Select color from dining room [yellow/blue/green/red/pink]");
                color = Color.valueOf(selectColor());
            }
            selectedStudents.add(color);
            diningRoom.set(color.ordinal(), diningRoom.get(color.ordinal()) - 1);
        }
        return selectedStudents;
    }

    /**
     * selectStudentsFromEntrance returns studentsToSelect number of students
     * @param studentsToSelect number of students to select
     * @return selected students
     */
    private List<Color> selectStudentsFromEntrance(int studentsToSelect) {
        List<Color> selectedStudents = new ArrayList<>();
        List<Color> studentsInEntrance = new ArrayList<>(clientModel.getEntrances().get(clientNickname));

        for (int i = 0; i < studentsToSelect; i++) {
            System.out.println("Entrance " + studentsInEntrance);
            System.out.println("Select student from entrance [yellow/blue/green/red/pink]");
            Color student = Color.valueOf(selectColor());
            while (!studentsInEntrance.contains(student)) {
                System.out.println("The selected student are not in the entrance");
                System.out.println("Entrance " + studentsInEntrance);
                System.out.println("Select student from entrance [yellow/blue/green/red/pink]");
                student = Color.valueOf(selectColor());
            }
            selectedStudents.add(student);
            studentsInEntrance.remove(student);
        }
        return selectedStudents;
    }

    private List<Color> selectStudentsOnCard(CharacterCardView card, int studentsToSelect) {
        List<Color> selectedStudents = new ArrayList<>();
        List<Color> studentsOnCard = new ArrayList<>(card.getStudents());

        for (int i = 0; i < studentsToSelect; i++) {
            System.out.println(studentsOnCard);
            System.out.println("Select one students from the card");
            Color student = Color.valueOf(selectColor());
            while (!studentsOnCard.contains(student)) {
                System.out.println("The selected student are not on the card");
                System.out.println(studentsOnCard);
                System.out.println("Select one students from the card");
                student = Color.valueOf(selectColor());
            }
            selectedStudents.add(student);
            studentsOnCard.remove(student);
        }
        return selectedStudents;
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
        System.out.println("\n");
    }

    private void printDiningRooms() {
        Map<String, List<Integer>> diningRooms = clientModel.getDiningRooms();

        System.out.println("----DiningRooms----");
        for (String player : diningRooms.keySet()) {
            if (player.equals(clientNickname))
                System.out.println("You----DiningRoom----");
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
                System.out.println("You----Entrance----");
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
            if (islands.get(index).getOwner() == null)
                System.out.println("owner : Nobody");
            else if (islands.get(index).getOwner().equals(clientNickname))
                System.out.println("owner : You");
            else
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
                System.out.println("You----Professor----");
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
        System.out.println("[pe] to print the entrances");
        System.out.println("[pd] to print the dining rooms");
        System.out.println("[pi] to print the islands");
        System.out.println("[pt] to print the towers");
        System.out.println("[pp] to print the professor");
        System.out.println("[pcl] to print the clouds");
        System.out.println("[ms] to move students from entrance");
        System.out.println("[mmn] to move Mother Nature ");
        if (gameMode.equals("experts")) {
            System.out.println("[ac] to activate a character card");
            System.out.println("[pc] to print the available character cards");
            System.out.println("[gc] to print the number of coins");
        }
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
            System.out.println("Description :");
            System.out.println(cards.get(name).getDescription());

            System.out.println("----------------");
        }
    }

    public void endGame(boolean isADraw, String winner) {
        if (isADraw) {
            printDraw();
        } else {
            if (winner.equals(clientNickname)) {
                printWin();
            } else {
                printLose(winner);
            }
        }
        endGameChoice();//play again?
    }

    public void printDraw() {
        Screen.clear();
        System.out.println("GAME ENDED IN A DRAW");
    }

    public void printWin() {
        Screen.clear();
        System.out.println("GAME ENDED");
        System.out.println(Color.ANSI_YELLOW + "CONGRATULATION! YOU WON THE GAME" + Color.ANSI_RESET);
    }

    public void printLose(String winner) {
        Screen.clear();
        System.out.println("GAME ENDED");
        System.out.println(Color.ANSI_RED + "YOU LOSE" + Color.ANSI_RESET);
        System.out.println("WINNER: " + winner);

    }

    public void endGameChoice() {

        printGameSummary();

        System.out.println("Select one of the available actions : [/" + Color.ANSI_RED + "exit" + Color.ANSI_RESET + "]");
        String action = getString("/exit");

        switch (action) {
            case "exit":
                Screen.clear();
                connectionToServer.sendMessageToServer(new EndActionPhase(clientNickname));
                lobbyRefresh();
                break;
            default:
                System.out.println("Invalid Command");
                printGameSummary();
        }

    }

    public void printGameSummary() {
        /*
        System.out.println("[pe] to print the entrances");
        System.out.println("[pd] to print the dining rooms");
        System.out.println("[pi] to print the islands");
        System.out.println("[pt] to print the towers");
        System.out.println("[pp] to print the professor");
        System.out.println("[pcl] to print the clouds");
        if (gameMode.equals("experts")) {
            System.out.println("[pc] to print the available character cards");
        }*/
        System.out.println(Color.ANSI_YELLOW + "---- GAME SUMMARY ----" + Color.ANSI_RESET);
        printTowers();
        printProfessors();
        printDiningRooms();
        System.out.println("\n[" + Color.ANSI_RED + "exit" + Color.ANSI_RESET + "] exit and start a new game");
    }

}
