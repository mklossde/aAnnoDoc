package org.openon.annodoc.test.diagram;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.diagram.ClassDiagramWriter;
import org.openon.aannodoc.asciidoc.diagram.SaltDiagramWriter;
import org.openon.aannodoc.asciidoc.diagram.SaltDiagramWriter.Align;

public class ClassDiagramTest {

	public static void main(String[] args) throws Exception {
		ClassDiagramTest dia=new ClassDiagramTest();
		
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
System.out.println(adoc);		
//adoc=":First Actor:\n:Another\nactor: as Men2  \nactor Men3\nactor :Last actor: as Men4\n";		
//adoc="User -> (Start)\nUser --> (Use the application) : A small label\n\n:Main Admin: ---> (Use the application) : This is\nyet another\nlabel";
		Map asciidoctorAttribtues=new HashMap();
		asciidoctorAttribtues.put(AsciiDocCreator.ASCIIDOC_ATTR_DOT, "C:/Data/Programme/graphviz/bin/dot.exe");
		AsciiDocCreator.Adoc2Html(adoc, "doc/ClassDiagrammTest.html",asciidoctorAttribtues);
	}		
	
	public void createDiagram(AsciiDocWriter wr) throws IOException {
		ClassDiagramWriter dia=new ClassDiagramWriter(wr, "myClassDiagram");

		dia.test();
		
		dia.end();
	}
}
