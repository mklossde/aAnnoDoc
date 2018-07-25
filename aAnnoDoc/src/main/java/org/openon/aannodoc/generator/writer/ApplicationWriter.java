package org.openon.aannodoc.generator.writer;

import java.io.IOException;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.annotation.aApplication;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;

public class ApplicationWriter {

	public static final String GENERATOR_INFO="aAnnoDoc created on";
	
	protected SourceAnnotations annotations;	
	protected AsciiDocWriter w;	
	protected Options options;
	
	protected AnnotationDoc application;
	protected String title=null,author=null,version=null,date=null,depcrecated=null,copyright=null;
	
	public ApplicationWriter(AsciiDocWriter w,SourceAnnotations adoc,Options options) throws IOException {
		this.w=w;  this.annotations=adoc; this.options=options; 				
	}
	
	//----------------------------------------------------------------------------------------
	
	/** write head of document **/
	public void writeHead() throws IOException {	
		
		String version=version(), date=date(), dateVersion;
		if(!w.e(version) && !w.e(date)) { dateVersion=version+" ("+date+")"; }
		else if(!w.e(version)) { dateVersion=version; } 
		else { dateVersion=date; } 
		
		w.title(title(),author(),email(),dateVersion); // ,doc.getAnnotation("author"),doc.getAnnotation("date"));
		
		String genLabel=(String)options.get("genlabel"); if(genLabel==null) { genLabel=GENERATOR_INFO; }
		w.nl().w(":last-update-label: "+genLabel).nl();	
		// deprecated
		if(depcrecated!=null) { w.warning(depcrecated); }
		// copyright
		w.paragraph(copyright);
	}
	
	//----------------------------------------------------------------------------------------
	
	/** get application **/
	public AnnotationDoc application() throws IOException  {		
		if(application==null) {
			application=this.annotations.getAnnotation(aApplication.class);
		}
		return application;
	}
	
	public String title() throws IOException {
		if(title==null) { title=(String)options.get(Options.OPTION_APP_TITLE); }
		if(title==null) { title=AnnoUtils.getTitle(application(),true); }
		if(title==null) { title="Document"; } // default Title
		return title;
	}
	
	public String author() throws IOException {
		if(author==null) { author=(String)options.get(Options.OPTION_APP_AUTHOR); }
		if(author==null) { author=AnnoUtils.getAuthor(application(), 1);}
		return author;
	}		
	
	public String email() throws IOException {
		return null;		
	}
	
	public String version() throws IOException {
		if(version==null) { version=(String)options.get(Options.OPTION_APP_VERSION); }
		if(version==null) { version=AnnoUtils.getVersion(application(), 1);}
		return version;
	}
	
	public String date() throws IOException {
		if(date==null) { date=(String)options.get(Options.OPTION_APP_DATE); }
		if(date==null) { date=AnnoUtils.getDate(application(), 1); }
		return date;
	}
	
	public String depcrecated() throws IOException {
		if(date==null) { date=(String)options.get(Options.OPTION_APP_DEPRECATED); }
		if(depcrecated==null) { depcrecated=AnnoUtils.getDeprecated(application(), 1);}
		return depcrecated;
	}
	
	public String copyright() throws IOException{
		if(copyright==null) { copyright=(String)options.get(Options.OPTION_APP_COPYRIGHT); }
		if(copyright==null) {
//			copyright=application.getValueString("copyright");
			copyright=AnnoUtils.get(application(), "copyright");			
		}
		return copyright;
	}
	
	
}
