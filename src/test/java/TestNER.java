import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ecp.reputation.NER.NERObject;
import ecp.reputation.NER.TwitterEntities;
import ecp.reputation.db.DAO;
import ecp.reputation.enums.NERtypesEnum;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class TestNER {

	public static void main(String[] args) {
		DAO db= new DAO();
		
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    String text =db.getTweet(70);
	    
	 // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    TwitterEntities tweetlist = new TwitterEntities();
	  tweetlist.tweetId = 2;
	  tweetlist.entities = new ArrayList<NERObject>();
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	       String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	     //   String pos = token.get(PartOfSpeechAnnotation.class);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class);
	        
	     // System.out.println("word: " + word + " ne:" + ne);	                
	        if (ne.equals("PERSON")){
	        	NERObject person = new NERObject();
	        	person.type=NERtypesEnum.PERSON;
	        	person.text = word;
	        	tweetlist.entities.add(person);
	        	
	      }else {if (ne.equals("ORGANIZATION")){
	    	  NERObject org = new NERObject();
	        	org.type=NERtypesEnum.ORGANIZATION;
	        	org.text = word;
	        	tweetlist.entities.add(org);
	        	
	      } else {if (ne.equals("MISC")){
	    	  NERObject misc = new NERObject();
	        	misc.type=NERtypesEnum.MISC;
	        	misc.text = word;
	        	tweetlist.entities.add(misc);
	      
	    	  } else {if (ne.equals("LOCATION")){
		    	  NERObject loc = new NERObject();
		        	loc.type=NERtypesEnum.LOCATION;
		        	loc.text = word;
		        	tweetlist.entities.add(loc);
		        	
	    	  } else {if (ne.equals("MONEY")){
		    	  NERObject money = new NERObject();
		    	  money.type=NERtypesEnum.MONEY;
		    	  money.text = word;
		    	  tweetlist.entities.add(money);
		    	  
	    	  } else {if (ne.equals("PERCENT")){
		    	  NERObject percent = new NERObject();
		    	  percent.type=NERtypesEnum.PERCENT;
		    	  percent.text = word;
		    	  tweetlist.entities.add(percent);
		    	  
	    	  } else {if (ne.equals("DATE")){
		    	  NERObject date = new NERObject();
		    	  date.type=NERtypesEnum.DATE;
		    	  date.text = word; 
		    	  tweetlist.entities.add(date);
		    	  
	    	  } else {if (ne.equals("TIME")){
		    	  NERObject time = new NERObject();
		    	  time.type=NERtypesEnum.TIME;
		    	  time.text = word;  
		    	  tweetlist.entities.add(time);}
	    	 
	    	  }}}}}}}}} }}


	      // this is the parse tree of the current sentence
	    //  Tree tree = sentence.get(TreeAnnotation.class);
	   //   System.out.println("parse tree:\n" + tree);

	      // this is the Stanford dependency graph of the current sentence
	  //    SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	//      System.out.println("dependency graph:\n" + dependencies);
	//    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	  //  Map<Integer, CorefChain> graph = 
	 //       document.get(CorefChainAnnotation.class);
	    
	//  }

	
		// TODO Auto-generated method stub
//get a tweet with a id
		//do ner
		//save back to neo4j
//	}

