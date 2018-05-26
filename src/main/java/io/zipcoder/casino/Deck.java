package io.zipcoder.casino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Deck {

    private ArrayList<Card> deckOfCards;

    public Deck() {
        this.deckOfCards = new ArrayList<>();
        for (Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                deckOfCards.add(new Card(rank, suit));
            }
        }
    }

    public ArrayList<Card> getDeckOfCards(){
        return deckOfCards;
    }

    public void shuffleDeck(){
        Collections.shuffle(deckOfCards);
    }

    public Card drawCard(){
        return deckOfCards.remove(deckOfCards.size() - 1);
    }

    public void clearDeck(){
        deckOfCards.clear();
    }

}

