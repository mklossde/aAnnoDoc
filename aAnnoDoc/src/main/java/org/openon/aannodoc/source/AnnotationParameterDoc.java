package org.openon.aannodoc.source;

import java.io.Serializable;

public class AnnotationParameterDoc extends FieldDoc implements Serializable {
	private static final long serialVersionUID = 1294074346777424561L;

//	public Object value;
	
	public AnnotationParameterDoc(String name,String className,DocObject parent,ClassDoc clSource,Object value) { 
		super(name, className, parent, clSource, value);
	}
	
//	public Object getValue() { return value; }
	
	public String toJava() { return toJava(modifiers)+" "+name+" = "+value; }
	public String toString() { return "AnnotationParam "+getJavaClassName()+"."+name; }
}
