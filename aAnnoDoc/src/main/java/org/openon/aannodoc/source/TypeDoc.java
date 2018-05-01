package org.openon.aannodoc.source;

import java.io.Serializable;
import java.util.Arrays;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.utils.AnnoUtils;
import org.openon.aannodoc.utils.SourceUtils;

/**
 * Description of a Java-Type (Method or field)
 * 
 * 	type			class Object 	
 * 
 * 	typeName:		full type of class including generic (e.g. java.util.List<TestObject2> )
 *  typeClassName:	full class (e.g. java.util.List)
 * 	typePackage		name of class package (e.g. java.util)
 * 	typeSimpleName	simpel name of class(e.g. List)
 *  typeGeneric		generic part (e.g. TestObject2 )
 *  
 *  modifiers		java modifier
 *  
 * 
 * 
 *
 */
@aDoc(title="scanner/TypeDoc")
public abstract class TypeDoc extends DocObject implements Serializable {
	private static final long serialVersionUID = 2116349780992234787L;
	

	/** java-type of field or java-return-type of method (e.g. public List<String> name > "List<String>" ) **/
	public String typeName; //	
	public String typeClassName;
	public String typePackage;
	public String typeSimpleName;	
	/** generic type (e.g. public List<String> name > "String" ) **/
	public String typeGeneric;
	
	
	protected Class type;
	
	public int modifiers;
	
	public TypeDoc(String name,String typeName,DocObject parent,DocObject group) { 
		super(name,parent,group); 
		
		setTypeName(typeName);
	}
	
	public void setTypeName(String typeName) {
		this.typeName=typeName;
		String a[]=SourceUtils.toSimpleName(typeName);	
		this.typePackage=a[0];this.typeSimpleName=a[1]; this.typeGeneric=a[2];
		if(typePackage==null || typePackage.length()==0) { typeClassName=typeSimpleName; }
		else { typeClassName=typePackage+"."+typeSimpleName; }
	}
	
	public String getClassName() { return typeClassName; }
	public String getSimpleName() { return typeSimpleName; }
	public String getTypePackage() { return typePackage; }
	public String getGeneric() { return typeGeneric; }
	
	public String getTypeName() { return typeName; }
	/** find java-class for type **/
	public Class getType() throws Exception {
		if(type!=null) { return type; } 
		type=Class.forName(typeName);
		return type; 
	} 

	/** find doc for type **/
	public TypeDoc getTypeClass() { return (TypeDoc)findClass(typeClassName); }
	
	public int getModifiers() { return modifiers; }
	 
	public boolean equals(Object obj) {
		if(obj==null) return false;
		else if(obj==this) return true;
		else if(obj instanceof String) {
			String s=(String)obj; 
			if(s.equals(name) || s.equals(typeName) || s.equals(typeSimpleName)) return true;
			else return false;
		}else return super.equals(obj);
	}

	public String toJava(int modifer) {
		return ""+modifer;
	}
	
}
