package edu.nus.tp.engine;

import edu.nus.tp.engine.saver.Persistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class HybridClassifier extends AbstractClassifier {

	public HybridClassifier(Persistence persistence) {
		super(persistence);
	}

	@Override
	public ClassifiedTweet classify(ClassifiedTweet unClassifiedTweet) {
		
		ClassifiedTweet returnTweet=null;
		
		BayesClassifier bayesClassifier =new BayesClassifier(persistence);
		SentiWordClassifier sentiClassifier=new SentiWordClassifier(persistence);
		ClassifiedTweet unclassifiedTweetSentiCopy=null;
		
		try {
			unclassifiedTweetSentiCopy = (ClassifiedTweet)unClassifiedTweet.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		System.out.println("\nProcessing Tweet : "+unClassifiedTweet.getTweetContent());
		ClassifiedTweet bayesClassifiedTweet = bayesClassifier.classify(unClassifiedTweet);
		ClassifiedTweet sentiClassifiedTweet = sentiClassifier.classify(unclassifiedTweetSentiCopy);
		
		
		
		System.out.println("Bayes weight :"+bayesClassifiedTweet.getWeight()+" classification:"+bayesClassifiedTweet.getClassification());
		System.out.println("Senti weight :"+sentiClassifiedTweet.getWeight()+" classification:"+sentiClassifiedTweet.getClassification());
		
		
		if (bayesClassifiedTweet.getClassification()==sentiClassifiedTweet.getClassification()){
			returnTweet= sentiClassifiedTweet; //return either of them should be fine
		}
		else{
			
			//find absolutes
			double bayesWeight=Math.abs(bayesClassifiedTweet.getWeight());
			double sentiWeight=Math.abs(sentiClassifiedTweet.getWeight());
			boolean sentiWeightIsStrong=sentiWeight>0.25;
			boolean bayesWeightIsStrong=bayesWeight>0.25;			
			
			if (bayesWeightIsStrong && sentiWeightIsStrong){
				System.out.println("This is screwed up. Both are strong.. Neutral???");
				System.out.println("Bayes Weight :  "+ bayesWeight + "::: Sentiweight :: "+sentiWeight);
				bayesClassifiedTweet.setClassification(Category.NEUTRAL);
				returnTweet=bayesClassifiedTweet;
			}
			else if (sentiWeightIsStrong){
				returnTweet= sentiClassifiedTweet;
			}
			else if (bayesWeightIsStrong){
				returnTweet= bayesClassifiedTweet;
			}
			
		}
		return returnTweet;
		
	}

}
