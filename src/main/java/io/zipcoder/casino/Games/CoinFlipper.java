package io.zipcoder.casino.Games;

import io.zipcoder.casino.Dice.Coin;
import io.zipcoder.casino.People.Person;

import java.util.Scanner;

public class CoinFlipper extends Game implements GameInterface {

    private boolean gameIsRunning;
    private Person player;
    private Scanner input;
    private Coin coin;

    public CoinFlipper(Person player) {
        this.player = player;
        this.input = new Scanner(System.in);
        this.coin = new Coin();
    }

    public void start() {
        gameIsRunning = true;
        System.out.println("Welcome to Coin Flipper!");
        System.out.println("In this simple game you can bet chips on a lucky coin.");
        System.out.println("If it lands on heads, you get double your money!  But if it lands on tails, you get nothing!");
        System.out.println("If you're feeling lucky, enter how much you would like to bet and hit enter:");
        engine();
    }

    public void engine() {
        while (gameIsRunning == true) {
            int thisRoundsBet = 0;
            if (checkChipAmount(player) == 0) {bootPlayerFromGame();}
            if (input.hasNextInt()) {
                thisRoundsBet = input.nextInt();
                placeBet(player, thisRoundsBet);
            } else {
                System.out.println("Invalid input!  Bye-bye!");
                end();
            }
            if (gameIsRunning) {
                String coinFace = coin.flip();
                if (coinFace.equals("heads")) {
                    int payout = thisRoundsBet * 2;
                    System.out.println("Heads!  You win!  Your payout: " + payout);
                    player.getWallet().addChips(payout);
                } else {
                    System.out.println("Tails!  You lose!");
                }
                System.out.println("Enter another bet to play again, or enter anything else to quit");
            }
        }
    }

    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChipAmount();
    }

    public void placeBet(Person personPlacingBet, int betAmount) {
        personPlacingBet.getWallet().removeChips(betAmount);
    }

    public void bootPlayerFromGame() {
        gameIsRunning = false;
        System.out.println("Sorry, you're out of money!  Bye-bye!");
    }

    public void end() {
        System.out.println("Thanks for playing!");
        gameIsRunning = false;
    }

}
