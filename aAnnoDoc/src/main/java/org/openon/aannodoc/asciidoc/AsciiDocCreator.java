package org.openon.aannodoc.asciidoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Asciidoctor.Factory;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.utils.DocUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crate Document with Ascii Doc
 * 
 * @author michael
 *
 */
public class AsciiDocCreator {
	private static final Logger LOG=LoggerFactory.getLogger(AsciiDocCreator.class);
	
	public static final String ENCODE_UTF8="UTF8";
	
	protected Asciidoctor asciidoctor;
	protected String oldWorkDir;
	
	protected Map asciidoctorAttribtues;
	public static final String ASCIIDOC_ATTR_DOT="dot";
	
	public static void createDoc(Object adoc,String format,String outputFile)  throws IOException {
//		aAnnoDoc.changeWorkDir(outputFile);
		AsciiDocCreator doc=new AsciiDocCreator();
		doc.create(adoc, format, outputFile);
//		aAnnoDoc.replaceWorkDir();
	}
	
	
	//-----------------------------------------------------------------------------------------
	
	public AsciiDocCreator() {}
	public AsciiDocCreator(Map asciidoctorAttribtues) { this.asciidoctorAttribtues=asciidoctorAttribtues;}
	
	public void init() {
		asciidoctor = Factory.create();
		asciidoctor.requireLibrary("asciidoctor-diagram"); // add diagramm functions
	}
	
	/** set attribtus for aciidoctor **/
	public void setAsciidoctorAttribtues(Map asciidoctorAttribtues) { this.asciidoctorAttribtues=asciidoctorAttribtues; }
	/** set Graphviz path (path to executable dot.exe) **/ 
	public void setAttribtueGraphviz(String dotExecutablePath) { if(this.asciidoctorAttribtues==null) { this.asciidoctorAttribtues=new HashMap(); } this.asciidoctorAttribtues.put(ASCIIDOC_ATTR_DOT, dotExecutablePath);}
	
	public void create(Object adoc,String format,String outputFile) throws IOException {
		if(outputFile==null || outputFile.length()==0) { throw new IOException("outputFile missing"); }
		new File(outputFile).getAbsoluteFile().getParentFile().mkdirs(); // create all dirs to doc
		if(format==null || format.length()==0 || format.equals(aAnnoDoc.FORMAT_ASCIIDOC)) { createAdoc(adoc, outputFile); }
		else if(format.equals(aAnnoDoc.FORMAT_PDF)) { createPdf(adoc, outputFile); }
		else if(format.equals(aAnnoDoc.FORMAT_HTML)) { createHtml(adoc, outputFile); }
		else { throw new IOException("unkown format "+format); }
	}
	
	
	//-------------------------------------------------------------------------
	
	public void createAdoc(Object adoc,Object outputFile) throws IOException {		
		PrintWriter wr=getWriter(outputFile);
		if(adoc instanceof String) {
			wr.write((String)adoc); 
		}else { 
			throw new IOException("unkown adoc "+adoc); 
		}
		wr.close();
	}
	
	public PrintWriter getWriter(Object outputFile) throws IOException {
		PrintWriter wr;
		if(outputFile instanceof OutputStream) { 		
			wr = new PrintWriter(new OutputStreamWriter((OutputStream)outputFile, ENCODE_UTF8), true);
		}else if(outputFile instanceof PrintWriter) { 
			wr=(PrintWriter)outputFile; 
		}else if(outputFile instanceof File) { 
			File file=(File)outputFile;
			File parent=file.getParentFile(); if(!parent.exists()) { parent.mkdirs(); }
			wr = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODE_UTF8), true);
		}else if(outputFile instanceof String) {
			String str=(String)outputFile;
			if(str.equals(aAnnoDoc.OUT_STDOUT)) {  
				wr = new PrintWriter(new OutputStreamWriter(System.out, ENCODE_UTF8), true);
			}else { 
//				wr = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(str)), ENCODE_UTF8), true);
				return getWriter(getFile(str));
			}
		}else { throw new IOException("unkown outputFile "+outputFile); }
		return wr;
	}	

	
	public void createHtml(Object adoc,String outputFile) throws IOException {	
		Options options=new Options();
		options.setSafe(SafeMode.UNSAFE);					

		options.setBackend("html");
		options.setToFile(outputFile);

//		Map acciidocAttribtues=new HashMap();
//		acciidocAttribtues.put(ASCIIDOC_ATTR_DOT, "C:/Data/Programme/graphviz/bin/dot.exe");
		if(asciidoctorAttribtues!=null) { options.setAttributes(asciidoctorAttribtues); }
		
		init();
		if(adoc instanceof String) {
			asciidoctor.convert((String)adoc,options);
		}else if(adoc instanceof File) {
			asciidoctor.convertFile((File)adoc,options);
		}else { throw new IOException("unkown adoc "+adoc); }
	}
	
	public void createPdf(Object adoc,String outputFile) throws IOException  {	
				
		// get absult fiel here before modify workDir
		if(adoc instanceof File) { adoc=((File)adoc).getAbsoluteFile(); }
		
		
		File file=getFile(outputFile);
		File parent=file.getParentFile();
		if(!parent.exists()) { parent.mkdirs(); }
		String parentDir=parent.getAbsolutePath();
		oldWorkDir=DocUtils.changeWorkDir(parentDir);		
		outputFile=file.getName();
		LOG.debug("create pdf {} in dir {} (old workDir:{})",outputFile,parentDir,oldWorkDir);
			
//		// copy adoc to new workDir 
//		File newFile=null;
//		if(adoc instanceof File) {
//			File orgFile=(File)adoc;
//			newFile=new File(orgFile.getName());
//			Files.copy(orgFile.toPath(), newFile.toPath());
//			adoc=newFile;
//		}
		
		Options options=new Options();
		options.setSafe(SafeMode.UNSAFE);
		
		if(asciidoctorAttribtues!=null) { options.setAttributes(asciidoctorAttribtues); }
		
//		options.setOption("imagesDir", "../images/");
//		options.setOption("imagesoutdir", "../images/");
//		options.setOption("outputDirectory", "doc");
		options.setBackend("pdf");		
		
//System.out.println("o:"+outputFile);		
		options.setToFile(outputFile);
			
		init();
		if(adoc instanceof String) {
			asciidoctor.convert((String)adoc,options);
		}else if(adoc instanceof File) {
			asciidoctor.convertFile((File)adoc,options);
		}else { throw new IOException("unkown adoc "+adoc); }
		
//		// remove copy orgfile
//		if(newFile!=null && newFile.exists()) { newFile.delete(); }
		// change back to old workDir
		if(oldWorkDir!=null) { DocUtils.replaceWorkDir(oldWorkDir); }		
	}
	
	
	//-------------------------------------------------------------
	
	/** get file and create dirs of parent **/
	public File getFile(String fileName) throws IOException  {
		File file=new File(fileName); 
		if(file==null) { throw new IOException("no file for "+fileName); }
//		if(createParent) {
//			File parent=file.getParentFile();
//			if(parent!=null) { parent.mkdirs(); }  // create parent dirs
//		}
//		return file;
		return new File(file.getAbsolutePath());
	}
	
	//-------------------------------------------------------------------
	
	/** create pdf for adoc **/
	public static void Adoc2Pdf(Object adoc,String pdfOutputFile,Map asciidoctorAttribtues) throws IOException {
		AsciiDocCreator doc=new AsciiDocCreator(asciidoctorAttribtues);
		doc.createPdf(adoc, pdfOutputFile);	
	}

	/** create html for adoc **/
	public static void Adoc2Html(Object adoc,String pdfOutputFile,Map asciidoctorAttribtues) throws IOException {
		AsciiDocCreator doc=new AsciiDocCreator(asciidoctorAttribtues);
		new File(pdfOutputFile).getParentFile().mkdirs(); // create all dirs to doc
		doc.createHtml(adoc, pdfOutputFile);	
	}
	
}
