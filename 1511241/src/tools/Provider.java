package tools;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cards.Card;
import facade.Facade;
import frames.auxiliarframes.InsuranceFrame;
import frames.bank.Bank;
import frames.player.Player;

public class Provider {
	public static int initialAmount = 500;
	public static ArrayList<JFrame> framesList = new ArrayList<JFrame>();
	
	public static void resetPlayers() { // Reset all players hands and update player info
		Player.bets = 0;
		for(Frame frame : Provider.framesList) {
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				
				// Reset players frame
				p.clearCards();
				
				// Reset bet
				p.setBet(0);
				
				// Update frame
				p.repaint();
				
				p.setInsured(false);
						
				// Restore leaving game option
				p.addWindowListener(Player.playerFrameClosing);
				
				Turn.updatePlayerTurn();
				
				if(p.isVisible() == false) {
					p.setVisible(true);
				}
			}
		}
	}
	// BANKFRAME
	
	public static WindowAdapter windowAdapter = new WindowAdapter() { // Exit on close window
		public void windowClosing(WindowEvent windowEvent) {
			System.exit(0);
		}
	};
			// UTILITY

	static public void checkInsurance() { // Check if insurance offer is available
		if(Bank.bank.getScore() == 11) {
			for(JFrame frame : Provider.framesList) {
				if(frame.getClass() == Player.class) {
					Player p = (Player) frame;
					
					new InsuranceFrame(p);
				}
			}
		}
	}
	
	static public void deliverPlayersCards() { // Add 2 cards for each player
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				
				p.addCard();
				p.addCard();
			}
		}
	}
	
	public static Player currentPlayer () { // Returns current player frame
		int currentPlayer = Turn.currentPlayerTurn();
		
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				if(p.getPlayerNumber() == currentPlayer) {
					return p;
				}
			}
		}
		
		return null;
	}
	
	public static void notifyWinnersAndLosers() { // Check winning/losing/tie conditions and notify players
		int bankScore;
		int reward;
		int betsWon = 0;
		int pay;
		int totalBet = 0;
		
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				totalBet += p.getBet();
			}
		}
		
		// Remove flipped card
		Bank.bank.removeFlippedCard();
		
		do {
			betsWon = 0;
			pay = totalBet;
			Bank.bank.addCard();
			for(JFrame frame : Provider.framesList) {
				if(frame.getClass() == Player.class) {
					Player p = (Player) frame;
					if(Bank.bank.getScore() > p.getScore() || p.getScore() > 21) {
						pay -= p.getBet();
						betsWon += p.getBet();
					}
					else if(Bank.bank.getScore() > p.getScore()) {
						pay -= p.getBet();
					}
				}
			}
		} while(pay > betsWon);
		
		bankScore = Bank.bank.getScore();
		
		if(bankScore > 21) { // Bank busts
			JOptionPane.showMessageDialog(null, "Bank busted. Every remaining player wins!");
			bankScore = 0;
		}
		
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == Player.class && frame.isVisible() == true) {
				Player p = (Player) frame;
				int playerScore = p.getScore();
				reward = 0;
				if(playerScore == bankScore) { // Player and Bank ties
//					JOptionPane.showMessageDialog(p, "Next round SHOW ME WHAT YOU GOT!");
					JOptionPane.showMessageDialog(p, "It's a tie!");
					reward = p.getBet();
				}
				else {
					if(playerScore == 21 && p.getCards().size() == 2) { // Player wins with Blackjack
//						JOptionPane.showMessageDialog(p, "You don't have to try to impress me, Morty.");
						JOptionPane.showMessageDialog(p, "BlackJack!");
						reward = p.getBet()*5/2;
					}
					else if(playerScore > bankScore) { // Player wins
//						JOptionPane.showMessageDialog(p, "Wubba lubba dub dub! I WON MORTY!");
						JOptionPane.showMessageDialog(p, "You win!");
						reward = p.getBet()*2;
					}
					else if(bankScore == 21 && p.isInsured() == true && Bank.bank.getCards().size() == 2) { // Bank has Blackjack
						JOptionPane.showMessageDialog(p, "I told you the insurance was worth it!");
						reward = p.getBet();
					}
					else { // Player loses
						JOptionPane.showMessageDialog(p, "You lose!");
					}
				}
				p.setMoney(p.getMoney() + reward); // Return money reward
			}
		}
	}
	
	// PLAYERFRAME
			
	public static void removeBrokenPlayers() { // Removes players without money
		Iterator<JFrame> i = Provider.framesList.iterator();
		while(i.hasNext()) {
			JFrame frame = i.next(); 
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				if(p.getMoney() == 0) { // Broken player
					JOptionPane.showMessageDialog(p, "Looks like you're out of money... Bye!"); // Warn broken player
					i.remove();
					Facade.closePlayer(p);
				}
			}
		}
	}
	
	

	

	public static void saveGame(BufferedWriter fileWriter) throws Exception { // Writes a text file with game info
		fileWriter.write(Integer.toString(Player.numPlayers));
		fileWriter.newLine();
		fileWriter.write(Turn.mapTrack());
		fileWriter.newLine();
		for(JFrame frame : Provider.framesList) {
			if(frame.getClass() == Player.class) {
				Player p = (Player) frame;
				
				fileWriter.write("P" + p.getPlayerNumber() + " ");
				fileWriter.write(p.getMoney() + " ");
				fileWriter.write(p.getBet() + " ");
				fileWriter.write(Boolean.toString(p.isInsured()));
				fileWriter.newLine();
				for(Card c : p.getCards()) {
					fileWriter.write(c.toString() + " ");
				}
				fileWriter.newLine();
			}
		}
		fileWriter.write("BANK");
		fileWriter.newLine();
		fileWriter.write(Boolean.toString(Bank.bank.getbNewRound().isEnabled()));
		fileWriter.newLine();
		for(Card c : Bank.bank.getCards()) {
			fileWriter.write(c.toString() + " ");
		}
		fileWriter.newLine();
	}
	
	
	
	static public void updateActivePlayers() { // Check remaining players on turn and handle case for new round
		Player.activePlayers--;
		if (Player.activePlayers == 0) { // No more players on this turn
			Bank.bank.getbNewRound().setEnabled(true);
			Player.activePlayers = Player.numPlayers;
			Provider.notifyWinnersAndLosers();
		}
		else { // Still have players waiting to play
			Turn.updatePlayerTurn();
		}
	}
	
	
}