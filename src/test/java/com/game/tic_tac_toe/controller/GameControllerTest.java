package com.game.tic_tac_toe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.service.GameService;
import com.game.tic_tac_toe.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player1 = new Player();
        player1.setId(1L);
        player1.setName("Player 1");

        player2 = new Player();
        player2.setId(2L);
        player2.setName("Player 2");
    }

    @Test
    public void testStartGame() throws Exception {
        Game game = new Game();
        game.setId(1L);

        when(gameService.startNewGame(any(GameType.class), any(GameLevel.class), any(Player.class), any(Player.class)))
                .thenReturn(game);

        mockMvc.perform(post("/api/games/start")
                        .param("gameType", "TIC_TAC_TOE")
                        .param("gameLevel", "EASY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(player1, player2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testEndGame() throws Exception {
        Game game = new Game();
        game.setId(1L);

        when(gameService.endGame(anyLong())).thenReturn(game);

        mockMvc.perform(post("/api/games/1/end"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCheckGameStatus() throws Exception {
        Game game = new Game();
        game.setId(1L);

        when(gameService.checkGameStatus(anyLong())).thenReturn(game);

        mockMvc.perform(get("/api/games/1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testMakeToss() throws Exception {
        when(playerService.getPlayerById(anyLong())).thenReturn(player1).thenReturn(player2);

        mockMvc.perform(post("/api/games/toss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(player1, player2))))
                .andExpect(status().isOk());
    }
}
