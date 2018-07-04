package io.zipcoder.casino.cards;

import java.util.ArrayList;
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
        Collections.shuffle(deckOfCards);
    }

    public void setDeckOfCards(ArrayList<Card> deckOfCards) {
        this.deckOfCards = deckOfCards;
    }

    public ArrayList<Card> getDeckOfCards(){
        return this.deckOfCards;
    }

    public void shuffleDeck(){
        Collections.shuffle(this.deckOfCards);
    }

    public Card drawCard(){
        return this.deckOfCards.remove(0);
    }

    public Card getCard(){
        return this.deckOfCards.get(0);
    }

    public void clearDeck(){
        this.deckOfCards.clear();
    }


    public ArrayList<Card> dealHand(int numberOfCards){
        ArrayList<Card> hand = new ArrayList<>();
        for(int i = 0; i<numberOfCards; i++){
            hand.add(this.deckOfCards.remove(i));
        }
        setDeckOfCards(this.deckOfCards);
        return hand;
    }

}

