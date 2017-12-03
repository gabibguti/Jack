package graphics;

                                                                                                               
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;                                                                                          
                                                                                                               
@SuppressWarnings("serial")
public class GameImage extends JComponent {                                                                       
	private Image img;                                                                              
    public GameImage(Image image) {                                                                       
        this.img = image;                                                                           
    }                                                                                                          
    @Override                                                                                                  
    protected void paintComponent(Graphics g) {                                                                
        super.paintComponent(g);                                                                               
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);                                      
    }                                                                                                          
}                                                                                                              
                                                                                                               