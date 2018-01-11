package org.openon.aannodoc.asciidoc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Asciidoctor.Factory;
import org.asciidoctor.AttributesBuilder;
import org.openon.aannodoc.aAnnoDoc;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;

/**
 * Crate Document with Ascii Doc
 * 
 * @author michael
 *
 */
public class AsciiDocCreator {


	
	protected Asciidoctor asciidoctor;
	
	public AsciiDocCreator() {
		asciidoctor = Factory.create();
		asciidoctor.requireLibrary("asciidoctor-diagram"); // add diagramm functions
	}
	
	
	public void create(Object adoc,String format,String outputFile) throws IOException {
		if(format==null || format.length()==0 || format.equals(aAnnoDoc.FORMAT_ASCIIDOC)) { createAdoc(adoc, outputFile); }
		else if(format.equals(aAnnoDoc.FORMAT_PDF)) { createPdf(adoc, outputFile); }
		else if(format.equals(aAnnoDoc.FORMAT_HTML)) { createHtml(adoc, outputFile); }
		else { throw new IOException("unkown format "+format); }
	}
	
	//-------------------------------------------------------------------------
	
	public void createAdoc(Object adoc,Object outputFile) throws IOException {
		PrintWriter wr;
		if(outputFile instanceof OutputStream) { wr=new PrintWriter((OutputStream)outputFile); }
		else if(outputFile instanceof PrintWriter) { wr=(PrintWriter)outputFile; }
		else if(outputFile instanceof File) { wr=new PrintWriter((File)outputFile);}
		else if(outputFile instanceof String) {
			String str=(String)outputFile;
			if(str.equals(aAnnoDoc.OUT_STDOUT)) { wr=new PrintWriter(System.out); }
			else { wr=new PrintWriter(getFile(str)); }
		}else { throw new IOException("unkown outputFile "+outputFile); }
		
		if(adoc instanceof String) { wr.write((String)adoc); }
		else { throw new IOException("unkown adoc "+adoc); }
		wr.close();
	}
	

	
	public void createHtml(Object adoc,String outputFile) throws IOException {	
		Options options=new Options();
		options.setSafe(SafeMode.UNSAFE);
				
		options.setBackend("html");
		options.setToFile(outputFile);
		
		create(adoc,options);
	}
	
	public void createPdf(Object adoc,String outputFile) throws IOException  {	
		
		Options options=new Options();
		options.setSafe(SafeMode.UNSAFE);
		
//		options.setOption("imagesDir", "../images/");
//		options.setOption("imagesoutdir", "../images/");

		options.setToFile(outputFile);
			
		create(adoc,options);
	}
	
	public void create(Object adoc,Options options) throws IOException  {	
		if(adoc instanceof String) {
			asciidoctor.convert((String)adoc,options);
		}else if(adoc instanceof File) {
			asciidoctor.convertFile((File)adoc,options);
		}else { throw new IOException("unkown adoc "+adoc); }
	}
	
	//-------------------------------------------------------------
	
	/** get file and create dirs of parent **/
	public File getFile(String fileName) throws IOException  {
		File file=new File(fileName); 
		if(file==null) { throw new IOException("no file for "+fileName); }
		File parent=file.getParentFile();
//		if(parent==null) { throw new IOException("no parent for "+fileName) ;}
		if(parent!=null) { parent.mkdirs(); }  // create parent dirs
		return file;
	}
	
}
