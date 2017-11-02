package cards;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import components.GameImage;
import main.Main;

public class Card {

    private final Rank rank;
    private final Suit suit;
    private BufferedImage image = null;
    // TODO: Try to use Main.img_path
    public String img_path = System.getProperty("user.dir") + "/src/images/";	// Images path

    private Card(Rank rank, Suit suit) {
        this.rank = rank; // Set card value
        this.suit = suit; // Set card suit
        try 
        {
            image = ImageIO.read(new File(img_path + this.toString() + ".gif"));
        } 
        catch (IOException e) 
        {
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