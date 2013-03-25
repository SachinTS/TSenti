package edu.nus.tp.engine;

import edu.nus.tp.web.tweet.ClassifiedTweet;

public interface Classifier {

	/**
	 * O(n*m) - not sure if there is a much more optimal way to do this
	 * 
	 * @param unClassifiedTweet
	 * @return
	 */
	public abstract ClassifiedTweet classify(ClassifiedTweet unClassifiedTweet);

}