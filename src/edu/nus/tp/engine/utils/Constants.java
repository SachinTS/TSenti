package edu.nus.tp.engine.utils;

import static com.google.common.base.CharMatcher.inRange;

import java.util.Set;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Sets;

public class Constants {
	
	public static final String EMPTY_STRING="";
	public static final String SPACE=" ";
	public static final Set<String> STOP_WORDS=Sets.newHashSet("I","a","about","an","are","as","at","be","by","com","for","from","how","in","is","it","of","on","or","that","the","this","to","was","what","when","where","who","will","with","the","www");
	
	
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

*/
	
}
