package comp6721;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random; 

public class Board {
	
	public class Play {
	    int score;
	    String move;
	    public Play(String move, int score) {
	        this.score = score;
	        this.move = move;
	    }
	    
	    @Override
	    public String toString() {
	        return "Playing " + move + " @ h(n)= " + score;
	    }
	}
	
	public static int max_depth = 5;
	
	public int boardSize;
	public int[][] board;
	
	public List<String> plays1; //Plays on this array are ordered by columns A1,B1,C1,A2,B2,C2,etc...
	public List<String> plays2; //Plays on this array are ordered by row A1,A2,A3,B1,B2,B3,etc...
	
	public List<Play> playsAndScores;
	
	public Board(int size) {
		if(size < 6) {
			System.out.println("Minimum board size is 6! Making size equal to 6");
			size = 6;
		}		
		
		switch(size) {
			case 6:
				max_depth = 8;
				break;
			case 7:
				max_depth = 7;
				break;
			case 8:
				max_depth = 6;
				break;
			default:
				max_depth = 5;
				break;
		}
		
		this.boardSize = size;
		this.board = new int[size][size];
		
		this.plays1 = new ArrayList<String>();
		this.plays2 = new ArrayList<String>();
		
		for(int r = 0 ; r < this.boardSize ; r++) 
			for(int c = 0 ; c < this.boardSize ; c++) {
					if(c < this.boardSize  - 1) {
						this.plays1.add(  Character.toString((char)(c+65)) + (r+1) );
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
	
	private void recalculatePlaysAt(int state,int row,int c) {
		int col = c + 1;
		if(state == 1) {
			this.plays1.remove(  Character.toString((char)(row+64)) + (col) );
			this.plays1.remove(  Character.toString((char)(row+65)) + (col) );
			this.plays1.remove(  Character.toString((char)(row+66)) + (col) );
			this.plays2.remove(  Character.toString((char)(row+65)) + (col-1) );
			this.plays2.remove(  Character.toString((char)(row+65)) + (col) );
			this.plays2.remove(  Character.toString((char)(row+66)) + (col-1) );
			this.plays2.remove(  Character.toString((char)(row+66)) + (col) );
		} else {
			this.plays1.remove(  Character.toString((char)(row+64)) + (col) );
			this.plays1.remove(  Character.toString((char)(row+64)) + (col+1) );
			this.plays1.remove(  Character.toString((char)(row+65)) + (col) );
			this.plays1.remove(  Character.toString((char)(row+65)) + (col+1) );
			this.plays2.remove(  Character.toString((char)(row+65)) + (col-1) );
			this.plays2.remove(  Character.toString((char)(row+65)) + (col) );
			this.plays2.remove(  Character.toString((char)(row+65)) + (col+1) );
		}		
	}
	
	private void recalculatePlaysAt2(int state,int row,int col) {
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
	
	private int heuristicValue(boolean isMAX) {
		if(isMAX && this.plays1.size() == 0) {
			return Integer.MIN_VALUE;
		} else if(!isMAX && this.plays2.size() == 0) {
			return Integer.MAX_VALUE;
		}
		
//		int a = effectivePlaysForPlayer(1);
//		System.out.println("Effective: " + a + ". " + this.plays1);
//		int b = effectivePlaysForPlayer(2);
//		System.out.println("Effective: " + b + ". " + this.plays2);
//		if ( a-b != 0 ) return a - b;
		
//		return  a - b;
		return this.plays1.size() - this.plays2.size();
	}
	
	private int effectivePlaysForPlayer(int player) {
		int count = 0;
		if(player == 1) {
			int lim = this.plays1.size();
			int currentCol = 0;
			char currentChar = '-';
			for(int i = 0 ; i < lim ; i++) {
				String move = this.plays1.get(i);
				int nextCol = Integer.parseInt(move.substring(1));
				if(nextCol != currentCol) {
					currentCol = nextCol;
					count++;
					currentChar = move.toUpperCase().charAt(0);
				} else {
					char nextChar = move.toUpperCase().charAt(0);
					if(nextChar > currentChar + 1) {
						count++;
						currentChar = nextChar; 
					}
				}				
			}
		} else {
			int lim = this.plays2.size();
			char currentChar = '-';
			int currentCol = 0;
			for(int i = 0 ; i < lim ; i++) {
				String move = this.plays2.get(i);
				char nextChar = move.toUpperCase().charAt(0);
				if(nextChar != currentChar) {
					currentChar = nextChar;
					count++;
					currentCol = Integer.parseInt(move.substring(1));
				} else {
					int nextCol = Integer.parseInt(move.substring(1));
					if(nextCol > currentCol + 1) {
						count++;
						currentCol = nextCol; 
					}
				}				
			}
		}	
		
		return count;
	}
	
	public String randomMove(int player) {
		Random rand = new Random();
		if(player == 1) {
			int lim = this.plays1.size();
			return this.plays1.get(rand.nextInt(lim));
		} else {
			int lim = this.plays2.size();
			return this.plays2.get(rand.nextInt(lim));
		}
	}
	
	public String generateAIPlay(int player) {
		String resp = "";
		
		this.playsAndScores = new ArrayList<Play>();
		int tmp = alphaBeta(this, max_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, (player == 1) ? true : false);
		for(Play p : this.playsAndScores) {
			if( p.score == tmp ) {
				System.out.println("Playing = " + p);
				resp = p.move;
				break;
			}
		}		
		return resp;
	}
	
	public boolean isTerminal(boolean isMAX) {
		boolean resp = false;
		if(isMAX && this.plays1.size() == 0) resp = true;
		else if(!isMAX && this.plays2.size() == 0) resp = true;
	
		return resp;
	}
		
	//The actual MinMax Logic is in this function
	public int alphaBeta(Board node,int depth,int a, int b, boolean isMAX) {
		
		if (depth == 0) {
			int resp = node.heuristicValue(isMAX);
			//System.out.println("Alpha-Beta reached max depth. Returning h(n)="+resp);
			return resp;
		} else if (node.isTerminal(isMAX)) {
			int resp = node.heuristicValue(isMAX);
			//System.out.println("Alpha-Beta reached terminal node. Returning h(n)="+resp);
			return resp;
		}
		
		if(isMAX) {
			int resp = Integer.MIN_VALUE;
			for(Iterator<String> i = node.plays1.iterator(); i.hasNext(); ) {
				String move = i.next();
				char rowH = move.toUpperCase().charAt(0);
				int col = Integer.parseInt(move.substring(1));
				Board newBoard = new Board(this);
				newBoard.play(1,rowH,col);
			    resp = Math.max(resp,alphaBeta(newBoard,depth - 1,a,b, false));
			    a = Math.max(a, resp);
			    if(depth == max_depth) {
			    	this.playsAndScores.add(new Play(move,a));
			    }			    	
			    if(b <= a) break; //B-prunning!!
			}
			return resp;
		} else {
			int resp = Integer.MAX_VALUE;
			for(Iterator<String> i = node.plays2.iterator(); i.hasNext(); ) {
				String move = i.next();
				char rowH = move.toUpperCase().charAt(0);
				int col = Integer.parseInt(move.substring(1));
				Board newBoard = new Board(this);
				newBoard.play(2,rowH,col);
			    resp = Math.min(resp,alphaBeta(newBoard,depth - 1,a,b, true));
			    b = Math.min(b, resp);
			    if(depth == max_depth) {
			    	this.playsAndScores.add(new Play(move,b));
			    }
			    if(b <= a) break; //A-prunning!!
			}
			return resp;
		}
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
