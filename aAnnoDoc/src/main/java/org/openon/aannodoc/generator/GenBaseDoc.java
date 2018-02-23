package org.openon.aannodoc.generator;

import java.io.IOException;

import org.openon.aannodoc.asciidoc.AsciiDocWriter;

public class GenBaseDoc extends AsciiDocGeneratorImpl implements DocGenerator {

	public AsciiDocWriter writer() { return this.w; }
	
	@Override public void head(String outputName) throws IOException {		
	}

	@Override public void body(String outputName) throws IOException {	
	}

	@Override public void bottom(String outputName) throws IOException {
	}
	

}
