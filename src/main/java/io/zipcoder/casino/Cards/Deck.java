package io.zipcoder.casino.Cards;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {

    private ArrayList<Card> deckOfCards;
    private Rank rank;
    private Suit suit;

    public Deck() {

        this.deckOfCards = new ArrayList<Card>();

        for (Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                deckOfCards.add(new Card(rank, suit));
            }
        }
    }

    public ArrayList<Card> getDeckOfCards(){

        return this.deckOfCards;
    }

    public void shuffleDeck(){
        Collections.shuffle(this.deckOfCards);
    }

    public Card drawCard(){

        return this.deckOfCards.remove(deckOfCards.size() - 1);
    }

    public void clearDeck(){
        this.deckOfCards.clear();
    }

}

