import java.util.*;
import java.io.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Maps integer values to actual track names. It is used every time except when the user
 * decides to try to match a track.
 * @author Brook
 *
 */
public class TrackMap 
{
	/** Maps an track ID to track name */
	private Map<TrackID,TrackInfo>trackMap=new HashMap<TrackID,TrackInfo>();
	
	/** The directory at which the tracks are located */
	private File directoryLocated;
	
	/** The track number that will keep track of the index 	 */
	public static int trackNo = 0;	
	
	/**
	 * Constructs a trackMap of the available audio files in supported formats
	 * @param baseDirectory The base directory (folder) from which the files will be indexed.
	 */
	public TrackMap(File baseDirectory)
	{
		directoryLocated = baseDirectory;
		if(baseDirectory.isDirectory()){
			addFolder(baseDirectory);	
		} else{
			addTrack(baseDirectory);
		}
	}
	
	/**
	 * Adds all the tracks inside the given file or folder into the map.
	 * @param baseDirectory The directory of tracks to be mapped.
	 */
	public void addFolder(File baseDirectory)
	{		
		File[] files = baseDirectory.listFiles(new AudioFileFilter());
		//Add the files to the map.
		for(int i=0; i<files.length; i++){
			File f = files[i];
			addTrack(f);
		}				
	}
	
	/** 
	 * Adds a new track to the track map.
	 * @param file The file to be added to the track map.
	 */
	public void addTrack(File file)
	{
		String fileName = file.getName();
		TrackID id = new TrackID(trackNo++);//this ensures unique TrackID
		TrackInfo info = new TrackInfo(id,fileName);
		trackMap.put(id,info);		
		
		//System.out.println(id.toString() + " " + info.toString());//TEST PRINT
	}
	
	/**
	 * Gets the number of tracks mapped in the index.
	 * @return Number of tracks inside.
	 */
	public int getNumberOfTracks()
	{
		return trackMap.size();
	}
	
	/**
	 * Gets an iterator that goes through all the trackID's
	 * @return An iterator.
	 */
	public Iterator<TrackID> getTrackIDIterator()
	{
		Collection<TrackID> keys = trackMap.keySet();
		return keys.iterator();
	}
	
	/**
	 * Gets the trackInfo object at the specific TrackID
	 * @param id The TrackID to find the TrackInfo for.
	 * @return The TrackInfo object.
	 */
	public TrackInfo getTrackInfo(TrackID id)
	{
		return(trackMap.get(id));		
	}
	
	/**
	 * Tests to see if the trackMap already has mapped the specified file
	 * @param file The file to map the trackMap.
	 * @return True if the track is already in the map, false otherwise.
	 */
	public boolean containsTrack(File file)
	{
		for(TrackID id: trackMap.keySet()){
			if(trackMap.get(id).getDescription().equals(file.getName()))return true;
		}
		return false;
	}
	/** 
	 * Inner class that will serve as a filter for the files ending in supported audio formats.
	 * @author Brook
	 *
	 */
	public static class AudioFileFilter implements FilenameFilter /*extends javax.swing.filechooser.FileFilter*/
	{
		private AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes();
	
		public boolean accept(File dir, String name){
			String lowercaseName = name.toLowerCase();
			for(int i=0; i<types.length; i++){
				if (lowercaseName.endsWith(types[i].getExtension())) return true;
			} 
			return false;	
		}	
	}

}
