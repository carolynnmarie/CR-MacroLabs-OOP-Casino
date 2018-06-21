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
    private Person dealer;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private Wallet playerWallet;
    private int playerChips;
    private int playerBet;


    private Deck deck;

    public Blackjack(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.playerWallet = player.getWallet();
        this.playerChips = player.getWallet().checkChips();
        this.deck = new Deck();
        this.playerBet = 0;
    }

    public Blackjack(Person player, int playerChips){
        this.player = player;
        this.dealer = new Person("Dealer");
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.playerWallet = player.getWallet();
        this.playerChips = playerChips;
        this.deck = new Deck();
        this.playerBet = 0;
    }

    public Person getPlayer() {
        return player;
    }

    public void setDeck(Deck deck){
        this.deck = deck;
    }
    public Deck getDeck() {
        return deck;
    }

    public int getPlayerChips(){ return this.playerChips; }

    public ArrayList<Card> getPlayerHand(){ return this.playerHand;}
    public void setPlayerHand(ArrayList<Card> cards){ this.playerHand = cards; }

    public Wallet getPlayerWallet(){ return this.playerWallet; }

    public void setPlayerBet(int playerBet){
        this.playerBet = playerBet;
    }
    public int getPlayerBet(){
        return this.playerBet;
    }

    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(ArrayList<Card> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public void start() {
        int personHandSum = 0;
        int dealerHandSum = 0;

        if (getPlayerChips() <= 0) {
            System.out.println("You don't have anymore chips to play");
            return;
        }
        setPlayerBet(starterBet());
        setPlayerHand(deck.dealHand(2));
        setDealerHand(deck.dealHand(2));
        dealerHandSum = rankSum(getDealerHand());

        System.out.println("+++ PLAY BLACKJACK +++");
        do {
            personHandSum = engine();
        } while (personHandSum < 21);

        if (personHandSum <= 21 && dealerHandSum <= 16) {
            this.hit(dealerHand);}

        System.out.println(whoWins(personHandSum, dealerHandSum));
        doYouWantToContinuePlaying();
    }

    private int engine(){
        String playerDecisionString = "";
        int personHandSum = rankSum(getPlayerHand());
        System.out.println(showHands());
        if (personHandSum == 21) {
            System.out.println("\n++++++++++++++++++++++++++++++++\nBLACKJACK!+++ You won " + getPlayerBet() + " chip(s)\n" +
                    "++++++++++++++++++++++++++++++++");
            getPlayerWallet().addChips(2 * getPlayerBet());
        } else if (personHandSum > 21) {
            System.out.println("+++++++++++++++++++++\nBUST! You lost " + getPlayerBet() + " chip(s)\n+++++++++++++++++++++");
        } else {
            System.out.println("Do you want to \"hit\" or \"stay\"?: ");
            Scanner scan = new Scanner(System.in);
            playerDecisionString = scan.nextLine();
            if (playerDecisionString.equals("hit")) {
                hit(playerHand);
            }
        }
        return personHandSum;
    }

    public int rankSum(ArrayList<Card> hand) {
        int handSum = 0;
        int aceRepeats = rankRepeats(hand, 1);
        for(Card card: hand){
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

    private int rankRepeats(ArrayList<Card> hand, int value) {
        int rankReps = 0;
        for (Card card : hand) {
            if (card.toInt() == value) { rankReps++; }
        }
        return rankReps;
    }

    @Override
    public String displayCards(ArrayList<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString()).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public ArrayList<Card> hit(ArrayList<Card> hand) {
        Card cardFromDealer = getDeck().drawCard();
        hand.add(cardFromDealer);
        return hand;
    }

    private int starterBet(){
        int betPlaced = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("How many chips do you want to bet?");
            Integer input = scanner.nextInt();
            try {
                betPlaced = input;
                if (betPlaced <= getPlayerChips()) {
                    placeBet(getPlayer(), betPlaced);
                } else {
                    System.out.println("Insufficient funds. You have " + getPlayerChips() + " chip(s). Please enter number of chips you wish to bet.");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Not an integer. Please enter number of chips you wish to bet.");
            }
        } while (betPlaced == 0);
        return betPlaced;
    }

    public String showHands(){
        String display = "\n" + getPlayer().getName() + ": \u270B\n"+ displayCards(getPlayerHand()) +
                "\n\u270B, hand = " + rankSum(getPlayerHand());
        String dealerCards = getDealerHand().get(1).toString();
        return (display + "Dealer: " + dealerCards);
    }

    private String whoWins(int personHandSum, int dealerHandSum) {
        String winner = "FINAL SCORE:\n" + getPlayer().getName() + ": \u270B\n" + displayCards(getPlayerHand()) + "\u270B, hand = "
                + personHandSum + "\nDealer: " + "\u270B" + displayCards(getDealerHand()) +"\n\u270B, hand = " + dealerHandSum + "\n\n";
        if ((dealerHandSum <= 21) && (dealerHandSum > personHandSum)) {
           winner += "Dealer wins!\nYou lost " + getPlayerBet() + " chip(s)";
        } else if ((dealerHandSum <= 21) && (dealerHandSum == personHandSum)) {
            winner += "It's a tie!\n You keep your " + getPlayerBet() + " chip(s)\n";
            getPlayerWallet().addChips(getPlayerBet());
        } else {
            winner += getPlayer().getName() + " wins!\nYou won " + getPlayerBet() + " chip(s)";
            getPlayerWallet().addChips(2 * getPlayerBet());
        }
        return winner;
    }

    private void doYouWantToContinuePlaying() {
        getDealerHand().clear();
        getDealerHand().clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Keep playing? (yes/no) ");
        String test = scanner.nextLine();
        if (test.equals("yes")) {
            start();
        } else if (test.equals("no")) {
            end();
        }
    }

    public void placeBet(Person player, int betAmount) {
        player.getWallet().removeChips(betAmount);
    }

    public void end() {
        System.out.println("Thank you for playing");
    }

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
    @Override
    public int checkNumberOfCards(Person person) {
        return 0;
    }

    @Override
    public ArrayList<Card> dealCards(Person person) {
        return null;
    }


}
