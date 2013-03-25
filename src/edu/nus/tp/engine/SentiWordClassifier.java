package edu.nus.tp.engine;

import java.util.Collection;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.engine.utils.FilterUtils;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class SentiWordClassifier extends AbstractClassifier{

	public SentiWordClassifier(Persistence persistence) {
		super(persistence);
	}

	@Override
	public ClassifiedTweet classify(ClassifiedTweet unClassifiedTweet) {
		//FIXME Seriously !! Are we going to do this again !!!
		Collection<String> eachParsedTweet = FilterUtils.doAllFilters(unClassifiedTweet.getTweetContent(),unClassifiedTweet.getTopic());
		
		double totalScore=0.0;
		for (String eachTerm : eachParsedTweet) {
			totalScore+=persistence.getSentiScoreForWord(eachTerm);
		}

		//This is a very primitive way to do this. Need to figure out something
		Category maxCategory=totalScore>0?Category.POSITIVE:Category.NEGATIVE;
		
		unClassifiedTweet.setClassification(maxCategory);
		
		return unClassifiedTweet;
		
	}
	
	
	
	

}
