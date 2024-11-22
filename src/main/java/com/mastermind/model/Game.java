package com.mastermind.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Game {
    private static Game instance;

    private GameStatus status;
    List<String> secretCode;
    List<Guess> guessHistory;


    public Game() {
        this.status = GameStatus.IN_PROGRESS;
        this.secretCode = new ArrayList<>();
        this.guessHistory = new ArrayList<>();
    }
}
