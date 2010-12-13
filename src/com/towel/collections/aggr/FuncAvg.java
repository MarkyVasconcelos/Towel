package com.towel.collections.aggr;

public class FuncAvg implements AggregateFunc<Number>{
	private Number x;
	private int total;
	@Override
	public void update(Number obj) {
		x = new Double(x.doubleValue() + obj.doubleValue());
		total++;
	}

	@Override
	public Number getResult() {
		return x.doubleValue() / total;
	}

	@Override
	public void init() {
		x = new Double(0);
		total = 0;
	}
	
}
