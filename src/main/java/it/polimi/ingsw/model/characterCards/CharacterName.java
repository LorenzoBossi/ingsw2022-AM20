package it.polimi.ingsw.model.characterCards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CharacterName {
    BANKER(null),
    POSTMAN(null),
    PROF_CARD(null),
    KNIGHT(null),
    CENTAUR(null),
    SELLER(Collections.singletonList(Requirement.COLOR_SELECTION)),
    HERBALIST(Collections.singletonList(Requirement.ISLAND_SELECTION)),
    VASSAL(Collections.singletonList(Requirement.ISLAND_SELECTION)),
    MUSICIAN(Collections.singletonList(Requirement.SWAPS)),
    JESTER(Collections.singletonList(Requirement.SWAPS)),
    PRINCESS(Collections.singletonList(Requirement.COLOR_SELECTION)),
    MONK(Arrays.asList(Requirement.COLOR_SELECTION, Requirement.ISLAND_SELECTION));


    private final List<Requirement> requirements;

    CharacterName(List<Requirement> requirements){
        this.requirements = requirements;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }
}
