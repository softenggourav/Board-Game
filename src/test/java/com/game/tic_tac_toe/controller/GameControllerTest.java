package com.game.tic_tac_toe.controller;

import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.Move;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {
    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    public void startGame() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setLevel(GameLevel.EASY);

        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Player1");
        player1.setSymbol('X');

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Player2");
        player2.setSymbol('O');

        when(gameService.startNewGame(eq(GameType.TIC_TAC_TOE), eq(GameLevel.EASY), any(Player.class), any(Player.class))).thenReturn(game);

        mockMvc.perform(post("/api/games/start")
                .param("gameType", "TIC_TAC_TOE")
                .param("gameLevel", "EASY")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"name\":\"Player1\", \"symbol\":\"X\"}, {\"name\":\"Player2\", \"symbol\":\"O\"}]"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"level\":\"EASY\"}"));
    }

    @Test
    public void makeMove() throws Exception {
        Move move = new Move();
        move.setId(1L);
        move.setRow(0);
        move.setCol(0);

        when(gameService.makeMove(anyLong(), anyLong(), eq(0), eq(0))).thenReturn(move);

        mockMvc.perform(post("/api/games/1/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player\":{\"id\":1}, \"row\":0, \"col\":0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"row\":0, \"col\":0}"));
    }

    @Test
    public void endGame() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setLevel(GameLevel.EASY);

        when(gameService.endGame(anyLong())).thenReturn(game);

        mockMvc.perform(post("/api/games/1/end"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"level\":\"EASY\"}"));
    }

    @Test
    public void checkGameStatus() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setLevel(GameLevel.EASY);

        when(gameService.checkGameStatus(anyLong())).thenReturn(game);

        mockMvc.perform(get("/api/games/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"level\":\"EASY\"}"));
    }
}
