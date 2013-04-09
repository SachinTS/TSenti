package edu.nus.tp.web.tweet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.nus.tp.engine.classifier.ClassifierType;
import edu.nus.tp.engine.classifier.Score;
import edu.nus.tp.engine.utils.Category;


public class ClassifiedTweet { //implements Cloneable{

	private String tweetContent;
	private Category classification;
	private String topic;
	private Map<ClassifierType, Score> scoreMap;
	private Collection<String> terms;
	
	public ClassifiedTweet(String tweet, Category classification,String topic) {
		
		this.tweetContent=tweet;
		this.classification=classification;
		this.topic=topic;
		scoreMap = new HashMap<ClassifierType, Score>();
		terms = new ArrayList<String>();
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	public ClassifiedTweet(String tweet) {
		this(tweet, Category.UNCLASSIFIED,"");
	}

	public String getTweetContent() {
		return tweetContent;
	}

	public Category getClassification() {
		return classification;
	}

	public void setClassification(Category classification) {
		this.classification = classification;
	}

	public void setTweetContent(String tweetContent) {
		this.tweetContent = tweetContent;
	}

/*	public Collection<String> getTweetContentAsTokens(){
		if (tweetContent==null) return Lists.newArrayList();
		return Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().trimResults().split(tweetContent));
	}*/
	
	/*
	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			ClassifiedTweet cloned = (ClassifiedTweet) super.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			System.out.println(e);
			return null;
		}
	}
*/
	public Map<ClassifierType, Score> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<ClassifierType, Score> scoreMap) {
		this.scoreMap = scoreMap;
	}

	public Collection<String> getTerms() {
		return terms;
	}

	public void setTerms(Collection<String> terms) {
		this.terms = terms;
	}
	
	
}
