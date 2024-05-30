package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameStatus;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.factory.GameFactory;
import com.game.tic_tac_toe.logic.GameLogic;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.GameRepository;
import com.game.tic_tac_toe.repository.GameStateRepository;
import com.game.tic_tac_toe.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameFactory gameFactory;

    @Mock
    private GameStateRepository gameStateRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartNewGame() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Player 1");

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Player 2");

        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setLevel(GameLevel.EASY);

        GameLogic gameLogic = mock(GameLogic.class);

        when(gameFactory.createGameLogic(GameType.TIC_TAC_TOE)).thenReturn(gameLogic);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game createdGame = gameService.startNewGame(GameType.TIC_TAC_TOE, GameLevel.EASY, player1, player2);

        assertNotNull(createdGame);
        assertEquals(GameStatus.IN_PROGRESS, createdGame.getStatus());
        assertEquals(GameLevel.EASY, createdGame.getLevel());
    }

    @Test
    public void testMakeMove() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player 1");
        player.setSymbol('X');

        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setLevel(GameLevel.EASY);

        GameLogic gameLogic = mock(GameLogic.class);
        when(gameLogic.makeMove(anyInt(), anyInt(), anyChar())).thenReturn(false);
        when(gameLogic.isValidMove(anyInt(), anyInt())).thenReturn(true);
        when(gameFactory.createGameLogic(GameType.TIC_TAC_TOE)).thenReturn(gameLogic);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
    }

    @Test
    public void testEndGame() {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        Game endedGame = gameService.endGame(1L);

        assertNotNull(endedGame);
        assertEquals(GameStatus.ENDED, endedGame.getStatus());
    }

    @Test
    public void testCheckGameStatus() {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        Game checkedGame = gameService.checkGameStatus(1L);

        assertNotNull(checkedGame);
        assertEquals(GameStatus.IN_PROGRESS, checkedGame.getStatus());
    }

    @Test
    public void testMakeMoveInvalidMove() {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);

        Player player = new Player();
        player.setId(1L);
        player.setName("Player 1");

        GameLogic gameLogic = mock(GameLogic.class);
        when(gameLogic.isValidMove(anyInt(), anyInt())).thenReturn(false);
        when(gameFactory.createGameLogic(GameType.TIC_TAC_TOE)).thenReturn(gameLogic);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        assertThrows(ResponseStatusException.class, () -> {
            gameService.makeMove(1L, 1L, 0, 0);
        });
    }
}
