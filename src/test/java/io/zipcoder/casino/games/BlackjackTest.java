package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Rank;
import io.zipcoder.casino.cards.Suit;
import io.zipcoder.casino.games.cardGames.Blackjack;
import io.zipcoder.casino.people.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class BlackjackTest {


    Person player = new Person("test");
    Blackjack blackjack = new Blackjack(player);


    @Before public void setUp() {
        player.getWallet().addChips(500);
    }

    @Test
    public void BlackJackDefaultConstructorTest() {
        String expectedPlayerName = "test";
        String actualPlayerName = blackjack.getPlayer1().getName();
        Assert.assertEquals(expectedPlayerName, actualPlayerName);
    }

    @Test
    public void rankSumTest() {
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(new Card(Rank.DEUCE, Suit.CLUBS),new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES)));
        int expectedPersonHandSum = 5;
        int actualPersonHandSum = blackjack.rankSum(cards);
        Assert.assertEquals(expectedPersonHandSum, actualPersonHandSum);
    }


    @Test
    public void handToStringTest() {
        // Given
        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card(Rank.DEUCE, Suit.CLUBS), new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES)));
        String expectedHandToString = "2♣ 3♦ 4♥ 5♠";
        // When
        String actualHandToString = blackjack.displayCards(hand);
        // Then
        Assert.assertEquals(expectedHandToString, actualHandToString);
    }

    @Test
    public void hitTest() {
        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card(Rank.DEUCE, Suit.CLUBS),new Card(Rank.THREE, Suit.DIAMONDS)));

        blackjack.hit(hand);
        blackjack.hit(hand);
        int expectedPlayerHandSize = 4;
        // When
        int actualPlayerHandSize = hand.size();
        // Then
        Assert.assertEquals(expectedPlayerHandSize, actualPlayerHandSize);
    }

    @Test
    public void placeBetTest() {
        // Given
        int betPlaced = 1;
        int chipsToStart = 500;
        int expectedChipsRemaining = chipsToStart - betPlaced; // 499
        // When
        Blackjack blackjack = new Blackjack(player);
        blackjack.placeBet(blackjack.getPlayer1(), betPlaced); // will remove 1 chip
        int actualChipsRemaining = blackjack.getPlayer1().getWallet().checkChips();
        // Then
        Assert.assertEquals(expectedChipsRemaining,actualChipsRemaining);
    }

}
