//Author: Mark Fisher
//fisher38@iupui.edu

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

//drawing using paintComponent based on:
//http://stackoverflow.com/questions/299495/java-swing-how-to-add-an-image-to-a-jpanel

public class Wall extends JPanel implements MouseWheelListener, AdjustmentListener, MouseListener{

	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> scaledImages = new ArrayList<BufferedImage>();
	private Semaphore imageAddSemaphore = new Semaphore(1,true);
	
	private JScrollBar bar = new JScrollBar();
	
	private int imageHeight = 75;
	private boolean needToRescaleImages = true;
	private int minimumImageHeight = 10;
	
	private int startY = 0; //y to start drawing at
	
	private Dimension previousSize = null;
	
	private int frameDisplayX = 0;
	private int frameDisplayY = 0;
	private int frameDisplayNumber = 0;
	private int frameDisplayFromProfile = 0;
	private boolean displayFrame = false; //true if a frame is being displayed
	
	public Wall(ArrayList<BufferedImage> profiles){
		//use a semaphore so that the images are not accessed when adding the image
		try {
			imageAddSemaphore.acquire();
		} catch (InterruptedException e) {
			s.print("unable to aquire semaphore");
			e.printStackTrace();
		}
		images = profiles;
		imageAddSemaphore.release();
		
		genericConstructor();
	}
	
	public Wall(){
		genericConstructor();
	}
	
	private void genericConstructor(){
		//add a scroll bar
		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.EAST);
		bar.addAdjustmentListener(this);
		
		//add mouse event handlers
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
	}
	
	public void addImage(BufferedImage image){
		//use a semaphore so that the images are not accessed when adding the image
		try {
			imageAddSemaphore.acquire();
		} catch (InterruptedException e) {
			s.print("unable to aquire semaphore");
			e.printStackTrace();
		}
		images.add(image);
		imageAddSemaphore.release();
		
		needToRescaleImages = true;
		
		this.repaint();
	}
	
	public void setImageHeight(int height){
		if (height >= minimumImageHeight){ //make sure the height can't be set to less than the minimum
			imageHeight = height;
			this.repaint();
		}
	}
	
	public void setMinimumImageHeight(int height){
		minimumImageHeight = height;
		
		//if the new minimum is greater than the current height
		//change the height to the minimum height
		if (imageHeight < minimumImageHeight){
			imageHeight = minimumImageHeight;
			this.repaint();
		}
	}
	
	public void paintComponent(Graphics graphics) {
		if (!this.getSize().equals(previousSize)){
			displayFrame=false;
		}
		previousSize = this.getSize();
		
		//scale the images only if the zoom amount has been changed
		if (needToRescaleImages){
			scaleImages();
			needToRescaleImages = false;
		}
		
		drawImages(graphics);
		
		
		displayFrame(graphics);
	}
	
	private void displayFrame(Graphics graphics) {
		//don't do anything if the user has not specified a frame
		if (!displayFrame){
			return;
		}
		
		((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		
		//draw a line that covers the frame cross section
		graphics.setColor(Color.RED);
		graphics.drawLine(frameDisplayX, frameDisplayY - 1, frameDisplayX, frameDisplayY - imageHeight);
		
		//Put call to frame displaying code here!!!
	}

	private void drawImages(Graphics graphics){
		((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		
		//clear the previous images
		graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		//start in the upper left corner
		int x = 0;
		int y = startY;
		
		//get the width of the viewable area
		int viewableWidth = this.getWidth() - bar.getWidth();
		
		//iterate the images to display
		Iterator<BufferedImage> i = scaledImages.iterator();
		while (i.hasNext()){
			//get a reference to the next image to display
			BufferedImage currentImage = i.next();
			
			int currentImageWidth = currentImage.getWidth();
			
			//draw the first part of the image
			graphics.drawImage(currentImage, x, y, null);
			
			//draw what's left of the image
			while (true){
				//check to see if the image was completely shown
				if ((x + currentImageWidth) < viewableWidth){
					//advanced the x position for the next image
					x = x + currentImageWidth;
					break;
				} else if ((x + currentImageWidth) == viewableWidth){
					x = 0; //the image ended at the end of the frame so reset x
					y = y + imageHeight; //advance to next row
					break;
				}
				
				//draw the off screen portion of the image at the beginning of the next row
				y = y + imageHeight; //advance to next row
				x = -(viewableWidth - x); //offset the image so that the previously displayed portion is hidden
				graphics.drawImage(currentImage, x, y, currentImageWidth, imageHeight, null);
			}
		}
		
		//set the scroll bar sizes
		bar.setMaximum((-startY + y) + imageHeight);
		bar.setVisibleAmount(this.getHeight());
	}
	
	private void scaleImages(){
		//use a semaphore to make sure that an image is not added while rescaling the images
		try {
			imageAddSemaphore.acquire();
		} catch (InterruptedException e) {
			s.print("unable to acquire semaphore");
			e.printStackTrace();
		}
		
		//image scaling method from http://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
		for (int i = 0; i < images.size(); i++){
			//if the image has already been scaled, it does not need to be rescaled 
			if ((i < scaledImages.size()) && (scaledImages.get(i).getHeight() == imageHeight)){
				continue;
			}
			
			//get a reference to the next image
			BufferedImage currentImage = images.get(i);
			
			//using the aspect ratio, calculate how wide the image has to be displayed so that it has the desired height and maintains its aspect ratio
			int imageWidth = (int) (imageHeight * ((double)currentImage.getWidth() / (double)currentImage.getHeight()));
			
			BufferedImage newImage = new BufferedImage(imageWidth, imageHeight, currentImage.getType());
			Graphics2D g = newImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawImage(currentImage, 0, 0, imageWidth, imageHeight, null);
			g.dispose();
			
			if (i < scaledImages.size()){
				scaledImages.set(i, newImage);
			} else {
				scaledImages.add(newImage);
			}
		}
		
		imageAddSemaphore.release();
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		if (imageHeight > minimumImageHeight || event.getWheelRotation() < 0){  //make sure that the images can't be made smaller than the minimum
			displayFrame = false;
			imageHeight -= event.getWheelRotation();
			needToRescaleImages = true;
			this.repaint();
		}
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		int prevStartY = startY;
		startY = -e.getValue();
		frameDisplayY -= prevStartY - startY;
		this.repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		//get click location relative to the viewing window
		int clickY = e.getY();
		int clickX = e.getX();
		
		//start in the upper left corner
		int paintX = 0;
		int paintY = startY;
		
		//get the width of the viewable area
		int viewableWidth = this.getWidth() - bar.getWidth();
		
		//iterate the images that were displayed
		Iterator<BufferedImage> i = images.iterator();
		frameDisplayFromProfile = 0;
		boolean imageFound = false;
		while (i.hasNext() && !imageFound){
			//get a reference to the next image that was displayed
			BufferedImage currentImage = i.next();
			
			//using the aspect ratio, calculate how wide the image was displayed
			int imageWidth = (int) (imageHeight * ((double)currentImage.getWidth() / (double)currentImage.getHeight()));
			
			//check to see if the click location was in any part of the image that was drawn
			while (true){
				//check to see if the click is in the current part of the drawn image
				if (
					(clickY >= paintY) &&
					(clickY <= paintY + imageHeight) &&
					(clickX >= paintX) &&
					(clickX <= paintX + imageWidth)
				){
					//calculate the x in the image using the ratio of the actual width to the zoomed width
					frameDisplayNumber = (int)((-paintX + clickX) * ((double)currentImage.getWidth() / (double)imageWidth));
					frameDisplayX = clickX;
					frameDisplayY = paintY + imageHeight;
					
					//stop looking
					imageFound = true;
					break;
				} else {
					//setup to look in the next part of the drawn image
					
					//check to see if the image was completely shown
					if ((paintX + imageWidth) < viewableWidth){
						//advanced the x position for the next image
						paintX = paintX + imageWidth;
						break;
					} else if ((paintX + imageWidth) == viewableWidth){
						paintX = 0; //the image ended at the end of the frame so reset x
						paintY = paintY + imageHeight; //advance to next row
						break;
					}
					
					//offset so that the next portion of the drawn image is searched
					paintY = paintY + imageHeight; //advance to next row
					paintX = -(viewableWidth - paintX); //offset the image so that the previously displayed portion is hidden
				}
			}
			frameDisplayFromProfile++;
		}
		
		//only display an image if an image was clicked on
		displayFrame = imageFound;
		
		if (imageFound){
			this.repaint();
		}
		
		s.print("profile: " + frameDisplayFromProfile + ", frame: " + frameDisplayNumber + ", frame draw coordinates: " + frameDisplayX + ", " + frameDisplayY + "\n");
	}

	//unused listeners
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}
	
}
