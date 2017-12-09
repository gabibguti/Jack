package facade;

import java.util.Map;

import javax.swing.JOptionPane;

import cards.Card;
import frames.bank.Bank;
import frames.player.Player;
import tools.Provider;
import tools.Turn;

public class Facade {

	public static void startNewGame (int numberOfPlayers) {
		// Set number of players
		Player.numPlayers = numberOfPlayers;
		
		createBank();
		createPlayers(numberOfPlayers);
		
		// Initialize Turns
		Turn.firstTurn(numberOfPlayers);
		
		JOptionPane.showMessageDialog(null, "Make your bets.");
			
	}
	
	public static void restartGame (int numberOfPlayers, Map<Integer, Integer> turns) {
		// Set number of players
		Player.numPlayers = numberOfPlayers;
		
		createBank();

		for (java.util.Map.Entry<Integer, Integer> e : turns.entrySet()) {
			Integer player = e.getKey();
			Provider.framesList.add(new Player(String.valueOf(player), Bank.bank)); // Create Player Frame and add to framesList
		}
		
		// Initialize Turns
		Turn.setTurn(turns);
	}

	public static void createBank () {
		// Create Bank
		Bank.createBank("Bank");
		Provider.framesList.add(Bank.bank);
	}
	
	public static void createPlayers (int numberOfPlayers) {
		int player;
		
		// Create Players
		for (player = 0; player < numberOfPlayers; player++) {
			Provider.framesList.add(new Player(String.valueOf(player + 1), Bank.bank)); // Create Player Frame and add to framesList
		}
	}
	
	public static void closePlayer(Player p) {
		Provider.framesList.remove(p); // Remove PlayerFrame from framesList
		Turn.removePlayer(p.getPlayerNumber());
		Player.numPlayers--;
		Provider.updateActivePlayers();
		System.out.println("closing player" + Player.activePlayers + Player.bets + Player.numPlayers);
		if(Player.bets == Player.numPlayers) {
			Facade.deliverCards();
		}
		p.dispose();
	}
	
	public static void newRound() {
		Provider.removeBrokenPlayers();
		
		Provider.resetPlayers();
		
		Turn.updatePlayerTurn();
	}

	public static void bet(int chip) {
		Player p = Provider.currentPlayer();
		if(p.getMoney() - chip >= 0) { // Check if player still has money to bet
			p.setBet(p.getBet() + chip);
			p.setMoney(p.getMoney() - chip); // Update money left for player
		}
		else {
			JOptionPane.showMessageDialog(p, "You have no more money to bet.");
		}

	}

	public static void deliverCards() {
		Bank.bank.addCard();
		Bank.bank.addFlippedCard();
		
		Provider.checkInsurance();
		
		Provider.deliverPlayersCards();
		
		Turn.updatePlayerTurn();

		Bank.bank.disableChipsClickListener();
	}

	public static void stand(Player p) { 
		p.configurePlayerActions(false, false, false, false, false);
		
		Turn.nextPlayerTurn();
		
		Provider.updateActivePlayers();
	}
	
	static public Card removeCardFromDeck() {
		Card card;
		try {
			card = Card.Deck.remove(0); // Remove card from deck
			return card;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Deck ended. Starting new deck..."); // Show message when deck runs out of cards and new deck is started
			Card.newDeck();
			return Card.Deck.remove(0);
		}
	}
}
