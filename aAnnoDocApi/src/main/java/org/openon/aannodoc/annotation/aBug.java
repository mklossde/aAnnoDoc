package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a bug and/or fix of a problem 
 * e.g. a attribute of a method or function
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aBug")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aBug {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";
	
	/** Critical level of bug - value examples are low,medium,high **/
	String level() default "";
	
	/** description (alternative to java-comment) **/
	String description() default "";
	
	/** informaiton about a todo **/
	String todo() default "";
	/** informaiton about the fix of this bug **/
	String fix() default "";
	
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
}