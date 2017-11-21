package org.openon.aannodoc.source;

import java.io.Serializable;
import java.lang.reflect.Method;

public class MethodDoc extends TypeDoc implements Serializable{
	private static final long serialVersionUID = -4482749601451320369L;

	protected ParameterDoc parameter;
	protected transient Method method;
	
	public MethodDoc(String name,String typeName,ParameterDoc parameter,DocObject parent,ClassDoc clSource)  { 
		super(name,typeName,parent,clSource); 
		this.parameter=parameter;
	}
	
	public Method getMethod() throws Exception  {
		if(this.method!=null) { return this.method; }
		ClassDoc clDoc=getClassDoc();		
		Class cl=clDoc.getType();
		Class types[]=null; if(parameter!=null) { types=parameter.getTypes(); }
//		try {
			this.method=cl.getMethod(name, types);
//		}catch(NoSuchMethodException e) { throw new IOException("parameter mismatch "+e); }
		return method;
	}
	
	public String toJava() { return toJava(modifiers)+" "+name+"("+(parameter.toJava())+")"; }
	public String toString() { return "Method "+name; }
}
