
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.ArrayList;

public class GUI extends JFrame{
	private Wall wall;
	
	public GUI(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("VideoWall");
		
		wall = new Wall("assets");
		wall.setImageHeight(75);
		wall.setMinimumImageHeight(70);
		this.add(wall);
		this.setSize(900, 500);
		setVisible(true);
	}
}


