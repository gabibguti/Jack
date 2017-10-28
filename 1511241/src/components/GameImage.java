package components;

                                                                                                               
import java.awt.*;                                                                                             
                                                                                                               
import javax.swing.*;                                                                                          
                                                                                                               
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
                                                                                                               