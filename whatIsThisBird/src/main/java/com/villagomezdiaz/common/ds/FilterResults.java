package com.villagomezdiaz.common.ds;

public class FilterResults {

	private String filter;
	private double result;
	
	
	public FilterResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FilterResults(String filter, double result) {
		super();
		this.filter = filter;
		this.result = result;
	}
	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "FilterResults [filter=" + filter + ", result=" + result + "]";
	}
	
	
}
