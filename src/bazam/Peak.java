package bazam;

/**
 * Represents a (time, frequency) point in the spectrogram that is a kind of local maximum
 * in the sense that there is higher power at his location than at other nearby points.
 * @author Brook
 *s
 */
public class Peak implements Comparable<Peak>
{	
	/** The index location of the peak */
	private int time;
	
	/** The frequency of the peak */
	private int frequency;	
	
	/** The power of the peak */
	private double power;
	
	/**
	 * Constructs a peak. 
	 * @param time The index location of peak.
	 * @param frequency The frequency of the peak.
	 */
	public Peak(int time, int frequency, double power)
	{
		this.time = time;
		this.frequency = frequency;
		this.power = power;
	}
	
	/**
	 * Gets the 'time' location of the peak. It is the index of the peak inside the spectrogram.
	 * @return The 'time' (index) value.
	 */
	public int getTime()
	{
		return time;
	}
	
	/**
	 * Gets the frequency of the peak.
	 * @return The frequency value.
	 */
	public int getFrequency()
	{
		return frequency;
	}
		
	/**
	 * Checks for equality of two peaks.
	 */
	public boolean equals(Object otherObject)
	{
		if(otherObject == null) return false;
		if(this.getClass() != otherObject.getClass())return false;
		Peak otherPeak = (Peak)otherObject;
		if(this == otherObject) return true;
		if(this.frequency == otherPeak.frequency && this.time == otherPeak.time) return true;
		return false;			
	}

	/**
	 * Compares two peaks.
	 * @param o The object to be compared
	 * @return 0 if equal, -1 if this < o, 1 if this > 0.
	 */
	public int compareTo(Peak o) {
		if(this.time > o.time)return 1;
		else if(this.time < o.time)return -1;	
		else {
			if(this.frequency > o.frequency) return 1;
			if(this.frequency < o.frequency) return -1;
			return 0;
		}		
	}
	
	/**
	 * Return a string representation of the peak object		
	 */
	public String toString()
	{
		return (time + " " + frequency + " " + power);
	}

}
