package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;

public interface CardGameInterface {

    ArrayList<Card> deck = new ArrayList<Card>();

    int checkNumberOfCards(Person person);

    void dealCards();
}