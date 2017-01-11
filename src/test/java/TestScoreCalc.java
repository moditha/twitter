import java.util.List;

import ecp.reputation.db.DAO;
import ecp.reputation.scoring.ScoreCalculator;
import ecp.reputation.sentiment.SentimentScore;

public class TestScoreCalc {

	public static void main(String[] args) {
		ScoreCalculator cal = new ScoreCalculator();
		DAO d = new DAO();
		List<SentimentScore> scores = d.getScores();
		cal.annotatedCalc(scores);
		cal.lnWeightedAnnotatedCalc(scores);	
		cal.lnplusWeightedAnnotatedCalc(scores);
		cal.genericCalc(scores);
		cal.lnWeightedCalc(scores);
		cal.lnplusWeightedCalc(scores);
	}
}
