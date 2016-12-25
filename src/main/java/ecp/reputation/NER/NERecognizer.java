package ecp.reputation.NER;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.neo4j.cypher.internal.compiler.v2_0.ast.IsNotNull;

import ecp.reputation.enums.NERtypesEnum;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NERecognizer {
	StanfordCoreNLP pipeline;

	public NERecognizer() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		pipeline = new StanfordCoreNLP(props);
	}

	public TwitterEntities NERrun(String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		TwitterEntities tweetlist = new TwitterEntities();
		tweetlist.entities = new ArrayList<NERObject>();
		String previousNe = "";
		NERObject obj = new NERObject();
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				// String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);

				if (previousNe.equals(ne) && (ne.equals("PERSON") || ne.equals("ORGANIZATION") || ne.equals("MISC")
						|| ne.equals("LOCATION") || ne.equals("PERCENT") || ne.equals("DATE") || ne.equals("TIME"))) {
					obj.text = obj.text + " " + word;
				} else {
					if (ne.equals("PERSON")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.PERSON;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("ORGANIZATION")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.ORGANIZATION;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("MISC")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.MISC;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("LOCATION")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.LOCATION;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("MONEY")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.MONEY;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("PERCENT")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.PERCENT;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("DATE")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.DATE;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
					if (ne.equals("TIME")) {
						obj = new NERObject();
						obj.type = NERtypesEnum.TIME;
						obj.text = word;
						previousNe = ne;
						tweetlist.entities.add(obj);
					}
				}
			}
			if (obj.text != null) {
				tweetlist.entities.add(obj);
			}
		}
		return tweetlist;
	}

}
