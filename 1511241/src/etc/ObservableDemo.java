package etc;
import java.util.Observable;

import javax.swing.JLabel;

public class ObservableDemo extends Observable {
	private int bet;
	private int money;
	private JLabel lBet;
	private JLabel lMoney;

	public ObservableDemo(int bet, int money) {
			this.bet = bet;
			this.money = money;
			this.lBet = new JLabel("Bet $" + this.bet);
			this.lMoney = new JLabel("Money $" + this.money);
    }

	public int getBet() {
	    return bet;
    }
	
	public int getMoney() {
		return money;
	}

	public JLabel getPlayerBet() {
	    return lBet;
    }
	
	public JLabel getPlayerMoney() {
	    return lMoney;
    }
	
	public void setBet(int bet) {
		this.bet = bet;
		lBet.setText("Bet $" + this.bet);
		lMoney.setText("Money $" + this.money);
	    setChanged();
	    notifyObservers();
    }
	
	public void setMoney(int money) {
		this.money = money;
		lMoney.setText("Money $" + this.money);
	    setChanged();
	    notifyObservers();
	}
}
