package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a change of a problem 
 * 
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aChange")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aChange {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";
		
	/** description (alternative to java-comment) **/
	String description() default "";
	
	/** informaiton about a todo **/
	String todo() default "";

	
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
	
	/** internal group - for java below 1.7 **/
	public  @interface aChanges { aChange[] value() default {}; }
}