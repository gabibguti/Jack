package frames;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cards.Card;
import components.GameImage;
import etc.Chip;
import main.Provider;

@SuppressWarnings("serial")
public class BankFrame extends JFrame {
	private Chip[] chips = new Chip[6];
	private JButton bEndGame;
	private JButton bNewRound;
	private JButton bSave;
	private JPanel pComponents;
	private JPanel pButtons;
	private ArrayList<Card> cards = new ArrayList<>();
	private Map<Integer, Rectangle> chips_position = new HashMap<Integer, Rectangle>();
	private Score score = new Score();
	
	static public BankFrame bank;
	
	{
		getChips()[0] = new Chip(1);
		getChips()[1] = new Chip(5);
		getChips()[2] = new Chip(10);
		getChips()[3] = new Chip(20);
		getChips()[4] = new Chip(50);
		getChips()[5] = new Chip(100);
	}
	
	public BankFrame(String name, BufferedImage bankBackground) {
		super(name);
		
		setLayout(new BorderLayout()); // Organize components
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));

		setpComponents(new JPanel());
		getpComponents().setLayout(new BorderLayout());
		getpComponents().setOpaque(false);										// Make transparent
		getpComponents().setSize(this.getWidth(), this.getHeight() - 50);
		
		setbEndGame(new JButton("End Game"));
		setbNewRound(new JButton("New Round"));
		setbSave(new JButton("Save"));
		
		getbNewRound().setEnabled(false);
		
		// EndGame button action listener
		getbEndGame().addActionListener(Provider.endGameListener);

		// NewRound action listener
		getbNewRound().addActionListener(Provider.newRoundListener);
			
		// Save button action listener
		getbSave().addActionListener(Provider.saveListener);
			
		// Add Listener
        addWindowListener(Provider.windowAdapter);
        
        setpButtons(new JPanel());
        // Add buttons
		getpButtons().add(getbEndGame());
		getpButtons().add(getbNewRound());
		getpButtons().add(getbSave());
		
		// Place buttons under image
		setLayout(new BorderLayout());
		
		// Add first card and flipped card
		getCards().add(Provider.RemoveCardFromDeck());
		getCards().add(Card.flippedCard);
		
		// Draw BankFrame
		setChips_position(Provider.UpdateBankHand (getCards(),
												  getChips(),
												  getpComponents(),
												  BankFrame.this,
												  bankBackground));
		
		// Remove flipped card
		getCards().remove(1);
		
		// Initial cards
		setScore(getCards());
		while(getScore() < 17) {						// Draw cards until score >= 17
			getCards().add(Provider.RemoveCardFromDeck());
			setScore(getCards());
		}
		
//		enableChipsClickListener();
		
		add(getpComponents());	// Add chips and cards to bank frame
		
		getpComponents().setOpaque(true);
		
		add(getpButtons(), BorderLayout.PAGE_END);	// Add game buttons to bank frame
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
	
	public void enableChipsClickListener () {
		addMouseListener(Provider.chipsClicked);
	}
	
	public void disableChipsClickListener () {
		// FIXME: Don't need to remove all mouse listeners, only chipsClicked
//		removeMouseListener(Provider.chipsClicked);
		for(java.awt.event.MouseListener m: getMouseListeners()) {
			removeMouseListener(m);
		}
	}

	/**
	 * @return the chips
	 */
	public Chip[] getChips() {
		return chips;
	}

	/**
	 * @return the bEndGame
	 */
	public JButton getbEndGame() {
		return bEndGame;
	}

	/**
	 * @param bEndGame the bEndGame to set
	 */
	public void setbEndGame(JButton bEndGame) {
		this.bEndGame = bEndGame;
	}

	/**
	 * @return the bNewRound
	 */
	public JButton getbNewRound() {
		return bNewRound;
	}

	/**
	 * @param bNewRound the bNewRound to set
	 */
	public void setbNewRound(JButton bNewRound) {
		this.bNewRound = bNewRound;
	}

	/**
	 * @return the bSave
	 */
	public JButton getbSave() {
		return bSave;
	}

	/**
	 * @param bSave the bSave to set
	 */
	public void setbSave(JButton bSave) {
		this.bSave = bSave;
	}

	/**
	 * @return the pComponents
	 */
	public JPanel getpComponents() {
		return pComponents;
	}

	/**
	 * @param pComponents the pComponents to set
	 */
	public void setpComponents(JPanel pComponents) {
		this.pComponents = pComponents;
	}

	/**
	 * @return the pButtons
	 */
	public JPanel getpButtons() {
		return pButtons;
	}

	/**
	 * @param pButtons the pButtons to set
	 */
	public void setpButtons(JPanel pButtons) {
		this.pButtons = pButtons;
	}

	/**
	 * @return the cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * @return the chips_position
	 */
	public Map<Integer, Rectangle> getChips_position() {
		return chips_position;
	}

	/**
	 * @param chips_position the chips_position to set
	 */
	public void setChips_position(Map<Integer, Rectangle> chips_position) {
		this.chips_position = chips_position;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score.getScore();
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(ArrayList<Card> cards) {
		score.UpdateScore(cards);
	}

}
