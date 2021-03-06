package tools;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import frames.player.Player;

public class Turn {
	static int totalPlayers;
	public static Map<Integer, Integer> playerTurn = new HashMap<Integer, Integer>();
	
	public static String mapTrack() {
		return playerTurn.toString();
	}
	
	public static void setTurn(Map<Integer, Integer> turns) { // Sets the turns
		for (java.util.Map.Entry<Integer, Integer> e : turns.entrySet()) {
			Integer player = e.getKey();
        	Integer turn = e.getValue();
			playerTurn.put(player, turn);
		}
		Turn.updatePlayerTurn();
	}
	
	public static void firstTurn(int numberOfPlayers) { // Start first turn starting with player 1
		int player;
		totalPlayers = numberOfPlayers;
		for(player = 1; player < numberOfPlayers + 1; player++)
			playerTurn.put(player, player);
		disablePlayer(1);
		updatePlayerTurn();
	}

	public static int nextPlayerTurn() { // Call next player to play
	   int nextPlayer = 1;
	   if(totalPlayers == 1)
		   return nextPlayer;
	   for (java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) {
        	Integer player = e.getKey();
        	Integer turn = e.getValue();
        	if(turn == 1)
        		turn = playerTurn.size() + 1;
        	else if(turn == 2)
        		nextPlayer = player;
        	playerTurn.replace(player, turn - 1);
       }
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
			JOptionPane.showMessageDialog(null, "Player does not exist."); // Invalid player
			return; // This player does not exist
		}
		if(playerTurn.size() == 1) {
			JOptionPane.showMessageDialog(null, "Last player on game removed! Ending game..."); // Last Player
			System.exit(0); // Leave game
		}
		removedTurn = playerTurn.get(playerNumber);
		playerTurn.remove(playerNumber);
	    for (java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) {
	     	Integer player = e.getKey();
	    	Integer turn = e.getValue();
	    	if(turn > removedTurn) {
	    		playerTurn.replace(player, turn - 1);
	    	}
	    }
	}
	
	static public void updatePlayerTurn() { // Enable actions for current player playing
		int currentPlayer = currentPlayerTurn();
		for(java.util.Map.Entry<Integer, Integer> e : playerTurn.entrySet()) { // Disable all players
	     	Integer player = e.getKey();
	     	disablePlayer(player);
		}
		if(Player.activePlayers != 0) {
			enablePlayer(currentPlayer); // Enable current player	
		}
	}
	
	static public void disablePlayer(int playerNumber) { // Disable player actions
		for(Frame frame: Provider.framesList) {
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				if(p.getPlayerNumber() == playerNumber)
				{
					for(Object child: p.getButtonsPanel().getComponents())
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
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				if(p.getPlayerNumber() == playerNumber) {
					if(Player.bets < Player.numPlayers) {
						p.configurePlayerActions(false, false, false, false, true);
					}
					else {
						p.configurePlayerActions(true, true, true, true, false);
					}
				}
			}
		}
	}
}
