import pygame
import sys
import sqlite3
import sysv_ipc
import hashlib
import signal

# Initialize PyGame
pygame.init()

# Constants
WIDTH, HEIGHT = 500, 600
LINE_WIDTH = 15
BOARD_SIZE = 3
CELL_SIZE = WIDTH // BOARD_SIZE
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
LINE_COLOR = (23, 145, 135)
X_COLOR = (66, 66, 230)
O_COLOR = (230, 66, 66)
BACKGROUND = (240, 240, 240)
DRAW_COLOR = (150, 150, 150)

# Generate a fixed key for message queue
def get_message_queue_key():
    key_string = "TicTacToe_P2P_Game_Key"
    hash_object = hashlib.md5(key_string.encode())
    hex_dig = hash_object.hexdigest()
    return (int(hex_dig, 16) % 65000) + 1

KEY = get_message_queue_key()
MSG_SIZE = 16

# Set up screen
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption('TicTacToe P2P')
font = pygame.font.Font(None, 36)
small_font = pygame.font.Font(None, 24)

# Message types for the message queue
MSG_TYPE_CONNECT_P2 = 1
MSG_TYPE_CONNECT_P1 = 2
MSG_TYPE_MOVE = 3
MSG_TYPE_RESTART = 4

class TicTacToeGame:
    def __init__(self, player_name):
        self.board = [[None for _ in range(BOARD_SIZE)] for _ in range(BOARD_SIZE)]
        self.current_turn = 'X'
        self.player_name = player_name
        self.player_symbol = None
        self.opponent_name = None
        self.winner = None
        self.is_game_over = False
        self.score = {'X': 0, 'O': 0, 'Remize': 0}
        self.is_waiting = True
        self.message_queue = None
        self.debug_messages = []
        self.create_database()
        self.setup_message_queue()

    def add_debug_message(self, msg):
        """Adds a debug message and prints it."""
        print(msg)
        self.debug_messages.append(msg)
        if len(self.debug_messages) > 5:
            self.debug_messages.pop(0)

    def setup_message_queue(self):
        self.add_debug_message(f"Using fixed key for message queue: {KEY}")
        try:
            self.message_queue = sysv_ipc.MessageQueue(KEY, sysv_ipc.IPC_CREAT | sysv_ipc.IPC_EXCL)
            self.add_debug_message(f"New queue created with key {KEY}")
            self.player_symbol = 'X'
        except sysv_ipc.ExistentialError:
            self.message_queue = sysv_ipc.MessageQueue(KEY)
            self.add_debug_message(f"Connected to existing queue with key {KEY}")
            self.player_symbol = 'O'
            self.is_waiting = False
            message = f"{self.player_name}".ljust(MSG_SIZE).encode()
            self.message_queue.send(message, type=MSG_TYPE_CONNECT_P2)
            self.add_debug_message(f"Sent name {self.player_name} to Player 1")
            message, _ = self.message_queue.receive(type=MSG_TYPE_CONNECT_P1)
            self.opponent_name = message.decode().strip()
            self.add_debug_message(f"Connected to opponent: {self.opponent_name}")
            self.load_scores()
        else:
            self.add_debug_message("Waiting for Player 2 to connect...")
            message, _ = self.message_queue.receive(type=MSG_TYPE_CONNECT_P2)
            self.opponent_name = message.decode().strip()
            self.is_waiting = False
            message = f"{self.player_name}".ljust(MSG_SIZE).encode()
            self.message_queue.send(message, type=MSG_TYPE_CONNECT_P1)
            self.add_debug_message(f"Player {self.opponent_name} has connected")
            self.load_scores()

    def create_database(self):
        """Creates the database table for storing scores if not exists."""
        with sqlite3.connect('tictactoe_scores.db') as conn:
            cursor = conn.cursor()
            cursor.execute('''
            CREATE TABLE IF NOT EXISTS scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player1 TEXT,
                player2 TEXT,
                player1_wins INTEGER,
                player2_wins INTEGER,
                draws INTEGER
            )
            ''')
            conn.commit()

    def load_scores(self):
        """Load scores for the current game if exists."""
        with sqlite3.connect('tictactoe_scores.db') as conn:
            cursor = conn.cursor()
            cursor.execute('''
            SELECT player1_wins, player2_wins, draws FROM scores 
            WHERE (player1 = ? AND player2 = ?) OR (player1 = ? AND player2 = ?)
            ''', (self.player_name, self.opponent_name, self.opponent_name, self.player_name))
            result = cursor.fetchone()

            if result:
                self.score['X'], self.score['O'], self.score['Remize'] = result
                self.add_debug_message(f"Loaded scores: X:{self.score['X']} O:{self.score['O']} Draws:{self.score['Remize']}")
            else:
                self.add_debug_message("No previous scores found")

    def save_score(self):
        """Save the updated score to the database."""
        player1 = self.player_name if self.player_symbol == 'X' else self.opponent_name
        player2 = self.opponent_name if self.player_symbol == 'X' else self.player_name
        player1_wins = self.score['X']
        player2_wins = self.score['O']

        with sqlite3.connect('tictactoe_scores.db') as conn:
            cursor = conn.cursor()
            cursor.execute('''
            SELECT id FROM scores 
            WHERE (player1 = ? AND player2 = ?) OR (player1 = ? AND player2 = ?)
            ''', (player1, player2, player2, player1))
            result = cursor.fetchone()

            if result:
                cursor.execute('''
                UPDATE scores 
                SET player1_wins = ?, player2_wins = ?, draws = ? 
                WHERE id = ?
                ''', (player1_wins, player2_wins, self.score['Remize'], result[0]))
                self.add_debug_message("Updated score in the database")
            else:
                cursor.execute('''
                INSERT INTO scores (player1, player2, player1_wins, player2_wins, draws) 
                VALUES (?, ?, ?, ?, ?)
                ''', (player1, player2, player1_wins, player2_wins, self.score['Remize']))
                self.add_debug_message("Saved new score in the database")

    def make_move(self, row, col):
        """Makes a move if the game is not over and it's the player's turn."""
        if self.board[row][col] is None and not self.is_game_over and self.current_turn == self.player_symbol:
            self.board[row][col] = self.player_symbol
            message = f"{row},{col}".ljust(MSG_SIZE).encode()
            self.message_queue.send(message, type=MSG_TYPE_MOVE)
            self.add_debug_message(f"Sent move: {row},{col}")
            self.check_winner()
            self.current_turn = 'O' if self.current_turn == 'X' else 'X'

    def receive_move(self):
        """Receives a move from the opponent."""
        try:
            message, _ = self.message_queue.receive(type=MSG_TYPE_MOVE, block=False)
            row, col = map(int, message.decode().strip().split(','))
            if self.board[row][col] is None:
                opponent_symbol = 'O' if self.player_symbol == 'X' else 'X'
                self.board[row][col] = opponent_symbol
                self.add_debug_message(f"Received move: {row},{col}")
                self.check_winner()
                self.current_turn = 'O' if self.current_turn == 'X' else 'X'
        except sysv_ipc.BusyError:
            pass
        except Exception as e:
            self.add_debug_message(f"Error receiving move: {e}")

    def request_restart(self):
        """Requests a restart if the game is over."""
        if self.is_game_over:
            message = "RESTART".ljust(MSG_SIZE).encode()
            self.message_queue.send(message, type=MSG_TYPE_RESTART)
            self.add_debug_message("Sent restart request")
            self.reset_game()

    def check_restart_request(self):
        """Checks if there is a restart request from the opponent."""
        try:
            message, _ = self.message_queue.receive(type=MSG_TYPE_RESTART, block=False)
            self.add_debug_message("Received restart request")
            self.reset_game()
        except sysv_ipc.BusyError:
            pass
        except Exception as e:
            self.add_debug_message(f"Error checking restart: {e}")

    def check_winner(self):
        """Checks if there's a winner."""
        for row in range(BOARD_SIZE):
            if self.board[row][0] == self.board[row][1] == self.board[row][2] and self.board[row][0] is not None:
                self.winner = self.board[row][0]
                self.is_game_over = True
                self.update_score()
                return

        for col in range(BOARD_SIZE):
            if self.board[0][col] == self.board[1][col] == self.board[2][col] and self.board[0][col] is not None:
                self.winner = self.board[0][col]
                self.is_game_over = True
                self.update_score()
                return

        if self.board[0][0] == self.board[1][1] == self.board[2][2] and self.board[0][0] is not None:
            self.winner = self.board[0][0]
            self.is_game_over = True
            self.update_score()
            return

        if self.board[0][2] == self.board[1][1] == self.board[2][0] and self.board[0][2] is not None:
            self.winner = self.board[0][2]
            self.is_game_over = True
            self.update_score()
            return

        if all(self.board[row][col] is not None for row in range(BOARD_SIZE) for col in range(BOARD_SIZE)):
            self.winner = "Remiză"
            self.is_game_over = True
            self.update_score()

    def update_score(self):
        """Updates the score based on the winner."""
        if self.winner == 'X':
            self.score['X'] += 1
            self.add_debug_message("Player X wins!")
        elif self.winner == 'O':
            self.score['O'] += 1
            self.add_debug_message("Player O wins!")
        else:
            self.score['Remize'] += 1
            self.add_debug_message("Game ends in a draw!")

        self.save_score()

    def reset_game(self):
        """Resets the game to the initial state."""
        self.board = [[None for _ in range(BOARD_SIZE)] for _ in range(BOARD_SIZE)]
        self.current_turn = 'X'
        self.winner = None
        self.is_game_over = False
        self.add_debug_message("Game reset")

    def draw_board(self):
        """Draws the game board and the game status."""
        screen.fill(BACKGROUND)

        # Draw grid lines
        for i in range(1, BOARD_SIZE):
            pygame.draw.line(screen, LINE_COLOR, (i * CELL_SIZE, 0), (i * CELL_SIZE, WIDTH), LINE_WIDTH)
            pygame.draw.line(screen, LINE_COLOR, (0, i * CELL_SIZE), (WIDTH, i * CELL_SIZE), LINE_WIDTH)

        # Draw X and O
        for row in range(BOARD_SIZE):
            for col in range(BOARD_SIZE):
                pos_x = col * CELL_SIZE + CELL_SIZE // 2
                pos_y = row * CELL_SIZE + CELL_SIZE // 2

                if self.board[row][col] == 'X':
                    pygame.draw.line(screen, X_COLOR, (pos_x - 50, pos_y - 50), (pos_x + 50, pos_y + 50), LINE_WIDTH)
                    pygame.draw.line(screen, X_COLOR, (pos_x + 50, pos_y - 50), (pos_x - 50, pos_y + 50), LINE_WIDTH)
                elif self.board[row][col] == 'O':
                    pygame.draw.circle(screen, O_COLOR, (pos_x, pos_y), 50, LINE_WIDTH)

        # Display player names and turn info
        player_text = f"{self.player_name} ({self.player_symbol})"
        player_surface = font.render(player_text, True, BLACK)
        screen.blit(player_surface, (10, WIDTH + 20))

        if self.opponent_name:
            opponent_symbol = 'O' if self.player_symbol == 'X' else 'X'
            opponent_text = f"{self.opponent_name} ({opponent_symbol})"
            opponent_surface = font.render(opponent_text, True, BLACK)
            screen.blit(opponent_surface, (WIDTH - opponent_surface.get_width() - 10, WIDTH + 20))

        if not self.is_waiting and not self.is_game_over:
            turn_text = "It's your turn!" if self.current_turn == self.player_symbol else "Wait for your opponent..."
            turn_color = X_COLOR if self.player_symbol == 'X' else O_COLOR
            turn_surface = font.render(turn_text, True, turn_color)
            screen.blit(turn_surface, (WIDTH // 2 - turn_surface.get_width() // 2, WIDTH + 70))

        if self.is_waiting:
            wait_text = "Waiting for Player 2 to connect..."
            wait_surface = font.render(wait_text, True, BLACK)
            screen.blit(wait_surface, (WIDTH // 2 - wait_surface.get_width() // 2, WIDTH + 70))

        if self.is_game_over:
            if self.winner == "Remiză":
                result_text = "Game Over: Draw!"
                result_color = DRAW_COLOR
            else:
                player_won = self.winner == self.player_symbol
                result_text = f"Game Over: {'You Win!' if player_won else 'You Lose!'}"
                result_color = X_COLOR if self.winner == 'X' else O_COLOR
            result_surface = font.render(result_text, True, result_color)
            screen.blit(result_surface, (WIDTH // 2 - result_surface.get_width() // 2, WIDTH + 70))

            restart_text = "Press SPACE for a new game"
            restart_surface = small_font.render(restart_text, True, BLACK)
            screen.blit(restart_surface, (WIDTH // 2 - restart_surface.get_width() // 2, WIDTH + 120))

        score_text = f"Score: X: {self.score['X']} | O: {self.score['O']} | Draws: {self.score['Remize']}"
        score_surface = small_font.render(score_text, True, BLACK)
        screen.blit(score_surface, (WIDTH // 2 - score_surface.get_width() // 2, HEIGHT - 30))

        key_text = f"Message Queue Key: {KEY}"
        key_surface = small_font.render(key_text, True, BLACK)
        screen.blit(key_surface, (10, HEIGHT - 60))

        y_offset = HEIGHT - 90
        for msg in reversed(self.debug_messages):
            debug_surface = small_font.render(msg[-40:] if len(msg) > 40 else msg, True, BLACK)
            screen.blit(debug_surface, (WIDTH - debug_surface.get_width() - 10, y_offset))
            y_offset -= 20

def cleanup(signal, frame):
    print("Cleaning up and exiting...")
    if game.player_symbol == 'X' and game.message_queue:
        game.message_queue.remove()
        print("Message queue removed")
    pygame.quit()
    sys.exit(0)

# Register cleanup signal handler
signal.signal(signal.SIGINT, cleanup)

def main():
    global game
    player_name = input("Enter your name: ")
    game = TicTacToeGame(player_name)

    clock = pygame.time.Clock()
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.MOUSEBUTTONDOWN and not game.is_waiting:
                if not game.is_game_over and game.current_turn == game.player_symbol:
                    mouseX, mouseY = event.pos
                    if mouseY < WIDTH:
                        clicked_row = mouseY // CELL_SIZE
                        clicked_col = mouseX // CELL_SIZE
                        game.make_move(clicked_row, clicked_col)
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_SPACE and game.is_game_over:
                    game.request_restart()

        if not game.is_waiting and not game.is_game_over and game.current_turn != game.player_symbol:
            game.receive_move()
        
        game.draw_board()
        pygame.display.update()
        clock.tick(60)

if __name__ == '__main__':
    main()
