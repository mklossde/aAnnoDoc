package org.openon.annodoc.test;

import java.io.File;
import java.io.FileWriter;

import org.openon.aannodoc.asciidoc.AsciiDocCreator;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;

public class AsciiDocTranslate {

	public static void main(String[] args) throws Exception {
		AsciiDocCreator adoc=new AsciiDocCreator();
		
		String file="doc/test.txt";
//		String file="ex/article.txt";
//		String file="ex/asciidoc.txt";
		
		adoc.create(new File(file), "pdf", 
				file.replace(".txt", ".pdf"));
		
		System.out.println("end");
	}
}
