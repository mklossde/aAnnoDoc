package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.Map;

import org.openon.aannodoc.scanner.SourceAnnotations;

public interface DocGenerator {

	/** init generator with attributes **/
	public void init(SourceAnnotations adoc,Map<String,Object> options);
	
	/** create document and structure **/
	public void create() throws IOException;
	
	/** output doucment to file of spezific format **/
	public void output() throws IOException;
	
	/** close generator **/
	public void close();
}
