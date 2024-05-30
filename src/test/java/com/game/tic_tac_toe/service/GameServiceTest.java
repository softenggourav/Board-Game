package com.game.tic_tac_toe.service;

import com.game.tic_tac_toe.constants.GameLevel;
import com.game.tic_tac_toe.constants.GameStatus;
import com.game.tic_tac_toe.constants.GameType;
import com.game.tic_tac_toe.factory.GameFactory;
import com.game.tic_tac_toe.logic.GameLogic;
import com.game.tic_tac_toe.model.Game;
import com.game.tic_tac_toe.model.Move;
import com.game.tic_tac_toe.model.Player;
import com.game.tic_tac_toe.repository.GameRepository;
import com.game.tic_tac_toe.repository.MoveRepository;
import com.game.tic_tac_toe.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private MoveRepository moveRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameFactory gameFactory;

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameLogic gameLogic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartNewGame() {
        Game game = new Game();
        game.setId(1L);
        game.setLevel(GameLevel.EASY);
        Player player1 = new Player();
        Player player2 = new Player();

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameFactory.createGameLogic(GameType.TIC_TAC_TOE)).thenReturn(gameLogic);

        Game result = gameService.startNewGame(GameType.TIC_TAC_TOE, GameLevel.EASY, player1, player2);

        assertEquals(game, result);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(gameLogic, times(1)).startGame();
    }

//    @Test
//    public void testMakeMove() {
//        Game game = new Game();
//        game.setId(1L);
//        game.setStatus(GameStatus.IN_PROGRESS);
//        Player player1 = new Player();
//        player1.setId(1L);
//        player1.setSymbol('X');
//
//        Player player2 = new Player();
//        player2.setId(1L);
//        player2.setSymbol('O');
//
//        // Ensure the startNewGame method correctly initializes the game logic.
//        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
//        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player1));
//        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player2));
//        when(gameLogic.makeMove(anyInt(), anyInt(), anyChar())).thenReturn(true);
//        when(gameFactory.createGameLogic(GameType.TIC_TAC_TOE)).thenReturn(gameLogic);
//        when(moveRepository.save(any(Move.class))).thenReturn(new Move());
//
//        gameService.startNewGame(GameType.TIC_TAC_TOE, GameLevel.EASY, player1, player2);
//
//        Move result = gameService.makeMove(1L, 1L, 0, 0);
//
//        assertEquals(1L, result.getPlayer().getId());
//        verify(gameLogic, times(1)).makeMove(0, 0, 'X');
//    }

    @Test
    public void testEndGame() {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game result = gameService.endGame(1L);

        assertEquals(GameStatus.ENDED, result.getStatus());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testCheckGameStatus() {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        Game result = gameService.checkGameStatus(1L);

        assertEquals(GameStatus.IN_PROGRESS, result.getStatus());
    }
}
