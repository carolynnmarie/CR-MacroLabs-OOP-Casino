package io.zipcoder.casino.Games;

import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;

public abstract class CardGames extends Game {

    public CardGames(Person player){
        super(player);
    }

    public CardGames(){

    }

    public abstract int checkNumberOfCards(Hand hand);
    public abstract void dealCards();
    public abstract void engine();
}
