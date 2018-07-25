package org.openon.annodoc.test.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;

public class AsciiDocExample {

	protected AsciiDocWriter wr;
	protected String file;
	
	public static void main(String[] args) throws Exception {
		AsciiDocExample ex=new AsciiDocExample();
		ex.open();
		ex.write();
		ex.output();
		
		System.out.println("end");
	}
	
//	@Before
	public void open() throws IOException {
		file="doc/AsciiDocExample.adoc";
		FileWriter w=new FileWriter("doc/AsciiDocExample.adoc");		
		wr=new AsciiDocWriter(w);
//wr=new AsciiDocWriter(System.out);
	}
	
//	@Test
	public void write()  throws IOException {
		
		wr.title("AsciiDocExample", "AnnoDoc", "mail@openon.org", "1.0"); e();
		
		s("wr.title1(\"title1\");");
		wr.title1("title1"); e();
		
		wr.title2("title2");
		wr.title3("title3");
		wr.title4("title4");
		wr.title5("title5");
		
		wr.attr("attr");
		
		wr.lead("lead");
		wr.litheral("litheral");
		
		wr.note("note");
		wr.tip("tip");
		wr.warning("warning");
		wr.caution("caution");
		wr.important("important");
		
		wr.table("Table", "one","two","three");
		wr.tableLine("aaa","bbb","ccc");
		wr.tableLine("11","222222","3");
		wr.tableEnd();
		
		wr.text("text");
		wr.paragraph("paragraph");
		s("wr.literalBlock(\"literalBlock\");"); wr.literalBlock("literalBlock"); e();
		wr.literalBlock().w("literalBlock").literalBlockEnd();

		wr.include("includeIt.adoc");
//		wr.includeOrg("C:/Data/ws/gitFrontend/front/onAnnoDoc/doc/includeIt.adoc");
//		wr.includeXX("C:/Data/ws/gitFrontend/front/onAnnoDoc/doc/includeIt.adoc");
//		
		s("wr.footnote(\"footnote\");");
		wr.footnote("footnote");e();

		s("wr.image(\"image.gif\");");
		wr.image("image.png"); e();
		
		s("wr.imageBlock(\"image.gif\",\"exampleImage\");");
		wr.imageBlock("image.png","exampleImage"); e();
		
		wr.bold("bold");
		wr.italic("italic");
		wr.bolditalic("bolditalic");
		wr.monospace("monospace");
		wr.cassia("cassia");
		wr.small("small");
		wr.big("big");
		wr.underline("underline");
		wr.linethrough("linethrough");
		wr.superScript("superScript");
		wr.subScript("subScript");

		wr.quotes("quotes");
		wr.doubleQuotes("doubleQuotes");
		
		wr.subTitle("lists"); //-------------------------------------------------------
		
		wr.list("list");
		wr.list2("list2");
		wr.list3("list3");
		wr.list4("list4");
		wr.list(5,"list");
		
		wr.listSub("listSub");
		wr.listSub("listSub");
		wr.listSub("listSub");
		wr.listEnd().listEnd().listEnd();
		
		wr.orderd("orderd");
		wr.orderd2("orderd2");
		wr.orderd3("orderd3");
		wr.orderd4("orderd4");
		wr.orderd(5,"orderd");
		
		wr.orderdSub("orderdSub");
		wr.orderdSub("orderdSub");
		wr.orderdSub("orderdSub");
		wr.orderdEnd().orderdEnd().orderdEnd();
		
		wr.subTitleEnd();  //-------------------------------------------------------
		
		wr.checked("checked");
		wr.checked(1,"checked2");
		wr.unchecked("unchecked");
		wr.unchecked(1,"unchecked");
		
		wr.label("label");
		wr.label("label","text");
		
		wr.reference("reference");
		
		wr.close();
		
	}
	
	public void output() throws IOException {

		
		AsciiDocCreator a=new AsciiDocCreator();		
		a.create(new File(file), "pdf",  file.replace(".adoc", ".pdf"));
	}
	
	//---------------------------------------------------------------------------
	
	public void s(String cmd) {
		wr.literalBlock("JavaCode: "+cmd); wr.cache();
	
	}
	public void e() {
		String str=wr.getCache();
//		wr.literalBlock().pass().w(str).literalBlockEnd();
	}
}
