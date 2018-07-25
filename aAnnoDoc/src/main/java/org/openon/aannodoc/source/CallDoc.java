package org.openon.aannodoc.source;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** a method call documentation **/
public class CallDoc extends DocObject {
	private static final long serialVersionUID = 8022254300241381545L;
	private static final Logger LOG=LoggerFactory.getLogger(DocObject.class);
		
//	public String args[];
	protected ParametersDoc parameter;
	protected transient Method method;
	
	public CallDoc(String name,DocObject parent,DocObject group) { 
		super(name, parent, group);
	}
	
	public void setParameter(ParametersDoc parameter) { this.parameter=parameter; }
	public ParametersDoc getParameter() { return parameter; }
	
	/** find first destination method for this call **/
	public MethodDoc getMethod(JarDoc unit) {return unit.findMethod(this);}
	/** find all destination method for this call **/
	public List<MethodDoc> getMethods(JarDoc unit) {return unit.findMethods(this);}
	
	public String[] toParams() {
		if(parameter==null) { return null; } 
		return parameter.toParamsSize();
//		return parameter.toParamsName();
//		return parameter.toParamsType();
	}
	
	public String toString() { return "CallDoc "+name+"("+parameter+")"; }
}
