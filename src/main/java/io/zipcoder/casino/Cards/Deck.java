package io.zipcoder.casino.Cards;

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
    }

    public ArrayList<Card> getDeck(){
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

    public int deckSize(){
        return deckOfCards.size();
    }

    public void addCards(ArrayList<Card> cards){
        this.deckOfCards.addAll(cards);
    }

    public ArrayList<Card> dealCards(int numberOfCards){
        ArrayList<Card> hand = new ArrayList();
        for(int i = 0; i<numberOfCards; i++){
            hand.add(deckOfCards.get(i));
        }
        deckOfCards.removeAll(hand);
        return hand;
    }
}

