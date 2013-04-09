package edu.nus.tp.engine.classifier;

import static edu.nus.tp.engine.utils.Constants.NEGATIVE_EMOTICONS;
import static edu.nus.tp.engine.utils.Constants.POSITIVE_EMOTICONS;
import static edu.nus.tp.engine.utils.Constants.SPACE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class EmoticonClassifier extends AbstractClassifier{

	public EmoticonClassifier(Persistence persistence) {
		super(persistence);
	}

	@Override
	public ClassifiedTweet classify(ClassifiedTweet tweet) {

		int positiveScore = 0;
		int negativeScore = 0;

		for (String emo: POSITIVE_EMOTICONS)
			positiveScore+= StringUtils.countMatches(tweet.getTweetContent(), emo);

		for (String emo: NEGATIVE_EMOTICONS)
			negativeScore+= StringUtils.countMatches(tweet.getTweetContent(), emo);
		
		Map<Category, Double> scoreMap = new HashMap<Category, Double>();
		scoreMap.put(Category.POSITIVE, (double) positiveScore);
		scoreMap.put(Category.NEGATIVE, (double) negativeScore);

		Score emoticonScore = new Score();
		emoticonScore.setScores(scoreMap);
		emoticonScore.setClassification(getClassification(scoreMap));
		tweet.getScoreMap().put(ClassifierType.EMOTICON, emoticonScore);

		return tweet;

	}

	/*	
	@Override
	public ClassifiedTweet classify(ClassifiedTweet tweet) {


		Collection <String> inputCollection=Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().trimResults().split(tweet.getTweetContent()));		
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

		Map<Category, Double> scoreMap = new HashMap<Category, Double>();
		scoreMap.put(Category.POSITIVE, (double) positiveEmoticonCollection.size());
		scoreMap.put(Category.NEGATIVE, (double) negativeEmoticonCollection.size());



		Score emoticonScore = new Score();
		emoticonScore.setScores(scoreMap);
		emoticonScore.setClassification(getClassification(scoreMap));
		tweet.getScoreMap().put(ClassifierType.EMOTICON, emoticonScore);

		return tweet;
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
	 */

}
