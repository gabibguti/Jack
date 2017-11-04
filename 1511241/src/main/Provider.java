package main;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cards.Card;
import components.GameImagePanel;
import frames.BankFrame;

public class Provider {
	public static ArrayList<Frame> framesList = new ArrayList<Frame>();
	
	   public void RequestNewCard (ArrayList<Card> hand, JPanel controlPanel, JFrame frame) // Provides new card for player or bank
	   {
			Card card;
			try { // TODO: Open window with message
				card = Main.deck.remove(0); // Remove card from deck
				hand.add(card); // Add card to hand
				controlPanel.add(new GameImagePanel(card.getImage())); // Add card image to control panel
				frame.revalidate(); // Update frame
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Deck ended. Cannot request any more cards.");
			}	   
	   }
}
