package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import io.zipcoder.casino.People.Dealer;
import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;

import java.util.*;

public class War extends Game implements GameInterface, CardGameInterface {

    private boolean gameIsRunning;
    private Dealer dealer;
    private Person player;
    private ArrayList<Card> playerPlayedCards;
    private ArrayList<Card> dealerPlayedCards;
    private Scanner input;
    private Hand dealerHand;
    private Hand playerHand;

    public War(Person player) {
        this.player = player;
        this.dealer = new Dealer();
        this.dealerHand=dealer.getHand();
        this.playerHand=player.getHand();
        this.gameIsRunning=true;
        this.playerPlayedCards = new ArrayList<>();
        this.dealerPlayedCards = new ArrayList<>();
        this.input = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to WAR! Enter anything into the console to play a card");
        System.out.println("Enter 'exit' at any time to end the game");
        Deck deck = new Deck();
        deck.shuffleDeck();
        for (int i = 0; i < 26; i++) {
            dealerHand.receiveCards(deck.getDeck().get(i));
            playerHand.receiveCards(deck.getDeck().get(51-i));
        }
        //dealer.getHand().shuffleHand();
        //dealCards();
        engine();
    }

    public void engine() {
        // String playerInput = input.nextLine();
        if (!("exit").equals(input.nextLine())) {
            while (gameIsRunning) {
                while (!emptyHand(dealerHand) && !emptyHand(playerHand)) {
                    playerPlayedCards.add(playerHand.drawCard());
                    dealerPlayedCards.add(dealerHand.drawCard());
                    System.out.println("You played " + playerPlayedCards.get(playerPlayedCards.size()-1) + " and the dealer played " +
                            dealerPlayedCards.get(dealerPlayedCards.size()-1));
                    int winner = compareCards(playerPlayedCards.get(playerPlayedCards.size()-1), dealerPlayedCards.get(dealerPlayedCards.size()-1));
                    announceWinner(winner);
                    if (("exit").equals(input.nextLine()) || emptyHand(playerHand) || emptyHand(dealerHand)) {
                        end();
                    }
                }
            }
        } else {
            end();
        }
    }


    private boolean emptyHand(Hand hand) {
        return hand.toArrayList().size() == 0;
    }

    private void announceWinner(int winnerNumber) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(playerPlayedCards);
        cards.addAll(dealerPlayedCards);
        if (winnerNumber == 1) {
            playerHand.receiveCards(cards);
            System.out.println("You won this round!");
            whoWon();
        } else if (winnerNumber == 2) {
            dealerHand.receiveCards(cards);
            System.out.println("You lost this round!");
            whoWon();
        }
        playerPlayedCards.clear();
        dealerPlayedCards.clear();

    }

    public int compareCards(Card card1, Card card2) {
        int value = 0;
        if (card1.getRankInt() == card2.getRankInt()) {
            do {
                iDeclareWar();
                Card playerTop = playerPlayedCards.get(playerPlayedCards.size() - 1);
                Card dealerTop = dealerPlayedCards.get(dealerPlayedCards.size() - 1);
                value = (playerTop.getRankInt() > dealerTop.getRankInt()) ? 1 : (playerTop.getRankInt() < dealerTop.getRankInt())?2:0;
            } while (value==0);
        } else if (card1.getRankInt() > card2.getRankInt()) {
            value = 1;
        } else {
            value = 2;
        }
        return value;
    }

    public void whoWon() {
        System.out.println("You have " + playerHand.toArrayList().size() + " cards and the dealer has " +
                dealerHand.toArrayList().size() + " cards");
    }

    // Make private after testing / Make public for testing
    public void iDeclareWar() {
        System.out.println("I   D E C L A R E   W A R!");
        int countDealer = dealerHand.toArrayList().size();
        int countPlayer = playerHand.toArrayList().size();
        int count = (countDealer<countPlayer)?countDealer:countPlayer;
        int x = (count<=4)?count-1:4;
        for(int i =0; i<x; i++){
            playerPlayedCards.add(playerHand.drawCard());
            dealerPlayedCards.add(dealerHand.drawCard());
        }
    }

    public void dealCards() {
        for (int i = dealerHand.toArrayList().size()-1; i >= 26; i--) {
            playerHand.toArrayList().add(dealerHand.toArrayList().remove(i));
        }
    }

    public void end() {
        String winner = "";
        if (playerHand.toArrayList().size() > 25) {
            winner += "you!";
        } else {
            winner += "the dealer!";
        }
        System.out.println("And the winner is " + winner);
        playerHand.clearHand();
        dealerHand.clearHand();
        System.out.println("If you want to play again, enter 'yes', or enter anything else to return to the casino");
        if (input.nextLine().equals("yes")) {
            start();
        }
        gameIsRunning = false;
    }

    public int checkNumberOfCards(Hand handToCheck) {
        return handToCheck.toArrayList().size();
    }


    public static void main(String[] args){
        Person person= new Person("Jack");
        War war = new War(person);
        war.start();
    }
}
