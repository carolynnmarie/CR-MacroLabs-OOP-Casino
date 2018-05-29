package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.money.GamblingInterface;
import io.zipcoder.casino.money.Wallet;
import io.zipcoder.casino.people.Dealer;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack extends Game implements CardGameInterface, GamblingInterface {

    private Person player;
    private ArrayList<Card> playerHand;
    private Wallet playerWallet;
    private int playerChips;

    private Dealer dealer;
    private ArrayList<Card> dealerHand;
    private Wallet dealerWallet;

    private Deck deck;

    // these 2 args are now in Person constructor for gambling games
    public Blackjack(Person player) {
        this.player = player;
        this.playerHand = player.getPlayerHand();
        this.playerWallet = player.getWallet();
        this.playerChips = player.getWallet().checkChips();
        this.dealer = new Dealer();
        this.dealerHand = dealer.getPlayerHand();
        this.dealerWallet = dealer.getDealerWallet();
        this.deck = new Deck();
    }
    public Blackjack(Person player, int playerChips){
        this.player = player;
        this.playerHand = player.getPlayerHand();
        this.playerWallet = player.getWallet();
        this.playerChips = playerChips;
        this.dealer = new Dealer();
        this.dealerHand = dealer.getPlayerHand();
        this.dealerWallet = dealer.getDealerWallet();
        this.deck = new Deck();

    }

    // can have another constructor that takes a number int of Persons, and places those Persons
    // into an array of players and one dealer.

    public Person getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getPlayerChips(){ return this.playerChips; }

    public ArrayList<Card> getPlayerHand(){ return this.playerHand;}

    public Wallet getPlayerWallet(){ return this.playerWallet; }

    public void setPlayerHand(ArrayList<Card> cards){
        this.playerHand = cards;
    }


    public int rankSum(Person person) {
        int handSum = 0;
        int aceRepeats = rankRepeats(person, 1);
        for(Card card: person.getPlayerHand()){
            int x = card.toInt();
            int value = (x == 11)? 10:(x == 12)? 10:(x == 13) ? 10: x;
            handSum +=value;
        }
        if(aceRepeats == 1){
            if(handSum <= 11){
                handSum +=10;
            }
        }
        return handSum;
    }

    public int rankRepeats(Person person, int value) {
        int rankReps = 0;
        for (Card card : person.getPlayerHand()) {
            if (card.toInt() == value) {
                rankReps++;
            }
        }
        return rankReps;
    }


//    public void personDecision(Person person) {
//        Scanner scanner = new Scanner(System.in);
//        String playerDecisionString = scanner.nextLine();
//
//        if (playerDecisionString.equals("hit")) {
//            this.hit(person);
//        }
//    }

    public String handToString(Person person) {
        ArrayList<Card> hand = person.getPlayerHand();
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString()).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

//    public ArrayList<Card> hit(Person person) {
//
//        Card cardFromDealer = this.getDeck().drawCard();
//        person.receiveCards(cardFromDealer);
//    }

    @Override
    public int checkNumberOfCards(Person person) {
        return 0;
    }

    @Override
    public ArrayList<Card> dealCards(Person person) {
        return null;
    }


    // GameInterface or Game Class


    public void start() {

        int personHandSum = 0;
        int dealerHandSum = 0;
        String playerDecisionString = "";

        if (getPlayerChips() <= 0) {
            System.out.println("You don't have anymore chips to play");
            return;
        }

        int betPlaced = starterBet();

        for(int i =0; i<4;i++){ hit(getPlayer());}

        System.out.println("+++ PLAY BLACKJACK +++");
        do {
            personHandSum = rankSum(getPlayer());
            System.out.println(displayCards(personHandSum));
            if (personHandSum == 21) {
                System.out.println("\n++++++++++++++++++++++++++++++++\nBLACKJACK!+++ You won " + betPlaced + " chip(s)\n" +
                        "++++++++++++++++++++++++++++++++");
                getPlayerWallet().addChips(2 * betPlaced);
            } else if (personHandSum > 21) {
                System.out.println("+++++++++++++++++++++\nBUST! You lost " + betPlaced + " chip(s)\n+++++++++++++++++++++");
            } else {
                System.out.println("Do you want to \"hit\" or \"stay\"?: ");
                Scanner scan = new Scanner(System.in);
                playerDecisionString = scan.nextLine();
                if (playerDecisionString.equals("hit")) {
                    hit(getPlayer());
                }
            }
        } while (personHandSum < 21);

        dealerHandSum = rankSum(getDealer());
        personHandSum = rankSum(getPlayer());

        if (personHandSum <= 21 && dealerHandSum <= 16) this.hit(dealer);


        if (playerDecisionString.equals("stay")) {
            whoWins(personHandSum,dealerHandSum);
        }
        finalScore(personHandSum, dealerHandSum);
        doYouWantToContinuePlaying();
    }

    private int starterBet(){
        int betPlaced = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("How many chips do you want to bet?");
            Integer input = scanner.nextInt(); // changed to nextLine to fix next loop
            try {
                betPlaced = input;
                if (betPlaced <= getPlayerChips()) {
                    placeBet(getPlayer(), betPlaced);
                } else {
                    System.out.println("Insufficient funds. You have " + getPlayerChips() +
                            " chip(s). Please enter number of chips you wish to bet.");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Not an integer. Enter number of chips you wish to bet.");
            }
        } while (betPlaced == 0);
        return betPlaced;
    }

    private String displayCards(int personHandSum){
        String display = "\n" + getPlayer().getName() + ": \u270B\n"+ handToString(getPlayer()) +
                "\n\u270B, hand = " + personHandSum;
        String dealerCards = getDealer().getPlayerHand().get(1).toString();
        return (display + "Dealer: " + dealerCards);
    }

    private Deck checkDeckSize(Deck deck){
        if (deck.getDeckOfCards().size() <= 16) {
            deck = new Deck();
            deck.shuffleDeck();
        }
        return deck;
    }

    private void whoWins(int personHandSum, int dealerHandSum) {
        int betPlaced = starterBet();
        if ((dealerHandSum <= 21) && (dealerHandSum > personHandSum)) {
            System.out.println("++++++++++++++++++++++++\nDealer wins!\nYou lost " + betPlaced + " chip(s)\n++++++++++++++++++++++++");
        } else if ((dealerHandSum <= 21) && (dealerHandSum == personHandSum)) {
            System.out.println("++++++++++++++++++++++++\n It's a tie!\n You keep your " + betPlaced + " chip(s)\n" +
                    "++++++++++++++++++++++++");
            getPlayerWallet().addChips(betPlaced);
        } else {
            System.out.println("+++++++++++++++++++++++++\n" + getPlayer().getName() + " wins!\nYou won " + betPlaced + " chip(s)" +
                    "+++++++++++++++++++++++++");
            getPlayerWallet().addChips(2 * betPlaced);
        }
    }

    private void finalScore(int personHandSum, int dealerHandSum){
        System.out.println("FINAL SCORE:\n" + getPlayer().getName() + ": \u270B\n" + handToString(getPlayer()) +
                "\u270B, hand = " + personHandSum);
        System.out.println("\nDealer: " + "\u270B" + handToString(getDealer()) +"\n\u270B, hand = " + dealerHandSum + "\n\n" +
                getPlayer().getName() + " has " + getPlayerChips() + " chip(s)");
    }


    private void doYouWantToContinuePlaying() {
        Scanner scanner = new Scanner(System.in);
        String test = "";
        System.out.println("Keep playing? (yes/no) ");
        test = scanner.nextLine();
        if (test.equals("yes")) {
            getPlayer().clearHand();
            getDealer().clearHand();
            start();
        }
    }

    public void placeBet(Person player, int betAmount) {
        player.getWallet().removeChips(betAmount);
    }

    public void end() {
        System.out.println("Thank you for playing");
    }

    public void dealCards() { }
    public int checkChipAmount(Person personToCheck) { return 0; }
    public int getAnte() { return 0; }
    public void bootPlayerFromGame(Person personToBoot) { }
    public int checkPot() { return 0; }

    public static void main (String[] args) {
        Person player = new Person("Luis");
        Blackjack blackjack = new Blackjack(player, 10);
        blackjack.start();
        blackjack.end();
    }

}
