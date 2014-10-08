/**
 * Chess Board Game
 * ~~~~~~~~~~~ Working Features: ~~~~~~~~~~~ 
 * Movement - To move the chess piece
 * Capture - To capture an enemy's piece
 * Checking - To check the enemy's king 
 * Castling - To cast the King and Rook
 * ~~~~~~~~~~~ Non-Working Features: ~~~~~~~~~~~ 
 * Checkmate - If the either side of King is check and doesn't have anymore move to uncheck
 * Draw - This includes the 50-move rule and stalemate
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

public class Chess implements ActionListener
{
	static JFrame frame = new JFrame();
	JPanel totalGUI;
	JLabel scoreLabel1, scoreLabel2, turnLabel;
	JButton[][] buttons = new JButton[8][8];
	GridBagConstraints gbc = new GridBagConstraints();
	int[][] pos = {
			// default position
			{8, 10, 9, 11, 12, 9, 10, 8},
			{7, 7, 7, 7, 7, 7, 7, 7},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 1},
			{2, 4, 3, 5, 6, 3, 4, 2}};
	int turn = 0;
	int castling[][] = {{0, 0}, {0, 0}};
	String[] pieceLabel = {
					"",
					"wPawn", "wRook", "wBishop", "wKnight", "wQueen", "wKing",
					"bPawn", "bRook", "bBishop", "bKnight", "bQueen", "bKing",
					};
	String[] pieceStr = {"Rook", "Knight", "Bishop", "Queen"};
	int lastMove[] = {-1, -1};
	final int Y_COORD = 0;
	final int X_COORD = 1;
	final int CAST_KING = 0;
	final int CAST_ROOK = 1;
	
	String[] msg = {
				"Unable to move.",
				"Invalid move position."
	};

	final int ERROR_CANNOT_MOVE_PIECE = 0;
	final int ERROR_CANNOT_MOVE_THERE = 1;
	String[] menuStr = {"Game", "Help"};
	char[] menuChar = {'G', 'H'};
	String[][] menuItemsStr = {
								{"Exit"},
								{"About"}
							};
	char[][] menuItemsChar = {
								{'E'},
								{'A'}
							};
	static JMenuBar menuBar = new JMenuBar();
	JMenu[] menus = new JMenu[menuStr.length];
	JMenuItem[][] menuItems = new JMenuItem[menuStr.length][5];
	final Color HIGHLIGHT = Color.GREEN;
	
	public JPanel createContentPane()
	{
		totalGUI = new JPanel();
		totalGUI.setLayout(new GridBagLayout());
		
		// adding menus
		for (int i = 0; i < menus.length; i++)
		{
			menus[i] = new JMenu(menuStr[i]);
			menuBar.add(menus[i]);
			menus[i].setMnemonic(menuChar[i]);
			
			for (int j = 0; j < menuItemsStr[i].length; j++)
			{
				menuItems[i][j] = new JMenuItem(menuItemsStr[i][j]);
				menus[i].add(menuItems[i][j]);
				menuItems[i][j].setMnemonic(menuItemsChar[i][j]);
				menuItems[i][j].addActionListener(this);
			}
		}
		
		

		// turn label
		turnLabel = new JLabel("Turn: White");
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.gridx = 7;
		gbc.gridy = 0;
		totalGUI.add(turnLabel, gbc);
		
		int board = 0;
		
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				String iconName = "";
				
				if (pos[y][x] != 0)
					iconName = pieceLabel[(pos[y][x])];
				
				if (iconName == "")
				{
					buttons[y][x] = new JButton("");
				}
				else
				{
					Icon icon = new ImageIcon(getClass().getResource("img/"+iconName+".png"));
					buttons[y][x] = new JButton("", icon);
				}
				
				if (board % 2 == 0)
				{
					buttons[y][x].setBackground(Color.white);
					buttons[y][x].setForeground(Color.gray);
				}
				else
				{
					buttons[y][x].setBackground(Color.gray);
					buttons[y][x].setForeground(Color.black);
				}
				
				gbc.gridx = x;
				gbc.gridy = y+1;
				gbc.gridwidth = 1;
				gbc.weightx = 0.0;
				gbc.weighty = 0.0;
				buttons[y][x].setFont(new Font("Arial", Font.PLAIN, 25));
				buttons[y][x].setBorder(BorderFactory.createLineBorder(Color.black, 1));
				buttons[y][x].setPreferredSize(new Dimension(80, 80));
				buttons[y][x].addActionListener(this);
				totalGUI.add(buttons[y][x], gbc);
				board++;
			}
			board++;
		}
		
		totalGUI.setOpaque(true);
		return totalGUI;
	}
	
	private int[] getLastMove()
	{
		return lastMove;
	}
	
	private void setLastMove(int x, int y)
	{
		lastMove[Y_COORD] = y;
		lastMove[X_COORD] = x;
	}
	
	private boolean isMovePerformed()
	{
		return lastMove[Y_COORD] == -1 && lastMove[X_COORD] == -1 ? false : true;
	}
	
	private void resetLastMove()
	{
		lastMove[Y_COORD] = -1;
		lastMove[X_COORD] = -1;
	}
	
	private int getPiece(int x, int y)
	{
		if ( y < 0 || y >= pos.length || x < 0 || x >= pos[y].length)
			return 0;
		
		return pos[y][x];
	}
	
	private int[] getPieceCoord(int p)
	{
		int temp[] = new int[2];
		
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				if (pos[y][x] == p)
				{
					temp[Y_COORD] = y;
					temp[X_COORD] = x;
					
					return temp;
				}
			}
		}
		
		return temp;
	}
	
	private String getMessage(int m)
	{
		return msg[m];
	}
	
	private void setPosition(int fromX, int fromY, int toX, int toY, int before)
	{
		buttons[fromY][fromX].setBorder(BorderFactory.createLineBorder(Color.black, 1));
			
		trans(toX, toY, getPiece(fromX, fromY));
		trans(fromX, fromY, before);
		
		turn++;
		String turnStr = "";
		
		if (getCurrentPlayer() == 0)
			turnStr = "White";
		else
			turnStr = "Black";
			
		turnLabel.setText("Turn: "+ turnStr);
		
		resetLastMove();
	}
	
	private void setPosition(int fromX, int fromY, int toX, int toY)
	{
		setPosition(fromX, fromY, toX, toY, 0);
	}
	
	private void trans(int x, int y, int to)
	{
		pos[y][x] = to;
		
		if (to == 0)
		{
			buttons[y][x].setIcon(null);
		}
		else
		{
			Icon icon = new ImageIcon(getClass().getResource("img/"+pieceLabel[to]+".png"));
			buttons[y][x].setIcon(icon);
		}
	}
	
	private boolean isCaptureable(int x, int y)
	{
		if (getCurrentPlayer() == 0 && getPiece(x, y) >= 1 && getPiece(x, y) <= 6 ||
			getCurrentPlayer() == 1 && getPiece(x, y) >= 7 && getPiece(x, y) <= 12)
			return false;
		
		return true;
	}
	
	private void promotePawn(int x, int y)
	{
		int promote;
		
		if (getCurrentPlayer() == 0 && y == 0 || getCurrentPlayer() == 1 && y == 7)
		{
			promote = JOptionPane.showOptionDialog(null, "Select the piece you to want to be promoted.", "Promotion", 
			        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
			        null, pieceStr, pieceStr[0]);
			
			// the piece to promote to
			switch (promote)
			{
				case 0:
					if (getCurrentPlayer() == 0)
						trans(x, y, 2);
					else
						trans(x, y, 8);
					break;
				case 1:
					if (getCurrentPlayer() == 0)
						trans(x, y, 4);
					else
						trans(x, y, 10);
					break;
				case 2:
					if (getCurrentPlayer() == 0)
						trans(x, y, 3);
					else
						trans(x, y, 9);
					break;
				case 3:
					if (getCurrentPlayer() == 0)
						trans(x, y, 5);
					else
						trans(x, y, 11);
					break;
			}
		}
	}
	
	private boolean initPawn(int fromX, int fromY, int toX, int toY)
	{
		if ( ! isCaptureable(toX, toY))
			return false;
		
		// moving itself
		if (fromX == toX)
		{
			// move one block in front
			if (getCurrentPlayer() == 0 && fromY-1 == toY && getPiece(toX, toY) == 0 ||
				getCurrentPlayer() == 1 && fromY+1 == toY && getPiece(toX, toY) == 0)
			{
				setPosition(fromX, fromY, toX, toY);
				promotePawn(toX, toY);
				return true;
			}
			else
			// can move two blocks in front if it is from the default position
			if (getCurrentPlayer() == 0 && fromY-2 == toY && fromY == 6 && getPiece(toX, toY) == 0 && getPiece(toX, toY+1) == 0 ||
					getCurrentPlayer() == 1 && fromY+2 == toY && fromY == 1 && getPiece(toX, toY) == 0 && getPiece(toX, toY-1) == 0)
			{
				setPosition(fromX, fromY, toX, toY);
				return true;
			}
		}
		else
		// capturing an enemy
		if (getCurrentPlayer() == 0 && fromX-1 == toX && fromY-1 == toY || getCurrentPlayer() == 0 && fromX+1 == toX && fromY-1 == toY ||
			getCurrentPlayer() == 1 && fromX+1 == toX && fromY+1 == toY || getCurrentPlayer() == 1 && fromX-1 == toX && fromY+1 == toY)
		{
			// cannot move diagonally
			if (getPiece(toX, toY) == 0)
				return false;
			
			setPosition(fromX, fromY, toX, toY);
			promotePawn(toX, toY);
			return true;
		}
		
		return false;
	}
	
	private boolean initRook(int fromX, int fromY, int toX, int toY)
	{
		if ( ! isCaptureable(toX, toY))
			return false;
		
		// horizontally
		if (fromX != toX && fromY == toY)
		{
			int min, max;
			
			if (fromX > toX)
			{
				min = toX;
				max = fromX;
			}
			else
			{
				min = fromX;
				max = toX;
			}
			
			for (int x = min; x < max; x++)
				if (getPiece(x, toY) != 0 && x != toX && fromX != x)
					return false;
			
			if (castling[getCurrentPlayer()][CAST_ROOK] == 0)
				castling[getCurrentPlayer()][CAST_ROOK] = 1;
			
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		else
		// vertically
		if (fromY != toY && fromX == toX)
		{

			int min, max;
			
			if (fromY > toY)
			{
				min = toY;
				max = fromY;
			}
			else
			{
				min = fromY;
				max = toY;
			}
			
			for (int y = min; y < max; y++)
				if (getPiece(toX, y) != 0 && y != toY && fromY != y)
					return false;

			if (castling[getCurrentPlayer()][CAST_ROOK] == 0)
				castling[getCurrentPlayer()][CAST_ROOK] = 1;
			
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		
		return false;
	}
	
	private boolean initBishop(int fromX, int fromY, int toX, int toY)
	{
		if ( ! isCaptureable(toX, toY))
			return false;
		
		int minY, minX, maxY, maxX, tempY, tempX;
		
		if (fromX > toX && fromY > toY)
		{
			minY = toY;
			maxY = fromY;
			minX = toX;
			maxX = fromX;

			tempY = (maxY - minY);
			tempX = (maxX - minX);
			
			if (tempX != tempY)
				return false;
			
			for (int y = minY, x = minX; x < maxX; x++, y++)
				if (getPiece(x, y) != 0 && fromX != x && fromY != y && toX != x && fromY != y)
					return false;
			
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		else
		if (fromX < toX && fromY < toY)
		{
			minY = fromY;
			maxY = toY;
			minX = fromX;
			maxX = toX;

			tempY = (maxY - minY);
			tempX = (maxX - minX);
			
			if (tempX != tempY)
				return false;
			
			for (int y = minY, x = minX; x < maxX; x++, y++)
				if (getPiece(x, y) != 0 && fromX != x && fromY != y && toX != x && fromY != y)
					return false;
			
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		else
		if (fromX < toX && fromY > toY)
		{
			minY = fromY;
			maxY = toY;
			minX = fromX;
			maxX = toX;

			tempY = (minY - maxY);
			tempX = (maxX - minX);
			
			if (tempX != tempY)
				return false;
			
			for (int y = minY, x = minX; x < maxX; x++, y--)
				if (getPiece(x, y) != 0 && fromX != x && fromY != y && toX != x && fromY != y)
					return false;
			
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		else
		if (fromX > toX && fromY < toY)
		{
			minY = fromY;
			maxY = toY;
			minX = fromX;
			maxX = toX;

			tempY = (maxY - minY);
			tempX = (minX - maxX);
			
			if (tempX != tempY)
				return false;
			
			for (int y = minY, x = minX; y < maxY; x--, y++)
				if (getPiece(x, y) != 0 && fromX != x && fromY != y && toX != x && fromY != y)
					return false;
			
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		
		return false;
	}
	
	private boolean initQueen(int fromX, int fromY, int toX, int toY)
	{
		if ( ! isCaptureable(toX, toY) || ! initBishop(fromX, fromY, toX, toY) && ! initRook(fromX, fromY, toX, toY))
			return false;
		
		return true;
	}
	
	private boolean initKnight(int fromX, int fromY, int toX, int toY)
	{
		if ( ! isCaptureable(toX, toY))
			return false;
		
		if (fromX+2 == toX && fromY+1 == toY ||
			fromX+2 == toX && fromY-1 == toY ||
			fromX-2 == toX && fromY+1 == toY ||
			fromX-2 == toX && fromY-1 == toY ||
			fromX+1 == toX && fromY+2 == toY ||
			fromX+1 == toX && fromY-2 == toY ||
			fromX-1 == toX && fromY+2 == toY ||
			fromX-1 == toX && fromY-2 == toY)
		{
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		
		return false;
	}
	
	private boolean initKing(int fromX, int fromY, int toX, int toY)
	{
		if (fromX == toX && fromY+1 == toY ||
			fromX+1 == toX && fromY+1 == toY ||
			fromX+1 == toX && fromY == toY ||
			fromX+1 == toX && fromY-1 == toY ||
			fromX-1 == toX && fromY == toY ||
			fromX-1 == toX && fromY-1 == toY ||
			fromX-1 == toX && fromY+1 == toY ||
			fromX == toX && fromY-1 == toY)
		{
			if ( ! isCaptureable(toX, toY))
				return false;
			
			if (castling[getCurrentPlayer()][CAST_KING] == 0)
				castling[getCurrentPlayer()][CAST_KING] = 1;
				
			setPosition(fromX, fromY, toX, toY);
			return true;
		}
		else
		if (castling[getCurrentPlayer()][CAST_KING] == 0 && castling[getCurrentPlayer()][CAST_ROOK] == 0)
		{
			if (getCurrentPlayer() == 0 && toX == 7 && toY == 7)
			{
				for (int x = fromX+1; x < 7; x++)
					if (getPiece(x, fromY) != 0)
						return false;

				castling[getCurrentPlayer()][CAST_KING] = 1;
				castling[getCurrentPlayer()][CAST_ROOK] = 1;

				setPosition(fromX, fromY, 6, 7);
				setPosition(7, 7, 5, 7);
				return true;
			}
			else
			if (getCurrentPlayer() == 1 && toX == 0 && toY == 0)
			{
				for (int x = fromX-1; x >= 0; x--)
					if (getPiece(x, fromY) != 0)
						return false;

				castling[getCurrentPlayer()][CAST_KING] = 1;
				castling[getCurrentPlayer()][CAST_ROOK] = 1;
				
				setPosition(fromX, fromY, 1, 0);
				setPosition(0, 0, 2, 0);
				return true;
			}	
		}
		
		return false;
	}
	
	private int isCheck()
	{
		int wKing[] = new int[2], bKing[] = new int[2];
		int check = 0;
		
		wKing = getPieceCoord(6);
		bKing = getPieceCoord(12);
		
		// white pawn
		if (getPiece(bKing[X_COORD]+1, bKing[Y_COORD]+1) == 1 ||
			getPiece(bKing[X_COORD]-1, bKing[Y_COORD]+1) == 1)
			check = 2;
		else
		// black pawn
		if (getPiece(wKing[X_COORD]+1, wKing[Y_COORD]-1) == 7 ||
			getPiece(wKing[X_COORD]-1, wKing[Y_COORD]-1) == 7)
			check += 1;
		
		if (check == 3)
			return check;
		
		// rooks and queens
		// vertical black
		for (int y = bKing[Y_COORD]; y < 8; y++)
		{
			if (getPiece(bKing[X_COORD], y) == 2 || getPiece(bKing[X_COORD], y) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[Y_COORD] == y)
				continue;
			else
			if (getPiece(wKing[X_COORD], y) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		for (int y = bKing[Y_COORD]; y >= 0; y--)
		{
			if (getPiece(bKing[X_COORD], y) == 2 || getPiece(bKing[X_COORD], y) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[Y_COORD] == y)
				continue;
			else
			if (getPiece(wKing[X_COORD], y) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		// vertical white
		for (int y = wKing[Y_COORD]; y < 8; y++)
		{
			if (getPiece(wKing[X_COORD], y) == 8 || getPiece(wKing[X_COORD], y) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[Y_COORD] == y)
				continue;
			else
			if (getPiece(wKing[X_COORD], y) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		for (int y = wKing[Y_COORD]; y >= 0; y--)
		{
			if (getPiece(wKing[X_COORD], y) == 8 || getPiece(wKing[X_COORD], y) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[Y_COORD] == y)
				continue;
			else
			if (getPiece(wKing[X_COORD], y) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		// horizontal black
		for (int x = bKing[X_COORD]; x < 8; x++)
		{
			if (getPiece(x, bKing[Y_COORD]) == 2 || getPiece(x, bKing[Y_COORD]) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, bKing[Y_COORD]) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		for (int x = bKing[X_COORD]; x >= 0; x--)
		{
			if (getPiece(x, bKing[Y_COORD]) == 2 || getPiece(x, bKing[Y_COORD]) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, bKing[Y_COORD]) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		// horizontal white
		for (int x = wKing[X_COORD]; x < 8; x++)
		{
			if (getPiece(x, wKing[Y_COORD]) == 8 || getPiece(x, wKing[Y_COORD]) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, wKing[Y_COORD]) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		for (int x = wKing[X_COORD]; x >= 0; x--)
		{
			if (getPiece(x, wKing[Y_COORD]) == 8 || getPiece(x, wKing[Y_COORD]) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, wKing[Y_COORD]) != 0)
				break;
		}
		
		if (check == 3)
			return check;
		
		// knight
		// black
		int tempX, tempY;
		tempX = bKing[X_COORD];
		tempY = bKing[Y_COORD];
				
		if (getPiece(tempX+2, tempY+1) == 4 ||
			getPiece(tempX+2, tempY-1) == 4 ||
			getPiece(tempX-2, tempY+1) == 4 ||
			getPiece(tempX-2, tempY-1) == 4 ||
			getPiece(tempX+1, tempY+2) == 4 ||
			getPiece(tempX+1, tempY-2) == 4 ||
			getPiece(tempX-1, tempY+2) == 4 ||
			getPiece(tempX-1, tempY-2) == 4)
		{
			if (check == 1)
				check += 2;
			else
			if (check != 3)
				check = 2;
		}
		
		if (check == 3)
			return check;
		
		tempX = wKing[X_COORD];
		tempY = wKing[Y_COORD];
		
		// white
		if (getPiece(tempX+2, tempY+1) == 10 ||
			getPiece(tempX+2, tempY-1) == 10 ||
			getPiece(tempX-2, tempY+1) == 10 ||
			getPiece(tempX-2, tempY-1) == 10 ||
			getPiece(tempX+1, tempY+2) == 10 ||
			getPiece(tempX+1, tempY-2) == 10 ||
			getPiece(tempX-1, tempY+2) == 10 ||
			getPiece(tempX-1, tempY-2) == 10)
		{
			if (check == 2)
				check += 1;
			else
			if (check != 3)
				check = 1;
		}
		
		if (check == 3)
			return check;
		
		// bishop
		// black
		for (int y = bKing[Y_COORD], x = bKing[X_COORD]; y < 8; y++, x++)
		{
			if (getPiece(x, y) == 3 || getPiece(x, y) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[Y_COORD] == y && bKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}
		
		if (check == 3)
			return check;

		for (int y = bKing[Y_COORD], x = bKing[X_COORD]; y >= 0; y--, x--)
		{
			if (getPiece(x, y) == 3 || getPiece(x, y) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[Y_COORD] == y && bKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}
		
		if (check == 3)
			return check;

		for (int y = bKing[Y_COORD], x = bKing[X_COORD]; y >= 0; y--, x++)
		{
			if (getPiece(x, y) == 3 || getPiece(x, y) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[Y_COORD] == y && bKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}
		
		if (check == 3)
			return check;

		for (int y = bKing[Y_COORD], x = bKing[X_COORD]; x >= 0; y++, x--)
		{
			if (getPiece(x, y) == 3 || getPiece(x, y) == 5)
			{
				if (check == 1)
					check += 2;
				else
				if (check != 3)
					check = 2;
				
				break;
			}
			else
			if (bKing[Y_COORD] == y && bKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}

		if (check == 3)
			return check;
		
		// white
		for (int y = wKing[Y_COORD], x = wKing[X_COORD]; y < 8; y++, x++)
		{
			if (getPiece(x, y) == 9 || getPiece(x, y) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[Y_COORD] == y && wKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}

		if (check == 3)
			return check;

		for (int y = wKing[Y_COORD], x = wKing[X_COORD]; y >= 0; y--, x--)
		{
			if (getPiece(x, y) == 9 || getPiece(x, y) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[Y_COORD] == y && wKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}

		if (check == 3)
			return check;

		for (int y = wKing[Y_COORD], x = wKing[X_COORD]; y >= 0; y--, x++)
		{
			if (getPiece(x, y) == 9 || getPiece(x, y) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[Y_COORD] == y && wKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}

		if (check == 3)
			return check;

		for (int y = wKing[Y_COORD], x = wKing[X_COORD]; x >= 0; y++, x--)
		{
			if (getPiece(x, y) == 9 || getPiece(x, y) == 11)
			{
				if (check == 2)
					check += 1;
				else
				if (check != 3)
					check = 1;
				
				break;
			}
			else
			if (wKing[Y_COORD] == y && wKing[X_COORD] == x)
				continue;
			else
			if (getPiece(x, y) != 0)
				break;
		}
		
		return check;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		// menu bars
		// game menu
		for (int i = 0; i < menuItems[0].length; i++)
		{
			if (e.getSource() == menuItems[0][i])
			{
				switch (i)
				{
					// exit
					case 0:
						if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Quit Game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
							System.exit(0);
						break;
				}
			}
		}
		
		// help menu
		for (int i = 0; i < menuItems[1].length; i++)
		{
			if (e.getSource() == menuItems[1][i])
			{
				switch (i)
				{
					case 0:
						JLabel aboutLabel = new JLabel(getAbout());
						aboutLabel.setFont(new Font("Arial", Font.PLAIN, 11));
						JOptionPane.showMessageDialog(null, aboutLabel, "About", JOptionPane.PLAIN_MESSAGE);
						break;
				}
			}
		}
		
		// chess pieces
		chessPiece:
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				if (e.getSource() == buttons[y][x])
				{	
					// no piece was selected before so we're going to select one.
					if ( ! isMovePerformed())
					{
						// white
						if (getCurrentPlayer() == 0)
						{
							// get the coords of the piece and check if it's from white.
							if (getPiece(x, y) >= 1 && getPiece(x, y) <= 6)
							{
								setLastMove(x, y);
								buttons[y][x].setBorder(BorderFactory.createLineBorder(HIGHLIGHT, 1));
							}
							else
							{
								JOptionPane.showMessageDialog(null, getMessage(ERROR_CANNOT_MOVE_PIECE));
							}
						}
						// black
						else
						{
							// get the coords of the piece and check if it's from black.
							if (getPiece(x, y) >= 7 && getPiece(x, y) <= 12)
							{
								setLastMove(x, y);
								buttons[y][x].setBorder(BorderFactory.createLineBorder(HIGHLIGHT, 1));
							}
							else
							{
								JOptionPane.showMessageDialog(null, getMessage(ERROR_CANNOT_MOVE_PIECE));
							}
						}
					}
					else
					{
						int[] from = new int[2];
						// get the coords of last moved piece.
						from = getLastMove();
						// get which type of piece was selected.
						int fromP = getPiece(from[X_COORD], from[Y_COORD]);

						// capturing or moving
						switch (fromP)
						{
							case 1: // pawn
							case 7:
								if ( ! initPawn(from[X_COORD], from[Y_COORD], x, y))
									invalidMove(from[X_COORD], from[Y_COORD]);
								break;
							case 2: // rook
							case 8:
								if ( ! initRook(from[X_COORD], from[Y_COORD], x, y))
									invalidMove(from[X_COORD], from[Y_COORD]);
								break;
							case 3: // bishop
							case 9:
								if ( ! initBishop(from[X_COORD], from[Y_COORD], x, y))
									invalidMove(from[X_COORD], from[Y_COORD]);
								break;
							case 4: // knight
							case 10:
								if ( ! initKnight(from[X_COORD], from[Y_COORD], x, y))
									invalidMove(from[X_COORD], from[Y_COORD]);
								break;
							case 5: // queen
							case 11:
								if ( ! initQueen(from[X_COORD], from[Y_COORD], x, y))
									invalidMove(from[X_COORD], from[Y_COORD]);
								break;
							case 6: // king
							case 12:
								if ( ! initKing(from[X_COORD], from[Y_COORD], x, y))
									invalidMove(from[X_COORD], from[Y_COORD]);
								break;
						}
						
						// checking
						switch (isCheck())
						{
							case 1:
								JOptionPane.showMessageDialog(null, "White Check");
								break;
							case 2:
								JOptionPane.showMessageDialog(null, "Black Check");
								break;
							case 3:
								JOptionPane.showMessageDialog(null, "White & Black Check");
								break;
						}
					}
					
					break chessPiece;
				}
			}
		}
	}
	
	private String getAbout()
	{
		String str = "";
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("cfg/about.txt"), Charset.defaultCharset());
			
			for (String line : lines)
				str += line + System.getProperty("line.separator"); } catch (IOException e) {
			System.out.println(e);
		}
		
		return str;
	}
	
	private int getCurrentPlayer()
	{
		return turn % 2;
	}

	private void invalidMove(int x, int y)
	{
		JOptionPane.showMessageDialog(null, getMessage(ERROR_CANNOT_MOVE_THERE));
		buttons[y][x].setBorder(BorderFactory.createLineBorder(Color.black, 1));
		resetLastMove();
	}

	public static void createAndShowGUI()
	{
		Chess cb = new Chess();
		frame.setTitle("ChessBoard Game");
		frame.setContentPane(cb.createContentPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(656, 710);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public static void main(String... args)
	{
		try {
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					createAndShowGUI();
				}
			});
		} catch(Exception e) {
			System.err.println("Unable to create a GUI");
		}
	}
}