package facade;

import javax.swing.JOptionPane;

import cards.Card;
import frames.bank.Bank;
import frames.player.Player;
import tools.Provider;
import tools.Turn;

public class Facade {

	public static void startNewGame (int numberOfPlayers) {
		int player;
		
		// Set number of players
		Player.numPlayers = numberOfPlayers;
		
		// Create Bank
		Bank.createBank("Bank");
		Provider.framesList.add(Bank.bank);
		
		// Create Players
		for (player = 0; player < numberOfPlayers; player++) {
			Provider.framesList.add(new Player(String.valueOf(player + 1), Bank.bank)); // Create Player Frame and add to framesList
		}
		
		// Initialize Turns
		Turn.firstTurn(numberOfPlayers);
			
	}

	public static void closePlayer(Player p) {
		Provider.framesList.remove(p); // Remove PlayerFrame from framesList
		Turn.removePlayer(p.getPlayerNumber());
		Player.numPlayers--;
		Provider.updateActivePlayers();
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
