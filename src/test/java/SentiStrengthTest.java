import uk.ac.wlv.sentistrength.SentiStrength;

public class SentiStrengthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Method 2: One initialisation and repeated classifications
		SentiStrength sentiStrength = new SentiStrength(); 
		//Create an array of command line parameters to send (not text or file to process)
		String ssthInitialisation[] = {"sentidata", "C:/SentStrength_Data/", "explain"};
		sentiStrength.initialise(ssthInitialisation);

System.out.println(sentiStrength.computeSentimentScores("I hate frogs.")); 
System.out.println(sentiStrength.computeSentimentScores("I love dogs.")); 


	}

}
