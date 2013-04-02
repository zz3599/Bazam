package bazam;
import java.io.*;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Contains the entire mapping of all the indexed probes. The underlying map is a 
 * TrackMap which maps trackID's(integer values) to trackInfo's (String values).
 * @author Brook
 *
 */
public class CachedIndexMap 
{	
	/** The base directory from which the user has chosen to load audio files*/
	private File baseDirectory;
	
	/** Contains the index files of ALL the audio files in the selected base folder. The list of data points is the list of 
	 * all tracks that have the same probe key. */
	private LinkedHashMap<Probe, ArrayList<ProbeDataPoint>> indexProbeMapping = new LinkedHashMap
		<Probe, ArrayList<ProbeDataPoint>>();	
	
	/**
	 * Constructs an index map.
	 */
	public CachedIndexMap()
	{	
	}
	
	/**
	 * Finds the results of the matches against the given Signal.
	 * @param signalQuery The signal to be matched
	 * @return The results of the match inside a MatchResults object.
	 */
	public MatchResults query(Signal signalQuery)
	{
		MatchResults results = new MatchResults(signalQuery.getName());//initialize matchResults object
		Spectrogram s = new Spectrogram(signalQuery);
		//TrackID thisID = new TrackID(Integer.MAX_VALUE);//to prevent overwriting index track map
		SpectrogramProbesExtractor ex = new SpectrogramProbesExtractor(s);
		ArrayList<HashPoint>hashPoints = ex.getHashPoints();
		for(HashPoint hash : hashPoints){
			Probe probeKey = hash.probe;
			int thisIndex = hash.index;
			if(indexProbeMapping.containsKey(probeKey)){
				//CREATE MATCH HISTOGRAM
				ArrayList<ProbeDataPoint>matchingPoints = indexProbeMapping.get(probeKey);
				results.matchTally(thisIndex,matchingPoints);
			}
		}
		return results;
	}	
	
	/**
	 * Gets the entire underlying mapping of probes to their data points.
	 * @return The map.
	 */
	public LinkedHashMap<Probe,ArrayList<ProbeDataPoint>> getIndexMap()
	{		
		return indexProbeMapping;
		
	}
	
	/**
	 * Indexes the specific audio file and places it into the cache hash map.
	 * @param file The file whose probe map is to be stored in the cache.
	 * @param id The trackID (integer wrapper) of the audio file to be indexed.
	 * @return The number of hash points inside the audio file that were added to index.
	 * @throws IOException Problem reading the audio file.
	 * @throws UnsupportedAudioFileException The audio file type is not supported.
	 */
	public int indexFile(File file, TrackID id) throws UnsupportedAudioFileException, IOException
	{
		String fileName = file.getName();
		AudioInputStream ain = AudioSystem.getAudioInputStream(file);
		AudioClip clip = AudioClip.fromStream(ain, fileName);
		ain.close();
		Spectrogram s = new Spectrogram(new Signal(clip, fileName));
		SpectrogramProbesExtractor ex = new SpectrogramProbesExtractor(s);
		ArrayList<HashPoint>hashPoints = ex.getHashPoints();
		int size = hashPoints.size();
		//System.out.println(size);
		for(HashPoint hash : hashPoints){
			Probe probeKey = hash.probe;//probe key
			int index = hash.index;//index location of probe
			ProbeDataPoint aDataPoint = new ProbeDataPoint(id,index);
			if(indexProbeMapping.containsKey(probeKey)){
				ArrayList<ProbeDataPoint>dataPoints = indexProbeMapping.get(probeKey);//get current data points inside map
				if(!dataPoints.contains(aDataPoint)){
					dataPoints.add(aDataPoint);
				}
				indexProbeMapping.put(probeKey, dataPoints);//add updated mapping back
			} else {//new probe 
				ArrayList<ProbeDataPoint>newDataPoints = new ArrayList<ProbeDataPoint>();
				newDataPoints.add(aDataPoint);
				indexProbeMapping.put(probeKey,newDataPoints);
			}
		}		
		return size;
		
	}

}
