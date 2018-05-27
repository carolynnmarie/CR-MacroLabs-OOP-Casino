package io.zipcoder.casino;

import io.zipcoder.casino.money.DealerWallet;
import org.junit.Assert;
import org.junit.Test;

public class DealerWalletTest {

    @Test
    public void dealerWalletRemoveTest() {
        DealerWallet testWallet = new DealerWallet();
        testWallet.removeChips(500);
        Integer expected = Integer.MAX_VALUE;
        Integer actual = testWallet.checkChips();
        Assert.assertEquals(expected, actual);
    }
}
