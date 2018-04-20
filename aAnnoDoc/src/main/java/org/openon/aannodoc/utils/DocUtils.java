package org.openon.aannodoc.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocUtils {
	private static final Logger LOG=LoggerFactory.getLogger(DocUtils.class);
	
	
	//------------------------------------------------------------------------------------------
	
	
	/** switch workDir to outputDir **/
	public static String changeWorkDir(String outDir) {
		if(outDir!=null && outDir.length()>0) {
			String workDir=System.getProperty("user.dir");
//			if(outDir.startsWith("/") || outDir.startsWith("\\")) { outDir=outDir.substring(1); }
//			 do not use ending with .xx
//			int i=outDir.lastIndexOf("/"); int t=outDir.lastIndexOf('.'); if(i>0 && t>0 && t>i) { outDir=outDir.substring(0,i-1); }
			String newWorkDir=new File(outDir).getAbsolutePath();
			LOG.info("change to ouputDir {} =>  {}",workDir,newWorkDir);
			System.setProperty("user.dir",newWorkDir);
			return workDir;
		}
		return null;
	}

	public static void replaceWorkDir(String orgWorkDir) {
		if(orgWorkDir!=null && orgWorkDir.length()>0) { System.setProperty("user.dir",orgWorkDir); }
	}
}
