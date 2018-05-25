package org.openon.annodoc.test.diagram;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.SequenceDiagramWriter;
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
		Map asciidoctorAttribtues=new HashMap();
		asciidoctorAttribtues.put(AsciiDocCreator.ASCIIDOC_ATTR_DOT, "C:/Data/Programme/graphviz/bin/dot.exe");
		AsciiDocCreator.Adoc2Html(adoc, "doc/UseCaseDiagrammTest.html",asciidoctorAttribtues);
	}		
	
	public void createDiagram(AsciiDocWriter wr) throws IOException {
		UseCaseDiagramWriter dia=new UseCaseDiagramWriter(wr, "myUseCase");
		
//		wr.w("\nUser -> (Start)\nUser --> (Use the application) : A small label\n\n:Main Admin: ---> (Use the application) : This is\nyet another\nlabel\n");
//		wr.w("\n:First Actor:\n:Another actor: as Men2  \nactor Men3\nactor :Last actor: as Men4\n");
		dia.usecase("1");dia.usecase("2");
		dia.actor("A","test1");dia.actor("B","Person");
		dia.link("A", "1");dia.link("A", "2",2,"example");
		dia.extend("A", "B", 3, "child");
		dia.noteTo("A", "Parent",dia.LEFT);
		dia.noteAs("X","SimpleNote");
		dia.line("X","A");
		dia.line("1","X");
		
		dia.rectangle("Infos");
		dia.usecase("3");dia.usecase("4");
		dia.connect("3","4",1,"Hallo","<",dia.LINE,">");
		dia.rectangleEnd();
		
		dia.line("3", "2");
		dia.end();
	}
}
