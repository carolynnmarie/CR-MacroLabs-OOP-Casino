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

    public ArrayList<Card> getDeckOfCards(){
        return deckOfCards;
    }

    public void shuffleDeck(){
        Collections.shuffle(deckOfCards);
    }

    public Card drawCard(){
        return deckOfCards.remove(deckOfCards.size() - 1);
    }

    public Card getCard(){
        return deckOfCards.get(deckOfCards.size()-1);
    }

    public void clearDeck(){
        deckOfCards.clear();
    }


    public ArrayList<Card> dealHand(int beginningHand){
        ArrayList<Card> hand = new ArrayList<>();
        for(int i = 0; i<beginningHand; i++){
            hand.add(deckOfCards.remove(deckOfCards.size()-1-i));
        }
        return hand;
    }

}

