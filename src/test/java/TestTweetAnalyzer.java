import java.util.ArrayList;

import ecp.reputation.db.DAO;
import ecp.reputation.tweets.TweetAnalyzer;

public class TestTweetAnalyzer {
	public static void main(String[] args){
		TweetAnalyzer anl= new TweetAnalyzer();
		DAO db= new DAO();
		ArrayList<Long> testtweets = db.getNoScoreTweets();
		for (Long long1 : testtweets) {
			anl.saveTweetSentiment(long1);
		anl.saveTweetEntities(long1);
		}
		
		
	}
}
