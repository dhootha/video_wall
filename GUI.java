
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GUI extends JFrame{
	private Wall wall;
	
	public GUI(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("VideoWall");
		
		File directory = new File("/home/mark/desktop/images");
		
		//http://stackoverflow.com/questions/5751335/using-file-listfiles-with-filenameextensionfilter
		File[] imageFiles = directory.listFiles(new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.toLowerCase().endsWith(".jpg");
			}
		});
		
		wall = new Wall();
		wall.setImageHeight(75);
		wall.setMinimumImageHeight(70);
		this.add(wall);
		this.setSize(900, 500);
		setVisible(true);
		
		for (int i = 0; i < imageFiles.length; i++){
			//image scaling method from http://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
			double scale=0.05;
			BufferedImage image = null;
			try {
				image = ImageIO.read(imageFiles[i]);
			} catch (IOException e) {
				System.out.println("unable to load image");
				System.exit(0);
			}
			BufferedImage newImage = new BufferedImage((int)(image.getWidth()*scale), (int)(image.getHeight()*scale), image.getType());
			Graphics2D g = newImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			g.drawImage(image, 0, 0, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale), null);
			g.dispose();
			wall.addImage(newImage);
		}
	}
}


