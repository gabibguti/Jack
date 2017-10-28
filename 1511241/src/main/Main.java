package main;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import cards.*;
import components.GameImage;

import frames.*;

public class Main {
   private JFrame mainFrame;
   private BankFrame bankFrame;
   private PlayerFrame playerFrame;
   private JLabel headerLabel;
   private JLabel subheaderLabel;
   private JLabel statusLabel;
   private JPanel mainCP;
   private JPanel playerCP;
   private JButton [] options;
   private String [] actions;
   private int maxPlayers = 4;
   BufferedImage bankBackground = null;
      
   static public ArrayList<Card> deck = Card.newDeck();
   
   static public String img_path = System.getProperty("user.dir") + "/src/images/";	// Images path

   public Main(){
	  // Prepare Guided User Interface
      prepareMainFrame();
      // Get images
      try 
      {
          bankBackground = ImageIO.read(new File(img_path + "blackjackBKG.png"));
      } 
      catch (IOException e) 
      {
          e.printStackTrace();
      }
   }
   
   public static void main(String[] args){
	  // Main function
      Main Main = new Main();
      Main.showEvent();
   }
   
   private void prepareMainFrame(){
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
      mainCP = new JPanel();
      mainCP.setLayout(new FlowLayout());

      // Add components to frame
      mainFrame.add(headerLabel);
      mainFrame.add(subheaderLabel);
      mainFrame.add(mainCP);
      mainFrame.add(statusLabel);
      
      // Allow us to see the frame
      mainFrame.setVisible(true);
      
      // Makes the frame pop up centered
      mainFrame.setLocationRelativeTo(null);
   }

   private void showEvent(){
	  int player;
	   
	  // Set header label
      headerLabel.setText("Welcome to Blackjack"); 
      subheaderLabel.setText("Select the number of players:");
      
      // Create Actions
      actions = new String[maxPlayers];
      for(player = 0; player < maxPlayers; player++)
      {
    	actions[player] = String.valueOf(player + 1);  // Set actions[0] = 1 and so on
      }
      
      // Create buttons
      options = new JButton[maxPlayers];
      for(player = 0; player < maxPlayers; player++)
      {
    	options[player] = new JButton(String.valueOf(player + 1));
      }

      // Set command actions to the buttons
      for(player = 0; player < maxPlayers; player++)
      {
    	options[player].setActionCommand(actions[player]);
      }

      // Add listeners to buttons 
      for(JButton b: options)
      {
    	  b.addActionListener(new ButtonClickListener());
      }
      
      // Add buttons to buttons panel
      for(JButton b: options)
      {
    	  mainCP.add(b);
      }
      
      // Allow us to see the frame
      mainFrame.setVisible(true);  
   }
   
   private class ButtonClickListener implements ActionListener{
	   // Listener function
	   public void actionPerformed(ActionEvent e) {
		 int player, numberOfPlayers = 0;
         String command = e.getActionCommand();
         
         if(Arrays.asList(actions).contains(command)) // Search for command in actions array
         {
             // Close Main Frame
             mainFrame.dispose();
             
             bankFrame = new BankFrame("Bank", bankBackground);
        	 
        	 numberOfPlayers = Integer.parseInt(command);

        	 for(player = 0; player < numberOfPlayers; player++)
        	 {
        		// Create Player Frame
        		playerFrame = new PlayerFrame(String.valueOf(player + 1), bankFrame);
        	 }
         }
         else
         {
        	statusLabel.setText("Invalid Option"); 
         }
      }		
   }
}