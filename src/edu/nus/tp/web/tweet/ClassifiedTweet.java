package edu.nus.tp.web.tweet;

import static edu.nus.tp.engine.utils.Constants.SPACE;

import java.util.Collection;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import edu.nus.tp.engine.utils.Category;


public class ClassifiedTweet {

	private String tweetContent;
	private Category classification;
	
	public ClassifiedTweet(String tweet, Category classification) {
		
		this.tweetContent=tweet;
		this.classification=classification;
	}
	
	public ClassifiedTweet(String tweet) {
		this(tweet, Category.UNCLASSIFIED);
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

/*	public Collection<String> getTweetContentAsTokens(){
		if (tweetContent==null) return Lists.newArrayList();
		return Lists.newArrayList(Splitter.on(SPACE).omitEmptyStrings().trimResults().split(tweetContent));
	}*/

}
