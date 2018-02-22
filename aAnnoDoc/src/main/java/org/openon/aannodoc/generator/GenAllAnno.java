package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator for Annotation-Based-Doucments
 * Scann all Annotaiotns in Source and create a document of all used annotations

 *
 * 
 * @author michael
 *
 */
@aDoc(title="generator/AnnoDocGenerator")
public class GenAllAnno extends AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(GenAllAnno.class);
	
	public static final String NAME="AllAnno";
	
	public GenAllAnno() {super();}
	
	
	//-----------------------------------------------------------------------------
	// Options
	
	/** 
	 * option: annotations = list all annotations used for document
	 *  string= a semicole based list of annotations
	 *  empty/default list all annotations
	 *  
	 *  @return option-annotation-list
	 **/
	@aAttribute(title="options/annotations")
	public Object[] getAnnotations() throws IOException {
		Object obj=options.get("annotations");
		if(obj==null) { obj=annotations.listAnnotions(false); }
		return ReflectUtil.toArray(obj);
	}
	
	//---------------------------------------------
	
	/** document head **/
	public void head(String outputName) throws IOException {
		w.title(doc.getName()); // ,doc.getAnnotation("author"),doc.getAnnotation("date"));
		String genLabel=(String)options.get("genlabel"); if(genLabel==null) { genLabel="aAnnoDoc created on"; }
		w.nl().w(":last-update-label: "+genLabel).nl();	
		w.paragraph(doc.getComment());
	}
	
	
	/** document bottom **/
	public void bottom(String outputName) throws IOException {
		w.close();
	}
	
	//----------------------------------------------------------
	
	
	/** document body **/
	public void body(String outputName) throws IOException {
		Object a[]=getAnnotations();
		Arrays.sort(a); 
		for(int i=0;a!=null && i<a.length;i++) {
			String annoClassName=toAnnotationName(a[i]);
			w.title1(annoClassName);
			List<AnnotationDoc> list=annotations.findAnnotation(annoClassName);
			Collections.sort(list);
			for(int t=0;list!=null && t<list.size();t++) {
				AnnotationDoc ad=list.get(t);
				w.title2(ad.getName()+" "+ad.getRef());
				w.reference(ad.getClassDoc().getTypeName());
				Map<String,Object> values=ad.getValues();
				for (Entry<String, Object> e: values.entrySet()) {
					w.label(e.getKey(),e.getValue());
				}
				w.paragraph(ad.getComment());
				
			}
		}
	}

		
	//---------------------------------------------
	
	public String toAnnotationName(Object obj) throws IOException {
		return (String)obj; 
	}
}
