package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class War extends Game implements CardGameInterface {

    private Person dealer;
    private Person player;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;

    private Deck deck;

    private Scanner input = new Scanner(System.in);

    public War(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.deck = new Deck();
        this.playerHand = deck.dealHand(26);
        this.dealerHand = deck.dealHand(26);
    }

    public void setDealerHand(ArrayList<Card> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public Person getPlayer(){
        return this.player;
    }
    public Person getDealer(){
        return this.dealer;
    }
    public ArrayList<Card> getPlayerHand(){
        return this.playerHand;
    }

    public ArrayList<Card> getDealerHand(){
        return this.dealerHand;
    }

    public void start() {
        System.out.println("Welcome to WAR! Enter anything into the console to play a card");
        System.out.println("Enter 'exit' at any time to end the game");
        engine(playerHand, dealerHand);
    }

    public void engine(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
        String exitStay = input.nextLine();
        System.out.println("Lets go to war!");
        while (!("exit").equals(exitStay)) {
            while (playerHand.size() !=0 && dealerHand.size() !=0) {
                Card playerCard = playerHand.remove(0);
                Card dealerCard = dealerHand.remove(0);
                int winCard = 0;
                System.out.println("You played " + playerCard + " and the dealer played " + dealerCard);
                if (playerCard.toInt() == dealerCard.toInt()) {
                    winCard = iDeclareWar(playerHand, dealerHand);
                    ArrayList<Card> warWin = new ArrayList<>(Arrays.asList(playerHand.get(0),playerHand.get(1),playerHand.get(2),
                            dealerHand.get(0), dealerHand.get(1), dealerHand.get(2)));
                    if (winCard == 1) {
                        playerHand.addAll(warWin);
                    } else if (winCard == 2) {
                        dealerHand.addAll(warWin);
                    }
                } else if (playerCard.toInt() > dealerCard.toInt()) {
                    playerHand.add(playerCard);
                    playerHand.add(dealerCard);
                } else {
                    dealerHand.add(playerCard);
                    dealerHand.add(dealerCard);
                }
                if (playerCard.toInt() > dealerCard.toInt()) {
                    System.out.println("You won the round!  You now have " + playerHand.size() + " cards.  Dealer now has "
                            + dealerHand.size() + " cards.");
                } else {
                    System.out.println("Dealer won the round.  You now have" + playerHand.size() + " cards.  Dealer now has "
                            +dealerHand.size() + " cards.");
                }
            }
        }
        end(playerHand,dealerHand);
    }



    // To avoid recursion I am changing making a minor alteration to the game: if the 3rd card in an "I declare war" is also a tie
    // then the win goes to the person whose 2nd card is higher, instead of both players going again.
    public int iDeclareWar(ArrayList<Card> player1, ArrayList<Card> dealer1) {
        int win = 0;
        if (player1.size() >= 3 && dealer1.size() >= 3) {
            System.out.println("I D E C L A R E  W A R!!\nPlayer's top card: " + player1.get(2) + ". Dealer's top card: " + dealer1.get(2));
            if(player1.get(2).toInt() > dealer1.get(2).toInt()){
                win = 1;
            } else if(player1.get(2).toInt() < dealer1.get(2).toInt()){
                win = 2;
            } else if(player1.get(2).toInt() == dealer1.get(2).toInt()){
                if(player1.get(1).toInt() > dealer1.get(1).toInt()){
                    win = 1;
                } else {
                    win = 2;
                }
            }
        } else if(player1.size()<3) {
            System.out.println("Sorry, you don't have enough cards to go to war.");
            end(player1, dealer1);
        } else {
            System.out.println("Dealer doesn't have enough cards to go to war");
            end(player1, dealer1);
        }
        return win;
    }


    public void end(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
        String winner = "And the winner is ";
        winner += playerHand.size() < 25 ? "the dealer!": "YOU!";
        winner += "If you want to play again, enter 'yes', or enter anything else to return to the casino";
        playerHand.clear();
        dealerHand.clear();
        System.out.println(winner);
        if (input.nextLine().equalsIgnoreCase("yes")) {
            start();
        }
    }

    public void end(){

    }

    public int checkNumberOfCards(Person handToCheck) {
        return handToCheck.getPlayerHand().size();
    }

    @Override
    public ArrayList<Card> dealCards(Person person) {
        return null;
    }

}
