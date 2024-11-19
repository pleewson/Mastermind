package com.mastermind.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Guess {

    private List<String> colors;
    private int blackHits;
    private int whiteHits;

    public Guess(List<String> colors, int blackHits, int whiteHits) {
        this.colors = colors;
        this.blackHits = blackHits;
        this.whiteHits = whiteHits;
    }

}
