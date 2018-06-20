package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.People.Hand;

import java.util.ArrayList;

public interface CardGameInterface {

    ArrayList<Card> deck = new ArrayList<Card>();

    int checkNumberOfCards(Hand hand);

    void dealCards();
}