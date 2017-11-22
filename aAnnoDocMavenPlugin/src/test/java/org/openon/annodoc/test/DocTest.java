package org.openon.annodoc.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.aAnnoDoc;

public class DocTest {

	public static void main(String[] args) throws IOException {
		String srcDir="src/main/java/";
		String docDir="doc/";
		
		String source=srcDir+"org/openon/onannodoc/example/SimpleJavaExample.java";
		String out=docDir+"SimpleJavaExample";
		String gen=aAnnoDoc.GENERATOR_JAVADOC; // AnnoDoc.GEN_AAPP; GEN_JAVA
		
		aAnnoDoc doc=new aAnnoDoc();
		Map options=aAnnoDoc.toOptions(out,aAnnoDoc.FORMAT_PDF,gen);
		
		doc.create(source,options);
		
		
		System.out.println("end");
	}
}
