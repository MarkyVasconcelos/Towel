package com.towel.collections.aggr;

public class FuncConcat implements AggregateFunc<String>{
	private StringBuilder x;
	private String separator;
	
	public FuncConcat(String x){
		separator = x;
	}
	@Override
	public void update(String obj) {
		x.append(obj).append(separator);
	}

	@Override
	public String getResult() {
		return x.delete(x.length() - separator.length(), x.length()).toString();
	}

	@Override
	public void init() {
		x = new StringBuilder();
	}
	
}
