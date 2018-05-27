package io.zipcoder.casino;

import io.zipcoder.casino.people.Person;

public class Main {

    public static void main(String[] args) {
        Person player = Casino.newPlayer();
        Casino.sendPlayerToGame(player);
    }

}