package com.game.tic_tac_toe.controller;

import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.service.PlayerService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlayerControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }

    @Test
    public void createPlayer() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setSymbol('X');

        when(playerService.createPlayer(any(Player.class))).thenReturn(player);

        mockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Player1\", \"symbol\":\"X\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"name\":\"Player1\", \"symbol\":\"X\"}"));
    }

    @Test
    public void getPlayer() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setSymbol('X');

        when(playerService.getPlayerById(anyLong())).thenReturn(player);

        mockMvc.perform(get("/api/players/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"name\":\"Player1\", \"symbol\":\"X\"}"));
    }
}
