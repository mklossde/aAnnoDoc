package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openon.aannodoc.AnnoDoc;
import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aConnection;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aExample;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aTest;
import org.openon.aannodoc.annotation.aValue;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.utils.ReflectUtil;
import org.openon.aannodoc.utils.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator for AnnoDoc-Annotation documents (aDoc,aAttribtue,aBug,..) 
 * 
 * @author michael
 *
 */
@aDoc(name="generator/AnnoDocGenerator")
public class AnnotationAppDoc extends AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(AnnotationAppDoc.class);
	
	public static final String NAME="AApp";
	
	public static final String ANNOTATIONS="annotations";

	public AnnotationAppDoc() { super(); }
	
	
	//-----------------------------------------------------------------------------
	// Options
		
	/**
	 * option: format of output 
	 * 
	 * 		adoc=> AsciiDoc
	 * 		html=> html (AsciiDoc)
	 * 		pdf => pdf (AsciiDoc)
	 * 
	 * 
	 * @return option-format
	 */
	@aAttribute(name="options/format")
	public String getFormat() {
		Object obj=options.get(AnnoDoc.FORMAT); if(obj==null) { obj="html"; }
		return (String)obj;
	}
	
	/**
	 * option: output-file
	 * 
	 * @return option-output-file
	 */
	@aAttribute(name="options/output")
	public String getOutput() throws IOException {
		String obj=(String)options.get(AnnoDoc.OUTPUT); 
		if(obj==null) { obj="AnnoDocOutput"; }
		if(obj.indexOf('.')==-1) { obj=obj+"."+getFormat(); }
		return (String)obj;
	}

	//---------------------------------------------
	
	/** document head **/
	public void head() throws IOException {
		w.title(doc.getName()); // ,doc.getAnnotation("author"),doc.getAnnotation("date"));
		w.paragraph(doc.getComment());
	}
	
	/** document body **/
	public void body() throws IOException {
		w.title1("Docs");
		List<AnnotationDoc> list=adoc.findAnnotation(aDoc.class);
		annotation(toTree(list));
		
		w.title1("Attributes");
		list=adoc.findAnnotation(aAttribute.class);
		annotation(toTree(list));
		
		w.title1("Values");
		list=adoc.findAnnotation(aValue.class);
		annotation(toTree(list));
		
		w.title1("Features");
		list=adoc.findAnnotation(aFeature.class);
		annotation(toTree(list));
		
		w.title1("Connections");
		list=adoc.findAnnotation(aConnection.class);
		annotation(toTree(list));	
		
		w.title1("Example");
		list=adoc.findAnnotation(aExample.class);
		annotation(toTree(list));
		
		w.title1("Bugs");
		list=adoc.findAnnotation(aBug.class);
		annotation(toTree(list));
		
		w.title1("Tests");
		list=adoc.findAnnotation(aTest.class);
		annotation(toTree(list));
//		for(int i=0;i<list.size();i++) { annotation(list.get(i)); }
	}
	
	public void annotation(Tree<AnnotationDoc> tree) {
		AnnotationDoc a=tree.getData();
		w.subTitle(tree.getName());
		if(a!=null) { annotation(a); }
		for(int i=0;i<tree.size();i++) { 
			Object o=tree.get(i);
			if(o instanceof Tree) { annotation((Tree)o); } 
			else { annotation((AnnotationDoc)o); }
		}
		w.subTitleEnd();
	}
	
	public void annotation(AnnotationDoc doc) {
		w.title2(doc.getValueName()+" "+doc.getValuePath());
		w.paragraph(doc.getComment());
	}
	
	/** document bottom **/
	public void bottom() throws IOException {
		w.close();
	}
		
	//------------------------------------------------------------------
	
	public Tree<AnnotationDoc> toTree(List<AnnotationDoc> list) {
		Tree<AnnotationDoc> tree=new Tree<AnnotationDoc>();
		for(int i=0;i<list.size();i++) {
			AnnotationDoc a=list.get(i);
			String name=a.getValueNameString();
			tree.getTreeOf(name, true).add(list.get(i));
		}
		return tree.sort(null);
	}

}
