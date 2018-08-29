package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.List;

import org.openon.aannodoc.annotation.aApplication;
import org.openon.aannodoc.generator.writer.AnnotationWriter;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;

public class GenGeneratorDoc extends AsciiDocGeneratorImpl implements DocGenerator {

	public static final String NAME="GeneratorDoc";
	public static String TITLE="Generator Documentation";
	
	protected AnnotationDoc application;
	protected String title=this.TITLE,author=null,version=null,date=null,depcrecated=null,copyright=null;
	
	public GenGeneratorDoc() { super(); }

	@Override public void head(String outputName) throws IOException {
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

	@Override public void body(String outputName) throws IOException {
		AnnotationWriter awr=new AnnotationWriter(w, this.annotations);
		List<AnnotationDoc> annos=awr.findAAnnoation();
		awr.tableAnnoations(annos);
		awr.infoAnnoations(annos);
		awr.close();
	}

	@Override public void bottom(String outputName) throws IOException {
		w.close();
	}
	
	//-------------------------------------------------------------------------------
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
