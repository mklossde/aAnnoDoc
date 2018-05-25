package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document example or test
 * 	e.g. a example which is included 
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aExample")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aExample {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";

	String reference() default "";
	
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
	
	/** internal group - for java below 1.7 **/
	public  @interface aExamples { aExample[] value() default {}; }
}