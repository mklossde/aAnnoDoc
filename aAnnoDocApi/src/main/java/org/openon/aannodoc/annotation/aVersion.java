package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document version or change of program 
 * e.g. a attribute of a method or function
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aBug")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aVersion {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";
	
	/** version this change describe **/
	String version() default "";
	
	/** description (alternative to java-comment) **/
	String description() default "";
		
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
	
	/** description is no longer correct **/
	String deprecated() default ""; 
}