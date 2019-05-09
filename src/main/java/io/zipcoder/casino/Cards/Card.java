package io.zipcoder.casino.Cards;

import java.util.*;

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
        return rank.toString() + "" + suit.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return getRank() == card.getRank() &&
                getSuit() == card.getSuit();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRank(), getSuit());
    }
}
