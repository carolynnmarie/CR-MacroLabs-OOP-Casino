package io.zipcoder.casino;

import io.zipcoder.casino.games.*;
import io.zipcoder.casino.people.*;
import org.junit.Assert;
import org.junit.Test;

public class GameMenuTest {

    @Test
    public void testChooseGame() {
        String chosenGame = "war";
        Person player = new Person ("Adam");
        Game expected = new War(player);

        Game actual = GameMenu.chooseGame(chosenGame, player);

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChooseGame2() {
        String chosenGame = "theibhwn";
        Person player = new Person ("Adam");
        Game expected = new War(player);

        Game actual = GameMenu.chooseGame(chosenGame, player);

        Assert.assertNotEquals(actual, expected);
    }

}
