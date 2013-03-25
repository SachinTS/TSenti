package edu.nus.tp.engine;

import java.util.Collection;

import com.google.common.collect.Lists;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public abstract class AbstractClassifier implements Classifier{
	
	protected Persistence persistence=null;
	
	public AbstractClassifier(Persistence persistence) {
		this.persistence=persistence;
	}

	public Collection<ClassifiedTweet> classify(Collection<ClassifiedTweet> unClassifiedTweetCollection){
	
		Collection<ClassifiedTweet> classifiedTweets=Lists.newArrayList();
		
		for (ClassifiedTweet eachUnClassifiedTweet : unClassifiedTweetCollection) {
			classifiedTweets.add(classify(eachUnClassifiedTweet));
		}
		
		return classifiedTweets;
	}
	
	
}
