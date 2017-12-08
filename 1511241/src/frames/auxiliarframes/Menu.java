package frames.auxiliarframes;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import facade.Facade;
import frames.bank.Bank;
import frames.player.Player;
import tools.Provider;
import tools.Turn;

public class Menu {
	private JLabel headerLabel;
	private JLabel subheaderLabel;
	private JPanel mainCP;
	private JButton newGame;
	private JButton loadGame;

	public static JFrame menuFrame;
	
	public Menu() {
		// Prepare Guided User Interface
		prepareMenuFrame();
	}

	private void prepareMenuFrame() {
		// Create Frame
		menuFrame = new JFrame("Blackjack");
		menuFrame.setSize(400, 400);
		menuFrame.setLayout(new GridLayout(4, 1)); // Organize components

		// Create Labels
		headerLabel = new JLabel("", JLabel.CENTER);
		subheaderLabel = new JLabel("", JLabel.CENTER);
		
		// Create buttons
		newGame = new JButton("New Game");
		loadGame = new JButton("Load Game");

		// Add Listener
		menuFrame.addWindowListener(Provider.windowAdapter);

		// Create Buttons Panel
		mainCP = new JPanel();
		mainCP.setLayout(new FlowLayout());

		// Add components to frame
		menuFrame.add(headerLabel);
		menuFrame.add(subheaderLabel);
		menuFrame.add(mainCP);

		// Allow us to see the frame
		menuFrame.setVisible(true);

		// Makes the frame pop up centered
		menuFrame.setLocationRelativeTo(null);
	}

	/**
	 * 
	 */
	private void showEvent() {
		// Set labels
		headerLabel.setText("BlackJack");
		subheaderLabel.setText("Select an option:");
		
		// Add listeners to buttons
		newGame.addActionListener(new ActionListener() { // Add ok listener event
			public void actionPerformed(ActionEvent actionEvent) {
				new StartGame();
				menuFrame.dispose();
			}
		});

		loadGame.addActionListener(new ActionListener() { // Add ok listener event
			public void actionPerformed(ActionEvent actionEvent) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(null); 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                System.out.println("Opening: " + file.getName() + ".");
	                try {
	                	BufferedReader br = new BufferedReader(new FileReader(file));
	                	int numplayers = Integer.valueOf(br.readLine());
	                	
	                	// Getting playerTurn
	                	String turnString = br.readLine();
	                	turnString = turnString.substring(1, turnString.length()-1);           //remove curly brackets
	                	String[] strings = turnString.split(",");              //split the string to creat key-value pairs
	                	Map<Integer, Integer> map = new HashMap<>();   

	                	for(String pair : strings){                        //iterate over the pairs
	                	    String[] entry = pair.split("=");                   //split the pairs to get key and value 
	                	    map.put(Integer.valueOf(entry[0].trim()), Integer.valueOf(entry[1].trim()));          //add them to the hashmap and trim whitespaces
	                	}
	                	
	                	Facade.restartGame(numplayers, map);
	                	
	                	// Get number of active players
	                	String activePlayersString = br.readLine();
	                	
	                	// Setting players info
	                	String line;
	                	for(JFrame frame : Provider.framesList){
	                		if(frame.getClass() == Player.class){
	                			Player p = (Player) frame;
	                			
	                			// Set bet, money and insurance
	                			line = br.readLine();
		                		String[] playerStrings = line.split(" ");
		                		p.setMoney(Integer.valueOf(playerStrings[1]));
		                		p.setBet(Integer.valueOf(playerStrings[2]));
		                		if(Integer.valueOf(playerStrings[2]) != 0) {
		                			Player.bets++;
		                		}
		                		p.setInsured(Boolean.valueOf(playerStrings[3]));
		                		
		                		// Set cards
		                		line = br.readLine();
		                		if (!line.isEmpty()) {
			                		playerStrings = line.split(" ");
			                		for(String s : playerStrings){
			                			p.addCard(s);
			                		}
		                		}
	                		}
	                	}
	                	
	                	// Reset turns, activePlayers and check end of round
	                	Player.activePlayers = Integer.valueOf(activePlayersString);
	                	Turn.setTurn(map);
	                	Provider.checkRound();
	                	
	                	// Setting bank info
	                	line = br.readLine(); // BANK
	                	line = br.readLine(); // New round enabled?
	                	Bank.bank.getbNewRound().setEnabled(Boolean.valueOf(line));
	                	
	                	line = br.readLine(); // Cards
	                	if (!line.isEmpty()) {
		                	String[] bankCards = line.split(" ");
	                		for(String s : bankCards){
	                			if(s.equals("flipped")){
	                				Bank.bank.addFlippedCard();
	                			}
	                			else{
	                				Bank.bank.addCard(s);
	                			}
	                		}
	                	}
	                	
	                	if(Player.bets != Player.numPlayers) {
	                		JOptionPane.showMessageDialog(null, "Make your bets.");
	                	}
	                	
	                    br.close();
	                }
	                catch(Exception e) {
	                	e.printStackTrace();
	                }
	                menuFrame.dispose();
	            } else {
	            	System.out.println("Open command cancelled by user.");
	            }
			}
		});
		
		// Add buttons to buttons panel
		mainCP.add(newGame);
		mainCP.add(loadGame);
		
		// Allow us to see the frame
		menuFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		// Open Menu
		Menu m = new Menu();
		m.showEvent();
	}
}