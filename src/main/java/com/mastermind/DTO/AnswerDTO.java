package com.mastermind.DTO;

import com.mastermind.model.GameStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {

    private GameStatus gameStatus;
    private int blackHits;
    private int whiteHits;

}
