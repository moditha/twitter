import java.util.List;

import ecp.reputation.db.DAO;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TestUser {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Twitter twitter = TwitterFactory.getSingleton();
		DAO db= new DAO();
	    Status status;
		List<User> statuses;
		try {
			Paging page = new Paging (1, 500);
			statuses = twitter.getFollowersList(twitter.getId(),-1);
			User u=twitter.showUser(twitter.getId());
			db.AddUser(u);
			//twitter.getFriendsl
			System.out.println("Showing home timeline.");
			int i=0;
		    for (User status1 : statuses) {
		    	System.out.println(status1.getName());
		    			    }
		    
		    long cursor =-1L;
		    PagableResponseList<User> friends;
	        do {
	            friends = twitter.getFriendsList(twitter.getId(),cursor);
	            db.AddUserFriends(twitter.getId(), friends);
	        } while((cursor = friends.getNextCursor())!=0 );
		    
		    
		   // db.AddUserFriends(twitter.getId(), twitter.getFriendsList(twitter.getId(), -1));
		    db.AddUserFollowers(twitter.getId(), statuses);
		    db.closeConnection();
		} catch (TwitterException e) {
			e.printStackTrace();
		}	
	}
}
