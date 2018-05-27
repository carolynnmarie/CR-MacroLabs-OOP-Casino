package io.zipcoder.casino.cards;

public class Card {

    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
    public Card(){}

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int toInt(){
        return this.rank.toInt();
    }

    public String toString() {
        return rank + "" + suit;
    }

}
