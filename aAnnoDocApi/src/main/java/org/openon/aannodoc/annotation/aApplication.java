package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Application documentation annotation - to document a application
 * 
 * Per Application there have to be only ONE \@aApplication Annotation
 * to define identify and define the application at one place 
 * 
 * 		\/**
 *   	  * This ist the first application
 *   	  *
 *   	  **\/
 * 		\@aApplication(title="MyFirstApplication");
 * 		public class myClass {...}
 * 
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aApplication(title="generator/AppDoc/aApplication")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aApplication {
	
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
	
	/** copyright information **/
	String copyright() default "";
	
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
	/** description belongs to version **/
	String version() default ""; 
	/** description is no longer correct **/
	String deprecated() default ""; 
	
	/** versions of application **/
	aVersion[] versions() default {};
	
}
