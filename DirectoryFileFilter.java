import java.io.*;

public class DirectoryFileFilter implements FileFilter {
  
  // override FileFilter#accept to validate files
  private final String[] acceptedExtensions = new String[] {"avi"};
  
  public boolean accept(File file){
    return file.isDirectory();
  }
}
