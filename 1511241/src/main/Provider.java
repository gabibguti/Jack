package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cards.Card;
import components.GameImagePanel;
import etc.Buy;
import etc.BuyFrame;
import etc.Chip;
import etc.Turn;
import frames.BankFrame;
import frames.PlayerFrame;

public class Provider {
	public static int initialAmount = 500;
	public static ArrayList<JFrame> framesList = new ArrayList<JFrame>();
	
	public static void createBank(String name, BufferedImage bankBackground) {
		BankFrame.bank = new BankFrame(name, bankBackground);
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

		Provider.createBank("Bank", Main.bankBackground); // Create Bank
		Provider.framesList.add(BankFrame.bank); // Add Bank to framesList
		
		numberOfPlayers = Integer.parseInt(command);
		
		for (player = 0; player < numberOfPlayers; player++)
			Provider.framesList.add(new PlayerFrame(String.valueOf(player + 1), BankFrame.bank)); // Create Player Frame and add to framesList
		
		Turn.firstTurn(numberOfPlayers);
		
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
		p.dispose();
	}
	
	public static WindowAdapter playerFrameClosing = new WindowAdapter() { // Remove Player from game on closing window
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			Provider.closePlayer((PlayerFrame) windowEvent.getSource());
		}
	};
	
	public static PlayerFrame currentPlayerFrame () { // Gets current player frame
		int currentPlayer = Turn.currentPlayerTurn();
		PlayerFrame frame = (PlayerFrame) Provider.framesList.get(currentPlayer);
		return frame;
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
			
			//Provider.RequestNewCard(p.getCards(), p.getCardsPanel(), p); // Hit
			p.addCard();
			
//			updateScore(p);

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
				
				//Provider.RequestNewCard(p.getCards(), p.getCardsPanel(), p); // Hit
				p.addCard();
	
//				updateScore(p);
				
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
			
			// Enable player actions after player bets
			if(p.getBet() != 0) {
				configurePlayerActions(p, true, true, true, true, false); // Disable bet
				
				BankFrame.bank.disableChipsClickListener(); // Disable chips bet
				
				// Get new cards
				p.getCards().clear();
				p.addCard();
				p.addCard();
			}
			else {
				JOptionPane.showMessageDialog(p, "You have to bet some money!"); // Warn bet = 0
			}
		}
	};
	
	public static void notifyWinnersAndLosers() {
		int bankScore = getBankScore();
		int reward = 0;
		
		if(bankScore > 21) { // Bank busts
			JOptionPane.showMessageDialog(null, "Bank busted. Every remaining player wins!");
			bankScore = 0;
		}
		
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class && frame.isVisible() == true) {
				PlayerFrame p = (PlayerFrame) frame;
				int playerScore = p.getScore();
				if(playerScore == 21) { // Player wins with Blackjack
					JOptionPane.showMessageDialog(p, "You don't have to try to impress me, Morty.");
					reward = p.getBet()*5/2;
				}
				else {
					if(playerScore > bankScore) { // Player wins
						JOptionPane.showMessageDialog(p, "Wubba lubba dub dub! I WON MORTY!");
						reward = p.getBet()*2;
					}
					else if(playerScore == bankScore) { // Player and Bank ties
						JOptionPane.showMessageDialog(p, "Next round SHOW ME WHAT YOU GOT!");
						reward = p.getBet();
					}
					else { // Player loses
						JOptionPane.showMessageDialog(p, "You're young, you have your whole life ahead of you, and your anal cavity is still taut yet malleable."); // Warn loser
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

			while(BankFrame.bank.getCards().isEmpty() == false) {	// Remove all cards from the bank
				BankFrame.bank.getCards().remove(0);
			}
			
			BankFrame.bank.setScore(BankFrame.bank.getCards());
			
			// Add first card and flipped card
			BankFrame.bank.getCards().add(Provider.RemoveCardFromDeck());
			BankFrame.bank.getCards().add(Card.flippedCard);
			
			// Draw BankFrame
			BankFrame.bank.setelements_position(Provider.UpdateBankHand (BankFrame.bank.getCards(),
																	 BankFrame.bank.getChips(),
																	 BankFrame.bank.getBuyCredit(),
																	 BankFrame.bank.getpComponents(),
																	 BankFrame.bank,
																	 Main.bankBackground));
			
			// Remove flipped card
			BankFrame.bank.getCards().remove(1);
			
			// Update score
			BankFrame.bank.setScore(BankFrame.bank.getCards());
			
			while(BankFrame.bank.getScore() < 17) {						// Draw cards until score >= 17
				BankFrame.bank.getCards().add(Provider.RemoveCardFromDeck());
				BankFrame.bank.setScore(BankFrame.bank.getCards());
			}
			
			
			// Removing broken players
			removeBrokenPlayers();
			
			startNextRound();
			
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
		for(Frame frame : Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				
				// Reset players frame
				p.clearCards();
				
				// Reset bet
				p.setBet(0);
				
				// Update frame
				p.repaint();
				
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
                		int playerBetting = Turn.currentPlayerTurn();
                		PlayerFrame p = (PlayerFrame) Provider.framesList.get(playerBetting);
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
			String s = "testezão do sucesso";
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		    int retrival = fc.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) {
		        try {
		            FileWriter fw = new FileWriter(fc.getSelectedFile() + ".txt");
		            fw.write(s);	// TODO: Change s for file containing game info
		            fw.close();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		}
	};

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
	
	static public Map<Integer, Rectangle> UpdateBankHand(ArrayList<Card> hand, Chip[] chips, Buy buyCredit, JPanel controlPanel,
			JFrame frame, Image background) { // Update bank frame redrawing all components
		Point imgPoint;
		int panelWidth = controlPanel.getWidth(), panelHeight = controlPanel.getHeight(), cardWidth, cardHeight,
				chipWidth = 0, chipHeight = 0, buyWidth = buyCredit.getImage().getWidth(), buyHeight = buyCredit.getImage().getHeight(),
				x = 0, y = 0, totalCards = hand.size(), totalChips = chips.length;
		boolean firstTime = true;
		Map<Image, Point> images = new HashMap<Image, Point>();
		Map<Integer, Rectangle> images_bounds = new HashMap<Integer, Rectangle>();

		controlPanel.removeAll(); // Clear control panel
		
		// Add cards
		for (Card hand_card : hand) {
			if (firstTime) { // For first card define:
				cardWidth = hand_card.getImage().getWidth();
				cardHeight = hand_card.getImage().getHeight();
				x = panelWidth / (2 * totalCards) - cardWidth / 2; // Set first card x point on the left
				y = panelHeight / 3 - cardHeight / 2; // Set all cards y point on 1/3 of the panel
				firstTime = false;
			}
			imgPoint = new Point(x, y);
			images.put(hand_card.getImage(), imgPoint); // Add card and defined point to images map
			x += panelWidth / totalCards; // Add next card horizontal padding
		}
		
		// Add chips
		firstTime = true;
		for (Chip chip : chips) {
			if (firstTime) { // For first chip define:
				chipWidth = chip.getImage().getWidth();
				chipHeight = chip.getImage().getHeight();
				x = panelWidth / (2 * (totalChips + 1)) - chipWidth / 2; // Set first chip x point on the left
				y = 5 * panelHeight / 6 - chipHeight / 2; // Set all chips y point on 5/6 of the panel
				firstTime = false;
			}
			imgPoint = new Point(x, y);
			images_bounds.put(chip.getValue(), 
							 new Rectangle(new Point(x + 10, y + 29),
										   new Dimension(chipWidth,
														 chipHeight))); // Add chip value and defined position to the chip bounds map
			images.put(chip.getImage(), imgPoint); // Add chip and defined point to images map
			x += panelWidth / (totalChips + 1); // Add next chip horizontal padding
		}
		
		// Add buy credit option
		y = 5 * panelHeight / 6 - buyHeight / 2; // Set all chips y point on 5/6 of the panel
		imgPoint = new Point(x, y);
		images_bounds.put(0, 
				 new Rectangle(new Point(x + 10, y + 29),
							   new Dimension(buyWidth,
											 buyHeight))); // Add chip value and defined position to the chip bounds map
		images.put(buyCredit.getImage(), imgPoint); // Add chip and defined point to images map
		
		// Update bank
		controlPanel.add(new GameImagePanel(images, background)); // Add cards and chips images to control panel
		frame.revalidate(); // Update frame
		controlPanel.setOpaque(false); // Set opaque to see background

		return images_bounds;
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
			
			// Update bank frame
			Provider.UpdateBankHand (BankFrame.bank.getCards(),
									 BankFrame.bank.getChips(),
									 BankFrame.bank.getBuyCredit(),
									 BankFrame.bank.getpComponents(),
									 BankFrame.bank,
									 Main.bankBackground);
			
			Provider.notifyWinnersAndLosers();
		}
		else {
			Turn.updatePlayerFrameTurn();
		}
	}
}
