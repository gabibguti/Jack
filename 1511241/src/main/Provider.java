package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	public static ArrayList<Frame> framesList = new ArrayList<Frame>();
	
	   static public void RequestNewCard (ArrayList<Card> hand, JPanel controlPanel, JFrame frame) // Provides new card for player or bank
	   {
		   	Point imgPoint;
			int x, y;
			int width;
			Map<Image, Point> cards_images = new HashMap<Image, Point>();

			Card card;
			width = controlPanel.getWidth();
			try {
				card = Main.deck.remove(0); // Remove card from deck
				hand.add(card); // Add card to hand
				x = width/(2 * hand.size()) - card.getImage().getWidth()/2;
				y = controlPanel.getHeight()/2 - card.getImage().getHeight()/2;
				controlPanel.removeAll(); // Clear control panel
				for(Card hand_card: hand)
				{
					imgPoint = new Point(x, y);
					cards_images.put(hand_card.getImage(), imgPoint);
					x += width/hand.size();
				}
				controlPanel.add(new GameImagePanel(cards_images)); // Add card image to control panel
				frame.revalidate(); // Update frame
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Deck ended. Cannot request any more cards.");
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
