package edu.nus.tp.engine;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import edu.nus.tp.engine.saver.InMemoryPersistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

public class TestClassifier {

	private static final double DELTA = 1e-30;
	BayesClassifier classifier=null;
	ClassifiedTweet tweet1, tweet2,tweet3,tweet4,testTweet;
			
	@Before
	public void setUp(){
		
		String tweet1String = "Chinese Beijing Chinese";
		String tweet2String = "Chinese Chinese Shanghai";
		String tweet3String = "Chinese Macao";
		String tweet4String = "Tokyo Japan Chinese";
		
		String testTweetString="Chinese Chinese Chinese Tokyo Japan";

		tweet1=new ClassifiedTweet(tweet1String, Category.POSITIVE);
		tweet2=new ClassifiedTweet(tweet2String, Category.POSITIVE);
		tweet3=new ClassifiedTweet(tweet3String, Category.POSITIVE);
		tweet4=new ClassifiedTweet(tweet4String, Category.NEGATIVE);
		
		testTweet=new ClassifiedTweet(testTweetString);
		
		classifier=new BayesClassifier(new InMemoryPersistence());
	}
	
	//@Test
	public void testLearning(){

		
		classifier.train(Lists.newArrayList(tweet1,tweet2,tweet3,tweet4));
		
		assertEquals(0.75, classifier.getPriorForCategory(Category.POSITIVE),DELTA);
		assertEquals(0.25, classifier.getPriorForCategory(Category.NEGATIVE),DELTA);
		
	}
	
	@Test
	public void testClassify(){
		classifier.train(Lists.newArrayList(tweet1,tweet2,tweet3,tweet4));
		ClassifiedTweet classifiedTweet=classifier.classify(testTweet);
		
		assertEquals(Category.POSITIVE,classifiedTweet.getClassification());
		
	}

}
