import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	System.out.println(removeUrl("Ah ouais .... Faraday Future's new FF91 electric vehicle will cost 'less than $300,000', says CEO Jia Yueting https://t.co/CQNNFrEPgC"));
	}
	
	private static String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentstr;
    }
}
