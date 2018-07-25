package org.openon.aannodoc.source;

import java.util.List;

/**
 * Annotation bases documenation about a method parameter
 * 		
 * 		method(Parameter)
 * 
 */
public class ParameterDoc extends DocObject {

	protected String name;
	protected String className;
	protected String simpleClassName;
	
	protected Class type;
	protected List<AnnotationDoc> paramAnnotations;
	
	public ParameterDoc(DocObject parent,DocObject group) {super("Parameter", parent, group);}
	public ParameterDoc(DocObject parent,DocObject group,String name,String className,Class type,List<AnnotationDoc> paramAnnotations) {
		super("Parameter", parent, group);
		this.name=name; this.className=className; this.type=type; this.paramAnnotations=paramAnnotations;
	}
	
	public String getName() {
		return name;
	}
	
	public String getClassName() {
		return className;
	}
	
	//----------------------------------------------------------------------
	// find class for types
	
	public Class getType() throws Exception { 
		if(type!=null) { return type; }
		type=Class.forName(className);
		return type;
	}
	
	//----------------------------------------------------------------------
	
	public boolean equals(Object obj) {
		if(obj==null) return false;
		else if(obj==this) return true;
		else if(obj instanceof String) {
			String s=(String)obj; 
//System.out.println("s:"+s+" n:"+name+" cn:"+className+" simpleClassName:"+simpleClassName);			
			if(s.equals(name) || s.equals(className) || s.equals(simpleClassName) ) return true;
			else return false;
		}else return super.equals(obj);
	}
	
	public String toString() {
		return className+" "+name;
	}
}

	