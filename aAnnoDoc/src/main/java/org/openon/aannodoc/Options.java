package org.openon.aannodoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
	public Options(String soruce,Object outputFile,Object generator,Object format) {
		put(OPTION_SOURCE,soruce);
		put(OPTION_OUTPUT,outputFile);
		put(OPTION_GENERATOR,generator);
		put(OPTION_FORMAT,format);				
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
