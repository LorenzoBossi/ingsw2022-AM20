package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {
    private List<Cloud> clouds;
    private final IslandsManager archipelago;
    private ProfessorManager professorManager;
    private List<Player> players;
    private final List<Assistant> assistants;
    private final Bag bag;
    private Player currPlayer;
    private InfluenceStrategy influenceStrategy;
    private List<CharacterCard> characterCards;

    /**
     * Constructor
     */
    public Game() {
        clouds = new ArrayList<>();
        archipelago = new IslandsManager();
        professorManager = new ProfessorManager();
        players = new ArrayList<>();
        assistants= Assistant.getEveryAssistant();
        bag = new Bag();
        currPlayer = null;
        influenceStrategy = new StandardInfluence();
        characterCards = new ArrayList<>();
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

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public void setInfluenceStrategy(InfluenceStrategy influenceStrategy) {
        this.influenceStrategy = influenceStrategy;
    }


    /**
     * Method isSameNickname checks if already exists a player with the chosen nickname
     *
     * @param nickname the nickname that I wanted to check
     * @return {@code true} if already exists a player with the chosen nickname,
     *         {@code false} if not exists a player with the chosen nickname
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
     *         {@code false} if the nickname has been chosen
     */
    public boolean addPlayer(String nickname) {
        if(isSameNickname(nickname))
            return false;
        players.add(new Player(nickname,assistants));
        if(currPlayer==null)
            currPlayer=players.get(0);
        return true;
    }


    /**
     * Method removePlayer removes the player with the selected nickname
     *
     * @param nickname the player that I wanted to remove
     */
    public void removePlayer(String nickname){
        players.remove(getPlayerByNickname(nickname));
    }


    /**
     * Method getPlayerByNickname searches the player with the selected nickname
     *
     * @param nickname the nickname of the player that I wanted to have
     * @return the player with the selected nickname or null if the player with the selected nickname doesn't exist
     */
    public Player getPlayerByNickname(String nickname) {
        for(Player player : players){
            if(player.getNickname().equals(nickname))
                return player;
        }
        return null;
    }


    /**
     * Method addClouds adds clouds
     *
     * @param numberOfClouds the number of the clouds that I wanted to add
     */
    public void addClouds(int numberOfClouds){
        for(int i = 0; i < numberOfClouds; i++)
            clouds.add(new Cloud());
    }


    /**
     * Method getAssistantByName searches the assistant that I select
     *
     * @param assistantName the name of the searched assistant
     * @return the assistant searched
     */
    public Assistant getAssistantByName(AssistantName assistantName){
        Assistant ast = null;
        for(Assistant assistant : assistants) {
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
        int influence = 0;
        for(Player player : players){
            influence = influenceStrategy.calculateInfluence(player, island, professorManager);
            if(influence > maxInfluence){
                maxInfluence = influence;
                currOwner = player;
            }
            else if(influence == maxInfluence){
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
                archipelago.mergeIslands(island);
            } else {
                if (!currentOwner.equals(island.getOwner())) {
                    island.getOwner().getPlayerBoard().addTowers(island.getNumberOfTowers());
                    currentOwner.getPlayerBoard().removeTowers(island.getNumberOfTowers());
                    island.setOwner(currentOwner);
                    archipelago.mergeIslands(island);
                }
            }
        }

        this.setInfluenceStrategy(new StandardInfluence());
    }


    /**
     * Method initCharacterCards initializes the character cards for the game;
     */
    public void initCharacterCards() {
        List<CharacterName> extractCards = randomCharacterExtraction();

        for(CharacterName extractCard : extractCards)
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
    public List<CharacterName> randomCharacterExtraction(){
        int numberOfCharacter = 12;
        List<CharacterName> characters = Arrays.asList(CharacterName.values());
        List<CharacterName> extractCharacters = new ArrayList<>();

        Random rand = new Random();
        CharacterName extractCharacter;
        boolean find;

        for(int i = 0; i < 3; i++){
            find = true;
            while(find) {

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
