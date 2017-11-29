package frames;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import components.GameImage;
import main.Provider;
import observer.ObservableCards;
import observer.ObserverBank;

@SuppressWarnings("serial")
public class BankFrame extends JFrame {
	private JButton bEndGame;
	private JButton bNewRound;
	private JButton bSave;
	private JPanel pButtons;
	private Map<Integer, Rectangle> elements_position = new HashMap<Integer, Rectangle>();
	public ObservableCards observableBank;
	public ObserverBank observerBank;
	
	static public BankFrame bank;
	
	public BankFrame(String name, BufferedImage bankBackground) {
		super(name);
		
		setLayout(new BorderLayout()); // Organize components
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));
		
		// Add bank observer
		observableBank = new ObservableCards();
		observerBank = new ObserverBank(this.getWidth(), this.getHeight());
		observableBank.addObserver(observerBank);

		// Add buttons
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
		
		add(observerBank.getComponents());	// Add chips and cards to bank frame
		
		add(getpButtons(), BorderLayout.PAGE_END);	// Add game buttons to bank frame
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
	
	public void enableChipsClickListener () {
		addMouseListener(Provider.bankClickHandler);
	}
	
	public void disableChipsClickListener () {
		// FIXME: Don't need to remove all mouse listeners, only chipsClicked
//		removeMouseListener(Provider.chipsClicked);
		for(java.awt.event.MouseListener m: getMouseListeners()) {
			removeMouseListener(m);
		}
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

	public void addCard() {
		observableBank.addCard();
		observerBank.update(observableBank, null);
	}
	
	public void addFlippedCard() {
		observableBank.addFlippedCard();
	}
	
	public void removeFlippedCard() {
		observableBank.removeFlippedCard();
	}
	
	public void clearCards() {
		observableBank.clearCards();
	}

	/**
	 * @return the elements_position
	 */
	public Map<Integer, Rectangle> getelements_position() {
		return elements_position;
	}

	/**
	 * @param elements_position the elements_position to set
	 */
	public void setelements_position(Map<Integer, Rectangle> elements_position) {
		this.elements_position = elements_position;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return observerBank.getScore();
	}
}
