package bazam;
import java.awt.Dimension;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 * Contains the underlying index of the tracks the user loads. Used when there is an attempt to match a track.
 * It contains both a TrackMap and CachedIndexMap.
 * @author Brook
 */
public class TrackIndex 
{
	/** The map of probes to their data points */
	private CachedIndexMap cachedIndexMap;
	
	/** The map of the track id's to their track data */
	private TrackMap trackMap;
	
	/** The base directory of the trackIndex, if a folder was indexed */	
	private File baseDirectory = null;
	
	/** Iterates over all the track ID's */
	private Iterator<TrackID>trackIDIterator;
	
	/**
	 * Constructs a trackIndex.
	 * @param baseDir The file or folder name.
	 */
	public TrackIndex(File baseDir)
	{		
		baseDirectory = baseDir;		
		trackMap = new TrackMap(baseDir);//create the map of ID to TrackInfo
		trackIDIterator = trackMap.getTrackIDIterator();//get the iterator
		cachedIndexMap = new CachedIndexMap();
		initializeIndex(baseDir);		
	}
	
	/**
	 * Sets up the cachedIndexMap
	 * @param baseDir The base directory - a file or folder.
	 */
	private void initializeIndex(File baseDir)
	{
		try {
			if(baseDir.isDirectory()){
				addFolder(baseDir);
			} else {
				addTrack(baseDir);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Adds a track to the trackIndex.
	 * @param file The file to be indexed.
	 */
	public void addTrack(File file)
	{
		if(!trackMap.containsTrack(file)){//if track map does not have the current file.
			trackMap.addTrack(file);
		}
		try{
			TrackID id = new TrackID(TrackMap.trackNo-1);
			//System.out.println(id.toString());
			int numberIndexed = cachedIndexMap.indexFile(file,id);			
			TrackInfo info = trackMap.getTrackInfo(id);
			info.setNumberHashPoints(numberIndexed);
			JOptionPane.showMessageDialog(null, "Indexed " + file.getName(), "Success", JOptionPane.PLAIN_MESSAGE);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a folder to the track index object.
	 * @param folder The folder to be indexed
	 */
	public void addFolder(File folder)
	{
		File[] files = folder.listFiles(new TrackMap.AudioFileFilter());		
		for(int i=0; i<files.length; i++){
			File f = files[i];
			addTrack(f);
		}
		if(files.length == 0){
			JOptionPane.showMessageDialog(null, "The folder -" + 
					folder.getName() + "-\nhas no tracks.");
		} else {
			JOptionPane.showMessageDialog(null, "The folder -" + 
					folder.getName() + "-\nwas successfully indexed.");
		}
	}
	
	/**
	 * Tries to match a signal against the index. It will not be added to the index.
	 * Displays the results on a new JFrame.
	 * @param s The signal to query.
	 */
	public void matchSignal(Signal s)
	{
		MatchResults results = cachedIndexMap.query(s);
		MatchResultsFrame matchFrame = new MatchResultsFrame(results, trackMap);		
	}
	
	/**
	 * Gets the trackInfo for the given track ID.
	 * @param id The trackID
	 * @return The trackInfo for the trackID.
	 */
	public TrackInfo getTrackInfo(TrackID id)
	{
		return trackMap.getTrackInfo(id);
	}
	
	/**
	 * Gets an iterator over the track id's.
	 * @return An iterator.
	 */
	public Iterator<TrackID> getTrackIDIterator()
	{
		return trackMap.getTrackIDIterator();
	}
	/**
	 * Gets the number of tracks in the index.
	 * @return The number of tracks indexed.
	 */
	public int getNumberOfTracks()
	{
		return trackMap.getNumberOfTracks();
	}
	
	/**
	 * Gets the base directory of the index.
	 * @return The file base directory.
	 */
	public File getBaseDirectory()
	{
		return baseDirectory;
	}
}
