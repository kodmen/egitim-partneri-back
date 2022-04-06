package com.hanrideb.service.utility;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class LeveUtilityTest {

    @Test
    void nextLevel() {}

    @Test
    void getPuan() {
        LevelUtility l = new LevelUtility();
        int result = l.getPuan(1);
        Assert.assertEquals(1000, result);
    }

    @Test
    void testGetPuan() {
        LevelUtility l = new LevelUtility();
        //l.levelYazdir(); {0=0, 1000=1, 2828=2, 5196=3, 8000=4, 11180=5, 14696=6, 18520=7, 22627=8,
        System.out.println(l.levelGetir(2000));
    }
}
