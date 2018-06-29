package org.openon.aannodoc.utils;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.source.DocObject;

/**
 * Source and Gernator filter 
 * 
 * This filter controls the scan and generation process by include/exclude files/parts
 * This is usable to scan the complete source and create different document by filter each generation-process
 * 
 * 
 * 
 * @author Michael.Kloss - mk@almi.de
 *
 */
@aFeature(title="Source and generator Filter")
public class DocFilter {

	protected Options options;
	
	public DocFilter() {}
	
	/** is this filter alrady init **/
	public boolean isInit() { return this.options!=null; }
	/** init this filter **/
	public void init(Options options) { this.options=options; }
	
	//-------------------------------------------------------------------------------
	// Scan
	
	public boolean scanDir(String javaSourceDir) {
		return true; 
	}
	
	/** 
	 * called before a class is scanned 
	 * 
	 * @param javaSourceName
	 * @return false=ignore class for scanning
	 */
	public boolean scanClass(String javaSourceName) {
		return true; 
	}
	
	//-------------------------------------------------------------------------------
	// Generate
	
	/**
	 * called before generation of outputName
	 * 
	 * @param outputName
	 * @return - false=skip this outputName
	 */
	public boolean generateOutput(String outputName) { 
		return true;
	}
	
	
	/**
	 * called before DocObject is included
	 * 
	 * @param title - tiele of doc-obejct
	 * @param doc - scanned doc-obejct
	 * @return false - do not include this
	 */
	public boolean generateTitle(String title,DocObject doc) { 
		return true;
	}
	
}
