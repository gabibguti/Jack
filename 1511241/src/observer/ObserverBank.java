package observer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import cards.Card;
import components.GameImagePanel;
import frames.BankFrame;
import main.Main;
import tools.Buy;
import tools.Chip;
import tools.Score;

public class ObserverBank implements Observer {

	private JPanel pComponents;
	private Score score;
	private Chip[] chips = new Chip[6];
	private Buy buyCredit;
	
	public ObserverBank(int width, int height) {
		pComponents = new JPanel();
		pComponents.setLayout(new BorderLayout());
		pComponents.setOpaque(false);	// Make transparent
		pComponents.setSize(width, height - 50);	// TODO: Remove -50
		
		chips[0] = new Chip(1);
		chips[1] = new Chip(5);
		chips[2] = new Chip(10);
		chips[3] = new Chip(20);
		chips[4] = new Chip(50);
		chips[5] = new Chip(100);
		
		buyCredit = new Buy();
		
		score = new Score();
	}
	
	@Override
    public void update(Observable observable, Object arg) {
		Point imgPoint;
		int panelWidth = pComponents.getWidth(), panelHeight = pComponents.getHeight(), cardWidth, cardHeight,
				chipWidth = 0, chipHeight = 0, buyWidth = buyCredit.getImage().getWidth(), buyHeight = buyCredit.getImage().getHeight(),
				x = 0, y = 0, totalChips = chips.length, totalCards;
		boolean firstTime = true;
		Map<Image, Point> images = new HashMap<Image, Point>();
		Map<Integer, Rectangle> images_bounds = new HashMap<Integer, Rectangle>();
		ObservableCards cards = (ObservableCards) observable;
		ArrayList<Card> hand = cards.getCards();
		
		pComponents.removeAll(); // Clear control panel

		if(hand == null) {
			totalCards = 0;
		}
		else {
			totalCards = hand.size();
		}
		
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
		pComponents.add(new GameImagePanel(images, Main.bankBackground)); // Add cards and chips images to control panel
		BankFrame.bank.revalidate(); // Update frame
		pComponents.setOpaque(false); // Set opaque to see background

		BankFrame.bank.setelements_position(images_bounds);
		
		score.UpdateScore(hand);
		
    }

	public JPanel getComponents() {
		return pComponents;
	}
	
	public int getScore() {
		return score.getScore();
	}
}