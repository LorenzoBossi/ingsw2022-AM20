package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.DrawException;
import it.polimi.ingsw.model.characterCards.*;
import it.polimi.ingsw.network.messages.serverMessage.ExtractedCard;
import it.polimi.ingsw.network.messages.serverMessage.GameEnd;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;
import it.polimi.ingsw.network.messages.serverMessage.PlayerCoinsEvent;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The container of all the elements of the game
 */
public class Game extends ObservableSubject implements EndObserver {
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
    private int remainingRounds = 10;
    private boolean isLastRound = false;
    /**
     * Game's constants
     */
    private final int TOTAL_NUMBER_OF_COINS = 20;
    private final int STUDENTS_ENTRANCE_2P = 7;
    private final int STUDENTS_ENTRANCE_3P = 9;
    private final int TOWERS_2P = 8;//8 modified to test
    private final int TOWERS_3P = 6;
    private final int STUDENTS_CLOUD_2P = 3;
    private final int STUDENTS_CLOUD_3P = 4;


    /**
     * Constructor
     *
     * @param numberOfPlayers the number of players in the Game
     */
    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        bag = new Bag();
        archipelago = new IslandsManager();
        clouds = new ArrayList<>();
        professorManager = new ProfessorManager();
        players = new ArrayList<>();
        assistants = Assistant.getEveryAssistant();
        currPlayer = null;
        influenceStrategy = new StandardInfluence();
        characterCards = new ArrayList<>();
        phase = Phase.LOBBY;
        actionOrder = new ArrayList<>();
    }

    /**
     * Initialize the game by creating every component and filling it with students according to the game mode and rules,
     * except for character cards.
     */
    public void initGame() {
        initIslands();
        archipelago.attach(new EndGameObserver(this));
        bag.fillBag(24);
        bag.attach(new EndGameObserver(this));
        addClouds(numberOfPlayers);
        refillClouds();
        initPlayerBoard();
    }

    /**
     * Method getNumberOfCoins returns the Game's number of coins
     *
     * @return the Game's number of coins
     */
    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    /**
     * Method initCoins initializes the Game's coins and adds one coin for each player
     */
    public void initCoins() {
        this.numberOfCoins = TOTAL_NUMBER_OF_COINS;
        for (Player player : players)
            addCoinsToPlayer(player.getNickname());
    }

    /**
     * Methods initTowers initializes the towers number for each player
     *
     * @param numberOfTowers the number of Towers for each PlayerBoard
     */
    public void initTowers(int numberOfTowers) {
        EndGameObserver endGameObserver = new EndGameObserver(this);
        PlayerBoard playerBoard;
        for (Player player : players) {
            playerBoard = player.getPlayerBoard();
            playerBoard.addTowers(numberOfTowers);
            playerBoard.attach(endGameObserver);
        }
    }

    /**
     * Methods initEntrance fills all the entrance with che correct number of students
     *
     * @param numberOfStudentsInEntrance the number of students for each entrance
     */
    private void initEntrances(int numberOfStudentsInEntrance) {
        List<Color> studentsToAdd;
        List<Color> studentsToNotify = new ArrayList<>();
        for (Player player : players) {
            studentsToNotify.clear();
            PlayerBoard board = player.getPlayerBoard();
            studentsToAdd = bag.getStudents(numberOfStudentsInEntrance);
            studentsToNotify.addAll(studentsToAdd);
            board.getEntrance().refillEntrance(studentsToAdd);
            notifyObserver(new MoveStudents(GameComponent.BAG, GameComponent.ENTRANCE, studentsToNotify, null, player.getNickname()));
        }
    }

    /**
     * Method initPlayerBoard initializes all the PlayerBoards
     */
    public void initPlayerBoard() {
        if (numberOfPlayers == 2) {
            initTowers(TOWERS_2P);
            initEntrances(STUDENTS_ENTRANCE_2P);
        } else if (numberOfPlayers == 3) {
            initTowers(TOWERS_3P);
            initEntrances(STUDENTS_ENTRANCE_3P);
        }
    }

    /**
     * Method characterCardPayment add the card's payment to the Game's coin
     *
     * @param payment the cost of the card
     */
    public void characterCardPayment(int payment) {
        currPlayer.useCoins(payment);
        numberOfCoins += payment;
        notifyObserver(new PlayerCoinsEvent(-payment, currPlayer.getNickname()));
    }

    /**
     * Method addCoinsToPlayer adds a coin to a player removing from the game
     *
     * @param nickname the player's nickname that needs the coin
     */
    public void addCoinsToPlayer(String nickname) {
        Player currPlayer = getPlayerByNickname(nickname);
        numberOfCoins--;
        currPlayer.addCoin();
        notifyObserver(new PlayerCoinsEvent(1, nickname));
    }

    /**
     * Method hasEnoughCoins controls if the Game has enough coins
     *
     * @return {@code true} if the Game has enough coins,
     * {@code false} if the Game hasn't enough coins
     */
    public boolean hasEnoughCoins() {
        return this.numberOfCoins > 0;
    }

    /**
     * Put the initial students on every island except the island of mother nature and the opposite one
     */
    private void initIslands() {
        int numberOfIslands, oppositeOfMotherNature;
        int i;
        Island island;
        List<Color> student;

        numberOfIslands = archipelago.getNumberOfIslands();
        oppositeOfMotherNature = archipelago.getNumberOfIslands() / 2;
        bag.fillBag(2);

        for (i = 1; i < oppositeOfMotherNature; i++) {
            student = bag.getStudents(1);
            archipelago.addStudentOnIsland(student.get(0), i, null);
        }
        for (i = oppositeOfMotherNature + 1; i < numberOfIslands; i++) {
            student = bag.getStudents(1);
            archipelago.addStudentOnIsland(student.get(0), i, null);
        }
    }

    /**
     * getter method of the bag containing students
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * getter method of the clouds of the game
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * getter method of the IslandManager which manage every island in the playerboard
     */
    public IslandsManager getArchipelago() {
        return archipelago;
    }
    /**
     * getter method of the ProfessorManager which manage all the professors logic
     */
    public ProfessorManager getProfessorManager() {
        return professorManager;
    }

    /**
     *getter method of the players in the game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     *getter method of the players in the game which is currently playing his turn
     */
    public Player getCurrPlayer() {
        return currPlayer;
    }
    /**
     *getter method of the character cards extracted in the game
     */
    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     *getter method of the current phase of the game
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Setter method of current Player, it set the player who is playing this turn
     * @param currPlayer the player who has to play now
     */
    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    /**
     * Setter method which sets the influenceStrategy to use in the current round to calculate inflence on islands
     * @param influenceStrategy the strategy to use
     */
    public void setInfluenceStrategy(InfluenceStrategy influenceStrategy) {
        this.influenceStrategy = influenceStrategy;
    }

    /**
     * Method start initializes the game
     */
    public void start() {
        initGame();
        phase = Phase.PIANIFICATION;
        firstPlayer = players.get(0);
        currPlayer = firstPlayer;
    }

    /**
     * update the current player and the phase of the game.
     * During the action phase this method resets the number of available moves of the current player
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
                remainingRounds--;
                if (remainingRounds == 0 || isLastRound)
                    end();
                else
                    startPianificationPhase();
            } else {
                currPlayer = next;
                resetAvailableMoves(currPlayer);
            }
            professorManager.resetComparator();
        }
    }

    /**
     * Gets the next player in the Players Order (clockwise).
     *
     * @return The corresponding player
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
     * @return The corresponding player
     */
    private Player getNextAction() {
        int indexOfCurrentPlayer = actionOrder.indexOf(currPlayer);

        if (indexOfCurrentPlayer == numberOfPlayers - 1)
            return actionOrder.get(0);
        else
            return actionOrder.get(indexOfCurrentPlayer + 1);
    }

    /**
     * This methods change the current phase to ACTION and sets the right player as current player.
     */
    private void startActionPhase() {
        firstPlayer = actionOrder.get(0);
        currPlayer = firstPlayer;
        phase = Phase.ACTION;
        resetAvailableMoves(currPlayer);
    }

    /**
     * Starts a pianification phase by refilling all the clouds, selecting the new current player and resetting every assistant
     * to not already played
     */
    private void startPianificationPhase() {
        refillClouds();
        actionOrder.clear();
        currPlayer = firstPlayer;
        phase = Phase.PIANIFICATION;
        for (Assistant assistant : assistants) {
            assistant.setAlreadyPlayed(false);
        }
    }

    /**
     * Refill every cloud with a number of students depending on the number of players in the game
     */
    private void refillClouds() {
        if (numberOfPlayers == 2) {
            initClouds(STUDENTS_CLOUD_2P);
        } else if (numberOfPlayers == 3) {
            initClouds(STUDENTS_CLOUD_3P);
        }
    }

    /**
     * Method initClouds initializes the clouds
     *
     * @param numberOfPlayers the number of the player in the game
     */
    private void initClouds(int numberOfPlayers) {
        List<Color> studentsToAdd;
        int cloudId;

        for (Cloud cloud : clouds) {
            studentsToAdd = bag.getStudents(numberOfPlayers);
            cloudId = clouds.indexOf(cloud);
            cloud.fill(studentsToAdd);
            cloud.setChosen(false);
            notifyObserver(new MoveStudents(GameComponent.BAG, GameComponent.CLOUD, studentsToAdd, null, cloudId));
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

    /**
     * Gets the right character card by giving his CharacterName
     * @param name the CharacterName corresponding to the card needed
     * @return the corresponding CharacterCard in the game
     */
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

        for (CharacterName extractCard : extractCards) {
            switch (extractCard) {
                case BANKER -> characterCards.add(new Banker());
                case POSTMAN -> characterCards.add(new PostMan());
                case PROF_CARD -> characterCards.add(new ProfCard());
                case KNIGHT -> characterCards.add(new InfluenceCard(CharacterName.KNIGHT, 2, new MorePointsInfluence(), CharacterCardType.NORMAL));
                case CENTAUR -> characterCards.add(new InfluenceCard(CharacterName.CENTAUR, 3, new NoTowerInfluence(), CharacterCardType.NORMAL));
                case SELLER -> characterCards.add(new InfluenceCard(CharacterName.SELLER, 3, new NoColorInfluence(), CharacterCardType.COLOR_SELECTION));
                case HERBALIST -> characterCards.add(new BanCharacter());
                case VASSAL -> characterCards.add(new Vassal());
                case MUSICIAN -> characterCards.add(new Musician());
                case JESTER -> characterCards.add(new Jester(bag));
                case PRINCESS -> characterCards.add(new Princess(bag));
                case MONK -> characterCards.add(new Monk(bag));
            }
        }

    }

    /**
     * Notify the observer of every extracted card at the beginning of the game
     */
    public void notifyExtractedCard() {
        for (CharacterCard card : characterCards)
            notifyObserver(new ExtractedCard(card.getName(), card.getCoinsRequired(), card.getCharacterCardType(), card.getStudents()));
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
                    find = false;
                }

            }
        }
        return extractCharacters;
    }

    /**
     * find the winner of the game.
     *
     * @return the Player who won the game or null if the game ends in a draw
     */
    public Player getWinner() throws DrawException {
        int minTowers = 10;
        int maxProfessors = -1;
        int numberOfProfessors;
        int numberOfTowers;
        boolean isADraw = false;
        Player winner = currPlayer;
        for (Player player : players) {

            numberOfTowers = player.getPlayerBoard().getNumberTower();

            if (numberOfTowers < minTowers) {
                minTowers = numberOfTowers;
                winner = player;
                numberOfProfessors = professorManager.getProfessorsOwnedBy(player).size();
                maxProfessors = numberOfProfessors;
                isADraw = false;
            } else if (numberOfTowers == minTowers) {
                numberOfProfessors = professorManager.getProfessorsOwnedBy(player).size();
                if (numberOfProfessors > maxProfessors) {
                    maxProfessors = numberOfProfessors;
                    winner = player;
                    isADraw = false;
                } else if (numberOfProfessors == maxProfessors) {
                    isADraw = true;
                }
            }

            if (isADraw) {
                throw new DrawException();
            }

        }
        return winner;
    }

    /**
     * Ends the game immediately by changing the phase and notifying the observer
     * with the nickname of the winner if there is one
     */
    public void end() {
        phase = Phase.ENDED;
        try {
            Player winner = getWinner();
            notifyObserver((new GameEnd(winner.getNickname())));
        } catch (DrawException e) {
            notifyObserver(new GameEnd());
        }
    }

    /**
     * Sets a variable which ends the game at the end of the current round
     */
    public void lastRound() {
        isLastRound = true;
    }
}