package org.openon.aannodoc.generator.writer;

import java.io.IOException;
import java.util.List;

import org.openon.aannodoc.annotation.aAnnotation;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.TableWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.AnnotationParameterDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.utils.AnnoUtils;

/**
 * Writer for generators to create document about annotations and its using 
 * 
 * 
 *
 */
public class AnnotationWriter {

	protected JarDoc jarDoc;
	protected SourceAnnotations annotations;	
	protected AsciiDocWriter w;	
	
	public AnnotationWriter(AsciiDocWriter w,SourceAnnotations adoc) throws IOException {
		this.w=w;
		this.annotations=adoc; 		
		this.jarDoc=annotations.doc();
	}
	
	public void close() {}
	
	/** find all defined annotations **/
	public List<AnnotationDoc> findAllAnnoation() throws IOException { return annotations.findAnnotation(null); }
	/** find all annotation with aAnnotaiton **/
	public List<AnnotationDoc> findAAnnoation() throws IOException { return annotations.findAnnotation(aAnnotation.class); }
		
	
//	public AnnotationWriter tableAnnoations() throws IOException {
//		return tableAnnoations(findAAnnoation());
//	}
	
//	public AnnotationWriter tableAnnoations(Object... annos) throws IOException {
	public AnnotationWriter tableAnnoations(List<AnnotationDoc> annos) throws IOException {		
		if(annos==null) { return this; }
		TableWriter twr=new TableWriter(w, "Annotations");
		twr.tableHead("Name","Title","Description");
		for(int i=0;i<annos.size();i++) {
			AnnotationDoc a=annos.get(i);
			ClassDoc doc=(ClassDoc)a.getParent();		
			AnnotationDoc deprecated=doc.getAnnotation(Deprecated.class);
			String name="@"+doc.getName();
			if(deprecated!=null) { name=w.toLinethrough(name); }
			twr.tableLine(name,w.toReference(AnnoUtils.getTitle(a)),AnnoUtils.getDocShort(a));
		}
		twr.tableEnd();
		return this;
	}

	/** write infos for all annotations **/
	public AnnotationWriter infoAnnoations(List<AnnotationDoc> annos) throws IOException {	
		w.subTitle("Annotations");
		for(int i=0;annos!=null && i<annos.size();i++) { infoAnnoations(annos.get(i)); }
		w.subTitleEnd();
		return this;
	}
		
	/** write inforamtion about annotation **/
	public AnnotationWriter infoAnnoations(AnnotationDoc anno) throws IOException {	
		if(anno==null) { return this ; }
		ClassDoc doc=(ClassDoc)anno.getParent();
		
		AnnotationDoc deprecated=doc.getAnnotation(Deprecated.class);
		String title=AnnoUtils.getTitle(anno);
		if(deprecated!=null) { 		
			w.subTitle(w.toLinethrough(title));
			w.warning("Deprecated: "+AnnoUtils.getDoc(deprecated)); 
		}else {		
			w.subTitle(title);
		}
		
		String name="@"+doc.getName();
		w.list(name);
		
		w.paragraph(AnnoUtils.getDoc(anno));
//		AnnoUtils.writeTable(w, anno.getName(), anno);
			
		
		List<FieldDoc> fields=doc.getAllFields(jarDoc);		
		tableAnnoationParam(name,fields);
		infoAnnoationParam(name,fields);
		
		w.subTitleEnd();
		return this;
	}
	
	public AnnotationWriter tableAnnoationParam(String anno,List<FieldDoc> fields) throws IOException {
		TableWriter twr=new TableWriter(w, "Parameter "+anno);
		twr.tableHead();
		
		for(int i=0;fields!=null && i<fields.size();i++) {
			if(fields.get(i) instanceof AnnotationParameterDoc) {
				AnnotationParameterDoc ap=(AnnotationParameterDoc)fields.get(i);
				
				AnnotationDoc deprecated=ap.getAnnotation(Deprecated.class);
				String name="Attribute "+ap.getName();
				if(deprecated!=null) { name=w.toLinethrough(name); }
				else { name=w.toReference(name); }
				
				twr.tableLine(name,AnnoUtils.getDocShort(ap));
			}
		}
		twr.tableEnd();
		return this;
	}
	
	public AnnotationWriter infoAnnoationParam(String anno,List<FieldDoc> fields) throws IOException {
		for(int i=0;fields!=null && i<fields.size();i++) {
			if(fields.get(i) instanceof AnnotationParameterDoc) {
				AnnotationParameterDoc ap=(AnnotationParameterDoc)fields.get(i);
				infoAnnoationParam(anno,ap);
			}
		}
		return this;
	}
	
	public AnnotationWriter infoAnnoationParam(String anno,AnnotationParameterDoc param) throws IOException {
		String name=param.getName();
		String value=w.toString(param.getValue());
		
		AnnotationDoc deprecated=param.getAnnotation(Deprecated.class);
		
		String title="Attribute "+name;
		if(deprecated!=null) { 		
			w.subTitle(w.toLinethrough(title));
			w.warning("Deprecated: "+AnnoUtils.getDoc(deprecated)); 
		}else {		
			w.subTitle(title);
		}
		
		w.list(anno+"("+name+"="+value+")");
		
		
		
		
		w.paragraph(param.getComment());
		
		w.table(null);
		w.tableLine("Type",param.getTypeName());
		
		w.tableLine("Required",value==null);
		w.tableLine("Default",value); 
		w.tableEnd();
		
		w.subTitleEnd();
		return this;
		
	}
}
