package com.towel.swing.splash;

public class ProgressCalculator {
	private int proccesses;
	private int current;

	public ProgressCalculator(int processes) {
		this.proccesses = processes;
	}

	public int getNextPercent() {
		return (getPercent(++current));
	}

	public int getPercent(int currentProccess) {
		current = currentProccess;
		return (currentProccess * 100) / proccesses;
	}
}
