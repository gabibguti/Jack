package frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cards.Card;
import main.Provider;
import observer.ObservableCards;
import observer.ObservableMoneyBet;
import observer.ObserverCards;
import observer.ObserverMoneyBet;

@SuppressWarnings("serial")
public class PlayerFrame extends JFrame {

	int centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	int centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	int gap = 4;
	private int playerNumber;
	private int nBuys = 0;
	private boolean insured = false;
	private JPanel buttonsPanel;
	private JPanel infoPanel;
	private JButton hitButton;
	private JButton standButton;
	private JButton doubleButton;
	private JButton surrenderButton;
	private JButton betButton;
	public ObservableMoneyBet observableMoneyBet;
	public ObserverMoneyBet observerMoneyBet;
	public ObservableCards observableCards;
	public ObserverCards observerCards;
		
	public static int activePlayers = 0;
	public static int bets = 0;
	public static int numPlayers = 0;

	public PlayerFrame(String playerNumber, Container cont) {
		super("Player " + playerNumber);
		this.setPlayerNumber(Integer.parseInt(playerNumber));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(Provider.playerFrameClosing);
		
		// Add money and bet observer
		observableMoneyBet = new ObservableMoneyBet(Provider.initialAmount); // Initial bet = 0. Initial money = 500
	    observerMoneyBet = new ObserverMoneyBet(Provider.initialAmount);
	    observableMoneyBet.addObserver(observerMoneyBet);
	    
	    // Add cards observer
	    observableCards = new ObservableCards();
	    observerCards = new ObserverCards();
	    observableCards.addObserver(observerCards);
		
		activePlayers++; // Add active player
		setSize(500, 350);
		setLayout(new BorderLayout());
		
		// Create Panel
		setButtonsPanel(new JPanel());
		setInfoPanel(new JPanel());
		getButtonsPanel().setLayout(new FlowLayout());
		getInfoPanel().setLayout(new FlowLayout(FlowLayout.CENTER));
		getButtonsPanel().setSize(500, 20);
		getInfoPanel().setSize(500, 20);
				
		// Create buttons
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
		
		// Add player score label to info panel
		getInfoPanel().add(observerCards.getPlayerScore());
		getInfoPanel().add(observerMoneyBet.getPlayerBet());
		getInfoPanel().add(observerMoneyBet.getPlayerMoney());

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
			
			Provider.configurePlayerActions(this, false, false, false, false, true);
		}
	}

	/**
	 * @return the bet
	 */
	public int getBet() {
		return observableMoneyBet.getBet();
	}

	/**
	 * @param bet the bet to set
	 */
	public void setBet(int bet) {
		observableMoneyBet.setBet(bet);
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
		return observableCards.getCards();
	}
	
	public void addCard() {
		observableCards.addCard();
	}
	
	public void clearCards() {
		observableCards.clearCards();
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
		return observerCards.getCardsPanel();
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
	public int getScore() {
		return observerCards.getScore();
	}

	/**
	 * @return the money
	 */
	public int getMoney() {
		return observableMoneyBet.getMoney();
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(int money) {
		observableMoneyBet.setMoney(money);
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

	/**
	 * @return the nBuys
	 */
	public int getnBuys() {
		return nBuys;
	}

	/**
	 * @param nBuys the nBuys to set
	 */
	public void setnBuys(int nBuys) {
		this.nBuys = nBuys;
	}

	/**
	 * @return the insured
	 */
	public boolean isInsured() {
		return insured;
	}

	/**
	 * @param insured the insured to set
	 */
	public void setInsured(boolean insured) {
		this.insured = insured;
	}
}
