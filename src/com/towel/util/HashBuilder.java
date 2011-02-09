package com.towel.util;

/**
 * Build a HASH value based on the added values. If none value is added,
 * DEFAULT_BASE is the hash returned in getHash() method.
 * <p>
 * The calculation used is proposed by Joshua Block in Effective Java p. 38
 * <p>
 * Above there's a sample of how to use hashBuilder:
 * 
 * <pre>
 * public class Person {
 * 	private String name;
 * 	private int age;
 * 
 * 	public Person(String name, int age) {
 * 		this.name = name;
 * 		this.age = age;
 * 	}
 * 
 * 	public int getAge() {
 * 		return age;
 * 	}
 * 
 * 	public int getName() {
 * 		return name;
 * 	}
 * 
 * 	public int hashCode() {
 * 		return new HashBuilder(name).add(age).hashCode();
 * 	}
 * }
 * </pre>
 */
public class HashBuilder {
	public static final int DEFAULT_BASE = 17;
	public static final int DEFAULT_SEED = 37;

	private int seed;
	private int base;

	private int result;

	public HashBuilder() {
		this(DEFAULT_BASE, DEFAULT_SEED);
	}

	public HashBuilder(int firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(long firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(char firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(boolean firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(float firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(double firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(Object firstValue) {
		this();
		add(firstValue);
	}

	public HashBuilder(int base, int seed) {
		if (base < 3)
			throw new IllegalArgumentException("Base must be bigger than 2!");

		if (seed < 3)
			throw new IllegalArgumentException("Seed must be bigger than 2!");

		this.seed = seed;
		this.base = base;

		result = base;
	}

	public void reset() {
		result = base;
	}

	public HashBuilder add(int value) {
		result = seed * result + value;
		return this;
	}

	public HashBuilder add(char value) {
		return add((short) value);
	}

	public HashBuilder add(long value) {
		return add((int) (value ^ (value >>> 32)));
	}

	public HashBuilder add(boolean value) {
		return add(value ? 1 : 0);
	}

	public HashBuilder add(float value) {
		return add(Float.floatToIntBits(value));
	}

	public HashBuilder add(double value) {
		return add(Double.doubleToLongBits(value));
	}

	public HashBuilder add(Object value) {
		return add(value == null ? 0 : value.hashCode());
	}

	public HashBuilder add(byte[] value) {
		for (byte b : value)
			add(b);
		return this;
	}

	public HashBuilder add(short[] value) {
		for (short s : value)
			add(s);
		return this;
	}

	public HashBuilder add(int[] value) {
		for (int i : value)
			add(i);
		return this;
	}

	public HashBuilder add(char[] value) {
		for (char c : value)
			add(c);
		return this;
	}

	public HashBuilder add(long[] value) {
		for (long l : value)
			add(l);
		return this;
	}

	public HashBuilder add(boolean[] value) {
		for (boolean b : value)
			add(b);
		return this;
	}

	public HashBuilder add(float[] value) {
		for (float f : value)
			add(f);
		return this;
	}

	public HashBuilder add(double[] value) {
		for (double d : value)
			add(d);
		return this;
	}

	public HashBuilder add(Object[] value) {
		for (Object o : value)
			add(o);
		return this;
	}

	@Override
	public boolean equals(Object value) {
		if (value == this)
			return true;

		if (!(value instanceof HashBuilder))
			return false;

		HashBuilder other = (HashBuilder) value;
		return other.result == result && other.base == base
				&& other.seed == seed;
	}

	@Override
	public int hashCode() {
		return result;
	}
}
