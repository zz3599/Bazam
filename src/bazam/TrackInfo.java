package bazam;
/**
 * TrackInfo is the mapped value for each TrackID. TrackInfo contains both 
 * the trackID ( track number in the index )
 * and the stringed description.
 * @author Brook
 *
 */
public class TrackInfo implements java.io.Serializable
{
	
	/** The track ID of the probe*/
	public TrackID oTrackID;
	
	/** Description of the track */
	private String description;
	
	/** Number of hash points for the particular id */
	public int numberHashPoints;
	
	
	/**
	 * Constructs a trackInfo object.
	 * @param trackID The index of the track inside TrackMap
	 * @param desc Description of the track.
	 */
	public TrackInfo(TrackID trackID, String desc)
	{
		oTrackID = trackID;
		description = desc;
	}
	
	/**
	 * Gets the ID of the track
	 * @return The name.
	 */
	public TrackID getTrackID()
	{
		return oTrackID;
	}
	
	/**
	 * Gets the description of the probe.
	 * @return String description.
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Sets the number of hash points to the given value.
	 * @param number The number of hash points to set to.
	 */
	public void setNumberHashPoints(int number)
	{
		numberHashPoints = number;
	}
	
	/**
	 * Gets the number of hash points inside.
	 * @return The number of hash points extracted from given track.
	 */
	public int getNumberHashPoints()
	{
		return numberHashPoints;
	}
	
	/**
	 * Tests for equality of two TrackInfo objects.
	 */
	public boolean equals(Object otherObject)
	{
		if(otherObject == null)return false;
		if(this.getClass() != otherObject.getClass())return false;
		TrackInfo otherTrackInfo = (TrackInfo)otherObject;
		if(this == otherTrackInfo)return true;
		if(this.oTrackID == otherTrackInfo.getTrackID() && this.description == otherTrackInfo.description)return true;
		return false;
	}
	
	/**
	 * Gets a string representation of the object.
	 */
	public String toString()
	{
		return("Name: "  + description + " / Number Hash Points: " + numberHashPoints);		
	}
}
