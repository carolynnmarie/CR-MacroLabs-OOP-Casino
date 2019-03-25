package io.zipcoder.casino.games.cardGames;


import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.games.GameInterface;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;


public interface CardGameInterface extends GameInterface {

    int checkNumberOfCards();

    ArrayList<Card> dealCards();

    String displayCards(ArrayList<Card> hand);
}