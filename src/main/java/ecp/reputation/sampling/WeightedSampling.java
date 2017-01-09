package ecp.reputation.sampling;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class WeightedSampling {

	private Driver driver;
	private Session session;

	public WeightedSampling() {
		this.driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "neo4jj"));
		this.session = driver.session();
	}
	
	private void getNeighbors(long nodeId){
		String cmd = "match (n )-[r]->(w) where id(n)="+ nodeId
				+ " return w ";
		StatementResult result = session.run(cmd);
		while (result.hasNext()){
			
		}
	}
}
