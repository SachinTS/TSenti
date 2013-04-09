package edu.nus.tp.engine.classifier;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.engine.utils.Constants;
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
	
	public Category getClassification(Map<Category, Double> scoreMap){
		/*
		Category maxCategory = Category.UNCLASSIFIED;
		double maxScore=Double.NEGATIVE_INFINITY;
		for (Entry<Category, Double> eachCategoryEntry : scoreMap.entrySet()) {			
			if (eachCategoryEntry.getValue()!= 0 && eachCategoryEntry.getValue() >maxScore){
				maxScore = eachCategoryEntry.getValue();
				maxCategory = eachCategoryEntry.getKey();
			}
			
		}
		return maxCategory;*/
		
		Category classification = Category.UNCLASSIFIED;
		double totalPositive = scoreMap.get(Category.POSITIVE);
		double totalNegative = scoreMap.get(Category.NEGATIVE);
		
		if (totalPositive>totalNegative){
			classification = Category.POSITIVE;
		} else if (totalNegative>totalPositive){
			classification = Category.NEGATIVE;
		} else {
			classification = Category.NEUTRAL;
		}
		return classification;
	}
	
}
