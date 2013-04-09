package edu.nus.tp.engine.classifier;

public class MaxMin{

	private double max;
	private double min;
	
	public MaxMin (double max, double min){
		this.max = max;
		this.min = min;
	}
	
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}

}
