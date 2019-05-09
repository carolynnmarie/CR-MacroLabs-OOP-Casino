package io.zipcoder.casino.Games;

import io.zipcoder.casino.People.*;

public abstract class CardGames extends Game {

    public CardGames(Person player){
        super(player);
    }

    public CardGames(){
    }

    public abstract int checkHandSize(Hand hand);
    public abstract void dealCards();
    public abstract void engine();
}
