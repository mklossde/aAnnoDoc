package junit.test.scanner;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openon.aannodoc.source.CallDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.source.JavaSourceScanner;
import org.openon.aannodoc.source.MethodDoc;

import junit.framework.Assert;

public class TestCalls {

	protected JavaSourceScanner scanner;
	protected JarDoc unit;
	
	public static void main(String[] args) throws Exception {
		TestCalls test=new TestCalls();
		
		test.open();
		
		test.testMethods();
		test.testCalls();
		
		System.out.println("end");
	}
	
	@Before
	public void open() throws Exception {
		scanner=new JavaSourceScanner(null,null);
		scanner.readFile("src/test/java/junit/test/scanner/TestCallsObj.java");
		scanner.readFile("src/test/java/junit/test/scanner/TestCallsObj2.java");
		unit=scanner.getUnit();
	}
	
	@Test
	public void testCalls() throws Exception {
		ClassDoc clDoc=unit.findClass("TestCallsObj");Assert.assertNotNull(clDoc);
		MethodDoc mDoc=clDoc.getMethod(unit, "one");Assert.assertNotNull(mDoc);
		
		CallDoc cDoc=mDoc.getCall("two");
		Assert.assertNotNull(cDoc);
		
		// find method of call
		MethodDoc mTwo=cDoc.getMethod(unit);
		Assert.assertNotNull(mTwo);
		List<MethodDoc> l=cDoc.getMethods(unit);
		Assert.assertNotNull(l);Assert.assertEquals(1, l.size());
		
		// find calls with parameter
		CallDoc three=mTwo.getCall("three");
		Assert.assertNotNull(three);
		MethodDoc mThree=three.getMethod(unit);
		Assert.assertNotNull(mThree);
		
		// find extern class
		CallDoc six=mTwo.getCall("six");
		Assert.assertNotNull(six);
		MethodDoc mSix=six.getMethod(unit);
		Assert.assertNotNull(mSix);
		
		// find extern class
		CallDoc seven=mSix.getCall("seven");
		Assert.assertNotNull(seven);
		MethodDoc mSeven=seven.getMethod(unit);
		Assert.assertNotNull(mSeven);
		
		// find inner class
		CallDoc four=mTwo.getCall("four");
		Assert.assertNotNull(four);
		MethodDoc mFour=four.getMethod(unit);
		Assert.assertNotNull(mFour);
		
		// find two methods
		CallDoc cA=mTwo.getCall("a");
		MethodDoc mA=cA.getMethod(unit);
		Assert.assertNotNull(mA);
		List<MethodDoc> lA=cA.getMethods(unit);
		Assert.assertNotNull(lA);Assert.assertEquals(4, lA.size());
		
//		MethodDoc cm=cDoc.getMethod();
	}
		
	@Test
	public void testMethods() throws Exception {

		
		
		ClassDoc clDoc=unit.findClass("TestCallsObj");
		Assert.assertNotNull(clDoc);
		
		MethodDoc mDoc=clDoc.getMethod(unit, "one");
		Assert.assertNotNull(mDoc);
		
		MethodDoc mDoc2=clDoc.getMethod(unit, "two");
		Assert.assertNotNull(mDoc2);

		// find method by unit
		MethodDoc m=unit.findMethod("two");
		Assert.assertNotNull(m);
		
		// find method by unit
		MethodDoc a=unit.findMethod("three",new String[2]);
		Assert.assertNotNull(a);
		a=unit.findMethod("three",new String[] {"aa","bb"});
		Assert.assertNotNull(a);	
		a=unit.findMethod("three",new String[] {"String","String"});
		Assert.assertNotNull(a);
		
		//-----------------------------------------------
		

	
	}
	

	
}
