package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;

import io.zipcoder.casino.Scoreboard;
import java.util.*;

public class GoFish extends Game implements CardGameInterface {

    private Person player1;
    private Person dealer;
    private Deck houseDeck;
    private Scanner userInput;
    private int booksTotalPlayer;
    private int booksTotalDealer;
    private Hand playerHand;
    private Hand dealerHand;
    private Scoreboard scoreboard;

    public GoFish(Person player) {
        this.player1 = player;
        this.dealer = new Person("Dealer");
        this.houseDeck = new Deck();
        this.userInput = new Scanner(System.in);
        this.booksTotalPlayer = 0;
        this.booksTotalDealer = 0;
        this.playerHand = player1.getHand();
        this.dealerHand = dealer.getHand();
        Person[] people = {player1,dealer};
        this.scoreboard = new Scoreboard(people);
    }

    public void start() {
        System.out.println("*************************  Welcome to Go Fish!  *************************\n" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\n" +
                "*************************  Welcome to Go Fish!  *************************\n" +
                "Enter: 1 for Ace, 11 for Jack, 12 for Queen, & 13 for King\n");
        houseDeck.shuffleDeck();
        dealCards();
        engine();
        whoWonTheGame();
        end();
    }

    public void dealCards() {
        for (int i = 0; i < 7; i++){
            playerHand.toArrayList().add(houseDeck.drawCard());
            dealerHand.toArrayList().add(houseDeck.drawCard());
        }
    }

    public void engine(){
        do{
            userTurn();
            if(checkDeckAndHand()){
                break;
            }
            dealerTurn();
        } while (!checkDeckAndHand());
    }

    public void userTurn(){
        boolean hasCard;
        int userChoice = 0;
        do {
            do {
                System.out.println("\n*****************************\nPlayer's turn! Choose a card to request from the dealer\n" +
                        "Enter: 1 for Ace, 11 for Jack, 12 for Queen, & 13 for King");
                System.out.println("Your hand: " + "\u270B" + playerHand.toArrayList() + "\u270B");
                checkNumberOfCards(playerHand);
                while (!userInput.hasNextInt()) {
                    userInput.next();
                }
                userChoice = userInput.nextInt();
            } while (userChoice < 0 && userChoice >= 13);
            hasCard = doesDealerHaveCard(userChoice, dealerHand);
            if(!hasCard){
                hasCard = goFishPlayer(userChoice);
            }
            countPlayerBooks();
            if(checkDeckAndHand()) {
                break;
            }
        } while(hasCard);

    }

    public void dealerTurn() {
        boolean hasCard;
        do {
            Collections.shuffle(dealerHand.toArrayList());
            int randomDealerCard = dealerHand.toArrayList().get(0).getRankInt();
            System.out.println("******************************\nDealer's turn! Dealer has chosen card: " + randomDealerCard);
            hasCard = doesPlayerHaveCard(randomDealerCard, playerHand);
            if(!hasCard){
                hasCard = goFishDealer(randomDealerCard);
            }
            countDealerBooks();
            Collections.shuffle(dealerHand.toArrayList());
            if(checkDeckAndHand()) {
                break;
            }
        } while(hasCard);
    }

    public boolean doesDealerHaveCard(Integer userInputSave, Hand hand){
        String rank = (userInputSave==13)? "K":(userInputSave==1)?"A":(userInputSave==11)?"J":
                (userInputSave==12)?"Q": userInputSave.toString();
        boolean hasCard = false;
        int counter = countDuplicates(userInputSave,hand);
        if (counter > 0){
            System.out.println("Dealer has " + counter + ": " + rank + "'s!");
            giveCards(userInputSave, dealerHand, playerHand);
            hasCard = true;
        } else {
            System.out.println("Dealer does not have a :" + rank);
        }
        return hasCard;
    }

    public boolean doesPlayerHaveCard(Integer dealersChoice, Hand hand){
        String rank = (dealersChoice==13)? "K":(dealersChoice==1)?"A":(dealersChoice==11)?"J":
                (dealersChoice==12)?"Q": dealersChoice.toString();
        boolean fished = false;
        if (countDuplicates(dealersChoice,hand) > 0) {
            System.out.println("Dealer takes all your " + rank + "'s.");
            giveCards(dealersChoice,playerHand, dealerHand);
            fished = true;
        } else {
            System.out.println("You do not have any " + rank + "'s.");
        }
        Collections.shuffle(dealerHand.toArrayList());
        return fished;
    }

    private int countDuplicates(Integer cardRank, Hand hand){
        int counter = 0;
        for(int i = 0; i < checkNumberOfCards(hand); i++){
            if (cardRank == hand.toArrayList().get(i).getRankInt()){
                counter++;
            }
        }
        return counter;
    }

    public void giveCards(int cardRank, Hand giver, Hand recipient){
        for(int i = checkNumberOfCards(giver)-1; i >=0; i--){
            Card card = giver.toArrayList().get(i);
            if (cardRank == card.getRankInt()){
                recipient.receiveCard(card);
                giver.removeCard(card);
            }
        }
    }

    public boolean goFishPlayer(int desiredRank){
        StringBuilder builder = new StringBuilder("********************\n  Player Go Fish!\n********************\n");
        boolean fishedCard=false;
        if (houseDeck.getDeck().size() == 0) {
            System.out.println("Out of Cards!");
        } else {
            Card card = houseDeck.drawCard();
            playerHand.receiveCard(card);
            if (card.getRankInt() == desiredRank) {
                builder.append("You fished your wish!\n");
                fishedCard = true;
            } else {
                builder.append("You did not fish your wish\n");
            }
            builder.append("\u270B You pulled a ")
                    .append(card.toString())
                    .append(" \u270B");
            System.out.println(builder.toString());
        }
        return fishedCard;
    }

    public boolean goFishDealer(int desiredRank){
        boolean fishedCard = false;
        System.out.println("********************\n  Dealer Go Fish!\n********************");
        if (houseDeck.getDeck().size() == 0) {
            System.out.println("Out of Cards!");
        } else {
            Card card = houseDeck.drawCard();
            dealerHand.receiveCard(card);
            if(card.getRankInt() == desiredRank){
                System.out.println("Dealer fished his wish!");
                fishedCard=true;
            } else {
                System.out.println("Dealer did not fish his wish");
            }
        }
        return fishedCard;
    }

    private void countDealerBooks(){
        System.out.println("\uD83C\uDCA0 Total deck of cards count: " + houseDeck.getDeck().size());
        int books = getBooks(dealerHand);
        booksTotalDealer += books;
        if(books>0){
            System.out.println("!$!$!$! Dealer Scored "+ books + " Books!  Dealer's Book total: " + booksTotalDealer);
        }
    }

    private void countPlayerBooks(){
        int books = getBooks(playerHand);
        booksTotalPlayer += books;
        if(books>0){
            System.out.println("!$!$!$! You Scored "+ books + " Books!  Your Book total: " + booksTotalPlayer);
        }
    }

    public int getBooks(Hand hand){
        ArrayList<Card> hand1 = new ArrayList<>();
        int bookCount = 0;
        for(Card card: hand.toArrayList()){
            int count = countDuplicates(card.getRankInt(),hand);
            if(count<4){
                hand1.add(0,card);
            } else if(count == 4) {
                bookCount++;
            }
        }
        bookCount = bookCount/4;
        hand.clearHand();
        hand.receiveCards(hand1);
        return bookCount;
    }

    public int checkNumberOfCards(Hand hand) {
        return hand.toArrayList().size();
    }

    public boolean checkDeckAndHand(){
        if(houseDeck.getDeck().size()==0|| checkNumberOfCards(playerHand)==0 || checkNumberOfCards(dealerHand)==0){
            return true;
        }
        return false;
    }

    public void whoWonTheGame(){
        StringBuilder builder = new StringBuilder("*******************  ");
        if (booksTotalPlayer > booksTotalDealer){
            builder.append("You Won!  *******************\nYou won the game with a total Book Score of ")
                    .append(booksTotalPlayer)
                    .append("!\nDealer lost game with a total Book Score of ")
                    .append(booksTotalDealer);
            scoreboard.addScore(player1,1);
            scoreboard.addScore(dealer,0);
        } else if (booksTotalPlayer == booksTotalDealer) {
            builder.append("You Tied!  *******************\nYou both had a book score of ")
                    .append(booksTotalPlayer);
            scoreboard.addScore(player1,0);
            scoreboard.addScore(dealer,0);
        } else {
            builder.append("You Lost!  *******************\nDealer had a Book Score of ")
                    .append(booksTotalDealer).append("!\nYou had a Book Score of ")
                    .append(booksTotalPlayer);
            scoreboard.addScore(player1,0);
            scoreboard.addScore(dealer,1);
        }
        builder.append("!\n");
        System.out.println(builder.toString());
    }

    public void end() {
        Scanner scanner = new Scanner(System.in);
        booksTotalPlayer = 0;
        booksTotalDealer = 0;
        playerHand.clearHand();
        dealerHand.clearHand();
        System.out.println(scoreboard.displayRunningGameTally());
        System.out.println("Play another round? yes or no...");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    public static void main(String[] args) {
        Person Joe = new Person("Joe");
        GoFish game = new GoFish(Joe);
        game.start();
    }
}
