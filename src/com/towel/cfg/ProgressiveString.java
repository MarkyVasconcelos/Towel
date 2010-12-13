package com.towel.cfg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProgressiveString {
	private List<CharInterval> charsIntervals;
	private int charIntervalIndex;
	private int minChar;
	private int maxChar;
	private char[] string;

	public ProgressiveString(CharInterval... charIntervals) {
		this("", charIntervals);
	}

	public ProgressiveString(String original, CharInterval... charIntervals) {
		if (charIntervals.length == 0)
			throw new RuntimeException("CharIntervals can't be null!");
		string = original.toCharArray();
		charsIntervals = Arrays.asList(charIntervals);
		Collections.sort(charsIntervals);
		charIntervalIndex = 0;
		minChar = (int) charsIntervals.get(0).first;
		maxChar = (int) charsIntervals.get(charsIntervals.size() - 1).last;
	}

	private char[] copyChars(int start, char[] old) {
		char[] str = new char[start + old.length];
		for (int i = 0; i < old.length; i++)
			str[start + i] = old[i];
		return str;
	}

	public void increase() {
		char[] str = string.clone();
		for (int i = str.length - 1; i >= 0; i--) {
			IncreaseResult result = tryIncrease(str[i]);
			str[i] = result.newChar;
			if (result.result == IncreaseResult.INCREASED)
				break;
			if (result.result == IncreaseResult.DECREASED) {
				if (i - 1 == -1) {
					str = copyChars(1, str);
					str[0] = (char) minChar;
					break;
				}
			}
		}
		string = str.clone();
	}

	private IncreaseResult tryIncrease(char c) {
		if (((int) c) == maxChar) {
			charIntervalIndex = 0;
			return new IncreaseResult((char) minChar, IncreaseResult.DECREASED);
		} else
			return new IncreaseResult(nextChar(c), IncreaseResult.INCREASED);
	}

	private char nextChar(char c) {
		int i = (int) c;
		if (i == charsIntervals.get(charIntervalIndex).last) {
			charIntervalIndex++;
			return (char) charsIntervals.get(charIntervalIndex).first;
		}
		return (char) (((int) c) + 1);
	}

	@Override
	public String toString() {
		return new String(string);
	}

	public static void main(String[] args) {
		ProgressiveString str = new ProgressiveString(
				String.valueOf((char) 33), new CharInterval('a', 'm'), new CharInterval('n', 'z'));
//		JFrame frame = new JFrame("MutableString");
//		JLabel label = new JLabel(" ");
//		frame.add(label);
//		frame.pack();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
		int loops = 2500000;
		for (int i = 0; i < loops; i++, str.increase(), System.out.println(str))
			;
	}

	public static class CharInterval implements Comparable<CharInterval> {
		protected int first;
		protected int last;

		public CharInterval(int first, int last) {
			this.first = first;
			this.last = last;
		}

		public boolean canIncrease(char character) {
			int i = (int) character;
			return i >= first && i <= last;
		}

		@Override
		public int compareTo(CharInterval arg0) {
			if (first < arg0.first)
				return -1;
			if (first > arg0.first)
				return 1;
			return 0;
		}
	}

	private class IncreaseResult {
		protected char newChar;
		protected int result;

		public IncreaseResult(char newC, int result) {
			newChar = newC;
			this.result = result;
		}

		protected static final int INCREASED = 0;
		protected static final int DECREASED = 1;
	}
}
