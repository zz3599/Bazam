import java.io.*;

import javax.swing.*;
import javax.sound.sampled.*;

/**
 * An AudioClip represents the digitized information in an audio clip.
 */

public class AudioClip 
{

    /** Audio format associated with the clip. */
    private AudioFormat format;

    /** Samples making up the clip. */
    private double[] samples;
    
    /** Source data line */
    private SourceDataLine outputLine;    

    /**
     * Create an empty audio clip of a given number of samples.
     *
     * @param length  The number of samples.
     */
    protected AudioClip(int length) {
    	samples = new double[length];
    }

    /**
     * Get the audio format of this clip.
     */
    public AudioFormat getAudioFormat() {
    	return format;
    }
    
    /**
     * Returns the samples for this current audio clip
     * @return The sampled double values. 
     */
    public double[] getSamples()
    {
    	return samples;
    }
    
    /**
     * Gets the frequency at which the clip was sampled at (Frames/second)
     * @return The frame rate.
     */
    public double getFrameRate()
    {
    	return(format.getFrameRate());    	
    }    

    /**
     * Extract an audio clip from an audio input stream.
     *
     * @param in The audio input stream from which to extract the clip.
     * @param name The name of the clip.
     * @return the extracted audio clip.
     * @throws IOException if there is an error reading the input stream.
     */
    public static AudioClip fromStream(AudioInputStream in, String name)
	throws IOException {
	// Verify that the input stream has the format we need.
	// Right now this is 16-bit signed PCM format.
    //System.out.println(in.getFrameLength());
	AudioFormat format = in.getFormat();
	if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
	    throw new IllegalArgumentException("Signed PCM format required.");
	int channels = format.getChannels();
	if(channels != 1 && channels != 2)
	    throw new IllegalArgumentException("Mono or stereo required.");
	int bytesPerFrame = format.getFrameSize();
	int bytesPerSample = bytesPerFrame/channels;
	if(bytesPerSample != 2)
	    throw new IllegalArgumentException("16-bit samples required.");

	boolean bigEndian = format.isBigEndian();
	long length = in.getFrameLength();
	if(length > Integer.MAX_VALUE)
	    throw new IllegalArgumentException("Clip too long");

	// Convert the samples in the original stream to floating point.
	// Stereo is reduced to mono by averaging the channel values.
	AudioClip result = new AudioClip((int)length);
	result.format = format;
	byte[] buf = new byte[bytesPerFrame];
	for(int i = 0; i < length; i++) {
	    double v = 0.0;
	    in.read(buf);
	    for(int j = 0; j < channels; j++) {
		byte b1 = buf[2*j];
		byte b2 = buf[2*j+1];
		if(!bigEndian) {
		    byte tmp = b1;
		    b1 = b2;
		    b2 = tmp;
		}
		int s = ((b1<<8) | (b2&0xff));
		v += (s/32768.0);
	    }
	    v /= channels;
	    result.samples[i] = v;
	}
	return result;
    }

    /**
     * Create an audio input stream from the data in this AudioClip.
     *
     * @param format The audio format to use to encode the data.
     * @return the audio input stream created from the data in this
     * Audio Clip.
     */
    public AudioInputStream toStream() {
	// Verify that the input stream has the format we need.
	// Right now this is 16-bit signed PCM format.
	if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
	    throw new IllegalArgumentException("Signed PCM format required.");
	int channels = format.getChannels();
	if(channels != 1 && channels != 2)
	    throw new IllegalArgumentException("Mono or stereo required.");
	int bytesPerFrame = format.getFrameSize();
	int bytesPerSample = bytesPerFrame/channels;
	if(bytesPerSample != 2)
	    throw new IllegalArgumentException("16-bit samples required.");
	boolean bigEndian = format.isBigEndian();

	// Reconstruct PCM samples from the clip and wrap them
	// in an input stream.
	byte[] result = new byte[samples.length * bytesPerFrame];
	int off = 0;
	for(int i = 0; i < samples.length; i++, off += bytesPerFrame) {
	    double v = samples[i] * 32768.0;
	    short s = v > Short.MAX_VALUE ? Short.MAX_VALUE : (short)v;
	    byte b1 = (byte)((s>>8) & 0xff);
	    byte b2 = (byte)(s & 0xff);
	    if(!bigEndian) {
		byte tmp = b1;
		b1 = b2;
		b2 = tmp;
	    }
	    for(int j = 0; j < channels; j++) {
		// If there is more than one channel, duplicate samples.
		result[off+2*j] = b1;
		result[off+2*j+1] = b2;
	    }
	}
	return
	    new AudioInputStream(new ByteArrayInputStream(result),
				 format, samples.length);
    }

    /**
     * Play this audio clip over the audio output device.
     *
     * @throws UnsupportedAudioFileException if the specified format is not
     * supported on this platform.
     * @throws LineUnavailableException if no audio line can be obtained
     * to play the clip.
     * @throws IOException if there is an error while playing the clip.
     */
    public void play()
	throws UnsupportedAudioFileException, LineUnavailableException,
	       IOException {
	AudioInputStream ain = toStream();
	/*SourceDataLine*/ outputLine = AudioSystem.getSourceDataLine(format);
	outputLine.open(format);
	outputLine.start();
	byte[] buf = new byte[1024];
	int n;
	while((n = ain.read(buf)) != -1)
	    outputLine.write(buf, 0, n);
	outputLine.drain();
	// For some reason I have found that closing the line too soon
	// at this point truncates the playing of the clip.  This doesn't
	// make sense, since drain() should have waited for the data
	// to be played, but maybe it is only a problem under FreeBSD.
	try {
	    Thread.sleep(1000);
	} catch(InterruptedException x){
	    // Do nothing.
	}
	outputLine.close();
    }

    /**
     * Open an audio file and create a window to display it.
     */
    private static AudioClip loadClip() {
	JFileChooser d = new JFileChooser();
	d.setDialogTitle("Select Audio File");
	d.setDialogType(JFileChooser.OPEN_DIALOG);
	d.setFileFilter
	    (new javax.swing.filechooser.FileFilter() {
		    private AudioFileFormat.Type[] types =
			AudioSystem.getAudioFileTypes();

		    public boolean accept(File f) {
			String name = f.getName();
			return(f.isDirectory() || supportedType(name));
		    }

		    private boolean supportedType(String name) {
			for(int i = 0; i < types.length; i++) {
			    if(name.endsWith(types[i].getExtension()))
				return true;
			}
			return false;
		    }
		    
		    public String getDescription() {
			return "audio files in supported formats";
		    }
		});
	int ret = d.showOpenDialog(null);
	if(ret == JFileChooser.APPROVE_OPTION) {
	    try {
		File f = d.getSelectedFile();
		AudioInputStream ain = AudioSystem.getAudioInputStream(f);
		AudioClip clip = AudioClip.fromStream(ain, f.getName());
		ain.close();
		return clip;
	    } catch(IOException x) {
		JOptionPane.showMessageDialog
		    (null, "IOException: " + x.getMessage());
	    } catch(UnsupportedAudioFileException x) {
		JOptionPane.showMessageDialog
		    (null, "Unsupported audio file format.");
	    } catch(Throwable x) {
		JOptionPane.showMessageDialog
		    (null, "Error: " + x);
		x.printStackTrace();
	    }
	}
	return null;
    }
    public static void main(String[] args){
    	AudioClip clip = AudioClip.loadClip();
    	try {
			clip.play();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

