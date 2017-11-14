package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cards.Card;
import components.GameImagePanel;
import frames.BankFrame;
import frames.PlayerFrame;

public class Provider {
	
	public static ArrayList<Frame> framesList = new ArrayList<Frame>();
	
	private static GridBagConstraints cards_constraints = new GridBagConstraints(); // Cards Layout for Player Frame

	{
		cards_constraints.fill = GridBagConstraints.HORIZONTAL; // Set horizontal Layout
		cards_constraints.gridy = 0; // Set to first line
		cards_constraints.insets = new Insets(10, 10, 10, 10); // Add Layout insets padding
	}
	
	public static WindowAdapter windowAdapter = new WindowAdapter() {
		public void windowClosing(WindowEvent windowEvent) {
			System.exit(0);
		}
	};
	
	public static WindowAdapter playerFrameClosing = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			Provider.framesList.remove(windowEvent.getSource());
			PlayerFrame.activePlayers--;
			PlayerFrame.numPlayers--;
			if (PlayerFrame.activePlayers == 0)
				BankFrame.newRoundSetEnabled(true, PlayerFrame.numPlayers);
			((Window) windowEvent.getSource()).dispose();
		}
	};
	
	public static ActionListener hitButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			JButton b = (JButton) actionEvent.getSource();
			PlayerFrame p = (PlayerFrame) b.getParent().getParent().getParent().getParent().getParent();
			Provider.RequestNewCard(p.cards, p.cardsPanel, p); // Hit
			p.ReloadLayout();
			p.totalScore.UpdateScore(p.cards); // Update score
			p.playerScore.setText("Score: " + p.totalScore.getScore());
			if(p.totalScore.getScore() > 21) { // Treat when player gets bursted
				JOptionPane.showMessageDialog(null, "Geez Rick. I got bursted."); // Warn bursted player
				p.setVisible(false); // "Close" player frame
				// Reset players frame
				p.cardsPanel.removeAll();
				// Reinitialize cards panel
				// TODO: Review because is taking cards from actual deck and not new deck	
				p.cards.clear();
				Provider.RequestNewCard(p.cards, p.cardsPanel, p);
				Provider.RequestNewCard(p.cards, p.cardsPanel, p);	
				p.totalScore.UpdateScore(p.cards);
				p.playerScore.setText("Score: " + p.totalScore.getScore());
				p.ReloadLayout();
				PlayerFrame.activePlayers--;
				if(PlayerFrame.activePlayers == 0)
					BankFrame.newRoundSetEnabled(true, PlayerFrame.numPlayers);
			}
		}
	};
	
	public static ActionListener standButtonListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			JButton b = (JButton) actionEvent.getSource();
			PlayerFrame p = (PlayerFrame) b.getParent().getParent().getParent().getParent().getParent();
//			p.hitButton.setEnabled(false); 		//TODO: Solve this line	hitButton = null)
			if(p.totalScore.getScore() > BankFrame.getBankScore()) { 
				JOptionPane.showMessageDialog(null, "Wubba lubba dub dub! I WON MORTY!"); // Warn bursted player
			}
			p.setVisible(false); // "Close" player frame
			// Reset players frame
			p.cardsPanel.removeAll();
			// Reinitialize cards panel
			// TODO: Review because is taking cards from actual deck and not new deck	
			p.cards.clear();
			Provider.RequestNewCard(p.cards, p.cardsPanel, p);
			Provider.RequestNewCard(p.cards, p.cardsPanel, p);	
			p.totalScore.UpdateScore(p.cards);
			p.playerScore.setText("Score: " + p.totalScore.getScore());
			p.ReloadLayout();
//			p.hitButton.setEnabled(true);		TODO: Solve this line (hitButton = null)
			PlayerFrame.activePlayers--;
			if(PlayerFrame.activePlayers == 0)
				BankFrame.newRoundSetEnabled(true, PlayerFrame.numPlayers);
		}
	};

	static public void RequestNewCard (ArrayList<Card> hand, JPanel controlPanel, JFrame frame) // Provides new card for player or bank
	   {
			Card card;
			try { 
				card = Main.deck.remove(0);																// Remove card from deck
				cards_constraints.gridx = hand.size();													// Set horizontal card position on Layout
				hand.add(card);																			// Add card to hand
				Icon icon = new ImageIcon(card.getImage());												// Create Icon with card image
				JLabel card_label = new JLabel(icon);													// Create Label with Icon
				controlPanel.add(card_label, cards_constraints);										// Add Label to Panel
				frame.revalidate();		
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Deck ended. Cannot request any more cards.");	// Show error message when deck is runs out of cards
			}	   
	   }

	static public Card RemoveCardFromDeck() {
		Card card;
		try {
			card = Main.deck.remove(0); // Remove card from deck
			return card;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Deck ended. Cannot request any more cards.");	// Show error message when deck is runs out of cards
			return null;
		}
	}
	
}
