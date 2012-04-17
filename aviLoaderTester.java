public class AviLoaderTester {
    public static void main(String[] args) {	   
        aviLoader test = new aviLoader(150, "33.avi");
        test.getFrame(); //returns a frame as buffered image
    }
}
