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
		for(int i=0;i<size;i++) { 
			params[i]=new ParameterDoc(this,group);
			addChild(params[i]);
		}
	}
	
	public ParameterDoc get(int index) { return params[index]; }
	
	protected void set(int index,List<AnnotationDoc> annos) { 
		params[index].annotations=annos;				
	}
	public List<AnnotationDoc> getAnnotations(int index) { return params[index].annotations; }
	
	protected void set(int index,String className,String simpleClassName,String name) { 
//		params[index]=new ParameterDoc(this, group, name, className, null, null);
		params[index].name=name;params[index].className=className; params[index].simpleClassName=simpleClassName;
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
	
	/** get first param by name **/
	public ParameterDoc getParam(String nameOrClass) {
		for(int i=0;params!=null && i<params.length;i++) {
			ParameterDoc p=params[i];
			if(p.equals(nameOrClass)) { return p; }
		}
		return null;	
	}
	
	//-----------------------------------------------------------
	
	/** get array with empty numer of parmetzer only **/
	public String[] toParamsSize() {
		if(params==null) { return null; }	
		return new String[params.length]; 
	}
	
	/** get array with names **/
	public String[] toParamsName() {
		if(params==null) { return null; }	
		String a[]=new String[params.length]; 
		for(int i=0;i<params.length;i++) { a[i]=params[i].name; }
		return a;
	}
	
	/** get array with types **/
	public String[] toParamsType() {
		if(params==null) { return null; }	
		String a[]=new String[params.length]; 
		for(int i=0;i<params.length;i++) { a[i]=params[i].simpleClassName; }
		return a;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof String[]) {
			String a[]=(String[])obj;
			if(a.length!=params.length) { return false; }
			for(int i=0;i<a.length;i++) {
//System.out.println("a:"+a[i]+"=="+params[i]);				
				if(a[i]!=null && !params[i].equals(a[i])) { return false; } 
			}
			return true;
			
		}else { return super.equals(obj); }
	}
	
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

	