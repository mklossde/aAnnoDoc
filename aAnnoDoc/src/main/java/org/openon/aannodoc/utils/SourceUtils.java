package org.openon.aannodoc.utils;

public class SourceUtils {


	
	/** 
	 * split java.util.List org.openon.annodoc.test.scanner.TestObject2 into {java.util,List,<org.openon.annodoc.test.scanner.TestObject2} 
	 * 
	 **/
	public static String[] toSimpleName(String name) {
		if(name==null || name.length()==0) { return new String[]{null,"",null}; }
		String pkg=null,nam=null,gen=null;
		int genIndex=name.indexOf('<'),genEnd=name.lastIndexOf('>');	
		
		if(genIndex!=-1 && genEnd>genIndex) {			
			gen=name.substring(genIndex+1,genEnd).trim(); name=name.substring(0,genIndex); 
		}
		int index=name.lastIndexOf('.');
		if(index==-1) { nam=name.trim(); }
		else { pkg=name.substring(0,index).trim(); nam=name.substring(index+1).trim();	}		
		return new String[]{pkg,nam,gen};
	}
	
}
