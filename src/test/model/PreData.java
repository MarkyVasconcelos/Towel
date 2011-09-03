package test.model;

import java.util.ArrayList;
import java.util.List;

public class PreData {
	public static List<Person> getSampleList() {
		List<Person> list2 = new ArrayList<Person>();
		list2.add(new Person("A", 10.0));
		list2.add(new Person("B", 20.0));
		list2.add(new Person("C", 30.0));
		list2.add(new Person("D", 40.0));
		list2.add(new Person("E", 50.0));
		return list2;
	}

	public static List<Person> getSampleList(int howMuch) {
		List<Person> list2 = new ArrayList<Person>();
		for (int i = 0; i < howMuch; i++)
			list2.add(new Person(String.valueOf(i), i, i % 2 == 0));
		return list2;
	}
}
