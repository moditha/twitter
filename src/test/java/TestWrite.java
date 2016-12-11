import java.util.List;

import ecp.reputation.db.DAO;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TestWrite {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Twitter twitter = TwitterFactory.getSingleton();
		DAO db= new DAO();
	    Status status;
		List<Status> statuses;
		try {
			Paging page = new Paging (1, 500);
			statuses = twitter.getHomeTimeline(page);
			//twitter.getFriendsl
			System.out.println("Showing home timeline.");
			int i=0;
		    for (Status status1 : statuses) {
		    	System.out.println(status1.getText());
		    	db.AddTweet(status1);
		    	i++;
		    }
		    db.closeConnection();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}

}
