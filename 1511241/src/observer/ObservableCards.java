package observer;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JOptionPane;

import cards.Card;
import facade.Facade;

public class ObservableCards extends Observable {
	private ArrayList<Card> cards;

	public ObservableCards() {
			cards = new ArrayList<>();
    }

	public ArrayList<Card> getCards() {
	    return cards;
    }
	
	public void addCard() {
		cards.add(Facade.removeCardFromDeck());
	    setChanged();
	    notifyObservers();
    }
	
	public void addCard(String s) {
		for(Card c : Card.Deck){
			if(c.toString().equals(s)){
				cards.add(c);
				Card.Deck.remove(c);
				setChanged();
			    notifyObservers();
			    return;
			}
		}
		JOptionPane.showMessageDialog(null, "Save is corrupted.");
		System.exit(1);
    }
	
	public void addFlippedCard() {
		cards.add(Card.flippedCard);
		setChanged();
	    notifyObservers();
	}
	
	public void removeFlippedCard() {
		cards.remove(Card.flippedCard);
		setChanged();
	    notifyObservers();
	}
	
	public void clearCards() {
		cards.removeAll(cards);
		setChanged();
	    notifyObservers();
	}
}
