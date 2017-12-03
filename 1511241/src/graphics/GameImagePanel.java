package graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Map;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameImagePanel extends JPanel{
	Map<Image, Point> imgs;
	Image backg;
	public GameImagePanel(Map<Image, Point> images, Image background) {
		imgs = images;
		backg = background;
	}                                                                                                          	
	@Override                                                                                                  
    protected void paintComponent(Graphics g) {                                                                
        super.paintComponent(g);
        g.drawImage(backg, 0, 0, getWidth(), getHeight(), this);
        for (java.util.Map.Entry<Image, Point> e : imgs.entrySet()) {
        	Image img = e.getKey();
        	Point imgPoint = e.getValue();
        	g.drawImage(img, imgPoint.x, imgPoint.y, this);
        }
    }
}
