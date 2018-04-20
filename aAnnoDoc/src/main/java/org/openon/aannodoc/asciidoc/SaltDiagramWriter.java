package org.openon.aannodoc.asciidoc;

import java.io.IOException;

import org.openon.aannodoc.annotation.aDoc;

/**
 * Design graphical interface
 * 
 * @author mk@almi.de
 *
 */
@aDoc(title="diagram/salt diagram")
public class SaltDiagramWriter {

	public static final String plutgin="plantuml";
	
	protected AsciiDocWriter wr;
	
	public SaltDiagramWriter(AsciiDocWriter wr)  throws IOException { this(wr,null); }
	public SaltDiagramWriter(AsciiDocWriter wr,String title)  throws IOException { 
		this.wr=wr;
		if(!wr.e(title)) {start(title); }
	}
	
	//--------------------------------------------------------------------
	// UML  sequenz diagram
	
	/** start sequenz diagram with title of it **/
	public SaltDiagramWriter start(String name) throws IOException {
		wr.nnl2().w('[').w(plutgin).w(',').w(name).w(']').nl();
		wr.w("----").nl();
		return this;
	}
}
