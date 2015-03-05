import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class GanjiHoDriver extends JFrame {

	/**
	 */
	private static final long serialVersionUID = 1L;
	public static final String INIT_MESSAGE = "Hello, please enter a board size and the names of the players";
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GanjiHoDriver window = new GanjiHoDriver();
				window.setTitle("Esteban Garro's Ganji-Ho Game - D1"); 
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setPreferredSize(new Dimension(700, 700));
				window.pack();
			    window.setLocationRelativeTo(null);
				window.setVisible(true);
			}
		});
	}

	public JPanel boardPanel;
	
	public JTextField sizeBox;
	public JTextField pBox1;
	public JTextField pBox2;	
	public JTextField moveBox;	
	
	public JLabel messageLabel;
	public JLabel alertLabel;
	public JButton startButton;
	
	public Game game;
	
	public int turn;
	
	public GanjiHoDriver() {	
		 this.initializeUI(); 		 
	}
	
	private class MoveBoxActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String move = moveBox.getText();
			moveBox.setText("");
			
			int m = game.getSize();
			
			//Here, decide whether the move is a valid move!
			int rowH = (int)(move.toUpperCase().charAt(0));
			if(rowH > 64 && rowH < 65+m) {
				//This is a valid letter [A-Z]
				int col = -1;
				try {
					col = Integer.parseInt(move.substring(1));
					if(col > 0 && col <= m) {
						boolean resp = false;
						//Valid move!
						resp = game.tryPlay(turn,(char)rowH,col);
						String player;
						
						if(resp) {
							//This changes the turn of the person!
							renderGame(); 
							if( !game.playerCanPlay(turn) ) {
								//GAME OVER! Player doesn't have a way to play
								player = game.getPlayerName(turn % 2);
								String previousPlayer = game.getPlayerName( (turn-1) % 2);
								
								messageLabel.setText("G A M E   O V E R  ! ! " + (((turn-1) % 2 == 1) ? " WHITE " : " BLACK ") + "WINS!!");
								alertLabel.setText("CONGRATULATIONS " + previousPlayer + "!! " + player + " does't have a way to play!");
							}
						}
						else {
							player = game.getPlayerName(turn % 2);
							alertLabel.setText("Not a valid move. " + player + ": Please, try again!");
						}
					}
					else {
						alertLabel.setText("2) End of the string should be a number between 1 and " + m);
					}
				} catch (Exception ex) {
					alertLabel.setText("1) End of the string should be a number between 1 and " + m);
				}
			} else {
				alertLabel.setText( "First character must be a valid letter [A-" + (char)(64+m) + "]!");
			}
		}
    }
	
	void renderGame() {
		displayTurn();
		removeGame();
		
		int m = this.game.getSize();
		System.out.println("Rendering Board of size: " + m);
		
        JPanel piecesPanel = new JPanel();
        piecesPanel.setLayout(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        
        piecesPanel.setPreferredSize(new Dimension(60*(m+1),60*(m+1)));
        piecesPanel.setBackground(Color.CYAN);

        ImageIcon image0 = new ImageIcon(getClass().getResource("0.png")); 
        ImageIcon image1 = new ImageIcon(getClass().getResource("1.png")); 
        ImageIcon image2 = new ImageIcon(getClass().getResource("2.png")); 
        
        for(int r = 0 ; r <= m ; r++) {
            for(int c = 0 ; c <= m ; c++) {
            	JLabel square = new JLabel();
            	int tmp;
            	
            	if(r == 0 && c != 0) tmp = 3;
            	else if (c == 0) tmp = 4;
            	else tmp = this.game.getState(r,c);
            	
            	switch(tmp) {
    			case 0:
    				square = new JLabel(image0);
    				break;
    			case 1:	
    				square = new JLabel(image1);
    				break;
    			case 2:	
    				square = new JLabel(image2);
    				break;  
    			case 3:	
    				square = new JLabel("" + c);
    				break;  
    			case 4:	
    				square = new JLabel(Character.toString((char)(r+64)));
    				break;  
    			default:
    				break;
            	}
            
            	square.setPreferredSize(new Dimension(60,60));
            	bag.fill = GridBagConstraints.HORIZONTAL;
            	bag.weightx = 0.5;
            	bag.gridx = c;
            	bag.gridy = r;
            	piecesPanel.add(square, bag);
            }
        }

               	
        this.boardPanel.add(piecesPanel, BorderLayout.CENTER);
	}
	
	void removeGame() {
		System.out.println("Removing Board");
		this.boardPanel.removeAll();
		this.boardPanel.revalidate();
		this.boardPanel.repaint();
	}
	
	void initializeUI() {	
		this.getContentPane().setLayout(new BorderLayout());
		
        JPanel topPanel = new JPanel(); 
        topPanel.setLayout(new BorderLayout());  
        topPanel.setPreferredSize(new Dimension(700, 200));
        topPanel.setBackground(Color.GREEN);
        
        this.boardPanel = new JPanel();
        this.boardPanel.setLayout(new BoxLayout(this.boardPanel, BoxLayout.Y_AXIS));
        this.boardPanel.setPreferredSize(new Dimension(700, 500));
        this.boardPanel.setBackground(Color.YELLOW);
        
        //Add all the buttons:
        String[] labels = {"Mode: ", "Grid Size: ", "White Player: ", "Black Player: ", ""};
        int numPairs = labels.length;

        //Create and populate the panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout( 0,1 ));
        buttonPanel.setPreferredSize(new Dimension(250, 200));
        buttonPanel.setBackground(Color.RED);
        
        for (int i = 0; i < numPairs; i++) {
            if(i == numPairs - 1) {
            	this.startButton = new JButton("START");
                this.startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                buttonPanel.add(this.startButton);
            }
            else { 
                JLabel l = new JLabel(labels[i],JLabel.LEFT);
                l.setBackground(Color.CYAN);
                l.setAlignmentX(Component.LEFT_ALIGNMENT);
                l.setPreferredSize(new Dimension(50, 120/numPairs));
                buttonPanel.add(l);
            	if(i>0) {
		            JTextField textField = new JTextField(10);
		            textField.setPreferredSize(new Dimension(150,120/numPairs));
		            textField.setMaximumSize( textField.getPreferredSize() );
		            l.setLabelFor(textField);
		            buttonPanel.add(textField);
		            
            		switch(i) {
            			case 1:
            				this.sizeBox = textField;
            				break;
            			case 2:	
            				this.pBox1 = textField;
            				break;
            			case 3:	
            				this.pBox2 = textField;
            				break;  
            			default:
            				break;
            		}
            	}
            }
        }
        
        topPanel.add(buttonPanel, BorderLayout.WEST);
        //Create and populate the panel.
        JPanel messagePanel = new JPanel(new BorderLayout());
        this.messageLabel  = new JLabel(INIT_MESSAGE);
        messagePanel.add(this.messageLabel,BorderLayout.NORTH);
        
        this.alertLabel  = new JLabel("");
        messagePanel.add(this.alertLabel,BorderLayout.CENTER);
        
		// Input Line
        JLabel aLabel = new JLabel("INPUT YOUR MOVE",JLabel.LEFT);
        messagePanel.add(aLabel,BorderLayout.SOUTH);
            
        this.moveBox = new JTextField();          
        this.moveBox.addActionListener(new MoveBoxActionListener());
        this.moveBox.setPreferredSize(new Dimension(300,50));
        aLabel.setLabelFor(this.moveBox);
        messagePanel.add(this.moveBox,BorderLayout.SOUTH);
        
        
        topPanel.add(messagePanel, BorderLayout.CENTER);
        this.getContentPane().add(topPanel, BorderLayout.PAGE_START);
        this.getContentPane().add(boardPanel, BorderLayout.CENTER);
        
        
        //******************* ACTION LISTENERS *****************************
        this.startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (startButton.getText().equals("START") ) {
					startButton.setText("RESTART");
					startActions();
				} else {
					startButton.setText("START");
					restartActions();
				}
			}
		});
        
        
	}
	
	public void startActions() {
		this.turn = 0;
		String player1 = this.pBox1.getText();
		String player2 = this.pBox2.getText();
		int size = 0;
		try {
			size = Integer.parseInt(this.sizeBox.getText());
		} catch (Exception e){
			alertLabel.setText("Please input a valid [6-9] board size!");
		}
		
		if(player1.equals("") || player2.equals("")){
			alertLabel.setText("You must enter the name of the players first!");
		} else if(size < 6) {
			alertLabel.setText("Please input a valid [6-9] board size!");
		} else {
			game = new Game(true,player1,player2,size);
			messageLabel.setText("WHITE: " + player1 + "   VS.  BLACK: " + player2);			
			renderGame();
		}
	}
	
	public void displayTurn() {
		this.turn++;
		if( this.turn % 2 == 1 ) {
			alertLabel.setText( this.pBox1.getText() + ", please input your move on the box below!");
		} else {
			alertLabel.setText( this.pBox2.getText() + ", please input your move on the box below!");
		} 
	}
	
	public void restartActions() {
		this.turn = 0;
		this.sizeBox.setText("");
		this.pBox1.setText("");
		this.pBox2.setText("");
		this.moveBox.setText("");	
		
		this.messageLabel.setText(INIT_MESSAGE);
		this.alertLabel.setText("");
		
		removeGame();
	}
}
