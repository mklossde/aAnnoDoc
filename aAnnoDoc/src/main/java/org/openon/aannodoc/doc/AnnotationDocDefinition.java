package org.openon.aannodoc.doc;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.JarDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationDocDefinition {

public static final String DOC_EXAMPLE="example";
	

	
	public static final String DOC_DOC="aDoc";
	
	/**
	 * \@see(src=NAVIGATE) "LABEL" 
	 * include link with label to a navigation-reference
	 * 
	 * Simple "@see label" => display a link to label named label  
	 * 
	 **/
	@aDoc(name="Annotation/Annotation see")
	public static final String DOC_SEE="see";
	/** include doc from reference **/
	public static final String DOC_INCLUDE="include";
	
	/** 
	 * \@code(src=FILE) CODE @
	 * Add code example with a src-name.
	 * 
	 * The \@code Annotation MUST end with a " \@ ". All code inside this will display as code (annotation inside are not interpreted)
	 * 
	 **/
	@aDoc(name="Annotation/Annotation code")
	public static final String DOC_CODE="code";
	
	/**
	 * <HTML>
	 * Display as html unitl ending > 
	 */
	@aDoc(name="Annotation/Annotation html")
	public static final String DOC_HTML="<";
	
	/** example **/
	public static final String DOC_SAMPLE="sample";
	
	/** author of source **/
	public static final String DOC_AUTHOR="author";
	/** version **/
	public static final String DOC_VERSION="version";
	
	/** todo **/
	public static final String DOC_TODO="todo";
	
	/** doc annotation label **/
	public static final String DOC_LABEL="label";
	
	public static final String DOCTYPE_SEE="anno";
	
//	public static final String DOC_HTML="#";
	
}
