package it.polimi.ingsw.network;

import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.network.messages.clientMessage.*;
import it.polimi.ingsw.network.messages.serverMessage.*;

import java.util.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class Server represents the main class of the Server
 */
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

    private final int port;

    private final ServerConnectionHandler serverConnectionHandler;

    private final Queue<String> activePlayers;

    private final Map<String, ClientConnectionHandler> clientConnectionHandlerMap;

    private final Queue<Integer> attendingLobbies;

    private final Map<Integer, String> lobbyGameModeMap;

    private final Map<Integer, Integer> lobbyNumberOfPlayersMap;

    private final Map<Integer, GameHandler> activeLobbies;

    private final Map<String, Integer> playerLobbyMap;

    private Integer lobbyId = 0;

    /**
     * Constructor
     *
     * @param port the Server's port
     */
    public Server(int port) {
        this.serverConnectionHandler = new ServerConnectionHandler(port, this);
        this.port = port;
        activePlayers = new ConcurrentLinkedQueue<>();
        clientConnectionHandlerMap = new ConcurrentHashMap<>();
        attendingLobbies = new ConcurrentLinkedQueue<>();
        activeLobbies = new ConcurrentHashMap<>();
        lobbyGameModeMap = new ConcurrentHashMap<>();
        lobbyNumberOfPlayersMap = new ConcurrentHashMap<>();
        playerLobbyMap = new ConcurrentHashMap<>();
    }

    /**
     * Starts a thread to accept connections from clients
     */
    public void startConnections() {
        (new Thread(serverConnectionHandler)).start();
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

    /**
     * Method getConnectionByPlayer returns the clientConnectionHandler of the selected player
     *
     * @param player the nickname of the selected player
     * @return the clientConnectionHandler of the selected player
     */
    public ClientConnectionHandler getConnectionByPlayer(String player) {
        return clientConnectionHandlerMap.get(player);
    }

    /**
     * Method getGameModeByLobbyID returns the game mode of the selected lobbyId
     *
     * @param lobbyId the selected lobbyId
     * @return the game mode of the selected lobbyId
     */
    public String getGameModeByLobbyID(int lobbyId) {
        return lobbyGameModeMap.get(lobbyId);
    }

    /**
     * Method getNumberOfPlayersByLobbyID returns the number of player of the selected lobbyId
     *
     * @param lobbyId the selected lobby
     * @return the number of player of the selected lobby
     */
    public int getNumberOfPlayersByLobbyID(int lobbyId) {
        return lobbyNumberOfPlayersMap.get(lobbyId);
    }

    /**
     * Method getGameHandlerByLobbyId returns the GameHandler of the selected lobbyId
     *
     * @param lobbyId the selected lobby
     * @return the GameHandler of the selected lobby
     */
    public GameHandler getGameHandlerByLobbyId(int lobbyId) {
        return activeLobbies.get(lobbyId);
    }

    /**
     * Method getAttendingLobbiesGameModeMap returns a map of the attending lobbies and the relative game mode
     *
     * @return a map of the attending lobby and the relative game mode
     */
    public Map<Integer, String> getAttendingLobbiesGameModeMap() {
        Map<Integer, String> attendingLobbiesGameModeMap = new HashMap<>();
        for (Integer lobbyId : attendingLobbies) {
            if (lobbyGameModeMap.containsKey(lobbyId)) {
                attendingLobbiesGameModeMap.put(lobbyId, lobbyGameModeMap.get(lobbyId));
            }
        }
        return attendingLobbiesGameModeMap;
    }

    /**
     * Method getAttendingLobbiesNumberOfPlayerMap returns a map of the attending lobbies and the relative number of players
     *
     * @return a map of the attending lobbies and the relative number of players
     */
    public Map<Integer, Integer> getAttendingLobbiesNumberOfPlayerMap() {
        Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap = new HashMap<>();
        for (Integer lobbyId : attendingLobbies) {
            if (lobbyNumberOfPlayersMap.containsKey(lobbyId)) {
                attendingLobbiesNumberOfPlayerMap.put(lobbyId, lobbyNumberOfPlayersMap.get(lobbyId));
            }
        }
        return attendingLobbiesNumberOfPlayerMap;
    }

    /**
     * Method createLobby creates a new lobby and update the Map
     *
     * @param nickname        the nickname of the player that creates the new lobby
     * @param numberOfPlayers the number of players of the lobby that I want to create
     * @param gameMode        the gameMode of the lobby that I want to create
     */
    public void createLobby(String nickname, int numberOfPlayers, String gameMode) {
        playerLobbyMap.put(nickname, lobbyId);

        attendingLobbies.add(lobbyId);
        lobbyNumberOfPlayersMap.put(lobbyId, numberOfPlayers);
        lobbyGameModeMap.put(lobbyId, gameMode);

        int playerRemainingToStartTheGame = getNumberOfPlayersByLobbyID(lobbyId) - 1;
        clientConnectionHandlerMap.get(nickname).sendMessageToClient(new JoiningLobby("You ", playerRemainingToStartTheGame));
        lobbyId++;
    }

    /**
     * Method registerPlayer registers a new player on the Server
     *
     * @param nickname                the nickname of the new player
     * @param clientConnectionHandler the new player's clientConnectionHandler
     */
    private void registerPlayer(String nickname, ClientConnectionHandler clientConnectionHandler) {
        activePlayers.add(nickname);
        clientConnectionHandlerMap.put(nickname, clientConnectionHandler);
        clientConnectionHandler.setNickname(nickname);
        clientConnectionHandler.sendMessageToClient(new SendLobbies(getAttendingLobbiesNumberOfPlayerMap(), getAttendingLobbiesGameModeMap()));
    }

    /**
     * Method joinLobby makes the player joining on the selected lobbyId
     *
     * @param nickname player's nickname
     * @param lobbyId  the selected lobby
     */
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

    /**
     * Method isLobbyFull checks if the selected lobby is full
     *
     * @param lobbyId the selected lobby
     * @return {@code true} if the lobby is full
     * {@code false} if the lobby isn't full
     */
    private boolean isLobbyFull(int lobbyId) {
        return getPlayersInSameLobby(lobbyId).size() == getNumberOfPlayersByLobbyID(lobbyId);
    }

    /**
     * Method closeLobby closes the lobby of the selected player
     *
     * @param playerDisconnected the disconnected player
     */
    public void closeLobby(String playerDisconnected) {
        if (playerLobbyMap.containsKey(playerDisconnected)) {
            int lobby = playerLobbyMap.get(playerDisconnected);
            playerLobbyMap.remove(playerDisconnected);
            activePlayers.remove(playerDisconnected);

            List<String> players = getPlayersInSameLobby(lobby);

            activePlayers.removeAll(players);
            if (attendingLobbies.contains(lobby)) {
                attendingLobbies.remove(lobby);
            } else if (activeLobbies.containsKey(lobbyId))
                activeLobbies.remove(lobby);

            for (String player : players) {
                playerLobbyMap.remove(player, lobby);
                clientConnectionHandlerMap.get(player).sendMessageToClient(new Disconnection(playerDisconnected));
                activePlayers.remove(player);
                clientConnectionHandlerMap.remove(player);
            }
        } else {
            activePlayers.remove(playerDisconnected);
        }
    }

    /**
     * Method startGame initializes a new GameHandler
     *
     * @param players the players in the same lobby
     * @param lobby   the lobbyId
     */
    private void startGame(List<String> players, Integer lobby) {
        LinkedList<TowerColor> towerColors = new LinkedList<>(Arrays.asList(TowerColor.values()));
        Map<String, TowerColor> towerColorMap = new HashMap<>();

        for (String player : players) {
            towerColorMap.put(player, towerColors.getFirst());
            towerColors.removeFirst();
        }

        for (String player : players) {
            clientConnectionHandlerMap.get(player).sendMessageToClient(new GameStarting(towerColorMap, players, getGameModeByLobbyID(lobby)));
        }

        attendingLobbies.remove(lobby);
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
            GameHandler gameHandler = getGameHandlerByLobbyId(lobby);
            gameHandler.handleGameMessage(message);
        }
    }


}
