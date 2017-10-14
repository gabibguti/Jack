import javax.swing.*;

public class Main {
	public static void main (String [] args)
	{
		System.out.println("Blackjack");
		JFrame parent = new JFrame();
		JButton button = new JButton();
		JLabel label = new JLabel();
		button.setText("First Button");
		label.setText("Welcome to BlackJack");
		parent.add(button);
		parent.add(label);
		parent.setVisible(true);
		parent.setSize(200, 100);
		parent.setLocationRelativeTo(null);
	}
}
