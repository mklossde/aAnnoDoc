package org.openon.aannodoc.maven;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;

/**
 * 
 * mavin-plugin to add AnnoDoc into maven-build-process
 * 
 * 
 * Add Maven-plugin to pom.xml
 * 
 * 
 *   <build>
 *  		<plugins>
 * 			<plugin> 
 *			  <groupId>org.openon</groupId>
 *			  <artifactId>onAnnoDocMavenPlugin</artifactId>
 *			  <version>1.0.0</version>
 *			  <executions>
 *			    <execution>
 *			      <phase>install</phase>
 *			      <goals>
 *			        <goal>annodoc</goal>
 *			      </goals>
 * 			      <configuration> 			      	
 * 			      	<generator>.....</generator>
 * 			      	<source>....</source>
 * 			      	<format>....</format>      
 *			      </configuration> 
 *			    </execution>
 *			  </executions>
 *			</plugin>
 *		</plugins>
 *	</build>
 *	
 *
 *
 *
GRAPHVIZ SETUP:
	GRAPHVIZ_HOME	c:\Software\graphviz
	PATH	[…​];%GRAPHVIZ_HOME%\bin
 

GRAPHVIZ MAVEN
				<configuration>
					<sourceDirectory>src/main/asciidoc</sourceDirectory>
					<requires>
						<require>asciidoctor-diagram</require>
					</requires>
					<attributes>
						<graphvizdot>${project.build.directory}/../graphviz/bin/dot.exe</graphvizdot>
					</attributes>
				</configuration>
				
 * @author Michael
 *
 */
@Mojo(name = "annodoc")
public class AnnoDocMojo extends AbstractMojo {
	
//	@Parameter(defaultValue = "${project}", readonly = true, required = true)
//	private MavenProject project;
	  
	@Parameter(property = "annodoc.source", defaultValue = "src")
	protected String source;
	
	@Parameter(property = "annodoc.outputFile", defaultValue = "doc/AnnoDocOutput")
	protected String outputFile;
	
	@Parameter(property = "annodoc.outputSource", defaultValue = "doc/AnnoSourceOutput")
	protected String outputSource;
	
	@Parameter(property = "annodoc.format", defaultValue = "pdf")
	protected String format;
	
	@Parameter(property = "annodoc.generator", defaultValue = aAnnoDoc.GENERATOR_ANNOTATIONS)
	protected String generator;
	
	@Parameter(property = "annodoc.sourceCharset", defaultValue = Options.CHARTSET_UTF8)
	protected String sourceCharset;
	
	/**
	 * plugin for maven - run via mvn in cmd 
	 * 	
	 * 		org.openon:onAnnoDocMavenPlugin:0.0.1:AnnoDoc
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String user = System.getProperty("user.name");
		
//System.out.println("pluginContext:"+getPluginContext());
		
		try {
			aAnnoDoc doc=new aAnnoDoc();
			String javaSourceFileOrDirectory=source;		
			Options options=new Options(javaSourceFileOrDirectory,outputFile,generator, format);
			if(outputSource!=null && outputSource.length()>0) { options.put(Options.OPTION_OUTSOURCE, outputSource);}
			if(sourceCharset!=null && sourceCharset.length()>0) { options.put(Options.OPTION_SOURCE_CHARTSET, sourceCharset);}
			
			getLog().info("source: " + source);
			getLog().info("sourceChartset: " + sourceCharset);
			getLog().info("outputFile: " + outputFile);
			getLog().info("outputSource: " + outputSource);
			getLog().info("format: " + format);
			getLog().info("generator: " +generator);
					
			doc.scan(options).create(options);
			
		}catch(IOException e) {
			getLog().error(e.getMessage(),e); 
			throw new MojoExecutionException(e.getMessage(),e);
		}
	}
	

}
