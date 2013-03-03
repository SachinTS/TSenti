package edu.nus.tp.engine;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.engine.utils.FilterUtils;
import edu.nus.tp.web.tweet.ClassifiedTweet;


public class BayesClassifier  {

	private Persistence persistence=null;
	
	public BayesClassifier(Persistence persistence) {
		this.persistence=persistence;
	}
	
	/**
	 * Trains using bunch of preclassified tweets 
	 * @param preLearnedTweets
	 */
	public void train(Collection<ClassifiedTweet> preLearnedTweets){
		
		for (ClassifiedTweet eachClassifiedTweet : preLearnedTweets) {
			train(eachClassifiedTweet);
		}
	}


	/**
	 * Trains using a single tweet and saves the result to the Persistence
	 * 
	 * @param eachClassifiedTweet
	 */
	public void train(ClassifiedTweet eachClassifiedTweet) {
		
		Collection<String> eachParsedTweet=null;
		
		eachParsedTweet=FilterUtils.doAllFilters(eachClassifiedTweet.getTweetContent(),eachClassifiedTweet.getTopic());
		
		if (eachParsedTweet!=null && eachParsedTweet.size()>0){
			
			persistence.saveTermsAndClassification(eachParsedTweet,eachClassifiedTweet.getClassification());
		}
	}
	
	
	public Collection<ClassifiedTweet> classify(Collection<ClassifiedTweet> unClassifiedTweetCollection){
	
		Collection<ClassifiedTweet> classifiedTweets=Lists.newArrayList();
		
		for (ClassifiedTweet eachUnClassifiedTweet : unClassifiedTweetCollection) {
			classifiedTweets.add(classify(eachUnClassifiedTweet));
		}
		
		return classifiedTweets;
	}
	
	/**
	 * O(n*m) - not sure if there is a much more optimal way to do this
	 * 
	 * @param unClassifiedTweet
	 * @return
	 */
	public ClassifiedTweet classify(ClassifiedTweet unClassifiedTweet){
		
		Collection<String> eachParsedTweet = FilterUtils.doAllFilters(unClassifiedTweet.getTweetContent(),unClassifiedTweet.getTopic());		
		
		EnumMap<Category, Double> allMAP=new EnumMap<Category,Double>(Category.class);
		
		for (Category eachCategory : Category.values()) {
		
			
			double prior=getPriorForCategory(eachCategory);
			double product=1.0;
			double numerator=0.0;
			//double denominator=log(persistence.getTermCountByCategory(eachCategory)+persistence.getUniqueTermsInVocabulary());
			double denominator=persistence.getTermCountByCategory(eachCategory)+persistence.getUniqueTermsInVocabulary();
			
			for (String eachTerm : eachParsedTweet) {
				
				product*=getFrequencyOfTermInCategory(eachTerm,eachCategory);
				//product+=log(getFrequencyOfTermInCategory(eachTerm,eachCategory));
			}
			
			numerator=prior*product;
			//numerator=log(prior)+log(product);
			
			allMAP.put(eachCategory, (double)numerator/denominator);
			//allMAP.put(eachCategory, (double)numerator-denominator);
			
			System.out.println(eachCategory.toString() +"::: Numerator ::: Denominator :::"+numerator +"::::"+denominator);
			
		}
		
		
		//getMax - this, I guess is the most inefficient method around.  No creative juice coming up 
		Category maxCategory=Category.UNCLASSIFIED;
		double maxProbability=Double.MIN_VALUE;
		for (Entry<Category, Double> eachCategoryEntry : allMAP.entrySet()) {
			
			if (eachCategoryEntry.getValue()>maxProbability){
				maxProbability=eachCategoryEntry.getValue();
				maxCategory=eachCategoryEntry.getKey();
			}
			
		}
		

		unClassifiedTweet.setClassification(maxCategory);
		
		return unClassifiedTweet;
		
	}
	
	
	private long getFrequencyOfTermInCategory(String eachTerm, Category eachCategory) {
		
		return persistence.getTermFrequencyInCategory(eachTerm, eachCategory);
		
	}

	public double getPriorForCategory(Category category){
		return persistence.getPriorClassificationForCategory(category);
	}
	
	
	
}
