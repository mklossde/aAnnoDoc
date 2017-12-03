package org.openon.aannodoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aDoc;
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
	public static final String OPTION_GENERATOR="generator";
	public static final String OPTION_FORMAT="format";
	public static final String OPTION_OUTPUT="output";
	
	public static final String OPTION_OUT_ADOC="outadoc";
	public static final String OPTION_OUTFILE_PREFIX="outprefix";
	/** write only docFile - contains file="xxx" in annotation-attribute **/
	public static final String OPTION_DOCFILE="docfile";
	
	/** option map **/
	protected Map<String,Object> options=new HashMap<String, Object>();
	
	/** actual resolved fitler **/
	protected DocFilter filter;
	
	/** instance new option obejct **/
	public Options() {}
	
	/** read options from file **/
	public Options(String propertieFile) throws IOException {
		readProperties(propertieFile);
	}
	
	/** read options from propertie stream **/
	public Options(InputStream propertieStream) throws IOException {
		readProperties(propertieStream);
	}
	
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
			else if(OPTION_SOURCE.equals(key)) { put(OPTION_SOURCE,args[++i]);} // -source SOURCE
			else if(OPTION_OUTPUT.equals(key)) { put(OPTION_OUTPUT,args[++i]);}
			else if(OPTION_GENERATOR.equals(key)) { put(OPTION_GENERATOR,args[++i]);}
			else if(OPTION_FORMAT.equals(key)) { put(OPTION_FORMAT,args[++i]);}
			else { throw new IOException("unkown arg '"+key+"'"); }
		}
	}
		
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
	
	public void put(String key,Object value) { options.put(key, value); }
	public Object get(String key) { return options.get(key); }
	public Object remove(String key) { return options.remove(key); }
	
	//-------------------------------------------------------------------------------------------------
	
	public DocFilter getFilter() throws IOException {
		if(this.filter!=null) { return filter; }
		this.filter=(DocFilter)ReflectUtil.getInstance(DocFilter.class,options.get("filter"));
		if(filter!=null && !filter.isInit()) { filter.init(this); }
		return filter;
	}
	

}
