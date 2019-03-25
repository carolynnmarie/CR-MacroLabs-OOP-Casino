package io.zipcoder.casino.People;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;

import java.util.ArrayList;
import java.util.Collections;


public class Hand {

    private ArrayList<Card> handArrayList;


    public Hand() {
        this.handArrayList = new ArrayList<>();
    }

    public ArrayList<Card> toArrayList() {
        return handArrayList;
    }

    public void receiveCard(Card card){
        handArrayList.add(card);
    }

    public void receiveCards(Card... cards) {
        for (Card card : cards) {
            handArrayList.add(0, card);
        }
    }

    public void receiveCards(ArrayList<Card> cards){
        for (Card card : cards) {
            handArrayList.add(0, card);
        }
    }

    public Card drawCard() {
        return handArrayList.remove(handArrayList.size() - 1);
    }

    public void clearHand() {
        handArrayList.clear();
    }

    public void shuffleHand() {
        Collections.shuffle(handArrayList);
    }

    public void removeCard(Card card){
        if(handArrayList.contains(card)){
            handArrayList.remove(card);
        }
    }

}
