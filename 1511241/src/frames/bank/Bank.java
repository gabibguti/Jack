package frames.bank;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cards.Card;
import facade.Facade;
import frames.auxiliarframes.BuyFrame;
import frames.player.Player;
import graphics.GameImage;
import observer.ObservableCards;
import observer.ObserverBank;
import tools.Provider;

@SuppressWarnings("serial")
public class Bank extends JFrame {
	private JButton bEndGame;
	private JButton bNewRound;
	private JButton bSave;
	private JPanel pButtons;
	private Map<Integer, Rectangle> elements_position = new HashMap<Integer, Rectangle>();
	private ObservableCards observableBank;
	private ObserverBank observerBank;
	
	static public BufferedImage bankBackground = null;
	static public String img_path = System.getProperty("user.dir") + "/src/images/"; // Images path
	static public Bank bank;
	
	public Bank(String name) {
		super(name);
		
		// Get images
		try {
			bankBackground = ImageIO.read(new File(img_path + "blackjackBKG.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout()); // Organize components
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));
		
		// Add bank observer
		observableBank = new ObservableCards();
		observerBank = new ObserverBank(this.getWidth(), this.getHeight());
		observableBank.addObserver(observerBank);

		// Add buttons
		bEndGame = new JButton("End Game");
		bNewRound = new JButton("New Round");
		bSave = new JButton("Save");
		
		bNewRound.setEnabled(false);
		
		// EndGame button action listener
		bEndGame.addActionListener(endGameListener);

		// NewRound action listener
		bNewRound.addActionListener(newRoundListener);
			
		// Save button action listener
		bSave.addActionListener(saveListener);
			
		// Add Listener
        addWindowListener(Provider.windowAdapter);
        
        pButtons = new JPanel();
        // Add buttons
		pButtons.add(bEndGame);
		pButtons.add(bNewRound);
		pButtons.add(bSave);
		
		// Place buttons under image
		setLayout(new BorderLayout());
		
		add(observerBank.getComponents());	// Add chips and cards to bank frame
		
		add(pButtons, BorderLayout.PAGE_END);	// Add game buttons to bank frame
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
	
	public void enableChipsClickListener () {
		addMouseListener(bankClickHandler);
	}
	
	public void disableChipsClickListener () {
		// FIXME: Don't need to remove all mouse listeners, only chipsClicked
//		removeMouseListener(Provider.chipsClicked);
		for(java.awt.event.MouseListener m: getMouseListeners()) {
			removeMouseListener(m);
		}
	}
	
	private static ActionListener newRoundListener = new ActionListener() { // Starts new round
		public void actionPerformed(ActionEvent actionEvent) {
			Player.activePlayers = Player.numPlayers;
			
			Bank.bank.bNewRound.setEnabled(false);

			Bank.bank.clearCards();

			Facade.newRound();
			
			Bank.bank.disableChipsClickListener();
			Bank.bank.enableChipsClickListener();
		}
	};

	public static ActionListener endGameListener = new ActionListener() { // Exit on button endGame
		public void actionPerformed(ActionEvent actionEvent) {
			System.exit(0);				
		}
	};
	
	public static void createBank(String name) {
		Bank.bank = new Bank(name);
		
		Bank.bank.observerBank.update(Bank.bank.observableBank, null);
		
		Bank.bank.enableChipsClickListener();
	}
	
	
	public static MouseAdapter bankClickHandler = new MouseAdapter() { // Handle clicks on Bank
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	Point me = e.getPoint();
	    	for(java.util.Map.Entry<Integer, Rectangle> entry : Bank.bank.elements_position.entrySet()) {
	    		Integer element = entry.getKey();
	        	Rectangle bounds = entry.getValue();
            	if(bounds.contains(me)) {
            		if(element == 0) { // Buy credit option
            			Bank.bank.disableChipsClickListener();
            			new BuyFrame ();
            		}
            		else { // Chip element
    	        		int chip = element;
    	        		Facade.bet(chip); // Update player bet with clicked chip value
            		}
            	}
	    	}
        }
	};
	
	public static ActionListener saveListener = new ActionListener() { // Saves current game
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		    int retrival = fc.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) {
		        try {
		        	FileWriter w = new FileWriter(fc.getSelectedFile() + ".txt", false);
		        	BufferedWriter fw = new BufferedWriter(w);
		        	Provider.saveGame(fw);
		            fw.close();
		        } catch (Exception ex) {
		        	// Error writing game file
		            ex.printStackTrace();
		        }
		    }
		}
	};

	public ArrayList<Card> getCards() {
		return observableBank.getCards();
	}
	
	public void addCard() {
		observableBank.addCard();
	}
	
	public void addCard(String s) {
		observableBank.addCard(s);
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
