package io.zipcoder.casino.games.cardGames;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.games.Game;
import io.zipcoder.casino.games.cardGames.CardGameInterface;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class War extends Game implements CardGameInterface {

    private Person dealer;
    private Person player;
    private Deck deck;
    private Scanner input = new Scanner(System.in);

    public War(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.deck = new Deck();
        this.player.setHand(deck.dealHand(26));
        dealer.setHand(deck.getDeckOfCards());
    }


    public Person getPlayer(){
        return this.player;
    }
    public Person getDealer(){
        return this.dealer;
    }

    public ArrayList<Card> getPlayerHand(){  return this.player.getHand(); }
    public ArrayList<Card> getDealerHand(){
        return this.dealer.getHand();
    }

    public void start() {
        System.out.println("Welcome to WAR! Enter anything into the console to play a card");
        System.out.println("Enter 'exit' at any time to end the game");
        engine(getPlayerHand(), getDealerHand());
    }

    public void engine(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
        String exitStay = input.nextLine();
        System.out.println("Lets go to war!");
        while (!("exit").equals(exitStay)) {
            while (checkIfEitherAreEmpty(playerHand, dealerHand)) {
                Card playerCard = playerHand.remove(0);
                Card dealerCard = dealerHand.remove(0);
                System.out.println("You played " + playerCard + " and the dealer played " + dealerCard);
                if (playerCard.toInt() == dealerCard.toInt()) {
                    warCardSwap(playerHand, dealerHand);
                    System.out.println("You now have " + playerHand.size() + " cards.  Dealer has " + dealerHand.size() + " cards.");
                } else if (playerCard.toInt() > dealerCard.toInt()) {
                    addToHand(playerCard, dealerCard, playerHand);
                    System.out.println("You won the round!  You now have " + playerHand.size() + " cards.  Dealer has "
                            + dealerHand.size() + " cards.");
                } else {
                    addToHand(playerCard, dealerCard, dealerHand);
                    System.out.println("Dealer won the round.  You now have" + playerHand.size() + " cards.  Dealer has "
                            +dealerHand.size() + " cards.");
                }
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
        System.out.println("I D E C L A R E  W A R!!\nPlayer's top card: " + player1.get(x-1) + ". Dealer's top card: " +
                dealer1.get(x-1));
        if(player1.get(x).toInt() > dealer1.get(x).toInt()){
            win = 1;
        } else if(player1.get(x).toInt() < dealer1.get(x).toInt()){
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
        System.out.println("If you want to play again, enter 'yes', or enter anything else to return to the casino");;
        if (input.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else{
            System.out.println("Thank you for playing War!");
        }
    }

    public int checkNumberOfCards(Person handToCheck) {
        return handToCheck.getHand().size();
    }

    @Override
    public int checkNumberOfCards() {
        return 0;
    }

    @Override
    public ArrayList<Card> dealCards() {
        return null;
    }

    @Override
    public String displayCards(ArrayList<Card> hand) {
        return null;
    }

}