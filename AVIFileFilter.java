import java.io.*;

public class AVIFileFilter implements FileFilter {
  
  // override FileFilter#accept to validate files
  private final String[] acceptedExtensions = new String[] {"avi"};
  
  public boolean accept(File file){
    for (String extension : acceptedExtensions){
      if (file.getName().toLowerCase().endsWith(extension)) return true;
    }
    return false;
  }
}