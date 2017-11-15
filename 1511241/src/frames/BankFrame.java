package frames;

import javax.swing.*;

import cards.Card;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import components.GameImage;
import etc.Chip;
import main.Main;
import main.Provider;

public class BankFrame extends JFrame {
	private Chip[] chips = new Chip[6];
	static public BankFrame bank;
	private JButton bEndGame;
	private JButton bNewRound;
	private JButton bSave;
	private JPanel pComponents;
//	private GridBagConstraints constraints;
	ArrayList<Card> cards = new ArrayList<>();
	Map<Integer, Rectangle> chips_position = new HashMap<Integer, Rectangle>();

	Score score = new Score();
	
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
//		pComponents.setLayout(new GridBagLayout());
		pComponents.setOpaque(false);										// Make transparent
		pComponents.setSize(this.getWidth(), this.getHeight() - 50);
		
		bEndGame = new JButton("End Game");
		bNewRound = new JButton("New Round");
		bSave = new JButton("Save");
		
		bNewRound.setEnabled(false);
		
		// EndGame button action listener
		bEndGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);				
			}
		});

		// NewRound action listener
		bNewRound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				BankFrame.newRoundSetEnabled(false);
				
				while(BankFrame.bank.cards.isEmpty() == false) {	// Remove all cards from the bank
					BankFrame.bank.cards.remove(0);
//					BankFrame.bank.pComponents.remove(0);	
				}
//				BankFrame.bank.pComponents.remove(0);				
				
				score.UpdateScore(cards);
				while(score.getScore() < 17) {						// Draw cards until score >= 17
					BankFrame.bank.cards.add(Provider.RemoveCardFromDeck());
					score.UpdateScore(BankFrame.bank.cards);
				}
				
				chips_position = Provider.UpdateBankHand (cards, chips, pComponents, BankFrame.this, bankBackground);
				setChipsClickListener(chips_position);
				
				for(Frame frame: Provider.framesList)
				{
					frame.setVisible(true);
				}
			}
		});
			
		// Save button action listener
		bSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent actionEvent) {
				String s = "testezão do sucesso";
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			    int retrival = fc.showSaveDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION) {
			        try {
			            FileWriter fw = new FileWriter(fc.getSelectedFile() + ".txt");
			            fw.write(s);	// TODO: Change s for file containing game info
			            fw.close();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
			    }
			}
			
		});
				
		// Add Listener
        addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent windowEvent){
              System.exit(0);
           }        
        });
        
        JPanel pButtons = new JPanel();
        // Add buttons
		pButtons.add(bEndGame);
		pButtons.add(bNewRound);
		pButtons.add(bSave);
		
		// Place buttons under image
		setLayout(new BorderLayout());
		
		// Initial cards
		score.UpdateScore(cards);
		while(score.getScore() < 17) {						// Draw cards until score >= 17
			cards.add(Provider.RemoveCardFromDeck());
			score.UpdateScore(cards);
		}
		
		chips_position = Provider.UpdateBankHand (cards, chips, pComponents, BankFrame.this, bankBackground);
		setChipsClickListener(chips_position);
		
		add(pComponents);	// Add chips and cards to bank frame
		
		pComponents.setOpaque(true);
		
		add(pButtons, BorderLayout.PAGE_END);	// Add game buttons to bank frame
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
	
	public static void createBank(String name, BufferedImage bankBackground) {
		bank = new BankFrame(name, bankBackground);
	}

	private void setChipsClickListener (Map<Integer, Rectangle> chips_position) {
		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	Point me = e.getPoint();
		    	for(java.util.Map.Entry<Integer, Rectangle> entry : chips_position.entrySet())
		    	{
		    		Integer chip = entry.getKey();
		        	Rectangle bounds = entry.getValue();
	            	if(bounds.contains(me))
	            	{
	            		System.out.println("Uh! Mo-Morty! Ah wa what are you doin' here?");
	            		System.out.println("I-I wanted the chip " + chip + " Rick");
	            	}
		    		
		    	}
	        }
		});
	}
	
	static void newRoundSetEnabled(boolean bool) {
		bank.bNewRound.setEnabled(bool);
	}
	
	static void newRoundSetEnabled(boolean bool, int numPlayers) {
		bank.bNewRound.setEnabled(bool);
		PlayerFrame.activePlayers = numPlayers;
	}
	
	static int getBankScore() {
		return BankFrame.bank.score.getScore();
	}
}
