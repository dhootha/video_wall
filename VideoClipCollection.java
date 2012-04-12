import java.io.*;
import java.util.ArrayList;

public class VideoClipCollection {
  
  private ArrayList<VideoClip> videoClips;
    
  public VideoClipCollection(String path){
    
    File sourceDirectory = new File(path);
    if(!sourceDirectory.isDirectory()){
      throw new IllegalArgumentException("path argument must be a directory");
    }
    
    DirectoryFileFilter directoryFilter = new DirectoryFileFilter();
    File[] sourcePaths = sourceDirectory.listFiles(directoryFilter);
    videoClips = new ArrayList<VideoClip>();
    
    if(sourcePaths.length < 1){
      throw new IllegalArgumentException("path directory must contain at least one sub-directory");
    }
    
    for (File subPath : sourcePaths){
      videoClips.add(new VideoClip(subPath.toString()));
    }
  }
  
  public ArrayList<VideoClip> getVideoClips(){
    return videoClips;
  }
      
}