package frames;

import java.util.ArrayList;

import cards.Card;

public class Score {
	private int score;
	
	public Score () {
		this.score = 0;
	}
	
	public int UpdateScore (ArrayList<Card> hand)
	{
		this.score = 0; // Reset score
		Integer aces = 0;
		
		for(Card card: hand) // Sum hand points
		{
			if(card.getPoints() == 11) // Ace card
			{
				aces += 1;
			}
			else
			{
				this.score += card.getPoints(); // Sum card point to score
			}
		}
		
		if(aces > 0) // Treat when hand has aces
		{
			for(Integer i = 0; i < aces; i++)
			{
				if(i == 0 && this.score + 11 <= 21) // First ace and score doesn't burst
				{
					this.score += 11;// Ace values 11
				}
				else
				{
					this.score += 1; // Ace values 1
				}
			}
		}
		
		return this.score;
	}
	
	public int getScore () // Gets the actual score
	{
		return this.score;
	}
}
