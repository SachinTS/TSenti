package edu.nus.tp.web.evaluate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nus.tp.engine.BayesClassifier;
import edu.nus.tp.engine.saver.RedisPersistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

/**
 * Servlet implementation class Evaluation
 */
@WebServlet(description = "Evaluate the sentiment", urlPatterns = { "/Evaluation" })
public class Evaluation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Evaluation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<String> retrievedTweets = (ArrayList<String>) request.getSession().getAttribute("tweetData");
		
		System.out.println("List data 1 : "+retrievedTweets.get(0));
		List<ClassifiedTweet> unClassifiedTweetCollection = new ArrayList<ClassifiedTweet>();
		
		for (String eachUnprocessedTweet : retrievedTweets) {
			unClassifiedTweetCollection.add(new ClassifiedTweet(eachUnprocessedTweet, Category.UNCLASSIFIED,request.getSession().getAttribute("topic")+""));
		}
		
		System.out.println("After loop...."+unClassifiedTweetCollection.size());
		BayesClassifier classifier=new BayesClassifier(new RedisPersistence());		
		Collection<ClassifiedTweet> classifiedCollection = classifier.classify(unClassifiedTweetCollection);
		for (ClassifiedTweet eachClassifiedTweet : classifiedCollection) {
			System.out.println(eachClassifiedTweet.getTweetContent() +":::::::::"+ eachClassifiedTweet.getClassification());
		}
	}

}
