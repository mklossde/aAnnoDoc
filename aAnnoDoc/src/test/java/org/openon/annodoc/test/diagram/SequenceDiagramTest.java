package org.openon.annodoc.test.diagram;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.diagram.SequenceDiagramWriter;
import org.openon.aannodoc.asciidoc.diagram.UseCaseDiagramWriter;
import org.openon.aannodoc.generator.GenBaseDoc;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.JarDoc;

public class SequenceDiagramTest {

	public static void main(String[] args) throws Exception {
		SequenceDiagramTest dia=new SequenceDiagramTest();
		
		dia.testSystemOut();
//		dia.test();
		System.out.println("end");
	}
	
	public void testSystemOut() throws Exception {
		
		AsciiDocWriter wr=new AsciiDocWriter(System.out);
		createDiagram(wr);
		wr.close();
		
	}
	
	public void test() throws Exception {
		
		ByteArrayOutputStream bout=new ByteArrayOutputStream();		
		AsciiDocWriter wr=new AsciiDocWriter(bout);
		createDiagram(wr);
		wr.close();
		
		String adoc=bout.toString();
		AsciiDocCreator.Adoc2Html(bout.toString(), "doc/SequenzeDiagrammTest.html",null);
	}	
	
	public void createDiagram(AsciiDocWriter wr) throws IOException  {
		SequenceDiagramWriter sqWr=new SequenceDiagramWriter(wr, "mySequenze");
		
		sqWr.to("test", "one", "two");
		sqWr.from("back","one", "two");
		sqWr.end();
	}
}
