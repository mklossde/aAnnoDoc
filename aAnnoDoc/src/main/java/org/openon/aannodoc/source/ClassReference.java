package org.openon.aannodoc.source;

public class ClassReference implements DocReference {

	protected JarDoc unit;
	protected String className;
	protected String field;
	/** resolved reference **/
	protected String resolved=null;
	
	public ClassReference(String className,String field,JarDoc unit) {
		this.className=className;
		this.field=field;
		this.unit=unit;
	}
	
	@Override public Object resolve()  {
		if(resolved!=null) { return resolved; }
		ClassDoc cl=unit.findClass(className);		
//		if(cl==null) { throw new RuntimeException("unkown class reference "+className); }
		if(cl==null) { return toString(); } // can't resolve class return name only
		FieldDoc f=cl.getField(unit,field);
		if(f==null) {  
			throw new RuntimeException("unkown field reference "+field+" in "+className); }
		Object obj=f.getValue();
		if(obj==null || obj instanceof String) { resolved=String.valueOf(obj); return resolved; }
		throw new RuntimeException("not a string '"+obj+"' at "+field+" in "+className); 
	}
	
	public String toString() {
		return className+"."+field; 
	}
}
