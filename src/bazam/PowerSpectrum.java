package bazam;
import java.io.*;
import java.util.*;
import edu.emory.mathcs.jtransforms.fft.*;
/** 
 * A spectrum represents a Fourier transformed sample of bytes through a particular time interval.
 * It contains the power of each spectra at the particular time domain
 * @author Brook
 *
 */
public class PowerSpectrum 
{
	/** The samples after calling Fast Fourier Transform, which are now in frequency domain instead of time domain */
	private double[] transformedSamples;
		
	/** The average power of all the spectrum in this spectrum */
	private double averagePower;
	
	/** The frequencies where the power is greater than the average power. Inserted into sequentially based on frequency */
	private ArrayList<Integer>validFrequencies;
	
	/** The sample ID (spectrum number) of the spectrum at the location generated  */
	private int spectrumNumber;
	
	/** List of all the powers for the given spectrum. It is half the size of the sampled segment. Indexed by frequency, contains power values */
	private double[] freqPower = new double[Spectrogram.SAMPLE_SIZE/2];
	
	/** This constant ensures that peaks must be a certain amount greater than neighboring values in order to be considered a peak. */
	public static double PEAK_THRESHOLD = 1.25;
	
	/** A list of all the peaks in the spectrogram */
	private ArrayList<Peak> peaks = new ArrayList<Peak>();
	
	/**
	 * Constructs a spectrum for the selected sample.
	 * @param samplesToBeTransformed The selected sample to be transformed. Depends on the SPECTRUM_SIZE 
	 * @param spectrumNumber The sample location at which the spectrum was generated. 
	 */
	public PowerSpectrum(double[] samplesToBeTransformed, int spectrumNumber) 
	{
		this.spectrumNumber = spectrumNumber;		
		DoubleFFT_1D d = new DoubleFFT_1D(Spectrogram.SAMPLE_SIZE);
		transformedSamples = samplesToBeTransformed;
		d.complexForward(transformedSamples);
		computePower();
		calculateAveragePower();
		filterPeaks();				
	}
	
	/**
	 * Cycles through the array of power values and gets rid of all the values less than the average power 
	 * for the interval.
	 */
	private void filterPeaks()
	{
		validFrequencies = new ArrayList<Integer>();//list of frequencies with power > average power
		for(int frequency = 0; frequency < freqPower.length; frequency++){
			double thisPower = freqPower[frequency];
			if(thisPower > averagePower){
				validFrequencies.add(frequency);//add to the list of valid frequencies if greater than average power				
			}
		}
		getLocalPeaks();	
	}
	
	/**
	 * Extracts the local peaks from the spectrum at this particular time interval.
	 */
	private void getLocalPeaks()
	{
		for(int i = 0; i < validFrequencies.size(); i++){			
			try{
				if(isPeakAt(i)){			
					int frequency = validFrequencies.get(i);//get the frequency at the particular index
					Peak peak = new Peak(spectrumNumber, frequency, freqPower[frequency]);
					//System.out.println(spectrumNumber + " " + frequency);
					peaks.add(peak);
					//System.out.println(peak.getTime() + " " +  peak.getFrequency());
				}
			} catch(IndexOutOfBoundsException e){
				//do nothing
			}
		}
	}
	/**
	 * Computes the power for each of the Spectrogram.SAMPLE_SIZE/2 frequency bins.
	 */
	private void computePower() 
	{
		for(int i = 0; i < Spectrogram.SAMPLE_SIZE; i+= 2){
			double ai = Math.pow(transformedSamples[i], 2);
			double bi = Math.pow(transformedSamples[i+1], 2);
			double power = ai + bi;
			power = Math.sqrt(power);
			freqPower[i/2] = power;
		}
	}
	
	/** 
	 * Determines if a power value is a peak at the spectrum. Compares it with the two frequency bins above and below.
	 * @param index The index location to test inside the current list of valid frequencies.
	 * @return True if it is a peak at that frequency, else false.
	 * @throws IndexOutOfBoundsException If the given index forces a comparison out of bounds
	 */
	private boolean isPeakAt(int index) throws IndexOutOfBoundsException
	{		
		int frequency = validFrequencies.get(index);//get the frequency at the point.
		double power = freqPower[frequency];
		
		for(int i=(-3+index); i<=(3+index);i++){
			if(i>=0 && i< validFrequencies.size() && i!=index){
				int oFrequency = validFrequencies.get(i);
				double oPower = freqPower[oFrequency];
				double dp = power - oPower;
				if(dp < PEAK_THRESHOLD)return false;
			}
		}
		return true;		
	}	
	
	/** 
	 * Gets the max power value of the entire spectrum.
	 * @return The maximum power value.
	 */
	public double getMaxPower()
	{
		double max = Double.MIN_VALUE;//the minimum power value
		for(int i = 0; i < freqPower.length; i++){
			double power = freqPower[i];
			if(power > max) max = power;
		}
		return max;
	}

	/**
	 * Get the Fourier Transformed data of the spectrum.
	 * @return The transformed samples.
	 */
	public double[] getSamples()
	{
		return transformedSamples;
	}
		
	/**
	 * Get the sample location of the spectrum - the index of the spectrum.
	 * @return The sampled (not real time) location of the spectrum.
	 */
	public int getSpectrumNumber()
	{
		return spectrumNumber;
	}
	
	/**
	 * Gets the power at the given index, which must be a frequency.
	 * @param index The index for which we must find the power for.
	 * @return The power at the selected point.
	 */
	public double getPowerAt(int index)
	{
		return freqPower[index];
	}
	
	/**
	 * Gets the number of elements in the frequency bin containing the power values.
	 * @return The total number of elements.
	 */
	public int getPowerSize()
	{
		return freqPower.length;
	}
	
	/**
	 * Gets the list of peaks inside this spectrum.
	 * @return The list of peaks.
	 */
	public ArrayList<Peak> getPeaks()
	{
		return peaks;
	}
	
	/** 
	 * Gets the average power of all the power in this particular time interval.
	 * Used to determine the peak.
	 */
	private void calculateAveragePower()
	{
		double totalPower = 0;
		double totalSamples = freqPower.length;
		for(int i = 0; i < totalSamples; i++){
			totalPower = totalPower + freqPower[i];			
		}
		averagePower = totalPower/totalSamples;	
	}
	
	/**
	 * Gets the average power of the spectrum
	 * @return The average power at this particular time.
	 */
	public double getAveragePower()
	{
		//System.out.println(averagePower);
		return averagePower;
	}
}
