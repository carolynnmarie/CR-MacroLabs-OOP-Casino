package io.zipcoder.casino.Games;

import io.zipcoder.casino.People.Person;

public abstract class DiceGames extends Game {

    public DiceGames(Person player){
        super(player);
    }

    public DiceGames(){

    }

    public abstract void rollDice();
}
