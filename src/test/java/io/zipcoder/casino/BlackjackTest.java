package io.zipcoder.casino;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Rank;
import io.zipcoder.casino.cards.Suit;
import io.zipcoder.casino.games.Blackjack;
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

        // Given
        String expectedPlayerName = "test";
        // When
        String actualPlayerName = blackjack.getPlayer().getName();
        // Then
        Assert.assertEquals(expectedPlayerName, actualPlayerName);
    }

    @Test
    public void sumOfRanksInHandTest() {
        // Add cards to player1 Hand
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(new Card(Rank.DEUCE, Suit.CLUBS),new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES)));

        blackjack.getPlayer().receiveCards(cards);

        // Given
        int expectedPersonHandSum = 5;

        // When
        int actualPersonHandSum = blackjack.rankSum(blackjack.getPlayer());

        // Then
        Assert.assertEquals(expectedPersonHandSum, actualPersonHandSum);

    }

    @Test
    public void countRankRepetitionsInHandTest() {
        // Add cards to player1 Hand
        Card notShuffled0 = new Card(Rank.DEUCE, Suit.CLUBS);
        Card notShuffled1 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card notShuffled2 = new Card(Rank.ACE, Suit.HEARTS);
        Card notShuffled3 = new Card(Rank.ACE, Suit.SPADES);
        blackjack.getPlayer().receiveCards(notShuffled0, notShuffled1, notShuffled2, notShuffled3);

        // Given
        int expectedPersonRankReps = 3; // ACE
        // When
        int actualPersonRankReps = blackjack.rankRepeats(blackjack.getPlayer(), notShuffled1.toInt());
        // Then
        Assert.assertEquals(expectedPersonRankReps, actualPersonRankReps);

    }


    @Test
    public void personDecisionTest() {

        //String playerDecisionString = blackjack.personDecision(blackjack.getPlayer());
    }

    @Test
    public void handToStringTest() {
        // Given
        player.receiveCards(new Card(Rank.DEUCE, Suit.CLUBS), new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES));
        String expectedHandToString = "5♠ 4♥ 3♦ 2♣";
        // When
        String actualHandToString = blackjack.handToString(player);
        System.out.println(actualHandToString);
        // Then
        Assert.assertEquals(expectedHandToString, actualHandToString);
    }

    @Test
    public void hitTest() {
        // if playerSum < 21, player can hit
        // if playerDecision = "hit", then dealer draws card and player hand receives card

        // Add cards to player1 Hand
        Card playerCard0 = new Card(Rank.DEUCE, Suit.CLUBS);
        Card playerCard1 = new Card(Rank.THREE, Suit.DIAMONDS);
        blackjack.getPlayer().receiveCards(playerCard0, playerCard1);

        // Given
        blackjack.hit(blackjack.getPlayerHand());
        blackjack.hit(blackjack.getPlayerHand());
        int expectedPlayerHandSize = 3;
        // When
        int actualPlayerHandSize = blackjack.getPlayer().getPlayerHand().size();
        // Then
        Assert.assertEquals(expectedPlayerHandSize, actualPlayerHandSize);
    }

    // Will keep this for future reference
//    @Test
//    public void askForPlayerNameTest() {
//        // Given
//        String expectedName = "Luis";
//        // When
//        Blackjack blackjack = new Blackjack("Luis", 50);
//        String askedName = blackjack.askForPlayerName();
//        String actualName = blackjack.getPlayer().getName();
//        // Then
//        Assert.assertEquals(expectedName, actualName);
//    }

    // GamblingInterface
    @Test
    public void placeBetTest() {
        // Given
        int betPlaced = 1;
        int chipsToStart = 500;
        int expectedChipsRemaining = chipsToStart - betPlaced; // 499
        // When
        Blackjack blackjack = new Blackjack(player);
        blackjack.placeBet(blackjack.getPlayer(), betPlaced); // will remove 1 chip
        int actualChipsRemaining = blackjack.getPlayer().getWallet().checkChips();
        // Then
        Assert.assertEquals(expectedChipsRemaining,actualChipsRemaining);
    }

    @Test
    public void checkNumberOfCards() {

    }
}
