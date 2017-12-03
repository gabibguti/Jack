package observer;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cards.Card;
import frames.player.Player;
import graphics.GameImagePanel;
import tools.Provider;
import tools.Score;
import tools.Turn;

public class ObserverCards implements Observer {

	private JPanel cardsPanel;
	private JLabel playerScore;
	private Score score;
	
	public ObserverCards() {
		cardsPanel = new JPanel();
		cardsPanel.setLayout(new BorderLayout());
		cardsPanel.setSize(500, 310);
		
		playerScore = new JLabel();
		playerScore.setSize(40, 15);
		score = new Score();
	}
	
	@Override
    public void update(Observable observable, Object arg) {
		Point imgPoint;
		int panelWidth = cardsPanel.getWidth(), panelHeight = cardsPanel.getHeight(), cardWidth, cardHeight, x, y, totalCards;
		Map<Image, Point> cards_images = new HashMap<Image, Point>();
		ObservableCards cards = (ObservableCards) observable;
		Player p = (Player) cardsPanel.getTopLevelAncestor();
		
		totalCards = cards.getCards().size();						// Get total cards number
		if(totalCards > 0) {
			cardWidth = cards.getCards().get(0).getImage().getWidth();	// Get card width
			cardHeight = cards.getCards().get(0).getImage().getHeight();// Get card height
			x = panelWidth/(2 * totalCards) - cardWidth/2;				// Set first card x point on the left
			y = panelHeight/2 - cardHeight/2;							// Set all cards y point on the middle of the panel
			cardsPanel.removeAll();
			for(Card hand_card: cards.getCards()) {
				imgPoint = new Point(x, y);
				cards_images.put(hand_card.getImage(), imgPoint);		// Add card and defined point to images map
				x += panelWidth/totalCards;								// Add next card horizontal padding
			}
			cardsPanel.add(new GameImagePanel(cards_images, null));		// Add images map of cards to control panel
			
			score.UpdateScore(cards.getCards());						// Set score
			if(score.getScore() < 10) {
//				playerScore.setText("Score " + score.getScore() + " (TINY RICK!!!)");			// Set text in Score label
				playerScore.setText("Score " + score.getScore());			// Set text in Score label
			}
			else {
				playerScore.setText("Score " + score.getScore());		// Set text in Score label
			}
		}
		else {
			cardsPanel.removeAll();
			
			score.UpdateScore(cards.getCards());
			playerScore.setText("");
		}
		
		p.revalidate();				// Revalidate PlayerFrame
		
		if(score.getScore() > 21) { // Treat when player gets busted
//			JOptionPane.showMessageDialog(p, "Geez Rick. I got busted."); // Warn busted player
			JOptionPane.showMessageDialog(p, "Busted."); // Warn busted player
			p.setVisible(false); // "Close" player frame	
			
			// Update player turn
			Turn.nextPlayerTurn();
			
			Provider.updateActivePlayers();
		}
    }

	public JLabel getPlayerScore() {
		return playerScore;
	}
	
	public JPanel getCardsPanel() {
		return cardsPanel;
	}
	
	public int getScore() {
		return score.getScore();
	}

}

