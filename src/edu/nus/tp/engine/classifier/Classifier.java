package edu.nus.tp.engine.classifier;

import edu.nus.tp.web.tweet.ClassifiedTweet;

public interface Classifier {

	public abstract ClassifiedTweet classify(ClassifiedTweet unClassifiedTweet);

}