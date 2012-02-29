/**
 * Represents the maximum match inside a given histogram. Will be used to find matches.
 * @author Brook
 *
 */
public class MaxMatch 
{
	/** Time difference between the matches */
	public int delta;
	
	/** Number of matching hashes */
	public int numberOfMatches;
	
	/**
	 * Constructs a new match with the maximum matches in its histogram.
	 * @param delta Time difference.
	 * @param numberOfMatches Number of matches.
	 */
	public MaxMatch(int delta, int numberOfMatches)
	{
		this.delta = delta;
		this.numberOfMatches = numberOfMatches;		
	}

}
