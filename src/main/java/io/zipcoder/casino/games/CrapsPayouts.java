package io.zipcoder.casino.games;

import java.util.HashMap;

public class CrapsPayouts {

    public CrapsPayouts(){ }


    public static HashMap<Integer, Double> placeWinBetPayout() {
        HashMap<Integer, Double> payout = new HashMap<>();
        payout.put(6, 1.16);
        payout.put(8, 1.16);
        payout.put(5, 1.4);
        payout.put(9, 1.4);
        payout.put(4, 1.8);
        payout.put(10, 1.8);
        return payout;
    }

    public static HashMap<Integer, Double> placeLoseBetPayout() {
        HashMap<Integer, Double> payout = new HashMap<>();
        payout.put(6, 0.8);
        payout.put(8, 0.8);
        payout.put(5, 0.62);
        payout.put(9, 0.62);
        payout.put(4, 0.45);
        payout.put(10, 0.45);
        return payout;
    }

    public static HashMap<Integer, Double> passLineComeBetPayout() {
        HashMap<Integer, Double> payout = new HashMap<>();
        payout.put(4, 2.0);
        payout.put(10, 2.0);
        payout.put(5, 1.5);
        payout.put(9, 1.5);
        payout.put(6, 1.2);
        payout.put(8, 1.2);
        return payout;
    }

    public static HashMap<Integer, Double> dontPassLineDontComePayout() {
        HashMap<Integer, Double> payout = new HashMap<>();
        payout.put(4, .5);
        payout.put(10, .5);
        payout.put(5, .66);
        payout.put(9, .66);
        payout.put(6, .83);
        payout.put(8, .83);
        return payout;
    }
}
