package frames;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import cards.Card;
import components.GameImagePanel;

import main.*;

public class PlayerFrame extends JFrame {

	int centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	int centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	int gap = 4;
	public static int activePlayers = 0;
	public static int numPlayers = 0;
	public ArrayList<Card> cards = new ArrayList<>();
	public JPanel buttonsPanel;
	public JPanel cardsPanel;
	public JPanel infoPanel;
	public JLabel playerScore;
	public Score totalScore = new Score();
	public GroupLayout layout;

	public PlayerFrame(String playerNumber, Container cont) {
		super("Player " + playerNumber);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(Provider.playerFrameClosing);
		
		activePlayers++; // Add active player
		setSize(400, 350);
		layout = new GroupLayout(getContentPane()); // Organize layout by groups
		getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true); 

		// Create Panel
		buttonsPanel = new JPanel();
		cardsPanel = new JPanel();
		infoPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		cardsPanel.setLayout(new GridBagLayout());
		infoPanel.setLayout(new FlowLayout());
		
		// Create player score label
		playerScore = new JLabel("");
		playerScore.setSize(this.getWidth(), 15);
				
		// Create button
		JButton hitButton = new JButton("Hit");
		JButton standButton = new JButton("Stand");

		// hitButton listener
		hitButton.addActionListener(Provider.hitButtonListener);

		// standButton listener
		standButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				hitButton.setEnabled(false);
				if(totalScore.getScore() > BankFrame.getBankScore()) { 
					JOptionPane.showMessageDialog(null, "Wubba lubba dub dub! I WON MORTY!"); // Warn bursted player
				}
				PlayerFrame.this.setVisible(false); // "Close" player frame
				// Reset players frame
				cardsPanel.removeAll();
				// Reinitialize cards panel
				// TODO: Review because is taking cards from actual deck and not new deck	
				cards.clear();
				Provider.RequestNewCard(cards, cardsPanel, PlayerFrame.this);
				Provider.RequestNewCard(cards, cardsPanel, PlayerFrame.this);	
				totalScore.UpdateScore(cards);
				playerScore.setText("Score: " + totalScore.getScore());
				ReloadLayout();
				hitButton.setEnabled(true);
				activePlayers--;
				if(activePlayers == 0)
					BankFrame.newRoundSetEnabled(true, PlayerFrame.numPlayers);
			}
		});

		
		// Add button to buttons panel
		buttonsPanel.add(hitButton);
		buttonsPanel.add(standButton);
		
		// Initial cards
		Provider.RequestNewCard(cards, cardsPanel, PlayerFrame.this);
		Provider.RequestNewCard(cards, cardsPanel, PlayerFrame.this);		
		
		// Initial score
		totalScore.UpdateScore(cards);
		playerScore.setText("Score: " + totalScore.getScore());
		
		// Add player score label to info panel
		infoPanel.add(playerScore);

		// Add components to frame
		ReloadLayout();
		
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
	
	public void ReloadLayout () {
		layout.setHorizontalGroup( // All panels in horizontal group
		    layout.createSequentialGroup()
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        	.addComponent(buttonsPanel)
		        	.addComponent(cardsPanel)
		        	.addComponent(infoPanel))
		);
			
		layout.setVerticalGroup( // Separate panel in vertical group
			layout.createSequentialGroup()
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(buttonsPanel))
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(cardsPanel))
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	        		.addComponent(infoPanel))
	    );
		
	}
}
