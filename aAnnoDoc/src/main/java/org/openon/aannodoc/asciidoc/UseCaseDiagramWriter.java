package org.openon.aannodoc.asciidoc;

import java.io.IOException;

import org.openon.aannodoc.annotation.aDoc;

/**
 * Create a use-case diagram, within generator, with UseCaseDiagramWriter
 * 
 * 
 * for information plantuml use-case-diagrams see <http://plantuml.com/use-case-diagram>
 * 
 */
@aDoc(title="diagram/use case diagram")
public class UseCaseDiagramWriter {

	public static final String plutgin="plantuml";
	
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

	public UseCaseDiagramWriter usecase(String title)  throws IOException {
		wr.nnl().w('(').w(title).w(')').nl();
		return this;			
	}


	public UseCaseDiagramWriter actor(String title)  throws IOException {
		wr.nnl().w(':').w(title).w(':').nl();
		return this;			
	}	
	
}
