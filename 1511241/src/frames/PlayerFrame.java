package frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

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
	public int bet = 0;
	public int playerNumber;
	public static int activePlayers = 0;
	public static int numPlayers = 0;
	private ArrayList<Card> cards = new ArrayList<>();
	private JPanel buttonsPanel;
	private JPanel cardsPanel;
	private JPanel infoPanel;
	private JLabel playerScore;
	private JLabel playerBet;
	private JButton hitButton;
	private JButton standButton;
	private Score totalScore = new Score();

	public PlayerFrame(String playerNumber, Container cont) {
		super("Player " + playerNumber);
		this.playerNumber = Integer.parseInt(playerNumber);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(Provider.playerFrameClosing);
		
		activePlayers++; // Add active player
		setSize(500, 350);
		setLayout(new BorderLayout());

		// Create Panel
		setButtonsPanel(new JPanel());
		setCardsPanel(new JPanel());
		setInfoPanel(new JPanel());
		getButtonsPanel().setLayout(new FlowLayout());
		getCardsPanel().setLayout(new BorderLayout());
		getInfoPanel().setLayout(new FlowLayout(FlowLayout.TRAILING));
		getButtonsPanel().setSize(500, 20);
		getCardsPanel().setSize(500, 310);
		getInfoPanel().setSize(500, 20);
		
		// Create player score label
		setPlayerScore(new JLabel(""));
		playerBet = new JLabel("$ " + bet);
		getPlayerScore().setSize(40, 15);
		playerBet.setSize(40, 15);
				
		// Create button
		setHitButton(new JButton("Hit"));
		setStandButton(new JButton("Stand"));

		// hitButton listener
		getHitButton().addActionListener(Provider.hitButtonListener);

		// standButton listener
		getStandButton().addActionListener(Provider.standButtonListener);
		
		// Add button to buttons panel
		getButtonsPanel().add(getHitButton());
		getButtonsPanel().add(getStandButton());
		
		// Initial cards
		Provider.RequestNewCard(getCards(), getCardsPanel(), PlayerFrame.this);
		Provider.RequestNewCard(getCards(), getCardsPanel(), PlayerFrame.this);		
		
		// Initial score
		getTotalScore().UpdateScore(getCards());
		if(getTotalScore().getScore() < 10)
			getPlayerScore().setText("Score: " + getTotalScore().getScore() + " (TINY RICK!!!)");
		else
			getPlayerScore().setText("Score: " + getTotalScore().getScore());

		
		// Add player score label to info panel
		getInfoPanel().add(getPlayerScore());
		getInfoPanel().add(playerBet);

		// Add components to frame
		this.add(getButtonsPanel(), BorderLayout.PAGE_START);
		this.add(getCardsPanel(), BorderLayout.CENTER);
		this.add(getInfoPanel(), BorderLayout.PAGE_END);
		
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

	/**
	 * @return the cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * @return the buttonsPanel
	 */
	public JPanel getButtonsPanel() {
		return buttonsPanel;
	}

	/**
	 * @param buttonsPanel the buttonsPanel to set
	 */
	public void setButtonsPanel(JPanel buttonsPanel) {
		this.buttonsPanel = buttonsPanel;
	}

	/**
	 * @return the cardsPanel
	 */
	public JPanel getCardsPanel() {
		return cardsPanel;
	}

	/**
	 * @param cardsPanel the cardsPanel to set
	 */
	public void setCardsPanel(JPanel cardsPanel) {
		this.cardsPanel = cardsPanel;
	}

	/**
	 * @return the infoPanel
	 */
	public JPanel getInfoPanel() {
		return infoPanel;
	}

	/**
	 * @param infoPanel the infoPanel to set
	 */
	public void setInfoPanel(JPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

	/**
	 * @return the playerScore
	 */
	public JLabel getPlayerScore() {
		return playerScore;
	}

	/**
	 * @param playerScore the playerScore to set
	 */
	public void setPlayerScore(JLabel playerScore) {
		this.playerScore = playerScore;
	}

	/**
	 * @return the hitButton
	 */
	public JButton getHitButton() {
		return hitButton;
	}

	/**
	 * @param hitButton the hitButton to set
	 */
	public void setHitButton(JButton hitButton) {
		this.hitButton = hitButton;
	}

	/**
	 * @return the standButton
	 */
	public JButton getStandButton() {
		return standButton;
	}

	/**
	 * @param standButton the standButton to set
	 */
	public void setStandButton(JButton standButton) {
		this.standButton = standButton;
	}

	/**
	 * @return the totalScore
	 */
	public Score getTotalScore() {
		return totalScore;
	}
	
//	public void bet(int value) {
//		bet += value; // Add value to player's bet amount
//		playerBet.setText("$ " + bet);
//	}
//	
//	public void resetBet() {
//		bet = 0; // Add value to player's bet amount
//		playerBet.setText("$ " + bet);
//	}

}
