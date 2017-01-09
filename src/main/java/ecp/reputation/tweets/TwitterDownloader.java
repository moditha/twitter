package ecp.reputation.tweets;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterDownloader {

	public Twitter twitter = TwitterFactory.getSingleton();

	public Status getTweetWithId(long id) {
		Status s = null;
		try {
			s = twitter.showStatus(id);
			return s;
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
		finally {
			return s;
		}
		
	}

	public ResponseList<User> getUserWithId(String screenNames) {
		ResponseList<User> list = null;
		try {
			list = twitter.lookupUsers(screenNames);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
