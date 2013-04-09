package edu.nus.tp.engine.classifier;

import java.util.HashMap;
import java.util.Map;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class SentiWordClassifier extends AbstractClassifier{

	public SentiWordClassifier(Persistence persistence) {
		super(persistence);
	}

	@Override
	public ClassifiedTweet classify(ClassifiedTweet tweet) {
		
		Map<Category, Double> scoreMap = new HashMap<Category, Double>();
		double positiveScore = 0;
		double negativeScore = 0;
		
		for (String eachTerm : tweet.getTerms()) {
			double thisScore=persistence.getSentiScoreForWord(eachTerm);
			if (thisScore>0) positiveScore+=thisScore;
			else negativeScore+=thisScore;
			
			//System.out.println("Senti score : "+eachTerm + ":::::"+ thisScore);
		}

		scoreMap.put(Category.POSITIVE, positiveScore);
		scoreMap.put(Category.NEGATIVE, negativeScore*-1);
		
		Score emoticonScore = new Score();
		emoticonScore.setScores(scoreMap);
		emoticonScore.setClassification(getClassification(scoreMap));
		tweet.getScoreMap().put(ClassifierType.SENTIWORD, emoticonScore);
		
		return tweet;
		
	}
	

}
