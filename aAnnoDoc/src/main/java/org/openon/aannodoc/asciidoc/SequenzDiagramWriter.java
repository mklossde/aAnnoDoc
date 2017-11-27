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
 * for information plantuml sequenz diagrams see <http://plantuml.com/sequence-diagram>
 * 
 */
@aDoc(title="diagram/sequenz diagram")
public class SequenzDiagramWriter {

	public static final String plutgin="plantuml";
	
	protected AsciiDocWriter wr;
	
	public SequenzDiagramWriter(AsciiDocWriter wr)  throws IOException { this(wr,null); }
	public SequenzDiagramWriter(AsciiDocWriter wr,String title)  throws IOException { 
		this.wr=wr;
		if(!wr.e(title)) {start(title); }
	}
	
	//--------------------------------------------------------------------
	// UML  sequenz diagram
	
	/** start sequenz diagram with title of it **/
	public SequenzDiagramWriter start(String name) throws IOException {
		wr.nnl2().w('[').w(plutgin).w(',').w(name).w(']').nl();
		wr.w("----").nl();
		return this;
	}
	
	/** define sequenz point of type [actor,boundary,control,entity,database] **/
	public SequenzDiagramWriter type(String name,String type,String color) throws IOException {
		wr.nnl().w(type).w(' ').w(name);
		if(!wr.e(color)) { wr.w(' ').w(color); }
		wr.nl(); return this;
	}
	
	public SequenzDiagramWriter autonumber(String nrInfo) throws IOException {
		wr.nnl().w("autonumber"); if(!wr.e(nrInfo)) { wr.w(' ').w(nrInfo); } return this;
	}
	
	
	/** 
	 * @param title - title of sequenz
	 * @param style - arrow style [->X,->,->>,-\,\\-,\\--,->o,o\\--,<->,<->o]
	 * @param from - left hand
	 * @param to - right hand
	 */
	public SequenzDiagramWriter sequenz(String title,String from,String style,String to)  throws IOException {
		wr.w(from);
		if(wr.e(style)) { wr.w(" -> "); } else { wr.w(' ').w(style).w(' '); }
		wr.w(to).w(": ").w(title).nl();
		return this;
	}
	
	public SequenzDiagramWriter to(String title,String from,String to)  throws IOException { return sequenz(title, from, "->",to);}
	public SequenzDiagramWriter to(String title,String from,String to,String color)  throws IOException { return sequenz(title, from, "-"+color+">",to);}
	public SequenzDiagramWriter from(String title,String from,String to)  throws IOException { return sequenz(title, from, "<-",to);}
	public SequenzDiagramWriter from(String title,String from,String to,String color)  throws IOException { return sequenz(title, from, "<"+color+"-",to);}

	//		wr.w(from).w(" -> ").w(to).w(": ").w(title).nl();
//		return this;
//	}
//	public AsciiDiagramWriter sequenzFrom(String title,String from,String to)  throws IOException {
//		wr.w(from).w(" <- ").w(to).w(": ").w(title).nl();
//		return this;
//	}

	
	/** start sequenz diagram **/
	public SequenzDiagramWriter end()  throws IOException {
		wr.nnl().w("----").nl();
		return this;
		
	}

}
