package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openon.aannodoc.aAnnoDoc;
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
	
	public static final String DEFAULT_FILE="default";
	
	protected JarDoc doc;
	protected SourceAnnotations adoc;
	protected Map<String,Object> options;
	
	protected AsciiDocWriter w;
	
	public AsciiDocGeneratorImpl() {		
	}
	
	@Override public void init(SourceAnnotations adoc,Map<String,Object> options) {
		this.adoc=adoc; this.doc=adoc.doc();		
		this.options=options;
	}
	
	@Override public void generate() throws IOException {
		String outputName=(String)options.get(aAnnoDoc.OPTION_DOCFILE);
		if(outputName!=null) { 
			generate(outputName); 
		}else {
			List<String> outputs=listOutputs();		
			for(int i=0;i<outputs.size();i++){ 	
				outputName=outputs.get(i);
				generate(outputName);
			}
		}
	}
	
	public void generate(String outputName) throws IOException {	
		w=new AsciiDocWriter();
		create(outputName);
		output(outputName);
		close(outputName);
	}
	
	/** get list of oututFiles **/
	public List<String> listOutputs() throws IOException {
		List<String> list=new ArrayList<String>();		
		list.add(getOutput());		
		return list;
	}
		
	//-----------------------------------------------------------------------------
	// Options
	
	/** 
	 * option: annotations = list all annotations used for document
	 *  string= a semicole based list of annotations
	 *  empty/default= list all annotations
	 *  
	 *  @return option-annotation-list
	 **/
	@aAttribute(title="options/annotations")
	public Object[] getAnnotations() throws IOException {
		Object obj=options.get("annotations");
		if(obj==null) { obj=adoc.listAnnotions(false); }
		return ReflectUtil.toArray(obj);
	}
	
	/**
	 * option: format of output 
	 * 
	 * 		adoc= AsciiDoc
	 * 		html= html (AsciiDoc)
	 * 		pdf = pdf (AsciiDoc)
	 * 
	 * 
	 * @return option-format
	 */
	@aAttribute(title="options/format")
	public String getFormat() {
		Object obj=options.get("format"); if(obj==null) { obj="html"; }
		return (String)obj;
	}
	
	/**
	 * option: output-file
	 * 
	 * @return option-output-file
	 */
	@aAttribute(title="options/output")
	public String getOutput() throws IOException {
		String obj=(String)options.get("output"); 
		if(obj==null) { obj="AnnoDocOutput"; }
		return obj;
	}
	
	//--------------------------------------------------------------------
	
	protected String toFile(String fileName) {
		if(fileName.equals(aAnnoDoc.OUT_STDOUT)) { return fileName; }
		
		String prefix=(String)options.get(aAnnoDoc.OPTION_OUTFILE_PREFIX);
		if(prefix==null) { prefix="doc"; }
		
		else if(fileName.indexOf('.')==-1) { fileName=fileName+"."+getFormat(); }
		return prefix+"/"+fileName;
	}
	
	//--------------------------------------------------------------------
	
	/** create document and structure **/
	@Override public void create(String outputName) throws IOException {
		LOG.info("crreate document {}",outputName);
		head(outputName);
		body(outputName);
		bottom(outputName);		
	}
	
	public abstract void head(String outputName) throws IOException;
	public abstract void body(String outputName) throws IOException;
	public abstract void bottom(String outputName) throws IOException;
	
	//----------------------------------------------------------------------
	
	/** write created to output **/
	@Override public void output(String outputName) throws IOException {		
		String adoc=w.toString();	
		LOG.trace("document {} adoc: {}",outputName,adoc);	
		
		if(outputName.equals(DEFAULT_FILE)) { outputName=getOutput(); } 
		AsciiDocCreator cr=new AsciiDocCreator();
		
		String file=toFile(outputName);
		LOG.info("write {} output to {}",outputName,file);
		cr.create(adoc, getFormat(), file);
	}
	
	/** close generator **/
	@Override public void close(String outputName) {
		w.close(); 
	}
	
}
