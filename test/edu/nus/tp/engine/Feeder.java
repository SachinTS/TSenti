package edu.nus.tp.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public class Feeder {

	public List <ClassifiedTweet> getTweets(String topic, int size) throws IOException, JSONException{
		String query = topic;
		int returnPerPage = size;

		URL twitterURL = new URL("http://search.twitter.com/search.json?q="+query+"&rpp="+ returnPerPage);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(twitterURL.openStream()));

		List <ClassifiedTweet> tweetList = new ArrayList<ClassifiedTweet>();
		String inputLine;
		while ((inputLine = in.readLine()) != null){
			JSONObject streamObj = new JSONObject(inputLine);
			JSONArray resultArray = (JSONArray) streamObj.get("results");
			for (int i =0; i< resultArray.length(); i++){
				JSONObject tweetObj = (JSONObject) resultArray.get(i);
				String tweet = tweetObj.get("text").toString();
				tweetList.add(new ClassifiedTweet(tweet,Category.UNCLASSIFIED));
			}
		}
		in.close();

		return tweetList;
	}
}


