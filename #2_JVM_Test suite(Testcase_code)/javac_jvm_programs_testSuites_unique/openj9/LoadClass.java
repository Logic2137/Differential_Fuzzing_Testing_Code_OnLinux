package com.ibm.j9.offload.tests.mapping;




public class LoadClass {

	public static void main(String[] args) {
		Class theClass;
		try {
			theClass = Class.forName(args[0]);
		} catch (ClassNotFoundException e){
			System.out.println("NOT FOUND");
			System.out.flush();
			return;
		} catch (Throwable t){
			return;
		}
		System.out.println("!FOUND!");
		System.out.flush();
		try {
			Object theObject = theClass.newInstance();
			System.out.println(theObject.toString());
			System.out.flush();
		} catch (InstantiationException e){
			
		} catch (IllegalAccessException e){
		
		}
	}
}
