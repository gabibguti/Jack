package main;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cards.Card;
import components.GameImagePanel;
import frames.BankFrame;

public class Provider {
	   public void RequestNewCard (ArrayList<Card> hand, JPanel controlPanel, JFrame frame) // Provides new card for player or bank
	   {
			Card card;
			try { // TODO: Open window with message
				card = Main.deck.remove(0); // Remove card from deck
				hand.add(card); // Add card to hand
				controlPanel.add(new GameImagePanel(card.getImage())); // Add card image to control panel
				frame.add(controlPanel); // Add control panel to frame
				SwingUtilities.updateComponentTreeUI((JFrame) frame); // Update frame
//				frame.revalidate();
//				frame.repaint();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Deck ended. Cannot request any more cards.");
			}	   
	   }
}
