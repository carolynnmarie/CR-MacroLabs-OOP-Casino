package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GoFish extends Game implements CardGameInterface {

    private Person player;
    private Person dealer;
    private Deck houseDeck;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;


    public GoFish(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.houseDeck = new Deck();
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
    }

    public GoFish() {
    }

    public ArrayList<Card> getPlayerHand(){
        return this.playerHand;
    }
    public ArrayList<Card> getDealerHand(){
        return this.dealerHand;
    }


    public void start() {
        playerHand = houseDeck.dealHand(7);
        dealerHand = houseDeck.dealHand(7);
        System.out.println("*************************  Welcome to Go Fish!  *************************");
        System.out.println("\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B");
        System.out.println("*************************  Welcome to Go Fish!  *************************\n\n" +
                "When choosing card enter 1 for Ace, 11 for Jack, 12 for Queen, 13 for King\n");
        userTurn(playerHand, dealerHand);
    }


    public int checkNumberOfCards(ArrayList<Card> hand){
        return hand.size();
    }

    private ArrayList<Card> currentHand(ArrayList<Card> hand ){
        return hand;
    }

    private String displayCards(Person person){ return person.displayHand(); }

    public void userTurn(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        int fish = 0;
        do {
            int userChoice = playerInput();
            if (doYouHaveCard(userChoice, dealerHand)) {
                swapCards(userChoice, dealerHand, playerHand);
            } else {
                System.out.println("Dealer doesn't have any " + userChoice + ", Go Fish!");
                fish = goFishTurn(userChoice, playerHand);
            }
        } while (fish == 1);
        checkPlayerAfterPlayerTurn(playerHand, dealerHand);
    }

    private int playerInput(){
        Scanner input = new Scanner(System.in);
        int userChoice = 0;
        do{
            System.out.println("****Player's Turn****\n Your current hand: " +
                    displayCards(player) + "\nChoose a card to request from dealer\n");
            while(!input.hasNextInt()) input.next();
            userChoice = input.nextInt();
        } while (userChoice <=0 || userChoice>13);
        return userChoice;
    }

    public void dealerTurn(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        int fish = 0;
        System.out.println("****Dealer's turn****");
        Deck deck = new Deck();
        int cardValue = 0;
        do{
            cardValue = deck.drawCard().toInt();
            System.out.println("\nDo you have any " + cardValue + "s ?\n");
            if (doYouHaveCard(cardValue, playerHand)) {
                swapCards(cardValue, playerHand, dealerHand);
            } else {
                fish = goFishTurn(cardValue,dealerHand);
                doYouHaveCard(cardValue, dealerHand);
                bookCountCheck(cardValue,dealerHand);
            }
        } while(fish == 1);
        checkDealerAfterDealerTurn(playerHand, dealerHand);
    }

    private boolean doYouHaveCard(int randomCard, ArrayList<Card> hand) {
        boolean yOrN = false;
        for (int i = 0; i < checkNumberOfCards(hand); i++) {
            if (randomCard == currentHand(hand).get(i).toInt()) {
                yOrN = true;
                break;
            }
        }
        return yOrN;
    }

    private void swapCards(int userInput, ArrayList<Card> giver, ArrayList<Card> receiver){
        for(int i = 0; i< checkNumberOfCards(giver); i++){
            if(userInput == currentHand(giver).get(i).toInt()){
                currentHand(receiver).add(currentHand(giver).get(i));
                currentHand(giver).remove(i);
            }
        }
        bookCountCheck(userInput, receiver);
        cardCountCheck(giver);
    }

    private int goFishTurn(int desiredCard, ArrayList<Card> pullsCard){
        int wish =0;
        cardCountCheck(pullsCard);
        System.out.println("******************* Go Fish! ********************");
        Card card = houseDeck.drawCard();
        currentHand(pullsCard).add(card);
        System.out.println("Card fished: " + card.toString());
        if(card.toInt()== desiredCard){
            wish = 1;
            System.out.println("You fished your wish! You get to take another turn");
        }
        bookCountCheck(card.toInt(), pullsCard);
        cardCountCheck(pullsCard);
        return wish;
    }



    private void bookCountCheck(int userInputSave, ArrayList<Card> playerHand){
        int num = 0;
        for(int i = 0; i < checkNumberOfCards(playerHand); i++) {
            if (userInputSave == currentHand(playerHand).get(i).toInt()) {
                num++;
            }
        }
        if (num == 4) {
            for (int j = checkNumberOfCards(playerHand) - 1; j >= 0 ; j--) {
                if (userInputSave == currentHand(playerHand).get(j).toInt()){
                    currentHand(playerHand).remove(currentHand(playerHand).get(j));
                }
            }
            player.setBook(player.getBook()+1);
            System.out.println("\n!$!$!$!$!$!$! You Scored a Book! (Four of a kind) !$!$!$!$!$!$!\n" +
                    "!!!!Your Book Total: " + player.getBook() + "\n");
        }
    }

    public void cardCountCheck(ArrayList<Card> person){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(person) == 0){
            System.out.println("Out of cards!");
            whoWonTheGame();
        }
    }

    public void checkPlayerAfterPlayerTurn(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(playerHand) == 0){
            whoWonTheGame();
        } else {
            dealerTurn(playerHand, dealerHand);
        }
    }

    public void checkDealerAfterDealerTurn(ArrayList<Card> playerHand, ArrayList<Card> dealerHand){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(dealerHand) == 0){
            whoWonTheGame();
        } else {
            userTurn(playerHand, dealerHand);
        }
    }

    public void whoWonTheGame(){
        if (player.getBook() > dealer.getBook()){
            System.out.println("*************************  You Won!  *************************" +
                    "You won the game with a total Book Score of " + player.getBook() + "!\n" +
                    "Dealer lost game with a total Book Score of " + dealer.getBook() + "!\n" +
                    "*************************  You Won!  *************************\n");
        } else if (player.getBook() == dealer.getBook()) {
            System.out.println("*************************  You Tied!  *************************"+
                    "Both Players Tied with a total Book Score of " + player.getBook()+ "!\n"+
                    "*************************  You Tied!  *************************\n");
        } else {
            System.out.println("*************************  You Lost!  *************************"+
                    "           You Lost! Dealer had a Book Score of " + dealer.getBook() + "!\n" +
                    "                 You had a Book Score of " + player.getBook() + "!\n"+
                    "*************************  You Lost!  *************************\n");
        }
        end();
    }

    public void end() {
        player.setBook(0);
        dealer.setBook(0);
        playerHand.clear();
        dealerHand.clear();
        Scanner anotherRoundScanner = new Scanner(System.in);
        System.out.println("Play another round? yes or no...");
        if (anotherRoundScanner.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    public static void main(String[] args) {
        GoFish game = new GoFish();
        game.start();
    }


    public int checkNumberOfCards(Person person) {
        return 0;
    }


    public ArrayList<Card> dealCards(Person person) {
        return null;
    }
}
