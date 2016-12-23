import ecp.reputation.db.DAO;

public class TestNonScore {

	public static void main(String[] args) {
		DAO db= new DAO();
		db.getNoScoreTweets();
	}

}
