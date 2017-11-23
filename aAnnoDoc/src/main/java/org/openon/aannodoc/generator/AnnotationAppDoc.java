package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aConnection;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aExample;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aTest;
import org.openon.aannodoc.annotation.aField;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator for AnnoDoc-Annotation documents (aDoc,aAttribtue,aBug,..) 
 * 
 * @author michael
 *
 */
@aDoc(title="generator/AnnoDocGenerator")
public class AnnotationAppDoc extends AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(AnnotationAppDoc.class);
	

	
	public static final String NAME="AppDoc";
	
	public static final String ANNOTATIONS="annotations";
	
	
	public AnnotationAppDoc() { super(); }
	
	
	//-----------------------------------------------------------------------------
	// Options
		
	/**
	 * option: format of output 
	 * 
	 * 		adoc= AsciiDoc
	 * 		html= html (AsciiDoc)
	 * 		pdf = pdf (AsciiDoc)
	 * 
	 * 
	 * @return option-format
	 */
	@aAttribute(title="options/format")
	public String getFormat() {
		Object obj=options.get(aAnnoDoc.OPTION_FORMAT); if(obj==null) { obj="html"; }
		return (String)obj;
	}
	
	/**
	 * option: output-file
	 * 
	 * @return option-output-file
	 */
	@aAttribute(title="options/output")
	public String getOutput() throws IOException {
		String obj=(String)options.get(aAnnoDoc.OPTION_OUTPUT); 
		if(obj==null) { obj="AnnoDocOutput"; }
		if(obj.indexOf('.')==-1) { obj=obj+"."+getFormat(); }
		return (String)obj;
	}

	//---------------------------------------------
	
	public List<String> listOutputs() throws IOException {
		List<String> list=new ArrayList<String>();		

		boolean defaultAdded=false;
		List<AnnotationDoc> l=adoc.findAnnotation(aDoc.class);
		for(int i=0;i<l.size();i++) {
			AnnotationDoc doc=l.get(i);
			String file=doc.getValueString("file");
			if(file!=null && file.length()>0) { list.add(file); }
			else if(!defaultAdded) { list.add(aAnnoDoc.DEFAULT_FILE); defaultAdded=true; }	
		}
			
		if(list.size()==0) { list.add(aAnnoDoc.DEFAULT_FILE); }
		
		return list;
	}
	
	//---------------------------------------------
	
	/** document head **/
	public void head(String outputName) throws IOException {
		w.title(doc.getName()); // ,doc.getAnnotation("author"),doc.getAnnotation("date"));
		String genLabel=(String)options.get("genlabel"); if(genLabel==null) { genLabel="aAnnoDoc created on"; }
		w.nl().w(":last-update-label: "+genLabel).nl();	
		w.paragraph(getDoc(doc,true));
	}
	
	/** document bottom **/
	public void bottom(String outputName) throws IOException {
//		w.nl2().w(":last-update-label: "+(new Date())+" [<openon.org/aannodoc>] ").nl2();		
		w.close();
	}
	
	//---------------------------------------------
	
	/** document body **/
	public void body(String outputName) throws IOException {
		docs(outputName);
		attributes(outputName);
		values(outputName);
		features(outputName);
		connections(outputName);
		examples(outputName);
		bugs(outputName);
		tests(outputName);
	}
	
	public void docs(String outputName) throws IOException {
		List<AnnotationDoc> list;
		if(outputName.equals(aAnnoDoc.DEFAULT_FILE)) { list=adoc.findAnnotation(aDoc.class); }
		else { list=adoc.findAnnotation(aDoc.class,"file",outputName); }
		if(list==null || list.size()==0) { return ; }
		
//		w.subTitle("Docs");
		annotation(toTree("Docs",list));
//		w.subTitleEnd();
	}
	
	public void attributes(String outputName) throws IOException {		
		List list=adoc.findAnnotation(aAttribute.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Attributes");
		annotation(toTree("Attributes",list));
//		w.subTitleEnd();
	}
	
	public void values(String outputName) throws IOException {				
		List  list=adoc.findAnnotation(aField.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Values");
		annotation(toTree("Values",list));
//		w.subTitleEnd();
	}
	
	public void features(String outputName) throws IOException {				
		List  list=adoc.findAnnotation(aFeature.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Features");
		annotation(toTree("Features",list));
//		w.subTitleEnd();
	}
	
	public void connections(String outputName) throws IOException {				
		List  list=adoc.findAnnotation(aConnection.class);
		if(list==null || list.size()==0) { return ; }
//		w.title1("Connections");
		annotation(toTree("Connections",list));
//		w.subTitleEnd();
	}
	
	public void examples(String outputName) throws IOException {				
		List list=adoc.findAnnotation(aExample.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Example");
		annotation(toTree("Example",list));
//		w.subTitleEnd();
	}
	
	public void bugs(String outputName) throws IOException {				
		List list=adoc.findAnnotation(aBug.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Bugs");
		annotation(toTree("Bugs",list));
		w.subTitleEnd();
	}
	
	public void tests(String outputName) throws IOException {				
		List list=adoc.findAnnotation(aTest.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Tests");
		annotation(toTree("Tests",list));
//		for(int i=0;i<list.size();i++) { annotation(list.get(i)); }
//		w.subTitleEnd();
	}
	
	//----------------------------------------------------------------
	
	public void annotation(Tree<AnnotationDoc> tree) {
		AnnotationDoc a=tree.getData();
		Object name=tree.getName();
		if(!w.e(name)) { w.subTitle(name); }
		if(a!=null) { annotation(a); }
		for(int i=0;i<tree.size();i++) { 
			Object o=tree.get(i);
			if(o instanceof Tree) { annotation((Tree)o); } 
			else { annotation((AnnotationDoc)o); }
		}
		if(!w.e(name)) { w.subTitleEnd(); }
	}
	
	public void annotation(AnnotationDoc doc) {
		String title=addString(doc.getValueName(), " ",doc.getValuePath());
		w.subTitle(title);
		w.paragraph(getDoc(doc,true));
		w.subTitleEnd();
	}
		
	//------------------------------------------------------------------
	
	/** get title of annotation **/
	public String getTitle(AnnotationDoc doc) {
		String title=toString(doc.getValue(ATR_TITLE),null);
		if(title==null) { title=doc.getParent().getName(); }
		return title;
	}
	
	//------------------------------------------------------------------
	public Tree<AnnotationDoc> toTree(String rootName,List<AnnotationDoc> list) {
		Tree<AnnotationDoc> tree=new Tree<AnnotationDoc>(rootName);
		for(int i=0;i<list.size();i++) {
			AnnotationDoc a=list.get(i);
//			String name=a.getValueNameString();
			String name=getTitle(a);
			tree.getTreeOf(name, true).add(list.get(i));
		}
		return tree.sort(null);
	}

}
