package org.openon.annodoc.test.diagram;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.SaltDiagramWriter;
import org.openon.aannodoc.asciidoc.SaltDiagramWriter.Align;

public class SaltDiagramTest {

	public static void main(String[] args) throws Exception {
		SaltDiagramTest dia=new SaltDiagramTest();
		
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
		AsciiDocCreator.Adoc2Html(adoc, "doc/SaltDiagrammTest.html",asciidoctorAttribtues);
	}		
	
	public void createDiagram(AsciiDocWriter wr) throws IOException {
		SaltDiagramWriter dia=new SaltDiagramWriter(wr, "mySaltDiagram");

		dia.menu(null,"one","two","three");
		dia.tab("bbb", "aaa","bbb","ccc","ddd");
		
		dia.form(2, true); 
		dia.input("input", "value", 20,Align.right);
		dia.checkbox("checkboxes", "2","1","2","3");
		dia.radio("radios", null,"1","2","3");
		dia.select("select", "selected value");
		dia.formLine();
		dia.empty(); 		
		dia.group(); dia.button("cancel","save","delete"); dia.groupEnd();		
		dia.formEnd();
		
		dia.tree();dia.treeLine("one");dia.treeSub("two");dia.treeSub("three");dia.treeSubEnd(); dia.treeLine("four"); dia.treeEnd();
		
		dia.table();dia.tableLine("111","222","333"); dia.tableLine("xxx","yyy","zzz");dia.tableEnd();

		
		dia.end();
	}
}
