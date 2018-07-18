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
	
	public void test() throws IOException {
		wr.wnl("class BlockProcessor");
		wr.wnl("Class01 <|-- Class02");
		
	}
	
}
