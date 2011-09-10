package test.el;

import test.model.EPerson;
import test.model.Person;
import test.model.PersonHolder;

import com.towel.bean.DefaultFormatter;
import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.el.handler.FieldHandler;

public class FieldAcessHandlerTest {
	public void testFieldAccess() {
		Person p = new Person("Mark", 19);
		FieldHandler handler = new FieldHandler();
		handler.resolveField(Person.class, "name");
		System.out.println(handler.getValue(p, new DefaultFormatter()).equals(
				"Mark"));
	}

	public void testInnerFieldAccess() {
		Person p = new Person("Mark", 19);
		Person other = new Person("Marcos", 40);
		p.setParent(other);

		FieldHandler handler = new FieldHandler();
		handler.resolveField(Person.class, "parent.name");
		System.out.println(handler.getValue(p, new DefaultFormatter()).equals(
				"Marcos"));
	}

	public void testHierarchyFieldAccess() {
		Person p = new EPerson("Mark", 19);

		FieldHandler handler = new FieldHandler();
		handler.resolveField(EPerson.class, "name");
		System.out.println(handler.getValue(p, new DefaultFormatter()).equals(
				"Mark"));
	}

	public void testHierarchyFieldAccess2() {
		PersonHolder holder = new PersonHolder();
		holder.setPerson(new EPerson("Mark", 19));

		FieldHandler handler = new FieldHandler();
		handler.resolveField(PersonHolder.class, "person.name");
		System.out.println(handler.getValue(holder, new DefaultFormatter())
				.equals("Mark"));
	}

	public void testHierarchyInnerFieldAccess() {
		Person p = new EPerson("Mark", 19);
		Person other = new EPerson("Marcos", 40);
		p.setParent(other);

		FieldHandler handler = new FieldHandler();
		handler.resolveField(EPerson.class, "parent.name");
		System.out.println(handler.getValue(p, new DefaultFormatter()).equals(
				"Marcos"));
	}

	public void testAnnotationResolver() {
		Person p = new Person("Mark", 19);

		AnnotationResolver solver = new AnnotationResolver(Person.class);
		FieldResolver[] res = solver.resolve("name");
		System.out.println(res[0].getValue(p).equals("Mark"));
	}

	public void testAnnotationResolverInner() {
		Person p = new Person("Mark", 19);
		p.setParent(new Person("Markk", 40));

		AnnotationResolver solver = new AnnotationResolver(Person.class);
		FieldResolver[] res = solver.resolve("parent.name");
		System.out.println(res[0].getValue(p).equals("Markk"));
	}

	public void testAnnotationResolverHierarq() {
		EPerson p = new EPerson("Mark", 19);

		AnnotationResolver solver = new AnnotationResolver(EPerson.class);
		FieldResolver[] res = solver.resolve("name");
		System.out.println(res[0].getValue(p).equals("Mark"));
	}

	public static void main(String[] args) {
		FieldAcessHandlerTest tests = new FieldAcessHandlerTest();
		tests.testFieldAccess();
		tests.testInnerFieldAccess();
		tests.testHierarchyFieldAccess();
		tests.testHierarchyInnerFieldAccess();
		tests.testAnnotationResolver();
		tests.testAnnotationResolverInner();
		tests.testAnnotationResolverHierarq();
		tests.testHierarchyFieldAccess2();
	}
}
