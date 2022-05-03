package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.InputChecker;
import it.polimi.ingsw.model.AssistantName;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.*;
import it.polimi.ingsw.network.messages.serverMessage.*;

import java.util.List;

public class GameHandler {
    private int i = 0;

    private Server server;

    private Game model;

    private int lobbyId;

    private Controller controller;

    private InputChecker inputChecker;

    public GameHandler(Server server, int lobbyId) {
        this.server = server;
        this.lobbyId = lobbyId;
    }

    public void startGameHandler() {
        model = new Game(server.getNumberOfPlayersByLobbyID(lobbyId));
        controller = new Controller(model);
        inputChecker = new InputChecker(model);
        addPlayersToModel(server.getPlayersInSameLobby(lobbyId));
        if (server.getGameModeByLobbyID(lobbyId).equals("experts")) {
            model.initCoins();
            model.initCharacterCards();
        }
        attachObserver();
        model.start();
        String firstPlayer = model.getCurrPlayer().getNickname();
        sendMessageToLobby(new StartPianificationPhase(firstPlayer));
    }

    private void attachObserver() {
        VirtualView virtualView = new VirtualView(this);
        List<CharacterCard> cards = model.getCharacterCards();

        controller.registerObserver(virtualView);
        model.getArchipelago().registerObserver(virtualView);
        model.registerObserver(virtualView);
        model.getProfessorManager().registerObserver(virtualView);
        for (CharacterCard card : cards) {
            card.registerObserver(virtualView);
        }
    }

    private void addPlayersToModel(List<String> players) {
        for (String player : players) {
            model.addPlayer(player);
        }
    }

    public void sendMessageToOneClient(String nickname, ServerMessage message) {
        ClientConnectionHandler clientConnectionHandler = server.getConnectionByPlayer(nickname);
        clientConnectionHandler.sendMessageToClient(message);
    }

    public void sendMessageToLobby(ServerMessage message) {
        List<String> players = server.getPlayersInSameLobby(lobbyId);
        for (String player : players) {
            server.getConnectionByPlayer(player).sendMessageToClient(message);
        }
    }

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

    public void handleActionPhaseMessage(ClientMessage message, String player) {
        if (message instanceof MoveStudentOnDiningRoom) {
            Color student = ((MoveStudentOnDiningRoom) message).getStudent();

            if (!inputChecker.checkStudentInEntrance(student)) {
                sendMessageToOneClient(player, new GameError(ErrorType.INVALID_INPUT, "The student chosen are not in the entrance"));
                return;
            }

            controller.moveStudentToDiningRoom(student);
            sendMessageToOneClient(player, new NextMove());

        } else if (message instanceof MoveStudentToIsland) {
            Color student = ((MoveStudentToIsland) message).getStudent();
            int islandPosition = ((MoveStudentToIsland) message).getIslandPosition();

            if (!inputChecker.checkMoveStudentToIsland(student, islandPosition)) {
                sendMessageToOneClient(player, new GameError(ErrorType.INVALID_INPUT, "Impossible move the student on the selected island"));
                return;
            }

            controller.moveStudentToIsland(student, islandPosition);
            sendMessageToOneClient(player, new NextMove());

        } else if (message instanceof ChosenMotherNatureMove) {
            int motherNatureMove = ((ChosenMotherNatureMove) message).getMotherNatureMove();

            if (!inputChecker.checkMotherNatureMove(motherNatureMove)) {
                sendMessageToOneClient(player, new GameError(ErrorType.MOTHER_NATURE_MOVE_INVALID, "Mother Nature selected move is not valid"));
                return;
            }

            controller.moveMotherNature(motherNatureMove);
            sendMessageToOneClient(player, new NextMove());

        } else if (message instanceof SelectedCloud) {
            int cloudId = ((SelectedCloud) message).getCloudId();

            if (!inputChecker.checkSelectedCloud(cloudId)) {
                sendMessageToOneClient(player, new GameError(ErrorType.SELECTED_CLOUD_ERROR, "CloudId selected is not valid"));
            }

            controller.selectCloud(cloudId);
            sendMessageToOneClient(player, new NextMove());

        } else if (message instanceof EndActionPhase) {
            nextPlayerHandler();

        } else if (message instanceof CharacterCardMessage) {
            handleCharacterChardActivation(message, player);

        }
    }

    public void handleCharacterChardActivation(ClientMessage message, String player) {
        if (message instanceof SelectedColor) {
            Color color = ((SelectedColor) message).getColor();

            controller.setColorSelection(color);

        } else if (message instanceof SelectedIsland) {
            int islandPosition = ((SelectedIsland) message).getIslandPosition();

            if (!inputChecker.checkIslandPosition(islandPosition)) {
                sendMessageToOneClient(player, new GameError(ErrorType.CARD_REQUIREMENTS_ERROR, "Island position selected is not valid"));
                return;
            }

            controller.setIslandSelection(islandPosition);

        } else if (message instanceof SelectedStudentsFromEntrance) {
            List<Color> students = ((SelectedStudentsFromEntrance) message).getStudents();

            if (!inputChecker.checkStudentInEntrance(students)) {
                sendMessageToOneClient(player, new GameError(ErrorType.CARD_REQUIREMENTS_ERROR, "Students chosen are not in the entrance"));
            }

            controller.setStudentsSelectedEntrance(students);

        } else if (message instanceof SelectedStudentsFromCard) {
            CharacterName name = ((SelectedStudentsFromCard) message).getName();
            List<Color> students = ((SelectedStudentsFromCard) message).getStudents();

            if(!inputChecker.checkStudentsOnCard(name, students)) {
                sendMessageToOneClient(player, new GameError(ErrorType.CARD_REQUIREMENTS_ERROR, "Students chosen from the selected card are not valid"));
            }

            controller.setStudentsSelectedFromCard(students);
        }
    }


}
