import java.util.List;
import java.util.ArrayList;

public class Board {
	public int boardSize;
	private int[][] board;
	
	public Board(int size) {
		if(size < 6) {
			System.out.println("Minimum board size is 6! Making size equal to 6");
			size = 6;
		}
		
		this.boardSize = size;
		this.board = new int[size][size];
		
		reset();
	}
	
	public String[] playsForPlayer(int player) {
		List<String> list = new ArrayList<String>();
		
		for(int r = 0 ; r < this.boardSize ; r++) 
			for(int c = 0 ; c < this.boardSize ; c++) {
				if(playerCanPlay(player, r, c)) {
					list.add(  Character.toString((char)(r+65)) + (c+1) );
				} 				
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	public boolean play(int state,char rowChar, int colH) {
		int col = colH - 1;
		int row = (int)Character.toUpperCase(rowChar) - 65;
		boolean resp = false;
		if(row < 0 || row > this.boardSize - 1) {
			System.out.println("Can't play on a out-of-bounds row: " + row);
		}
		else if(col < 0 || col > this.boardSize - 1) {
			System.out.println("Can't play on a out-of-bounds column: " + col);
		}
		else if(playerCanPlay(state, row, col)) {
				if(state == 1) {
					this.board[row][col] = 2; 
					this.board[row+1][col] = 2; 
				}
				else if(state == 2) {
					this.board[row][col] = 1; 
					this.board[row][col+1] = 1; 
				}
				resp = true;
		} 
		
		return resp;
	}
	
	public int getState(int rowH, int colH) {
		int col = colH - 1;
		int row = rowH - 1;
		
		if(row < 0 || row > this.boardSize - 1) {
			System.out.println("Out of bounds row: " + row);
		}
		else if(col < 0 || col > this.boardSize - 1) {
			System.out.println("Out of bounds column: " + col);
		}
		else return board[row][col];
		
		return 0;
	}
	
	public int getSquareState(char rowChar, int colH) {
		int row = (int)Character.toUpperCase(rowChar) - 64;
		return getState(row,colH);
	}
	
	public void reset() {
		for(int i = 0 ; i < this.boardSize ; i++) 
			for(int j = 0 ; j < this.boardSize ; j++) 
			board[i][j] = 0;
	}
	
	private boolean playerCanPlay(int p, int row, int col) {
		int player = 2;
		if(p % 2 == 1) 
			player = 1;
		
		boolean resp = false;
		
		if(player == 1) {
			//White is playing
			if(row > this.boardSize - 2 || this.board[row+1][col] != 0) {
				System.out.println("NOT A VALID MOVE!");
			}
			else if (this.board[row][col] != 0) {
				System.out.println("Occupied play location!");
			}
			else {
				resp = true;		
			}
		}
		else if(player == 2) {
			//Black is playing (Horizontal moves allowed)
			if(col > this.boardSize - 2 || this.board[row][col+1] != 0) {
				System.out.println("NOT A VALID MOVE!");
			}
			else if (this.board[row][col] != 0) {
				System.out.println("Occupied play location!");
			}
			else {
				resp = true;		
			}
		}
		
		return resp;
	}
	
}
