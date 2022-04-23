package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;
import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game extends ObservableSubject {
    private List<Cloud> clouds;
    private final IslandsManager archipelago;
    private ProfessorManager professorManager;
    private final List<Assistant> assistants;
    private final Bag bag;
    private InfluenceStrategy influenceStrategy;
    private final List<CharacterCard> characterCards;
    private Phase phase;
    private Player currPlayer;
    private List<Player> players;
    private int numberOfPlayers;
    private List<Player> actionOrder;
    private Player firstPlayer;
    private int numberOfCoins = 0;
    private final int TOTAL_NUMBER_OF_COINS = 20;


    /**
     * Constructor
     */
    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        bag = new Bag();
        archipelago = new IslandsManager();
        initIslands();
        bag.fillBag(24);
        clouds = new ArrayList<>();
        professorManager = new ProfessorManager();
        players = new ArrayList<>();
        assistants = Assistant.getEveryAssistant();
        currPlayer = null;
        influenceStrategy = new StandardInfluence();
        characterCards = new ArrayList<>();
        phase = Phase.LOBBY;
        actionOrder = new ArrayList<>();
        addClouds(numberOfPlayers);
        refillClouds();
    }

    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    public void initCoins() {
        this.numberOfCoins = TOTAL_NUMBER_OF_COINS;
        for (Player player : players)
            addCoinsToPlayer(player);
    }

    public void characterCardPayment(int payment) {
        currPlayer.useCoins(payment);
        numberOfCoins += payment;
    }

    public void addCoinsToPlayer(Player player) {
        numberOfCoins--;
        player.addCoin();
    }

    public boolean hasEnoughCoins(int numberOfCoins) {
        return this.numberOfCoins > numberOfCoins;
    }

    /**
     * Put the initial students on every island except the island of mother nature and the opposite one
     */
    private void initIslands() {
        int numberOfIslands, oppositeOfMotherNature;
        int i;
        Island island;
        Color student;

        numberOfIslands = archipelago.getNumberOfIslands();
        oppositeOfMotherNature = archipelago.getNumberOfIslands() / 2 - 1;
        bag.fillBag(2);

        for (i = 1; i < oppositeOfMotherNature; i++) {
            student = bag.getStudents(1).get(0);
            island = archipelago.getIsland(i);
            island.addStudent(student);
        }
        for (i = oppositeOfMotherNature + 1; i < numberOfIslands; i++) {
            student = bag.getStudents(1).get(0);
            island = archipelago.getIsland(i);
            island.addStudent(student);
        }
    }

    public Bag getBag() {
        return bag;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public IslandsManager getArchipelago() {
        return archipelago;
    }

    public ProfessorManager getProfessorManager() {
        return professorManager;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public void setInfluenceStrategy(InfluenceStrategy influenceStrategy) {
        this.influenceStrategy = influenceStrategy;
    }

    public void start() {
        phase = Phase.PIANIFICATION;
        firstPlayer = players.get(0);
        currPlayer = firstPlayer;
    }

    /**
     * update the current player and the phase of the game.
     * During the action phase this methods resets the number of available moves of the current player
     * according to the number of players of the game
     */
    public void nextPlayer() {
        Player next;
        if (phase == Phase.PIANIFICATION) {
            updateActionOrder();
            next = getNextPianification();
            if (next.equals(firstPlayer))
                startActionPhase();
            else
                currPlayer = next;
        } else if (phase == Phase.ACTION) {
            next = getNextAction();
            if (next.equals(firstPlayer)) {
                startPianificationPhase();
            } else {
                currPlayer = next;
                resetAvailableMoves(currPlayer);
            }
        }
    }

    /**
     * Gets the next player in the Players Order (clockwise).
     *
     * @return The corrisponding player
     */
    private Player getNextPianification() {
        int indexOfCurrentPlayer = players.indexOf(currPlayer);

        if (indexOfCurrentPlayer == numberOfPlayers - 1)
            return players.get(0);
        else
            return players.get(indexOfCurrentPlayer + 1);
    }

    /**
     * insert the current player in the right place of the action order according to his priority
     */
    private void updateActionOrder() {
        int position = 0;
        for (Player player : actionOrder) {
            if (player.getPlayerPriority() <= currPlayer.getPlayerPriority()) {
                position++;
            } else
                break;
        }

        actionOrder.add(position, currPlayer);

    }

    /**
     * Gets the next player in the Action Order.
     *
     * @return The corrisponding player
     */
    private Player getNextAction() {
        int indexOfCurrentPlayer = actionOrder.indexOf(currPlayer);

        if (indexOfCurrentPlayer == numberOfPlayers - 1)
            return actionOrder.get(0);
        else
            return actionOrder.get(indexOfCurrentPlayer + 1);
    }

    private void startActionPhase() {
        firstPlayer = actionOrder.get(0);
        currPlayer = firstPlayer;
        phase = Phase.ACTION;
        resetAvailableMoves(currPlayer);
    }

    private void startPianificationPhase() {
        refillClouds();
        actionOrder.clear();
        currPlayer = firstPlayer;
        phase = Phase.PIANIFICATION;
    }

    /**
     * refill every cloud with a number of students depending on the number of players in the game
     */
    private void refillClouds() {
        if (numberOfPlayers == 2) {
            for (Cloud cloud : clouds) {
                cloud.fill(bag.getStudents(3));
            }
        } else if (numberOfPlayers == 3) {
            for (Cloud cloud : clouds) {
                cloud.fill(bag.getStudents(4));
            }
        }
    }

    /**
     * resets the number of available moves of the current player
     * according to the number of players of the game
     *
     * @param player the player to reset the number of moves
     */
    private void resetAvailableMoves(Player player) {
        if (numberOfPlayers == 2) {
            player.setAvailableMoves(3);
        } else if (numberOfPlayers == 3) {
            player.setAvailableMoves(4);
        }
    }

    /**
     * Method isSameNickname checks if already exists a player with the chosen nickname
     *
     * @param nickname the nickname that I wanted to check
     * @return {@code true} if already exists a player with the chosen nickname,
     * {@code false} if not exists a player with the chosen nickname
     */
    public boolean isSameNickname(String nickname) {
        Player player = getPlayerByNickname(nickname);
        return player != null;
    }


    /**
     * Method addPlayer adds a player if the nickname hasn't been chosen
     *
     * @param nickname the players nickname that I wanted to add
     * @return {@code true} if the nickname hasn't been chosen
     * {@code false} if the nickname has been chosen
     */
    public boolean addPlayer(String nickname) {
        if (isSameNickname(nickname))
            return false;
        players.add(new Player(nickname, assistants));
        if (currPlayer == null)
            currPlayer = players.get(0);
        return true;
    }


    /**
     * Method removePlayer removes the player with the selected nickname
     *
     * @param nickname the player that I wanted to remove
     */
    public void removePlayer(String nickname) {
        players.remove(getPlayerByNickname(nickname));
    }


    /**
     * Method getPlayerByNickname searches the player with the selected nickname
     *
     * @param nickname the nickname of the player that I wanted to have
     * @return the player with the selected nickname or null if the player with the selected nickname doesn't exist
     */
    public Player getPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equals(nickname))
                return player;
        }
        return null;
    }


    /**
     * Method addClouds adds clouds
     *
     * @param numberOfClouds the number of the clouds that I wanted to add
     */
    private void addClouds(int numberOfClouds) {
        for (int i = 0; i < numberOfClouds; i++)
            clouds.add(new Cloud());
    }


    /**
     * Method getAssistantByName searches the assistant that I select
     *
     * @param assistantName the name of the searched assistant
     * @return the assistant searched
     */
    public Assistant getAssistantByName(AssistantName assistantName) {
        Assistant ast = null;
        for (Assistant assistant : assistants) {
            if (assistant.getAssistant() == assistantName)
                ast = assistant;
        }
        return ast;
    }


    /**
     * Method calculateIslandNewOwner calculates the new island owner
     *
     * @param island the island on which I want to check the influence
     * @return the new island's owner or null if the owner doesn't change
     */
    public Player calculateIslandNewOwner(Island island) {
        Player currOwner = null;
        int maxInfluence = 0;
        int influence;
        for (Player player : players) {
            influence = influenceStrategy.calculateInfluence(player, island, professorManager);
            if (influence > maxInfluence) {
                maxInfluence = influence;
                currOwner = player;
            } else if (influence == maxInfluence) {
                currOwner = null;
            }
        }
        return currOwner;
    }


    /**
     * Method updateInfluence checks per each player the influence on the specified island, change the island's owner
     * if necessary and moves the towers consequently
     *
     * @param island the island on which I want to update influence
     */
    public void updateInfluence(Island island) {
        Player currentOwner = calculateIslandNewOwner(island);

        if (currentOwner != null) {
            if (island.getOwner() == null) {
                island.setOwner(currentOwner);
                currentOwner.getPlayerBoard().removeTowers(1);
                island.setNumberOfTowers(1);
                archipelago.changeIslandOwner(currentOwner, island);
                archipelago.mergeIslands(island);
            } else {
                if (!currentOwner.equals(island.getOwner())) {
                    island.getOwner().getPlayerBoard().addTowers(island.getNumberOfTowers());
                    currentOwner.getPlayerBoard().removeTowers(island.getNumberOfTowers());
                    archipelago.changeIslandOwner(currentOwner, island);
                    archipelago.mergeIslands(island);
                }
            }
        }

        this.setInfluenceStrategy(new StandardInfluence());
    }

    public CharacterCard getCharacterCardByName(CharacterName name) {
        for (CharacterCard card : characterCards) {
            if (card.getName() == name)
                return card;
        }
        return null;
    }


    /**
     * Method initCharacterCards initializes the character cards for the game;
     */
    public void initCharacterCards() {
        List<CharacterName> extractCards = randomCharacterExtraction();

        for (CharacterName extractCard : extractCards)
            switch (extractCard) {
                case BANKER:
                    characterCards.add(new Banker());
                    break;
                case POSTMAN:
                    characterCards.add(new PostMan());
                    break;
                case PROF_CARD:
                    characterCards.add(new ProfCard());
                    break;
                case KNIGHT:
                    characterCards.add(new InfluenceCard(CharacterName.KNIGHT, 2, new MorePointsInfluence()));
                    break;
                case CENTAUR:
                    characterCards.add(new InfluenceCard(CharacterName.CENTAUR, 3, new NoTowerInfluence()));
                    break;
                case SELLER:
                    characterCards.add(new InfluenceCard(CharacterName.SELLER, 3, new NoColorInfluence()));
                    break;
                case HERBALIST:
                    characterCards.add(new BanCharacter());
                    break;
                case VASSAL:
                    characterCards.add(new Vassal());
                    break;
                case MUSICIAN:
                    characterCards.add(new Musician());
                    break;
                case JESTER:
                    characterCards.add(new Jester(bag));
                    break;
                case PRINCESS:
                    characterCards.add(new Princess(bag));
                    break;
                case MONK:
                    characterCards.add(new Monk(bag));
                    break;
            }
    }


    /**
     * Method randomCharacterExtraction takes randomly 3 character cards
     *
     * @return the 3 character cards extracted
     */
    public List<CharacterName> randomCharacterExtraction() {
        int numberOfCharacter = 12;
        List<CharacterName> characters = Arrays.asList(CharacterName.values());
        List<CharacterName> extractCharacters = new ArrayList<>();

        Random rand = new Random();
        CharacterName extractCharacter;
        boolean find;

        for (int i = 0; i < 3; i++) {
            find = true;
            while (find) {

                extractCharacter = characters.get(rand.nextInt(numberOfCharacter));

                if (!extractCharacters.contains(extractCharacter)) {
                    extractCharacters.add(extractCharacter);
                    numberOfCharacter--;
                    find = false;
                }

            }
        }
        return extractCharacters;
    }

}
