package com.hanrideb.service.utility;

import com.hanrideb.domain.Entry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class LevelUtility {

    public static HashMap<Integer, Integer> level;
    public static TreeMap<Integer, Integer> levelPuan;

    public LevelUtility() {
        level = new HashMap<>();
        levelPuan = new TreeMap<>();
        level.put(0, 0);
        levelPuan.put(0, 0);
        levelEkle();
    }

    public void levelYazdir() {
        System.out.println(levelPuan);
    }

    private void levelEkle() {
        for (int i = 1; i < 101; i++) {
            level.put(i, (int) nextLevel(i));
            levelPuan.put((int) nextLevel(i), i);
        }
    }

    public double nextLevel(double level) {
        var exponent = 1.5;
        var baseXP = 1000;
        return Math.floor(baseXP * (Math.pow(level, exponent)));
    }

    public int getPuan(int levelDeger) {
        return level.get(levelDeger);
    }

    public int levelGetir(int puan) {
        return levelPuan.floorEntry(puan).getValue();
    }

    public static void main(String[] args) {
        LevelUtility leveUtility = new LevelUtility();
        leveUtility.levelYazdir();
    }
}
