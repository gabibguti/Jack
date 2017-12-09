package frames.player;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cards.Card;
import facade.Facade;
import frames.bank.Bank;
import observer.ObservableCards;
import observer.ObservableMoneyBet;
import observer.ObserverCards;
import observer.ObserverMoneyBet;
import tools.Provider;
import tools.Turn;

@SuppressWarnings("serial")
public class Player extends JFrame {

	private int centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	private int centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	private int gap = 4;
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
	private ObservableMoneyBet observableMoneyBet;
	private ObserverMoneyBet observerMoneyBet;
	private ObservableCards observableCards;
	private ObserverCards observerCards;
	
	public static int activePlayers = 0;
	public static int bets = 0;
	public static int numPlayers = 0;

	public Player(String playerNumber, Container cont) {
		super("Player " + playerNumber); // Frame Name
		setSize(500, 350);
		setLayout(new BorderLayout());
		
		this.initialConfigurations(playerNumber);
		
		// Add money and bet observer
		observableMoneyBet = new ObservableMoneyBet(Provider.initialAmount); // Initial bet = 0. Initial money = 500
	    observerMoneyBet = new ObserverMoneyBet(Provider.initialAmount);
	    observableMoneyBet.addObserver(observerMoneyBet);
	    
	    // Add cards observer
	    observableCards = new ObservableCards();
	    observerCards = new ObserverCards();
	    observableCards.addObserver(observerCards);
		
		activePlayers++; // Add active player
		
		// Create Panel
		buttonsPanel = new JPanel();
		infoPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setSize(500, 20);
		infoPanel.setSize(500, 20);
				
		// Create buttons
		hitButton = new JButton("Hit");
		standButton = new JButton("Stand");
		doubleButton = new JButton("Double");
		surrenderButton = new JButton("Surrender");
		betButton = new JButton("Bet");
		
		// hitButton listener
		hitButton.addActionListener(hitButtonListener);

		// standButton listener
		standButton.addActionListener(standButtonListener);
		
		// doubleButton listener
		doubleButton.addActionListener(doubleButtonListener);
		
		// surrenderButton listener
		surrenderButton.addActionListener(surrenderButtonListener);
		
		// betButton listener
		betButton.addActionListener(betButtonListener);
		
		// Add button to buttons panel
		buttonsPanel.add(hitButton);
		buttonsPanel.add(standButton);
		buttonsPanel.add(doubleButton);
		buttonsPanel.add(surrenderButton);
		buttonsPanel.add(betButton);
		
		// Add player score label to info panel
		infoPanel.add(observerCards.getPlayerScore());
		infoPanel.add(observerMoneyBet.getPlayerBet());
		infoPanel.add(observerMoneyBet.getPlayerMoney());

		// Add components to frame
		this.add(buttonsPanel, BorderLayout.PAGE_START);
		this.add(getCardsPanel(), BorderLayout.CENTER);
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
			
			this.configurePlayerActions(false, false, false, false, true);
		}
	}
	
	private void initialConfigurations(String playerNumber) {
		this.playerNumber = Integer.parseInt(playerNumber);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(playerFrameClosing);
	}

	public static WindowAdapter playerFrameClosing = new WindowAdapter() { // Remove Player from game on closing window
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			Facade.closePlayer((Player) windowEvent.getSource());
		}
	};
	
	public static ActionListener hitButtonListener = new ActionListener() { // Player draws card
		public void actionPerformed(ActionEvent actionEvent) {
			Player p = Provider.currentPlayer();
			
			// Can't double or surrender anymore
			p.configurePlayerActions(true, true, false, false, false);
			
			p.addCard();
		}
	};
	
	public static ActionListener standButtonListener = new ActionListener() { // Player stands
		public void actionPerformed(ActionEvent actionEvent) {
			Player p = Provider.currentPlayer();
			
			Facade.stand(p); // Stand
		}
	};
	
	public static ActionListener doubleButtonListener = new ActionListener() { // Player doubles down
		public void actionPerformed(ActionEvent actionEvent) {
			Player p = Provider.currentPlayer();
			
			if(p.getMoney() < p.getBet()) {
				JOptionPane.showMessageDialog(p, "You don't have enough money to double your bet");
			}
			else {
				// Double bet
				p.setMoney(p.getMoney() - p.getBet());
				p.setBet(p.getBet()*2);
				p.addCard();
				Facade.stand(p); // Stand
			}
		}
	};

	public static ActionListener surrenderButtonListener = new ActionListener() { // Player surrenders
		public void actionPerformed(ActionEvent actionEvent) {
			Player p = Provider.currentPlayer();
			
			p.configurePlayerActions(false, false, false, false, false);
			
			// Receives half bet back
			p.setMoney(p.getMoney() + p.getBet()/2);
			p.setBet(0);
						
			p.setVisible(false); // "Close" player frame
			
			Turn.nextPlayerTurn();
			
			Provider.updateActivePlayers();
		}
	};

	public static ActionListener betButtonListener = new ActionListener() { // Player bets
		public void actionPerformed(ActionEvent actionEvent) {
			Player p = Provider.currentPlayer();
			
			Bank.bank.disableChipsClickListener();
			Bank.bank.enableChipsClickListener();
			
			p.removeWindowListener(Player.playerFrameClosing);
			
			// Enable player actions after player bets
			if(p.getBet() != 0) {
				p.configurePlayerActions(false, false, false, false, false); // Disable bet
				
				Player.bets++;
				
				Turn.nextPlayerTurn();
				Turn.updatePlayerTurn();
				
				if(Player.bets == Player.numPlayers) {
					Facade.deliverCards();
				}
			}
			else {
				JOptionPane.showMessageDialog(p, "You have to bet some money!"); // Warn bet = 0
			}
		}
	};

	public void configurePlayerActions(boolean hit, boolean stand, boolean doubleDown, boolean surrender, boolean bet) {
		hitButton.setEnabled(hit);
		standButton.setEnabled(stand);
		doubleButton.setEnabled(doubleDown);
		surrenderButton.setEnabled(surrender);
		betButton.setEnabled(bet);
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
	 * @return the cards
	 */
	public ArrayList<Card> getCards() {
		return observableCards.getCards();
	}
	
	public void addCard() {
		observableCards.addCard();
	}
	
	public void addCard(String s) {
		observableCards.addCard(s);
	}
	
	public void clearCards() {
		observableCards.clearCards();
	}

	/**
	 * @return the cardsPanel
	 */
	public JPanel getCardsPanel() {
		return observerCards.getCardsPanel();
	}
	
	public JPanel getButtonsPanel() {
		return buttonsPanel;
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
