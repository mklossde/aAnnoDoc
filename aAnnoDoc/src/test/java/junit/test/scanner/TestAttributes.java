package junit.test.scanner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openon.aannodoc.Options;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.source.JavaSourceScanner;
import org.openon.aannodoc.utils.AnnoUtils;

import junit.framework.Assert;

public class TestAttributes {

	public static void main(String[] args) throws Exception {
		TestAttributes test=new TestAttributes();
		test.test();
		System.out.println("end");
	}
	
	
	@Test
	public void test() throws Exception {
		Options options=new Options();
		
		JavaSourceScanner scanner=new JavaSourceScanner(null,options);
		scanner.readFile("src/test/java/junit/test/scanner/TestAttributeObj.java");
		
		JarDoc unit=scanner.getUnit();
		ClassDoc clDoc=unit.findClass("TestAttributeObj");
		Assert.assertNotNull(clDoc);
		
		FieldDoc fDoc; AnnotationDoc anno; List<AnnotationDoc> list;
		
		fDoc=clDoc.getField(unit, "test");
		Assert.assertNotNull(fDoc);	
		anno=fDoc.getAnnotation(aDoc.class);
		Assert.assertNotNull(anno);
		
		String now=new SimpleDateFormat("dd.MM.YYYY").format(new Date());
		Assert.assertEquals(now,AnnoUtils.get(anno, aDoc.fDATE));
		
	}
	
	
	
}
