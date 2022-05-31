package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.List;

public class CharacterCardView {
    private CharacterCardType type;
    private int coinsRequired;
    private List<Color> students;
    private int banCards;
    private String description;

    public CharacterCardView(CharacterName name, CharacterCardType type, int coinsRequired, List<Color> students) {
        this.type = type;
        this.coinsRequired = coinsRequired;
        this.students = students;
        if (name == CharacterName.HERBALIST)
            banCards = 4;
        description = linkDescription(name);
    }

    public void addStudents(List<Color> students) {
        this.students.addAll(students);
    }

    public void removeStudents(List<Color> students) {
        for (Color student : students)
            this.students.remove(student);
    }

    public void addCoin() {
        coinsRequired++;
    }

    public void addBanCard() {
        banCards++;
    }

    public void removeBanCard() {
        banCards--;
    }

    public CharacterCardType getType() {
        return type;
    }

    public int getCoinsRequired() {
        return coinsRequired;
    }

    public List<Color> getStudents() {
        return students;
    }

    public int getBanCards() {
        return banCards;
    }

    public String getDescription() {
        return description;
    }

    public String linkDescription(CharacterName name) {
        JSONParser parser = new JSONParser();
        String desc = null;
        try {
            Object obj = parser.parse(new FileReader("src/main/resources/description.json"));
            JSONObject jsonObject = (JSONObject) obj;
            desc = (String) jsonObject.get(name.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }
}
