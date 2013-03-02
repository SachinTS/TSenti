package edu.nus.tp.engine.utils;

import java.util.HashMap;
import java.util.Map;

public enum Category {

	UNCLASSIFIED(0),
	POSITIVE(1),
	NEGATIVE(2),
	NEUTRAL(3),
	;

	private static Map<Integer, Category> valueToKeyMap=new HashMap<Integer,Category>(5);
	
	private Integer id;

	private Category (int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Category getCategoryForId(int id){
		return valueToKeyMap.get(id);
	}
}
