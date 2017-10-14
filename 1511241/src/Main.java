import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel subheaderLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;

   public Main(){
	  // Prepare Guided User Interface
      prepareGUI();
   }
   public static void main(String[] args){
	  // Main function
      Main Main = new Main();  
      Main.showEvent();       
   }
   private void prepareGUI(){
	  // Create Frame
      mainFrame = new JFrame("Blackjack");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(4, 1)); // Organize components

      // Create Labels
      headerLabel = new JLabel("",JLabel.CENTER );
      subheaderLabel = new JLabel("", JLabel.CENTER);
      statusLabel = new JLabel("",JLabel.CENTER);        
      statusLabel.setSize(350,100);
      
      // Add Listener
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });
      // Create Buttons Panel
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      // Add components to frame
      mainFrame.add(headerLabel);
      mainFrame.add(subheaderLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      
      // Allow us to see the frame
      mainFrame.setVisible(true);
      
      // Makes the frame pop up centered
      mainFrame.setLocationRelativeTo(null);
   }
   private void showEvent(){
	  // Set header label
      headerLabel.setText("Welcome to Blackjack"); 
      subheaderLabel.setText("Select the number of players:");
      
      // Create buttons
      JButton [] options = new JButton[4];
      options[0] = new JButton("1");
      options[1] = new JButton("2");
      options[2] = new JButton("3");
      options[3] = new JButton("4");

      // Set command actions to the buttons
      options[0].setActionCommand("1 Player");
      options[1].setActionCommand("2 Players");
      options[2].setActionCommand("3 Players");
      options[3].setActionCommand("4 Players");

      // Add listeners to buttons 
      for(JButton b: options)
      {
    	  b.addActionListener(new ButtonClickListener());
      }
      
      // Add buttons to buttons panel
      for(JButton b: options)
      {
    	  controlPanel.add(b);
      }
      
      // Allow us to see the frame
      mainFrame.setVisible(true);  
   }
   private class ButtonClickListener implements ActionListener{
	   // Listener function
	   public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();  
         
         if( command.equals( "1 Player" ))  {
            statusLabel.setText("Starting game for 1 player.");
         } else if( command.equals( "2 Players" ) )  {
            statusLabel.setText("Starting game for 2 players."); 
         } else if( command.equals( "3 Players" ) ) {
            statusLabel.setText("Starting game for 3 players.");
         } else {
        	 statusLabel.setText("Starting game for 4 players.");
         }
      }		
   }
}