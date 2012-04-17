import java.io.*;

public class DirectoryFileFilter implements FileFilter {
  
  // override FileFilter#accept to validate files
  public boolean accept(File file){
    return file.isDirectory();
  }
}
