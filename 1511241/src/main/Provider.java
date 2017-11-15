package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Array;
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
import etc.Chip;
import frames.BankFrame;

public class Provider {
	public static ArrayList<Frame> framesList = new ArrayList<Frame>();
	
	   static public void RequestNewCard (ArrayList<Card> hand, JPanel controlPanel, JFrame frame) // Provides new card for player
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
	   
	   static public Map<Integer, Rectangle> UpdateBankHand (ArrayList<Card> hand, Chip[] chips, JPanel controlPanel, JFrame frame, Image background) // Update bank frame redrawing all components
	   {
		   	Point imgPoint;
			int panelWidth = controlPanel.getWidth(), panelHeight = controlPanel.getHeight(), cardWidth, cardHeight, chipWidth, chipHeight, x = 0, y = 0, totalCards = hand.size(), totalChips = chips.length;
			boolean firstTime = true;
			Map<Image, Point> cardsNchips_images = new HashMap<Image, Point>();
			Map<Integer, Rectangle> chips_bounds = new HashMap<Integer, Rectangle>();
			
			controlPanel.removeAll();																// Clear control panel
			for(Card hand_card: hand)																
			{
				if(firstTime)																		// For first card define:
				{
					cardWidth = hand_card.getImage().getWidth();
					cardHeight = hand_card.getImage().getHeight();
					x = panelWidth/(2 * totalCards) - cardWidth/2;									// Set first card x point on the left
					y = panelHeight/3 - cardHeight/2;												// Set all cards y point on 1/3 of the panel
					firstTime = false;
				}
				imgPoint = new Point(x, y);
				cardsNchips_images.put(hand_card.getImage(), imgPoint);								// Add card and defined point to images map
				x += panelWidth/totalCards;															// Add next card horizontal padding
			}
			firstTime = true;
			for(Chip chip: chips)
			{
				if(firstTime)																		// For first chip define:
				{
					chipWidth = chip.getImage().getWidth();
					chipHeight = chip.getImage().getHeight();
					x = panelWidth/(2 * totalChips) - chipWidth/2;									// Set first chip x point on the left
					y = 5*panelHeight/6 - chipHeight/2;												// Set all chips y point on 5/6 of the panel
					firstTime = false;
				}
				imgPoint = new Point(x, y);
				chips_bounds.put(chip.getValue(), new Rectangle(imgPoint, new Dimension(chip.getImage().getWidth(), chip.getImage().getHeight()))); // Add chip value and defined position to the chip bounds map
				cardsNchips_images.put(chip.getImage(), imgPoint);									// Add chip and defined point to images map
				x += panelWidth/totalChips;															// Add next chip horizontal padding						
			}
			controlPanel.add(new GameImagePanel(cardsNchips_images, background));					// Add cards and chips images to control panel
			frame.revalidate();																		// Update frame
			controlPanel.setOpaque(false);															// Set opaque to see background
			
			return chips_bounds;
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
