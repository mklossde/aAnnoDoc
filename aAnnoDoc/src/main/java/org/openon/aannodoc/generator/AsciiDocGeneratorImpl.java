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
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(AsciiDocGeneratorImpl.class);
	
	public static final String ATR_TITLE="title";
	public static final String ATR_DESCRIPTION="desciption";
	
	
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
	
	protected String toFile(String fileName,String format,boolean setFormat) {
		if(fileName.equals(aAnnoDoc.OUT_STDOUT)) { return fileName; }
		
		String prefix=(String)options.get(aAnnoDoc.OPTION_OUTFILE_PREFIX);
		if(prefix==null) { prefix="doc"; }
		int index=fileName.lastIndexOf('.');
		if(index==-1) { fileName=fileName+"."+format; }
		else if(setFormat) { fileName= (fileName.substring(0,index+1))+format; }
		return  prefix+"/"+fileName;
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
		
		String outputFile=outputName;
		if(outputName.equals(aAnnoDoc.DEFAULT_FILE)) { outputFile=getOutput(); } 
		AsciiDocCreator cr=new AsciiDocCreator();
		
		if(options.get(aAnnoDoc.OPTION_OUT_ADOC)!=null) { 
			String file=toFile(outputFile,aAnnoDoc.FORMAT_ASCIIDOC,true);
			LOG.info("write {} format adoc to {}",outputName,file);
			cr.create(adoc, aAnnoDoc.FORMAT_ASCIIDOC, file);
		}
		
		String format=getFormat();
		String file=toFile(outputFile,format,false);
		LOG.info("write {} format {} to {}",outputName,format,file);
		cr.create(adoc, format, file);
	}
	
	/** close generator **/
	@Override public void close(String outputName) {
		w.close(); 
	}
	
	//----------------------------------------------------------------------
	
	/** get doc of DocObject **/
	public String getDoc(DocObject doc,boolean preFormat) {
		String text=doc.getComment();
		// add descibtoon of annotation **/
		if(doc instanceof AnnotationDoc) { text=addString(text, ((AnnotationDoc)doc).getValue(ATR_DESCRIPTION));}
		if(text==null) { return null; }
		text=removeLeadingSpace(text);
		text=removeBackslash(text);
//		if(preFormat) { text=preFormat(text); }
		return text;
	}
	
	//----------------------------------------------------------------------
	
	public String removeLeadingSpace(String text) { return text.replaceAll("\n ", "\n");}
	public String removeBackslash(String text) {  return text.replaceAll("\\\\", "");}
	public String preFormat(String text) { return text.replaceAll("\n", " +\n");}
		
	
	/** add toStrings into one **/
	public String addString(Object... objs) {
		StringBuilder sb=new StringBuilder();
		for (Object object : objs) {
			if(object!=null) { sb.append(toString(object,"")); }
		}
		if(sb.length()==0) { return null; }
		else { return sb.toString(); }
	}
//	public String addString(Object one,Object two) {
//		if(one==null && two==null) { return null; }
//		else if(one!=null && two!=null) { return toString(one)+toString(two); }
//		else if(one==null) { return toString(two); }
//		else { return toString(one); }
//	}
	
	/** get string of obejct **/
	public String toString(Object obj,String def) {
		if(obj==null) { return def; }
		else if(obj instanceof String) { return (String)obj; }
		else { return obj.toString(); }
	}
}
