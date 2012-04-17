public class AviLoaderTester {
    public static void main(String[] args) {	   
        aviLoader gs = new aviLoader(150, "33.avi");
        Thread th = new Thread(gs); // thread to speed things up
        th.start();
    }
}
