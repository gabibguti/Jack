package cards;
import java.util.*;

public class Card {

    private final Rank rank;
    private final Suit suit;

    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }
    
    public int getPoints() {
    	return this.rank.getRankPoints();
    }

    public String toString() {
        return rank.toString() + suit.toString().toLowerCase().charAt(0);
    }

    private static final List<Card> Deck = new ArrayList<Card>();

    static {
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                Deck.add(new Card(rank, suit));
        // Shuffles deck
        Collections.shuffle(Deck);
    }

    public static ArrayList<Card> newDeck() {

        return new ArrayList<Card>(Deck);
    }

}