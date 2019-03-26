package io.zipcoder.casino.Games;

import io.zipcoder.casino.People.Hand;

public interface CardGameInterface extends GameInterface {

    int checkNumberOfCards(Hand hand);
    void dealCards();
    void engine();

}