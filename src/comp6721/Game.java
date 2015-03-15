package comp6721;

public class Game {
	
	private boolean manualMode;
	private String Player1;
	private String Player2;
	private int gridSize;
	private Board board;
	public int turn;
	
	private String AIMove;

	public Game(boolean isManual, String aPlayer1, String aPlayer2, int aSize) {
		this.manualMode = isManual;
		this.Player1 = aPlayer1;
		this.Player2 = aPlayer2;
		this.gridSize = aSize;
		this.board = new Board(this.gridSize);
		this.turn = 1;
		this.AIMove = "";
	}
	
	public boolean tryPlay(char rowChar, int colH) {	
		boolean resp = this.board.play(turn, rowChar, colH);
		if(resp) {
			this.turn = (this.turn % 2 == 0 ) ? 1 : 2;
		}
		return resp;
	}
	
	public String getAMove() {
		return this.board.randomMove(this.turn);
	}
	
	public String getAIMove() {
		return this.AIMove;
	}
	
	public int getState(int r,int c) {
		return this.board.getState(r, c);
	}
	
	public void reset() {
		this.Player1 = "";
		this.Player2 = "";
		this.board.reset();
		this.turn = 1;
	}
	
	public boolean currentPlayerCanPlay() {
		boolean resp = false;	
		if(this.board.playsForPlayer( this.turn ) > 0) 
			resp = true;

		return resp;
	}
	
	public boolean isManual() {
		return this.manualMode;
	}
	
	public String currentPlayer() {
		if( this.turn == 1 ) {
			return Player1;
		} else {
			return Player2;
		} 
	}
	
	public String previousPlayer() {
		if( this.turn == 1 ) {
			return Player2;
		} else {
			return Player1;
		} 
	}
	
	public String colorForPlayer(int i) {
		if(i%2 == 1) return "WHITE";
		else return "BLACK";
	}
	
	public int getSize() {
		return this.gridSize;
	}
	
	public void playAI() {
        if(!this.manualMode) {
    		this.AIMove = "";
        	System.out.print("Generating AI move... ");
        	
    		if(this.Player1.equals("COMPUTER") && this.turn == 1 ) {
    			this.AIMove = this.board.generateAIPlay(1);
    		} else if(this.Player2.equals("COMPUTER") && this.turn == 2 ) {
    			this.AIMove = this.board.generateAIPlay(2);
    		}
    		
    		System.out.println("Done! (" + this.AIMove + ")");
        }
	}
}
