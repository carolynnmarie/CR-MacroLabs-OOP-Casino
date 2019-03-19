package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;
import io.zipcoder.casino.Games.Blackjack;
import io.zipcoder.casino.People.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BlackjackTest {


    Person player = new Person("test");
    Blackjack blackjack = new Blackjack(player);

    @Before public void setUp() {
        player.getWallet().addChips(500);

    }

    @Test
    public void BlackJackDefaultConstructorTest() {
        /**
         * construct a blackjack game
         * - extends class Game
         * - implements Card interface
         * - implements Gambling interface
         */
        // Given
        String expectedPlayerName = "test";
        // When
        Blackjack blackjack = new Blackjack(player);
        String actualPlayerName = blackjack.getPlayer().getName();
        // Then
        Assert.assertEquals(expectedPlayerName, actualPlayerName);
    }

    @Test
    public void sumOfRanksInHandTest() {

        // Add cards to player1 Hand
        Card notShuffled0 = new Card(Rank.DEUCE, Suit.CLUBS);
        Card notShuffled1 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card notShuffled2 = new Card(Rank.ACE, Suit.HEARTS);
        Card notShuffled3 = new Card(Rank.ACE, Suit.SPADES);
        blackjack.getPlayer().getHand().receiveCards(notShuffled0, notShuffled1, notShuffled2, notShuffled3);

        // Add cards to Dealer Hand
        Card dealerCard0 = new Card(Rank.ACE, Suit.CLUBS);
        Card dealerCard1 = new Card(Rank.TEN, Suit.DIAMONDS);
        Card dealerCard2 = new Card(Rank.JACK, Suit.HEARTS);
        Card dealerCard3 = new Card(Rank.FIVE, Suit.SPADES);
        blackjack.getDealer().getHand().receiveCards(dealerCard0, dealerCard1, dealerCard2, dealerCard3);

        // Given
        int expectedPersonHandSum = 5; // 5 = 2 + 1 + 1 + 1
        int expectedDealerHandSum = 26; // 26 = 1 + 10 + 10 + 5
        // When
        int actualPersonHandSum = blackjack.handTotal(blackjack.getPlayer());
        int actualDealerHandSum = blackjack.handTotal(blackjack.getDealer());
        // Then
        Assert.assertEquals(expectedPersonHandSum, actualPersonHandSum);
        Assert.assertEquals(expectedDealerHandSum, actualDealerHandSum);
    }
/*
    @Test
    public void countRankRepetitionsInHandTest() {

        // Add cards to player1 Hand
        Card notShuffled0 = new Card(Rank.DEUCE, Suit.CLUBS);
        Card notShuffled1 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card notShuffled2 = new Card(Rank.ACE, Suit.HEARTS);
        Card notShuffled3 = new Card(Rank.ACE, Suit.SPADES);
        blackjack.getPlayer().getHand().receiveCards(notShuffled0, notShuffled1, notShuffled2, notShuffled3);

        // Add cards to Dealer Hand
        Card dealerCard0 = new Card(Rank.SIX, Suit.CLUBS);
        Card dealerCard1 = new Card(Rank.SIX, Suit.DIAMONDS);
        Card dealerCard2 = new Card(Rank.EIGHT, Suit.HEARTS);
        //Card dealerCard3 = new Card(Rank.FIVE, Suit.SPADES);
        blackjack.getDealer().getHand().receiveCards(dealerCard0, dealerCard1, dealerCard2);

        // Given
        int expectedPersonRankReps = 3; // ACE
        int expectedDealerRankReps = 2; // SIX
        // When
        int actualPersonRankReps = blackjack.countAceDuplicates(blackjack.getPlayer(), notShuffled1);
        int actualDealerRankReps = blackjack.countAceDuplicates(blackjack.getDealer(), dealerCard0);
        // Then
        Assert.assertEquals(expectedPersonRankReps, actualPersonRankReps);
        Assert.assertEquals(expectedDealerRankReps, actualDealerRankReps);
    }
*/

    @Test
    public void personDecisionTest() {
        Blackjack blackjack = new Blackjack(player);
        //String playerDecisionString = blackjack.personDecision(blackjack.getPlayer());
    }

    @Test
    public void handToStringTest() {
        blackjack.getPlayer().getHand().receiveCards(new Card(Rank.DEUCE, Suit.CLUBS), new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.HEARTS), new Card(Rank.FIVE, Suit.SPADES));
        String expectedHandToString = "5♠ 4♥ 3♦ 2♣";
        // When
        String actualHandToString = blackjack.handToString(blackjack.getPlayer());
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
        blackjack.getPlayer().getHand().receiveCards(playerCard0, playerCard1);

        // Add cards to Dealer Hand
        Card dealerCard0 = new Card(Rank.SIX, Suit.CLUBS);
        Card dealerCard1 = new Card(Rank.SEVEN, Suit.DIAMONDS);
        blackjack.getDealer().getHand().receiveCards(dealerCard0, dealerCard1);

        // Given
        blackjack.hit(blackjack.getPlayer());
        blackjack.hit(blackjack.getDealer());
        int expectedPlayerHandSize = 3;
        int expectedDealerHandSize = 3;
        // When
        int actualPlayerHandSize = blackjack.getPlayer().getHand().toArrayList().size();
        int actualDealerHandSize = blackjack.getDealer().getHand().toArrayList().size();
        // Then
        Assert.assertEquals(expectedPlayerHandSize, actualPlayerHandSize);
        Assert.assertEquals(expectedDealerHandSize, actualDealerHandSize);
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
        int actualChipsRemaining = blackjack.getPlayer().getWallet().checkChipAmount();
        // Then
        Assert.assertEquals(expectedChipsRemaining,actualChipsRemaining);
    }

    @Test
    public void checkNumberOfCards() {

    }
}
