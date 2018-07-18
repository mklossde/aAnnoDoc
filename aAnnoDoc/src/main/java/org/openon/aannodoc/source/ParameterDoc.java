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
	
	public String toString() {
		return className+" "+name;
	}
}

	