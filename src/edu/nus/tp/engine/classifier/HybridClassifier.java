package edu.nus.tp.engine.classifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.engine.utils.Constants;
import edu.nus.tp.engine.utils.FilterUtils;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class HybridClassifier extends AbstractClassifier {

	public HybridClassifier(Persistence persistence) {
		super(persistence);
	}

	public List<ClassifiedTweet> classify(List<ClassifiedTweet> tweets) {

		for (ClassifiedTweet tweet: tweets){
			classify(tweet);
		}

		normalizeScoring(tweets);

		for (ClassifiedTweet tweet: tweets){
			generateFinalClassification(tweet);
		}

		return tweets;

	}

	private void generateFinalClassification(ClassifiedTweet tweet) {
		Category finalClassification = null;
		Map <ClassifierType, Score>scoreMap = tweet.getScoreMap();

		if (scoreMap.get(ClassifierType.EMOTICON).getScores().get(Category.POSITIVE) != 0 || 
				scoreMap.get(ClassifierType.EMOTICON).getScores().get(Category.NEGATIVE) != 0){
			finalClassification = scoreMap.get(ClassifierType.EMOTICON).getClassification();
		} else if (scoreMap.get(ClassifierType.SENTIWORD).getClassification() == scoreMap.get(ClassifierType.NAIVEBAYES).getClassification()){
			finalClassification = scoreMap.get(ClassifierType.NAIVEBAYES).getClassification();
		} else{
			double totalPositive = tweet.getScoreMap().get(ClassifierType.SENTIWORD).getScores().get(Category.POSITIVE) +
					tweet.getScoreMap().get(ClassifierType.NAIVEBAYES).getScores().get(Category.POSITIVE);
			double totalNegative = tweet.getScoreMap().get(ClassifierType.SENTIWORD).getScores().get(Category.NEGATIVE) +
					tweet.getScoreMap().get(ClassifierType.NAIVEBAYES).getScores().get(Category.NEGATIVE);
			
			if (totalPositive>totalNegative && (totalPositive-totalNegative)/totalNegative > Constants.THRESHOLD){
				finalClassification = Category.POSITIVE;
			} else if (totalNegative>totalPositive && (totalNegative-totalPositive)/totalPositive > Constants.THRESHOLD){
				finalClassification = Category.NEGATIVE;
			} else {
				finalClassification = Category.NEUTRAL;
			}
			
		}

		tweet.setClassification(finalClassification);
	}

	// Normalize scores to range of 0 to 1
	public List<ClassifiedTweet> normalizeScoring(List<ClassifiedTweet> tweets) {

		Map<ClassifierType, MaxMin> maxMinMap = new HashMap<ClassifierType, MaxMin>();

		for (ClassifierType type: ClassifierType.getClassifierTypeToNormalize()){
			double max = Double.NEGATIVE_INFINITY;
			double min = Double.POSITIVE_INFINITY;

			for (ClassifiedTweet tweet: tweets){
				double positiveScore = tweet.getScoreMap().get(type).getScores().get(Category.POSITIVE)/tweet.getTerms().size();
				double negativeScore = tweet.getScoreMap().get(type).getScores().get(Category.NEGATIVE)/tweet.getTerms().size();

				max = positiveScore>max?positiveScore:max;
				max = negativeScore>max?negativeScore:max;

				min = positiveScore<min?positiveScore:min;
				min = negativeScore<min?negativeScore:min;

			}
			MaxMin maxMin = new MaxMin(max, min);
			maxMinMap.put(type, maxMin);
		}

		for (ClassifierType type: ClassifierType.getClassifierTypeToNormalize()){
			for (ClassifiedTweet tweet: tweets){
				double positiveScore = tweet.getScoreMap().get(type).getScores().get(Category.POSITIVE)/tweet.getTerms().size();
				double negativeScore = tweet.getScoreMap().get(type).getScores().get(Category.NEGATIVE)/tweet.getTerms().size();

				double maxScore = maxMinMap.get(type).getMax();
				double minScore = maxMinMap.get(type).getMin();

				positiveScore = (positiveScore-minScore)/(maxScore-minScore);
				negativeScore = (negativeScore-minScore)/(maxScore-minScore);
				tweet.getScoreMap().get(type).getScores().put(Category.POSITIVE,positiveScore);
				tweet.getScoreMap().get(type).getScores().put(Category.NEGATIVE,negativeScore);
			}
		}

		return tweets;
	}

	@Override
	public ClassifiedTweet classify(ClassifiedTweet tweet) {

		ClassifiedTweet returnTweet=null;

		Collection<String> terms = FilterUtils.doAllFilters(tweet.getTweetContent(),tweet.getTopic());		
		tweet.setTerms(terms);

		BayesClassifier bayesClassifier =new BayesClassifier(persistence);
		SentiWordClassifier sentiClassifier=new SentiWordClassifier(persistence);
		EmoticonClassifier emoticonClassifier = new EmoticonClassifier(persistence);

		bayesClassifier.classify(tweet);
		sentiClassifier.classify(tweet);
		emoticonClassifier.classify(tweet);

		return returnTweet;

	}

}
