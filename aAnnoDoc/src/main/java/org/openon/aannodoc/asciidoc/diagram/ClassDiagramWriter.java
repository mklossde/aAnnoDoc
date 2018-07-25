package org.openon.aannodoc.asciidoc.diagram;

import java.io.IOException;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;

/**
 * Class diagram with plant uml
 * 
 * @author mk@almi.de
 *
 */
@aDoc(title="diagram/class diagram")
public class ClassDiagramWriter {

	public static final String plugin="plantuml";
	public static final String format="png";
	
	protected AsciiDocWriter wr;
	
	public ClassDiagramWriter(AsciiDocWriter wr)  throws IOException { this(wr,null); }
	public ClassDiagramWriter(AsciiDocWriter wr,String title)  throws IOException { 
		this.wr=wr;
		if(!wr.e(title)) {start(title); }
	}
	
	//--------------------------------------------------------------------
	//diagram
	
	/** start diagram with title of it **/
	public ClassDiagramWriter start(String name) throws IOException {		
		wr.nnl2().w('[').w(plugin).w(',').w(name).w(',').w(format).w(']').nl();
		wr.w("----").nl();
		return this;
	}
		
	/** end diagram **/
	public ClassDiagramWriter end()  throws IOException {
		wr.w("----").nl();
		return this;		
	}
	
	//--------------------------------------------------------------------
	
	//type: abstract,interface
	
	public ClassDiagramWriter addEnum(String name,String...values) throws IOException {
		wr.nnl().w("enum ").w(name).wnl(" {");
		for(int i=0;values!=null && i<values.length;i++) { wr.w(" ").wnl(values[i]); }
		wr.wnl("}");
		return this;
	}
	
	public ClassDiagramWriter addClass(String className) throws IOException { return addClass(className,"abstract"); }
	public ClassDiagramWriter addAbstract(String className) throws IOException { return addClass(className,"interface"); }
	public ClassDiagramWriter addInterface(String className) throws IOException { return addClass(className,"class"); }
	
	public ClassDiagramWriter addClass(String className,String type) throws IOException {
		wr.nnl();
		wr.w(type).w(" ");
		wr.wnl(className);return this;
	}
	
	// visible: -=private, #=protected, ~=packagePrivate, +=public
	// classifier: static or abstract
	
	public ClassDiagramWriter addMethod(String className,String classifier,String visible,String methodReturn,String methodName) throws IOException {
		wr.nnl().wnl(className).w(" : ");
		if(methodReturn!=null) {wr.w(methodReturn).w(" "); }
		if(classifier!=null) {wr.w("{").w(classifier).w("} "); }
		if(visible!=null) { wr.w(visible); }
		wr.w(methodName).w("()");
		wr.nl();
		return this;
	}
	
	//line: --,==,--
	public ClassDiagramWriter addline(String line,String lineInfo) {
		wr.nnl().w(line);
		if(lineInfo!=null) { wr.w(" ").w(lineInfo).w(" ").w(line); }
		wr.nl(); return this;
	}
	
	public ClassDiagramWriter note(String note,String to) {
		//note "This is a floating note" as N1
		wr.nnl().w(note).w(" \"").w(note).w("\" as ").w(to).nl();
		return this;
	}
	
//	public ClassDiagramWriter noteLink(String note,String to) {
//		//note "This is a floating note" as N1
//		wr.nnl().w(note).w(" \"").w(note).w("\" as ").w(to).nl();
//		return this;
//	}

	
	
	//--------------------------------------------------------------------
	
	// --|> --* --o --x --# --{ --+ --^ --
	
//	protected String to(String t,boolean dot) {if(dot) { return ".."+t; } else { return "--"+t; }}
//	protected String from(String t,boolean dot) {if(dot) { return t+".."; } else { return t+"--"; }}
	
	public ClassDiagramWriter to(String from, String to) throws IOException {		
		return link(from,"--|>" ,to);
	}
	public ClassDiagramWriter from(String to, String from) throws IOException {		
		return link(from,"<|--" ,to);
	}
	
	public ClassDiagramWriter link(String from, String type,String to) throws IOException { return link(from, null, type, null, to, null,null); }
	public ClassDiagramWriter link(String from, String fromMany, String type,String toMany, String to,String label,Boolean labelTo) throws IOException {
		wr.nnl().w(from); 
		if(fromMany!=null) { wr.w(" ").w(fromMany); }
		wr.w(" ").w(type);
		if(toMany!=null) { wr.w(" ").w(toMany); }
		wr.w(" ").w(to);
		if(label!=null) { 
			if(labelTo!=null && !labelTo) { wr.w("< "); }
			wr.w(" : ").w(label);
			if(labelTo!=null && labelTo) { wr.w(" >"); }
		}
		wr.nl();
		return this;
	}
	
	public void test() throws IOException {
		wr.wnl("class BlockProcessor");
		wr.wnl("Class01 <|-- Class02");
		
	}
	
}
