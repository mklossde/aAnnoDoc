package org.openon.aannodoc.source;

import java.io.Serializable;

public class ConstructorDoc extends TypeDoc implements Serializable {
	private static final long serialVersionUID = -7930397263383533668L;

	public ConstructorDoc(String name,String typeName,DocObject parent,ClassDoc clSource) { 
		super(name,typeName,parent,clSource); 
	}
	
	public String toString() { return "Contructur "+getJavaClassName()+"."+name; }
}
