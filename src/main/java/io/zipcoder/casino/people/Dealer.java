package io.zipcoder.casino.people;

import io.zipcoder.casino.money.Wallet;

public class Dealer extends Person {


    private Wallet dealerWallet;

    public Dealer() {
        super("Dealer");
        this.dealerWallet = new Wallet();
    }


    public Wallet getDealerWallet() {
        return this.dealerWallet;
    }

}
