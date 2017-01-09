package ecp.reputation.tweets;

import ecp.reputation.NER.NERecognizer;
import ecp.reputation.NER.TwitterEntities;
import ecp.reputation.db.DAO;
import ecp.reputation.sentiment.SentimentScore;
import ecp.reputation.sentiment.SentimentScorer;
import uk.ac.wlv.sentistrength.SentiStrength;

public class TweetAnalyzer {
	SentimentScorer scorer;
	NERecognizer NERtweet;
	DAO db;
	public TweetAnalyzer() {
		scorer = new SentimentScorer();
		db= new DAO();
		NERtweet = new NERecognizer();
	}
	
	public void saveTweetEntities(long tweetId){
		String tweetText=db.getTweet(tweetId);
		TwitterEntities tweetNER = NERtweet.NERrun(tweetText);
		tweetNER.tweetId = tweetId;
//		System.out.println(tweetNER.entities.get(0).text);
		db.saveNER(tweetNER);
	}
	
	public void saveTweetSentiment(long tweetId){
		String tweetText=db.getTweet(tweetId);
		SentimentScore score=scorer.getSentimentScores(tweetText);
		score.tweetId=tweetId;
		db.saveSentiment(score);
	System.out.println("saving score ->"+ score.positive);
	}
}
