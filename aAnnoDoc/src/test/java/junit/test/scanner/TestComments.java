package junit.test.scanner;

import java.util.List;

import org.junit.Test;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aDoc.aDocs;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.source.JavaSourceScanner;
import org.openon.aannodoc.utils.AnnoUtils;

import junit.framework.Assert;

public class TestComments {

	public static void main(String[] args) throws Exception {
		TestComments test=new TestComments();
		
		test.testCommentsObj();
		test.testCommentsAnno();
		
		System.out.println("end");
	}
	
	@Test
	public void testCommentsAnno() throws Exception {
		JavaSourceScanner scanner=new JavaSourceScanner(null,null);
		scanner.readFile("src/test/java/junit/test/scanner/TestCommentsAnno.java");
		
		FieldDoc fDoc; AnnotationDoc aDoc; List<AnnotationDoc> list;
		
		JarDoc unit=scanner.getUnit();
		ClassDoc clDoc=unit.findClass("TestCommentsAnno");
//System.out.println("doc:"+doc);
		Assert.assertNotNull(clDoc);
		
		// test comment of class
		Assert.assertEquals("TestCommentsAnno", clDoc.getComment());
		aDoc=clDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDocTestCommentsAnno", aDoc.getComment());
				
		
		fDoc=clDoc.getField(unit, "aktion");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("aktion", fDoc.getComment());
		Assert.assertEquals("String",fDoc.getTypeName());
		
		fDoc=clDoc.getField(unit, "aktion2");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("aktion2", fDoc.getComment());		
		aDoc=fDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDoc", aDoc.getComment());		
		Assert.assertEquals("String",fDoc.getTypeName());
		
		fDoc=clDoc.getField(unit, "length");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("length", fDoc.getComment());		
		aDoc=fDoc.getAnnotation(Deprecated.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("Deprecated", aDoc.getComment());		
		Assert.assertEquals("Int",fDoc.getTypeName());
	}
	
	
	@Test
	public void testCommentsObj() throws Exception {
		JavaSourceScanner scanner=new JavaSourceScanner(null,null);
		scanner.readFile("src/test/java/junit/test/scanner/TestCommentsObj.java");
//scanner.readFile("src/test/java/junit/test/scanner/TestCommentsObj2.java");
		
		FieldDoc fDoc; AnnotationDoc aDoc; List<AnnotationDoc> list;
		
		JarDoc unit=scanner.getUnit();
		ClassDoc clDoc=unit.findClass("TestCommentsObj");
//ClassDoc clDoc=unit.findClass("TestCommentsObj2");		
//System.out.println("doc:"+doc);
		Assert.assertNotNull(clDoc);		
		
		// Test class commment
		Assert.assertEquals("TestCommentsObj", clDoc.getComment());
		
		// test annoation
		aDoc=clDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDocTestCommentsObj", aDoc.getComment());
		// check only one annotation
		List<AnnotationDoc> aList=clDoc.findAnnotation(aDoc.class);
		Assert.assertNotNull(aList);
		Assert.assertEquals(1, aList.size());		
		
		fDoc=clDoc.getField(unit, "aktion");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("aktion", fDoc.getComment());
		aDoc=fDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDoc", aDoc.getComment());
		
		fDoc=clDoc.getField(unit, "aktion2");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("aktion2", fDoc.getComment());
		aDoc=fDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDoc2", aDoc.getComment());
		
		fDoc=clDoc.getField(unit, "aktion3");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("aktion3", fDoc.getComment());
		aDoc=fDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDoc3", aDoc.getComment());
		Assert.assertEquals("title", aDoc.getValueString("title"));
		
		fDoc=clDoc.getField(unit, "docs");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("docs", fDoc.getComment());
		aDoc=fDoc.getAnnotation(aDocs.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDocs", aDoc.getComment());
		list=(List<AnnotationDoc>)aDoc.getValueObject("value");
		Assert.assertNotNull(list); 
		Assert.assertEquals(3, list.size());
		aDoc=list.get(0); Assert.assertNotNull(aDoc);Assert.assertEquals("one", aDoc.getComment());
		aDoc=list.get(1); Assert.assertNotNull(aDoc);Assert.assertEquals("two", aDoc.getComment());
		aDoc=list.get(2); Assert.assertNotNull(aDoc);Assert.assertEquals("three", aDoc.getComment());
		
		// Komments in one line------------------------------------------
		fDoc=clDoc.getField(unit, "line");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("line", fDoc.getComment());
		aDoc=fDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDocLine", aDoc.getComment());
		
		fDoc=clDoc.getField(unit, "docsLine");
		Assert.assertNotNull(fDoc);	
		Assert.assertEquals("docsLine", fDoc.getComment());
		aDoc=fDoc.getAnnotation(aDocs.class);
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("aDocsLine", aDoc.getComment());
		list=(List<AnnotationDoc>)aDoc.getValueObject("value");
		Assert.assertNotNull(list); 
		Assert.assertEquals(3, list.size());
		aDoc=list.get(0); Assert.assertNotNull(aDoc);Assert.assertEquals("oneLine", aDoc.getComment());
		aDoc=list.get(1); Assert.assertNotNull(aDoc);Assert.assertEquals("twoLine", aDoc.getComment());
		aDoc=list.get(2); Assert.assertNotNull(aDoc);Assert.assertEquals("threeLine", aDoc.getComment());
		
//		List<AnnotationDoc> list=clDoc.findAnnotation(aDoc.class);
//		for(int i=0;i<list.size();i++) {
//			AnnotationDoc a=list.get(i);	
//			String d=AnnoUtils.getComment(a);
//System.out.println("d:"+d);			
//		}
	}
	
	
	
}
