package org.openon.aannodoc;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.generator.AnnotationAppDoc;
import org.openon.aannodoc.generator.AnnotationListDoc;
import org.openon.aannodoc.generator.DocGenerator;
import org.openon.aannodoc.generator.JavaDoc;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnoDoc {
	private static final Logger LOG=LoggerFactory.getLogger(AnnoDoc.class);
	
	public static final String GENERATOR="generator";
	public static final String FORMAT="format";
	public static final String OUTPUT="output";
	
	public static final String ADOC="adoc";
	public static final String HTML="html";
	public static final String PDF="pdf";
	
	public static final String GEN_AAPP=AnnotationAppDoc.NAME;
	public static final String GEN_ALIST=AnnotationListDoc.NAME;
	public static final String GEN_JAVA=JavaDoc.NAME;
	
	/** option-output==stdout => write output to standard-out (console) **/
	public static final String OUT_STDOUT="stdout";
	
//	protected Map options;
	
	public static void main(String[] args) throws IOException {
		String source=args[0];
		Map options=new HashMap();
		if(args.length>1) { options.put(FORMAT, args[1]); }
		if(args.length>2) { options.put(GENERATOR, args[2]); }
		if(args.length>3) { options.put(OUTPUT, args[3]); }
		
		new AnnoDoc().create(source,options);
		
		System.out.println("end");
	}
	
	public AnnoDoc() {}
	
	
	public void create(String javaSourceFileOrDirectory,Map options) throws IOException {		
		SourceAnnotations anno=new SourceAnnotations(javaSourceFileOrDirectory);
		
		LOG.info("create AnnoDoc (working dir:{})",Paths.get("").toAbsolutePath());
		
		DocGenerator generator=getGenerator(anno,options);
		generator.init(anno, options);
		generator.create();
		generator.output();
		generator.close();
	}
	
	/** get doc generator **/
	public DocGenerator getGenerator(SourceAnnotations anno,Map options) throws IOException {
		try{
			Object generator=options.get(GENERATOR);		
			if(generator instanceof Class) { return (DocGenerator)((Class)generator).newInstance();  }
			else if(generator instanceof DocGenerator) { return (DocGenerator)generator; }
			else if(generator==null) { return new AnnotationListDoc(); }
			else if(generator instanceof String) {
				String gen=(String)generator;
				if(gen.length()==0|| gen.equalsIgnoreCase(AnnotationListDoc.NAME)) { return new AnnotationListDoc();
				}else if(gen.equalsIgnoreCase(AnnotationAppDoc.NAME)) { return new AnnotationAppDoc();
				}else if(gen.equalsIgnoreCase(JavaDoc.NAME)) { return new JavaDoc();
				}else {
					Class cl=Class.forName(gen);
					 return (DocGenerator)((Class)generator).newInstance(); 
				}
			}
			throw new IOException("unokwn generator "+generator);
		}catch(IOException e) { throw e;
		}catch(Exception e) { throw new IOException(e); }
	}

	//-----------------------------------------------------------------
	
	/** get options for attribtues **/
	public static Map<String,Object> toOptions(Object outputFile,String format,Object generator) {
		Map<String,Object>  options=new HashMap<String, Object>();
		options.put(AnnoDoc.FORMAT,format);
		options.put(AnnoDoc.OUTPUT,outputFile);
		options.put(AnnoDoc.GENERATOR,generator);
		return options;
	}
}
