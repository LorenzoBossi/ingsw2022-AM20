package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.InputChecker;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.*;
import it.polimi.ingsw.network.messages.serverMessage.*;

import java.util.List;

/**
 * This class init the Game class and handles the GameMessage from the client
 */
public class GameHandler {

    private VirtualView virtualView;

    private final Server server;

    private Game model;

    private final int lobbyId;

    private Controller controller;

    private InputChecker inputChecker;

    /**
     * Constructor
     * @param server the Server
     * @param lobbyId the lobby associated with the GameHandler
     */
    public GameHandler(Server server, int lobbyId) {
        this.server = server;
        this.lobbyId = lobbyId;
    }

    /**
     * initializes game attributes and the Controller class
     */
    public void startGameHandler() {
        model = new Game(server.getNumberOfPlayersByLobbyID(lobbyId));
        controller = new Controller(model);
        inputChecker = new InputChecker(model);
        addPlayersToModel(server.getPlayersInSameLobby(lobbyId));
        attachObserver();
        model.start();
        if (server.getGameModeByLobbyID(lobbyId).equals("experts")) {
            model.initCoins();
            model.initCharacterCards();
            attachCardObserver();
            model.notifyExtractedCard();
        }
        String firstPlayer = model.getCurrPlayer().getNickname();
        sendMessageToLobby(new StartPianificationPhase(firstPlayer));
    }

    /**
     * Attach the observable object in the model to the VirtualView
     */
    private void attachObserver() {
        List<Player> players = model.getPlayers();
        virtualView = new VirtualView(this);

        model.getArchipelago().registerObserver(virtualView);
        model.registerObserver(virtualView);
        model.getProfessorManager().registerObserver(virtualView);
        for (Player player : players) {
            player.registerObserver(virtualView);
        }
    }

    /**
     * Attach the observable cards to the VirtualView
     */
    private void attachCardObserver() {
        List<CharacterCard> cards = model.getCharacterCards();

        for (CharacterCard card : cards) {
            card.registerObserver(virtualView);
        }
    }

    /**
     * Add the lobby players to the model
     * @param players the player in the lobby
     */
    private void addPlayersToModel(List<String> players) {
        for (String player : players) {
            model.addPlayer(player);
        }
    }

    /**
     * Send message to one client
     * @param nickname the nickname of the client
     * @param message the message to send
     */
    public void sendMessageToOneClient(String nickname, ServerMessage message) {
        ClientConnectionHandler clientConnectionHandler = server.getConnectionByPlayer(nickname);
        clientConnectionHandler.sendMessageToClient(message);
    }

    /**
     * Send message to all the player in the lobby
     * @param message the message to send in the lobby
     */
    public void sendMessageToLobby(ServerMessage message) {
        List<String> players = server.getPlayersInSameLobby(lobbyId);
        for (String player : players) {
            server.getConnectionByPlayer(player).sendMessageToClient(message);
        }
    }

    /**
     * Calculate the next player and at what phase of the game is and notify the clients
     */
    public void nextPlayerHandler() {
        model.nextPlayer();
        String currPlayer = model.getCurrPlayer().getNickname();
        Phase phase = model.getPhase();

        if (phase == Phase.PIANIFICATION) {
            sendMessageToLobby(new StartPianificationPhase(currPlayer));
        } else if (phase == Phase.ACTION) {
            sendMessageToLobby(new StartActionPhase(currPlayer));
        }
    }

    /**
     * Handles the GameMessage from the client
     * @param message the message sent by the client
     */
    public void handleGameMessage(ClientMessage message) {
        String player = ((GameMessage) message).getNickname();

        if (!inputChecker.checkValidTurn(player)) {
            sendMessageToOneClient(player, new GameError(ErrorType.NOT_YOUR_TURN, "Not your turn"));
            return;
        }

        if (message instanceof ChosenAssistant) {
            AssistantName name = ((ChosenAssistant) message).getName();
            if (!inputChecker.checkAssistantPlayed(name)) {
                sendMessageToOneClient(player, new GameError(ErrorType.ASSISTANT_NOT_PLAYABLE, "Assistant chosen is not playable"));
                return;
            }
            controller.playAssistant(name);
            nextPlayerHandler();

        } else if (message instanceof ActionPhaseMessage) {
            handleActionPhaseMessage(message, player);
        }
    }

    /**
     * Handles the ActionPhaseMessage sent by the client
     * @param message the message sent by the client
     * @param player the nickname of the player that sent the message
     */
    public void handleActionPhaseMessage(ClientMessage message, String player) {
        if (message instanceof MoveStudentOnDiningRoom) {
            Color student = ((MoveStudentOnDiningRoom) message).getStudent();

            if (!inputChecker.checkStudentInEntrance(student)) {
                sendMessageToOneClient(player, new GameError(ErrorType.INVALID_INPUT, "The student chosen are not in the entrance"));
                return;
            }

            if(!inputChecker.checkStudentInTheDiningRoom(student)) {
                sendMessageToOneClient(player, new GameError(ErrorType.INVALID_INPUT, "You can no longer add students of that color to the dining room"));
                return;
            }

            controller.moveStudentToDiningRoom(student);
            sendMessageToLobby(new NextMove(model.getCurrPlayer().getNickname()));

        } else if (message instanceof MoveStudentToIsland) {
            Color student = ((MoveStudentToIsland) message).getStudent();
            int islandPosition = ((MoveStudentToIsland) message).getIslandPosition();

            if (!inputChecker.checkMoveStudentToIsland(student, islandPosition)) {
                sendMessageToOneClient(player, new GameError(ErrorType.INVALID_INPUT, "Impossible move the student on the selected island"));
                return;
            }

            controller.moveStudentToIsland(student, islandPosition);
            sendMessageToLobby(new NextMove(model.getCurrPlayer().getNickname()));

        } else if (message instanceof ChosenMotherNatureMove) {
            int motherNatureMove = ((ChosenMotherNatureMove) message).getMotherNatureMove();

            if (!inputChecker.checkMotherNatureMove(motherNatureMove)) {
                sendMessageToOneClient(player, new GameError(ErrorType.MOTHER_NATURE_MOVE_INVALID, "Mother Nature selected move is not valid"));
                return;
            }

            controller.moveMotherNature(motherNatureMove);

            if (!model.getPhase().equals(Phase.ENDED))
                sendMessageToLobby(new NextMove(model.getCurrPlayer().getNickname()));

        } else if (message instanceof SelectedCloud) {
            int cloudId = ((SelectedCloud) message).getCloudId();

            if (!inputChecker.checkSelectedCloud(cloudId)) {
                sendMessageToOneClient(player, new GameError(ErrorType.SELECTED_CLOUD_ERROR, "CloudId selected is not valid"));
                return;
            }

            controller.selectCloud(cloudId);
            sendMessageToLobby(new NextMove(model.getCurrPlayer().getNickname()));

        } else if (message instanceof EndActionPhase) {
            nextPlayerHandler();

        } else if (message instanceof CharacterCardMessage) {
            handleCharacterCardActivation(message, player);

        }
    }

    /**
     * Handles the CharacterCardActivation message sent by the client
     * @param message the message sent by the client
     * @param player the nickname of the player that sent the message
     */
    public void handleCharacterCardActivation(ClientMessage message, String player) {
        if (message instanceof SelectedColor) {
            Color color = ((SelectedColor) message).getColor();

            controller.setColorSelection(color);

        } else if (message instanceof SelectedIsland) {
            int islandPosition = ((SelectedIsland) message).getIslandPosition();

            if (!inputChecker.checkIslandPosition(islandPosition)) {
                return;
            }

            controller.setIslandSelection(islandPosition);

        } else if (message instanceof SelectedStudentsFromEntrance) {
            List<Color> students = ((SelectedStudentsFromEntrance) message).getStudents();

            controller.setStudentsSelectedEntrance(students);

        } else if (message instanceof SelectedStudentsFromCard) {
            List<Color> students = ((SelectedStudentsFromCard) message).getStudents();

            controller.setStudentsSelectedFromCard(students);

        } else if (message instanceof ActiveEffect) {
            CharacterName name = ((ActiveEffect) message).getName();

            if (!inputChecker.checkCharacterCardActivation(name)) {
                model.getCurrPlayer().setPlayerChoice(new PlayerChoice());
                sendMessageToOneClient(player, new GameError(ErrorType.CARD_REQUIREMENTS_ERROR, "Error during the activation of " + name + " character card"));
                return;
            }

            controller.useCharacterCard(name);
            if (!model.getPhase().equals(Phase.ENDED))
                sendMessageToLobby(new NextMove(model.getCurrPlayer().getNickname()));
        }
    }
    
}
