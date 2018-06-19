package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a ToDo/FixMe
 *  
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aToDo")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aToDo {
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
	
	/** internal group - for java below 1.7 **/
	public  @interface aToDos { aToDo[] value() default {}; }
}