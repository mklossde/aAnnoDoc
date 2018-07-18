package org.openon.annodoc.test.writer;

import java.io.ByteArrayOutputStream;

import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.TableWriter;

public class TestTableWriter {

	public static void main(String[] args) throws Exception {
		TestTableWriter test=new TestTableWriter();
		test.test();
	}
	
	public void test() throws Exception {
		ByteArrayOutputStream bout=new ByteArrayOutputStream();		
		AsciiDocWriter w=new AsciiDocWriter(bout);
		
		TableWriter twr=new TableWriter(w,"test").tableHead("eins","zwei");
//		twr.tableLine("a","b");
		twr.tableLine(null,"","c");
		twr.tableEnd();
		
		w.close();
System.out.println(bout.toString());	
	}
}
