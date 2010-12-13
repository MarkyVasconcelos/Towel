package test;

import java.util.List;

import test.model.Person;
import test.model.PreData;

public class CollectionsUtilTest {
	@SuppressWarnings("unused")
	private List<Person> list;
	
//	@Before
	public void setup(){
		PreData data = new PreData();
		list = data.getSampleList();
	}

}
