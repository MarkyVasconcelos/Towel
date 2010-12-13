package com.towel.time;

public class Hour implements Cloneable {
	private double hour;
	private double minute;

	public Hour(double hour, double minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public Hour between(Hour other) {
		double hoursBetween = 0.0D;
		double minutesBetween = 0.0D;
		if (other.hour - hour >= 0.0D) {
			hoursBetween = other.hour - hour;
		} else {
			hoursBetween = (24D - hour) + other.hour;
		}
		if (other.minute - minute >= 0.0D) {
			minutesBetween = other.minute - minute;
		} else {
			minutesBetween = (60D - minute) + other.minute;
			hoursBetween--;
		}
		return new Hour(hoursBetween, minutesBetween);
	}

	public int minutesBetweenPlus(Hour end, Hour start) {
		if (!end.isAfter(start) || !end.isAfter(this))
			return 0;

		int startHour = (int) ((start.isAfter(this)) ? start.hour : hour);
		int startMinute = (int) ((start.isAfter(this)) ? start.minute : minute);

		return (int) ((end.hour - startHour) * 60 + end.minute - startMinute);
	}

	public boolean isAfter(Hour other) {
		if (hour > other.hour)
			return true;
		if (hour == other.hour)
			return (minute > other.minute);
		return false;
	}

	public int minutesBetween(Hour other) {
		Hour between = between(other);
		return (int) (between.minute + (between.hour * 60));
	}

	public Hour between(double otherHour, double otherMinute) {
		return between(new Hour(otherHour, otherMinute));
	}

	public static Hour between(Hour start, Hour end) {
		return start.between(end);
	}

	public static Hour between(Hour start, double endH, double endM) {
		return start.between(endH, endM);
	}

	public static Hour between(double startH, double startM, double endH,
			double endM) {
		return (new Hour(startH, startM)).between(endH, endM);
	}

	public Hour clone() {
		try {
			Hour hour = (Hour) super.clone();
			hour.hour = this.hour;
			hour.minute = this.minute;
			return hour;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String toString() {
		return (new StringBuilder(String.valueOf(String.valueOf((int) hour))))
				.append(":").append(String.valueOf((int) minute)).toString();
	}
}