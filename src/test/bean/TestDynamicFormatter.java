package test.bean;

import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import test.model.Person;

import com.towel.bean.DynamicFormatter;
import com.towel.bean.Formatter;
import com.towel.el.FieldResolver;

public class TestDynamicFormatter {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFormat() {
		// TESTE 1
		DynamicFormatter<Person> d = new DynamicFormatter<Person>(Person.class, " - ");
		d.addField(new FieldResolver(Person.class, "name"));
		d.addField(new FieldResolver(Person.class,"age"));
		d.addField(new FieldResolver(Person.class, "live"));

		Person myPojoMock = new Person("Felipe", 23,true, "29061991");
		Object s = d.format(myPojoMock);

		assertTrue( s instanceof String);
		assertTrue( s.equals("Felipe - 23 - true"));

		// TESTE 2
		d.clear();
		d.addField(new FieldResolver(Person.class,"name"));
		d.addField(new FieldResolver(Person.class,"age"));
		myPojoMock = new Person("Felipe", 23,true, "20101990");
		
		d.setSeparator(" ");

		s = d.format(myPojoMock);

		assertTrue( s instanceof String);
		assertTrue( s.equals("Felipe 23"));

		// TESTE UTILIZANDO O FORMAT

		d.clear();
		d.addField(new FieldResolver(Person.class, "name"));
		
		FieldResolver resolver=  new FieldResolver(Person.class, "money");
		resolver.setFormatter(new Formatter(){
			@Override
			public String format(Object obj) {
				return NumberFormat.getNumberInstance(new Locale("pt","pt_BR")).format(Double.parseDouble(obj.toString()));
			}

			@Override
			public Object parse(Object source) {
				return null;
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

		});
		d.addField(resolver);
		myPojoMock.setMoney(25.10);

		s = d.format(myPojoMock);

		assertTrue( s instanceof String);
		assertTrue( s.equals("Felipe 25,1"));

		// TESTE 4

		s = d.format(null);
		assertTrue( s instanceof String);
		assertTrue( s.equals(""));
	}

}
