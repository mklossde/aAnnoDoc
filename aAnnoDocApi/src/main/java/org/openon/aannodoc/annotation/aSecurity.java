package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a security implementation
 * 	e.g. a rest service inside this application
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aService")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aSecurity {
	/** group this documenation belongs to **/
	String group() default "";	
	
	/** title or subTitle for documentation **/
	String title() default "";
	
	/** description (alternative to java-comment) **/
	String description() default "";
	
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
	/** description belongs to version **/
	String version() default "";
	
	/** description is no longer correct **/
	String deprecated() default ""; 
	
	/** internal group - for java below 1.7 **/
	public  @interface aSecuritys { aSecurity[] value() default {}; }
}