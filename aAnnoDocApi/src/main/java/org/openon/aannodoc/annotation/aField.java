package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a field/object
 * e.g. a attribute of a method or function
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aAttribute")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aField {
	/** group this documenation belongs to **/
	String group() default "";
	/** name of attribute **/
	String title() default "";
	 
	/** default value of attribtue **/
	String value() default "";
	
	/** is this attribute options **/
	String optional() default "";
		
	/** options of attribtues **/
	String[] options() default {};
	
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
	
	/** description is no longer correct **/
	String deprecated() default ""; 
}
