package bazam;

/**
 * Wrapper class that contains the audio data stream of the audio clip selected.
 * @author Brook
 *
 */
public class Signal implements Runnable
{
	/** The samples of this signal object */
	private double[] samples;
	
	/** The frame rate (frames per second). Also known as frequency */
	private double frameRate;
	
	/** Length of the signal in seconds */
	private double length;
	
	/** The audio clip selected */
	private AudioClip audioClip;
	
	/** The name of the signal */
	private String name;
	
	/** The static frame rate */
	public static double FRAMERATE;
	
	/** The frequency in hertz per frequency bin */
	public static double HZ_PER_FREQ_BIN;
	
	/**
	 * Constructs a signal object from the audio clip.
	 * @param audioClip The audio clip whose data will be passed into this signal.
	 * @param name The name of the audio clip.
	 */
	public Signal(AudioClip audioClip, String name)
	{
		this.audioClip = audioClip;		
		this.name = name;
		samples = audioClip.getSamples();
		frameRate = audioClip.getFrameRate();
		HZ_PER_FREQ_BIN = frameRate/Spectrogram.SAMPLE_SIZE;	
		length = samples.length/frameRate;	 
	}

	/**
	 * Gets the samples contained inside the signal
	 * @return The samples of the signal.
	 */
	public double[] getSamples()
	{
		return samples;
	}	
	
	/**
	 * Gets the length of the signal.
	 * @return In seconds.
	 */
	public double getLength()
	{
		return length;
	}
	
	/**
	 * Gets the frames per second of the underlying audioClip.
	 * @return The frame rate.
	 */
	public double getFrameRate()
	{
		return frameRate;
	}

	/**
	 * Plays the audio file.
	 */
	public void run() 
	{
		try{
			audioClip.play();
			Thread.sleep(1000);
		} catch(Exception e){
		}
	}
	
	/**
	 * Gets the name of the underlying audio clip.
	 * @return The string name of the clip.
	 */
	public String getName()
	{
		return name;		
	}
	
	/**
	 * Gets the total length of the track.
	 * @return The length of the bytes.
	 */
	public long getSamplesLength()
	{
		return samples.length;
	}
	
}
