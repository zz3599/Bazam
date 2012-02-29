import java.util.*;
/**
 * This class represents a map of the number of probes that matched mapped to the 
 * time offset (dt)  between the probe to match and the probe in the constructed index. There is 
 * one histogram for each track in the index. 
 * @author Brook
 *
 */
public class Histogram 
{
	/** List of all the matches. Key is the time offset, value is the number of matches the time offset */
	private HashMap<Integer,Integer>matches;
	
	/**
	 * Creates a histogram
	 */
	public Histogram()
	{
		matches = new HashMap<Integer,Integer>();		
	}
	
	/**
	 * Increase the count of matches at the given time difference
	 * @param dt
	 */
	public void matchAt(int dt)
	{
		if(matches.containsKey(dt)){//there is an identical time difference already found
			int count = matches.get(dt);
			count++;
			matches.put(dt,count);
		} else {//the time difference is new
			int count = 1; //new count
			matches.put(dt, count);
		}		
	}
	
	/**
	 * Gets the number of matches for a particular index difference.
	 * @param dt Difference in the index.
	 * @return The number of matches.
	 */
	public int getCount(int dt)
	{
		return (matches.get(dt));
		
	}
	
	/**
	 * Gets the combined values of all the matches in the histogram.
	 * @return The total matches for at all the dt's of the particular histogram.
	 */
	public int getTotalMatches()
	{
		int result = 0;
		for(Integer i : matches.keySet()){
			int matchTally = matches.get(i).intValue();
			result+=matchTally;
		}
		return result;
	}
	
	/**
	 * Gets the maximum match at any given delta, and returns that as a <delta, number matched> pair
	 * @return An object that contains the maximum match inside the histogram.
	 */
	public MaxMatch getMaxMatch()
	{
		MaxMatch result;
		int maxTally = Integer.MIN_VALUE;
		int delta = 0;
		for(Integer i : matches.keySet()){
			int matchTally = matches.get(i).intValue();
			if(matchTally > maxTally){
				maxTally = matchTally;	
				delta = i;
			}
		}
		result = new MaxMatch(delta,maxTally);
		return result;		
	}
	/**
	 * Prints out the contents of the histogram.
	 */
	public void print()
	{
		for(Integer delta : matches.keySet()){			
			int matchCount = matches.get(delta);
			System.out.println("Delta " + delta/Spectrogram.SAMPLE_SIZE + " Matches: " + matchCount);
		}		
	}
	
	/**
	 * Gets a string representation of the contents.
	 * @return The string representation of all the match results of this histogram.
	 */
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		for(Integer delta : matches.keySet()){			
			int matchCount = matches.get(delta);
			s.append("Delta " + delta/Spectrogram.SAMPLE_SIZE + " / Matches: " + matchCount + "\n");
		}	
		return s.toString();
		
	}
}
