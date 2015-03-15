import java.util.List;
import java.util.ArrayList;

public class Board {
	public int boardSize;
	public int[][] board;
	
	public List<String> plays1;
	public List<String> plays2;
	
	public Board(int size) {
		if(size < 6) {
			System.out.println("Minimum board size is 6! Making size equal to 6");
			size = 6;
		}		
		this.boardSize = size;
		this.board = new int[size][size];
		
		this.plays1 = new ArrayList<String>();
		this.plays2 = new ArrayList<String>();
		
		for(int r = 0 ; r < this.boardSize ; r++) 
			for(int c = 0 ; c < this.boardSize ; c++) {
					if(r < this.boardSize  - 1) {
						this.plays1.add(  Character.toString((char)(r+65)) + (c+1) );
					}
					
					if(c < this.boardSize  - 1) {
						this.plays2.add(  Character.toString((char)(r+65)) + (c+1) );
					}				
			} 						
		reset();
	}
	
	public Board(Board oldBoard) {
		this.boardSize = oldBoard.boardSize;
		this.board = new int[oldBoard.boardSize][oldBoard.boardSize];
		for(int r = 0 ; r < this.boardSize ; r++) 
			for(int c = 0 ; c < this.boardSize ; c++) {
				this.board[r][c] = oldBoard.board[r][c];				
		}
		
		this.plays1 = new ArrayList<String>();
		for(int i = 0 ; i < oldBoard.plays1.size() ; i++) {
			this.plays1.add(oldBoard.plays1.get(i));				
		}
		
		this.plays2 = new ArrayList<String>();
		for(int i = 0 ; i < oldBoard.plays2.size() ; i++) {
			this.plays2.add(oldBoard.plays2.get(i));				
		}
	}
	
	public int playsForPlayer(int player) {
		if(player%2 == 1) return this.plays1.size();
		else return this.plays2.size(); 
	}
	
	public boolean play(int turn, char rowChar, int colH) {
		int col = colH - 1;
		int row = (int)Character.toUpperCase(rowChar) - 65;
		boolean resp = false;
		if(row < 0 || row > this.boardSize - 1) {
			System.out.println("Can't play on a out-of-bounds row: " + row);
		}
		else if(col < 0 || col > this.boardSize - 1) {
			System.out.println("Can't play on a out-of-bounds column: " + col);
		}
		else if(playerCanPlay(turn, row, col)) {
				if(turn == 1) {
					this.board[row][col] = 1; 
					this.board[row+1][col] = 1; 
				} else if(turn == 2) {
					this.board[row][col] = 2; 
					this.board[row][col+1] = 2; 
				} else {
					System.out.println("1) Should not be called with parameter: " + turn);
				}
				recalculatePlaysAt(turn,row,col);
				resp = true;
		} 
		
		return resp;
	}
	
	private void recalculatePlaysAt(int state,int row,int col) {
		int rMin = 0; int rMax = 0; int cMin = 0; int cMax = 0;
	
		if(state == 1) {
			rMin = row - 1;
			if(rMin < 0) rMin = 0;
			rMax = row + 2;
			if(rMax > this.boardSize - 1) rMax = this.boardSize - 1;
			
			cMin = col - 1;
			if(cMin < 0) cMin = 0;
			cMax = col + 1;
			if(cMax > this.boardSize - 1) cMax = this.boardSize - 1;
		} else if(state == 2) {
			rMin = row - 1;
			if(rMin < 0) rMin = 0;
			rMax = row + 1;
			if(rMax > this.boardSize - 1) rMax = this.boardSize - 1;
			
			cMin = col - 1;
			if(cMin < 0) cMin = 0;
			cMax = col + 2;
			if(cMax > this.boardSize - 1) cMax = this.boardSize - 1;
		} else {
			System.out.println("2) Should not be called with parameter: " + state);
		}
		
		for(int r = rMin ; r <= rMax ; r++) 
			for(int c = cMin ; c <= cMax ; c++) {
				if(!playerCanPlay(1, r, c)) {
					this.plays1.remove(  Character.toString((char)(r+65)) + (c+1) );
				}
				if(!playerCanPlay(2, r, c)) {
					this.plays2.remove(  Character.toString((char)(r+65)) + (c+1) );
				}
		}
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
		boolean resp = false;
		if(p == 1) {
			//White is playing
			if( this.board[row][col] == 0 && row < this.boardSize - 1 && this.board[row+1][col] == 0 ) {
				resp = true;
			}
		} else if(p == 2) {
			//Black is playing (Horizontal moves allowed)
			if( this.board[row][col] == 0 && col < this.boardSize - 1 && this.board[row][col+1] == 0 ) {
				resp = true;
			}
		} else {
			System.out.println("3) Should not be called with parameter: " + p);
		}		
		return resp;
	}
	
	private int heuristicValueForBoard() {
		return this.plays1.size() - this.plays2.size();
	}
	
	public void generateAIPlay(int player) {
		System.out.println("Generating AI move for player " + player);
	}
/*	private String[] calculatePlaysForPlayer(int player) {
		ArrayList<String> plays = new ArrayList<String>();
		for(int r = 0 ; r < this.boardSize ; r++) 
			for(int c = 0 ; c < this.boardSize ; c++) {
				if(playerCanPlay(player, r, c)) {
					plays.add(  Character.toString((char)(r+65)) + (c+1) );
				} 				
		}
		return plays.toArray(new String[plays.size()]);
	} */
}
