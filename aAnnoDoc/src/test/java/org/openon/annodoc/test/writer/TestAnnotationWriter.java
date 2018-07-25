package org.openon.annodoc.test.writer;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.annotation.aInterface;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.generator.writer.AnnotationWriter;
import org.openon.aannodoc.generator.writer.ServiceWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;

public class TestAnnotationWriter {

	protected SourceAnnotations annoDoc;
	protected ByteArrayOutputStream bout;
	protected AsciiDocWriter w;
	
	public static void main(String[] args) throws Exception {
		TestAnnotationWriter test=new TestAnnotationWriter();
		
		test.test();
		
		System.out.println("end");
	}
	
	
	public void open() throws Exception {		
		// create writer
		bout=new ByteArrayOutputStream();		
		w=new AsciiDocWriter(bout);
	}
	
	public void close() throws Exception {
		w.close();

		System.out.println(bout.toString());	
//		// write output
//		Map asciidoctorAttribtues=new HashMap();
//		asciidoctorAttribtues.put(AsciiDocCreator.ASCIIDOC_ATTR_DOT, "C:/Data/Programme/graphviz/bin/dot.exe");
//		AsciiDocCreator.Adoc2Html(bout.toString(), "doc/ServiceWriterTest.html",asciidoctorAttribtues);
				
	}
	
	public void test() throws Exception {
		String javaSource="../";
		Options options=new Options();
		annoDoc=new SourceAnnotations(javaSource,null,options);
		
		// generate doc
		AnnotationWriter wr=new AnnotationWriter(w, annoDoc);
	
		List<AnnotationDoc> list=wr.findAAnnoation();
System.out.println("l:"+list);
//		wr.writeSequenz(list);
		
	}
}
