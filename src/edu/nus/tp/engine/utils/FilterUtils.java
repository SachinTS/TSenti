package edu.nus.tp.engine.utils;

import static edu.nus.tp.engine.utils.Constants.SPACE;
import static edu.nus.tp.engine.utils.Constants.STOP_WORDS;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * Add public static methods for various filters
 * 
 * This should be a decorator instead
 *
 */
public class FilterUtils {
	
	private static StanfordCoreNLP coreNlp=null;
	
	static{
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    coreNlp=new StanfordCoreNLP(props);
	}
	
	public static String filterHashTags(String input)
	{
		Pattern pattern=Pattern.compile("#\\w+");
		Matcher matcher=pattern.matcher(input);		
		while(matcher.find())
		{
			String matchedString=matcher.group();
			String matchedTrimmedString=matchedString.substring(1);			
			Pattern newPattern=Pattern.compile("[A-Z|a-z][a-z|0-9]*");
			Matcher newMatcher=newPattern.matcher(matchedTrimmedString);
			String replacementString="";
			while(newMatcher.find())
			{
				replacementString+=newMatcher.group()+" ";				
			}
			replacementString.trim();		
			input=input.replace(matchedString, replacementString);
		}
		return input;
	}
	public static String filterTopicTerm(String rawInput,String topic)
	{
		rawInput=rawInput.replace(topic.toLowerCase(),"");
		return rawInput;
	}
	public static Collection<String> doAllFilters(String rawInput,String topic) {

		//1.Strip out Hashes and tokenize Hash tags
		rawInput=filterHashTags(rawInput);
		
		rawInput=rawInput.toLowerCase();
		
		//2.Remove Topic term from tweet
		rawInput=filterTopicTerm(rawInput,topic);
		
		//3.Remove Special Characters/Symbols		 		
		rawInput=stripSpecialCharacters(rawInput);
		
		System.out.println("After special characters processing : "+rawInput);
				
		//4. filter stop words
		Collection<String> processedTweet=filterStopWords(rawInput);
		System.out.println("Stop word processed tweet : "+processedTweet);
		
		//5. lemmatize - needs entire string, not tokens
		String inputTokensAsString=Joiner.on(SPACE).join(processedTweet);
		processedTweet=lemmatize(inputTokensAsString);
		
		System.out.println("Final post processed tweet : "+processedTweet);
		
		return processedTweet;
	}

	/*
	 
	 	public static Collection<String> doAllFilters(Collection<String> rawInput) {
		
		rawInput=FilterUtils.filterSpecialCharacters(rawInput);
		rawInput=FilterUtils.filterStopWords(rawInput);
		rawInput=FilterUtils.lemmatize(rawInput);
		
		return rawInput;
	}
	 
	 
	public static Collection<String> filterStopWords(Collection<String> rawInput){
		
		//TODO Filter stop words here
		
		return rawInput; //for now
	}

	public static Collection<String> lemmatize(Collection<String> rawInput) {
		
		return rawInput; //for now
	}

	public static Collection<String> filterSpecialCharacters(Collection<String> rawInput) {
		return rawInput;
	}
	*/
	


	public static String stripSpecialCharacters(String rawInput) {
		return rawInput.replaceAll("[^\\p{Alpha}\\p{Digit} #]+","");
	}
	
	public static Collection<String> filterStopWords(String rawInput) {
		Collection <String> inputCollection=Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().trimResults().split(rawInput.toString()));
		
		Collection<String> processedCollection=Lists.newArrayList(Iterables.filter(inputCollection,new StopWordPredicate()));
		
		return processedCollection;
	}

	public static Collection<String> lemmatize(String rawInput) {

		Collection<String> lemmas=Lists.newArrayListWithCapacity(30); //should to the initial capacity in other places too
		Annotation rawInputAnnotation=new Annotation(rawInput);
		coreNlp.annotate(rawInputAnnotation);
		
		List<CoreLabel> allTokens = rawInputAnnotation.get(TokensAnnotation.class);
		
		for (CoreLabel eachToken : allTokens) {
			lemmas.add(eachToken.get(LemmaAnnotation.class));	
		}
		
		return lemmas;
	}
	
	static class StopWordPredicate implements Predicate<String>{
		
		@Override
		public boolean apply(String eachToken) {
			return !STOP_WORDS.contains(eachToken);
		}
		
	}
	

}