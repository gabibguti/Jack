package cards;

public enum Rank {
    DEUCE(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
    NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11), FLIPPED(0);

    private int points;

    Rank(int points) {
        this.points = points;
    }
    
    public String toString() {
    	if(this.points == 0) {
    		return "flipped";
    	}
    	else if(this.points < 10) {
    		return String.valueOf(this.points);
    	}
    	else if(this.points == 11) {
    		return "a";
    	}
    	else {
    		switch(this){
    		case TEN:
    			return "t";
    		case JACK:
    			return "j";
    		case QUEEN:
    			return "q";
    		case KING:
    			return "k";
    		default:
    			return "";
    		}
    	}
    }

    public int getRankPoints() {
        return this.points;
    }

}
