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
import org.openon.aannodoc.generator.writer.ServiceWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;

public class TestServiceWriter {

	protected SourceAnnotations annoDoc;
	protected ByteArrayOutputStream bout;
	protected AsciiDocWriter w;
	
	public static void main(String[] args) throws Exception {
		TestServiceWriter test=new TestServiceWriter();
		
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
		Options options=new Options();
		annoDoc=new SourceAnnotations(TestServiceWriter_Example.class,null,options);
		
		// generate doc
		ServiceWriter wr=new ServiceWriter(w, annoDoc);
	
		List<AnnotationDoc> list=annoDoc.findAnnotation(aInterface.class);
		wr.writeSequenz(list);
		
	}
}
