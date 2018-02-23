package org.openon.annodoc.test.diagram;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.SequenzDiagramWriter;
import org.openon.aannodoc.generator.GenBaseDoc;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.JarDoc;

public class SequenceDiagramTest {

	public static void main(String[] args) throws Exception {
		SequenceDiagramTest dia=new SequenceDiagramTest();
		
		dia.test();
		System.out.println("end");
	}
	
	public void testSystemOut() throws Exception {
		
		AsciiDocWriter wr=new AsciiDocWriter(System.out);
		SequenzDiagramWriter sqWr=new SequenzDiagramWriter(wr, "myDiagram");
		
		sqWr.to("test", "one", "two");
		sqWr.from("back","one", "two");
		sqWr.end();
		wr.close();
		
	}
	
	public void test() throws Exception {
//		String src="src/main/java"+"/org/openon/aannodoc"+"/aAnnoDoc.java";		
//		aAnnoDoc adoc=new aAnnoDoc().scan(src,null);
//		adoc.createDocFiles("READ.md", null, aAnnoDoc.FORMAT_ASCIIDOC);
		Options options=new Options();
		aAnnoDoc adoc=new aAnnoDoc();
		JarDoc jarDoc=new JarDoc("test");
		SourceAnnotations sAdoc=new SourceAnnotations(jarDoc);
		GenBaseDoc gen=new GenBaseDoc(); gen.init(sAdoc, options);
		AsciiDocWriter wr=gen.writer();
		
		SequenzDiagramWriter sqWr=new SequenzDiagramWriter(wr, "myDiagram");
		
		sqWr.to("test", "one", "two");
		sqWr.from("back","one", "two");
		sqWr.end();
		wr.close();
		
	}	
}
