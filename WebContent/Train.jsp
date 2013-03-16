<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="twitter4j.internal.org.json.JSONArray,twitter4j.internal.org.json.JSONException,twitter4j.internal.org.json.JSONObject,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<body>
	<h2>Fetched Tweets</h2>

	<form name="display" method="post" action="./TrainClassifier">

		<table>
			<%
				List result = (ArrayList) request.getSession().getAttribute(
						"tweetData");
				Iterator<String> i = result.iterator();
				int j = 0;
				while (i.hasNext()) {
			%>

			<tr>
				<td><%=i.next()%><br></td>
				<td><input type="radio" name="classification<%=j%>" value="1">Positive</td>
				<td><input type="radio" name="classification<%=j%>" value="-1">Negative</td>
				<td><input type="radio" name="classification<%=j%>" value="0">Neutral</td>
			</tr>

			<%
				j++;
				}
			%>
			<tr>
				<td colspan="4"><input type="submit" value="submit" /></td>
			</tr>

		</table>
	</form>

</body>
</html>