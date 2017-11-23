package org.openon.aannodoc;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.generator.AnnotationAppDoc;
import org.openon.aannodoc.generator.AnnotationListDoc;
import org.openon.aannodoc.generator.DocGenerator;
import org.openon.aannodoc.generator.JavaDoc;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * aAnnoDoc - Java annotation based documentation
 * (Apache License 2.0)
 * 
 * DONATE :-) to https://www.paypal.me/openonorg/5
 * (If you like aAnnoDoc please donate via paypal to openonorg. Every $1-$nnn is welcome. )
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
 * - scanner: SourceCode or JAR scanner/reader which read all the project informations 
 * - generator: generates the structure/documents into a readable format (like AsciiDoc,PDF,HTML,.. )  
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
 * The Scanner scans all java-sources and use comments for documenation. 
 * Simple: 
 * 		- The comment above belong to the annotation/method below. 
 * 		- Test/comment-annotations belong to the text right until next \@ 
 * 		- Inline-comments are ignored !!! 
 * 
 * 		\/** this is the AnnoDoc documentation of aDoc **\/
 * 		\@aDoc
 * 		\/** this is api-documenation of myFunction **\/
 * 		public void myFunction(String arg) {
 * 			// this inline code will be ignores
 * 			\/** this is the annoDoc-documentaiton of aFiled **\/
 * 			\@aFiled
 * 			\/** this is the api-documentaiton of myVar **\/
 * 			String myVar;
 * 		}
 * 		\/**
 * 		  * This is the myReturn api-documenation (which include comment-annotations) 
 * 		  *	\@Bug - this is the annoDoc documentation of aBug (until next \@) 
 * 		  **\/
 * 		public void myReturn(String arg) { }
 * 
 * 
 * @author Michael Kloss- mk@almi.de
 * @version 0.0.1 - 21.11.2017
 * 
 */
@aDoc(file="READ.md")
public class aAnnoDoc {
	private static final Logger LOG=LoggerFactory.getLogger(aAnnoDoc.class);
	
	@aAttribute()
	public static final String OPTION_SOURCE="soruce";
	public static final String OPTION_GENERATOR="generator";
	public static final String OPTION_FORMAT="format";
	public static final String OPTION_OUTPUT="output";
	
	public static final String OPTION_OUT_ADOC="outadoc";
	public static final String OPTION_OUTFILE_PREFIX="outprefix";
	/** write only docFile - contains file="xxx" in annotation-attribute **/
	public static final String OPTION_DOCFILE="docfile";
	
	// Output-Formats
	public static final String FORMAT_ASCIIDOC="adoc";
	public static final String FORMAT_HTML="html";
	public static final String FORMAT_PDF="pdf";	
	
	/** generator for aDoc-documents - create document for aDoc-Annotations**/
	public static final String GENERATOR_ADOC=AnnotationAppDoc.NAME;
	/** generator for annotation-documents - list all annotations and its comments **/
	public static final String GENERATOR_ANNOTATIONS=AnnotationListDoc.NAME;
	/** generator for javaDoc like document **/
	public static final String GENERATOR_JAVADOC=JavaDoc.NAME;
	
	/** option-output==stdout = write output to standard-out (console) **/
	public static final String OUT_STDOUT="stdout";
	
	public static final String DEFAULT_FILE="default";
	
	protected SourceAnnotations anno;

	/**
	 * Execute aAnnoDoc with command-line attribtue
	 * 		java -jar aAnnoDoc.jar ATTRIBUTES
	 * 
	 * ATTRIBUTES: OPTION_SOURCE, OPTION_FORMAT, OPTION_GENERATOR, OPTION_FORMAT
	 * 
	 */
	@aFeature(title="execute/command-line")
	public static void main(String[] args) throws IOException {
		Map options=new HashMap();
		if(args.length>0) { options.put(OPTION_SOURCE, args[0]); }
		if(args.length>1) { options.put(OPTION_OUTPUT, args[1]); }
		if(args.length>2) { options.put(OPTION_GENERATOR, args[2]); }
		if(args.length>3) { options.put(OPTION_FORMAT, args[3]); }
		
		new aAnnoDoc(options);		
		
		System.out.println("end");
	}
	
	/** instance a AnnoDoc **/
	public aAnnoDoc() {}
	
	//-------------------------------------------------------------------------------------------------
	
	/** create docuemntation with attribtues **/
	public aAnnoDoc(String source,String outputFile,String generator,String format) throws IOException {
		Map options=aAnnoDoc.toOptions(source,outputFile, generator, format);
		scan(options).create(options);
	}
	/** create documenation with options **/
	public aAnnoDoc(Map options) throws IOException { scan(options).create(options); }
		 
	//-------------------------------------------------------------------------------------------------
	
	/** first step - scan soruce **/
	public aAnnoDoc scan(Map options) throws IOException {
		String javaSourceFileOrDirectory=(String)options.get(OPTION_SOURCE);
		return scan(javaSourceFileOrDirectory);
	}
	
	/** first step - scan soruce **/
	public aAnnoDoc scan(String javaSourceFileOrDirectory) throws IOException {
		this.anno=new SourceAnnotations(javaSourceFileOrDirectory);
		return this;
	}
	
	/** second step - create documenation **/
	public aAnnoDoc create(Map options) throws IOException {		
		LOG.info("create AnnoDoc (working dir:{})",Paths.get("").toAbsolutePath());
		
		DocGenerator generator=getGenerator(anno,options);
		generator.init(anno, options);
		generator.generate();
		return this;
	}
	
//	/** map of generators **/
//	protected Map<String,Object> generatorMap=new HashMap<String,Object>();	
//	/** add generator to generator map **/
//	public void addGenerator(String generatorName,Object generator) { generatorMap.put(generatorName, generator);}
//	/** remove gerator from generator map **/
//	public Object removeGenerator(String generatorName) { return generatorMap.remove(generatorName); }
	
	/**
	 * get doc generator
	 * 
	 * @param anno
	 * @param options
	 * @return
	 * @throws IOException
	 */
	protected DocGenerator getGenerator(SourceAnnotations anno,Map options) throws IOException {
		try{
			Object generator=options.get(OPTION_GENERATOR);		
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
	
	/**
	 * @param outputFile
	 * @param format
	 * @param generator
	 * @return
	 */
	public static Map<String,Object> toOptions(String soruce,Object outputFile,Object generator,Object format) {
		Map<String,Object>  options=new HashMap<String, Object>();
		options.put(aAnnoDoc.OPTION_SOURCE,soruce);
		options.put(aAnnoDoc.OPTION_OUTPUT,outputFile);
		options.put(aAnnoDoc.OPTION_GENERATOR,generator);
		options.put(aAnnoDoc.OPTION_FORMAT,format);				
		return options;
	}
	
	//-----------------------------------------------------------------
	
	/**
	 * Create aDoc-documenation Files for given source in format
	 * 
	 * @param source - source-file or source-directory
	 * @param format - output-format
	 */
	@aFeature(title="execute/manuel/DocFiles")
	/** create documentation for all given files in source **/
	public static void DocFiles(String source,String output,String format) throws IOException  {
		Map options=aAnnoDoc.toOptions(source,output,GENERATOR_ADOC,format);
		new aAnnoDoc(options);		
	}
	
	/**
	 * Create aDoc-documenation Files in format (for alrady scanned soruces) 
	 * @param format - output-format
	 */
	@aFeature(title="execute/manuel/DocFiles")
	public aAnnoDoc createDocFiles(String useDocFile,String output,String format) throws IOException  {
		Map options=aAnnoDoc.toOptions(null,output,GENERATOR_ADOC,format);
options.put(aAnnoDoc.OPTION_OUT_ADOC, "true");		
		if(useDocFile!=null) { options.put(aAnnoDoc.OPTION_DOCFILE, useDocFile); }
		return create(options);
	}

}
