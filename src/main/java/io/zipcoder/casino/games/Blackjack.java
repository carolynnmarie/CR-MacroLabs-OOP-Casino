package io.zipcoder.casino.games;

import io.zipcoder.casino.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack extends Game implements GameInterface, CardGameInterface, GamblingInterface {

    private Person player;
    private Hand playerHand;
    private Wallet playerWallet;
    private int playerChips;

    private Dealer dealer;
    private Hand dealerHand;
    private Wallet dealerWallet;

    private Deck deck;

    // these 2 args are now in Person constructor for gambling games
    public Blackjack(Person player) {
        this.player = player;
        this.playerHand = player.getHand();
        this.playerWallet = player.getWallet();
        this.playerChips = playerWallet.checkChips();
        this.dealer = new Dealer();
        this.dealerHand = dealer.getHand();
        this.dealerWallet = dealer.getDealerWallet();
        this.deck = new Deck();
    }
    public Blackjack(Person player, int playerChips){
        this.player = player;
        this.playerHand = player.getHand();
        this.playerWallet = player.getWallet();
        this.playerChips = playerChips;
        this.dealer = new Dealer();
        this.dealerHand = dealer.getHand();
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


    public int rankSum(Person person) {
        int handSum = 0;
        Card aceRank = new Card(Rank.ACE, Suit.CLUBS);
        int aceRepeats = rankRepeats(person, aceRank);

        // If there are 2 or more Aces in handArrayList, treat all Aces as = 1
        if (aceRepeats != 1) {
            for (Card card : person.getPlayerHand()) {
                if (card.toInt() == 11 || card.toInt() == 12
                        || card.getRank().toInt() == 13) {
                    handSum += 10;
                } else {
                    handSum += card.getRank().toInt();
                }
            }
        } else { // implement Ace = 1 or Ace = 11 rankSum/difference/choice
            int sumAce1 = 0;
            int sumAceEleven = 0;
            for (Card card : person.getPlayerHand()) {
                if (card.toInt() == 11 || card.toInt() == 12
                        || card.getRank().toInt() == 13) {
                    sumAce1 += 10;
                    sumAceEleven += 10;
                } else if (card.toInt() == 1) {
                    sumAce1 += 1;
                    sumAceEleven += 11;
                } else {
                    sumAce1 += card.getRank().toInt();
                    sumAceEleven += card.getRank().toInt();
                }
            }
            if ( (sumAce1 <= 21) && (sumAceEleven <= 21) ) {
                handSum = greaterofTwo(sumAce1, sumAceEleven);
            } else {
                handSum = smallerOfTwo(sumAce1, sumAceEleven);
            }
        }

        return handSum;
    }

    public int rankRepeats(Person person, Card cardRankToCount) {
        int rankReps = 0;
        for (Card card : person.getPlayerHand()) {
            if (card.toInt() == cardRankToCount.toInt()) {
                rankReps++;
            }
        }
        return rankReps;
    }

    public int smallerOfTwo(int number1, int number2) {
        if (number1 <= number2) {
            return number1;
        } else {
            return number2;
        }
    }

    public int greaterofTwo(int number1, int number2) {
        if (number1 >= number2) {
            return number1;
        } else {
            return number2;
        }
    }

    public void personDecision(Person person) {
        Scanner scanner = new Scanner(System.in);
        String playerDecisionString = scanner.nextLine();

        if (playerDecisionString.equals("hit")) {
            this.hit(person);
        }
    }

    public String handToString(ArrayList<Card> handArrayList) {
        StringBuilder sb = new StringBuilder();
        for (Card card : handArrayList) {
            sb.append(card.toString() + " ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public void hit(Person person) {
        Card cardFromDealer = this.getDeck().drawCard();
        person.receiveCards(cardFromDealer);
    }

    // CardGameInterface
    public int checkNumberOfCards() {
        return 0;
    }

    @Override
    public int checkNumberOfCards(Hand hand) {
        return 0;
    }

    // CardGameInterface
    public void dealCards() {

    }

    // GamblingInterface
    public int checkChipAmount(Person personToCheck) {
        return 0;
    }

    // GamblingInterface
    public void placeBet(Person player, int betAmount) {
        player.getWallet().removeChips(betAmount);
    }

    // GamblingInterface
    public int getAnte() {
        return 0;
    }

    // GamblingInterface
    public void bootPlayerFromGame(Person personToBoot) {

    }

    // GamblingInterface
    public int checkPot() {
        return 0;
    }

//    public String askForPlayerName() {
//        return "nameTest";
//    }

    // GameInterface or Game Class
    public void start() {
        this.getDeck().shuffleDeck();
        Scanner scanner = new Scanner(System.in);

        if (this.getPlayer().getWallet().checkChips() <= 0) {
            System.out.println("You don't have anymore chips to play");
            return; // THIS EXITS THE METHOD
        }

        // Check bet > 0
        int betPlaced = 0;
        do {
            System.out.println("How many chips do you want to bet?");
            String inputString = scanner.nextLine(); // changed to nextLine to fix next loop
            try {
                betPlaced = Integer.parseInt(inputString);
                if (betPlaced <= 0) {
                    System.out.println("Your bet must be a number greater than zero.");
                    System.out.println();
                    continue;
                }
                else if (betPlaced <= this.getPlayer().getWallet().checkChips()) {
                    this.placeBet(this.getPlayer(), betPlaced);
                    break;
                } else {
                    System.out.println("You only have " + this.getPlayer().getWallet().checkChips() + " chip(s)");
                    System.out.println();
                }
            } catch (NumberFormatException ne) {
                System.out.println("Please try again.");
                System.out.println();
            }
        } while (true);

        this.hit(getPlayer());
        this.hit(getPlayer());
        this.hit(getDealer());
        this.hit(getDealer());

        int personHandSum = 0;
        int dealerHandSum = 0;

        String playerDecisionString = "";

        System.out.println();
        System.out.println("+++ PLAY BLACKJACK +++");
        do {
            personHandSum = rankSum(getPlayer());
            System.out.println();
            System.out.print(player.getName() + ": " + "\u270B");
            System.out.print(this.handToString(player.getPlayerHand()));
            System.out.print("\u270B" + ", hand = " + personHandSum);

            System.out.println();
            String dealerCardShown = getDealer().getPlayerHand().get(1).toString();
            System.out.print("Dealer: " + "\u270B" + "\uD83C\uDCA0" + " " + dealerCardShown + "\u270B");

            if (personHandSum == 21) {
                System.out.println();
                System.out.println("++++++++++++++++++++++++++++++++");
                System.out.println("BLACKJACK!+++ You won " + betPlaced + " chip(s)");
                System.out.println("++++++++++++++++++++++++++++++++");
                this.getPlayer().getWallet().addChips(2 * betPlaced);
                break;
            } else if (personHandSum > 21){
                System.out.println();
                System.out.println("+++++++++++++++++++++");
                System.out.println("BUST! You lost " + betPlaced + " chip(s)");
                System.out.println("+++++++++++++++++++++");
                break;
            }

            // get new deck if card number falls <= 16
            if (getDeck().getDeckOfCards().size() <= 16) {
                this.deck = new Deck();
                getDeck().shuffleDeck();
            }

            System.out.println();
            System.out.println("Do you want to \"hit\" or \"stay\"?: ");
            playerDecisionString = scanner.nextLine();

            if (playerDecisionString.equals("hit")) {
                this.hit(this.getPlayer());
            } else if (playerDecisionString.equals("stay")) {
                break;
            }
        } while (personHandSum < 21);

        dealerHandSum = this.rankSum(this.getDealer());
        if ( !(personHandSum >= 21) ) { // If player stayed with a hand < 21
            if (dealerHandSum <= 16) { // If dealer's hand >= 17, dealer has to stay
                this.hit(dealer); // Hit dealer only once if dealer's hand <= 16
            }
        }

        dealerHandSum = this.rankSum(this.getDealer());
        if (playerDecisionString.equals("stay")) {
            if ( (dealerHandSum <= 21) && (dealerHandSum > personHandSum) ) {
                System.out.println("++++++++++++++++++++++++");
                System.out.println("Dealer wins!");
                System.out.println("You lost " + betPlaced + " chip(s)");
                System.out.println("++++++++++++++++++++++++");
            } else if ( (dealerHandSum <= 21) && (dealerHandSum == personHandSum) ) {
                System.out.println("++++++++++++++++++++++++");
                System.out.println("It's a tie!");
                System.out.println("You keep your " + betPlaced + " chip(s)");
                System.out.println("++++++++++++++++++++++++");
                this.getPlayer().getWallet().addChips(betPlaced);
            } else {
                System.out.println("+++++++++++++++++++++++++");
                System.out.println(this.getPlayer().getName() + " wins!");
                System.out.println("You won " + betPlaced + " chip(s)");
                System.out.println("+++++++++++++++++++++++++");
                this.getPlayer().getWallet().addChips(2 * betPlaced);            }
        }

        System.out.println();
        System.out.println("FINAL SCORE:");
        personHandSum = this.rankSum(this.getPlayer());
        System.out.print(this.getPlayer().getName() + ": " + "\u270B");
        System.out.print(this.handToString(getPlayer().getPlayerHand()));
        System.out.print("\u270B" + ", hand = " + personHandSum);

        dealerHandSum = this.rankSum(this.getDealer());
        System.out.println();
        System.out.print("Dealer: " + "\u270B");
        System.out.print(handToString(getDealer().getPlayerHand()));
        System.out.print("\u270B" + ", hand = " + dealerHandSum);
        System.out.println();
        System.out.println();
        System.out.print(getPlayer().getName() + " has ");
        System.out.println(getPlayer().getWallet().checkChips() + " chip(s)");

        System.out.println();
        this.doYouWantToContinuePlaying();
        System.out.println();
    }

    public void doYouWantToContinuePlaying() {
        Scanner scanner = new Scanner(System.in);
        String test = "";
        do {
            System.out.println("Keep playing? (yes/no) ");
            test = scanner.nextLine();
            if (test.equals("yes")) {
                getPlayer().clearHand();
                getDealer().clearHand();
                start();
                break;
            } if (test.equals("no")) {
                break;
            }
        } while(true);
    }

    // Game Class
    public void end() {
        System.out.print("Thank you for playing");
        System.out.println();
    }

    public static void main (String[] args) {
        Person player = new Person("Luis");
        Blackjack blackjack = new Blackjack(player, 10);
        blackjack.start();
        blackjack.end();
    }
}
