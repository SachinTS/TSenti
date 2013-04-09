package edu.nus.tp.engine.classifier;

import java.util.Map;

import edu.nus.tp.engine.utils.Category;

public class Score{
	
private Map<Category, Double> scores;
private Category classification;

public Map<Category, Double> getScores() {
	return scores;
}
public void setScores(Map<Category, Double> scores) {
	this.scores = scores;
}
public Category getClassification() {
	return classification;
}
public void setClassification(Category classification) {
	this.classification = classification;
}


	
}
