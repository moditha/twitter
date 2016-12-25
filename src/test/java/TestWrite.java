import java.util.List;

import ecp.reputation.db.DAO;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TestWrite {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Twitter twitter = TwitterFactory.getSingleton();

		DAO db = new DAO();
		Status status;
		List<Status> statuses;
		try {
//			Paging page = new Paging(1, 500);
			Query query = new Query("trump");
			// if(result.hasNext())//there is more pages to load
			// {
			// query = result.nextQuery();
			// result = twitter.search(query);
			// }
			query.setCount(100);
			QueryResult result;
			result = twitter.search(query);
			// twitter.getFriendsl 
			
			System.out.println("Showing home timeline.");
			int i = 0;
			do {
				for (Status status1 : result.getTweets()) {
					System.out.println(status1.getText());
					if(status1.isRetweet()){
						System.out.println("YESSSS");
						db.AddRetweet(status1, status1.getRetweetedStatus());
					}
					db.AddTweet(status1);
					i++;
					System.out.println(i);
				}

				if(result.hasNext()){
				query = result.nextQuery();
				result = twitter.search(query);}
			} while (result.hasNext());
			db.closeConnection();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
