package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document fields/inputs/values/beans of applciation
 * 	e.g. a field is recevied - computed - and send to backend
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="annotation/aField")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aField {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";

	/** bean or group this field belongs to **/
	String bean() default "";
	/** input information about this field **/
	String input() default "";
	/** output information about this field **/
	String output() default "";
	/** compute information about this field **/
	String compute() default "";
	
	/** document hte validation of this field **/
	String validation() default "";
	
	/** define the layer of this field **/
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
	
}
