
public class Game {
	
	private boolean manualMode;
	private String Player1;
	private String Player2;
	private int gridSize;
	private Board board;
	
	public Game(boolean isManual, String aPlayer1, String aPlayer2, int aSize) {
		if(!isManual) {
			System.out.println("Automated mode not yet active. Forcing manual mode");
			isManual = true;
		}
		this.manualMode = isManual;
		this.Player1 = aPlayer1;
		this.Player2 = aPlayer2;
		this.gridSize = aSize;
		this.board = new Board(this.gridSize);
	}
	
	public boolean tryPlay(int player,char rowChar, int colH) {
		int p = 2;
		if(player % 2 == 1) 
			p = 1;
		
		return this.board.play(p, rowChar, colH);
	}
	
	public int getState(int r,int c) {
		return this.board.getState(r, c);
	}
	
	public void reset() {
		this.Player1 = "";
		this.Player2 = "";
		this.board.reset();
	}
	
	public boolean playerCanPlay(int p) {
		boolean resp = false;
		int player = 2;
		if(p % 2 == 1) player = 1;
		
		String []plays = this.board.playsForPlayer(player);
		if(plays.length > 0) 
			resp = true;
		
		return resp;
	}
	
	public boolean getMode() {
		return this.manualMode;
	}
	
	public String getPlayerName(int i) {
		if(i%2 == 1) return this.Player1;
		else return this.Player2;
	}
	
	public int getSize() {
		return this.gridSize;
	}
}
