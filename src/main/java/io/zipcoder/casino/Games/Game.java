package io.zipcoder.casino.Games;

import io.zipcoder.casino.People.Person;

public abstract class Game implements GameInterface {

    protected Person player;

    public Game(){}

    public Game(Person player){
        this.player = player;
    }

    public abstract void start();
    public abstract void end();

}
