package junit.test.scanner;

import org.junit.Test;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.source.JavaSourceScanner;
import org.openon.aannodoc.source.TypeDoc;

public class TestScanner {

	public static void main(String[] args) throws Exception {
		TestScanner test=new TestScanner();
		test.test();
		System.out.println("end");
	}
	
	@Test
	public void test() throws Exception {
		JavaSourceScanner scanner=new JavaSourceScanner(null,null);
		scanner.readDir("src/test/java/junit/test/scanner");
		
		JarDoc unit=scanner.getUnit();
		DocObject doc=unit.findClass("TestObject");
System.out.println("doc:"+doc);

//		Object doc2=ReflectUtil.get(doc, "obj");
//System.out.println("doc2:"+doc2);

//System.out.println("x:"+((ClassDoc)doc).getLocalFields());
		ClassDoc cl=((ClassDoc)doc);
		TypeDoc obj2=cl.getLocalField("obj");
System.out.println("doc2:"+obj2.getTypeName());
System.out.println("doc2:"+obj2.getSimpleName());

		JarDoc u=obj2.findJarDoc();
//System.out.println("u:"+u.findClass("TestObject"));		
//		System.out.println("x:"+obj2.findClass("TestObject"));		
		Object docObj2=obj2.getTypeClass();
System.out.println("docObj2:"+docObj2);

	}
}
