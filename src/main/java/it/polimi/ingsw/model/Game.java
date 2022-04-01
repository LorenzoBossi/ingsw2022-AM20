package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Cloud> clouds;
    private final IslandsManager archipelago;
    private List<Player> players;
    private final List<Assistant> assistants;
    private final Bag bag;
    private Player currPlayer;

    /**
     * Constructor
     */
    public Game() {
        clouds = new ArrayList<Cloud>();
        archipelago = new IslandsManager();
        players = new ArrayList<Player>();
        assistants = new ArrayList<Assistant>();
        for(AssistantName assistant : AssistantName.values())
            assistants.add(new Assistant(assistant));
        bag = new Bag();
        currPlayer = null;
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

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
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
        if(player == null)
            return false;
        return true;
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
        players.add(new Player(nickname));
        return true;
    }


    /**
     * Method removePlayer removes the player with the selected nickname
     *
     * @param nickname the player that i wanted to remove
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

}
