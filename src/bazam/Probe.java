package bazam;
import java.io.Serializable;

import javax.swing.JOptionPane;
/**
 * This class represents a probe that the program will collect for each audio file.
 * 
 * @author Brook
 *
 */
public class Probe implements Comparable<Probe>, Serializable 
{
	/** Frequency of the first peak */
	private int firstFrequency;
	
	/** Frequency of the second peak */
	private int secondFrequency;
	
	/** Time difference between the two peaks */
	private int dt;
	
	/** The two peaks must be separated by this time offset */
	public static int TIME_OFFSET = 10;
	
	/** The two peaks must be also separated by this frequency value. */
	public static int FREQ_OFFSET = 5;
	
	/**
	 * Constructs a probe from two peaks. The first is the anchor peak, which is the base value from which 
	 * the first frequency will be constructed.
	 * @param anchorPeak First peak.
	 * @param secondPeak Second peak.
	 */
	public Probe(Peak anchorPeak, Peak secondPeak)
	{
		firstFrequency = anchorPeak.getFrequency();
		secondFrequency = secondPeak.getFrequency();
		dt = Math.abs(secondPeak.getTime() - anchorPeak.getTime());		
	}
	
	/** 
	 * Increases the selectivity of the probes. There will be less probes.
	 */
	public static void increaseSelectivity()
	{
		TIME_OFFSET--;
		FREQ_OFFSET--;
		if(TIME_OFFSET <= 0 || FREQ_OFFSET <= 0){
			TIME_OFFSET = 1;
			FREQ_OFFSET = 1;
			JOptionPane.showMessageDialog(null, "Reached maximum selectivity of probes.", "Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Increased the selectivity of probes.");
		}
	}
	
	/**
	 * Decreases the selectivity of the probes. There will be more probes.
	 */
	public static void decreaseSelectivity()
	{
		TIME_OFFSET++;
		FREQ_OFFSET++;
		if(TIME_OFFSET > 10 || FREQ_OFFSET > 5){
			TIME_OFFSET = 10; 
			FREQ_OFFSET = 5;	
			JOptionPane.showMessageDialog(null, "Reached minimum selectivity of probes.", "Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Decreased the selectivity of probes.");
		}
	}
	
	/**
	 * Checks for equality of two probes
	 */
	public boolean equals (Object otherObject)
	{
		if(otherObject == null)return false;
		if(this.getClass() != otherObject.getClass())return false;
		Probe otherProbe = (Probe) otherObject;		
		if(this == otherProbe) return true;
		if(this.dt == otherProbe.dt && this.firstFrequency == otherProbe.firstFrequency
				&& this.secondFrequency == otherProbe.secondFrequency) return true;
		return false;
	}
	
	/**
	 * Get the frequency of the first peak.
	 * @return The frequency value of the first peak.
	 */
	public int getFirstFrequency()
	{
		return firstFrequency;
	}
	
	/** 
	 * Gets the frequency of the second peak.
	 * @return The frequency value of the second peak.
	 */
	public int getSecondFrequency()
	{
		return secondFrequency;
	}
	
	/**
	 * Gets the time difference between the two peaks.
	 * @return The time difference between the two peaks.
	 */
	public int getDt()
	{
		return dt;
	}

	/**
	 * Compares two probes.
	 * @param o The object to be compared
	 * @return 0 if equal, -1 if this < o, 1 if this > 0.
	 */
	public int compareTo(Probe o) {
		if(this.dt > o.dt)return 1;
		if(this.dt < o.dt)return -1;
		if(this.firstFrequency > o.firstFrequency)return 1;
		if(this.firstFrequency < o.firstFrequency)return -1;	
		if(this.secondFrequency > o.secondFrequency)return 1;
		if(this.secondFrequency < o.secondFrequency)return -1;
		return 0;
	}	
	
	/**
	 * Computes the hash value of the probe. When we serialize the 
	 * map, we no longer need to know the exact values of the frequencies 
	 * or the changes in time. We match probes by simply looking at their hash values.
	 */
	public int hashCode()
	{
		int factor1 = 13;
		int factor2 = 17;
		int factor3 = 23;
		int a = dt*factor1;
		int b = (firstFrequency * factor2);
		int c = (secondFrequency * factor3);
		int result = a+b+c;
		//System.out.println(result);
		return result;
	}
	
	/**
	 * Get a string representation of this object.	
	 */
	public String toString()
	{
		return(dt + " " + firstFrequency + " " + secondFrequency);
	}
	
	/**
	 * This is the value that will be used to store the probe into the hash.
	 */
	public int getBits()
	{
		int hashCode = hashCode();
		return hashCode();
		
	}
}
