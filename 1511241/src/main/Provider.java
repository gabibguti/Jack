package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
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
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cards.Card;
import components.GameImagePanel;
import etc.Chip;
import frames.BankFrame;
import frames.PlayerFrame;

public class Provider {
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
		
		PlayerFrame.numPlayers = numberOfPlayers;

		// Close Main Frame
		mainFrame.setVisible(false);
		mainFrame.dispose();
	}
	
	public static WindowAdapter playerFrameClosing = new WindowAdapter() { // Remove Player from game on closing window
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			Provider.framesList.remove(windowEvent.getSource()); // Remove PlayerFrame from framesList
			PlayerFrame.activePlayers--;
			PlayerFrame.numPlayers--;
			if (PlayerFrame.activePlayers == 0) {
				Provider.newRoundSetEnabled(true, PlayerFrame.numPlayers);
				
				// Update bank frame
				Provider.UpdateBankHand (BankFrame.bank.cards,
										 BankFrame.bank.chips,
										 BankFrame.bank.pComponents,
										 BankFrame.bank,
										 Main.bankBackground);
				
				Provider.notifyWinnersAndLosers();
			}
			((Window) windowEvent.getSource()).dispose();
		}
	};
	
	public static ActionListener hitButtonListener = new ActionListener() { // Player draws card
		public void actionPerformed(ActionEvent actionEvent) {
			JButton b = (JButton) actionEvent.getSource();
			PlayerFrame p = (PlayerFrame) b.getTopLevelAncestor();
			Provider.RequestNewCard(p.cards, p.cardsPanel, p); // Hit

			// Update score
			p.totalScore.UpdateScore(p.cards); 
			if(p.totalScore.getScore() < 10)
				p.playerScore.setText("Score: " + p.totalScore.getScore() + " (TINY RICK!!!)");
			else
				p.playerScore.setText("Score: " + p.totalScore.getScore());
			
			if(p.totalScore.getScore() > 21) { // Treat when player gets busted
				JOptionPane.showMessageDialog(p, "Geez Rick. I got busted."); // Warn busted player
				p.setVisible(false); // "Close" player frame
				
//				// Reset player frame
//				p.cardsPanel.removeAll();
//				// Reinitialize cards panel	
//				p.cards.clear();
//				Provider.RequestNewCard(p.cards, p.cardsPanel, p);
//				Provider.RequestNewCard(p.cards, p.cardsPanel, p);	
//				p.totalScore.UpdateScore(p.cards);
//				if(p.totalScore.getScore() < 10)
//					p.playerScore.setText("Score: " + p.totalScore.getScore() + " (TINY RICK!!!)");
//				else
//					p.playerScore.setText("Score: " + p.totalScore.getScore());
				
				PlayerFrame.activePlayers--;
				if(PlayerFrame.activePlayers == 0) {
					Provider.newRoundSetEnabled(true, PlayerFrame.numPlayers);
					
					// Update bank frame
					Provider.UpdateBankHand (BankFrame.bank.cards,
											 BankFrame.bank.chips,
											 BankFrame.bank.pComponents,
											 BankFrame.bank,
											 Main.bankBackground);
					
					Provider.notifyWinnersAndLosers();
				}
			}
		}
	};
	
	public static ActionListener standButtonListener = new ActionListener() { // Player stands
		public void actionPerformed(ActionEvent actionEvent) {
			JButton b = (JButton) actionEvent.getSource();
			PlayerFrame p = (PlayerFrame) b.getTopLevelAncestor(); 
			p.hitButton.setEnabled(false);
			p.standButton.setEnabled(false);
//			p.setVisible(false); // "Close" player frame
			PlayerFrame.activePlayers--;
			if(PlayerFrame.activePlayers == 0) {
				Provider.newRoundSetEnabled(true, PlayerFrame.numPlayers);
				
				// Update bank frame
				Provider.UpdateBankHand (BankFrame.bank.cards,
										 BankFrame.bank.chips,
										 BankFrame.bank.pComponents,
										 BankFrame.bank,
										 Main.bankBackground);
				
				// Notify winners and losers
				Provider.notifyWinnersAndLosers();
			}
		}
	};
	
	public static void notifyWinnersAndLosers() {
		if(BankFrame.bank.score.getScore() > 21)
			JOptionPane.showMessageDialog(null, "Bank busted. Every remaining player wins!"); // Warn bank busted
		else {
			for(JFrame p : Provider.framesList) {
				if(p.getClass() == PlayerFrame.class) {
					if(((PlayerFrame) p).totalScore.getScore() <= 21){
						if(((PlayerFrame) p).totalScore.getScore() == 21)
							JOptionPane.showMessageDialog(p, "You don't have to try to impress me, Morty."); // Warn blackjack
						if(((PlayerFrame) p).totalScore.getScore() > Provider.getBankScore())
							JOptionPane.showMessageDialog(p, "Wubba lubba dub dub! I WON MORTY!"); // Warn winner
						else if(((PlayerFrame) p).totalScore.getScore() == Provider.getBankScore())
							JOptionPane.showMessageDialog(p, "Next round SHOW ME WHAT YOU GOT!"); // Warn tie
						else
							JOptionPane.showMessageDialog(p, "You're young, you have your whole life ahead of you, and your anal cavity is still taut yet malleable."); // Warn loser
					// TODO: #SHAKEITOFF JOptionPane.showMessageDialog(null, "Don't think about it.");
					}
				}
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

			while(BankFrame.bank.cards.isEmpty() == false) {	// Remove all cards from the bank
				BankFrame.bank.cards.remove(0);
			}
			
			BankFrame.bank.score.UpdateScore(BankFrame.bank.cards);
			
			// Add first card and flipped card
			BankFrame.bank.cards.add(Provider.RemoveCardFromDeck());
			BankFrame.bank.cards.add(Card.flippedCard);
			
			// Draw BankFrame
			BankFrame.bank.chips_position = Provider.UpdateBankHand (BankFrame.bank.cards,
																	 BankFrame.bank.chips,
																	 BankFrame.bank.pComponents,
																	 BankFrame.bank,
																	 Main.bankBackground);
			
			// Remove flipped card
			BankFrame.bank.cards.remove(1);
			
			// Update score
			BankFrame.bank.score.UpdateScore(BankFrame.bank.cards);
			
			while(BankFrame.bank.score.getScore() < 17) {						// Draw cards until score >= 17
				BankFrame.bank.cards.add(Provider.RemoveCardFromDeck());
				BankFrame.bank.score.UpdateScore(BankFrame.bank.cards);
			}
			
			for(Frame frame : Provider.framesList) {
				if(frame.getClass() == PlayerFrame.class) {
					// Reset players frame
					((PlayerFrame) frame).cardsPanel.removeAll();
					
					// Reinitialize cards panel
					((PlayerFrame) frame).cards.clear();
					Provider.RequestNewCard(((PlayerFrame) frame).cards, ((PlayerFrame) frame).cardsPanel, ((PlayerFrame) frame));
					Provider.RequestNewCard(((PlayerFrame) frame).cards, ((PlayerFrame) frame).cardsPanel, ((PlayerFrame) frame));	
					((PlayerFrame) frame).totalScore.UpdateScore(((PlayerFrame) frame).cards);
					if(((PlayerFrame) frame).totalScore.getScore() < 10)
						((PlayerFrame) frame).playerScore.setText("Score: " + ((PlayerFrame) frame).totalScore.getScore() + " (TINY RICK!!!)");
					else
						((PlayerFrame) frame).playerScore.setText("Score: " + ((PlayerFrame) frame).totalScore.getScore());
					
					// Enabling buttons
					((PlayerFrame) frame).hitButton.setEnabled(true);
					((PlayerFrame) frame).standButton.setEnabled(true);
				}
				
				frame.setVisible(true);
			}
		}
	};
	
	public static MouseAdapter chipsClicked = new MouseAdapter() { // Handle chips click
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	Point me = e.getPoint();
	    	for(java.util.Map.Entry<Integer, Rectangle> entry : BankFrame.bank.chips_position.entrySet())
	    	{
	    		Integer chip = entry.getKey();
	        	Rectangle bounds = entry.getValue();
            	if(bounds.contains(me))
            	{
            		System.out.println("Uh! Mo-Morty! Ah wa what are you doin' here?");
            		System.out.println("I-I wanted the chip " + chip + " Rick");
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
		BankFrame.bank.bNewRound.setEnabled(bool);
	}
	
	public static void newRoundSetEnabled(boolean bool, int numPlayers) {
		BankFrame.bank.bNewRound.setEnabled(bool);
		PlayerFrame.activePlayers = numPlayers;
	}
	
	public static int getBankScore() {
		return BankFrame.bank.score.getScore();
	}
	
	static public void RequestNewCard (ArrayList<Card> hand, JPanel controlPanel, JFrame frame) // Provides new card for player or bank
	   {
		   	Point imgPoint;
			int panelWidth = controlPanel.getWidth(), panelHeight = controlPanel.getHeight(), cardWidth, cardHeight, x, y, totalCards;
			Map<Image, Point> cards_images = new HashMap<Image, Point>();

			Card card = RemoveCardFromDeck();														// Remove a card from deck
			hand.add(card);																			// Add card to player's hand
			totalCards = hand.size();																// Get total cards number
			cardWidth = card.getImage().getWidth();
			cardHeight = card.getImage().getHeight();
			x = panelWidth/(2 * totalCards) - cardWidth/2;											// Set first card x point on the left
			y = panelHeight/2 - cardHeight/2;														// Set all cards y point on the middle of the panel
			controlPanel.removeAll(); 																// Clear control panel
			for(Card hand_card: hand)
			{
				imgPoint = new Point(x, y);
				cards_images.put(hand_card.getImage(), imgPoint);									// Add card and defined point to images map
				x += panelWidth/totalCards;															// Add next card horizontal padding
			}
			controlPanel.add(new GameImagePanel(cards_images, null));								// Add images map of cards to control panel
			frame.revalidate();																		// Update frame
	   }

	static public Map<Integer, Rectangle> UpdateBankHand(ArrayList<Card> hand, Chip[] chips, JPanel controlPanel,
			JFrame frame, Image background) // Update bank frame redrawing all components
	{
		Point imgPoint;
		int panelWidth = controlPanel.getWidth(), panelHeight = controlPanel.getHeight(), cardWidth, cardHeight,
				chipWidth, chipHeight, x = 0, y = 0, totalCards = hand.size(), totalChips = chips.length;
		boolean firstTime = true;
		Map<Image, Point> cardsNchips_images = new HashMap<Image, Point>();
		Map<Integer, Rectangle> chips_bounds = new HashMap<Integer, Rectangle>();

		controlPanel.removeAll(); // Clear control panel
		for (Card hand_card : hand) {
			if (firstTime) // For first card define:
			{
				cardWidth = hand_card.getImage().getWidth();
				cardHeight = hand_card.getImage().getHeight();
				x = panelWidth / (2 * totalCards) - cardWidth / 2; // Set first card x point on the left
				y = panelHeight / 3 - cardHeight / 2; // Set all cards y point on 1/3 of the panel
				firstTime = false;
			}
			imgPoint = new Point(x, y);
			cardsNchips_images.put(hand_card.getImage(), imgPoint); // Add card and defined point to images map
			x += panelWidth / totalCards; // Add next card horizontal padding
		}
		firstTime = true;
		for (Chip chip : chips) {
			if (firstTime) // For first chip define:
			{
				chipWidth = chip.getImage().getWidth();
				chipHeight = chip.getImage().getHeight();
				x = panelWidth / (2 * totalChips) - chipWidth / 2; // Set first chip x point on the left
				y = 5 * panelHeight / 6 - chipHeight / 2; // Set all chips y point on 5/6 of the panel
				firstTime = false;
			}
			imgPoint = new Point(x, y);
			chips_bounds.put(chip.getValue(),
					new Rectangle(imgPoint, new Dimension(chip.getImage().getWidth(), chip.getImage().getHeight()))); // Add chip value and defined position to the chip bounds map
			cardsNchips_images.put(chip.getImage(), imgPoint); // Add chip and defined point to images map
			x += panelWidth / totalChips; // Add next chip horizontal padding
		}
		controlPanel.add(new GameImagePanel(cardsNchips_images, background)); // Add cards and chips images to control
																				// panel
		frame.revalidate(); // Update frame
		controlPanel.setOpaque(false); // Set opaque to see background

		return chips_bounds;
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

}
