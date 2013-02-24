package Training;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tweet.ClassifiedTweet;

/**
 * Servlet implementation class TrainClassifier
 */
@WebServlet("/TrainClassifier")
public class TrainClassifier extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrainClassifier() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List result = (ArrayList) request.getSession().getAttribute("tweetData");
		List<ClassifiedTweet> classifiedTweet=new ArrayList<ClassifiedTweet>();
		Iterator<String> i = result.iterator();
		int j = 0;		
		while (i.hasNext()) {
			String tweet=i.next();
			String cls=request.getParameter("classification"+j);
			if(cls!=null)
			{
				classifiedTweet.add(new ClassifiedTweet(tweet, Integer.parseInt(cls)));
			}			
			j++;
		}
		
		//Remove Stop words
		//add terms to the existing database of words
		
	}

}
