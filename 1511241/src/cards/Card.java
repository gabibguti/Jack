package cards;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class Card {

    private final Rank rank;
    private final Suit suit;
    private BufferedImage image = null;
    // TODO: Try to use Main.img_path
    public String img_path = System.getProperty("user.dir") + "/src/images/";	// Images path
    static public Card flippedCard = new Card();

    private Card(Rank rank, Suit suit) {
        this.rank = rank; // Set card value
        this.suit = suit; // Set card suit
        try {
            image = ImageIO.read(new File(img_path + this.toString() + ".gif"));
        } 
        catch (IOException e) {
        	System.out.println(this.toString());
            e.printStackTrace();
        }
    }

    private Card() { // Constructor to flipped card
    	this.rank = Rank.FLIPPED;
    	this.suit = Suit.DIAMONDS;
    	try {
            image = ImageIO.read(new File(img_path + "deck1.gif"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }
    
    public BufferedImage getImage() {
    	return this.image;
    }
    
    public int getPoints() {
    	return this.rank.getRankPoints();
    }

    public String toString() {
    	if(rank.getRankPoints() == 0) {
    		return rank.toString();
    	}
    	else {
    		return rank.toString() + suit.toString().toLowerCase().charAt(0);
    	}
    }

    public static List<Card> Deck;

    static {
        Card.newDeck();
    }

    public static void newDeck() {
        Deck = new ArrayList<Card>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
            	if(rank.getRankPoints() != 0) {
            		Deck.add(new Card(rank, suit));
            	}
            }
        }
        // Shuffles deck
        Collections.shuffle(Deck);
    }
}