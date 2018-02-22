package org.openon.aannodoc.source;

public class DocReference {

	protected JarDoc unit;
	protected String className;
	protected String field;
	
	public DocReference(String className,String field,JarDoc unit) {
//		if(className.indexOf('.')==-1) { 
//			List<String> imports=clSource.getImports();
//			for(int i=0;imports!=null && i<imports)
//			className=pkg+"."+className; // hope that no reference is on root-package 
//		} 
		this.className=className;
		this.field=field;
		this.unit=unit;
	}
	
	public String resolve()  {
		ClassDoc cl=unit.findClass(className);		
//		if(cl==null) { throw new RuntimeException("unkown class reference "+className); }
		if(cl==null) { return toString(); } // can't resolve class return name only
		FieldDoc f=cl.getField(unit,field);
		if(f==null) {  
			throw new RuntimeException("unkown field reference "+field+" in "+className); }
		Object obj=f.getValue();
		if(obj==null || obj instanceof String) { return String.valueOf(obj); }
		throw new RuntimeException("not a string '"+obj+"' at "+field+" in "+className); 
	}
	
	public String toString() {
		return className+"."+field; 
	}
}
