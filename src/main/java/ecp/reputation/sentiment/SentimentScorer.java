package ecp.reputation.sentiment;

import uk.ac.wlv.sentistrength.SentiStrength;

public class SentimentScorer {
 
	SentiStrength sentiStrength;
	public SentimentScorer() {
		sentiStrength = new SentiStrength();
		String ssthInitialisation[] = { "sentidata", "C:/SentStrength_Data/" };
		sentiStrength.initialise(ssthInitialisation);
	}
	
	public SentimentScore getSentimentScores(String text){
		SentimentScore score= new SentimentScore();
		String[] val=sentiStrength.computeSentimentScores(text).split(" ");
		score.positive=Integer.valueOf(val[0]);
		score.negative=Integer.valueOf(val[1]);
		return score;
	}
}
