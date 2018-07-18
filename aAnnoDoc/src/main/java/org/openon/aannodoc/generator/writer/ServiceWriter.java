package org.openon.aannodoc.generator.writer;

import java.io.IOException;
import java.util.List;

import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.asciidoc.diagram.SequenceDiagramWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;

/**
 * Writer of Service informations
 * 
 *
 */
public class ServiceWriter {

	protected SourceAnnotations annotations;	
	protected AsciiDocWriter w;	
	
	public ServiceWriter(AsciiDocWriter w,SourceAnnotations adoc) throws IOException {
		this.w=w;
		this.annotations=adoc; 				
	}
	

	public void writeSequenz(List<AnnotationDoc> docs) throws IOException {
		SequenceDiagramWriter dia=new SequenceDiagramWriter(w,"Services");
		
		for(int i=0;docs!=null && i<docs.size();i++) {
//			w.text("d:"+docs.get(i));
			AnnotationDoc doc=docs.get(i);			
			String name=AnnoUtils.getTitle(doc);
			dia.to(name, "WebService","Host");
		}
		
		dia.end();
	}
}
