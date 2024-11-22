package com.mastermind.controller;

import com.mastermind.DTO.AnswerDTO;
import com.mastermind.DTO.ColorsDTO;
import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import com.mastermind.service.GameService;
import com.mastermind.service.GuessService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class GameController {

    private GameService gameService;
    private GuessService guessService;

    public GameController(GameService gameService, GuessService guessService) {
        this.gameService = gameService;
        this.guessService = guessService;
    }


    @GetMapping("/")
    public String getHome() {
        return "home";
    }


    @PostMapping("/start-game-vs-computer")
    public String startGameVsComputer(HttpSession session) {
        Game game = gameService.startGameWithComputer(4);
        session.setAttribute("game", game);

        log.info("colors in game: {}", game.getSecretCode());
        return "game";
    }


    @GetMapping("/secret-code")
    String getGameVsPlayer() {
        return "secret-code";
    }


    @PostMapping("/start-game-vs-player")
    public ResponseEntity<String> submitResponses(@RequestBody ColorsDTO colorsDTO, HttpSession session) {
        Game game = gameService.startGameWithPlayer(colorsDTO.getColors());
        session.setAttribute("game", game);

        log.info("response secretCode: {}", colorsDTO.getColors());
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/get-game")
    public String getGame() {
        return "game";
    }


    @PostMapping("/submitGuess")
    public ResponseEntity<AnswerDTO> submitGuess(@RequestBody ColorsDTO colorsDTO, HttpSession session) {
        Game game = (Game) session.getAttribute("game");
        Guess resultGuess = guessService.checkGuess(colorsDTO.getColors(), game);

        log.info("FRONTEND COLORS: {}", colorsDTO.getColors());
        log.info("GAME INFO: SECRET CODE{}", game.getSecretCode());
        log.info("GAME INFO: STATUS:{}", game.getStatus());

        return ResponseEntity.ok(gameService.getAnswer(game,resultGuess));
    }
}
