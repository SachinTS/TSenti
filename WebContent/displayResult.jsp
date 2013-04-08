<%@page import="edu.nus.tp.engine.utils.Category"%>
<%@page import="edu.nus.tp.web.tweet.ClassifiedTweet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="twitter4j.internal.org.json.JSONArray,twitter4j.internal.org.json.JSONException,twitter4j.internal.org.json.JSONObject,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<title>Twitter Sentiment</title>
</head>
<body>
	<center>
		<h2>Sentiment analysis</h2>
		<form name="display" method="post" action="./TrainClassifier">
			<table cellpadding=10>
				<%
					List result = (ArrayList) request.getSession().getAttribute(
							"classifiedData");
					Iterator<ClassifiedTweet> i = result.iterator();
					while (i.hasNext()) {
						ClassifiedTweet ct = (ClassifiedTweet) i.next();
				%>
				<tr>
					<td>
						<%
							if (ct.getClassification().equals(Category.NEGATIVE)) {
						%>
						<div class="alert alert-error">
							<%
								} else if (ct.getClassification().equals(Category.POSITIVE)){
							%>
							<div class="alert alert-success">
								<%
									}else {
								%>
								<div class="alert alert-info">
								<% } %>

								<%=ct.getTweetContent()%>
							</div>
					</td>
				</tr>
				<%
					}
				%>
			</table>
		</form>
	</center>


</body>
</html>