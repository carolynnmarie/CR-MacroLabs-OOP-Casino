package io.zipcoder.casino;

public class Dealer extends Person{


    private Wallet dealerWallet;

    public Dealer() {
        super("Dealer");
        this.dealerWallet = new DealerWallet();
    }


    public Wallet getDealerWallet() {
        return this.dealerWallet;
    }

}
