package org.openon.aannodoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.generator.GenAppDoc;
import org.openon.aannodoc.generator.GenAllAnno;
import org.openon.aannodoc.generator.DocGenerator;
import org.openon.aannodoc.generator.GenJavaDoc;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.utils.DocFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * aAnnoDoc - (Java) annotation based documentation
 * (Apache License 2.0)
 * 
 * DONATE :-) to https://www.paypal.me/openonorg/5
 * (If you like aAnnoDoc, donates are welcome via paypal to openonorg. Every $1 is fine ;-)
 * 
 * Background Story, why aAnnoDoc
 * Normal, documentation and development are two separated parts
 * and it is time and coast intensive to write one after each other. 
 * A good idea is two sync the development and documentation process,
 * by put the information near by the source code. 
 * 
 * This is the base idea of AnnoDoc-Project to add application documentation
 * (like deception, bugs, features, examples, attributes,...) to (or near to) 
 * the source code and generate the documentation during compile-process (or maven deploy process). 
 * Write application information (like Manuals, installtion-guids, ReadMes, AsiccDoc,.. ) inside each project. 
 * 
 * IT IS NOT ANOTHER JAVA-API-DOCUMENATION ! Its a application-documenation.
 * 
 * The documentation is flexible by use (or development) different generators.  
 * For example the "AppDoc"-generator create a docs for predefined Annotations. 
 * Other generators are "ProjectDoc" for quick project-documents, "RestDoc" 
 * for Java-rest-service documents, "AnnoInfo" for Annotation-Overview. 
 * 
 * To work with AnnoDoc use one of this ways
 * - add the aAnnoDocMavenPlugin to generate documents during maven deployment
 * - call org.openon.aannodoc.AnnoDoc from console with options
 * - program a AnnoDoc Process method 
 * 
 * For more information or examples see directory docs/
 * 
 * Have fun... 
 * 
 * 
 *  
 * Simple Example of a aDoc documentation:
 *  
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
 * 
 * @author Michael Kloss- mk@almi.de
 * @version 1.0.0 - 03.12.2017
 * 
 */
@aDoc(file="READ.md")
public class aAnnoDoc {
	private static final Logger LOG=LoggerFactory.getLogger(aAnnoDoc.class);
	

	
	// Output-Formats
	public static final String FORMAT_ASCIIDOC="adoc";
	public static final String FORMAT_HTML="html";
	public static final String FORMAT_PDF="pdf";	
	
	/** generator for aDoc-documents - create document for aDoc-Annotations**/
	public static final String GENERATOR_ADOC=GenAppDoc.NAME;
	/** generator for annotation-documents - list all annotations and its comments **/
	public static final String GENERATOR_ANNOTATIONS=GenAllAnno.NAME;
	/** generator for javaDoc like document **/
	public static final String GENERATOR_JAVADOC=GenJavaDoc.NAME;
	
	/** option-output==stdout = write output to standard-out (console) **/
	public static final String OUT_STDOUT="stdout";
	
//	public static final String DEFAULT_FILE="default";
	
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
		Options options=new Options(args);
		aAnnoDoc annoDoc=new aAnnoDoc(options);		
		
		System.out.println("end");
	}
	
	/** instance a AnnoDoc **/
	public aAnnoDoc() {}
	
	//-------------------------------------------------------------------------------------------------
	
	/** create docuemntation with attribtues **/
	public aAnnoDoc(String source,String outputFile,String generator,String format) throws IOException {
		Options options=new Options(source,outputFile, generator, format);
		scan(options).create(options);
	}
	/** create documenation with options **/
	public aAnnoDoc(Options options) throws IOException { scan(options).create(options); }
		 
	/** create documenation by options from propertie-file **/
	public aAnnoDoc(String propertieFile) throws IOException { 
		Options options=new Options(propertieFile);
		scan(options).create(options); 
	}

	
	//-------------------------------------------------------------------------------------------------
	

	/**
	 * The document-generation is splitted into two parts
	 * - scanner: SourceCode or JAR scanner/reader which read all the project informations 
	 * - generator: generates the structure/documents into a readable format (like AsciiDoc,PDF,HTML,.. )
	 * 
	 *  The first step is scanning and read all source/packages/files/.. which includes the documenation
	 *   
	 */
	@aDoc(title="scan")
	/** first step - scan soruce **/
	public aAnnoDoc scan(Options options) throws IOException {		
		return scan((String)options.get(Options.OPTION_SOURCE),options.getFilter());
	}
	
//	public aAnnoDoc scan(String javaSourceFileOrDirectory,Map<String,Object> options) throws IOException {
//		return scan(javaSourceFileOrDirectory,getFilter(options));
//	}
	
	/** first step - scan soruce **/
	public aAnnoDoc scan(String javaSourceFileOrDirectory,DocFilter filter) throws IOException {		
		this.anno=new SourceAnnotations(javaSourceFileOrDirectory,filter);
		return this;
	}
	
	/**
	 * The important part are the gernerator, which are individual programmable to 
	 * generate individual documents. There are some base generators included
	 * to create a Application-Manual, a Annotation-Overview,... 
	 */
	@aDoc(title="Gernator")
	/** second step - create documenation **/
	public aAnnoDoc create(Options options) throws IOException {	
//		String outDir=(String)options.get(Options.OPTION_OUT_DIR);
//		String workDir=changeWorkDir(outDir);
		LOG.info("create AnnoDoc (working dir:{})",System.getProperty("user.dir"));	

		DocGenerator generator=getGenerator(anno,options);
		generator.init(anno, options);
		generator.generate();
		
//		if(workDir!=null && workDir.length()>0) { System.setProperty("user.dir",workDir); }
		return this;
	}
		
	/**
	 * get doc generator
	 * 
	 * @param anno
	 * @param options
	 * @return
	 * @throws IOException
	 */
	protected DocGenerator getGenerator(SourceAnnotations anno,Options options) throws IOException {
		try{
			Object generator=options.get(Options.OPTION_GENERATOR);		
			if(generator instanceof Class) { return (DocGenerator)((Class)generator).newInstance();  }
			else if(generator instanceof DocGenerator) { return (DocGenerator)generator; }
			else if(generator==null) { return new GenAllAnno(); }
			else if(generator instanceof String) {
				String gen=(String)generator;
				if(gen.length()==0|| gen.equalsIgnoreCase(GenAllAnno.NAME)) { return new GenAllAnno();
				}else if(gen.equalsIgnoreCase(GenAppDoc.NAME)) { return new GenAppDoc();
				}else if(gen.equalsIgnoreCase(GenJavaDoc.NAME)) { return new GenJavaDoc();
				}else {
					Class cl=Class.forName(gen);
					return (DocGenerator)cl.newInstance(); 
				}
			}
			throw new IOException("unokwn generator "+generator);
		}catch(IOException e) { throw e;
		}catch(Exception e) { throw new IOException(e); }
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
		Options options=new Options(source,output,GENERATOR_ADOC,format);
		new aAnnoDoc(options);		
	}
	
	/**
	 * Create aDoc-documenation Files in format (for alrady scanned soruces) 
	 * @param format - output-format
	 */
	@aFeature(title="execute/manuel/DocFiles")
	public aAnnoDoc createDocFiles(String useDocFile,String output,String format) throws IOException  {
		Options options=new Options(null,output,GENERATOR_ADOC,format);	
		if(useDocFile!=null) { options.put(Options.OPTION_DOCFILE, useDocFile); }
		return create(options);
	}


	
}
