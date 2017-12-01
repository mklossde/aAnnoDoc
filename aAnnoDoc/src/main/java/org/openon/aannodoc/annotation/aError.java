package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document error-handling  
 * 	e.g. what's happening when a timeout occurs   
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="annotation/aError")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aError {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";
	
	/** error will occure when **/
	String when() default "";
	
	/** description (alternative to java-comment) **/
	String description() default "";
	
}
