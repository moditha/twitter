package ecp.reputation.db;

import org.neo4j.helpers.collection.MapUtil;

import java.util.Map;

import org.neo4j.driver.v1.*;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.User;

public class DAO {
	private Driver driver;
	private Session session;

	public DAO() {
		this.driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "1234"));
		this.session = driver.session();
	}

	public void AddTweet(Status tweet) {

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

		AddUser(id, u);
		for (HashtagEntity tag : tweet.getHashtagEntities()) {
			AddHashtag(id, tag);
		}

	}

	public void AddHashtag(long tweetId, HashtagEntity hashTag) {
		// MERGE (h:Hashtag {text:"WOW"}) WITH (h) as y
		// MATCH (tw:Tweet) where id(tw)=203
		// MERGE (tw)-[r:hasHashtag]->(y)
		String cmd = "MERGE (h:Hashtag {" + "text:{hash} }) WITH (h)" + " MATCH (tw:Tweet) where id(tw)=" + tweetId
				+ "   MERGE (tw)-[r:hasHashtag]->(h) " + " MERGE (h)-[rel:hasTweet]->(tw)";
		session.run(cmd, MapUtil.map("hash", hashTag.getText()));
	}

	public void AddUser(long tweetId, User u) {

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
	public void closeConnection() {
		this.session.close();
		this.driver.close();
	}
}
