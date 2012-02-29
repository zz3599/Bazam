import java.util.*;
/**
 * An aggregation of all the match statistics, mapping trackID's to their respective histograms of matches.
 * @author Brook
 *
 */
public class MatchResults
{
	/** The file matched for this MatchResults */
	private String fileMatched;
	
	/** The results of the matching process. The key is the id of the song that matches, the histogram is the count at particular time
	 * intervals */
	private Map <TrackID,Histogram> matchResults;
	
	/**
	 * Constructs a new Match Results object for a file that is to be matched.
	 * @param fileMatched The file that was matched.
	 */
	public MatchResults(String fileMatched)
	{
		matchResults = new HashMap<TrackID,Histogram>();
		this.fileMatched = fileMatched;			
	}
	
	/**
	 * Increments the count on a particular index.
	 * @param index The index of probe query. 
	 * @param dataPoints The list of data points that was mapped to an identical probe.
	 */
	public void matchTally(int index, ArrayList<ProbeDataPoint> dataPoints)
	{
		for(ProbeDataPoint dataPoint : dataPoints){
			TrackID id = dataPoint.getTrackID();
			int indexOfMatch = dataPoint.getIndex();
			int diff = index - indexOfMatch;//the index difference between the probe to match and the matching probe.
			if(!matchResults.containsKey(id)){//matching song not yet in map
				Histogram h = new Histogram();
				h.matchAt(diff);
				matchResults.put(id, h);
			} else {//the specific song already has a match
				Histogram h = matchResults.get(id);
				h.matchAt(diff);
				matchResults.put(id, h);				
			}
		}		
	}
	/**
	 * Gets the name of the file queried to be matched. 
	 * @return The name of the file queried.
	 */
	public String getFileMatched()
	{
		return fileMatched;
	}
	
	/**
	 * Facilitates iterating over the entire match results object.
	 * @return An iterator.
	 */
	public Iterator<TrackID> getContentsIterator()
	{
		return matchResults.keySet().iterator();
	}
	/**
	 * Prints out contents in matchResults
	 */
	public void print()
	{
		for(TrackID id : matchResults.keySet()){
			int ID = id.getIntID();
			Histogram h = matchResults.get(id);
			System.out.print("TrackID: " + ID + " ");
			h.print();			
		}		
	}
	
	/**
	 * Get a histogram at the specified index key.
	 * @param id The TrackID index.
	 * @return A histogram at the id.
	 */
	public Histogram getHistogramAt(TrackID id)
	{
		return matchResults.get(id);
	}
}
