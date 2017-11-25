package etc;
import java.util.Observable;

import javax.swing.JLabel;

public class ObservableDemo extends Observable
{
	private int BET;

	public ObservableDemo(int bet, JLabel j)
    {
			this.BET = bet;
			j = new JLabel("Bet $" + this.BET);
    }

	public int getBET()
    {
	    return BET;
    }

	public void setBET(int bet, JLabel j)
    {
		this.BET = bet;
		j.setText("Bet $" + this.BET);
	    setChanged();
	    notifyObservers();
    }
}
