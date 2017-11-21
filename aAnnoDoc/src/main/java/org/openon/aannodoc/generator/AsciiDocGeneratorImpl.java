package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.Map;

import org.openon.aannodoc.AnnoDoc;
import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(AsciiDocGeneratorImpl.class);
	
	protected JarDoc doc;
	protected SourceAnnotations adoc;
	protected Map<String,Object> options;
	
	protected AsciiDocWriter w;
	
	public AsciiDocGeneratorImpl() {
		this.w=new AsciiDocWriter();
	}
	
	@Override public void init(SourceAnnotations adoc,Map<String,Object> options) {
		this.adoc=adoc; this.doc=adoc.doc();		
		this.options=options;
	}
	
	/** write created to output **/
	@Override public void output() throws IOException {		
		String adoc=w.toString();	
//System.out.println("a:"+adoc);
		LOG.trace("document adoc: {}",adoc);
		String outputFile=getOutput();
		LOG.info("write output to {}",outputFile);
		
		AsciiDocCreator cr=new AsciiDocCreator();
		cr.create(adoc, getFormat(), outputFile);
	}
	
	/** close generator **/
	@Override public void close() {
		w.close(); w=null;
		doc=null;
		adoc=null;
	}
	
	//-----------------------------------------------------------------------------
	// Options
	
	/** 
	 * option: annotations => list all annotations used for document
	 *  string=> a semicole based list of annotations
	 *  empty/default=> list all annotations
	 *  
	 *  @return option-annotation-list
	 **/
	@aAttribute(name="options/annotations")
	public Object[] getAnnotations() throws IOException {
		Object obj=options.get("annotations");
		if(obj==null) { obj=adoc.listAnnotions(false); }
		return ReflectUtil.toArray(obj);
	}
	
	/**
	 * option: format of output 
	 * 
	 * 		adoc=> AsciiDoc
	 * 		html=> html (AsciiDoc)
	 * 		pdf => pdf (AsciiDoc)
	 * 
	 * 
	 * @return option-format
	 */
	@aAttribute(name="options/format")
	public String getFormat() {
		Object obj=options.get("format"); if(obj==null) { obj="html"; }
		return (String)obj;
	}
	
	/**
	 * option: output-file
	 * 
	 * @return option-output-file
	 */
	@aAttribute(name="options/output")
	public String getOutput() throws IOException {
		String obj=(String)options.get("output"); 
		if(obj==null) { obj="AnnoDocOutput"; }
		if(obj.equals(AnnoDoc.OUT_STDOUT)) {}
		else if(obj.indexOf('.')==-1) { obj=obj+"."+getFormat(); }
		return (String)obj;
	}
	
	//--------------------------------------------------------------------
	
	/** create document and structure **/
	@Override public void create() throws IOException {
		LOG.info("crreate document");
		head();
		body();
		bottom();		
	}
	
	public abstract void head() throws IOException;
	public abstract void body() throws IOException;
	public abstract void bottom() throws IOException;
	
	
}
