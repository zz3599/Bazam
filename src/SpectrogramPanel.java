

import java.util.*;
import java.io.*;
import javax.swing.*;


import java.awt.*;
/**
 * Displays the spectrogram that the user selects to play.
 * It is separated from the actual object that contains all the 
 * spectra details.
 * @author Brook
 *
 */
public class SpectrogramPanel extends JPanel
{	
	/** The spectrogram contains the spectrum objects */
	private Spectrogram spectrogram;
	
	/** The Signal selected by the user */
	private Signal signal;
	
	/** Original set of complete samples of audio */
	private double[] samples;
	
	/** The scale of the waveform	 */
	protected double hscale = 0.1;
	
	/** The scale of the waveform vertically - each frequency bin will be this thick */
	protected double vscale = 250;
	
	/** The number of total spectra in the object */
	private int numberOfSpectra;	
		
	/** The frequency at which the signal was sampled */
	private double frameRate;
	
	/** List of all the peaks inside the spectrogram */
	private ArrayList<Peak>peaks;
	
	/** Name of the clip - used for creating TrackInfo objects */
	private String name;
	
	/**
	 * Constructs a spectrogram panel which paints the spectrogram.
	 * @param signal The signal from which the data will be calculated.
	 */
	public SpectrogramPanel(Signal signal)
	{
		super();
		this.signal = signal;
		name = signal.getName();
		frameRate = signal.getFrameRate();
		
		samples = signal.getSamples();		
		spectrogram = new Spectrogram(signal);
		numberOfSpectra = spectrogram.size();	
		
		double width = samples.length*hscale;
		setPreferredSize(new Dimension((int)width,WaveformPanel.HEIGHT));
		setBackground(Color.WHITE);	
		revalidate();
		repaint();		
	}
	
	/**
	 * Paints the panel.
	 * @param g
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	
		double totalWidth = samples.length*hscale;
		setPreferredSize(new Dimension((int) totalWidth, WaveformPanel.HEIGHT));
		for(int i = 0; i < spectrogram.size(); i++){
			PowerSpectrum s = spectrogram.getPowerSpectrumAt(i); 				
			double maxPower = s.getMaxPower();//gets the max power for the entire time interval
			for(int j = 0; j < Spectrogram.SAMPLE_SIZE/2; j++){				
				double x = ((double)i/(double)numberOfSpectra);
				//double x1 = (x*getWidth());
				double x1 = (x*totalWidth);
				x = ((double)(i+1)/(double)numberOfSpectra);//the next value
				//double x2 = x*getWidth();
				double x2 = x*totalWidth;
				int width = (int)(x2 - x1);
				if (width == 0)//error check to see if the width turned out to be zero
					width = 1;
				double dy = ((double)getHeight()/(Spectrogram.SAMPLE_SIZE/2.0));
				double y = j*dy;
				y = getHeight() - y - dy;	//because the first frequency starts at the bottom		
				double power = s.getPowerAt(j); // the intensity at the index
				float ratio = (float) (power/maxPower); //to be used for setting the color;				
				g.setColor(new Color(ratio, ratio, ratio));
				g.drawRect((int)x1, (int) y, width, (int)dy );
				g.fillRect((int)x1, (int) y, width, (int)dy );						
			}
		}		
		
		spectrogram.extractPeaks();
		peaks = spectrogram.getAllPeaks();
		
		//now draw dots for the peaks in the spectrogram
		g.setColor(Color.yellow);
		for(int i = 0; i < peaks.size(); i++){
			//Peak peak = peaks.get(i);
			Peak peak = peaks.get(i);
			double x = peak.getTime();//TODO: check if multiply by sample_rate is ok
			x = x * (double) totalWidth/ (double)(numberOfSpectra * Spectrogram.SAMPLE_SIZE);
			double y = (double)peak.getFrequency()/((double)Spectrogram.SAMPLE_SIZE/2.0); 
			y = y * (double)getHeight();
			y = (double)getHeight() - y;
			g.drawOval((int)x,(int)y,2,2);
			g.fillOval((int)x,(int)y,2,2);			
		}		
	}
	
	/**
	 * Zooms in to the spectrogram panel.
	 */
	public void zoomIn()
	{
		hscale *= 1.5;
		revalidate();
		repaint();
	}
	
	/** 
	 * Zooms out of the spectrogram panel.
	 */
	public void zoomOut()
	{
		hscale /= 1.5;
		revalidate();
		repaint();
	}
	
	/**
	 * Resets the graph of the spectrogram.
	 */
	public void resetZoom()
	{
		hscale = WaveformPanel.HSCALE;
		revalidate();
		repaint();
	}
}
