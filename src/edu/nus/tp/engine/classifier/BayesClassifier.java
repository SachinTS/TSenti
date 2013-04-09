package edu.nus.tp.engine.classifier;

import static edu.nus.tp.engine.utils.Constants.NEGATIVE_EMOTICONS;
import static edu.nus.tp.engine.utils.Constants.POSITIVE_EMOTICONS;
import static edu.nus.tp.engine.utils.Constants.SPACE;
import static java.lang.Math.log10;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Transaction;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.engine.utils.FilterUtils;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class BayesClassifier extends AbstractClassifier {

	public BayesClassifier(Persistence persistence) {
		super(persistence);
	}
	
	/**
	 * Trains using bunch of preclassified tweets 
	 * @param preLearnedTweets
	 */
	public void train(Collection<ClassifiedTweet> preLearnedTweets){

		List<ClassifiedTweet> allTweets=Lists.newArrayList(preLearnedTweets);
		
		int size=preLearnedTweets.size();
		final int BATCH_SIZE=1000;
		int batchCount=size/BATCH_SIZE;
		int lastBatch=size%BATCH_SIZE;
		
		for (int count = 0; count < batchCount; count++) {

			Collection<ClassifiedTweet> subList=allTweets.subList(count*BATCH_SIZE, (count+1)*BATCH_SIZE);
			trainBatch(subList);
			
		}
		
		trainBatch(allTweets.subList(batchCount, batchCount+lastBatch));
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
	
	/* (non-Javadoc)
	 * @see edu.nus.tp.engine.Classifier#classify(edu.nus.tp.web.tweet.ClassifiedTweet)
	 */
	@Override
	public ClassifiedTweet classify(ClassifiedTweet tweet){
		
		double prior, product, numerator, denominator, eachCategoryProbability;
		
		Map<Category, Double> scoreMap = new HashMap<Category, Double>();
		
		for (Category eachCategory : Category.getClassificationClasses()) {
		
			//prior=getPriorForCategory(eachCategory);
			prior=log10(getPriorForCategory(eachCategory));
			product=0.0;
			numerator=0.0;
			//double denominator=log(persistence.getTermCountByCategory(eachCategory)+persistence.getUniqueTermsInVocabulary());
			denominator=log10(persistence.getTermCountByCategory(eachCategory)+persistence.getUniqueTermsInVocabulary());
			
			System.out.println(eachCategory+": prior :"+prior);
			System.out.println(eachCategory+": denominator :"+denominator);
			
			for (String eachTerm : tweet.getTerms()) {
				numerator = log10(getFrequencyOfTermInCategory(eachTerm,eachCategory)+1);
				//product*=numerator/denominator;
				product+=numerator-denominator;
			}
			System.out.println(eachCategory+": numerator/denominator :"+product);
			
			//eachCategoryProbability=(double)prior*product;
			eachCategoryProbability=prior+product;
			
			//scoreMap.put(eachCategory, (double)numerator-denominator);
			scoreMap.put(eachCategory, eachCategoryProbability);
			
		}

		Score nbScore = new Score();
		nbScore.setScores(scoreMap);
		nbScore.setClassification(getClassification(scoreMap));
		tweet.getScoreMap().put(ClassifierType.NAIVEBAYES, nbScore);
		
		return tweet;
		
	}
	
	
	private long getFrequencyOfTermInCategory(String eachTerm, Category eachCategory) {
		
		return persistence.getTermFrequencyInCategory(eachTerm, eachCategory);
		
	}
	

	public double getPriorForCategory(Category category){
		return persistence.getPriorClassificationForCategory(category);
	}
	
	
	public void trainBatch(Collection<ClassifiedTweet> preLearnedTweets) {
		
		
		int count=0;
		Transaction txn=persistence.startBatch();
		for (ClassifiedTweet eachTweet : preLearnedTweets) {
				count++;
				if (count%1000==0){
					System.out.println("Pushing to transaction : "+count);
				}
				
				Collection<String> eachParsedTweet=FilterUtils.doAllFilters(eachTweet.getTweetContent(),eachTweet.getTopic());

				//System.out.println("Filter done"+count);
				if (eachParsedTweet!=null && eachParsedTweet.size()>0){
					persistence.saveTermsAndClassificationBatch(eachParsedTweet,eachTweet.getClassification(), txn);
				}
		}
		persistence.endBatch(txn);
	
	}
	
	
}
