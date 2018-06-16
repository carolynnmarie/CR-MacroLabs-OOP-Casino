package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.cards.Rank;
import io.zipcoder.casino.cards.Suit;
import io.zipcoder.casino.games.GoFish;
import io.zipcoder.casino.people.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GoFishTest {

    private Deck houseDeckTest = new Deck();

    private Person player1 = new Person("Carolynn");
    private Person dealer = new Person("Dealer");
    private Person playerBookTest = new Person("BookTest");

    private Card card1 = new Card(Rank.SEVEN,Suit.SPADES);
    private Card card2 = new Card(Rank.SEVEN,Suit.HEARTS);
    private Card card3 = new Card(Rank.SEVEN,Suit.DIAMONDS);
    private Card card4 = new Card(Rank.SEVEN,Suit.CLUBS);
    private Card card5 = new Card(Rank.ACE,Suit.SPADES);


    @Test
    public void StartingDeckTest() {

        GoFish goFishTest = new GoFish(player1);
        goFishTest.setDealerHand(7);
        goFishTest.setPlayerHand(7);
        int expected = 38;
        int actual = goFishTest.getHouseDeck().getDeckOfCards().size();

        Assert.assertEquals(expected, actual);
    }

//    @Test
//    public void doYouHaveTheCardThatIWant() {
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//        Card cardSend = new Card(Rank.ACE, Suit.SPADES);
//        int expected = 1;
//        int actual = goFishTest.compAsksForCard(1, dealerTest);
//
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void removeCardsFromComputerPlayerAndIntoHandTest () {
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//
//        Card cardSend = new Card(Rank.ACE, Suit.SPADES);
//
//        int actual = goFishTest.moveFromComputerToPlayer(1, dealerTest, player1Test);
//        int expected = cardSend.getRank().toInt();
//
//
//        Assert.assertEquals(expected, actual);
//    }

    @Test
    public void BookCheckerTest(){
        String expected = "\n!$!$!$!$!$!$! You Scored a Book! (Four of a kind) !$!$!$!$!$!$!\n" +
                "!!!!Your Book Total: 1\n";
        String actual = playerBookTest.getPlayerHand().toString();
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void goFishTest(){
        Card cardExpected = new Card(Rank.KING,Suit.SPADES);

        String expected = "K♠";
        String actual = cardExpected.toString();

        Assert.assertEquals(expected, actual);
    }

//    @Test
//    public void checkNumberOfHandsTest(){
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//
//        int expected = 0;
//        int actual = goFishTest.;
//
//        Assert.assertEquals(expected, actual);
//    }

}
