package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cards.Card;
import components.GameImagePanel;
import frames.BankFrame;

public class Provider {
	private static GridBagConstraints cards_constraints = new GridBagConstraints(); // Cards Layout for Player Frame

	public static WindowAdapter windowAdapter = new WindowAdapter() {
		public void windowClosing(WindowEvent windowEvent) {
			System.exit(0);
		}
	};
	
	{
		cards_constraints.fill = GridBagConstraints.HORIZONTAL; // Set horizontal Layout
		cards_constraints.gridy = 0; // Set to first line
		cards_constraints.insets = new Insets(10, 10, 10, 10); // Add Layout insets padding
	}

	public static ArrayList<Frame> framesList = new ArrayList<Frame>();

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
