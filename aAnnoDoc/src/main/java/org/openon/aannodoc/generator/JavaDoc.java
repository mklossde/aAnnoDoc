package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.List;

import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.MethodDoc;
import org.openon.aannodoc.source.TypeDoc;
import org.openon.aannodoc.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator for JavaDoc based on AsciiDoc 
 * 
 * 
 * @author michael
 *
 */
@aDoc(name="generator/JavaDocGenerator")
public class JavaDoc extends AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(JavaDoc.class);
	
	public static final String NAME="Java";
	
	public JavaDoc() {super();}

	
	//---------------------------------------------
	
	/** document head **/
	public void head() throws IOException {
		w.title(doc.getName()); // ,doc.getAnnotation("author"),doc.getAnnotation("date"));
		w.paragraph(doc.getComment());
	}
	
	/** document bottom **/
	public void bottom() throws IOException {
		w.close();
	}
	
	//----------------------------------------------
	
	
	/** document body **/
	public void body() throws IOException {
		List<ClassDoc> list=doc.getAllClasses();
		for(int i=0;i<list.size();i++) { write(list.get(i)); }
	}
	
	public void write(ClassDoc cl) {
		w.subTitle(cl.getTypeName());
		w.paragraph(cl.getComment());
		
		String ext=cl.getExtends();
		if(ext!=null) { w.paragraph("extends ").reference(ext); }
		// Class
		List impl=cl.getImplements();
		w.paragraph("implements ");
		for(int i=0;impl!=null && i<impl.size();i++) { w.list("").reference(impl.get(i)); }
		
		// Fields
		w.subTitle("Fields");
		List<FieldDoc> fields=cl.getLocalFields();
		for(int i=0;i<fields.size();i++) { write(fields.get(i)); }
		w.subTitleEnd();
		
		// Methods
		w.subTitle("Methods");
		List<MethodDoc> methods=cl.getLocalMethods();
		for(int i=0;i<methods.size();i++) { write(methods.get(i)); }
		w.subTitleEnd();
		
		w.subTitleEnd();
		
	}
	
	public void write(TypeDoc field) {
		w.subTitle(field.getName());
		w.label("  "+field.toJava());
		w.paragraph(field);
		w.subTitleEnd();
	}
	
//	public void write(FieldDoc field) {
//		w.subTitle(field.getName());
//		w.paragraph(field);
//		w.subTitleEnd();
//	}

		
	//---------------------------------------------
	
	public String toAnnotationName(Object obj) throws IOException {
		return (String)obj; 
	}
}
