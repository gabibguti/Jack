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
	public Chip[] chips = new Chip[6];
	static public BankFrame bank;

	public JButton bEndGame;
	public JButton bNewRound;
	public JButton bSave;
	public JPanel pComponents;
	public JPanel pButtons;
	public ArrayList<Card> cards = new ArrayList<>();
	public Map<Integer, Rectangle> chips_position = new HashMap<Integer, Rectangle>();
	public Score score = new Score();
	
	{
		chips[0] = new Chip(1);
		chips[1] = new Chip(5);
		chips[2] = new Chip(10);
		chips[3] = new Chip(20);
		chips[4] = new Chip(50);
		chips[5] = new Chip(100);
	}
	
	public BankFrame(String name, BufferedImage bankBackground) {
		super(name);
		
		setLayout(new BorderLayout()); // Organize components
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));

		pComponents = new JPanel();
		pComponents.setLayout(new BorderLayout());
		pComponents.setOpaque(false);										// Make transparent
		pComponents.setSize(this.getWidth(), this.getHeight() - 50);
		
		bEndGame = new JButton("End Game");
		bNewRound = new JButton("New Round");
		bSave = new JButton("Save");
		
		bNewRound.setEnabled(false);
		
		// EndGame button action listener
		bEndGame.addActionListener(Provider.endGameListener);

		// NewRound action listener
		bNewRound.addActionListener(Provider.newRoundListener);
			
		// Save button action listener
		bSave.addActionListener(Provider.saveListener);
			
		// Add Listener
        addWindowListener(Provider.windowAdapter);
        
        pButtons = new JPanel();
        // Add buttons
		pButtons.add(bEndGame);
		pButtons.add(bNewRound);
		pButtons.add(bSave);
		
		// Place buttons under image
		setLayout(new BorderLayout());
		
		// Add first card and flipped card
		cards.add(Provider.RemoveCardFromDeck());
		cards.add(Card.flippedCard);
		
		// Draw BankFrame
		chips_position = Provider.UpdateBankHand (cards,
												  chips,
												  pComponents,
												  BankFrame.this,
												  bankBackground);
		
		// Remove flipped card
		cards.remove(1);
		
		// Initial cards
		score.UpdateScore(cards);
		while(score.getScore() < 17) {						// Draw cards until score >= 17
			cards.add(Provider.RemoveCardFromDeck());
			score.UpdateScore(cards);
		}
		
		setChipsClickListener();
		
		add(pComponents);	// Add chips and cards to bank frame
		
		pComponents.setOpaque(true);
		
		add(pButtons, BorderLayout.PAGE_END);	// Add game buttons to bank frame
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
	
	public void setChipsClickListener () {
		addMouseListener(Provider.chipsClicked);
	}
}
