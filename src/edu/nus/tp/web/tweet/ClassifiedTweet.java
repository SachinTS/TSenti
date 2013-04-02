package edu.nus.tp.web.tweet;

import edu.nus.tp.engine.utils.Category;


public class ClassifiedTweet implements Cloneable{

	private String tweetContent;
	private Category classification;
	private String topic;
	private double weight;
	
	public ClassifiedTweet(String tweet, Category classification,String topic) {
		
		this.tweetContent=tweet;
		this.classification=classification;
		this.topic=topic;
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	public ClassifiedTweet(String tweet) {
		this(tweet, Category.UNCLASSIFIED,"");
	}

	public String getTweetContent() {
		return tweetContent;
	}

	public Category getClassification() {
		return classification;
	}

	public void setClassification(Category classification) {
		this.classification = classification;
	}

	public void setTweetContent(String tweetContent) {
		this.tweetContent = tweetContent;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

/*	public Collection<String> getTweetContentAsTokens(){
		if (tweetContent==null) return Lists.newArrayList();
		return Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().trimResults().split(tweetContent));
	}*/

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			ClassifiedTweet cloned = (ClassifiedTweet) super.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			System.out.println(e);
			return null;
		}
	}
}
