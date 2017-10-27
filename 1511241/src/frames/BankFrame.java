package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import components.GameImage;

public class BankFrame extends JFrame {
	public BankFrame(String name, BufferedImage bankBackground) {
		super(name);
		
		setLayout(new BorderLayout()); // Organize components
		
		JButton bEndGame = new JButton("End Game");
		JButton bNewRound = new JButton("New Round");
		JButton bSave = new JButton("Save");
		
		// EndGame button action listener
		bEndGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);				
			}
		});
		
		setSize(bankBackground.getWidth(), bankBackground.getHeight());
		setContentPane(new GameImage(bankBackground));
		
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
		add(pButtons, BorderLayout.SOUTH);
		
        // Allow us to see the frame
        setVisible(true);
        
        // Makes the frame pop up centered
        setLocationRelativeTo(null);
	}
}
