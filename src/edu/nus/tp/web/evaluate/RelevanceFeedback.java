package edu.nus.tp.web.evaluate;

import static edu.nus.tp.engine.utils.Constants.TEMP_FILES;

import java.io.BufferedReader;
import java.io.FileReader;
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

import edu.nus.tp.engine.classifier.BayesClassifier;
import edu.nus.tp.engine.classifier.HybridClassifier;
import edu.nus.tp.engine.saver.RedisPersistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

/**
 * Servlet implementation class Evaluation
 */
@WebServlet(description = "Feedback the classification results", urlPatterns = { "/RelevanceFeedback" })
public class RelevanceFeedback extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RelevanceFeedback() {
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
		
		String accuracy=request.getParameter("accuracy")+"";
		
		if (accuracy.equals("accurate")){
			List<ClassifiedTweet> classifiedTweets = (ArrayList<ClassifiedTweet>) request.getSession().getAttribute(
					"classifiedData");
			BayesClassifier classifier=new BayesClassifier(new RedisPersistence());
			
			for (ClassifiedTweet tweet:  classifiedTweets) 
				if (tweet.getClassification()==Category.NEUTRAL) classifiedTweets.remove(tweet);
			classifier.train(classifiedTweets);
		}
		
		request.getRequestDispatcher("./Evaluate.html").forward(request, response);
	}

}
