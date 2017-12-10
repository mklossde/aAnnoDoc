package org.openon.annodoc.test;

public class AnnotationObjectTest {

//	public static void main(String[] args) {
//		AnnotationObjectTest test=new AnnotationObjectTest();
//		
//		test.testOneAnnotation();
//		test.testMultiAnnotations();
//		test.testDoc();
//		
//		System.out.println("end");
//	}
//	
//	@Test
//	public void testDoc() {
//		String str="";
//		AnnotationDocScanner obj=AnnotationDocScanner.scan(str);
//		
//	}
//	
//	@Test
//	public void testMultiAnnotations() {
//		String str; AnnotationDocScanner obj;
//		
//		str="@see @look"; obj=AnnotationDocScanner.scan(str); obj=AnnotationDocScanner.scan(str,obj.pos);
//		Assert.assertEquals("look", obj.name);
//		str="@see@look"; obj=AnnotationDocScanner.scan(str); obj=AnnotationDocScanner.scan(str,obj.pos);
//		Assert.assertEquals(null, obj);
//		str="@see @look"; obj=AnnotationDocScanner.scan(str); obj=AnnotationDocScanner.scan(str,obj.pos);
//		Assert.assertEquals("look", obj.name);		
//
//	}
//	
//	
//	@Test
//	public void testOneAnnotation() {
//		// find start
//		Assert.assertEquals(null, AnnotationDocScanner.scan("x@x"));
//		Assert.assertEquals(null, AnnotationDocScanner.scan("@"));
//		Assert.assertEquals(null, AnnotationDocScanner.scan("@ "));
//		Assert.assertEquals(null, AnnotationDocScanner.scan("x@ "));
//		Assert.assertEquals(null, AnnotationDocScanner.scan(" @"));
//		
//		// find name
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see ").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see@").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see\\").name);	
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see[").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan(" @see ").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan("  		\t\n @see ").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan(" @see @ ").name);
//		
//		// name end
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see	").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see(").name);
//		Assert.assertEquals("see", AnnotationDocScanner.scan("@see@").name);
//		
//		// attribute empty
//		Assert.assertEquals(null, AnnotationDocScanner.scan("@see()").attr);
//		Assert.assertEquals(null, AnnotationDocScanner.scan("@see(").attr);
//		
//		Assert.assertEquals("my value", AnnotationDocScanner.scan("@see \"my value\"").value);
//		
//		// attribtues simple
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key=value)").attr.get("key"));		
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key=value,)").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key=value,a=b)").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see( key = value )").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(	key	=	value	)").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key=\"value\")").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key='value')").attr.get("key"));
//		Assert.assertEquals(null, AnnotationDocScanner.scan("@see(key=\"value)").attr);
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key= \"value\") ").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(key= \"value\"),a=b").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see(\"key\"=\"value\"").attr.get("key"));
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see('key'=\"value\"").attr.get("key"));		
//		Assert.assertEquals("value", AnnotationDocScanner.scan("@see ('key'=\"value\"").attr.get("key"));
//		
//		// multi values
//		Assert.assertEquals("b", AnnotationDocScanner.scan("@see(key=value,a=b)").attr.get("a"));
//		Assert.assertEquals("d", AnnotationDocScanner.scan("@see(key=value,a=b,c=d)").attr.get("c"));
//		Assert.assertEquals("d", AnnotationDocScanner.scan("@see(key=value,a=b,c='d')").attr.get("c"));
//		
//		// value 
//		Assert.assertEquals("Hallo", AnnotationDocScanner.scan("@see Hallo").value);
//		Assert.assertEquals("Hallo@", AnnotationDocScanner.scan("@example Hallo@").value);
//		Assert.assertEquals("Hallo", AnnotationDocScanner.scan("@example Hallo @").value);
//		Assert.assertEquals("Hallo", AnnotationDocScanner.scan("@example()Hallo").value);
//		Assert.assertEquals("Hallo", AnnotationDocScanner.scan("@example() Hallo").value);
//		
//		Assert.assertEquals("Value", AnnotationDocScanner.scan("@example(name=\"value\")\nValue").value);
//		Assert.assertEquals("<c:button label='Hallo'/>", AnnotationDocScanner.scan("@example(name=\"value\")\n<c:button label='Hallo'/>").value);
//		
//		Assert.assertEquals("\\@a", AnnotationDocScanner.scan(" @code(file=/src)\\@a").value);
//		
//		Assert.assertEquals("xx", AnnotationDocScanner.scan("@code xx @ ").value);
//		Assert.assertEquals("xx", AnnotationDocScanner.scan("@code xx @").value);
//		
////		Assert.assertEquals("<", AnnotationObject.scan("@<HELLO>").name);
//	}
}
