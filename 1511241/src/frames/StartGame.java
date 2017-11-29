package frames;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Provider;

public class StartGame {
	public static JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel subheaderLabel;
	private JLabel statusLabel;
	private JPanel mainCP;
	private JButton[] options;
	private String[] actions;
	private int maxPlayers = 4;
	public static BufferedImage bankBackground = null;

	static public String img_path = System.getProperty("user.dir") + "/src/images/"; // Images path

	public StartGame() {
		// Prepare Guided User Interface
		prepareMainFrame();
		// Get images
		try {
			bankBackground = ImageIO.read(new File(img_path + "blackjackBKG.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareMainFrame() {
		// Create Frame
		mainFrame = new JFrame("Blackjack");
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(4, 1)); // Organize components

		// Create Labels
		headerLabel = new JLabel("", JLabel.CENTER);
		subheaderLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);
		statusLabel.setSize(350, 100);

		// Add Listener
		mainFrame.addWindowListener(Provider.windowAdapter);

		// Create Buttons Panel
		mainCP = new JPanel();
		mainCP.setLayout(new FlowLayout());

		// Add components to frame
		mainFrame.add(headerLabel);
		mainFrame.add(subheaderLabel);
		mainFrame.add(mainCP);
		mainFrame.add(statusLabel);

		// Allow us to see the frame
		mainFrame.setVisible(true);

		// Makes the frame pop up centered
		mainFrame.setLocationRelativeTo(null);
		
		showEvent();
	}

	private void showEvent() {
		int player;

		// Set header label
		headerLabel.setText("Welcome to Blackjack");
		subheaderLabel.setText("Select the number of players:");

		// Create Actions
		setActions(new String[maxPlayers]);
		for (player = 0; player < maxPlayers; player++) {
			getActions()[player] = String.valueOf(player + 1); // Set actions[0] = 1 and so on
		}

		// Create buttons
		options = new JButton[maxPlayers];
		for (player = 0; player < maxPlayers; player++) {
			options[player] = new JButton(String.valueOf(player + 1));
		}

		// Set command actions to the buttons
		for (player = 0; player < maxPlayers; player++) {
			options[player].setActionCommand(getActions()[player]);
		}
		
		// Add listeners to buttons
		for (JButton b : options) {
			b.addActionListener(new ButtonClickListener());
		}

		// Add buttons to buttons panel
		for (JButton b : options) {
			mainCP.add(b);
		}

		// Allow us to see the frame
		mainFrame.setVisible(true);
	}

	/**
	 * @return the actions
	 */
	public String[] getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(String[] actions) {
		this.actions = actions;
	}

	private class ButtonClickListener implements ActionListener {
		// Listener function
		public void actionPerformed(ActionEvent e) {
			Provider.numPlayersButtonAction(e);
		}
	}
}