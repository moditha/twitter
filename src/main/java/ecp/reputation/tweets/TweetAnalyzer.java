package ecp.reputation.tweets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	Set<String> vocabulary = new HashSet<String>();

	public TweetAnalyzer() {
		scorer = new SentimentScorer();
		db = new DAO();
		NERtweet = new NERecognizer();

		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/SentStrength_Data/words.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				vocabulary.add(line);
				line = br.readLine();
			}
			br.close();;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveTweetEntities(long tweetId) {
		String tweetText = replaceHashtags(removeUrl(db.getTweet(tweetId)));
		TwitterEntities tweetNER = NERtweet.NERrun(tweetText);
		tweetNER.tweetId = tweetId;
		db.saveNER(tweetNER);
	}

	public void saveTweetSentiment(long tweetId) {
		String tweetText = replaceHashtags(removeUrl(db.getTweet(tweetId)));
		SentimentScore score = scorer.getSentimentScores(tweetText);
		score.tweetId = tweetId;
		db.saveSentiment(score);
		System.out.println("saving score ->" + score.positive);
	}

	private String removeUrl(String commentstr) {
		String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(commentstr);
		int i = 0;
		while (m.find()) {
			commentstr = commentstr.replaceAll(m.group(i), "").trim();
			i++;
		}
		return commentstr;
	}

	private String replaceHashtags(String text) {
		ArrayList<TextWithIndex> hashtags = new ArrayList<TextWithIndex>();

		String urlPattern = "\\S*#(?:\\[[^\\]]+\\]|\\S+)";
		Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		int i = 0;
		while (m.find()) {
			System.out.println("---" + m.group());
			for (String voc : vocabulary) {
				if (m.group().contains(voc)) {
					hashtags.add(new TextWithIndex(voc, m.group().indexOf(voc) + i * 100, i));
				}
			}
			i++;
		}
		ArrayList<TextWithIndex> tmphashtags = new ArrayList<TextWithIndex>();

		for (int j = 0; j < hashtags.size(); j++) {
			boolean b = false;
			for (int k = 0; k < hashtags.size(); k++) {
				if (hashtags.get(k).text.contains(hashtags.get(j).text) && k != j
						&& hashtags.get(k).groupIndex == hashtags.get(j).groupIndex) {
					b = true;
				}
			}
			if (!b) {
				tmphashtags.add(hashtags.get(j));
			}
		}
		Collections.sort(tmphashtags);
		m.reset();
		for (int j = 1; j <= i; j++) {
			m.find();
			int y = j - 1;
			String s = String.join(" ", tmphashtags.stream().filter(h -> h.groupIndex == y).map(hs -> hs.text)
					.collect(Collectors.toList()));
			text = text.replaceAll(m.group(), s);

		}
		return text;
	}
}
