import java.awt.image.BufferedImage;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class aviLoader{
	BufferedImage outputImage;
	int frameNum;
    String fileName;
    boolean looking = true;
    
    public aviLoader(int frame, String name) {
    	frameNum = frame;
    	fileName = name;
    }   
    
    public BufferedImage getFrame() {
        FrameGrabber grabber = new OpenCVFrameGrabber(fileName); // for AVI
        try {
        	int i = 0;
            grabber.start();
            IplImage img;
            while (looking) {
                img = grabber.grab(); //grabs frame
                if (img != null) {
                	i++;
                	if (i == frameNum)
                	{
                		outputImage = img.getBufferedImage();//saves frame to a buffered image
                		grabber.stop();
                		looking = false;
                	}
                }
            }
        } catch (Exception e) {
        }      
		return outputImage;
    }
}
