package org.openon.aannodoc.asciidoc;

import java.io.IOException;

import org.openon.aannodoc.annotation.aDoc;

/**
 * Create a use-case diagram, within generator, with UseCaseDiagramWriter
 * 
 * 
 * for information plantuml use-case-diagrams see http://plantuml.com/use-case-diagram
 * 
 */
@aDoc(title="diagram/use case diagram")
public class UseCaseDiagramWriter {

	public static final String LINK="----------------------------------------------------------------------------------------------------";
	public static final String LINE="....................................................................................................";
	public static final String plutgin="plantuml";
	
	public static final String LEFT="left";
	public static final String RIGHT="right";
	public static final String TOP="top";
	public static final String BOTTOM="bottom";
	
	protected AsciiDocWriter wr;
	
	public UseCaseDiagramWriter(AsciiDocWriter wr)  throws IOException { this(wr,null); }
	public UseCaseDiagramWriter(AsciiDocWriter wr,String title)  throws IOException { 
		this.wr=wr;
		if(!wr.e(title)) {start(title); }
	}
	
	/** start sequenz diagram with title of it **/
	public UseCaseDiagramWriter start(String name) throws IOException {
		wr.nnl2().w('[').w(plutgin).w(',').w(name).w(']').nl();
		wr.w("----").nl();
		return this;
	}
	
	/** start sequenz diagram **/
	public UseCaseDiagramWriter end()  throws IOException {
		wr.nnl().w("----").nl();
		return this;
		
	}
	
	//--------------------------------------------------------------------
	// diagram

	public UseCaseDiagramWriter usecase(String name)  throws IOException { return usecase(name,null); }
	public UseCaseDiagramWriter usecase(String name,String as)  throws IOException {
		wr.nnl().w('(').w(name).w(')');
		if(as!=null && as.length()>0) { wr.w(" as \"").w(as).w("\"");}
		wr.nl();
		return this;			
	}

	public UseCaseDiagramWriter actor(String name) throws IOException { return actor(name,null); } 
	public UseCaseDiagramWriter actor(String name,String stereotype)  throws IOException {
		wr.nnl().w(':').w(name).w(':');
		if(!wr.e(stereotype)) { wr.w(" << ").w(stereotype).w(" >>"); }
		wr.nl();
		return this;			
	}	
	
	//--------------------------------
	
	public UseCaseDiagramWriter link(String name1,String name2) throws IOException { return link(name1,name2,2,null); }
	public UseCaseDiagramWriter link(String name1,String name2,String text) throws IOException { return link(name1,name2,2,text); }
	public UseCaseDiagramWriter link(String name1,String name2,int len,String text) throws IOException {
		return connect(name1, name2, len, text,null, LINK, ">");
	}
	
	public UseCaseDiagramWriter extend(String name1,String name2) throws IOException { return extend(name1, name2, 2, null); }
	public UseCaseDiagramWriter extend(String name1,String name2,String text) throws IOException { return extend(name1, name2, 2, text); }
	public UseCaseDiagramWriter extend(String name1,String name2,int len,String text) throws IOException {
		return connect(name1, name2, len, text,null, LINK, "|>");
	}
	
	public UseCaseDiagramWriter line(String name1,String name2) throws IOException { return line(name1,name2,2,null); }
	public UseCaseDiagramWriter line(String name1,String name2,int len,String text) throws IOException {
		return connect(name1, name2, len, text, null, LINE, null);
	}
	
	public UseCaseDiagramWriter connect(String name1,String name2,int len,String text,String type,String after) throws IOException { return connect(name1, name2, len, text, null, type, after);}
	public UseCaseDiagramWriter connect(String name1,String name2,int len,String text,String before,String type,String after) throws IOException {
		wr.nnl().w(name1).w(" ").w(before); wr.w(type.substring(0,len));
		wr.w(after).w(" ").w(name2);
		if(!wr.e(text)) { wr.w(" : ").w(text); }
		wr.nl();return this;
	}
	
	//--------------------------------
	
	public UseCaseDiagramWriter noteTo(String to,String text) throws IOException { return noteTo(to,text,null); }
	public UseCaseDiagramWriter noteTo(String to,String text,String align) throws IOException { 
		if(wr.e(align)) { align="top"; }
		wr.nnl().w("note ").w(align).w(" of ").w(to);
		wr.nl();
		wr.w(text);
		wr.nl().w("end note").nl();
		return this;
	}
	
	public UseCaseDiagramWriter noteAs(String noteName,String text) throws IOException {
		wr.nnl().w("note \"").w(text).w("\" as ").w(noteName).nl();
		return this;
	}
	
	//--------------------------------
	
	public UseCaseDiagramWriter rectangle(String name) { wr.nnl().w("rectangle ").w(name).w(" {").nl(); return this; }
	public UseCaseDiagramWriter rectangleEnd() { wr.nnl().w("}").nl(); return this; }
	
	//--------------------------------
	
	public UseCaseDiagramWriter newpage() {  wr.nnl().w("newpage").nl(); return this;}
	
	
}
