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
import etc.ObservableDemo;
import etc.ObserverExample;
import main.Provider;

@SuppressWarnings("serial")
public class PlayerFrame extends JFrame {

	int centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	int centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	int gap = 4;
	private int bet = 0;
	private int money;
	private int initialAmount = 500;
	private int playerNumber;
	private ArrayList<Card> cards = new ArrayList<>();
	private JPanel buttonsPanel;
	private JPanel cardsPanel;
	private JPanel infoPanel;
	private JLabel playerScore;
	private JLabel playerBet;
	private JLabel playerMoney;
	private JButton hitButton;
	private JButton standButton;
	private JButton doubleButton;
	private JButton surrenderButton;
	private JButton betButton;
	private Score totalScore = new Score();
	public ObservableDemo observable;
	
	public static int activePlayers = 0;
	public static int numPlayers = 0;

	public PlayerFrame(String playerNumber, Container cont) {
		super("Player " + playerNumber);
		this.setPlayerNumber(Integer.parseInt(playerNumber));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(Provider.playerFrameClosing);
		
		// Add observer
		observable = new ObservableDemo(0, getPlayerBet());
	    ObserverExample observer = new ObserverExample();
	    observable.addObserver(observer);
		
		activePlayers++; // Add active player
		setSize(500, 350);
		setLayout(new BorderLayout());

		// Set initial amount of money
		setMoney(initialAmount);
		
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
		
		// Create player labels
		setPlayerScore(new JLabel(""));
		setPlayerBet(new JLabel(""));
		observable.setBET(0, getPlayerBet());
		getPlayerScore().setSize(40, 15);
		getPlayerBet().setSize(40, 15);
		setPlayerMoney(new JLabel(""));
		getPlayerMoney().setText("Money $" + getMoney());
				
		// Create button
		setHitButton(new JButton("Hit"));
		setStandButton(new JButton("Stand"));
		setDoubleButton(new JButton("Double"));
		setSurrenderButton(new JButton("Surrender"));
		setBetButton(new JButton("Bet"));
		
		// hitButton listener
		getHitButton().addActionListener(Provider.hitButtonListener);

		// standButton listener
		getStandButton().addActionListener(Provider.standButtonListener);
		
		// doubleButton listener
		getDoubleButton().addActionListener(Provider.doubleButtonListener);
		
		// surrenderButton listener
		getSurrenderButton().addActionListener(Provider.surrenderButtonListener);
		
		// betButton listener
		getBetButton().addActionListener(Provider.betButtonListener);
		
		// Add button to buttons panel
		getButtonsPanel().add(getHitButton());
		getButtonsPanel().add(getStandButton());
		getButtonsPanel().add(getDoubleButton());
		getButtonsPanel().add(getSurrenderButton());
		getButtonsPanel().add(getBetButton());
		
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
		getInfoPanel().add(getPlayerBet());
		getInfoPanel().add(getPlayerMoney());

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
	 * @return the bet
	 */
	public int getBet() {
		return bet;
	}

	/**
	 * @param bet the bet to set
	 */
	public void setBet(int bet) {
		this.bet = bet;
	}

	/**
	 * @return the playerNumber
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * @param playerNumber the playerNumber to set
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
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

	/**
	 * @return the playerBet
	 */
	public JLabel getPlayerBet() {
		return playerBet;
	}

	/**
	 * @param playerBet the playerBet to set
	 */
	public void setPlayerBet(JLabel playerBet) {
		this.playerBet = playerBet;
	}

	/**
	 * @return the playerMoney
	 */
	public JLabel getPlayerMoney() {
		return playerMoney;
	}

	/**
	 * @param playerMoney the playerMoney to set
	 */
	public void setPlayerMoney(JLabel playerMoney) {
		this.playerMoney = playerMoney;
	}

	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return the doubleButton
	 */
	public JButton getDoubleButton() {
		return doubleButton;
	}

	/**
	 * @param doubleButton the doubleButton to set
	 */
	public void setDoubleButton(JButton doubleButton) {
		this.doubleButton = doubleButton;
	}

	/**
	 * @return the surrenderButton
	 */
	public JButton getSurrenderButton() {
		return surrenderButton;
	}

	/**
	 * @param surrenderButton the surrenderButton to set
	 */
	public void setSurrenderButton(JButton surrenderButton) {
		this.surrenderButton = surrenderButton;
	}

	/**
	 * @return the betButton
	 */
	public JButton getBetButton() {
		return betButton;
	}

	/**
	 * @param betButton the betButton to set
	 */
	public void setBetButton(JButton betButton) {
		this.betButton = betButton;
	}
}
