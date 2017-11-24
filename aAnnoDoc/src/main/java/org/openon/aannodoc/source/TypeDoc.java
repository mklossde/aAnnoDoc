package org.openon.aannodoc.source;

import java.io.Serializable;

/**
 * 
 * 	type			class Object 	
 * 	typeName:		name of class (e.g. java.lang.String)
 * 	typePackage		name of class package (e.g. java.lang)
 * 	typeSimpleName	simpel name of class(e.g. String)
 * 
 *  modifiers		java modifier
 *  
 * 
 * 
 *
 */
public abstract class TypeDoc extends DocObject implements Serializable {
	private static final long serialVersionUID = 2116349780992234787L;
	
	public String typeName; //
	public String typePackage;
	public String typeSimpleName;	
	
	protected Class type;
	
	public int modifiers;
	
	public TypeDoc(String name,String typeName,DocObject parent,DocObject group) { 
		super(name,parent,group); 
		
		setTypeName(typeName);
	}
	
	public void setTypeName(String typeName) {
		this.typeName=typeName;
		if(typeName!=null) {
			int index=typeName.lastIndexOf('.');
			if(index==-1) { this.typeSimpleName=typeName; typePackage=""; }
			else {this.typeSimpleName=typeName.substring(index+1); typePackage=typeName.substring(0,index); }
		}
	}

	public String getSimpleName() { return typeSimpleName; }
	public String getTypePackage() { return typePackage; }
	public String getTypeName() { return typeName; }
	/** find java-class for type **/
	public Class getType() throws Exception {
		if(type!=null) { return type; } 
		type=Class.forName(typeName);
		return type; 
	} 
	
	public int getModifiers() { return modifiers; }
	 
	public boolean equals(Object obj) {
		if(obj==null) return false;
		else if(obj==this) return true;
		else if(obj instanceof String) {
			if(((String)obj).equals(name) || ((String)obj).equals(typeName) || ((String)obj).equals(typeSimpleName)) return true;
			else return false;
		}else return super.equals(obj);
	}

	public String toJava(int modifer) {
		return ""+modifer;
	}
	
}
