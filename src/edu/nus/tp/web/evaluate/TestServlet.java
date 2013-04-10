package edu.nus.tp.web.evaluate;

import static edu.nus.tp.engine.utils.Constants.TEMP_FILES;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nus.tp.engine.classifier.HybridClassifier;
import edu.nus.tp.engine.saver.RedisPersistence;
import edu.nus.tp.engine.utils.Category;
import edu.nus.tp.web.tweet.ClassifiedTweet;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet(description = "Evaluationg on Test dataset", urlPatterns = { "/TestServlet" })
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processResult(request,response);
	}

	private void processResult(HttpServletRequest request,
			HttpServletResponse response) {
		ArrayList<String> retrievedTweets=new ArrayList<String>(); 
		
		BufferedReader br = null;
		try
		{
			String sCurrentLine;
			String action=request.getParameter("action")+"";
			
			if(action.equals("negative"))
			{
				br = new BufferedReader(new FileReader(TEMP_FILES+"Negative_Test.txt"));
			}
			else
			{
				br = new BufferedReader(new FileReader(TEMP_FILES+"Positive_Test.txt"));
			}
			int count=0;
			while ((sCurrentLine = br.readLine()) != null) {				
				retrievedTweets.add(sCurrentLine);		
			}
			System.out.println(count);
		}catch(Exception e)
		{
			
		}
		
		System.out.println("List data 1 : "+retrievedTweets.get(0));
		List<ClassifiedTweet> unClassifiedTweetCollection = new ArrayList<ClassifiedTweet>();
		String topic=request.getSession().getAttribute("topic")+"";
		for (String eachUnprocessedTweet : retrievedTweets) {
			unClassifiedTweetCollection.add(new ClassifiedTweet(eachUnprocessedTweet, Category.UNCLASSIFIED,""));
		}
		HybridClassifier classifier=new HybridClassifier(new RedisPersistence());		
		
		Collection<ClassifiedTweet> classifiedCollection = classifier.classify(unClassifiedTweetCollection);
		
		for(ClassifiedTweet c:classifiedCollection)
		{
			System.out.println(c.getTweetContent()+":"+c.getClassification());
		}
		
		request.getSession().setAttribute("classifiedData",classifiedCollection);
		try {
			request.getRequestDispatcher("./displayResult.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processResult(request,response);
	}

}
