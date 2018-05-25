package org.openon.aannodoc.source;

import java.util.Arrays;
import java.util.List;

/**
 * Annotation bases documenation about a parameter-set
 * 
 */
public class ParametersDoc extends DocObject {

//	protected String[] names;
//	protected String[] classNames;
//	protected Class[] types;
//	protected List<AnnotationDoc>[] paramAnnotations;
	protected ParameterDoc params[];
	
	public ParametersDoc(DocObject parent,DocObject group,int size) {
		super("Parameters", parent, group);
		params=new ParameterDoc[size];	
		for(int i=0;i<size;i++) { params[i]=new ParameterDoc(this,group); }
	}
	
	public ParameterDoc get(int index) { return params[index]; }
	
	protected void set(int index,List<AnnotationDoc> annos) { params[index].annotations=annos; }
	public List<AnnotationDoc> getAnnotations(int index) { return params[index].annotations; }
	
	protected void set(int index,String className,String name) { 
//		params[index]=new ParameterDoc(this, group, name, className, null, null);
		params[index].name=name;params[index].className=className;  
	} 
	
	/** number of parameter **/
	public int size() { 
		if(params==null) { return 0; }
		else { return params.length; } 
	}


	
	public String getName(int index) {
		return params[index].name;
	}

	public String[] getNames() {
		String names[]=new String[params.length];
		for(int i=0;i<params.length;i++) { names[i]=getName(i); }		
		return names; 
	}
	
	public String getClassName(int index) {
		return params[index].className;
	}
	
//	public String[] getClassNames() { return classNames;}
	
	//----------------------------------------------------------------------
	// find class for types
	
	public Class getType(int index) throws Exception {
		return params[index].getType();
	}
	
	public Class[] getTypes() throws Exception {
		Class cl[]=new Class[params.length];
		for(int i=0;i<cl.length;i++) { cl[i]=params[i].getType(); }
		return cl;
	}
	
//	public Class[] getTypes() throws Exception { 
//		if(types!=null) { return types; }
//		types=new Class[classNames.length];
//		for(int i=0;i<types.length;i++) {
//			types[i]=Class.forName(classNames[i]);
//		}
//		return types;
//	}
	
	//----------------------------------------------------------------------
	
	public String toJava() { 
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<size();i++) {
			sb.append(params[i].type+" "+getName(i));
		}
		return sb.toString();
	}
	public String toString() { return "Paramerts "+Arrays.toString(params); }
}

	