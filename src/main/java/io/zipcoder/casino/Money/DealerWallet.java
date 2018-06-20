package io.zipcoder.casino.Money;

public class DealerWallet extends Wallet {
    private int chipAmount = Integer.MAX_VALUE;

    @Override
    public void addChipsToAmount(int chipsToAdd) {

    }

    @Override
    public void removeChipsFromAmount(int chipsToRemove) {

    }

    @Override
    public int checkChipAmount() {
        return chipAmount;

    }
}
