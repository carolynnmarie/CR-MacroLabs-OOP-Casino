package io.zipcoder.casino;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Wallet {

    private int chipAmount;

    public Wallet(){
        this.chipAmount = 0;
    }

    public void addChips(int chipsToAdd) {
        this.chipAmount = this.chipAmount + chipsToAdd;
    }

    public void removeChips(int chipsToRemove) {
        this.chipAmount = this.chipAmount - chipsToRemove;
    }

    public int checkChips(){
        return this.chipAmount;
    }


    public void fillWallet() {
        String isGambling;
        int walletAmount;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("If you will be playing a gambling game press y, if not, press n.");
            isGambling = in.nextLine();
        } while (!(isGambling.equals("y")) && !(isGambling.equals("n")));
        if(isGambling.equals("y")) {
            System.out.println("Please enter number of chips you would like to begin with");
            walletAmount = getUserInput();
            addChips(walletAmount);
        }
    }

    private int getUserInput(){
        Scanner in = new Scanner(System.in);
        try {
            return in.nextInt();
        } catch (InputMismatchException e){
            System.out.println("Input not recognized. Setting chips to 500.");
            return 500;
        }
    }

}
