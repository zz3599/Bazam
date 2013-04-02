package bazam;

/**
 * Hashes the probe object inside using a Probe and the index location of the 
 * anchor peak within the track. This allows the probes extractor to not have to worry about the 
 * TrackID.
 * @author Brook
 *
 */
public class HashPoint 
{
	/** The probe inside the hash point */
	public Probe probe;
	
	/** The location of the anchor peak inside the probe, in terms of spectrogram index */
	public int index;
	
	/**
	 * Constructs a has point.
	 * @param aProbe The probe inside.
	 * @param anIndex The index at which it is located in the spectrogram. AKA the index location of the anchor peak.
	 */
	public HashPoint(Probe aProbe, int anIndex)
	{
		probe = aProbe;
		index = anIndex;
	}

}
