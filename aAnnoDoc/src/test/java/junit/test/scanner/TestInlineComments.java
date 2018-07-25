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

public class TestInlineComments {

	public static void main(String[] args) throws Exception {
		TestInlineComments test=new TestInlineComments();
		test.test();
		System.out.println("end");
	}
	
	
	@Test
	public void test() throws Exception {
		JavaSourceScanner scanner=new JavaSourceScanner(null,null);
		scanner.readFile("src/test/java/junit/test/scanner/TestInlineCommentsObj.java");
		
		JarDoc unit=scanner.getUnit();
		ClassDoc clDoc=unit.findClass("TestInlineCommentsObj");
//System.out.println("doc:"+doc);
		Assert.assertNotNull(clDoc);
		Assert.assertEquals(0, clDoc.getComment().indexOf("TestInlineCommentsObj"));
		Assert.assertEquals("mk", clDoc.getAuthor());
		Assert.assertEquals("v1.0", clDoc.getVersion());
		Assert.assertEquals("dep", clDoc.getDeprecated());
		Assert.assertEquals("mk", AnnoUtils.getAuthor(clDoc));
		
//		 * @author mk 
//		 * @version v1.0
//		 * @deprecated dep
//		 * @category cat
//		 * @see see
//		 * @serial serial
//		 * @since since
		 
		FieldDoc fDoc; AnnotationDoc aDoc; List<AnnotationDoc> list;
//		String author=clDoc.getAuthor();
		aDoc=clDoc.getAnnotation("author");
		Assert.assertNotNull(aDoc);
		Assert.assertEquals("mk", aDoc.getComment().trim());
		
		
//		fDoc=clDoc.getField(unit, "aktion");
//		Assert.assertNotNull(fDoc);	
//		Assert.assertEquals("aktion", fDoc.getComment());
//		aDoc=fDoc.getAnnotation(aDoc.class);
//		Assert.assertNotNull(aDoc);
//		Assert.assertEquals("aDoc", aDoc.getComment());
//		
//		fDoc=clDoc.getField(unit, "aktion2");
//		Assert.assertNotNull(fDoc);	
//		Assert.assertEquals("aktion2", fDoc.getComment());
//		aDoc=fDoc.getAnnotation(aDoc.class);
//		Assert.assertNotNull(aDoc);
//		Assert.assertEquals("aDoc2", aDoc.getComment());
//		
//		fDoc=clDoc.getField(unit, "aktion3");
//		Assert.assertNotNull(fDoc);	
//		Assert.assertEquals("aktion3", fDoc.getComment());
//		aDoc=fDoc.getAnnotation(aDoc.class);
//		Assert.assertNotNull(aDoc);
//		Assert.assertEquals("aDoc3", aDoc.getComment());
//		Assert.assertEquals("title", aDoc.getValueString("title"));
//		
//		fDoc=clDoc.getField(unit, "docs");
//		Assert.assertNotNull(fDoc);	
//		Assert.assertEquals("docs", fDoc.getComment());
//		aDoc=fDoc.getAnnotation(aDocs.class);
//		Assert.assertNotNull(aDoc);
//		Assert.assertEquals("aDocs", aDoc.getComment());
//		list=(List<AnnotationDoc>)aDoc.getValueObject("value");
//		Assert.assertNotNull(list); 
//		Assert.assertEquals(3, list.size());
//		aDoc=list.get(0); Assert.assertNotNull(aDoc);Assert.assertEquals("one", aDoc.getComment());
//		aDoc=list.get(1); Assert.assertNotNull(aDoc);Assert.assertEquals("two", aDoc.getComment());
//		aDoc=list.get(2); Assert.assertNotNull(aDoc);Assert.assertEquals("three", aDoc.getComment());
//		
//		// Komments in one line------------------------------------------
//		fDoc=clDoc.getField(unit, "line");
//		Assert.assertNotNull(fDoc);	
//		Assert.assertEquals("line", fDoc.getComment());
//		aDoc=fDoc.getAnnotation(aDoc.class);
//		Assert.assertNotNull(aDoc);
//		Assert.assertEquals("aDocLine", aDoc.getComment());
//		
//		fDoc=clDoc.getField(unit, "docsLine");
//		Assert.assertNotNull(fDoc);	
//		Assert.assertEquals("docsLine", fDoc.getComment());
//		aDoc=fDoc.getAnnotation(aDocs.class);
//		Assert.assertNotNull(aDoc);
//		Assert.assertEquals("aDocsLine", aDoc.getComment());
//		list=(List<AnnotationDoc>)aDoc.getValueObject("value");
//		Assert.assertNotNull(list); 
//		Assert.assertEquals(3, list.size());
//		aDoc=list.get(0); Assert.assertNotNull(aDoc);Assert.assertEquals("oneLine", aDoc.getComment());
//		aDoc=list.get(1); Assert.assertNotNull(aDoc);Assert.assertEquals("twoLine", aDoc.getComment());
//		aDoc=list.get(2); Assert.assertNotNull(aDoc);Assert.assertEquals("threeLine", aDoc.getComment());
		

	}
	
	
	
}
