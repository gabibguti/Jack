package etc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Chip {
	private final int value;
	private BufferedImage image = null;

	public Chip(int value) {
        this.value = value;
        try 
        {
            image = ImageIO.read(new File("../images/ficha " + String.valueOf(this.value) + "$.png"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

	public int getValue() {
		return value;
	}

	public BufferedImage getImage() {
		return image;
	}
	
}
