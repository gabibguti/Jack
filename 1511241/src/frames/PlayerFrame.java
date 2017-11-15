package frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;
import main.Provider;

@SuppressWarnings("serial")
public class PlayerFrame extends JFrame {

	int centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	int centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	int gap = 4;
	public static int activePlayers = 0;
	public static int numPlayers = 0;
	public ArrayList<Card> cards = new ArrayList<>();
	public JPanel buttonsPanel;
	public JPanel cardsPanel;
	public JPanel infoPanel;
	public JLabel playerScore;
	public JButton hitButton;
	public JButton standButton;
	public Score totalScore = new Score();
	public GroupLayout layout;

	public PlayerFrame(String playerNumber, Container cont) {
		super("Player " + playerNumber);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(Provider.playerFrameClosing);
		
		activePlayers++; // Add active player
		setSize(500, 350);
		setLayout(new BorderLayout());

		// Create Panel
		buttonsPanel = new JPanel();
		cardsPanel = new JPanel();
		infoPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		cardsPanel.setLayout(new BorderLayout());
		infoPanel.setLayout(new FlowLayout());
		buttonsPanel.setSize(500, 20);
		cardsPanel.setSize(500, 310);
		infoPanel.setSize(500, 20);
		
		// Create player score label
		playerScore = new JLabel("");
		playerScore.setSize(this.getWidth(), 15);
				
		// Create button
		JButton hitButton = new JButton("Hit");
		JButton standButton = new JButton("Stand");

		// hitButton listener
		hitButton.addActionListener(Provider.hitButtonListener);

		// standButton listener
		standButton.addActionListener(Provider.standButtonListener);
		
		// Add button to buttons panel
		buttonsPanel.add(hitButton);
		buttonsPanel.add(standButton);
		
		// Initial cards
		Provider.RequestNewCard(cards, cardsPanel, PlayerFrame.this);
		Provider.RequestNewCard(cards, cardsPanel, PlayerFrame.this);		
		
		// Initial score
		totalScore.UpdateScore(cards);
		if(totalScore.getScore() < 10)
			playerScore.setText("Score: " + totalScore.getScore() + " (TINY RICK!!!)");
		else
			playerScore.setText("Score: " + totalScore.getScore());

		
		// Add player score label to info panel
		infoPanel.add(playerScore);

		// Add components to frame
		this.add(buttonsPanel, BorderLayout.PAGE_START);
		this.add(cardsPanel, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.PAGE_END);
		
		// Allow us to see the frame
		setVisible(true);

		// Frame location
		switch (Integer.parseInt(playerNumber)) {
		case 1:
			setLocation(centerX - cont.getWidth() / 2 - gap - getWidth(), centerY - cont.getHeight() / 2);
			break;
		case 2:
			setLocation(centerX - cont.getWidth() / 2 - gap - getWidth(), centerY + cont.getHeight() / 2 - getHeight());
			break;
		case 3:
			setLocation(centerX + cont.getWidth() / 2 + gap, centerY - cont.getHeight() / 2);
			break;
		case 4:
			setLocation(centerX + cont.getWidth() / 2 + gap, centerY + cont.getHeight() / 2 - getHeight());
			break;
		default:
			setLocationRelativeTo(null);
		}
	}
}
