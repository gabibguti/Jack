package etc;
import java.util.Observable;

public class ObservableDemo extends Observable {
	private int bet;
	private int money;

	public ObservableDemo(int bet, int money) {
			this.bet = bet;
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
