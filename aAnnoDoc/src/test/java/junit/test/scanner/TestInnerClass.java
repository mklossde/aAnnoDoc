package junit.test.scanner;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.source.JavaSourceScanner;
import org.openon.aannodoc.source.MethodDoc;

import junit.framework.Assert;

public class TestInnerClass {

	protected JavaSourceScanner scanner;
	protected JarDoc unit;
	
	public static void main(String[] args) throws Exception {
		TestInnerClass test=new TestInnerClass();
		
		test.open();
		
		test.test();
		
		System.out.println("end");
	}
	
	@Before
	public void open() throws Exception {
		scanner=new JavaSourceScanner(null,null);
		scanner.readFile("src/test/java/junit/test/scanner/TestInnerClassObj.java");
		unit=scanner.getUnit();
	}
	
	@Test
	public void test() throws Exception {
		FieldDoc fDoc; AnnotationDoc aDoc; List<AnnotationDoc> list;
		
		ClassDoc clDoc=unit.findClass("TestInnerClassObj");
		Assert.assertNotNull(clDoc);
		Assert.assertEquals("TestInnerClassObj",clDoc.getComment());
		aDoc=clDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDocTestInnerClassObj",aDoc.getComment());
		
		
		ClassDoc innerCl=unit.findClass("innerClass");
		Assert.assertNotNull(innerCl);
		Assert.assertEquals("innerClass",innerCl.getComment());
		aDoc=innerCl.getAnnotation(aDoc.class); Assert.assertNotNull(aDoc); Assert.assertEquals("aDocinnerClass",aDoc.getComment());
		
		ClassDoc innerCl2=unit.findClass("innerClass2");
		Assert.assertNotNull(innerCl2); 
		Assert.assertEquals("innerClass2",innerCl2.getComment());
		aDoc=innerCl2.getAnnotation(aDoc.class); Assert.assertNotNull(aDoc); Assert.assertEquals("aDocinnerClass2",aDoc.getComment());	

		ClassDoc outClass=unit.findClass("outClass");
		Assert.assertNotNull(outClass);
		Assert.assertEquals("outClass",outClass.getComment());
		aDoc=outClass.getAnnotation(aDoc.class); Assert.assertNotNull(aDoc); Assert.assertEquals("aDocoutClass",aDoc.getComment());	

		
		ClassDoc innerCl3=unit.findClass("innerClass3");
		Assert.assertNotNull(innerCl3);
		Assert.assertEquals("innerClass3",innerCl3.getComment());
		aDoc=innerCl3.getAnnotation(aDoc.class); Assert.assertNotNull(aDoc); Assert.assertEquals("aDocinnerClass3",aDoc.getComment());	
		

		
//		FieldDoc fDoc=clDoc.getField(unit, "x");
//		Assert.assertNotNull(fDoc);
//		Assert.assertEquals("x",fDoc.getComment());
	}
}
