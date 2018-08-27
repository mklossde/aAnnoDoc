package org.openon.aannodoc.example;

import java.lang.reflect.Method;

import org.openon.aannodoc.doc.AnnotationDocDefinition;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.MethodDoc;

public class ExampleDoc {
	protected ClassDoc cl;
	protected AnnotationDoc doc;
	
	protected String name;
	public String attribute;
	public String design;
	
	protected boolean executed=false;
	
	/** source of example **/
	protected Object source;
	/** result of example **/
	protected Object result;
	
	public ExampleDoc(AnnotationDoc doc) {
		this.doc=doc;
		this.cl=this.doc.getClassDoc();
		
		this.name=doc.getTitle();
		if(this.name==null) { name=this.cl.getParent().getName(); }
		this.attribute=doc.getResolveString("attribute"); 
		
		this.design=doc.getResolveString("design");
		if(this.design==null) {
			AnnotationDoc aDoc=cl.getAnnotationType(AnnotationDocDefinition.DOC_EXAMPLE);
			if(aDoc!=null) { this.design=(String) aDoc.getResolveString("design"); }
		}	
	}
	
	public ClassDoc cl() { return cl; }
	public AnnotationDoc doc() { return doc; }
	
	public String getName() { return name; }
	public String getDoc() { return doc.getComment(); }
	
	public Object getSource() { return source; }
	public Object getResult() { return result; }
	public boolean isExecuted() { return executed; }
	
	/** find method for this doc **/
	public Method getMethod() throws Exception {
		return ((MethodDoc)doc.getParent()).getMethod();
	}
	
	public String toString() {
		return "Example "+name+" attr:"+attribute+" design:"+design;
	}
	
	//-----------------------------------------------------------------------------------------
	
	/** execute example **/
	public void exec(Object exampleClass) throws Exception {
		executed=true;
		Method method=getMethod();
		ExampleObject example=(ExampleObject)method.invoke(exampleClass, new Object[0]);
		
		this.source=example.request;
		this.result=example.result;
	}
	
}
