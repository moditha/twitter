package ecp.reputation.tweets;

import ecp.reputation.db.DAO;
import ecp.reputation.sentiment.SentimentScore;
import ecp.reputation.sentiment.SentimentScorer;
import uk.ac.wlv.sentistrength.SentiStrength;

public class TweetAnalyzer {
	SentimentScorer scorer;
	DAO db;
	public TweetAnalyzer() {
		scorer = new SentimentScorer();
		db= new DAO();
	}
	
	public void saveTweetSentiment(long tweetId){
		String tweetText=db.getTweet(tweetId);
		SentimentScore score=scorer.getSentimentScores(tweetText);
		score.tweetId=tweetId;
		db.saveSentiment(score);
	}
}
