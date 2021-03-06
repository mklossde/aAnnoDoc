package org.openon.annodoc.test;

import java.io.IOException;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;

public class CreateDocumenation {

	public static void main(String[] args)  throws IOException  {
		CreateDocumenation doc=new CreateDocumenation();		
//		doc.createReadme();
		doc.createDocument();
//		doc.createExamples();
		
//		doc.create();
		
		System.out.println("end");
	}
	
	public void createExamples() throws IOException {
		String src="src/test/java"+"/examples"+"/SimpleaDocExample.java";
		aAnnoDoc adoc=new aAnnoDoc().scan(src,null,null);
		adoc.createDocFiles("doc/SimpleaDocExample", aAnnoDoc.GENERATOR_ADOC, aAnnoDoc.FORMAT_HTML);	
	}
	
	public void createReadme() throws IOException {
		String src="src/main/java"+"/org/openon/aannodoc"+"/aAnnoDoc.java";
		
		aAnnoDoc adoc=new aAnnoDoc().scan(src,null,null);
		adoc.createDocFiles("doc/READ.md", aAnnoDoc.GENERATOR_ADOC, aAnnoDoc.FORMAT_ASCIIDOC);
	}
	
	public void createDocument() throws IOException {
		String src="src/main/java";
		
		aAnnoDoc adoc=new aAnnoDoc().scan(src,null,null);
//		adoc.createDocFiles("doc/aAnnoDoc",aAnnoDoc.GENERATOR_ADOC,aAnnoDoc.FORMAT_HTML);
		Options options=new Options(null,"doc/aAnnoDoc",aAnnoDoc.GENERATOR_ADOC,aAnnoDoc.FORMAT_HTML);
//		options.put(Options.OPTION_APP_VERSION, "V2");
//		options.put(Options.OPTION_APP_TITLE, "myTitle");
//		options.put(Options.OPTION_APP_DATE, "ddd");
//		options.put(Options.OPTION_APP_AUTHOR, "xxx");
		adoc.create(options);
	}
	
	public void create() throws IOException {
//		String src="src/main/java"+"/org/openon/aannodoc";
//		aAnnoDoc.DocFiles(src+"/AnnoDoc.java",aAnnoDoc.FORMAT_ASCIIDOC);
		
		String src="src/main/java";
//		String src="src/main/java"+"/org/openon/aannodoc"+"/aAnnoDoc.java";
//		String src="src/main/java"+"/org/openon/aannodoc"+"/test.adoc";
		
		aAnnoDoc adoc=new aAnnoDoc().scan(src,null,null);
//		adoc.createDocFiles("READ.md", null, aAnnoDoc.FORMAT_ASCIIDOC);
//		adoc.createDocFiles(aAnnoDoc.DEFAULT_FILE, "aAnnoDoc",aAnnoDoc.FORMAT_HTML);
		adoc.createDocFiles("doc/aAnnoDoc",aAnnoDoc.GENERATOR_ADOC, aAnnoDoc.FORMAT_HTML);	
	}
}
