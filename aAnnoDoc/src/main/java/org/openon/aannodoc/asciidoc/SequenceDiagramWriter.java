package org.openon.aannodoc.asciidoc;

import java.io.IOException;

import org.openon.aannodoc.annotation.aDoc;

/**
 * Create a sequenz diagram, within generator, with SequenzDiagramWriter
 * 
 * 		SequenzDiagramWriter dr=new SequenzDiagramWriter(asciiWriter,"My ServcieCall");		// instance SequenzDiagramWriter
 *		sw.type("WebFrontend","actor","#green");					// define frontend as actor (a green color)
 *		dw.to("call", "WebFrontend", "WebFrontend"); 
 * 		dw.from("response, "WebFrontend", "WebService");
 *		dw.end();												// end sequenz
 * 
 * for information plantuml sequenz diagrams see http://plantuml.com/sequence-diagram
 * 
 */
@aDoc(title="diagram/sequenz diagram")
public class SequenceDiagramWriter {

	public static final String plutgin="plantuml";
	
	protected AsciiDocWriter wr;
	
	protected String last;
	
	public SequenceDiagramWriter(AsciiDocWriter wr)  throws IOException { this(wr,null); }
	public SequenceDiagramWriter(AsciiDocWriter wr,String title)  throws IOException { 
		this.wr=wr;
		if(!wr.e(title)) {start(title); }
	}
	
	//--------------------------------------------------------------------
	// UML  sequenz diagram
	
	/** start sequenz diagram with title of it **/
	public SequenceDiagramWriter start(String name) throws IOException {
		wr.nnl2().w('[').w(plutgin).w(',').w(name).w(']').nl();
		wr.w("----").nl();
		return this;
	}
	
	/** define sequenz point of type [actor,boundary,control,entity,database] **/
	public SequenceDiagramWriter type(String name,String type,String color) throws IOException {
		wr.nnl().w(type).w(' ').w(name);
		if(!wr.e(color)) { wr.w(' ').w(color); }
		wr.nl(); return this;
	}
	
	public SequenceDiagramWriter autonumber(String nrInfo) throws IOException {
		wr.nnl().w("autonumber"); if(!wr.e(nrInfo)) { wr.w(' ').w(nrInfo); } return this;
	}
	
	
	/** 
	 * @param title - title of sequenz
	 * @param style - arrow style 
	 * @param from - left hand
	 * @param to - right hand
	 */
	public SequenceDiagramWriter sequenz(Object title,String from,String style,String to)  throws IOException {
		wr.w(toName(from));
		if(wr.e(style)) { wr.w(" -> "); } else { wr.w(' ').w(style).w(' '); }
		wr.w(toName(to)).w(": ").w(title).nl();
		return this;
	}
	
	public String toName(String name) { 
		if(name==null) { return "NULL"; }
		return name.replace('-', '_').replace(':', '_').replace(' ', '_');
	}
	
	public SequenceDiagramWriter to(Object title,String to)  throws IOException { sequenz(title, last, "->",to); last=to; return this;}
	public SequenceDiagramWriter to(Object title,String from,String to)  throws IOException { sequenz(title, from, "->",to); last=to; return this;}
	public SequenceDiagramWriter to(Object title,String from,String to,String color)  throws IOException { sequenz(title, from, "-"+color+">",to); last=to; return this;}
	
	public SequenceDiagramWriter from(Object title,String from)  throws IOException { sequenz(title, from, "<-",last); last=from; return this; }
	public SequenceDiagramWriter from(Object title,String from,String to)  throws IOException { sequenz(title, from, "<-",to); last=from; return this;}
	public SequenceDiagramWriter from(Object title,String from,String to,String color)  throws IOException { sequenz(title, from, "<"+color+"-",to); last=from; return this;}

	//		wr.w(from).w(" -> ").w(to).w(": ").w(title).nl();
//		return this;
//	}
//	public AsciiDiagramWriter sequenzFrom(String title,String from,String to)  throws IOException {
//		wr.w(from).w(" <- ").w(to).w(": ").w(title).nl();
//		return this;
//	}

	
	/** start sequenz diagram **/
	public SequenceDiagramWriter end()  throws IOException {
		wr.nnl().w("----").nl();
		return this;
		
	}

}
