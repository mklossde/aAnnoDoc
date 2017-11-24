package org.openon.annodoc.test;

import java.io.IOException;

import org.openon.aannodoc.aAnnoDoc;

public class CreateDocumenation {

	public static void main(String[] args)  throws IOException  {
		CreateDocumenation doc=new CreateDocumenation();
		doc.createDocs();
		System.out.println("end");
	}
	
	public void createDocs() throws IOException {
//		String src="src/main/java"+"/org/openon/aannodoc";
//		aAnnoDoc.DocFiles(src+"/AnnoDoc.java",aAnnoDoc.FORMAT_ASCIIDOC);
		
//		String src="src/main";
		String src="src/main/java"+"/org/openon/aannodoc"+"/aAnnoDoc.java";
//		String src="src/main/java"+"/org/openon/aannodoc"+"/aAnnoDoc.adoc";
		
		aAnnoDoc adoc=new aAnnoDoc().scan(src,null);
//		adoc.createDocFiles("READ.md", null, aAnnoDoc.FORMAT_ASCIIDOC);
//		adoc.createDocFiles(aAnnoDoc.DEFAULT_FILE, "aAnnoDoc",aAnnoDoc.FORMAT_HTML);
		adoc.createDocFiles(null, "aAnnoDoc",aAnnoDoc.FORMAT_HTML);	
		
		
	}
}
