package main;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frames.StartGame;

public class Menu {
	public static JFrame menuFrame;
	private JLabel headerLabel;
	private JLabel subheaderLabel;
//	private JLabel statusLabel;
	private JPanel mainCP;
	private JButton newGame;
	private JButton loadGame;
//	private JButton[] options;
//	private String[] actions;
//	private int maxPlayers = 4;
//	public static BufferedImage bankBackground = null;
//
//	static public String img_path = System.getProperty("user.dir") + "/src/images/"; // Images path
//
	public Menu() {
		// Prepare Guided User Interface
		prepareMenuFrame();
	}

	private void prepareMenuFrame() {
		// Create Frame
		menuFrame = new JFrame("Blackjack");
		menuFrame.setSize(400, 400);
		menuFrame.setLayout(new GridLayout(4, 1)); // Organize components

		// Create Labels
		headerLabel = new JLabel("", JLabel.CENTER);
		subheaderLabel = new JLabel("", JLabel.CENTER);
		
		// Create buttons
		newGame = new JButton("New Game");
		loadGame = new JButton("Load Game");

		// Add Listener
		menuFrame.addWindowListener(Provider.windowAdapter);

		// Create Buttons Panel
		mainCP = new JPanel();
		mainCP.setLayout(new FlowLayout());

		// Add components to frame
		menuFrame.add(headerLabel);
		menuFrame.add(subheaderLabel);
		menuFrame.add(mainCP);

		// Allow us to see the frame
		menuFrame.setVisible(true);

		// Makes the frame pop up centered
		menuFrame.setLocationRelativeTo(null);
	}

	/**
	 * 
	 */
	private void showEvent() {
		// Set header label
		headerLabel.setText("BlackJack");
		subheaderLabel.setText("Select an option:");
		
		// Add listeners to buttons
		newGame.addActionListener(new ActionListener() { // Add ok listener event
			public void actionPerformed(ActionEvent actionEvent) {
				new StartGame();
				menuFrame.dispose();
			}
		});

		loadGame.addActionListener(new ActionListener() { // Add ok listener event
			public void actionPerformed(ActionEvent actionEvent) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(null); 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would open the file.
	                System.out.println("Opening: " + file.getName() + ".");
	                try {
	                	BufferedReader br = new BufferedReader(new FileReader(file));
	                    String line;
	                    while ((line = br.readLine()) != null) {
	                    	// process the line.
	                    	System.out.println("Line: " + line);
	                    }
	                }
	                catch(Exception e) {
	                	e.printStackTrace();
	                }
	                menuFrame.dispose();
	            } else {
	            	System.out.println("Open command cancelled by user.");
	            }
			}
		});
		
		// Add buttons to buttons panel
		mainCP.add(newGame);
		mainCP.add(loadGame);
		
		// Allow us to see the frame
		menuFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		// Open Menu
		Menu m = new Menu();
		m.showEvent();
	}
}