package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Basic documentation annotation - to document anything by title 
 * 	e.g. a how-to-start, application-description  
 * 
 * 		\/**
 *   	  * This application is working hard on its result by..
 *   	  *
 *   	  **\/
 * 		\@aDoc(title="ApplicationResult");
 * 		public class myClass {...}
 * 
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aDoc")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aDoc {
	public static final String fFILE="file";
	public static final String fGROUP="group";
	public static final String fTITLE="title";
	public static final String fDESCIPTION="description";
	public static final String fSIMPLE="simple";
	public static final String fAUTHOR="author";
	public static final String fDATE="date";
	public static final String fVERSION="version";
	public static final String fDEPRECATED="deprecated";
	
	
	/** group this documenation belongs to **/
	String group() default "";	
	/** 
	 * title or subTitle for documentation
	 * possible to construct structured by a path with / (e.g. "orders/Shopping Card") 
	 **/
	@aAttribute
	String title() default "DEFAULT";
	
	/** description (alternative to java-comment) **/
	String description() default "";
	/** simple or short information **/
	String simple() default "";
	
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
	/** description belongs to version **/
	String version() default ""; 
	/** description is no longer correct **/
	String deprecated() default ""; 
}
