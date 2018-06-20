package io.zipcoder.casino.People;

import io.zipcoder.casino.Money.DealerWallet;

public class Dealer extends Person {


    private DealerWallet dealerWallet;

    public Dealer() {
        super("Dealer");
        this.dealerWallet = new DealerWallet();
    }

    public DealerWallet getDealerWallet() {
        return this.dealerWallet;
    }


}
