//Author: Mark Fisher
//fisher38@iupui.edu

//This is a class that has static functions designed to make programming in java a little more convenient.

public class s {
	private static long startTime;
	private static boolean ticOccured = false;
	
	//This function makes it so that you don't have to type "System.out" every time you want to print.
	public static void print(Object output){
		System.out.print(output);
	}
	
	//This function is used with s.toc() for profiling.  Call s.tic() at the beginning of the code to profile, and s.toc() at the end.
	public static void tic(){
		ticOccured = true;
		startTime = System.nanoTime();
	}
	
	//This function is used with s.tic() for profiling.  Call s.tic() at the beginning of the code to profile, and s.toc() at the end.
	public static void toc(String label){
		long endTime = System.nanoTime();
		
		if (ticOccured){
			s.print(label + ": " + (endTime-startTime) + " ns\n");
		} else {
			s.print("Error - " + label + " toc without tic\n");
		}
	}
}
