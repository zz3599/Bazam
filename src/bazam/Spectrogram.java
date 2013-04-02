package bazam;
import java.util.*;
/**
 * A spectra object contains a collection of PowerSpectrum objects. It will extract the peaks only 
 * when the method is called.
 * @author Brook
 *
 */
public class Spectrogram 
{
	/** The initial samples obtained from the signal */
	private double[] initialSamples;
	
	/** The collection of PowerSpectrum objects, which each represent a particular time domain */
	private PowerSpectrum[] spectrums;
	
	/** Collection of Peak objects */
	private ArrayList<Peak> globalPeaks = new ArrayList<Peak>();
	
	/** The signal underlying the spectrogram */
	private Signal signal;
	
	/** The sample size per spectrum*/
	public static int SAMPLE_SIZE = 1024;
	
	/** The total number of PowerSpectrum objects inside the spectrogram */
	private int numberOfSpectra;
	
	/** The size of the power spectrum array is half the number of samples per interval. */
	public static int POWER_SIZE = SAMPLE_SIZE/2;	
	
	/**
	 * Constructs a spectrogram using the array of samples from the audio clip. 
	 * @param signal The signal from which to construct spectrogram from.
	 */
	public Spectrogram(Signal signal)
	{
		initialSamples = signal.getSamples();
		this.signal = signal;
		double[] testSample = new double[2*SAMPLE_SIZE];
		numberOfSpectra = (int)(initialSamples.length/SAMPLE_SIZE);//this will prevent access out of bounds				
		spectrums = new PowerSpectrum[numberOfSpectra];		
		//Initialize the samples to be transformed
		int spectrumNumber = 0;
		for(int i = 0; spectrumNumber < numberOfSpectra; i += SAMPLE_SIZE){			
			for(int j = 0; j < SAMPLE_SIZE; j++){	
				testSample[j*2] = initialSamples[spectrumNumber*SAMPLE_SIZE+j];
				testSample[j*2+1] = 0;
			}
			PowerSpectrum aPowerSpectrum = new PowerSpectrum(testSample, i); //TODO: TO REFLECT INDEX, use spectrumNumber?
			//Spectrogram.getSignalID(spectrumNumber);
			spectrums[spectrumNumber] = aPowerSpectrum;
			spectrumNumber++;			
		}			
		extractPeaks();
	}	
	
	/**
	 * Gets the average power at the specified index
	 * @param index The spectra number.
	 * @return The average power at that location.
	 */
	private double getAveragePowerAt(int index)
	{
		return spectrums[index].getAveragePower();
	}
	
	/**
	 * Extracts the peaks inside the spectrogram from each frequency bin by filtering
	 * the values that are less than the corresponding values in neighboring spectra.
	 */
	public void extractPeaks()
	{
		for(int index = 0; index < numberOfSpectra-1; index++){//loop through each spectrum.
			PowerSpectrum s = spectrums[index];
			ArrayList<Peak>localPeaks = s.getPeaks();//each spectrum's local peaks
			for(int j = 0; j < localPeaks.size(); j++){//loop through each peak of each spectrum.
				Peak peak = localPeaks.get(j);
				int frequency = peak.getFrequency();
				double power = s.getPowerAt(frequency);
				if(checkAdjacentSpectra(index, frequency, power))
					globalPeaks.add(peak);
			}			
		}	
		/*for(Peak peak :globalPeaks){
			System.out.println(peak.getTime() + " " + peak.getFrequency() + " ");
		}*/
	}	
	
	/**
	 * Checks the given power value with the those of the adjacent areas
	 * @param index The index location on the spectrogram(not real time)
	 * @param frequency The frequency.
	 * @param power The power value
	 * @return Whether the local peak is still considered a global peak. True if it still is, false otherwise.
	 */
	private boolean checkAdjacentSpectra(int index, int frequency, double power)
	{
		//Check the power at the spectrum one sample length ahead
		for(int i = (-3+index); i <= (3+index); i++){
			if(i >=0 && i < numberOfSpectra && i!=index){
				PowerSpectrum oPowerSpectrum = spectrums[i];
				double oPower = oPowerSpectrum.getPowerAt(frequency);
				if( (power - oPower) < PowerSpectrum.PEAK_THRESHOLD)return false;
				if( (power - getAveragePowerAt(i)) < PowerSpectrum.PEAK_THRESHOLD)return false;	
			}
		}
		return true;	
	}
	
	/**
	 * Gets all the peaks from the spectrogram. The peaks are already in order, first by time of occurrence, 
	 * then by the frequency values. No sort was implemented. The peaks were added simply from one end of the spectrogram 
	 * to the other. The returned list therefore can be used without sorting.
	 * @return The list of peaks.
	 */
	public ArrayList<Peak>getAllPeaks()
	{
		return globalPeaks;
	}	
	
	/**
	 * Returns a PowerSpectrum object at the specified location
	 * @param index The index of the PowerSpectrumobject.
	 * @return The PowerSpectrumat the index location.
	 */
	public PowerSpectrum getPowerSpectrumAt(int index)
	{
		return spectrums[index];
	}
	
	/**
	 * Gets the number of PowerSpectrumobjects inside this spectrogram
	 * @return the Size of the spectrogram.
	 */
	public int size()
	{
		return spectrums.length;
	}	
	
	/**
	 * Get the signal underlying the track.
	 * @return The name.
	 */
	public Signal getSignal()
	{
		return signal;
		
	}
	
}
