package etc;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import frames.PlayerFrame;
import main.Provider;

public class Turn {
	static int totalPlayers;
	public static Map<Integer, Integer> playerTurn = new HashMap<Integer, Integer>();
	
	public static void firstTurn(int numberOfPlayers) { // Start first turn starting with player 1
		int player;
		totalPlayers = numberOfPlayers;
		for(player = 1; player < numberOfPlayers + 1; player++)
			playerTurn.put(player, player);
		updatePlayerFrameTurn();
	}

	public static int nextPlayerTurn() { // Call next player to play
	   int nextPlayer = 1;
	   if(totalPlayers == 1)
		   return nextPlayer;
	   System.out.println(playerTurn);
	   for (java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) {
        	Integer player = e.getKey();
        	Integer turn = e.getValue();
        	if(turn == 1)
        		turn = playerTurn.size() + 1;
        	else if(turn == 2)
        		nextPlayer = player;
        	playerTurn.replace(player, turn - 1);
       }
	   System.out.println(playerTurn);
       updatePlayerFrameTurn();
       return nextPlayer;
	}

	public static int currentPlayerTurn() { // Gets current player playing
		   int currentPlayer = 1;
		   if(totalPlayers == 1)
			   return currentPlayer;
	       for (java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) {
	        	Integer player = e.getKey();
	        	Integer turn = e.getValue();
	        	if(turn == 1)
	        		currentPlayer = player;
	       }
	       return currentPlayer;
	}

	public static void removePlayer(int playerNumber) { // Remove player from turns and update frames
		int removedTurn;
		if(playerNumber < 1 || playerNumber > 4) {
			JOptionPane.showMessageDialog(null, "Invalid player number."); // Invalid player
			return;
		}
		if(playerTurn.get(playerNumber) == null) {
			JOptionPane.showMessageDialog(null, "Player is not playing anymore."); // Invalid player
			return; // Don't have this player
		}
		if(playerTurn.size() == 1) {
			JOptionPane.showMessageDialog(null, "Last player on game removed! Ending game..."); // Last Player
			System.exit(0); // Leave game
		}
		removedTurn = playerTurn.get(playerNumber);
		playerTurn.remove(playerNumber);
		System.out.println(removedTurn);
	    for (java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) {
	     	Integer player = e.getKey();
	    	Integer turn = e.getValue();
	    	if(turn > removedTurn)
	    		playerTurn.replace(player, turn - 1);
	    }
	    updatePlayerFrameTurn();
	}
	
	static void updatePlayerFrameTurn() { // Enable actions for current player playing
		int currentPlayer = currentPlayerTurn();
		for(java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) { // Disable all players
	     	Integer player = e.getKey();
	    	disablePlayer(player);
		}
		enablePlayer(currentPlayer); // Enable current player
	}
	
	static public void disablePlayer(int playerNumber) { // Disable player actions
		for(Frame frame: Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				if(p.playerNumber == playerNumber)
				{
					for(Object child: p.buttonsPanel.getComponents())
					{
					    JButton b = (JButton) child;
					    b.setEnabled(false);
					}
				}
			}
		}
	}

	static public void enablePlayer(int playerNumber) { // Enable player actions
		for(Frame frame: Provider.framesList) {
			if(frame.getClass() == PlayerFrame.class) {
				PlayerFrame p = (PlayerFrame) frame;
				if(p.playerNumber == playerNumber)
				{
					for(Object child: p.buttonsPanel.getComponents())
					{
					    JButton b = (JButton) child;
					    b.setEnabled(true);
					}
				}
			}
		}
	}
}
