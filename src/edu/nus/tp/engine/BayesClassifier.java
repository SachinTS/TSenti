package edu.nus.tp.engine;

import static edu.nus.tp.engine.utils.Constants.*;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
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
		System.out.println("Inside train batch");
		
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
	public ClassifiedTweet classify(ClassifiedTweet unClassifiedTweet){
		
		int emoticonScore=getEmoticonScore(unClassifiedTweet);
		
		Collection<String> eachParsedTweet = FilterUtils.doAllFilters(unClassifiedTweet.getTweetContent(),unClassifiedTweet.getTopic());		
		
		EnumMap<Category, Double> allMAP=new EnumMap<Category,Double>(Category.class);
		
		double prior, product, numerator, denominator, eachCategoryProbability;
		
		for (Category eachCategory : Category.getClassificationClasses()) {
		
			prior=getPriorForCategory(eachCategory);
			product=1.0;
			numerator=0.0;
			//double denominator=log(persistence.getTermCountByCategory(eachCategory)+persistence.getUniqueTermsInVocabulary());
			denominator=persistence.getTermCountByCategory(eachCategory)+persistence.getUniqueTermsInVocabulary();
			
			for (String eachTerm : eachParsedTweet) {
				numerator = getFrequencyOfTermInCategory(eachTerm,eachCategory) + 1;
				product*=numerator/denominator;
				System.out.println(eachCategory.toString() +"::::" + eachTerm + " Numerator : "+numerator + " Denominator = " + denominator);
				
			}
			System.out.println(eachCategory.toString() +":::: Prior : "+prior);
			
			eachCategoryProbability=(double)prior*product;
			
			allMAP.put(eachCategory, eachCategoryProbability);
			//allMAP.put(eachCategory, (double)numerator-denominator);
			
			System.out.println(eachCategory.toString() +":::: Probability : "+eachCategoryProbability);
			
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
	public int getEmoticonScore(ClassifiedTweet unClassifiedTweet) {
		Collection <String> inputCollection=Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().trimResults().split(unClassifiedTweet.getTweetContent()));		
		Collection<String> negativeEmoticonCollection=Lists.newArrayList(Iterables.filter(inputCollection,new NegativeEmoticonPredicate()));	
		Collection<String> positiveEmoticonCollection=Lists.newArrayList(Iterables.filter(inputCollection,new PositiveEmoticonPredicate()));
		
		Iterator<String> i=negativeEmoticonCollection.iterator();		
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
		i=positiveEmoticonCollection.iterator();
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
		
		return positiveEmoticonCollection.size()-negativeEmoticonCollection.size();
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
	static class NegativeEmoticonPredicate implements Predicate<String>{
		
		@Override
		public boolean apply(String eachToken) {
			return NEGATIVE_EMOTICONS.contains(eachToken);
		}
		
	}
	static class PositiveEmoticonPredicate implements Predicate<String>{
		
		@Override
		public boolean apply(String eachToken) {
			return POSITIVE_EMOTICONS.contains(eachToken);
		}
		
	}
	
	
}
