package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import io.zipcoder.casino.People.Dealer;
import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;

import io.zipcoder.casino.Scoreboard;
import java.util.*;

public class War extends Game implements GameInterface, CardGameInterface {

    private Dealer dealer;
    private Person player1;
    private ArrayList<Card> playerPlayedCards;
    private ArrayList<Card> dealerPlayedCards;
    private Scanner input;
    private Hand dealerHand;
    private Hand playerHand;
    private Scoreboard scoreboard;

    public War(Person player) {
        this.player1 = player;
        this.dealer = new Dealer();
        this.dealerHand = dealer.getHand();
        this.playerHand = player1.getHand();
        this.playerPlayedCards = new ArrayList<>();
        this.dealerPlayedCards = new ArrayList<>();
        this.input = new Scanner(System.in);
        Person[] people = {player1,dealer};
        this.scoreboard = new Scoreboard(people);
    }

    public void start() {
        System.out.println("Welcome to WAR! Press Enter key to play a card\nType 'exit' at any time to end the game");
        dealCards();
        engine();
        end();
    }

    public void dealCards() {
        Deck deck = new Deck();
        deck.shuffleDeck();
        for (int i = 0; i < 26; i++) {
            dealerHand.receiveCards(deck.getDeck().get(i));
            playerHand.receiveCards(deck.getDeck().get(51-i));
        }
    }

    public void engine() {
        while (!("exit").equals(input.nextLine()) && !dealerHand.toArrayList().isEmpty() && !playerHand.toArrayList().isEmpty()) {
            Card playerCard = playerHand.drawCard();
            Card dealerCard = dealerHand.drawCard();
            playerPlayedCards.add(playerCard);
            dealerPlayedCards.add(dealerCard);
            System.out.println("You played " + playerCard + " and the dealer played " + dealerCard);
            int winner = compareCards(playerCard, dealerCard);
            while(winner == 0){
                iDeclareWar();
                Card playerTop = playerPlayedCards.get(playerPlayedCards.size() - 1);
                Card dealerTop = dealerPlayedCards.get(dealerPlayedCards.size() - 1);
                winner = (playerTop.getRankInt() > dealerTop.getRankInt()) ? 1 : (playerTop.getRankInt() < dealerTop.getRankInt())?2:0;
                System.out.println("Your top card is: " + playerTop.toString() +" and the dealer's top card is: " + dealerTop.toString());
            }
            announceWinner(winner);
        }
    }


    public int compareCards(Card card1, Card card2) {
        int value;
        if (card1.getRankInt() == card2.getRankInt()) {
            value = 0;
        } else if (card1.getRankInt() > card2.getRankInt()) {
            value = 1;
        } else {
            value = 2;
        }
        return value;
    }

    public void iDeclareWar() {
        System.out.println("I   D E C L A R E   W A R!");
        int countDealer = checkNumberOfCards(dealerHand);
        int countPlayer = checkNumberOfCards(playerHand);
        int count = (countDealer<countPlayer)?countDealer:countPlayer;
        int x = (count<=3)?count-1:3;
        for(int i =0; i<x; i++){
            playerPlayedCards.add(playerHand.drawCard());
            dealerPlayedCards.add(dealerHand.drawCard());
        }
    }

    private void announceWinner(int winnerNumber) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(playerPlayedCards);
        cards.addAll(dealerPlayedCards);
        if (winnerNumber == 1) {
            playerHand.receiveCards(cards);
            System.out.println("You won this round!");
        } else {
            dealerHand.receiveCards(cards);
            System.out.println("You lost this round!");
        }
        System.out.println("You have " + checkNumberOfCards(playerHand) + " cards and the dealer has " +
                checkNumberOfCards(dealerHand) + " cards");
        playerPlayedCards.clear();
        dealerPlayedCards.clear();
    }

    public int checkNumberOfCards(Hand handToCheck) {
        return handToCheck.toArrayList().size();
    }

    public void end() {
        String winner = "And the winner is ";
        if (playerHand.toArrayList().size() > 25) {
            winner += "you!";
            scoreboard.addScore(player1,1);
            scoreboard.addScore(dealer,0);
        } else {
            winner += "the dealer!";
            scoreboard.addScore(player1,0);
            scoreboard.addScore(dealer,1);
        }
        System.out.println(scoreboard.displayRunningGameTally());
        System.out.println(winner + "\nIf you want to play again, enter 'yes', or enter anything else to return to the casino");
        playerHand.clearHand();
        dealerHand.clearHand();
        if (input.nextLine().equals("yes")) {
            start();
        }
    }

    public static void main(String[] args){
        Person person= new Person("Jack");
        War war = new War(person);
        war.start();
    }
}
