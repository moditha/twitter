import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import ecp.reputation.db.DAO;

public class ReadManualAnnotations {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csvFile = "C:/Users/Suela/Documents/IIR/Manually annotated tweets/louvre1.csv";
		DAO dao = new DAO();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			
			while ((line = reader.readNext()) != null) {
			 
				long tweetID = Long.parseLong(line[0]);
				int annotation = Integer.parseInt(line[2]);
				dao.saveManualAnnotation(tweetID, annotation);
				System.out.println("Tweetid= " + line[0] + "annotation" + line[2] );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
}
		      

		  