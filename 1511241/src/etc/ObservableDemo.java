package etc;
import java.util.Observable;

import javax.swing.JLabel;

public class ObservableDemo extends Observable {
	private int BET;
	private JLabel lBet;

	public ObservableDemo(int bet) {
			this.BET = bet;
			this.lBet = new JLabel("Bet $0");
    }

	public int getBET() {
	    return BET;
    }

	public JLabel getPlayerBet() {
	    return lBet;
    }
	
	public void setBET(int bet) {
		this.BET = bet;
		lBet.setText("Bet $" + this.BET);
	    setChanged();
	    notifyObservers();
    }
}
