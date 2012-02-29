import java.util.*;
/**
 * Extracts probes from the particular spectrogram and places it into a hash.
 * @author Brook
 */
public class SpectrogramProbesExtractor 
{
	/** The spectrogram underlying the extractor */
	private Spectrogram spectrogram;
	
	/** The list of all the peaks */
	private ArrayList<Peak> p;
	
	private HashMap<Probe,ProbeDataPoint>mapping;
	
	/** List of hashpoints */
	private ArrayList<HashPoint>hashPoints;
	
	/** The name of the track from which the probes will be extracted */
	private String name;
	
	
	/**
	 * Constructs an object.
	 * @param spectrogram The spectrogram from which we will compute the probes
	 */
	public SpectrogramProbesExtractor(Spectrogram spectrogram)
	{
		this.spectrogram = spectrogram;	
		name = spectrogram.getSignal().getName();
		p = spectrogram.getAllPeaks();	
		generateProbes();
	}
	
	/**
	 * Generates all the probes and saves it into file.
	 */
	public void generateProbes()
	{
		hashPoints = new ArrayList<HashPoint>();
		for(int i = 0; i < p.size(); i++){
			Peak peak = p.get(i);
			createProbe(peak,i);
		}
	}
	
	/**
	 * Constructs a probe(if possible) by using the parameters specified by the user. 
	 * The target region is a rectangle bounded by the TIME_OFFSET along the time axis and the FREQ_OFFSET
	 * along the frequency bins.
	 * @param anchorPeak The anchor peak.
	 */
	private void createProbe(Peak anchorPeak, int baseIndex)
	{
		//First get the frequency and time of the anchor peak. They will serve as the lower bounds.
		int anchorFrequency = anchorPeak.getFrequency();
		int anchorTime = anchorPeak.getTime();		
		//Now extract probes
		int targetTimeBound = anchorTime + Probe.TIME_OFFSET*Spectrogram.SAMPLE_SIZE;//upper bound of time
		int targetFreqBound = anchorFrequency + Probe.FREQ_OFFSET;//upper bound of frequency
		//Start searching from the base index - take advantage of the fact that peaks are inserted sequentially by index
		for(int i = baseIndex; i < p.size(); i++){
			Peak peak = p.get(i);
			int otherTime = peak.getTime();
			int otherFreq = peak.getFrequency();
			if(otherTime > targetTimeBound)return;//we have jumped out of the target zone.
			
			//If the same time and/or frequency, invalid peak. If frequency out of bounds, invalid peak.
			if(otherFreq < anchorFrequency || otherFreq > targetFreqBound || otherTime == anchorTime 
					|| otherFreq == anchorFrequency) continue;
			//otherwise
			Probe probe = new Probe(anchorPeak, peak);
			HashPoint aHash = new HashPoint(probe,anchorTime);
			hashPoints.add(aHash);													
		}		
		
	}
	
	/**
	 * Gets the list of hash points inside the extractor.
	 * @return The list of hash points.
	 */
	public ArrayList<HashPoint> getHashPoints()
	{
		return hashPoints;
	}

	/**
	 * Gets the hash map of probes to their data points.
	 * @return The mapping.
	 */
	public HashMap<Probe,ProbeDataPoint> getProbeMapping()
	{
		return mapping;
	}

}
