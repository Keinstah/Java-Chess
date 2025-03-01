# Chess Game in Java

## Overview
This is a simple chess game implemented in Java, featuring key chess functionalities such as movement, capturing, checking, and castling. It offers a graphical user interface (GUI) to play the game, including support for board themes and special moves like En Passant.

### Features
#### Working Features:
- **Movement**: Move chess pieces across the board.
- **Capture**: Capture an opponent's piece.
- **Checking**: Check the enemy's king.
- **Castling**: Move the king and rook for castling.
- **Board Theme**: Switch between different board themes (Default, Dark, Light, Blue).
- **En Passant**: A special pawn capture that can happen immediately after a pawn moves two squares forward from its starting position, and an enemy pawn could have captured it if the pawn had moved one square forward.

#### Non-Working Features:
- **Checkmate**: A game-ending condition where the opponent's king is in check and there is no way to remove the threat.
- **Draw**: A game-ending condition where the game ends in a tie.

## Requirements
- Java Development Kit (JDK) 8 or later.
- Swing for the graphical user interface.

## How to Run
1. Download or clone the repository:
   ```bash
   git clone http://github.com/Feefty/Java-Chess.git
   ```
2. Open the project in your favorite IDE (e.g., IntelliJ IDEA, Eclipse, etc.).
3. Compile and run the `ChessGame` class.
4. The game window will open, and you can start playing.

## Gameplay
- **Selecting a Piece**: Click on a piece to select it.
- **Moving a Piece**: After selecting a piece, click on an empty space or an opponent’s piece to move or capture.
- **Castling**: You can castle by moving the king two spaces, provided both the king and the rook have not moved, and there are no pieces between them.
- **En Passant**: This special pawn capture can be made when an opponent’s pawn moves two squares forward from its starting position and lands next to one of your pawns. If your pawn is in a position to capture it en passant, you can do so.

## Controls
- **Menu**:
  - **Game**: Exit the game.
  - **Help**: View information about the game.
  - **Themes**: Switch between different board themes.

## File Structure
- `ChessGame.java`: Main game logic, including piece movement and GUI rendering.
- `img/`: Folder containing images for the chess pieces.
- `cfg/about.txt`: Contains information about the game.

## Themes
The game supports the following themes:
- **Default**: Standard chessboard colors.
- **Dark**: Darker color scheme for the board.
- **Light**: Lighter color scheme for the board.
- **Blue**: Blue-themed board.

## Contribution
Feel free to fork the repository, make changes, and submit pull requests. Contributions are welcome!

## License
This project is open-source and available under the MIT License.
