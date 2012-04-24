import java.awt.image.BufferedImage;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class aviLoader{
	BufferedImage outputImage;
	double framePercent;
    String fileName;
    int frameNum;
    boolean looking = true;
    
    public aviLoader(double Percent, String name) {
    	framePercent = Percent;
    	fileName = name;
    }   
    
    public BufferedImage getFrame() {
        FrameGrabber grabber = new OpenCVFrameGrabber(fileName); // for AVI
        try {
        	int i = 0;
            grabber.start();
            int totalFrames = grabber.getLengthInFrames();
            frameNum = (int)(framePercent * totalFrames);
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
