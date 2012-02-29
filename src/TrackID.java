
/**
 * Each track in the index has a unique trackID object.
 * @author Brook
 *
 */
public class TrackID implements java.io.Serializable
{
	/** The integer representation of the track */
	private int trackID;

	public TrackID(int id)
	{
		trackID = id;		
	}
	
	/** 
	 * Gets the track ID value.
	 * @return The track ID value.
	 */
	public int getIntID()
	{
		return trackID;			
	}
	
	/** 
	 * Returns the hash code of the trackID object.
	 */
	public int hashCode()
	{
		int c = 13;
		return trackID*c;		
	}
	
	/**
	 * Tests to see if the TrackID object matches the other object.
	 */
	public boolean equals(Object otherObject)
	{
		if(otherObject == null)return false;
		if(otherObject.getClass()!=this.getClass())return false;
		TrackID otherID = (TrackID)otherObject;
		if(this == otherID)return true;
		if(trackID == otherID.trackID)return true;
		return false;		
	}
	
	/**
	 * Get the string representation of this object
	 */
	public String toString()
	{
		return ("Track ID: " + trackID );
		
	}
	
}
