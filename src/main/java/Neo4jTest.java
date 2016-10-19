import org.neo4j.driver.v1.*;

public class Neo4jTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "neo4jj" ) );
		Session session = driver.session();

		//session.run( "CREATE (a:Person {name:'Arthur', title:'King'})" );

		StatementResult result = session.run( "MATCH (nineties:Movie) WHERE nineties.released > 1990 AND nineties.released < 2000 RETURN nineties.title" );
		while ( result.hasNext() )
		{
		    Record record = result.next();
		    System.out.println( record.get( "nineties.title" ).asString() );
		}

		session.close();
		driver.close();
	}

}
