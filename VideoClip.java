import java.io.*;
import java.util.ArrayList;

public class VideoClip {
  
  private File aviClip;
  private ArrayList<File> profileImages;
  
  // path will be relative to the directory VideoClip.java lives in
  public VideoClip(String path){
    
    File sourceDirectory = new File(path);
    if(!sourceDirectory.isDirectory()){
      throw new IllegalArgumentException("path argument must be a directory");
    }
    
    AVIFileFilter aviFilter = new AVIFileFilter();
    if(sourceDirectory.listFiles(aviFilter).length != 1){
      throw new IllegalArgumentException("a single avi file must be provided");
    }
    aviClip = sourceDirectory.listFiles(aviFilter)[0];
    
    ImageFileFilter imageFilter = new ImageFileFilter();
    if(sourceDirectory.listFiles(imageFilter).length < 1){
      throw new IllegalArgumentException("at least one video profile image must be provided");
    }
    profileImages = new ArrayList<File>();
    for (File image : sourceDirectory.listFiles(imageFilter)){
      profileImages.add(image);
    }
  }
  
  public File getAviClip(){
    return aviClip;
  }
  
  public ArrayList<File> getProfileImages(){
    return profileImages;
  }
      
}