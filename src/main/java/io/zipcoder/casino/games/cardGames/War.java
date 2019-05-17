package io.zipcoder.casino.games.cardGames;

import io.zipcoder.casino.cards.*;
import io.zipcoder.casino.games.Game;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class War extends Game implements CardGameInterface {

    private Person dealer;
    private Person player;
    private Scanner input;

    public War(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.input  = new Scanner(System.in);
    }

    public Person getPlayer(){
        return this.player;
    }

    public ArrayList<Card> getPlayerHand(){
        return this.player.getHand();
    }
    public ArrayList<Card> getDealerHand(){
        return this.dealer.getHand();
    }

    public void start() {
        System.out.println("Welcome to WAR! Enter anything into the console to play a card\nEnter 'exit' at any time to end the game");
        dealCards();
        engine(getPlayerHand(), getDealerHand());
    }

    @Override
    public ArrayList<Card> dealCards() {
        Deck deck = new Deck();
        player.setHand(deck.dealHand(26));
        dealer.setHand(deck.getDeckOfCards());
        return null;
    }

    public void engine(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
        String exitStay = input.nextLine();
        System.out.println("Lets go to war!");
        while (!("exit").equalsIgnoreCase(exitStay)) {
            while (checkIfEitherAreEmpty(playerHand, dealerHand)) {
                String play = "";
                Card playerCard = playerHand.remove(0);
                Card dealerCard = dealerHand.remove(0);
                int pCardVal = playerCard.toInt();
                int dCardVal = dealerCard.toInt();
                System.out.println("You played " + playerCard + " and the dealer played " + dealerCard);
                if (pCardVal == dCardVal) {
                    warCardSwap(playerHand, dealerHand);
                    play = "\nYou now have " + checkCards(playerHand) + " cards.  Dealer has " + checkCards(dealerHand) + " cards.";
                } else if (pCardVal > dCardVal) {
                    addToHand(playerCard, dealerCard, playerHand);
                    play = "\nYou won the round!  You now have " + checkCards(playerHand) + " cards.  Dealer has "
                            + checkCards(dealerHand) + " cards.";
                } else {
                    addToHand(playerCard, dealerCard, dealerHand);
                    play = "\nDealer won the round.  You now have" + checkCards(playerHand) + " cards.  Dealer has "
                            + checkCards(dealerHand) + " cards.";
                }
                System.out.println(play);
            }
            declareWinner(playerHand, dealerHand);
        }
        end();
    }

    public void warCardSwap(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        int winCard = 0;
        do {
            if (!checkIfEitherAreEmpty(playerHand, dealerHand)) {
                break;
            }else{
                winCard = iDeclareWar(playerHand, dealerHand);
                ArrayList<Card> warWin = getWarTablePile(playerHand, dealerHand);
                if (winCard == 1) {
                    playerHand.addAll(warWin);
                } else if (winCard == 2) {
                    dealerHand.addAll(warWin);
                }
            }
        } while (winCard == 0);
    }

    public int iDeclareWar(ArrayList<Card> player1, ArrayList<Card> dealer1) {
        int win = 0;
        int x = warCardsNumber(player1, dealer1);
        int pCard = player1.get(x).toInt();
        int dCard = dealer1.get(x).toInt();
        System.out.println("I D E C L A R E  W A R!!\nPlayer's top card: " + player1.get(x-1) + ". Dealer's top card: " +
                dealer1.get(x-1));
        if( pCard > dCard ){
            win = 1;
        } else if(pCard < dCard){
            win = 2;
        }
        return win;
    }

    public ArrayList<Card> getWarTablePile(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        ArrayList<Card> warWin = new ArrayList<>();
        int x= warCardsNumber(playerHand,dealerHand);
        for(int i = 0; i<x; i++){
            warWin.add(playerHand.remove(0));
            warWin.add(dealerHand.remove(0));
        }
        return warWin;
    }

    public int warCardsNumber(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        int x = 0;
        if(playerHand.size()>=3 && dealerHand.size()>=3){
            x=3;
        } else {
            x = Math.min(playerHand.size(), dealerHand.size());
        }
        return x;
    }

    public void addToHand(Card playerCard, Card dealerCard, ArrayList<Card> hand){
        hand.add(playerCard);
        hand.add(dealerCard);
    }

    public boolean checkIfEitherAreEmpty(ArrayList<Card> playerHand, ArrayList<Card>dealerHand){
        if(playerHand.size() == 0 || dealerHand.size() == 0){
            return false;
        }
        return true;
    }


    public String declareWinner(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        String winner = "And the winner is ";
        winner += (playerHand.size() < dealerHand.size())? "the dealer!": player.getName();
        return winner;
    }

    public void end() {
        player.getHand().clear();
        dealer.getHand().clear();
        System.out.println("If you want to play again, enter 'yes', or enter anything else to return to the casino");
        if (input.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else{
            System.out.println("Thank you for playing War!");
        }
    }

    @Override
    public int checkNumberOfCards() {
        return 0;
    }

    public int checkCards(ArrayList<Card> cards){
        return cards.size();
    }

    @Override
    public String displayCards(ArrayList<Card> hand) {
        return null;
    }

}
