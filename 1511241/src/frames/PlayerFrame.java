package frames;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import cards.Card;
import components.GameImage;
import components.GameImagePanel;
import main.Main;
import main.Main.*;

public class PlayerFrame extends JFrame {
	
	int centerX = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x;
	int centerY = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y;
	int  gap = 4;
	ArrayList<Card> cards = new ArrayList<>();
	JPanel playerCP;
	JPanel cardsPanel;

	  
	public PlayerFrame(String playerNumber, Container cont) {
		super("Player " + playerNumber);
				
		setSize(220, 300);
		setLayout(new GridLayout(2, 1)); // Organize components
		
		// Create Panel
	    playerCP = new JPanel();
	    cardsPanel = new JPanel();
	    playerCP.setLayout(new FlowLayout());
	    cardsPanel.setLayout(new GridLayout(1, 1));
	    
	    // Create button
	    JButton newCardButton = new JButton("New Card");
	    
	    // newCardButton listener
	    newCardButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent actionEvent) {
				Card card = Main.deck.remove(0);	// Remove card from deck
				cards.add(card);	// Add card to hand
				cardsPanel.add(new GameImagePanel(card.getImage()));	// Add card image to player panel
				PlayerFrame.this.add(cardsPanel); 	// Add cards panel
				SwingUtilities.updateComponentTreeUI((JFrame) PlayerFrame.this);	// Update frame				
			}
	    	
	    });
	    
		// Add button to panel
		playerCP.add(newCardButton);

		// Add components to frame
		add(playerCP);
		add(cardsPanel);

		// Allow us to see the frame
		setVisible(true);
		
		// Frame location
		switch(Integer.parseInt(playerNumber)){
      	case 1:
    	  setLocation(centerX - cont.getWidth()/2 - gap - getWidth(), centerY - cont.getHeight()/2); // TODO: Adjust to better view
    	  break;
      	case 2:
      	  setLocation(centerX - cont.getWidth()/2 - gap - getWidth(), centerY + cont.getHeight()/2 - getHeight()); // TODO: Adjust to better view
      	  break;
      	case 3:
    	  setLocation(centerX + cont.getWidth()/2 + gap, centerY - cont.getHeight()/2); // TODO: Adjust to better view
    	  break;
      	case 4:
    	  setLocation(centerX + cont.getWidth()/2 + gap, centerY + cont.getHeight()/2 - getHeight()); // TODO: Adjust to better view
    	  break;
      	default:
      		setLocationRelativeTo(null);	  
      }
	}
}
