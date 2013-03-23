package edu.nus.tp.engine.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public enum Category {

	UNCLASSIFIED(-2),
	POSITIVE(1),
	NEGATIVE(-1),
	NEUTRAL(0),
	;

	private static Map<Integer, Category> valueToKeyMap=new HashMap<Integer,Category>(5);

	static {
		for (Category cat: Category.values())
			valueToKeyMap.put(cat.getId(), cat);
	}

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
		if (id>=0 && id<=3){
			return valueToKeyMap.get(id);
		}
		return UNCLASSIFIED;
	}

	public static List<Category> getClassificationClasses(){
		return Lists.newArrayList(Category.POSITIVE, Category.NEGATIVE, Category.NEUTRAL);
	}
}
