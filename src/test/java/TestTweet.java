import ecp.reputation.tweets.TwitterDownloader;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

public class TestTweet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TwitterDownloader d=new TwitterDownloader();
		try {
			System.out.println(d.twitter.getRateLimitStatus());
		
		//Status s=d.getTweetWithId(3423434);
		//System.out.println(s.getText());
		ResponseList<User> u= d.getUserWithId("pardonlauren");
		for (User user : u) {
			System.out.println(user.isProtected());
		}
		System.out.println(d.twitter.getRateLimitStatus());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
