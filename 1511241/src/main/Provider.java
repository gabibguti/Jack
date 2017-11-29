package main;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cards.Card;
import frames.BankFrame;
import frames.BuyFrame;
import frames.InsuranceFrame;
import frames.PlayerFrame;
import frames.StartGame;
import tools.Turn;

public class Provider {
	public static int initialAmount = 500;
	public static ArrayList<JFrame> framesList = new ArrayList<JFrame>();
	
	public static void createBank(String name, BufferedImage bankBackground) {
		BankFrame.bank = new BankFrame(name, bankBackground);
		
		BankFrame.bank.observerBank.update(BankFrame.bank.observableBank, null);
		
		JOptionPane.showMessageDialog(null, "Make your bets.");
		
		BankFrame.bank.enableChipsClickListener();
	}
	
	public static WindowAdapter windowAdapter = new WindowAdapter() { // Exit on close window
		public void windowClosing(WindowEvent windowEvent) {
			System.exit(0);
		}
	};
	
	public static void numPlayersButtonAction(ActionEvent e) { // Initialize Bank and Players
		int player, numberOfPlayers = 0;
		JButton b = (JButton) e.getSource();
		JFrame mainFrame = (JFrame) b.getTopLevelAncestor();
		String command = e.getActionCommand();

		Provider.createBank("Bank", StartGame.bankBackground); // Create Bank
		Provider.framesList.add(BankFrame.bank); // Add Bank to framesList
		
		numberOfPlayers = Integer.parseInt(command);
		
		for (player = 0; player < numberOfPlayers; player++)
			Provider.framesList.add(new PlayerFrame(String.valueOf(player + 1), BankFrame.bank)); // Create Player Frame and add to framesList
		
		Turn.firstTurn(numberOfPlayers);
		Provider.configurePlayerActions((PlayerFrame) Provider.framesList.get(Turn.currentPlayerTurn()), false, false, false, false, true);
		
		PlayerFrame.numPlayers = numberOfPlayers;

		// Close Main Frame
		mainFrame.setVisible(false);
		mainFrame.dispose();
	}
	
	public static void closePlayer(PlayerFrame p) {
		Provider.framesList.remove(p); // Remove PlayerFrame from framesList
		Turn.removePlayer(p.getPlayerNumber());
		PlayerFrame.numPlayers--;
		Provider.updateActivePlayers();
		if(PlayerFrame.bets == PlayerFrame.numPlayers) {
			Provider.prepareBank();
		}
		p.dispose();
	}
	
	public static WindowAdapter playerFrameClosing = new WindowAdapter() { // Remove Player from game on closing window
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			Provider.closePlayer((PlayerFrame) windowEvent.getSource());
		}
	};
	
	
	// TODO: TRY NOT TO LOSE IT
	public static PlayerFrame currentPlayerFrame () { // Gets current player frame
		int currentPlayer = Turn.currentPlayerTurn();
//		PlayerFrame frame = (PlayerFrame) Provider.framesList.get(currentPlayer);
		
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				if(p.getPlayerNumber() == currentPlayer) {
					return p;
				}
			}
		}
		
		return null;
	}
	
	public static void stand(PlayerFrame p) { // Disable player actions and pass turn to next player
		configurePlayerActions(p, false, false, false, false, false);
		
		Turn.nextPlayerTurn();
		
		Provider.updateActivePlayers();
	}
	
	public static void configurePlayerActions(PlayerFrame p, boolean hit, boolean stand, boolean doubleDown, boolean surrender, boolean bet) {
		p.getHitButton().setEnabled(hit);
		p.getStandButton().setEnabled(stand);
		p.getDoubleButton().setEnabled(doubleDown);
		p.getSurrenderButton().setEnabled(surrender);
		p.getBetButton().setEnabled(bet);
	}
	
	public static ActionListener hitButtonListener = new ActionListener() { // Player draws card
		public void actionPerformed(ActionEvent actionEvent) {
			PlayerFrame p = currentPlayerFrame();
			
			// Can't double or surrender anymore
			configurePlayerActions(p, true, true, false, false, false);
			
			p.addCard();
		}
	};
	
	public static ActionListener standButtonListener = new ActionListener() { // Player stands
		public void actionPerformed(ActionEvent actionEvent) {
			PlayerFrame p = currentPlayerFrame();
			
			stand(p); // Stand
		}
	};
	
	public static ActionListener doubleButtonListener = new ActionListener() { // Player doubles down
		public void actionPerformed(ActionEvent actionEvent) {
			PlayerFrame p = currentPlayerFrame();
			
			if(p.getMoney() < p.getBet()) {
				JOptionPane.showMessageDialog(p, "You don't have enough money to double your bet");
			}
			else {
				// Double bet
				p.setMoney(p.getMoney() - p.getBet());
				p.setBet(p.getBet()*2);
				p.addCard();
				stand(p); // Stand
			}
		}
	};

	public static ActionListener surrenderButtonListener = new ActionListener() { // Player surrenders
		public void actionPerformed(ActionEvent actionEvent) {
			PlayerFrame p = currentPlayerFrame();
			
			configurePlayerActions(p, false, false, false, false, false);
			
			// Receives half bet back
			p.setMoney(p.getMoney() + p.getBet()/2);
			p.setBet(p.getBet()/2);
						
			p.setVisible(false); // "Close" player frame
			
			Turn.nextPlayerTurn();
			
			updateActivePlayers();
		}
	};

	public static ActionListener betButtonListener = new ActionListener() { // Player bets
		public void actionPerformed(ActionEvent actionEvent) {
			PlayerFrame p = currentPlayerFrame();
			
			BankFrame.bank.disableChipsClickListener();
			BankFrame.bank.enableChipsClickListener();
			
			// Enable player actions after player bets
			if(p.getBet() != 0) {
				configurePlayerActions(p, false, false, false, false, false); // Disable bet
				
				PlayerFrame.bets++;
				
				Turn.nextPlayerTurn();
				Turn.updatePlayerFrameTurn();
				
				if(PlayerFrame.bets == PlayerFrame.numPlayers) {
					Provider.prepareBank();
				}
			}
			else {
				JOptionPane.showMessageDialog(p, "You have to bet some money!"); // Warn bet = 0
			}
		}
	};
	
	public static void notifyWinnersAndLosers() {
		int bankScore;
		int reward;
		
		// Remove flipped card
		BankFrame.bank.removeFlippedCard();
		
		while(BankFrame.bank.getScore() < 17) {
			BankFrame.bank.addCard();
		}
		
		bankScore = BankFrame.bank.getScore();
		
		if(bankScore > 21) { // Bank busts
			JOptionPane.showMessageDialog(null, "Bank busted. Every remaining player wins!");
			bankScore = 0;
		}
		
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class && frame.isVisible() == true) {
				PlayerFrame p = (PlayerFrame) frame;
				int playerScore = p.getScore();
				reward = 0;
				if(playerScore == bankScore) { // Player and Bank ties
//					JOptionPane.showMessageDialog(p, "Next round SHOW ME WHAT YOU GOT!");
					JOptionPane.showMessageDialog(p, "It's a tie!");
					reward = p.getBet();
				}
				else {
					if(playerScore == 21 && p.getCards().size() == 2) { // Player wins with Blackjack
//						JOptionPane.showMessageDialog(p, "You don't have to try to impress me, Morty.");
						JOptionPane.showMessageDialog(p, "BlackJack!");
						reward = p.getBet()*5/2;
					}
					else if(playerScore > bankScore) { // Player wins
//						JOptionPane.showMessageDialog(p, "Wubba lubba dub dub! I WON MORTY!");
						JOptionPane.showMessageDialog(p, "You win!");
						reward = p.getBet()*2;
					}
					else if(bankScore == 21 && p.isInsured() == true && BankFrame.bank.getCards().size() == 2) { // Bank has Blackjack
						JOptionPane.showMessageDialog(p, "I told you the insurance was worth it!");
						reward = p.getBet();
					}
					else { // Player loses
						JOptionPane.showMessageDialog(p, "You lose!");
					}
				}
				p.setMoney(p.getMoney() + reward); // Return money reward
			}
		}
	}
	
	public static ActionListener endGameListener = new ActionListener() { // Exit on button endGame
		public void actionPerformed(ActionEvent actionEvent) {
			System.exit(0);				
		}
	};
	
	public static ActionListener newRoundListener = new ActionListener() { // Starts new round
		public void actionPerformed(ActionEvent actionEvent) {
			Provider.newRoundSetEnabled(false);

			BankFrame.bank.clearCards();

			// Removing broken players
			removeBrokenPlayers();
			
			startNextRound();
			
			
			// TODO: NEVER CALL TURN.FIRSTTURN EVER AGAIN HERE, THX
//			Turn.firstTurn(PlayerFrame.numPlayers);
			
			Turn.updatePlayerFrameTurn();
			
			BankFrame.bank.disableChipsClickListener();
			BankFrame.bank.enableChipsClickListener();
		}
	};
	
	public static void removeBrokenPlayers() {
		Iterator<JFrame> i = Provider.framesList.iterator();
		while(i.hasNext()) {
			JFrame frame = i.next(); 
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				if(p.getMoney() == 0) { // Broken player
					JOptionPane.showMessageDialog(p, "Looks like you're out of money... Bye!"); // Warn broken player
					i.remove();
					Provider.closePlayer(p);
				}
			}
		}
	}
	
	public static void startNextRound() {
		PlayerFrame.bets = 0;
		for(Frame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				
				// Reset players frame
				p.clearCards();
				
				// Reset bet
				p.setBet(0);
				
				// Update frame
				p.repaint();
				
				p.setInsured(false);
						
				Turn.updatePlayerFrameTurn();
				
				if(p.isVisible() == false) {
					p.setVisible(true);
				}
			}
		}
	}
	
	public static MouseAdapter bankClickHandler = new MouseAdapter() { // Handle chips click
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	Point me = e.getPoint();
	    	for(java.util.Map.Entry<Integer, Rectangle> entry : BankFrame.bank.getelements_position().entrySet()) {
	    		Integer element = entry.getKey();
	        	Rectangle bounds = entry.getValue();
            	if(bounds.contains(me)) {
            		if(element == 0) { // Buy credit option
            			BankFrame.bank.disableChipsClickListener();
            			new BuyFrame ();
            		}
            		else { // Chip element
                		// Update player bet with clicked chip value
    	        		int chip = element;
//                		int playerBetting = Turn.currentPlayerTurn();
//                		PlayerFrame p = (PlayerFrame) Provider.framesList.get(playerBetting);
    					PlayerFrame p = Provider.currentPlayerFrame();
    					System.out.println(p.getPlayerNumber());
    	        		if(p.getMoney() - chip >= 0) { // Check if player still has money to bet
        					p.setBet(p.getBet() + chip);
        					p.setMoney(p.getMoney() - chip); // Update money left for player
    					}
    					else {
    						JOptionPane.showMessageDialog(p, "You have no more money to bet.");
    					}
    	        	}
            	}
	    	}
        }
	};
	
	public static ActionListener saveListener = new ActionListener() { // Saves current game
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		    int retrival = fc.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) {
		        try {
		        	FileWriter w = new FileWriter(fc.getSelectedFile() + ".txt", false);
		        	BufferedWriter fw = new BufferedWriter(w);
		        	saveGame(fw);
		            fw.close();
		        } catch (Exception ex) {
		        	// Error writing game file
		            ex.printStackTrace();
		        }
		    }
		}
	};
	
	public static void saveGame(BufferedWriter fileWriter) throws Exception {
		fileWriter.write(Integer.toString(PlayerFrame.numPlayers));
		fileWriter.newLine();
		fileWriter.write(Turn.mapTrack());
		fileWriter.newLine();
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				
				fileWriter.write("P" + p.getPlayerNumber() + " ");
				fileWriter.write(p.getMoney() + " ");
				fileWriter.write(p.getBet() + " ");
				fileWriter.write(Boolean.toString(p.isInsured()));
				fileWriter.newLine();
				for(Card c : p.getCards()) {
					fileWriter.write(c.toString() + " ");
				}
				fileWriter.newLine();
			}
		}
		fileWriter.write("BANK");
		fileWriter.newLine();
		fileWriter.write(Boolean.toString(BankFrame.bank.getbNewRound().isEnabled()));
		fileWriter.newLine();
		for(Card c : BankFrame.bank.getCards()) {
			fileWriter.write(c.toString() + " ");
		}
		fileWriter.newLine();
	}

	public static void newRoundSetEnabled(boolean bool) {
		BankFrame.bank.getbNewRound().setEnabled(bool);
	}
	
	public static void newRoundSetEnabled(boolean bool, int numPlayers) {
		BankFrame.bank.getbNewRound().setEnabled(bool);
		PlayerFrame.activePlayers = numPlayers;
	}
	
	public static int getBankScore() {
		return BankFrame.bank.getScore();
	}
	
	static public Card RemoveCardFromDeck() {
		Card card;
		try {
			card = Card.Deck.remove(0); // Remove card from deck
			return card;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Deck ended. Starting new deck..."); // Show message when deck runs out of cards and new deck is started
			Card.newDeck();
			return Card.Deck.remove(0);
		}
	}
	
	static public void updateActivePlayers() {
		PlayerFrame.activePlayers--;
		if (PlayerFrame.activePlayers == 0) {
			Provider.newRoundSetEnabled(true, PlayerFrame.numPlayers);
			Provider.notifyWinnersAndLosers();
		}
		else {
			Turn.updatePlayerFrameTurn();
		}
	}
	
	static public void prepareBank() {
		// Add first card and flipped card
		BankFrame.bank.addCard();
		BankFrame.bank.addFlippedCard();
		
		if(BankFrame.bank.getScore() == 11) {
			for(JFrame frame : Provider.framesList) {
				if(frame.getClass() == PlayerFrame.class) {
					PlayerFrame p = (PlayerFrame) frame;
					
					new InsuranceFrame(p);
				}
			}
		}
		
		// Players cards
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				
				p.addCard();
				p.addCard();
			}
		}
		
		Turn.updatePlayerFrameTurn();
		
		PlayerFrame p = (PlayerFrame) Provider.framesList.get(Turn.currentPlayerTurn());
		configurePlayerActions(p, true, true, true, true, false);
		BankFrame.bank.disableChipsClickListener();
	}
}