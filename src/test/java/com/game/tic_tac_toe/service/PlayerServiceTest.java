package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        player.setId(1L);
        player.setName("Player1");
        player.setSymbol('X');

        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.createPlayer(player);

        assertEquals(player, result);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void testGetPlayerById() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setSymbol('X');

        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(1L);

        assertEquals(player, result);
    }

    @Test
    public void testGetPlayerByIdNotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            playerService.getPlayerById(1L);
        });
    }
}
