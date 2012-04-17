import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.CanvasFrame; //used to get canvas

public class aviLoader implements Runnable {
	int frameNum;
    IplImage image;
    String fileName;
    boolean looking = true;
    CanvasFrame canvas = new CanvasFrame("Video"); //debug code to show frame
    public aviLoader(int frame, String name) {
    	frameNum = frame;
    	fileName = name;
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE); //debug code to show frame
    }
    
    public void run() {
        FrameGrabber grabber = new OpenCVFrameGrabber("vids\\"+ fileName); // for AVI
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
                		canvas.showImage(img); //debug code to show frame
                		cvSaveImage("FRAME.jpg", img); //saves in working directory
                		grabber.stop();
                		looking = false;
                	}
                }
            }
        } catch (Exception e) {
        }
    }
}
