package com.mastermind.controller;

import com.mastermind.DTO.AnswerDTO;
import com.mastermind.DTO.GuessDTO;
import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import com.mastermind.service.GameService;
import com.mastermind.service.GuessService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public String startGameVsComputer(HttpSession session, Model model) {
        Game game = gameService.startGameWithComputer(4);
        session.setAttribute("game", game);
        log.info("colors in game: {}", game.getSecretCode());
        return "game"; //todo change address
    }


    @GetMapping("/secret-code")
    String getGameVsPlayer() {
        return "secret-code";
    }


    @PostMapping("/start-game-vs-player")
    public String submitResponses(@RequestParam String color1,
                                  @RequestParam String color2,
                                  @RequestParam String color3,
                                  @RequestParam String color4,
                                  HttpSession session) {
        Game game = new Game();
        List<String> colors = new ArrayList<>();
        colors.add(color1);
        colors.add(color2);
        colors.add(color3);
        colors.add(color4);
        game.setSecretCode(colors); //todo separate to service
        session.setAttribute("game", game);

        log.info("response: {}", colors);
        return "game";
    }


    //1
    @PostMapping("/game")
    public String beginGame(HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        model.addAttribute("currentRound", game.getGuessHistory().size() + 1);
        model.addAttribute("guessHistory", game.getGuessHistory());
        model.addAttribute("difficulty", game.getSecretCode().size());

        return "game";
    }


    //2
    @PostMapping("/submitGuess")
    public ResponseEntity<AnswerDTO> submitGuess(@RequestBody GuessDTO guess, HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        Guess resultGuess = guessService.checkGuess(guess.getGuess(), game);
        log.info("FRONTEND COLORS: {}", guess.getGuess());
        log.info("GAME INFO: SECRET CODE{}", game.getSecretCode());
//        log.info("GAME INFO: HISTORY SIZE{}", game.getGuessHistory().size());
//        log.info("HISTORY [0] {}", game.getGuessHistory().get(0));

        log.info("GAME INFO: STATUS:{}", game.getStatus());

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setGameStatus(game.getStatus());
        answerDTO.setBlackHits(resultGuess.getBlackHits());
        answerDTO.setWhiteHits(resultGuess.getWhiteHits());
        //todo clear it

        return ResponseEntity.ok(answerDTO);
    }


}
