package org.openon.annodoc.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.AnnoDoc;

public class DocTest {

	public static void main(String[] args) throws IOException {
		String srcDir="src/main/java/";
		String docDir="doc/";
		
		String source=srcDir+"org/openon/onannodoc/example/SimpleJavaExample.java";
		String out=docDir+"SimpleJavaExample";
		String gen=AnnoDoc.GEN_JAVA; // AnnoDoc.GEN_AAPP; GEN_JAVA
		
		AnnoDoc doc=new AnnoDoc();
		Map options=AnnoDoc.toOptions(out,AnnoDoc.PDF,gen);
		
		doc.create(source,options);
		
		
		System.out.println("end");
	}
}
