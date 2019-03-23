package io.zipcoder.casino.Cards;

public class Card {

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRankInt(){
        return rank.toInt();
    }

    public String toString() {
        return rank + "" + suit;
    }

}
