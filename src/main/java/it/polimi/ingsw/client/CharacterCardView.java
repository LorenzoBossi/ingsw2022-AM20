package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Class CharacterCardView is the character card representation client side
 */
public class CharacterCardView {
    private CharacterCardType type;
    private int coinsRequired;
    private List<Color> students;
    private int banCards;
    private String description;

    /**
     * Constructor
     * @param name the name of the characterCard
     * @param type the type of the characterCard
     * @param coinsRequired the coins required in order to activate the card
     * @param students students on the character card
     */
    public CharacterCardView(CharacterName name, CharacterCardType type, int coinsRequired, List<Color> students) {
        this.type = type;
        this.coinsRequired = coinsRequired;
        this.students = students;
        if (name == CharacterName.HERBALIST)
            banCards = 4;
        description = linkDescription(name);
    }

    /**
     * Adds students to the card
     * @param students the students to add
     */
    public void addStudents(List<Color> students) {
        this.students.addAll(students);
    }

    /**
     * Remove students from the card
     * @param students the students to remove
     */
    public void removeStudents(List<Color> students) {
        for (Color student : students)
            this.students.remove(student);
    }

    /**
     * Increase coins required in order to activate the card
     */
    public void addCoin() {
        coinsRequired++;
    }

    /**
     * Increase the number of the ban card on the character card
     */
    public void addBanCard() {
        banCards++;
    }

    /**
     * Remove a ban card on the character card
     */
    public void removeBanCard() {
        banCards--;
    }

    /**
     * Gets the type of the character card
     * @return the type of the character card
     */
    public CharacterCardType getType() {
        return type;
    }

    /**
     * Gets the coins required to activate the card
     * @return the coins required to activate the card
     */
    public int getCoinsRequired() {
        return coinsRequired;
    }

    /**
     * Gets the students on the cards
     * @return the students on the card
     */
    public List<Color> getStudents() {
        return students;
    }

    /**
     * Gets the number of the ban cards on the card
     * @return
     */
    public int getBanCards() {
        return banCards;
    }

    /**
     * Gets the description of the card
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Links the character card with his description
     * @param name the name of the character card
     * @return the description of the character card
     */
    public String linkDescription(CharacterName name) {
        JSONParser parser = new JSONParser();
        String desc = null;
        try {
            InputStream inputstream = getClass().getResourceAsStream("/json/descriptionGUI.json");
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(inputstream, "UTF-8"));
            desc = (String) jsonObject.get(name.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }
}
