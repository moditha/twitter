import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import ecp.reputation.db.DAO;

public class ReadManualAnnotations {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csvFile = "D:/iphone.csv";
		DAO dao = new DAO();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			int i=0;
			while ((line = reader.readNext()) != null) {
			 
				long tweetID = Long.parseLong(line[0]);
				int annotation = Integer.parseInt(line[1]);
				dao.saveManualAnnotation(tweetID, annotation);
				System.out.println(i+"-->"+tweetID+"-->"+annotation);
				i++;
				//System.out.println("Tweetid= " + line[0] + "annotation" + line[2] );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
}
		      

		  