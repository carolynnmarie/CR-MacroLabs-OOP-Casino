package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.*;
import io.zipcoder.casino.People.*;
import io.zipcoder.casino.Scoreboard;

import java.util.*;

public class War extends CardGames {

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
        boolean endG;
        do{
            dealCards();
            engine();
            endG = endGame();
        } while(!endG);
        end();
    }

    public void dealCards() {
        Deck deck = new Deck();
        deck.shuffleDeck();
        dealerHand.receiveCards(deck.dealCards(26));
        playerHand.receiveCards(deck.dealCards(26));
    }

    public void engine() {
        while (!("exit").equalsIgnoreCase(input.nextLine()) && !dealerHand.toArrayList().isEmpty() && !playerHand.toArrayList().isEmpty()) {
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
            determineWinner(winner);
        }
    }


    public int compareCards(Card card1, Card card2) {
        return (card1.getRankInt() == card2.getRankInt())?0: (card1.getRankInt() > card2.getRankInt())?1:2;
    }

    public void iDeclareWar() {
        System.out.println("I   D E C L A R E   W A R!");
        int countDealer = checkHandSize(dealerHand);
        int countPlayer = checkHandSize(playerHand);
        int x = (countDealer>=3 && countPlayer>=3)?3:(countDealer<countPlayer)?countDealer:countPlayer;
        for(int i =0; i<x; i++){
            playerPlayedCards.add(playerHand.drawCard());
            dealerPlayedCards.add(dealerHand.drawCard());
        }
    }

    private void determineWinner(int winnerNumber) {
        StringBuilder builder = new StringBuilder();
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(playerPlayedCards);
        cards.addAll(dealerPlayedCards);
        if (winnerNumber == 1) {
            playerHand.receiveCards(cards);
            builder.append("You won this round!\n");
        } else {
            dealerHand.receiveCards(cards);
            builder.append("You lost this round!\n");
        }
        builder.append("You have " + checkHandSize(playerHand) + " cards. ")
                .append("The dealer has " + checkHandSize(dealerHand) + " cards.");
        System.out.println(builder);
        playerPlayedCards.clear();
        dealerPlayedCards.clear();
    }

    public int checkHandSize(Hand handToCheck) {
        return handToCheck.toArrayList().size();
    }

    public boolean endGame(){
        StringBuilder builder = new StringBuilder("And the winner is ");
        int pScore = 0;
        int dScore = 0;
        if (playerHand.toArrayList().size() > 25) {
            builder.append("you!\n");
            pScore = 1;
        } else {
            builder.append("the dealer!\n");
            dScore = 1;
        }
        scoreboard.addScore(player1,pScore);
        scoreboard.addScore(dealer,dScore);
        builder.append(scoreboard.displayRunningGameTally())
                .append("\nIf you want to play again, enter 'yes', or enter anything else to return to the casino");
        System.out.println(builder.toString());
        playerHand.clearHand();
        dealerHand.clearHand();
        if (input.nextLine().equals("yes")) {
            return false;
        }
        return true;
    }

    public void end() {
        System.out.println("Thank you for playing War!");
    }

    public static void main(String[] args){
        War war = new War(new Person("Jack"));
        war.start();
    }
}
