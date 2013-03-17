package edu.nus.tp.web.evaluate;

import java.io.IOException;
import java.util.ArrayList;
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
		
		List result = (ArrayList) request.getSession()
				.getAttribute("tweetData");
		List<ClassifiedTweet> unClassifiedTweetCollection = new ArrayList<ClassifiedTweet>();
		Iterator<String> i = result.iterator();
		int j = 0;
		
		while (i.hasNext()) {
			String tweet=i.next();
			String cls=request.getParameter("classification"+j);
			if(cls!=null)
			{
				unClassifiedTweetCollection.add(new ClassifiedTweet(tweet, Category.getCategoryForId(0),request.getSession().getAttribute("topic")+""));
			}			
			j++;
		}
		BayesClassifier classifier=new BayesClassifier(new RedisPersistence());		
		classifier.classify(unClassifiedTweetCollection);
	}

}
