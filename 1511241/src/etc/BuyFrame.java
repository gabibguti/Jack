package etc;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import frames.BankFrame;

public class BuyFrame {
	private JButton ok;
	private JFrame buyFrame;
	private JLabel text;
	private JSpinner credit;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	
    public BuyFrame() {
		buyFrame = new JFrame("Get Credit");
		buyFrame.setSize(300, 200);
		buyFrame.setLocationRelativeTo(null);
		buyFrame.setLayout(new GridLayout(3, 1)); // Organize components
		
		WindowAdapter windowAdapter = new WindowAdapter() { // Exit on close window
			public void windowClosing(WindowEvent windowEvent) {
				BankFrame.bank.enableChipsClickListener();
			}
		};
		
		buyFrame.addWindowListener(windowAdapter);
		
		middlePanel = new JPanel();
		middlePanel.setLayout(new FlowLayout());
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		
		text = new JLabel("Please enter the amount of credit wanted", JLabel.CENTER);

		credit = new JSpinner();
		credit.setPreferredSize(new Dimension(100, 30));
		
		ok = new JButton("OK");
		
		ok.addActionListener(new ActionListener() { // Add ok listener event
			public void actionPerformed(ActionEvent actionEvent) {
				try {
				    credit.commitEdit();
				} catch ( java.text.ParseException e ) { 
					System.out.println("Spinner error on credit buy");
				}
				int value = (Integer) credit.getValue();
				if(value > Buy.getMaxCredit()) {
					JOptionPane.showMessageDialog(null, "Maximum quantity of credit allowed per buy is " + Buy.getMaxCredit());
				}
				else {
	        		Buy.getCredit(value);
					buyFrame.dispose();
					BankFrame.bank.enableChipsClickListener();
				}
			}
		});
		
		middlePanel.add(credit);
		bottomPanel.add(ok);

		buyFrame.add(text);
		buyFrame.add(middlePanel);
		buyFrame.add(bottomPanel);
		
		buyFrame.setVisible(true);
    }
}