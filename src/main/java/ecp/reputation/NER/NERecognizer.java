package ecp.reputation.NER;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

				// System.out.println("word: " + word + " ne:" + ne);
				if (ne.equals("PERSON")) {
					NERObject person = new NERObject();
					person.type = NERtypesEnum.PERSON;
					person.text = word;
					tweetlist.entities.add(person);
				}
				if (ne.equals("ORGANIZATION")) {
					NERObject org = new NERObject();
					org.type = NERtypesEnum.ORGANIZATION;
					org.text = word;
					tweetlist.entities.add(org);

				}
				if (ne.equals("MISC")) {
					NERObject misc = new NERObject();
					misc.type = NERtypesEnum.MISC;
					misc.text = word;
					tweetlist.entities.add(misc);

				}
				if (ne.equals("LOCATION")) {
					NERObject loc = new NERObject();
					loc.type = NERtypesEnum.LOCATION;
					loc.text = word;
					tweetlist.entities.add(loc);

				}
				if (ne.equals("MONEY")) {
					NERObject money = new NERObject();
					money.type = NERtypesEnum.MONEY;
					money.text = word;
					tweetlist.entities.add(money);

				}
				if (ne.equals("PERCENT")) {
					NERObject percent = new NERObject();
					percent.type = NERtypesEnum.PERCENT;
					percent.text = word;
					tweetlist.entities.add(percent);

				}
				if (ne.equals("DATE")) {
					NERObject date = new NERObject();
					date.type = NERtypesEnum.DATE;
					date.text = word;
					tweetlist.entities.add(date);

				}
				if (ne.equals("TIME")) {
					NERObject time = new NERObject();
					time.type = NERtypesEnum.TIME;
					time.text = word;
					tweetlist.entities.add(time);

				}
			}
		}
		return tweetlist;
	}

}
