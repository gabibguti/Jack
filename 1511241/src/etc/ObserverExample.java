package etc;
import java.util.Observable;
import java.util.Observer;

public class ObserverExample implements Observer {

	private ObservableDemo beth ;

	@Override
    public void update(Observable observable, Object arg) {
			beth = (ObservableDemo) observable;
			System.out.println("BET CHANGED "+ beth.getBET());
    }
}

