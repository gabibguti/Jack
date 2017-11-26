package etc;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

public class ObserverExample implements Observer {

	private JLabel lBet;
	private JLabel lMoney;
	
	public ObserverExample(int initialAmount) {
		lBet = new JLabel("Bet $0");
		lBet.setSize(40, 15);
		lMoney = new JLabel("Money $" + initialAmount);
	}
	
	@Override
    public void update(Observable observable, Object arg) {
			ObservableDemo bet = (ObservableDemo) observable;
			lBet.setText("Bet $"+ bet.getBet());
			lMoney.setText("Money $" + bet.getMoney());
    }

	public JLabel getPlayerMoney() {
		return lMoney;
	}
	
	public JLabel getPlayerBet() {
		return lBet;
	}

}

