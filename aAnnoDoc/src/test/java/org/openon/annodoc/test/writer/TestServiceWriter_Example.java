package org.openon.annodoc.test.writer;

public class TestServiceWriter_Example {

	public TestServiceWriter_Example t() { return this; }
	
	public String one() {
		String a="Hallo";
		TestServiceWriter_Example x=new TestServiceWriter_Example();
//		two(a);
//		x.two(a);
//		x.t().two(a); 
//		TestServiceWriter_Example.two(a); 
		return null;
	}
	
	
	public static String two(String a) { return "Hallo"; }
}
