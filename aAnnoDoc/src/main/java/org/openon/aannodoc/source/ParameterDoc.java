package org.openon.aannodoc.source;

import java.util.Arrays;

public class ParameterDoc {

	protected String[] names;
	protected String[] classNames;
	protected Class[] types;
	
	public ParameterDoc(int size) {		
		names=new String[size];
		classNames=new String[size];
	}
	
	protected void set(int index,String className,String name) { 
		classNames[index]=className; 
		names[index]=name;
	} 
	
	/** number of parameter **/
	public int size() { 
		if(types==null) { return 0; }
		else { return types.length; } 
	}


	
	public String getName(int index) {
		return names[index];
	}

	public String[] getNames() { return names; }
	
	public String getClassName(int index) {
		return classNames[index];
	}
	
	public String[] getClassNames() { return classNames;}
	
	//----------------------------------------------------------------------
	// find class for types
	
	public Class getType(int index) throws Exception {
		return getTypes()[index];
	}
	
	public Class[] getTypes() throws Exception { 
		if(types!=null) { return types; }
		types=new Class[classNames.length];
		for(int i=0;i<types.length;i++) {
			types[i]=Class.forName(classNames[i]);
		}
		return types;
	}
	
	//----------------------------------------------------------------------
	
	public String toJava() { 
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<names.length;i++) {
			sb.append(types[i]+" "+names[i]);
		}
		return sb.toString();
	}
	public String toString() { return "Paramerts "+Arrays.toString(names); }
}

	