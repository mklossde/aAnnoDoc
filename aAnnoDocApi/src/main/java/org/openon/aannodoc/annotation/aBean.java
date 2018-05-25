package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document any data-container like fields/inputs/values/beans/objects of application
 * 	e.g. a field is recevied - computed - and send to backend
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aObject")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aBean {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";

	
	/** input information about this bean **/
	String input() default "";
	/** output information about this bean **/
	String output() default "";
	/** compute information about this bean **/
	String compute() default "";
	
	/** document hte validation of this bean **/
	String validation() default "";
	
	/** define the layer of this bean **/
	String layer() default "";
	
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
	public  @interface aBeans { aBean[] value() default {}; }
	
}
