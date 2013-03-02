package edu.nus.tp.engine.utils;

import java.util.Collection;

/**
 * Add public static methods for various filters
 * 
 * This should be a decorator instead
 *
 */
public class FilterUtils {
	
	
	public static Collection<String> doAllFilters(Collection<String> rawInput) {
		
		rawInput=FilterUtils.filterSpecialCharacters(rawInput);
		rawInput=FilterUtils.filterStopWords(rawInput);
		rawInput=FilterUtils.lemmatize(rawInput);
		
		return rawInput;
	}


	public static Collection<String> filterStopWords(Collection<String> rawInput){
		
		//TODO Filter stop words here
		
		return rawInput; //for now
	}

	public static Collection<String> lemmatize(Collection<String> rawInput) {
		
		return rawInput; //for now
	}

	public static Collection<String> filterSpecialCharacters(Collection<String> rawInput) {
		return rawInput;
	}
	
	
	

}
