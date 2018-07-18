package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a annotations and its feature
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aAnnotation(title="aAnnotation")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aAnnotation {
	/** group this documenation belongs to **/
	String group() default "";
	/** name of attribute **/
	String title() default "";
	 	
	/** is this attribute options **/
	String relevant() default "";
		
	/** description (alternative to java-comment) **/
	String description() default "";
	
	/** description is no longer correct **/
	String deprecated() default ""; 
	
	/** internal group - for java below 1.7 **/
	public  @interface aAttributes { aAnnotation[] value() default {}; }
}
