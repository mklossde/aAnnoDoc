package org.openon.annodoc.test.diagram;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.SequenzDiagramWriter;
import org.openon.aannodoc.asciidoc.UseCaseDiagramWriter;
import org.openon.aannodoc.generator.GenBaseDoc;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.JarDoc;

public class UseCaseDiagramTest {

	public static void main(String[] args) throws Exception {
		UseCaseDiagramTest dia=new UseCaseDiagramTest();
		
//		dia.testSystemOut();
		dia.test();
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
//adoc=":First Actor:\n:Another\nactor: as Men2  \nactor Men3\nactor :Last actor: as Men4\n";		
//adoc="User -> (Start)\nUser --> (Use the application) : A small label\n\n:Main Admin: ---> (Use the application) : This is\nyet another\nlabel";
		AsciiDocCreator.Adoc2Html(adoc, "doc/UseCaseDiagrammTest.html");
	}		
	
	public void createDiagram(AsciiDocWriter wr) throws IOException {
		UseCaseDiagramWriter sqWr=new UseCaseDiagramWriter(wr, "myUseCase");
		
//		wr.w("\nUser -> (Start)\nUser --> (Use the application) : A small label\n\n:Main Admin: ---> (Use the application) : This is\nyet another\nlabel\n");
		wr.w("\n:First Actor:\n:Another actor: as Men2  \nactor Men3\nactor :Last actor: as Men4\n");
		sqWr.end();
	}
}
