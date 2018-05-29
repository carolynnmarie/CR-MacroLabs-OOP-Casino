package io.zipcoder.casino.games;

import io.zipcoder.casino.people.Person;

public abstract class Game implements GameInterface {

    // default constructor for 2 players? (minimum number of players);

    private Person player;
    public Game(Person player){
        this.player = player;
    }

    public Game(){}

    public abstract void start();

    public abstract void end();

}
