package edu.nus.tp.engine.utils;

import java.util.Set;

import com.google.common.collect.Sets;

public class Constants {

	public static final String TEMP_FILES="/TweetData/";
	public static final String EMPTY_STRING="";
	public static final String SPACE=" ";

	public static final String TWEET_COUNT_BY_CATEGORY="TWEET_COUNT_BY_CATEGORY";
	public static final String TERM_COUNT_BY_CATEGORY="TERM_COUNT_BY_CATEGORY";

	public static final String REDIS_HOST = "50.30.35.9";
	/*public static final int REDIS_PORT = 2871;
	public static final String PASSWORD = "5cb88a1bcb1c2baae9335c4f964caf9d";*/
	public static final int REDIS_PORT = 2845;
	public static final String PASSWORD = "c4bcd8f4e55ad6048ec1e9bf0800979d";

	public static final String POSITIVE = "POSITIVE";
	public static final String NEUTRAL = "NEUTRAL";
	public static final String NEGATIVE = "NEGATIVE";
	public static final String SENTIWORDSCORE = "SENTIWORDSCORE";
	
	public static final double THRESHOLD = 0.04;
	public static final double NAIVE_CONSTANT = 7.5;
	

	public static final Set<String> STOP_WORDS=Sets.newHashSet("I","a","about","an","are","as","at","be","by","com","for","from","how","in","is","it","of","on","or","that","the","this","to","was","what","when","where","who","will","with","the","www","and");
	public static final Set<String> NEGATIVE_EMOTICONS=Sets.newHashSet(":o",":(","=(",":/",":'(",">:o",">.<","-__-",">:(","=|","o.O","o_O","O.O","O_O",":\\");
	public static final Set<String> POSITIVE_EMOTICONS=Sets.newHashSet("(:",":)","XD",";D",":-)","@_@",":P","8D",":1",":D",":-D",":p","=)","=p");

/**STOP WORDS FROM GOOGLE HISTORY **/
/**********************************
http://www.ranks.nl/resources/stopwords.html

I 
a 
about 
an 
are 
as 
at 
be 
by 
com 
for 
from
how
in 
is 
it 
of 
on 
or 
that
the 
this
to 
was 
what 
when
where
who 
will 
with
the
www
and

*/

}