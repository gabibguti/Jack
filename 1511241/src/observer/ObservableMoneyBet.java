package observer;
import java.util.Observable;

public class ObservableMoneyBet extends Observable {
	private int bet;
	private int money;

	public ObservableMoneyBet(int money) {
			this.bet = 0;
			this.money = money;
    }

	public int getBet() {
	    return bet;
    }
	
	public int getMoney() {
		return money;
	}
	
	public void setBet(int bet) {
		this.bet = bet;
	    setChanged();
	    notifyObservers();
    }
	
	public void setMoney(int money) {
		this.money = money;
	    setChanged();
	    notifyObservers();
	}
}
