package org.openon.aannodoc.source;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Annoation documenation of a Java-Method
 *
 */
public class MethodDoc extends TypeDoc implements Serializable{
	private static final long serialVersionUID = -4482749601451320369L;

	protected ParametersDoc parameter;
	protected transient Method method;
	
	public MethodDoc(String name,String typeName,DocObject parent,ClassDoc clSource)  { 
		super(name,typeName,parent,clSource); 
		
	}
	
	public void setParameter(ParametersDoc parameter) { this.parameter=parameter; }
	
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
	
	/** get method parameter **/
	public ParametersDoc getParameter() { return parameter; }
	
	public String toJava() { return toJava(modifiers)+" "+name+"("+(parameter.toJava())+")"; }
	public String toString() { return "Method "+name; }
}
