package com.hanrideb.service.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultsOfExam {

    private int countOfCorrect;
    private int countOfWrong;

    public int getCountOfBlank() {
        return countOfBlank;
    }

    public List<cevaplar> getResult() {
        return result;
    }

    private int countOfBlank;
    private List<cevaplar> result;

    public ResultsOfExam() {
        this.countOfCorrect = 0;
        this.countOfWrong = 0;
        result = new ArrayList<>();
    }

    public void increaseOfCorrect() {
        this.countOfCorrect++;
        this.result.add(cevaplar.dogru);
    }

    public void increaseOfWrong() {
        this.countOfWrong++;
        this.result.add(cevaplar.yanlis);
    }

    public void increaseOfBlank() {
        this.countOfBlank++;
        this.result.add(cevaplar.bos);
    }

    public int getCountOfCorrect() {
        return countOfCorrect;
    }

    public int getCountOfWrong() {
        return countOfWrong;
    }
}

enum cevaplar {
    dogru,
    yanlis,
    bos,
}
