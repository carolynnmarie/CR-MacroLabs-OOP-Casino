package io.zipcoder.casino.money;

import io.zipcoder.casino.people.Person;

public interface GamblingInterface {
    int checkChipAmount(Person personToCheck);
    void placeBet(Person personPlacingBet, int betAmount);
    int getAnte();
    boolean bootPlayerFromGame(Person personToBoot);
    int checkPot();
}
