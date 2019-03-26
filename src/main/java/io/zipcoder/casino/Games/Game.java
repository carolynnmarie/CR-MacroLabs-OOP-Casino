package io.zipcoder.casino.Games;

import io.zipcoder.casino.People.Person;

public abstract class Game {

    protected Person player;

    public Game(){}

    public Game(Person player){
        this.player = player;
    }

    public abstract void start();
    public abstract void end();

    public Person getPlayer(){
        return player;
    }

}
