package io.zipcoder.casino.games.cardGames;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.games.Game;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;
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

    public void setPlayerHand(int numberOfCards) {
        this.player.setHand(houseDeck.dealHand(numberOfCards));
    }
    public ArrayList<Card> getPlayerHand(){
        return this.player.getHand();
    }

    public void setDealerHand(int numberOfCards) {
        this.dealer.setHand(houseDeck.dealHand(numberOfCards));
    }
    public ArrayList<Card> getDealerHand(){
        return this.dealer.getHand();
    }

    public Person getPlayer() {
        return player;
    }
    public Person getDealer() {
        return dealer;
    }

    public void start() {
        setDealerHand(7);
        setPlayerHand(7);
        System.out.println("*************************  Welcome to Go Fish!  *************************");
        System.out.println("\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B");
        System.out.println("*************************  Welcome to Go Fish!  *************************\n\n" +
                "When choosing card enter 1 for Ace, 11 for Jack, 12 for Queen, 13 for King\n");
        engine();
    }

    public void engine(){
        do{
            userTurn();
            cardCountCheck(player.getHand(),dealer.getHand());
            dealerTurn();
        } while(cardCountCheck(player.getHand(),dealer.getHand()));
    }

    public void userTurn(){
        Scanner input = new Scanner(System.in);
        int fish = 0;
        int userChoice = 0;
        do {
            do{
                System.out.println("****Player's Turn****\n Your current hand: " +
                        displayCards(getPlayerHand()) + "\nChoose a card value to request from dealer\n");
                while(!input.hasNextInt()){
                    input.next();
                }
                userChoice = input.nextInt();
            } while (userChoice <=0 || userChoice>13);
            fish = turn(getPlayer(), getDealer(), userChoice);
        } while (fish == 1);
    }

    public void dealerTurn(){
        int fish = 0;
        do{
            System.out.println("****Dealer's turn****");
            int random = (int)Math.round(Math.random()*(getDealerHand().size()-1));
            int cardValue = getDealerHand().get(random).toInt();
            System.out.println("\nDo you have any " + cardValue + "'s?\n");
            fish = turn(getDealer(),getPlayer(),cardValue);

        } while(fish == 1);
    }

    public int turn(Person personOne, Person personTwo, int cardValue){
        int fish = 0;
        if (doYouHaveCard(cardValue, personOne.getHand())) {
            System.out.println(personOne.getName() + " has a " + cardValue + "." + personTwo.getName() +
                    " gets the card and takes another turn. ");
            swapCards(cardValue, personOne.getHand(), personTwo.getHand());
            fish = 1;
        } else {
            System.out.println(personOne.getName() + " doesn't have any " + cardValue + "'s. " + personTwo.getName() +  " has to go fish!");
            fish = goFishTurn(cardValue,personTwo.getHand());
        }
        cardCountCheck(personOne.getHand(), personTwo.getHand());
        return fish;
    }

    public boolean doYouHaveCard(int randomCard, ArrayList<Card> hand) {
        boolean yOrN = false;
        for (int i = 0; i < checkNumberOfCards(hand); i++) {
            if (randomCard == hand.get(i).toInt()) {
                yOrN = true;
                break;
            }
        }
        return yOrN;
    }

    public void swapCards(int userInput, ArrayList<Card> giver, ArrayList<Card> receiver){
        for(int i = 0; i< checkNumberOfCards(giver); i++){
            if(userInput == giver.get(i).toInt()){
                receiver.add(giver.get(i));
                giver.remove(i);
            }
        }
        bookCountCheck(userInput, receiver);
        cardCountCheck(giver);
    }

    private int goFishTurn(int desiredCard, ArrayList<Card> cards){
        int wish = 0;
            cardCountCheck(cards);
            System.out.println("******************* Go Fish! ********************");
            getHouseDeck().shuffleDeck();
            Card card = getHouseDeck().drawCard();
            cards.add(card);
            System.out.println("Card fished: " + card.toString());
            if (card.toInt() == desiredCard) {
                wish = 1;
                System.out.println("You fished your wish! You get to take another turn");
            }
            bookCountCheck(card.toInt(), cards);
            cardCountCheck(cards);
        return wish;
    }


    private void bookCountCheck(int userInputSave, ArrayList<Card> playerHand){
        int num = 0;
        for(int i = 0; i < checkNumberOfCards(playerHand); i++) {
            if (userInputSave == playerHand.get(i).toInt()) { num++; }
        }
        if (num == 4) {
            for (int j = checkNumberOfCards(playerHand) - 1; j >= 0 ; j--) {
                if (userInputSave == playerHand.get(j).toInt()){
                    playerHand.remove(playerHand.get(j));
                }
            }
            player.setBook(player.getBook()+1);
            System.out.println("\nYou Scored a Book! (Four of a kind)\nYour Book Total: " + player.getBook());
        }
    }

    public void cardCountCheck(ArrayList<Card> person){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(person) == 0){
            System.out.println("Out of cards!");
            whoWonTheGame();
        }
    }

    public boolean cardCountCheck(ArrayList<Card> person, ArrayList<Card> dealer){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(person) == 0 || checkNumberOfCards(dealer)==0){
            System.out.println("Out of cards!");
            whoWonTheGame();
            return false;
        } else{
            return true;
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

    public Deck getHouseDeck() {
        return houseDeck;
    }


    public static void main(String[] args) {
        Person person = new Person("Luis");
        GoFish game = new GoFish(person);
        game.start();
    }

    public int checkNumberOfCards(ArrayList<Card> hand){
        return hand.size();
    }

    @Override
    public int checkNumberOfCards() {
        return 0;
    }

    @Override
    public ArrayList<Card> dealCards() {
        return null;
    }

    public String displayCards(ArrayList<Card> hand){
        String stringHand = "";
        for(Card card: hand){
            stringHand += card.toString() + " ";
        }
        return stringHand; }


}
