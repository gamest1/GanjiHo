package comp6721;

public class Game {
	
	private static Game instance = null;
	
	private boolean isOver;
	private boolean manualMode;
	private String Player1;
	private String Player2;
	private int gridSize;
	private Board board;
	public int turn;
	
	private String AIMove;

	public static Game getInstance() {
		if(instance == null) {
			System.out.println("Should never getInstance before calling main constructor");
		}
		return instance;
	}
	
	public Game(boolean isManual, String aPlayer1, String aPlayer2, int aSize) {
		this.manualMode = isManual;
		this.Player1 = aPlayer1;
		this.Player2 = aPlayer2;
		this.gridSize = aSize;
		this.board = new Board(this.gridSize);
		this.turn = 1;
		this.AIMove = "";
		this.isOver = false;
		instance = this;
	}
	
	public boolean tryPlay(char rowChar, int colH) {	
		boolean resp = this.board.play(turn, rowChar, colH);
		if(resp) {
			this.turn = (this.turn % 2 == 0 ) ? 1 : 2;
		}
		return resp;
	}
	
	public String getAMove() {
		if(!this.isOver)
			return this.board.randomMove(this.turn);
		
		return "";
	}
	
	public String getAIMove() {
		if(!this.isOver)
			return this.AIMove;
		
		return "";
	}
	
	public int getState(int r,int c) {
		return this.board.getState(r, c);
	}
	
	public void reset() {
		this.Player1 = "";
		this.Player2 = "";
		this.board.reset();
		this.turn = 1;
		this.isOver = false;
	}
	
	public boolean currentPlayerCanPlay() {
		boolean resp = false;	
		if(this.board.playsForPlayer( this.turn ) > 0) {
			resp = true;
		} else {
			this.isOver = true;
		}

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
	
	public void finishGame() {
		this.isOver = true;
	}
	
	public boolean isOver() {
		return this.isOver;
	}
	
	public void playAI() {
        if(!this.manualMode && !this.isOver) {
    		this.AIMove = "";
        	System.out.print("Generating AI move... ");
        	
    		if(this.Player1.equals("COMPUTER") && this.turn == 1 ) {
    			this.AIMove = this.board.generateAIPlay(1);
    		} else if(this.Player2.equals("COMPUTER") && this.turn == 2 ) {
    			this.AIMove = this.board.generateAIPlay(2);
    		}
        }
	}
}
