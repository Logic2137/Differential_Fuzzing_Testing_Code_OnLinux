package org.openj9.test.typeAnnotation;



import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@interface TestAnn {
	String site();
}


@interface InvisAnn {}

@Retention(RetentionPolicy.RUNTIME)
@interface VisibleAnn {}
@Retention(RetentionPolicy.RUNTIME)
@interface ParamAnn0 {}


@Retention(RetentionPolicy.RUNTIME)
@interface ParamAnn1 {}


@Target(ElementType.TYPE_USE)
@interface InvisTypeAnn {}
@InvisAnn
@TestAnn(site="classAnnotation")
public class TypeAnnotatedTestClass <@TestAnn(site="typeParameterAnnotation") Bar
extends Iterable<@TestAnn(site="extendsGenericAnnotation") String>> extends @TestAnn(site="extendsAnnotation") Object
implements @TestAnn(site="implementsSerializableAnnotation") Serializable, @TestAnn(site="implementsIterableAnnotation") Iterable<Integer>
 {

	private static final long serialVersionUID = 1L;


	public TypeAnnotatedTestClass(@TestAnn(site="constructorAnnotation") String foo) {
		super();
	}

	@TestAnn(site="path length 0") Map<@TestAnn(site="path length 1") ?
			extends @TestAnn(site="path length 2") String, @TestAnn(site="path length 2") List<@TestAnn(site="path length 2")
			Object>> testPath;

	private String []@TestAnn(site="array")[] myArray = new String[1][1];

	@VisibleAnn public @TestAnn(site="fieldAndTypeAnnotatedField") String fieldAndTypeAnnotatedField = "fieldAndTypeAnnotatedField";
	public static void main(String[] args) {
		Annotation[] al = TypeAnnotatedTestClass.class.getAnnotations();
		for (Annotation a: al) {
			System.err.println(a.toString());
		}
		String myString = new @TestAnn(site="newAnnotation") String("TEST_STATUS: TEST_PASSED");
		System.err.println(myString);
		@SuppressWarnings("unused")
		Object myObj = (ArrayList<@TestAnn(site="list1") ? extends Iterator<String>>) new ArrayList <Iterator<String>> ();
		AnnotatedType tasc = TypeAnnotatedTestClass.class.getAnnotatedSuperclass();
	}

	@SuppressWarnings("rawtypes")
	@TestAnn(site="returnTypeAnnotation") Class myMethod(@InvisTypeAnn @TestAnn(site="formalParameterAnnotation") TypeAnnotatedTestClass arg)
			throws @TestAnn(site="throwsAnnotation") ClassNotFoundException {
		throw new ClassNotFoundException();
	}

	  <@TestAnn(site="methodGenericType") E extends @TestAnn(site="methodTypeBounds") Iterable> Bar myTypeAnnotatedMethod(Bar t) {
		    return t;
		  }

	void myParameterAnnotatedMethod(@ParamAnn0 @ParamAnn1 String foo, @ParamAnn0 Integer bar) {
		return;
	}

	public Iterator<Integer> iterator() {
		return null;
	}

	Object myTryCatch (@TestAnn(site="receiverTypeAnnotation") TypeAnnotatedTestClass<Bar> this ) {
		try {
			Class<?> c = myMethod(null);
			c.hashCode();
		} catch (@TestAnn(site="catchAnnotation") ClassNotFoundException e) {
			return null;
		}
		return null;
	}

	


}
