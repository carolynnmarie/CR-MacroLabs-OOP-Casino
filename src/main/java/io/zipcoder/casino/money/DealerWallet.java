package io.zipcoder.casino.money;

public class DealerWallet extends Wallet {
    private int chipAmount = Integer.MAX_VALUE;

    @Override
    public void addChips(int chipsToAdd) {

    }

    @Override
    public void removeChips(int chipsToRemove) {

    }

    @Override
    public int checkChips() {
        return chipAmount;

    }
}
