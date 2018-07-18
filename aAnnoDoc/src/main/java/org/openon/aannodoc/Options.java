package org.openon.aannodoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.utils.DocFilter;
import org.openon.aannodoc.utils.ReflectUtil;

/**
 * Options of AnnoDoc
 *
 * source: source file or directory
 * generator: generator name/class/object
 * format: output format
 * output: output-file
 * 
 * outadoc: create adoc output,too
 * outoprefix: give all output file a prefix (or directory)
 * docfile: write only docFile - contains file="xxx" in annotation-attribute
 * 
 *  
 */
@aDoc(title="Options/aAnnoDoc Options")

/**
 * Option-Object - contains and handle all opttions
 * 
 * @author Michael Kloß
 *
 */
public class Options {
	public static final String OPTIONFILE="options";
	
	@aAttribute()
	public static final String OPTION_SOURCE="soruce";
	
	/** add option to set charset of source (to scann source/comments with this charset) **/
	@aFeature(date="16.07.2018",title="soruceChartset")
	/** define source code charset (e.g. UTF-8/windows-1252 )**/	
	public static final String OPTION_SOURCE_CHARTSET="soruceChartset";
//TODO: inspect maven (pom.xml) for charset	
	public static final String CHARTSET_UTF8="UTF-8";
	public static final String CHARTSET_WINDOWS="windows-1252"; 
	
	public static final String OPTION_GENERATOR="generator";
	public static final String OPTION_FORMAT="format";
	public static final String OPTION_OUTPUT="output";
	
	/** output source for output-format to "outsource" file (e.g. asciidoc) **/
	public static final String OPTION_OUTSOURCE="outsource";
	
	public static final String OPTION_AD_GRAPHVIZ="dot"; // asciiDoc Attribute DOC/GRAPHVIZ
	
//	/** write only docFile - contains file="xxx" in annotation-attribute **/
//	public static final String OPTION_DOCFILE="docfile";
	/** define generator output-name (e.g. group) **/
	public static final String OPTION_OUTPUTNAME="outputname";
	
	public DateFormat df=new SimpleDateFormat("dd.MM.YYYY");
	
	/** option map **/
	protected Map<String,Object> options=new HashMap<String, Object>();
	/** asciidoctor attribtues **/
	protected Map asciidoctorAttribtues=new HashMap();
	/** value attribtues **/
	protected Map attributes=new HashMap();
	
	/** actual resolved fitler **/
	protected DocFilter filter;
	
	//-----------------------------------------------------------------
	
	/** instance new option obejct **/
	public Options() {}
	
	public Map getAsciidoctorAttribtues() { return asciidoctorAttribtues; }
	
	/** read options from file **/
	public Options(String propertieFile) throws IOException {
		readProperties(propertieFile);
	}
	
	/** read options from propertie stream **/
	public Options(InputStream propertieStream) throws IOException {
		readProperties(propertieStream);
	}
	
	/** set aciidoctor attribtue GRAPHVIZ/DOT **/
	public Options setGraphviz(String dotExecutablePath) { asciidoctorAttribtues.put(OPTION_AD_GRAPHVIZ,dotExecutablePath); return this; }
	
	public Options(String soruce,Object outputFile,Object generator,Object format) {
		put(OPTION_SOURCE,soruce);
		put(OPTION_OUTPUT,outputFile);
		put(OPTION_GENERATOR,generator);
		put(OPTION_FORMAT,format);				
	}
	
	/** create option by java main-args[] **/ 
	public Options(String args[]) throws IOException {
		for(int i=0;args!=null && i<args.length;i++) {
			String key=args[i]; if(key.startsWith("-")) {key=key.substring(1);} // simple take -file==file
			if(OPTIONFILE.equals(key)) { readProperties(args[++i]); } // -file PROPERITEFILE			
//			else if(OPTION_SOURCE.equals(key)) { put(OPTION_SOURCE,args[++i]);} // -source SOURCE
//			else if(OPTION_OUTPUT.equals(key)) { put(OPTION_OUTPUT,args[++i]);}
//			else if(OPTION_GENERATOR.equals(key)) { put(OPTION_GENERATOR,args[++i]);}
//			else if(OPTION_FORMAT.equals(key)) { put(OPTION_FORMAT,args[++i]);}
//			else if(OPTION_OUT_DIR.equals(key)) { put(OPTION_OUT_DIR,args[++i]);}			
//			else { throw new IOException("unkown arg '"+key+"'"); }
			else { put(key,args[++i]); }
		}
	}
	
	//---------------------------------------------------------------------------------------------
	public Object getAttribute(String key) {
		Object val=attributes.get(key);
		if(val!=null) { return val; }
		if(key.equalsIgnoreCase("NOW")) { return toDate(System.currentTimeMillis()); }
		else { return null; }
	}
	public void setAttribute(String key,Object value) { attributes.put(key, value); }		

	
	public String toDate(long time) {return df.format(time);}
	
	//---------------------------------------------------------------------------------------------
	/** read options from propertie file **/
	protected void readProperties(String propertieFile) throws IOException {
		File file=new File(propertieFile);
		if(!file.exists()) { throw new IOException("unkown option-file '"+propertieFile+"'"); }
		readProperties(new FileInputStream(propertieFile));
	}
	/** read options from propertie stream **/
	protected void readProperties(InputStream propertieStream) throws IOException {
		Properties prop=new Properties();
		prop.load(propertieStream);
		this.options.putAll((Map)prop);
		propertieStream.close();
	}
	
	//-------------------------------------------------------------------------------------------------
	
	/** set option **/
	public void put(String key,Object value) { options.put(key, value); }
	/** get option **/
	public Object get(String key) { return options.get(key); }
	/** get option as string **/
	public String getString(String key) { return Objects.toString(options.get(key),null); }
	/** remove key **/
	public Object remove(String key) { return options.remove(key); }	
	/** do optiin contaisn key **/
	public boolean have(String key) { return options.containsKey(key); }
	/** put if optin is not set **/
	public void putIfEmpty(String key,Object value) { if(!have(key)) { put(key, value); }}
	
	//-------------------------------------------------------------------------------------------------
	
	public void setFilter(DocFilter filter) throws IOException { this.filter=filter; }
	public DocFilter getFilter() throws IOException {
		if(this.filter!=null) { return filter; }
		this.filter=(DocFilter)ReflectUtil.getInstance(DocFilter.class,options.get("filter"));
		if(filter!=null && !filter.isInit()) { filter.init(this); }
		return filter;
	}
	
	/** get source-code charset option **/
	public String getSourceCharset() { return getString(OPTION_SOURCE_CHARTSET); }
	/** set source-code charset option **/
	public Options setSourceCharset(String charset) { put(OPTION_SOURCE_CHARTSET, charset); return this; }
	
}
