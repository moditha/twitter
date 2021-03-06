package ecp.reputation.db;

import org.neo4j.helpers.collection.MapUtil;

import ecp.reputation.NER.TwitterEntities;
import ecp.reputation.sentiment.SentimentScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		if (tweet.getLang().equals("en")) {
			String cmd = "MERGE (t:Tweet {" + "id_str:" + tweet.getId() + "} ) " + " SET t.created_at="
					+ tweet.getCreatedAt().getTime() + " SET t.text= {text}" + " SET t.fav_cnt="
					+ tweet.getFavoriteCount() + " Set t.retweet_cnt=" + tweet.getRetweetCount() + " return id(t)";
			// tweet.getRetweetCount()
			// System.out.println(driver);
			// System.out.println(session);
			// System.out.println(cmd);

			User u = tweet.getUser();

			StatementResult result = session.run(cmd, MapUtil.map("text", tweet.getText()));

			Record record = result.next();
			long id = record.get("id(t)").asLong();

			AddUserForTweet(id, u);
			for (HashtagEntity tag : tweet.getHashtagEntities()) {
				AddHashtagforTweet(id, tag);
			}

			return id;
		} else {
			System.out.println(tweet.getLang());
			return 0;

		}
	}

	public void AddRetweet(Status reTweet, Status tweet) {
		long reTweetId = AddTweet(reTweet);
		long origTweetId = AddTweet(tweet);

		String cmd = "MATCH (tw:Tweet) where id(tw)=" + origTweetId + "   WITH tw MATCH (tw1:Tweet) where id(tw1)="
				+ reTweetId + "   MERGE (tw)-[r:hasRetweet]->(tw1) " + " SET tw1.isRetweet=true";

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

		// u.getName()
		// u.getScreenName()
		// u.getFollowersCount()
		// u.getFriendsCount()
		String cmd = "MERGE (u:user {" + "user_id:" + u.getId() + " })" + "   WITH u MATCH (tw:Tweet) where id(tw)="
				+ tweetId + "   MERGE (u)-[r:tweeted]->(tw) " + " SET u.name={name}" + "SET u.noFollowers="
				+ u.getFollowersCount() + " SET u.noStatuses=" + u.getStatusesCount() + " SET u.noFriends="
				+ u.getFriendsCount() + " SET u.noLists=" + u.getListedCount();

		// System.out.println(cmd);
		session.run(cmd, MapUtil.map("name", u.getName()));
	}

	public String getTweet(long tweetId) {
		String text = "";
		String cmd = "MATCH (n:Tweet)where id(n)= " + tweetId + " RETURN n.text";
		StatementResult result = session.run(cmd);
		while (result.hasNext()) {
			Record record = result.next();
			// System.out.println(record.get( "n.text" ).asString() );
			text = record.get("n.text").asString();
		}
		return text;
	}

	public void saveNER(TwitterEntities e) {
		for (int i = 0; i < e.entities.size(); i++) {
			String cmd = "MERGE (e:entity {" + "text:{name} })" + "   WITH e MATCH (tw:Tweet) where id(tw)=" + e.tweetId
					+ "   MERGE (tw)-[r:hasEntity]->(e) " + " SET e.type = '" + e.entities.get(i).type + "'";
			session.run(cmd, MapUtil.map("name", e.entities.get(i).text));
			// System.out.println(cmd);
		}
	}

	public void AddUser(User user) {
		String cmd = "MERGE (u:user {" + "user_id:" + user.getId() + " }) SET u.name={name} " + " SET u.noFollowers="
				+ user.getFollowersCount() + " SET u.noStatuses=" + user.getStatusesCount() + " SET u.noFriends="
				+ user.getFriendsCount() + " SET u.noLists=" + user.getListedCount();

		session.run(cmd, MapUtil.map("name", user.getName()));
	}

	public void AddUserFollowers(long userId, List<User> followers) {
		for (User user : followers) {
			String cmd = "MERGE (u:user {" + "user_id:" + user.getId() + " })"
					+ "   WITH u MATCH (u2:user) where (u2.user_id)=" + userId + "   MERGE (u)-[r:follows]->(u2) "
					+ " SET u.name={name} " + "SET u.noFollowers=" + user.getFollowersCount() + " SET u.noStatuses="
					+ user.getStatusesCount() + " SET u.noFriends=" + user.getFriendsCount() + " SET u.noLists="
					+ user.getListedCount();
			session.run(cmd, MapUtil.map("name", user.getName()));
		}
	}

	public void AddUserFriends(long userId, List<User> friends) {
		for (User user : friends) {
			String cmd = "MERGE (u:user {" + "user_id:" + user.getId() + " })"
					+ "   WITH u MATCH (u2:user) where (u2.user_id)=" + userId + "   MERGE (u2)-[r:follows]->(u) "
					+ " SET u.name={name} " + "SET u.noFollowers=" + user.getFollowersCount() + " SET u.noStatuses="
					+ user.getStatusesCount() + " SET u.noFriends=" + user.getFriendsCount() + " SET u.noLists="
					+ user.getListedCount();
			session.run(cmd, MapUtil.map("name", user.getName()));
		}
	}

	public void saveSentiment(SentimentScore score) {
		String cmd = "MATCH (tw:Tweet) where id(tw)=" + score.tweetId + " SET tw.positive=" + score.positive
				+ " SET tw.negative=" + score.negative;
		session.run(cmd);
		// System.out.println(cmd);
	}

	public ArrayList<Long> getNoScoreTweets() {
		String cmd = "MATCH (n:Tweet) WHERE NOT EXISTS(n.positive) AND NOT EXISTS(n.negative) return id(n)";
		StatementResult result = session.run(cmd);
		// System.out.println(cmd);
		ArrayList<Long> idtweets = new ArrayList<Long>();
		while (result.hasNext()) {
			Record record = result.next();
			idtweets.add(record.get("id(n)").asLong());
			// text= record.get( "n.text" ).asString();
		}
		return idtweets;
	}

	public void saveManualAnnotation(long tweetId, int annotation) {
		String cmd = "MATCH (n:Tweet)where id(n)= " + tweetId + " SET n.annotation=" + annotation;
		session.run(cmd);
		// session.
	}

	public List<SentimentScore> getScores() {
		String cmd = "MATCH p=(u:user)-[r:tweeted]->(n:Tweet)where  EXISTS(n.annotation) AND NOT EXISTS(n.isRetweet) "
				+ "return n.positive,n.negative,n.retweet_cnt,n.fav_cnt,u.noFollowers,n.annotation";
		StatementResult result = session.run(cmd);
		// System.out.println(cmd);
		ArrayList<SentimentScore> scores = new ArrayList<SentimentScore>();
		while (result.hasNext()) {
			Record record = result.next();
			SentimentScore score = new SentimentScore();
			score.positive = record.get("n.positive").asInt();
			score.negative = record.get("n.negative").asInt();
			score.overall = score.positive + score.negative;
			score.retweets = record.get("n.retweet_cnt").asInt();
			score.favorites = record.get("n.fav_cnt").asInt();
			score.followers = record.get("u.noFollowers").asInt();
			score.annotated = record.get("n.annotation").asInt();
			scores.add(score);
		}
		return scores;
	}

	public void closeConnection() {
		this.session.close();
		this.driver.close();
	}
}
