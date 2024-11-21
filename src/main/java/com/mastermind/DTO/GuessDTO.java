package com.mastermind.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GuessDTO {
    private List<String> guess;
}
