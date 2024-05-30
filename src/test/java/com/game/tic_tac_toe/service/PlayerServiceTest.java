package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePlayer() {
        Player player = new Player();
        player.setName("Player 1");

        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player createdPlayer = playerService.createPlayer(player);

        assertNotNull(createdPlayer);
        assertEquals("Player 1", createdPlayer.getName());
    }

    @Test
    public void testGetPlayerById() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player 1");

        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        Player foundPlayer = playerService.getPlayerById(1L);

        assertNotNull(foundPlayer);
        assertEquals("Player 1", foundPlayer.getName());
    }

    @Test
    public void testGetPlayerByIdNotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            playerService.getPlayerById(1L);
        });
    }
}
