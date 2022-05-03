package it.polimi.ingsw.network;

import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.clientMessage.*;
import it.polimi.ingsw.network.messages.serverMessage.*;

import java.util.*;
import java.util.Map;

public class Server {

    public static void main(String[] args) {
        int port = 26000;

        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid format... Please insert a number");
                System.exit(-1);
            }
            if (port < 1023) {
                System.err.println("GameError... Insert a number greater than 1023");
                System.exit(-1);
            }
        }
        System.out.println("Server running on port : " + port);
        Server server = new Server(port);
        (new Thread(server.serverConnectionHandler)).start();

    }

    private int port;

    private ServerConnectionHandler serverConnectionHandler;

    private List<String> activePlayers;

    private Map<String, ClientConnectionHandler> clientConnectionHandlerMap;

    private List<Integer> attendingLobbies;

    private Map<Integer, List<TowerColor>> lobbyTowerColorsAvailableMap;

    private Map<String, TowerColor> playerTowerColorMap;

    private Map<Integer, String> lobbyGameModeMap;

    private Map<Integer, Integer> lobbyNumberOfPlayersMap;

    private Map<Integer, GameHandler> activeLobbies;

    private Map<String, Integer> playerLobbyMap;

    private int lobbyId = 0;

    public Server(int port) {
        this.serverConnectionHandler = new ServerConnectionHandler(port, this);
        this.port = port;
        activePlayers = new ArrayList<>();
        clientConnectionHandlerMap = new HashMap<>();
        attendingLobbies = new ArrayList<>();
        activeLobbies = new HashMap<>();
        lobbyGameModeMap = new HashMap<>();
        playerTowerColorMap = new HashMap<>();
        lobbyTowerColorsAvailableMap = new HashMap<>();
        lobbyNumberOfPlayersMap = new HashMap<>();
        playerLobbyMap = new HashMap<>();
    }

    /**
     * Method getPlayersInSameLobby takes the player in the selected lobby
     *
     * @param lobbyId the id for the selected lobby
     * @return list of the player in the selected lobby
     */
    public List<String> getPlayersInSameLobby(int lobbyId) {
        List<String> playerInSameLobby = new ArrayList<>();

        for (String player : playerLobbyMap.keySet()) {
            if (playerLobbyMap.get(player) == lobbyId) {
                playerInSameLobby.add(player);
            }
        }
        return playerInSameLobby;
    }

    public ClientConnectionHandler getConnectionByPlayer(String player) {
        return clientConnectionHandlerMap.get(player);
    }

    public String getGameModeByLobbyID(int lobbyId) {
        return lobbyGameModeMap.get(lobbyId);
    }

    public int getNumberOfPlayersByLobbyID(int lobbyId) {
        return lobbyNumberOfPlayersMap.get(lobbyId);
    }

    public GameHandler getGameHandlerByLobbyId(int lobbyId) {
        return activeLobbies.get(lobbyId);
    }


    public List<TowerColor> getTowerColorsAvailableByLobbyId(int lobbyId) {
        return lobbyTowerColorsAvailableMap.get(lobbyId);
    }

    public TowerColor getTowerColorByPlayer(String nickname) {
        return playerTowerColorMap.get(nickname);
    }

    public Map<Integer, String> getAttendingLobbiesGameModeMap() {
        Map<Integer, String> attendingLobbiesGameModeMap = new HashMap<>();
        for (Integer lobbyId : attendingLobbies) {
            if (lobbyGameModeMap.containsKey(lobbyId)) {
                attendingLobbiesGameModeMap.put(lobbyId, lobbyGameModeMap.get(lobbyId));
            }
        }
        return attendingLobbiesGameModeMap;
    }

    public Map<Integer, Integer> getAttendingLobbiesNumberOfPlayerMap() {
        Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap = new HashMap<>();
        for (Integer lobbyId : attendingLobbies) {
            if (lobbyNumberOfPlayersMap.containsKey(lobbyId)) {
                attendingLobbiesNumberOfPlayerMap.put(lobbyId, lobbyNumberOfPlayersMap.get(lobbyId));
            }
        }
        return attendingLobbiesNumberOfPlayerMap;
    }

    public void createLobby(String nickname, int numberOfPlayers, String gameMode) {
        List<TowerColor> towerColorsAvailable = new ArrayList<>(Arrays.asList(TowerColor.values()));

        List<String> player = new ArrayList<>();
        player.add(nickname);

        playerLobbyMap.put(nickname, lobbyId);

        attendingLobbies.add(lobbyId);
        lobbyTowerColorsAvailableMap.put(lobbyId, towerColorsAvailable);
        lobbyNumberOfPlayersMap.put(lobbyId, numberOfPlayers);
        lobbyGameModeMap.put(lobbyId, gameMode);

        int playerRemainingToStartTheGame = getNumberOfPlayersByLobbyID(lobbyId) - 1;
        clientConnectionHandlerMap.get(nickname).sendMessageToClient(new JoiningLobby("You ", playerRemainingToStartTheGame));
        lobbyId++;
    }

    private void registerPlayer(String nickname, ClientConnectionHandler clientConnectionHandler) {
        activePlayers.add(nickname);
        clientConnectionHandlerMap.put(nickname, clientConnectionHandler);
        clientConnectionHandler.setNickname(nickname);
        clientConnectionHandler.sendMessageToClient(new SendLobbies(getAttendingLobbiesNumberOfPlayerMap(), getAttendingLobbiesGameModeMap()));
    }

    private void joinLobby(String nickname, Integer lobbyId) {
        if (isLobbyFull(lobbyId)) {
            clientConnectionHandlerMap.get(nickname).sendMessageToClient(new GameError(ErrorType.LOBBY_ERROR, "The selected Lobby is already full"));
            return;
        }
        if (!attendingLobbies.contains(lobbyId)) {
            clientConnectionHandlerMap.get(nickname).sendMessageToClient(new GameError(ErrorType.LOBBY_ERROR, "The selected Lobby doesn't exist anymore"));
            return;
        }
        playerLobbyMap.put(nickname, lobbyId);
        List<String> players = getPlayersInSameLobby(lobbyId);
        int playersRemainingToStartGame = getNumberOfPlayersByLobbyID(lobbyId) - players.size();
        for (String player : players) {
            clientConnectionHandlerMap.get(player).sendMessageToClient(new JoiningLobby(nickname, playersRemainingToStartGame));
        }

        if (isLobbyFull(lobbyId)) {
            startGame(players, lobbyId);
        }
    }

    private boolean isLobbyFull(int lobbyId) {
        return getPlayersInSameLobby(lobbyId).size() == getNumberOfPlayersByLobbyID(lobbyId);
    }

    public void closeLobby(String playerDisconnected) {
        if (playerLobbyMap.containsKey(playerDisconnected)) {
            int lobby = playerLobbyMap.get(playerDisconnected);
            List<String> players = getPlayersInSameLobby(lobby);

            activePlayers.removeAll(players);
            if(attendingLobbies.contains(lobby))
                attendingLobbies.remove(lobby);
            activeLobbies.remove(lobby);

            for (String player : players) {
                playerLobbyMap.remove(player, lobby);
                clientConnectionHandlerMap.get(player).sendMessageToClient(new Disconnection());
                activePlayers.remove(player);
                clientConnectionHandlerMap.remove(player);
            }
        } else {
            activePlayers.remove(playerDisconnected);
        }
    }

    private void startGame(List<String> players, Integer lobby) {
        for (String player : players) {
            clientConnectionHandlerMap.get(player).sendMessageToClient(new GameStarting(players, getGameModeByLobbyID(lobby)));
        }
        if (lobby == 1) {
            System.out.println("Uguale a 1");
        }
        System.out.println(attendingLobbies);
        System.out.println(attendingLobbies.contains(lobby));
        try {
            System.out.println(attendingLobbies.remove(lobby));
        } catch (UnsupportedOperationException e) {
            System.out.println("Operazione non supportata");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("OutofBound");
            e.printStackTrace();
        }
        GameHandler gameHandler = new GameHandler(this, lobby);
        activeLobbies.put(lobby, gameHandler);
        gameHandler.startGameHandler();
    }

    /**
     * Method messageDispatcher handles the nickname setup and the lobby phase pf the game and dispatches the different client message to the right GameHandler
     *
     * @param message                 the message sends by the client
     * @param clientConnectionHandler the client connection to receive the client's message and to send the server's message
     */
    public void messageDispatcher(ClientMessage message, ClientConnectionHandler clientConnectionHandler) {
        if (message instanceof NicknameRequest) {
            String nickname = ((NicknameRequest) message).getNickname();
            if (activePlayers.contains(nickname)) {
                clientConnectionHandler.sendMessageToClient(new GameError(ErrorType.NICKNAME_ALREADY_TAKEN, "Nickname already taken"));
            } else {
                registerPlayer(nickname, clientConnectionHandler);
            }
        } else if (message instanceof CreateLobby) {
            int numberOfPlayer = ((CreateLobby) message).getNumberOfPlayers();
            String gameMode = ((CreateLobby) message).getGameMode();
            createLobby(clientConnectionHandler.getNickname(), numberOfPlayer, gameMode);
        } else if (message instanceof JoinLobby) {
            int lobbyId = ((JoinLobby) message).getLobbyId();
            joinLobby(clientConnectionHandler.getNickname(), lobbyId);
        } else if (message instanceof GetLobbies) {
            clientConnectionHandler.sendMessageToClient(new SendLobbies(getAttendingLobbiesNumberOfPlayerMap(), getAttendingLobbiesGameModeMap()));
        } else if (message instanceof GameMessage) {
            String nickname = clientConnectionHandler.getNickname();
            int lobby = playerLobbyMap.get(nickname);
            GameHandler gameHandler = activeLobbies.get(lobby);
            gameHandler.handleGameMessage(message);
        }


    }


}
