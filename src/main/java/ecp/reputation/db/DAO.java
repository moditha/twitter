package ecp.reputation.db;

import org.neo4j.helpers.collection.MapUtil;

import ecp.reputation.sentiment.SentimentScore;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.*;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.User;

public class DAO {
	private Driver driver;
	private Session session;

	public DAO() {
		this.driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "neo4jj"));
		this.session = driver.session();
	}

	public long AddTweet(Status tweet) {

		if(tweet.getLang().equals("en")){
		String cmd = "MERGE (t:Tweet {" + "id_str:" + tweet.getId() + "} ) " + " SET t.created_at="
				+ tweet.getCreatedAt().getTime() + " SET t.text= {text}" + " SET t.fav_cnt=" + tweet.getFavoriteCount()
				+ " Set t.retweet_cnt=" + tweet.getRetweetCount() + " return id(t)";
		// tweet.getRetweetCount()
		// System.out.println(driver);
		// System.out.println(session);
		// System.out.println(cmd);
		
		User u=tweet.getUser();
		
		StatementResult result = session.run(cmd, MapUtil.map("text", tweet.getText()));

		Record record = result.next();
		long id = record.get("id(t)").asLong();

		AddUserForTweet(id, u);
		for (HashtagEntity tag : tweet.getHashtagEntities()) {
			AddHashtagforTweet(id, tag);
		}

		return id;
		}
		else{
		System.out.println(tweet.getLang());
			return 0;
		
		}
	}

	public void AddRetweet(Status reTweet, Status tweet){
		long reTweetId=AddTweet(reTweet);
		long origTweetId=AddTweet(tweet);
		
		String cmd = "MATCH (tw:Tweet) where id(tw)="+origTweetId
				+ "   WITH tw MATCH (tw1:Tweet) where id(tw1)=" + reTweetId
				+ "   MERGE (tw)-[r:hasRetweet]->(tw1) "
				+ " SET tw1.isRetweet=true";
		
		session.run(cmd);
	}
	
	public void AddHashtagforTweet(long tweetId, HashtagEntity hashTag) {
		// MERGE (h:Hashtag {text:"WOW"}) WITH (h) as y
		// MATCH (tw:Tweet) where id(tw)=203
		// MERGE (tw)-[r:hasHashtag]->(y)
		String cmd = "MERGE (h:Hashtag {" + "text:{hash} }) WITH (h)" + " MATCH (tw:Tweet) where id(tw)=" + tweetId
				+ "   MERGE (tw)-[r:hasHashtag]->(h) " + " MERGE (h)-[rel:hasTweet]->(tw)";
		session.run(cmd, MapUtil.map("hash", hashTag.getText()));
	}

	public void AddUserForTweet(long tweetId, User u) {

//		u.getName()
//		u.getScreenName()
//		u.getFollowersCount()
//		u.getFriendsCount()
		String cmd = "MERGE (u:user {" + "user_id:"+u.getId()+" })"
				+ "   WITH u MATCH (tw:Tweet) where id(tw)=" + tweetId
				+ "   MERGE (u)-[r:tweeted]->(tw) "
				+ " SET u.name={name}";
		
		//System.out.println(cmd);
		session.run(cmd, MapUtil.map("name", u.getName()));
	}
	
	public String getTweet(long tweetId){
		String text="";
		String cmd = "MATCH (n:Tweet)where id(n)= "+tweetId+" RETURN n.text";
		StatementResult result = session.run(cmd);
		while ( result.hasNext() )
		{
		Record record = result.next();
		//System.out.println(record.get( "n.text" ).asString() );
		text= record.get( "n.text" ).asString();
		}
		return text;
	}

	public void saveNER(){
		
	}
	
	public void AddUser(User user){
		String cmd = "MERGE (u:user {" + "user_id:"+user.getId()+" }) SET u.name={name}";
		session.run(cmd, MapUtil.map("name", user.getName()));
	}
	
	public void AddUserFollowers(long userId, List<User> followers){
		for (User user : followers) {
			String cmd = "MERGE (u:user {" + "user_id:"+user.getId()+" })"
					+ "   WITH u MATCH (u2:user) where (u2.user_id)=" + userId
					+ "   MERGE (u)-[r:follows]->(u2) "
					+ " SET u.name={name}";
			session.run(cmd, MapUtil.map("name", user.getName()));
		}		
	}
	
	public void AddUserFriends(long userId, List<User> friends){
		for (User user : friends) {
			String cmd = "MERGE (u:user {" + "user_id:"+user.getId()+" })"
					+ "   WITH u MATCH (u2:user) where (u2.user_id)=" + userId
					+ "   MERGE (u2)-[r:follows]->(u) "
					+ " SET u.name={name}";
			session.run(cmd, MapUtil.map("name", user.getName()));
		}		
	}
	
	public void saveSentiment(SentimentScore score){
		String cmd = "MATCH (tw:Tweet) where id(tw)="+ score.tweetId
				+ " SET tw.positive="+ score.positive+ " tw.negative="+ score.negative;
		session.run(cmd);
	}

	public void closeConnection() {
		this.session.close();
		this.driver.close();
	}
}
