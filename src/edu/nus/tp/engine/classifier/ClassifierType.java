package edu.nus.tp.engine.classifier;

import java.util.List;

import com.google.common.collect.Lists;


public enum ClassifierType {
	EMOTICON,
	SENTIWORD,
	NAIVEBAYES;

	public static List<ClassifierType> getClassifierTypeToNormalize(){
		return Lists.newArrayList(ClassifierType.NAIVEBAYES, ClassifierType.SENTIWORD);
	}
}
