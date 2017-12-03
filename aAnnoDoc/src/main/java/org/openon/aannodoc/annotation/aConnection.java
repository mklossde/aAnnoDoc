package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a connection/interface to a differnt system/network  
 * 	e.g. a database-connection, Remote-Rest-Service,  
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aConnection")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aConnection {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";
	
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