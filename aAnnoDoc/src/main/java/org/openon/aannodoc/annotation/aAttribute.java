package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a attribute 
 * e.g. a attribute of a method or function
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="annotation/aAttribute")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aAttribute {
	/** file for comment will by inserted **/
	String file() default "";
	/** group this documenation belongs to **/
	String group() default "";
	/** name of attribute **/
	String title() default "";
	
	/** default value of attribtue **/
	String defaultValue() default "";
	
	/** is this attribute options **/
	String optional() default "";
		
	/** options of attribtues **/
	String[] options() default {};
	
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
