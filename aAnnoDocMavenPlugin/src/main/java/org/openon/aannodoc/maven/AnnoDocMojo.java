package org.openon.aannodoc.maven;

import java.io.IOException;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
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
 * @author Michael
 *
 */
@Mojo(name = "annodoc")
public class AnnoDocMojo extends AbstractMojo {
	

//	@Parameter(property = "annodoc.docDirectory", defaultValue = "..\\generated-docs")
//	private String docDirectory;
	
	@Parameter(property = "annodoc.source", defaultValue = "src")
	private String source;
	
	@Parameter(property = "annodoc.outputFile", defaultValue = "doc/AnnoDocOutput")
	private String outputFile;
	
	@Parameter(property = "annodoc.format", defaultValue = "pdf")
	private String format;
	
	@Parameter(property = "annodoc.generator", defaultValue = aAnnoDoc.GENERATOR_ANNOTATIONS)
	private String generator;
	
	/**
	 * plugin for maven - run via mvn in cmd 
	 * 	
	 * 		org.openon:onAnnoDocMavenPlugin:0.0.1:AnnoDoc
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String user = System.getProperty("user.name");
		
		try {
			aAnnoDoc doc=new aAnnoDoc();
			String javaSourceFileOrDirectory=source;		
			Map<String, Object> options=doc.toOptions(outputFile, format, generator);
					
			getLog().info("source: " + source);
			getLog().info("outputFile: " + outputFile);
			getLog().info("format: " + format);
			getLog().info("generator: " +generator);
		
			doc.create(javaSourceFileOrDirectory, options);
			
		}catch(IOException e) {
			getLog().error(e.getMessage(),e); 
			throw new MojoExecutionException(e.getMessage(),e);
		}
	}
	

}
