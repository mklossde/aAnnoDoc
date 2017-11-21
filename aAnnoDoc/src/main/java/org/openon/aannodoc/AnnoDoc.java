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

/**
 * AnnoDoc - Java annotation based documentation
 * (Apache License 2.0)
 * 
 * Normaly documenation and devleopment are two seperated parts
 * and it is time and coast intensive to write one after each other. 
 * 
 * A good idea is two sync the development and documenation process,
 * by put the information near by the source code. 
 * 
 * This is the base idea of AnnoDoc-Project to add application documenation
 * (like dsciption, bugs, features, examples, atrriubtes,...) to (or near to) 
 * the source code and generate the documenation during 
 * compile-process (or maven deploy process). 
 * 
 * IT IS NOT ANOTHER JAVA-API-DOCUMENATION ! 
 * The idea is write application information (like Manuals, installtion-guids, ReadMes, AsiccDoc,.. )  
 * inside the project. 
 * 
 * The document-generation is splitted into two parts
 * - scanner => SourceCode or JAR scanner/reader which read all the project informations 
 * - generator => generates the structure/documents into a readable format (like AsciiDoc,PDF,HTML,.. )  
 * 
 * The important part are the gernerator, which are individual programmable to 
 * generate individual documents. There are some base generators included
 * to create a Application-Manual, a Annotation-Overview,... 
 * 
 * To work with AnnoDoc use one of this ways
 * - add the aAnnoDocMavenPlugin to generate documents during deployment
 * - call org.openon.aannodoc.AnnoDoc from console
 * - program a AnnoDoc Process manual 
 * 
 * 
 * 
 * 
 * @author Michael Kloss- mk@almi.de
 * @version 0.0.1 - 21.11.2017
 * 
 */
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
