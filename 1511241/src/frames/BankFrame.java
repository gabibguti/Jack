package frames;

import javax.swing.*;

import cards.Card;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import components.GameImage;
import etc.Chip;
import main.Main;
import main.Provider;

public class BankFrame extends JFrame {
	private Chip[] chips = new Chip[6];
	static public BankFrame bank;
	public JButton bEndGame;
	public JButton bNewRound;
	public JButton bSave;
	public JPanel pComponents;
	public JPanel pButtons;
	public GridBagConstraints constraints;
	public ArrayList<Card> cards = new ArrayList<>();
	public ArrayList<JLabel> cardsLabels = new ArrayList<>();
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
		
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));
		
		// Add Listener
        addWindowListener(Provider.windowAdapter);
        
        pButtons = new JPanel();
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
		

		// Draw components images
		pComponents = new JPanel();
		pComponents.setLayout(new GridBagLayout());
		pComponents.setOpaque(false);										// Make transparent
		constraints = new GridBagConstraints();								// Defining new constraints
		constraints.fill = GridBagConstraints.HORIZONTAL;					// Insert images horizontally
		constraints.gridy = 0;												// Insert in the first row
		constraints.insets= new Insets(220, 0, 0, 15);						// Padding inside panel layout
		// Cards
		int i = 0;
		for(Card c : cards) {
			constraints.gridx = i+2;										// Insert in the ith column
			Icon icon = new ImageIcon(c.getImage());
			JLabel lb = new JLabel(icon);									// Create Label with image
			pComponents.add(lb, constraints);								// Add to panel
			i++;
		}
		score.UpdateScore(cards);
		//Chips
		constraints.gridy = 1;												// Insert in the second row
		for(i = 0; i < chips.length; i++) {
			constraints.gridx = i;											// Insert in the ith column
			Icon icon = new ImageIcon(chips[i].getImage());
			JLabel lb = new JLabel(icon);									// Create Label with image
			lb.addMouseListener(new ChipClickListener(chips[i].getValue()));
			pComponents.add(lb, constraints);								// Add to panel
		}
		
		add(pComponents);	// Add chips and cards to bank frame
		
		add(pButtons, BorderLayout.PAGE_END);	// Add game buttons to bank frame
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
	
	private class ChipClickListener implements MouseListener {

		int value;
		public ChipClickListener(int value) {
			this.value = value;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(value);

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
