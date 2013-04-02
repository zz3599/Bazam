package bazam;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class MatchResultsFrame extends JFrame 
{
	/** The results of the matching process */
	private MatchResults matchResults;
	
	/** The map of trackID to track description */
	private TrackMap trackMap;
		
	/** Menu item to get the top match to the selected clip. */
	private JMenuItem getMatch;
	
	/**
	 * Constructs a new frame to display the match results
	 * @param matchResults The results to display.
	 * @param trackMap The mapping to use.
	 */
	public MatchResultsFrame(MatchResults matchResults, TrackMap trackMap)
	{
		super("Match Results");
		this.matchResults = matchResults;
		this.trackMap = trackMap;
		//histograms = new ArrayList<Histogram>();
		setPreferredSize(new Dimension(400,200));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		getContentPane().add(scrollPane);		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu match = new JMenu("Match");
		menuBar.add(match);
		getMatch = new JMenuItem("Get Top Match");
		match.add(getMatch);
		getMatch.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					getMatch();
				}
			});		
		
		//Displays the contents of the match results
		for(Iterator<TrackID>it = matchResults.getContentsIterator();it.hasNext();){
			TrackID id = it.next();
			Histogram h = matchResults.getHistogramAt(id);
			StringBuilder builder = new StringBuilder("Track ID: " + id.getIntID() + " / Song name: " + 
					trackMap.getTrackInfo(id).getDescription() + "\n");
			builder.append(h.toString());
			textArea.append(builder.toString());
			textArea.append("\n");
		}
		pack();
		setVisible(true);		
	}
	
	/**
	 * Gets the matches of the selected track.
	 */
	public void getMatch()
	{
		int maxNumberOfMatches = Integer.MIN_VALUE;
		double matchRate = 0;//this is the one we use - the percentage of matches at the max match must be the highest percentage
		TrackID matchID = null;
		int timeOffset = 0;
		for(Iterator<TrackID>it = matchResults.getContentsIterator();it.hasNext();){
			TrackID id = it.next();
			Histogram h = matchResults.getHistogramAt(id);
			MaxMatch thisMatch = h.getMaxMatch();
			int totalMatches = h.getTotalMatches();
			int thisNumberOfMatches = thisMatch.numberOfMatches;//at a particular delta
			/*if(thisNumberOfMatches > maxNumberOfMatches){
				maxNumberOfMatches = thisNumberOfMatches;
				matchID = id;
				timeOffset = thisMatch.delta;
			}*/
			double thisMatchRate = (double)((double)thisNumberOfMatches/(double)totalMatches);
			if(thisMatchRate > matchRate){
				maxNumberOfMatches = thisNumberOfMatches;
				matchID = id;
				timeOffset = thisMatch.delta;
				matchRate = thisMatchRate;				
			}
		}
		TrackInfo matchInfo = trackMap.getTrackInfo(matchID);
		JOptionPane.showMessageDialog(null, new String("The matching track is [" + matchInfo.toString() + 
				"]\nIndex offset: " + timeOffset/Spectrogram.SAMPLE_SIZE + " / Total matching hashes: " + maxNumberOfMatches
				+ "\nPercent of matches at offset: " + matchRate*100 + "%"), "Best Match Results", JOptionPane.PLAIN_MESSAGE);
		
	}
}
