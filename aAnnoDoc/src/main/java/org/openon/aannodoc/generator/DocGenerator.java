package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.Map;

import org.openon.aannodoc.scanner.SourceAnnotations;

public interface DocGenerator {

	/** init generator with attributes **/
	public void init(SourceAnnotations adoc,Map<String,Object> options);
	
	/** start generator **/
	public void generate() throws IOException;
	
	
	//------------------------------------------------------------------------
	// generation steps
	
	/** create document and structure **/
	public void create(String outputName) throws IOException;
	
	/** output doucment to file of spezific format **/
	public void output(String outputName) throws IOException;
	
	/** close acturl gernatioon **/
	public void close(String outputName);
}
