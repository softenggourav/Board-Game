let gameId;
let currentPlayer = 'X';
let players = {};

async function addPlayers() {
    const player1Name = document.getElementById('player1').value;
    const player2Name = document.getElementById('player2').value;

    if (!player1Name || !player2Name) {
        alert('Please enter names for both players.');
        return;
    }

    players['X'] = await createPlayer(player1Name, 'X');
    players['O'] = await createPlayer(player2Name, 'O');

    startGame();
}

async function createPlayer(name, symbol) {
    const response = await fetch('/api/players', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, symbol })
    });
    return response.json();
}

async function startGame() {
    const response = await fetch('/api/games/start?gameType=TicTacToe', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(Object.values(players))
    });
    const game = await response.json();
    gameId = game.id;

    document.getElementById('player-form').style.display = 'none';
    document.getElementById('game-board').style.display = 'block';
}

async function makeMove(row, col) {
    const move = {
        player: { id: players[currentPlayer].id },
        row,
        col
    };

    try {
        const response = await fetch(`/api/games/${gameId}/move`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(move)
        });

        if (response.ok) {
            document.querySelector(`[data-row="${row}"][data-col="${col}"]`).innerText = currentPlayer;
            const newMove = await response.json();
            checkGameStatus(newMove);
        } else {
            const error = await response.text();
            alert(error);
        }
    } catch (error) {
        console.error('Error making move:', error);
    }
}

function checkGameStatus(move) {
    if (move.game.status.startsWith('WINNER')) {
        alert(`${currentPlayer} wins!`);
        document.getElementById('status').innerText = `${currentPlayer} wins!`;
    } else if (move.game.status === 'DRAW') {
        alert('It\'s a draw!');
        document.getElementById('status').innerText = 'It\'s a draw!';
    } else {
        currentPlayer = currentPlayer === 'X' ? 'O' : 'X';
        document.getElementById('status').innerText = `Player ${currentPlayer}'s turn`;
    }
}

document.querySelectorAll('.cell').forEach(cell => {
    cell.addEventListener('click', function() {
        const row = this.getAttribute('data-row');
        const col = this.getAttribute('data-col');
        if (!this.innerText) {
            makeMove(row, col);
        }
    });
});
