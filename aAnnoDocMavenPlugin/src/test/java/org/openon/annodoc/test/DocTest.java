package org.openon.annodoc.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;

public class DocTest {

	public static void main(String[] args) throws IOException {
		String srcDir="src/main/java/";
		String docDir="doc/";
		
		String source=srcDir+"org/openon/onannodoc/example/SimpleJavaExample.java";
		String out=docDir+"SimpleJavaExample";
		String gen=aAnnoDoc.GENERATOR_JAVADOC; // AnnoDoc.GEN_AAPP; GEN_JAVA
		
		aAnnoDoc doc=new aAnnoDoc();
		Options options=new Options(source,out,gen,aAnnoDoc.FORMAT_PDF);
		
		doc.create(options);
		
		
		System.out.println("end");
	}
}
