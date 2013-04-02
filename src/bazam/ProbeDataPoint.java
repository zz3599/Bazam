package bazam;

/**
 * Each probe has a specific probeDataPoint mapped to it. This value is unique, because
 * the index of each probe inside the spectrogram is different, and the trackID is different
 * for those probes that have the same index. 
 * @author Brook
 *
 */
public class ProbeDataPoint 
{
	/** A class that contains an integer to map it to a specific clip */
	private TrackID trackID;
	
	/** The index location of the probe inside the audio clip */
	private int index;
	
	/**
	 * Constructs a probeDataPoint, located at the specified index in the audio clip, with the 
	 * given trackID. 
	 * @param id The track ID of the clip from which the probe was created.
	 * @param index The index of the probe inside the clip.
	 */
	public ProbeDataPoint(TrackID id, int index)
	{
		trackID = id;
		this.index = index;		
	}
	
	/**
	 * Gets trackID of the ProbeDataPoint.
	 * @return TrackID.
	 */
	public TrackID getTrackID()
	{
		return trackID;
	}

	/**
	 * Gets the index of the ProbeDataPoint
	 * @return The index of the data point inside the audio clip.
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * Returns a string representation of the object.
	 */
	public String toString()
	{
		return (trackID.getIntID() + " index: " + index );
	}
	
	/**
	 * Calculates the hash code for the object.
	 */
	public int hashCode()
	{
		int c = 13;
		int c2 = 23;
		return(trackID.getIntID()*c + index*c*c);
	}
	
	/**
	 * Determines whether this is equal to some other object.
	 * @return True if equal, false otherwise.
	 */
	public boolean equals(Object otherObject)
	{
		if(otherObject==null)return false;
		if(otherObject.getClass()!=this.getClass())return false;
		ProbeDataPoint oDataPoint = (ProbeDataPoint)otherObject;
		if(this==oDataPoint)return true;
		if(this.trackID==oDataPoint.trackID && this.index==oDataPoint.index)return true;
		return false;
	}
}
