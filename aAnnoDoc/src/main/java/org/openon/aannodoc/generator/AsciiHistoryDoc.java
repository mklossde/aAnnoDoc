package org.openon.aannodoc.generator;

import java.io.IOException;

import org.openon.aannodoc.annotation.aApplication;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.generator.writer.ApplicationWriter;
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
		ApplicationWriter appWr=new ApplicationWriter(w, annotations, options);
		appWr.writeHead();

		// App Doc		
		w.literalBlock(AnnoUtils.getDoc(application)); 
	}
	
	/** document body **/
	public void body(String outputName) throws IOException {
		VersionWriter verWr=new VersionWriter(w, annotations,options);
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
			copyright=application.getResolveString("copyright");
		}
	}


}
