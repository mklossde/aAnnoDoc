package org.openon.aannodoc.generator;

import java.io.IOException;

import org.openon.aannodoc.annotation.aApplication;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.generator.writer.VersionWriter;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;

/**
 * Generator for History-Documenation via AsciiDocumentation
 * 
 * @author Michael Kloss
 *
 */
@aDoc(title="generator/AsciiHistoryDoc")
public class AsciiHistoryDoc extends AsciiDocGeneratorImpl implements DocGenerator {
	
	public static final String NAME="HistoryDoc";
	
	public static String TITLE="History Documentation";
	
	//-----------------------------------------------------------------------------
	
	/** aAplication doc **/
	protected AnnotationDoc application;
	protected String title=this.TITLE,author=null,version=null,date=null,depcrecated=null,copyright=null;
	
//	protected List<AnnotationDoc> versions,versionList;
	
	//-----------------------------------------------------------------------------
	
	public AsciiHistoryDoc() { super(); }
	
	/** document head **/
	public void head(String outputName) throws IOException {	
		init();		
		// Doc Title
		w.title(title+" "+TITLE,author,null,version);		
		String genLabel=(String)options.get("genlabel"); if(genLabel==null) { genLabel="aAnnoDoc created on"; }
		w.nl().w(":last-update-label: "+genLabel).nl();	
		if(depcrecated!=null) { w.warning(depcrecated); }		
		// App Doc		
		w.literalBlock(AnnoUtils.getDoc(application)); 
		// Copyright
		w.paragraph(copyright);
	}
	
	/** document body **/
	public void body(String outputName) throws IOException {
		VersionWriter verWr=new VersionWriter(w, annotations);
		verWr.wrtieVersionTable(options);
		verWr.wrtieVersions(options);
	}
	
	/** document bottom **/
	public void bottom(String outputName) throws IOException {		
		w.close();
	}
	
	//--------------------------------------------------------------------------------------
	
	public void init() throws IOException {		
		application=annotations.getAnnotation(aApplication.class);
		if(application!=null) {			
			title=AnnoUtils.getTitle(application,true);
			author=AnnoUtils.getAuthor(application, 1);version=AnnoUtils.getVersion(application, 1);
			date=AnnoUtils.getDate(application, 1); depcrecated=AnnoUtils.getDeprecated(application, 1);
			copyright=application.getValueString("copyright");
		}
	}


}
