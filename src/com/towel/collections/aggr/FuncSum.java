package com.towel.collections.aggr;

public class FuncSum implements AggregateFunc<Number>{
	private Number x;
	@Override
	public void update(Number obj) {
		x = new Double(x.doubleValue() + obj.doubleValue());
	}

	@Override
	public Number getResult() {
		return x;
	}

	@Override
	public void init() {
		x = new Double(0);
	}
	
}
